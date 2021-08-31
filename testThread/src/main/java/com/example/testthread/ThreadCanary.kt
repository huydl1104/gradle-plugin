package com.example.testthread

import android.annotation.SuppressLint
import android.content.Context
import com.example.testthread.model.ThreadPoolTrace
import com.example.testthread.model.ThreadTrace
import com.example.testthread.ui.ThreadCanaryActivity
import com.example.testthread.util.Notifications
import com.example.testthread.util.runOnUiThread
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


@SuppressLint("StaticFieldLeak")
internal object ThreadCanary {

  internal lateinit var singleThreadPool: ThreadPoolExecutor

  private val threadPoolTraces: HashMap<String, ThreadPoolTrace> = hashMapOf()

  private val threadTraces: HashMap<Long, ThreadTrace> = hashMapOf()

  private val threadChangedListeners: MutableList<OnThreadChangedListener> = mutableListOf()

  private lateinit var context: Context

  private val threadCanaryPrefs by lazy {
    context.getSharedPreferences("ThreadCanaryPrefs", Context.MODE_PRIVATE)
  }

  internal var enableWarningNotification: Boolean
    get() {
      return threadCanaryPrefs
        .getBoolean("WarningNotificationEnabled", true)
    }
    set(value) {
      threadCanaryPrefs
        .edit()
        .putBoolean("WarningNotificationEnabled", value)
        .apply()
    }

  internal var warningNotificationThreads: Int
    get() {
      return threadCanaryPrefs
        .getInt("WarningNotificationThreads", 500)
    }
    set(value) {
      threadCanaryPrefs
        .edit()
        .putInt("WarningNotificationThreads", value)
        .apply()
    }

  @JvmStatic
  fun initialize(context: Context) {
    ThreadCanary.context = context
    singleThreadPool = ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
      LinkedBlockingQueue(), ThreadCanaryThreadFactory(), ThreadPoolExecutor.DiscardPolicy())

    onUpdate {
      if (enableWarningNotification) {
        val threadSize = getThreadSize()
        if (threadSize >= warningNotificationThreads) {
          val pendingIntent = ThreadCanaryActivity.createPendingIntent(context)
          val title = context.getString(R.string.thread_canary_notification_title).format(threadSize)
          val content = context.getString(R.string.thread_canary_notification_content)
          Notifications.showNotification(context, title, content, pendingIntent, 0)
        }
      }
    }
  }

  @Synchronized
  fun onUpdate(block: () -> Unit) : () -> Unit {
    threadChangedListeners.add(block)
    return {
      threadChangedListeners.remove(block)
    }
  }

  @JvmStatic
  @Synchronized
  fun addThreadPoolTrace(threadPoolTrace: ThreadPoolTrace) {
    threadPoolTraces[threadPoolTrace.name] = threadPoolTrace
    dispatchThreadChangedListener()
  }

  @JvmStatic
  @Synchronized
  fun addThreadTrace(threadPoolName: String, threadTrace: ThreadTrace) {
    if (threadPoolTraces.containsKey(threadPoolName)) {
      threadPoolTraces[threadPoolName]?.addThread(threadTrace.id)
      addThreadTrace(threadTrace)
    }
  }

  @JvmStatic
  @Synchronized
  fun removeThreadPoolTrace(threadPoolName: String): Boolean {
    val result = threadPoolTraces.remove(threadPoolName) != null
    dispatchThreadChangedListener()
    return result
  }

  @JvmStatic
  fun getThreadPoolTrace(threadPoolName: String?): ThreadPoolTrace? {
    return threadPoolName?.let { threadPoolTraces[it] }
  }

  @JvmStatic
  @Synchronized
  fun addThreadTrace(threadTrace: ThreadTrace) {
    if (!threadTraces.containsKey(threadTrace.id)) {
      threadTraces[threadTrace.id] = threadTrace
      dispatchThreadChangedListener()
    }
  }

  @JvmStatic
  @Synchronized
  fun removeThreadTrace(threadId: Long): Boolean {
    val result = threadTraces.remove(threadId) != null
    dispatchThreadChangedListener()
    return result
  }

  @JvmStatic
  fun getThreadTrace(threadId: Long): ThreadTrace? {
    return threadTraces[threadId]
  }

  @JvmStatic
  fun getThreadPoolTraces(): List<ThreadPoolTrace> {
    return threadPoolTraces.values.toList()
  }

  @JvmStatic
  fun getThreadTraces(): List<ThreadTrace> {
    val result = mutableListOf<ThreadTrace>()
    val stackTraces = Thread.getAllStackTraces()
    for (stackTrace in stackTraces) {
      val thread = stackTrace.key
      val trace = stackTrace.value
      if (threadTraces.containsKey(thread.id)) {
        val threadTrace = threadTraces[thread.id] as ThreadTrace
        result.add(threadTrace.copy())
      } else {
        val newThreadTrace = ThreadTrace.newInstance(thread, trace)
        threadTraces[thread.id] = newThreadTrace
        result.add(newThreadTrace)
      }
    }
    return result
  }

  @JvmStatic
  fun getThreadSize(): Int {
    return getThreadTraces().size
  }

  private fun dispatchThreadChangedListener() {
    runOnUiThread {
      for (threadChangedListener in threadChangedListeners) {
        threadChangedListener.onThreadChanged()
      }
    }
  }

  internal fun interface OnThreadChangedListener {

    fun onThreadChanged()
  }

  internal class ThreadCanaryThreadFactory : ThreadFactory {
    private val threadNumber = AtomicInteger(1)

    override fun newThread(r: Runnable): Thread {
      val t = Thread(r)
      t.name = "threadcanary-thread-${threadNumber.getAndIncrement()}"
      t.isDaemon = true
      return t
    }
  }
}
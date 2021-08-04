package com.example.testthread.ui.screen

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testthread.ThreadCanary
import com.example.testthread.model.StackTrace
import com.example.testthread.model.ThreadPoolTrace
import com.example.testthread.model.ThreadTrace
import com.example.testthread.ui.adapter.ThreadsAdapter
import com.example.testthread.ui.navigation.activity
import com.example.testthread.ui.navigation.inflate
import com.example.testthread.ui.navigation.onCreateOptionsMenu
import com.example.testthread.ui.navigation.onScreenExiting
import com.example.testthread.util.runOnUiThread
import com.example.testthread.R
import java.util.*


internal class ThreadsScreen : RememberScreen() {

  @Transient
  private val threadsAdapter = ThreadsAdapter()

  override fun createContentView(container: ViewGroup) = container.inflate(R.layout.thread_canary_list).apply {
    val recyclerView = findViewById<RecyclerView>(R.id.thread_canary_list)
    recyclerView.adapter = threadsAdapter
  }

  override fun onScreenShowing(contentView: View) {
    contentView.run {
      onAddRefreshMenu()

      val unsubscribeRefresh = ThreadCanary.onUpdate {
        onSetThreads()
      }

      onScreenExiting {
        unsubscribeRefresh()
      }

      onSetThreads()
    }
  }

  private fun View.onAddRefreshMenu() {
    onCreateOptionsMenu { menu ->
      val iconDrawable = ContextCompat.getDrawable(context, R.drawable.thread_canary_anim_refresh) as AnimatedVectorDrawable
      menu.add(R.string.thread_canary_refresh_threads)
        .setIcon(iconDrawable)
        .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        .setOnMenuItemClickListener {
          iconDrawable.start()
          onSetThreads()
          true
        }
    }
  }

  private fun View.onSetThreads() {
    ThreadCanary.singleThreadPool.submit {
      val adapterList = LinkedList<Pair<ThreadsAdapter.ItemType, StackTrace>>()

      val threadPoolTraces = ThreadCanary.getThreadPoolTraces()
      threadPoolTraces.forEach { threadPoolTrace ->
        adapterList.add(ThreadsAdapter.ItemType.THREAD_POOL to threadPoolTrace)
      }

      val threadTraces = ThreadCanary.getThreadTraces()
      threadTraces.filter { it.isSingleThread() }.forEach { threadTrace ->
        adapterList.add(ThreadsAdapter.ItemType.SINGLE_THREAD to threadTrace)
      }

      adapterList.sortByDescending { it.second }

      for (index in adapterList.size - 1 downTo 0) {
        val stackTrace = adapterList[index].second
        if (stackTrace is ThreadPoolTrace) {
          val traces = mutableListOf<ThreadTrace>()
          stackTrace.getThreadIds().forEach { threadId ->
            ThreadCanary.getThreadTrace(threadId)?.let { traces.add(it) }
          }
          traces.sortDescending()

          traces.forEachIndexed { i, v ->
            adapterList.add(index + i + 1, ThreadsAdapter.ItemType.THREAD_POOL_THREAD to v)
          }
        }
      }

      runOnUiThread {
        activity.title = String.format(resources.getString(R.string.thread_canary_threads), threadTraces.size)
        threadsAdapter.setItems(adapterList)
      }
    }
  }

}
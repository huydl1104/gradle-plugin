package com.example.testthread.model


/**
 * @author yudongliang
 * create time 2021-08-04
 * describe : trace 的信息bean
 */
internal data class ThreadPoolTrace @JvmOverloads constructor(
  /**
   * The thread pool name (unique).
   */
  val name: String,

  /**
   * The Thread IDs created by the current thread pool.
   */
  private val threadIds: MutableList<Long> = ArrayList(),

  /**
   * Create the stack trace information of the thread pool.
   */
  override val stackTraces: Array<StackTraceElement> = emptyArray(),

  /**
   * The thread pool create time.
   */
  override val createTime: Long = System.currentTimeMillis(),

  /**
   * The thread pool update time.
   */
  override var updateTime: Long = createTime
) : StackTrace {

  fun addThread(threadId: Long) {
    threadIds.add(threadId)
    updateTime = System.currentTimeMillis()
  }

  fun removeThread(threadId: Long) {
    threadIds.add(threadId)
    updateTime = System.currentTimeMillis()
  }

  fun getThreadIds(): List<Long> {
    return threadIds
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ThreadPoolTrace

    return name == other.name
  }

  override fun hashCode(): Int {
    return 31 * name.hashCode()
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun newInstance(name: String, stackTraces: Array<StackTraceElement>, threadIds: MutableList<Long> = ArrayList()): ThreadPoolTrace {
      return ThreadPoolTrace(name, threadIds, stackTraces)
    }
  }
}
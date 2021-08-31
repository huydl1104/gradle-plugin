package com.example.testthread.model


/**
 * @author yudongliang
 * create time 2021-08-04
 * describe : Thread trace information.
 */
internal data class ThreadTrace(

  /**
   * The thread id (unique).
   */
  val id: Long,

  /**
   * The thread name.
   */
  val name: String,

  /**
   * The thread state string.
   *
   * @see Thread.State
   */
  val state: String,

  /**
   * The thread pool name of the created thread.
   *
   * If it is null, it is single thread.
   */
  val threadPoolName: String? = null,

  /**
   * Create or start the stack trace information of the thread.
   */
  override val stackTraces: Array<StackTraceElement>,

  /**
   * The thread create or start time.
   */
  override val createTime: Long = System.currentTimeMillis(),

  /**
   * The thread update time.
   */
  override var updateTime: Long = createTime
) : StackTrace {

  /**
   * Whether the thread was created by the thread pool.
   *
   * @return true: just single thread, false: created by thread pool.
   */
  fun isSingleThread(): Boolean = threadPoolName == null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ThreadTrace

    return id == other.id
  }

  override fun hashCode(): Int {
    return 31 * id.toInt()
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun newInstance(thread: Thread, stackTraces: Array<StackTraceElement>, threadPoolName: String? = null): ThreadTrace {
      return ThreadTrace(thread.id, thread.name, thread.state.name, threadPoolName, stackTraces)
    }
  }
}
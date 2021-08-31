package com.example.testthread.model


/**
 * @author yudongliang
 * create time 2021-08-04
 * describe : 基本的trace interface，更具更新的时间排序
 */
internal interface StackTrace : Comparable<StackTrace> {

  /**
   * stack trace information.
   */
  val stackTraces: Array<StackTraceElement>

  /**
   * creation time.
   */
  val createTime: Long

  /**
   * last updated time.
   */
  var updateTime: Long

  override fun compareTo(other: StackTrace) = (updateTime - other.updateTime).toInt()

}
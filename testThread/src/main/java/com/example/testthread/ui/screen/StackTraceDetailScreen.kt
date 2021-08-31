package com.example.testthread.ui.screen

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testthread.ThreadCanary
import com.example.testthread.ui.adapter.StackTraceAdapter
import com.example.testthread.R
import com.example.testthread.ui.navigation.Screen
import com.example.testthread.ui.navigation.activity
import com.example.testthread.ui.navigation.inflate
import com.example.testthread.ui.navigation.onCreateOptionsMenu
import com.example.testthread.ui.navigation.shareText

/**
 * @author airsaid
 */
internal class StackTraceDetailScreen(private val threadId: Long? = null, private val threadPoolName: String? = null) : Screen() {

  init {
    if (threadId == null && threadPoolName == null) {
      throw IllegalArgumentException("The threadId or threadPoolName arguments are missing!")
    }
  }

  override fun createView(container: ViewGroup) =
    container.inflate(R.layout.thread_canary_list).apply {
      val recyclerView = findViewById<RecyclerView>(R.id.thread_canary_list)
      val stackTraceAdapter = StackTraceAdapter()
      recyclerView.adapter = stackTraceAdapter

      threadId?.apply {
        ThreadCanary.getThreadTrace(this)?.apply {
          onSetThreadTrace(name, stackTraceAdapter, stackTraces.toList(), threadPoolName)
        }
      }

      threadPoolName?.apply {
        ThreadCanary.getThreadPoolTrace(this)?.apply {
          onSetThreadTrace(name, stackTraceAdapter, stackTraces.toList())
        }
      }
    }

  private fun View.onSetThreadTrace(title: String, adapter: StackTraceAdapter, stackTraces: List<StackTraceElement>, threadPoolName: String? = null) {
    activity.title = title

    val itemList = mutableListOf<Pair<StackTraceAdapter.ItemType, Any>>()
    threadPoolName?.let {
      itemList.add(StackTraceAdapter.ItemType.HEADER_THREAD_POOL to it)
    }

    stackTraces.forEach { stackTrace ->
      itemList.add(StackTraceAdapter.ItemType.STACK_TRACE to stackTrace)
    }

    adapter.setItems(itemList)

    val stackTrace = stackTraces.joinToString("\n")
    if (stackTrace.isNotEmpty()) {
      onCreateOptionsMenu { menu ->
        menu.add(R.string.thread_canary_share_stack_trace)
          .setOnMenuItemClickListener {
            stackTrace.shareText(context)
            true
          }
      }
    }
  }
}
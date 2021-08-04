package com.example.testthread.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.testthread.model.ThreadPoolTrace
import com.example.testthread.model.ThreadTrace
import com.example.testthread.ui.navigation.goTo
import com.example.testthread.R
import com.example.testthread.ui.screen.StackTraceDetailScreen

/**
 * @author airsaid
 */
internal class ThreadsAdapter(items: MutableList<Pair<ItemType, Any>> = ArrayList()) :
  MutableListAdapter<Pair<ThreadsAdapter.ItemType, Any>, ThreadsAdapter.ViewHolder>(items) {

  enum class ItemType(val id: Int) {
    SINGLE_THREAD(0),
    THREAD_POOL(1),
    THREAD_POOL_THREAD(2)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.create(parent, viewType)
  }

  override fun getItemViewType(position: Int): Int {
    return get(position).first.id
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val context = holder.itemView.context
    val data = get(position).second
    var hasStackTrace = false

    when (holder.itemViewType) {
      ItemType.SINGLE_THREAD.id, ItemType.THREAD_POOL_THREAD.id -> {
        (data as ThreadTrace).apply {
          holder.name.text = name
          holder.state.text = state
          hasStackTrace = stackTraces.isNotEmpty()
        }
      }
      ItemType.THREAD_POOL.id -> {
        (data as ThreadPoolTrace).apply {
          holder.name.text = name
          holder.state.text = getThreadIds().size.toString()
          hasStackTrace = stackTraces.isNotEmpty()
        }
      }
    }

    setEnable(hasStackTrace, holder.name, holder.state)

    holder.itemView.setOnClickListener {
      if (!hasStackTrace) {
        Toast.makeText(context, R.string.thread_canary_no_stack_trace_info, Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      when (holder.itemViewType) {
        ItemType.SINGLE_THREAD.id, ItemType.THREAD_POOL_THREAD.id -> {
          it.goTo(StackTraceDetailScreen((data as ThreadTrace).id))
        }
        ItemType.THREAD_POOL.id -> {
          it.goTo(StackTraceDetailScreen(threadPoolName = (data as ThreadPoolTrace).name))
        }
      }
    }
  }

  private fun setEnable(enable: Boolean, vararg views: View) {
    for (view in views) {
      view.alpha = if (enable) 1.0f else 0.5f
    }
  }

  internal class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name)
    val state: TextView = itemView.findViewById(R.id.state)

    companion object {
      fun create(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = when (viewType) {
          ItemType.SINGLE_THREAD.id -> R.layout.thread_canary_item_single_thread
          ItemType.THREAD_POOL.id -> R.layout.thread_canary_item_thread_pool
          else -> R.layout.thread_canary_item_thread_pool_thread
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
      }
    }
  }

  override fun compareItems(checkContent: Boolean, a: Pair<ItemType, Any>, b: Pair<ItemType, Any>): Boolean {
    val v1 = a.second
    val v2 = b.second
    if (v1 is ThreadPoolTrace && v2 is ThreadPoolTrace) {
      return if (checkContent) v1 == v2 else v1.name == v2.name
    }
    if (v1 is ThreadTrace && v2 is ThreadTrace) {
      return if (checkContent) v1 == v2 else v1.id == v2.id
    }
    return false
  }
}
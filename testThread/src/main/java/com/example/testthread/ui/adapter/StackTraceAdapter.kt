package com.example.testthread.ui.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testthread.ui.navigation.goTo
import com.example.testthread.R
import com.example.testthread.ui.screen.StackTraceDetailScreen
import com.example.testthread.widget.StackTraceConnectorView

/**
 * @author airsaid
 */
internal class StackTraceAdapter : MutableListAdapter<Pair<StackTraceAdapter.ItemType, Any>, StackTraceAdapter.ViewHolder>() {

  enum class ItemType(val id: Int) {
    HEADER_THREAD_POOL(0),
    STACK_TRACE(1)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.create(parent, viewType)
  }

  override fun getItemViewType(position: Int): Int {
    return get(position).first.id
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = get(position)
    when (item.first) {
      ItemType.HEADER_THREAD_POOL -> {
        val threadPoolName = item.second as String
        setThreadPoolTrace(holder, threadPoolName)
      }
      ItemType.STACK_TRACE -> {
        val stackTraceElement = item.second as StackTraceElement
        setStackTrace(holder, position, stackTraceElement)
      }
    }
  }

  private fun setThreadPoolTrace(holder: ViewHolder, threadPoolName: String) {
    val context = holder.itemView.context
    val seeDetail = context.getString(R.string.thread_canary_see_detail)
    val threadPoolInfo = "$threadPoolName: "

    val threadPoolSpan = SpannableString(threadPoolInfo + seeDetail)
    val startIndex = threadPoolInfo.length
    val endIndex = threadPoolSpan.length
    threadPoolSpan.setSpan(object : ClickableSpan() {
      override fun onClick(widget: View) {
        widget.goTo(StackTraceDetailScreen(threadPoolName = threadPoolName))
      }
    }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    holder.traceText.movementMethod = LinkMovementMethod.getInstance()
    holder.traceText.text = threadPoolSpan
  }

  private fun setStackTrace(holder: ViewHolder, position: Int, stackTraceElement: StackTraceElement) {
    val stackTrace = stackTraceElement.toString()
    setHighlightingColor(holder, stackTrace)
    holder.traceText.text = stackTrace

    holder.traceText.post {
      holder.connectorView?.apply {
        val lp = layoutParams
        lp.height = holder.traceText.measuredHeight
        layoutParams = lp

        type = when {
          position == 1 && get(0).first == ItemType.HEADER_THREAD_POOL -> StackTraceConnectorView.Type.START
          position == size - 1 -> StackTraceConnectorView.Type.END
          else -> StackTraceConnectorView.Type.MIDDLE
        }
      }
    }
  }

  private fun setHighlightingColor(holder: ViewHolder, stackTrace: String) {
    val context = holder.itemView.context
    val defaultColor = ContextCompat.getColor(context, R.color.thread_canary_text_color)
    val highlightingColor = ContextCompat.getColor(context, R.color.thread_canary_text_highlighting)
    if (stackTrace.startsWith(context.packageName)) {
      holder.traceText.setTextColor(highlightingColor)
      holder.traceText.typeface = Typeface.DEFAULT_BOLD
    } else {
      holder.traceText.setTextColor(defaultColor)
      holder.traceText.typeface = Typeface.DEFAULT
    }
  }

  override fun compareItems(checkContent: Boolean, a: Pair<ItemType, Any>, b: Pair<ItemType, Any>): Boolean {
    val v1 = a.second
    val v2 = b.second
    return if (checkContent) v1 == v2 else v1 === v2
  }

  internal class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val connectorView: StackTraceConnectorView? = itemView.findViewById(R.id.connectorView)
    val traceText: TextView = itemView.findViewById(R.id.traceText)

    companion object {
      fun create(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = if (viewType == ItemType.HEADER_THREAD_POOL.id) {
          R.layout.thread_canary_item_head_thread_pool
        } else {
          R.layout.thread_canary_item_stack_trace
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
      }
    }
  }
}
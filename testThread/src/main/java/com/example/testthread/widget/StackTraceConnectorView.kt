package com.example.testthread.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testthread.R


internal class StackTraceConnectorView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

  enum class Type {
    START,
    MIDDLE,
    END
  }

  var type: Type = Type.MIDDLE
    set(value) {
      field = value
      invalidate()
    }

  private val circleRadius: Float by lazy {
    resources.getDimensionPixelSize(R.dimen.thread_canary_stack_trace_connector_circle_radius).toFloat()
  }

  private val circleColor: Int by lazy {
    ContextCompat.getColor(getContext(), R.color.thread_canary_stack_trace_connector_circle)
  }

  private val lineWidth: Float by lazy {
    resources.getDimensionPixelSize(R.dimen.thread_canary_stack_trace_connector_line_width).toFloat()
  }

  private val lineColor: Int by lazy {
    ContextCompat.getColor(getContext(), R.color.thread_canary_stack_trace_connector_line)
  }

  private val arrowheadSize: Float by lazy {
    resources.getDimensionPixelSize(R.dimen.thread_canary_stack_trace_connector_arrowhead_size).toFloat()
  }

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  override fun onDraw(canvas: Canvas) {
    val cx = measuredWidth / 2f
    val cy = measuredHeight / 2f

    if (type != Type.END) {
      // draw line
      paint.strokeWidth = lineWidth
      paint.color = lineColor
      canvas.drawLine(cx, 0f, cx, measuredHeight - lineWidth, paint)

      // draw arrowhead
      canvas.save()
      canvas.translate(0f, cy - lineWidth)
      canvas.rotate(45f, cx, cy)
      canvas.drawLine(cx - arrowheadSize, cy, cx, cy, paint)
      canvas.drawLine(cx, cy + lineWidth / 2f, cx, cy - arrowheadSize, paint)
      canvas.restore()
    }

    if (type != Type.START) {
      // draw circle
      paint.color = circleColor
      canvas.drawCircle(cx, circleRadius, circleRadius, paint)
    }
  }
}
package com.example.guardia_app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class GoalProgressBar (context: Context, attrs: AttributeSet) : View(context, attrs) {

        private var progress: Int = 0
        private var max: Int = 100

        private val backgroundPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.pale_pink)
        }

        private val holePaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.blood_red)
        }

        private val progressPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.blood_red)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            // Draw background track
            canvas.drawArc(rect, 180f, 180f, false, backgroundPaint)

            // Calculate progress width
            val sweepAngle = (progress.toFloat() / max) * 180f

            // Draw progress fill
            canvas.drawArc(rect, 180f, sweepAngle, false, progressPaint)

            // Cut out the inner circle to make it a donut
            val holeRadius = width / 4f
            canvas.drawCircle(width / 2f, height.toFloat(), holeRadius, holePaint)
        }

        fun setProgress(value: Int) {
            progress = value.coerceIn(0, max)
            invalidate() // redraw with new progress
        }

        fun setMax(value: Int) {
            max = value
            invalidate()
        }
}

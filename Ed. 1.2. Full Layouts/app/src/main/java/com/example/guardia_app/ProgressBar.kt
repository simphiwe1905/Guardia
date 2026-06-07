package com.example.guardia_app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class ProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress: Int = 0
    private var max: Int = 100

    private val backgroundPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.pale_pink)
    }

    private val progressPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.blood_red)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background track
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        // Calculate progress width
        val progressWidth = (progress.toFloat() / max) * width

        // Draw progress fill
        canvas.drawRect(0f, 0f, progressWidth, height.toFloat(), progressPaint)
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
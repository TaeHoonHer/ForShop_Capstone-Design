package com.example.a4shop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GridOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    var showGrid = false
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!showGrid) return

        // 카메라 프리뷰의 크기를 가져와써
        val parentWidth = (parent as View).width.toFloat()
        val parentHeight = (parent as View).height.toFloat()

        val numCells = 3

        val cellWidth = parentWidth / numCells
        val cellHeight = parentHeight / numCells

        // 격자 그리기
        for (i in 1 until numCells) {
            val startX = cellWidth * i
            canvas.drawLine(startX, 0f, startX, parentHeight, paint)
        }

        for (i in 1 until numCells) {
            val startY = cellHeight * i
            canvas.drawLine(0f, startY, parentWidth, startY, paint)
        }
    }
}

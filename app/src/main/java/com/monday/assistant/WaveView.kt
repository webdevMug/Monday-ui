package com.monday.assistant

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.PI
import kotlin.math.sin

class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var phase = 0f
    private var level = 0.15f
    private var targetLevel = 0.15f
    private val path = Path()
    private val colors = intArrayOf(
        Color.parseColor("#00E5FF"),
        Color.parseColor("#2979FF"),
        Color.parseColor("#B388FF")
    )

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        maskFilter = BlurMaskFilter(12f, BlurMaskFilter.Blur.SOLID)
    }
    private val corePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00E5FF")
        maskFilter = BlurMaskFilter(40f, BlurMaskFilter.Blur.NORMAL)
    }

    private val animator = ValueAnimator.ofFloat(0f, (2 * PI).toFloat()).apply {
        duration = 2500
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            phase = it.animatedValue as Float
            invalidate()
        }
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun setLevel(value: Float) {
        targetLevel = value.coerceIn(0.05f, 1f)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val cy = h / 2
        level += (targetLevel - level) * 0.12f

        canvas.drawCircle(w / 2, cy, h * 0.10f * (1f + level), corePaint)

        for (i in 0..2) {
            path.reset()
            path.moveTo(0f, cy)
            val amp = h * 0.28f * level * (1f - i * 0.25f)
            var x = 0f
            while (x <= w) {
                val t = (x / w).toDouble()
                val y = cy + (amp * sin(t * 2 * PI * (1.5 + i * 0.5) + phase * (1 + i * 0.4)) * sin(PI * t)).toFloat()
                path.lineTo(x, y)
                x += 6f
            }
            wavePaint.color = colors[i]
            canvas.drawPath(path, wavePaint)
        }
    }
}

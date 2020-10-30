package com.app.xandone.widgetlib.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

/**
 * author: Admin
 * created on: 2020/9/4 09:27
 * description:
 */
class CodeEditTextView : AppCompatEditText, View.OnClickListener {
    private var paddingHorizontal = 0f
    private var lineSpace = 0f
    private var lineWidth = 0f
    private val rectF = RectF()
    private val bounds = Rect()
    private var mLineColor = 0
    private var mLineColorFilled = 0
    private val mBgColor = Color.TRANSPARENT
    private var paint: Paint? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        paint = Paint()
        paint?.isAntiAlias = true
        lineWidth = dp2px(context, 2f).toFloat()
        mLineColor = Color.BLACK
        mLineColorFilled = Color.BLACK
        isCursorVisible = false
        setOnClickListener(this)
        setBackgroundResource(0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF[0f, 0f, w.toFloat()] = h.toFloat()
        lineSpace = w * 0.2f
        paddingHorizontal = lineSpace / (MAX_LENTH + 1)
    }

    override fun onDraw(canvas: Canvas) {
        paint?.color = mBgColor
        paint?.style = Paint.Style.FILL
        canvas.drawRoundRect(rectF, height / 2.toFloat(), height / 2.toFloat(), paint!!)
        paint?.style = Paint.Style.FILL
        paint?.strokeWidth = 0f
        paint?.textSize = textSize
        paint?.color = textColors.defaultColor
        val chars: CharArray
        var len = 0
        if (text != null) {
            chars = text.toString().toCharArray()
            len = chars.size
            for (i in 0 until len) {
                paint?.getTextBounds(chars, i, 1, bounds)
                canvas.drawText(
                    chars,
                    i,
                    1,
                    lineSpace / 2 + (paddingHorizontal + lineSpace) * i + paddingHorizontal / 2,
                    height / 2 + bounds.height() / 2.toFloat(),
                    paint!!
                )
            }
        }
        paint?.color = mLineColor
        paint?.strokeWidth = lineWidth
        for (i in 0 until MAX_LENTH) {
            if (i == 0 || i < len) {
                paint?.color = mLineColorFilled
            } else {
                paint?.color = mLineColor
            }
            canvas.drawLine(
                (paddingHorizontal + lineSpace) * i + paddingHorizontal,
                height.toFloat(),
                (i + 1) * (lineSpace + paddingHorizontal),
                height.toFloat(),
                paint!!
            )
        }
    }

    override fun onClick(v: View) {
        val index = if (text == null) 0 else text?.length
        setSelection(index!!)
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {
        private const val MAX_LENTH = 4
    }
}
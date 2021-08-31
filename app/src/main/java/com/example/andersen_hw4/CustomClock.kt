package com.example.andersen_hw4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

private var mHeight = 0

private var mWidth = 0
private var fontSize = 0
private var numeralSpacing = 0
private  var mPadding = 0
private var mHandTruncation = 0
private var mHourHandTruncation = 0
private var mRadius = 0
private lateinit var mPaint: Paint
private var isInt = false
private val mClockHours = listOf(1,2,3,4,5,6,7,8,9,10,11,12)
private val mRect = Rect()

class CustomClock(context: Context?) : View(context) {

    init {
        mWidth = width
        mHeight = height
        mPaint = Paint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas.apply {
            mPaint.reset()
            mPaint.color = Color.RED
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = 4F
            mPaint.isAntiAlias = true
            canvas?.drawCircle((mWidth / 2).toFloat(),
                (mHeight / 2).toFloat(), (mRadius + mPadding - 10).toFloat(), mPaint)

            /** clock-center */
            mPaint.style = Paint.Style.FILL;
            canvas?.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), 12F, mPaint)


            val fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                14F,
                resources.displayMetrics);
            mPaint.textSize = fontSize

            for (hour in mClockHours) {
                val tmp = hour.toString()
                mPaint.getTextBounds(tmp, 0, tmp.length, mRect)

                // find the circle-wise (x, y) position as mathematical rule
                val angle = Math.PI / 6 * (hour - 3);
                val x = (mWidth / 2 + cos(angle) * mRadius - mRect.width() / 2)
                val y = (mHeight / 2 + sin(angle) * mRadius + mRect.height() / 2)

                canvas?.drawText(tmp, x.toFloat(), y.toFloat(), mPaint)
            }


            /** draw clock hands to represent the every single time */

            val calendar = Calendar.getInstance();
            var hour = calendar.get(Calendar.HOUR_OF_DAY);
            hour = if (hour > 12) hour - 12 else hour;

            drawHandLine(canvas,
                (hour + calendar.get(Calendar.MINUTE) / 60) * 5f,
                true,
                b1 = false) // draw hours
            drawHandLine(canvas,
                calendar.get(Calendar.MINUTE).toFloat(),
                false,
                b1 = false) // draw minutes
            drawHandLine(canvas,
                calendar.get(Calendar.SECOND).toFloat(),
                false,
                b1 = true) // draw seconds


            /** invalidate the appearance for next representation of time  */
            postInvalidateDelayed(500)
            invalidate()
        }
    }

    private fun drawHandLine(canvas: Canvas?, get: Float, b: Boolean, b1: Boolean) {
        val angle = Math.PI * get / 30 - Math.PI / 2;
        val handRadius = if(b) mRadius - mHandTruncation - mHourHandTruncation else mRadius - mHandTruncation;
        if (b1) mPaint.setColor(Color.YELLOW);
        canvas?.drawLine((mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),  ((mWidth / 2 + Math.cos(angle) * handRadius).toFloat()),  ((mHeight / 2 + Math.sin(angle) * handRadius).toFloat()), mPaint);
    }
}
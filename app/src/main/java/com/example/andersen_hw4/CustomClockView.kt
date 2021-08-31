package com.example.andersen_hw4

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CustomClockView (context: Context, val attr: AttributeSet) :
    androidx.appcompat.widget.AppCompatTextView(context, attr) {


    init {

    }
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private lateinit var paint: Paint
    private var isInit = false
    private val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()
    private var secondArrowColor = 0
    private var minuteArrowColor = 0
    private var hourArrowColor = 0
    private var centerX = 0
    private var centerY = 0

    private fun init(){
        centerX = width/2
        centerY = height/2
        padding = numeralSpacing + 50
        fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20F, resources.displayMetrics).toInt()
        val min = min(height, width)
        radius = min / 2 - padding

        paint = Paint()
        isInit = true
        val taped: TypedArray = context.obtainStyledAttributes(attr,R.styleable.CustomClockView)

        secondArrowColor = taped.getColor(R.styleable.CustomClockView_second_arrow_color, Color.BLUE)
        minuteArrowColor = taped.getColor(R.styleable.CustomClockView_minute_arrow_color, Color.RED)
        hourArrowColor = taped.getColor(R.styleable.CustomClockView_hour_arrow_color, Color.BLACK)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (!isInit) {
            init();
        }
        canvas?.drawColor(Color.LTGRAY)

        drawCircle(canvas)
        drawNumeral(canvas)
        drawHands(canvas!!)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawHand(canvas: Canvas?, loc: Float, isHour: Boolean){
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas!!.drawLine((centerX).toFloat(), (centerY).toFloat(),
            (centerX+cos(angle)*handRadius).toFloat(), (centerY+ sin(angle)*handRadius).toFloat(),
            paint)
    }

    private fun drawHands(canvas: Canvas){

        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        paint.color = hourArrowColor
        drawHand(canvas, ((hour + c.get(Calendar.MINUTE) / 60) * 5f), true)
        paint.color = secondArrowColor
        drawHand(canvas, c.get(Calendar.MINUTE).toFloat(), false)
        paint.color = minuteArrowColor
        drawHand(canvas, c.get(Calendar.SECOND).toFloat(), false)
    }

    private fun drawNumeral (canvas: Canvas?){
        paint.textSize = fontSize.toFloat()

        for (number in numbers){
            val tmp = number.toString()
            paint.getTextBounds(tmp,0,tmp.length,rect)
            val angle = Math.PI/6 * (number-3)
            val x = ((centerX + cos(angle) * radius - rect.width() / 2).toFloat())
            val y = ((centerY + sin(angle) * radius + rect.height() / 2).toFloat())
            canvas?.drawText(tmp,x,y, paint)
        }
    }

    private fun drawCircle(canvas: Canvas?){
        paint.reset()
        paint.color = Color.BLACK
        paint.strokeWidth = 5F
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas?.drawCircle((centerX).toFloat(), (centerY).toFloat(),
            (radius + padding - 10).toFloat(), paint)

    }
}
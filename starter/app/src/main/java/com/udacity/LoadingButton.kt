package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.animation.addListener
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val round = 360
    private val animationDuration = 1000L
    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0

    private val loadingRect = Rect()

    private var primaryBackgroundColor = context.getColor(R.color.colorPrimary)
    private var primaryDarkBackgroundColor = context.getColor(R.color.colorPrimaryDark)

    private var textLoading = context.getText(R.string.button_loading).toString()
    private var textDefault = context.getText(R.string.button_download).toString()
    private var textColor = Color.WHITE
    private var textToDraw = textDefault

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                valueAnimator.setIntValues(0, round)
                valueAnimator.setDuration(animationDuration).apply {
                    addUpdateListener {
                        progress = animatedValue as Int
                        invalidate()
                    }

                    start()
                }
                paint.color = primaryDarkBackgroundColor
            }
            else -> {
                paint.color = primaryBackgroundColor
            }
        }
        requestLayout()
    }

    init {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

            // Save canvas
            it.save()

            // Fill the background
            it.drawColor(primaryBackgroundColor)


            if (buttonState == ButtonState.Loading) {
                paint.color = primaryDarkBackgroundColor
                loadingRect.set(progress*widthSize/round, 0, widthSize, heightSize)
                textToDraw = textLoading
                it.drawRect(loadingRect, paint)
            } else {
                textToDraw = textDefault
            }

            paint.color = textColor
            it.drawText(textToDraw, widthSize / 2f, (heightSize / 2) - ((paint.descent() + paint.ascent()) / 2), paint)

        }
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    @JvmName("setButtonState1")
    fun setButtonState(newState: ButtonState) {
        buttonState = newState
    }
}
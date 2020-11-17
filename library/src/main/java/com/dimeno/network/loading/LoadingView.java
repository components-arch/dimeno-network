package com.dimeno.network.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

import com.dimeno.network.R;

/**
 * loading view
 * Created by wangzhen on 2018/11/22.
 */
public class LoadingView extends View {
    private int mWidth;
    private int mHeight;
    private final Paint mPaint;

    private int startAngle = -90;
    private int minAngle = -90;
    private int sweepAngle = 0;
    private int curAngle = 0;

    private final RectF mRect;

    private ValueAnimator mAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
        int radius = (int) typedArray.getDimension(R.styleable.LoadingView_radius, dip2px(20));
        int color = typedArray.getColor(R.styleable.LoadingView_border_color, Color.WHITE);
        float strokeWidth = typedArray.getDimension(R.styleable.LoadingView_border_width, dip2px(2));
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRect = new RectF(-radius, -radius, radius, radius);

        startAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void startAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimator.setRepeatCount(Animation.INFINITE);
        mAnimator.setRepeatMode(Animation.RESTART);
        mAnimator.setDuration(2000);
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth * 1f / 2, mHeight * 1f / 2);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        if (startAngle == minAngle) {
            sweepAngle += 6;
        }
        if (sweepAngle >= 300 || startAngle > minAngle) {
            startAngle += 6;
            if (sweepAngle > 20) {
                sweepAngle -= 6;
            }
        }
        if (startAngle > minAngle + 300) {
            startAngle %= 360;
            minAngle = startAngle;
            sweepAngle = 20;
        }
        canvas.rotate(curAngle += 5, 0, 0);
        canvas.drawArc(mRect, startAngle, sweepAngle, false, mPaint);
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

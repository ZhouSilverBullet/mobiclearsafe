package com.mobi.clearsafe.ui.clear.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.utils.UiUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/23 10:09
 * @Dec 略
 */
public class GoodChangeLayout extends LinearLayout {

    private ValueAnimator valueAnimator;
    private int currentHeight;
    private TextView tvDec;

    public GoodChangeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.view_good_change_layout, this);

        tvDec = findViewById(R.id.tvDec);

//        currentHeight = (getResources().getDisplayMetrics().heightPixels * 2) / 3;
        currentHeight = UiUtils.dp2px(getContext(), 1080);

        valueAnimator = ValueAnimator.ofInt(currentHeight, UiUtils.dp2px(getContext(), 220));
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
//            layout(0, 0, getWidth(), animatedValue);
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = animatedValue;
            setLayoutParams(layoutParams);
        });
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, currentHeight);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

//        if (gradientPaint == null) {
//            gradientPaint = new Paint();
//            greenShader = new LinearGradient(0, 0, getWidth(), currentHeight, new int[]{getResources().getColor(R.color.c_0043ff), getResources().getColor(R.color.c_008dff)}, null, Shader.TileMode.REPEAT);
//            rect = new Rect(0, 0, getWidth(), currentHeight);
//            gradientPaint.setShader(greenShader);
//
//        }
    }

    public void setTvDec(String value) {
        tvDec.setText(value);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawRect(rect, gradientPaint);
        super.onDraw(canvas);

    }

    public void startAnim() {
        if (valueAnimator.isRunning()) {
            return;
        }
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }
}

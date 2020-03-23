package com.mobi.clearsafe.ui.clear.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.utils.ToastUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/22 20:52
 * @Dec 略
 */
public class SpeedLayout extends View {
    Paint paint;
    private ValueAnimator valueAnimator;

    //用来执行动画的
    private float mDrawHeight = -1;
    private Bitmap bitmap;
    private Paint gradientPaint;
    private Shader redShader;
    private Shader greenShader;
    private Rect rect;

    public SpeedLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        paint = new Paint();
        bitmap = getBitmap();

        //定义一个Paint
        gradientPaint = new Paint();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (valueAnimator == null) {
            initValueAnim(getHeight() + bitmap.getHeight(), bitmap.getHeight());

            rect = new Rect(0, 0, getWidth(), getHeight());
            greenShader = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{getResources().getColor(R.color.c_0043ff), getResources().getColor(R.color.c_008dff)}, null, Shader.TileMode.REPEAT);
            redShader = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{getResources().getColor(R.color.c_FF6A00), getResources().getColor(R.color.c_F49F1F)}, null, Shader.TileMode.REPEAT);
            gradientPaint.setShader(redShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawColor(getResources().getColor(R.color.white));
        canvas.drawRect(rect, gradientPaint);
        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2.0f, mDrawHeight, paint);

    }

    private void initValueAnim(float value, int bitmapHeight) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(value, 0);
            valueAnimator.setDuration(5000);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                mDrawHeight = animatedValue;
                if (mDrawHeight == 0) {
                    ToastUtils.showShort("执行完毕");
                    Log.e("SpeedLayout", " mDrawHeight " + mDrawHeight);
                }
                if (mDrawHeight <= value / 3) {
                    gradientPaint.setShader(greenShader);
                }
                invalidate();
            });
            valueAnimator.start();
        }
    }

    private Bitmap getBitmap() {
        Drawable drawable = getResources().getDrawable(R.mipmap.exit_icon);
        return drawableToBitmap(drawable);
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("PowerCoolActivity", "onDetachedFromWindow");
        //取消动画
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
        }
    }
}

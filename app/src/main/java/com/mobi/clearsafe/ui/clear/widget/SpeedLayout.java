package com.mobi.clearsafe.ui.clear.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
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

    public SpeedLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        paint = new Paint();
        bitmap = getBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (valueAnimator == null) {
            initValueAnim(getHeight() + bitmap.getHeight(), bitmap.getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(getResources().getColor(R.color.white));
        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2.0f, mDrawHeight, paint);


    }

    private void initValueAnim(float value, int bitmapHeight) {
        if (valueAnimator == null) {
//            valueAnimator = ValueAnimator.ofFloat(value, -bitmapHeight);
            valueAnimator = ValueAnimator.ofFloat(value, 0);
            valueAnimator.setDuration(5000);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(1000);
//            valueAnimator.reverse();
            valueAnimator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                mDrawHeight = animatedValue;
                if (mDrawHeight == 0) {
                    ToastUtils.showShort("执行完毕");
                    Log.e("SpeedLayout", " mDrawHeight " + mDrawHeight);
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("PowerCoolActivity", "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("PowerCoolActivity", "onDetachedFromWindow");
    }
}

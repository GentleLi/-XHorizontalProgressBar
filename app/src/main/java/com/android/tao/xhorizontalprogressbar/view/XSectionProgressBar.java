package com.android.tao.xhorizontalprogressbar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * PepperTV项目中宠物的进度条
 * Created by lijiantao on 2017/2/4.
 */

public class XSectionProgressBar extends View {

    private static final String TAG = XSectionProgressBar.class.getSimpleName();
    private Context mContext;
    private boolean mIsRoundRect = true;
    private int mProgress = 0;
    private int MAX = 100;
    private float mDefaultProgressHeight = 30;//像素
    private int mWidth, mHeight;// 整个的宽和高
    private int mProgressWidth;
    private int mProgressHeight;
    private float mProgressRadius;// 进度条圆角半径
    private float mProgressValue;//第二个值
    private float mProgressLeft = 5;
    private float mProgressTop = 5;
    private float mRealProgressWidth;
    private float mSectionWidth;
    private int mSectionValue;//每一个小矩形表示的进度值
    private Paint mValuePaint;
    private Paint mBgPaint;
    private int mValueColor = Color.BLUE;
    private int mBgColor = Color.GRAY;

    public XSectionProgressBar(Context context) {
        this(context, null);
    }

    public XSectionProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XSectionProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgPaint.setColor(Color.parseColor("#3e9e00"));
        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mValuePaint.setColor(Color.parseColor("#ffee16"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            //一般是设置了明确的值或者是MATCH_PARENT
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            //一般是设置了明确的值或者是MATCH_PARENT
            height = heightSize;
        } else {
            height = dp2px(20);
        }

        //调用父类的方法
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        calculateDimension();
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * calculate width and height
     */
    private void calculateDimension() {
        mWidth = getWidth();
        mHeight = getHeight();
        Log.e(TAG, "控件宽度：" + mWidth);
        Log.e(TAG, "控件高度：" + mHeight);
        mProgressLeft = 5;
        mProgressTop = 5;
        mProgressWidth = mWidth - 10;//进度条的实际显示的宽度 相当于padding=5
        mProgressHeight = mHeight - 10;//进度条实际显示的高度 相当于padding=5
        mProgressRadius = mProgressHeight / 2;
        mProgressValue = mProgressWidth / 4;//初始的进度条值

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mValuePaint.setColor(mValueColor);
        mBgPaint.setColor(mBgColor);
        mRealProgressWidth = mProgressWidth - 3 * 9;//每一个矩形之间预留5个像素的间隔
        mSectionWidth = mRealProgressWidth / 10;//每一个矩形的宽度
        float mSectionLeft = mProgressLeft;
        mSectionValue = MAX / 10;
        int count = mProgress / mSectionValue;
        mProgressRadius = mProgressHeight / 2;
        float remain = (mProgress % mSectionValue) * mSectionWidth / mSectionValue;//最后一段应该画的长度
        for (int i = 0; i < 10; i++) {
            mSectionLeft = mProgressLeft + i * (3 + mSectionWidth);//矩形中间的间隔
            drawRect(canvas, mBgPaint, mSectionLeft, mProgressTop, mSectionLeft + mSectionWidth, mProgressTop + mProgressHeight);
            if (i < count) {
                drawRect(canvas, mValuePaint, mSectionLeft, mProgressTop + 0.5f, mSectionLeft + mSectionWidth, mProgressTop + mProgressHeight - 0.5f);
            } else if (i == count) {
                if ((mProgress % mSectionValue) * 100 / mSectionValue < 20) {
                    int dif = mProgressHeight / 6;
                    //TODO 李建涛 待优化
//                    canvas.drawOval(mSectionLeft + 0.5f, mProgressTop, mSectionLeft + remain - 0.5f, mProgressTop + mProgressHeight,mValuePaint);
                    /*RectF rectF=new RectF(mSectionLeft + 0.5f, mProgressTop, mSectionLeft + remain - 0.5f, mProgressTop + mProgressHeight);
                    canvas.drawOval(rectF,mValuePaint);*/
                    drawRect(canvas, mValuePaint, mSectionLeft + 0.5f, mProgressTop + dif, mSectionLeft + remain - 0.5f, mProgressTop + mProgressHeight - dif);
                } else {
                    drawRect(canvas, mValuePaint, mSectionLeft, mProgressTop + 1, mSectionLeft + remain, mProgressTop + mProgressHeight - 1);
                }
            }
        }
    }


    /**
     * 绘制矩形
     *
     * @param canvas
     * @param paint
     * @param left   左
     * @param top    上
     * @param right  右
     * @param bottom 下
     */
    private void drawRect(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
        RectF rectF = new RectF(left, top, right, bottom);
        if (mIsRoundRect) {
            canvas.drawRoundRect(rectF, mProgressRadius, mProgressRadius, paint);
        } else {
            canvas.drawRect(rectF, paint);
        }
    }


    /**
     * 重新绘制
     */
    private void refreshUI() {
        this.mProgressValue = (mProgress * mProgressWidth) / MAX;
        invalidate();
    }

    /**
     * set and get begin
     */

    public void setIsRoundRect(boolean isRoundRect) {
        this.mIsRoundRect = isRoundRect;
    }

    public void setProgress(int progress) {
        if (progress < 0) return;
        if (progress > MAX) {
            this.mProgress = MAX;
        } else {
            this.mProgress = progress;
        }
        refreshUI();
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setMax(int max) {
        this.MAX = max;
    }

    public void setBgColor(int bgColor) {
        this.mBgColor = bgColor;
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.mValueColor = progressColor;
        invalidate();
    }

    /**
     * setter and getter end
     */


    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}






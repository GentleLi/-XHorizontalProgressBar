package com.android.tao.xhorizontalprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.android.tao.xhorizontalprogressbar.R;

/**
 * 圆角进度条
 * Created by 李建涛 on 2017/1/12.
 */

public class XHorizontalProgressBar extends View {

    private static final String TAG = XHorizontalProgressBar.class.getSimpleName();
    private float DEFAULT_RADIUS = 0f;
    /**
     * 控件默认的高度（宽度默认为全屏）
     * 单位为dp
     */
    private int DEFAULT_HEIGHT = 80;

    private Context mContext;
    private int mWidth, mHeight;// 整个的宽和高
    private int mProgressWidth;
    private int mProgressHeight;
    private float mProgressRadius;// 进度条圆角半径
    private float mProgressValue;//第二个值
    private float mProgressLeft = 5;
    private float mProgressTop = 5;
    private float mLevelLeft = 5;
    private float mLevelTop = 5;
    private boolean mIsRoundRect;
    private Paint mBackgroundPaint;
    private Paint mValuePaint;
    private int mDefaultBgColor = Color.GRAY;
    private int mDefaultValueColor = Color.BLUE;
    private int mProgressBackgroundColor;
    private int mValueColor = mDefaultValueColor;
    private float mDefaultProgressHeight = 30;//像素
    private int MAX = 100;
    private int mProgress;
    private int mLevelRadius = 20;
    private String mLevelText;
    private Paint mTextPaint;

    public XHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public XHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.XHorizontalProgressBar, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.XHorizontalProgressBar_progressColor:
                    mValueColor = typedArray.getColor(attr, mDefaultValueColor);
                    break;
                case R.styleable.XHorizontalProgressBar_backgroundColor:
                    mProgressBackgroundColor = typedArray.getColor(attr, mDefaultBgColor);
                    break;
                case R.styleable.XHorizontalProgressBar_isRoundRect:
                    mIsRoundRect = typedArray.getBoolean(attr, true);
                    break;
                case R.styleable.XHorizontalProgressBar_radius:
                    mProgressRadius = typedArray.getDimension(attr, DEFAULT_RADIUS);
                    break;
                case R.styleable.XHorizontalProgressBar_valueTextColor:

                    break;
                case R.styleable.XHorizontalProgressBar_showTextProgress:

                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    /**
     * set and get method begin
     */
    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int progress) {
        if (progress < 0 || progress > MAX) return;
        this.mProgress = progress;
        refreshUI();
    }


    public int getMAX() {
        return MAX;
    }

    public void setMAX(int max) {
        this.MAX = max;
        refreshUI();
    }

    public float getmProgressRadius() {
        return mProgressRadius;
    }

    public void setmProgressRadius(float mProgressRadius) {
        this.mProgressRadius = mProgressRadius;
        invalidate();
    }

    public int getmLevelRadius() {
        return mLevelRadius;
    }

    public void setmLevelRadius(int mLevelRadius) {
        this.mLevelRadius = mLevelRadius;
    }

    public String getmLevelText() {
        return mLevelText;
    }

    public void setmLevelText(String mLevelText) {
        this.mLevelText = mLevelText;
    }

    public boolean ismIsRoundRect() {
        return mIsRoundRect;
    }

    public void setmIsRoundRect(boolean mIsRoundRect) {
        this.mIsRoundRect = mIsRoundRect;
    }

    public int getProgressBackgroundColor() {
        return mProgressBackgroundColor;
    }

    public void setProgressBackgroundColor(int mBackgroundColor) {
        this.mProgressBackgroundColor = mBackgroundColor;
    }

    /** set and get end */

    /**
     * 初始化
     */
    private void init() {
        mProgressBackgroundColor = mDefaultBgColor;
        mValueColor = mDefaultValueColor;
        mIsRoundRect = true;
        mLevelText = "LV";
        mProgressHeight = 70;//默认
        mBackgroundPaint = new Paint();
        mValuePaint = new Paint();
        mTextPaint = new Paint();
        initPain(mBackgroundPaint);
        initPain(mValuePaint);
        initTextPaint(mTextPaint);
    }

    private void initTextPaint(Paint paint) {
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(35);
        paint.setColor(Color.WHITE);
    }

    /**
     * 初始化画笔
     *
     * @param paint
     */
    private void initPain(Paint paint) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);// 抗锯齿效果
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
        mLevelRadius = (mHeight - 10) / 2;
        mProgressLeft = mLevelLeft + mLevelRadius * 2;
        mProgressTop = mLevelTop + mLevelRadius - mProgressHeight / 2;
        mProgressWidth = mWidth - 10 - mLevelRadius * 2;//进度条的宽度
        mProgressRadius = mProgressHeight / 2;
        mProgressValue = mWidth / 4;//初始的进度条值
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        mBackgroundPaint.setColor(mDefaultBgColor);
        mValuePaint.setColor(mValueColor);
        drawRect(canvas, mBackgroundPaint, mProgressLeft - 5, mProgressTop, mProgressLeft + mProgressWidth, mProgressTop + mProgressHeight);//预留5个像素的边距
        drawRect(canvas, mValuePaint, mProgressLeft - 3, mProgressTop + 2, mProgressLeft + mProgressValue - 2, mProgressTop + mProgressHeight - 2);//相当于padding=2;
        drawLevelCircle(canvas, mValuePaint, mLevelLeft, mLevelTop, mLevelRadius);
        drawLevelText(canvas, mTextPaint, mLevelText);
    }

    /**
     * 画等级的字体
     */
    private void drawLevelText(Canvas canvas, Paint textPaint, String level) {
        // FontMetrics对象
        textPaint.setFakeBoldText(true);
        textPaint.measureText(level);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        float textWidth = textPaint.measureText(level, 0, level.length());//TODO 李建涛 待优化
        canvas.drawText(level, mLevelLeft + mLevelRadius - textWidth / 2, mLevelTop + mLevelRadius + offY / 2, textPaint);
    }

    /**
     * 画左边的等级图标
     */
    private void drawLevelCircle(Canvas canvas, Paint mValuePaint, float mLevelStart, float mLevelTop, int mLevelRadius) {
        canvas.drawCircle(mLevelStart + mLevelRadius, mLevelTop + mLevelRadius, mLevelRadius, mValuePaint);

    }


    /**
     * 重新绘制
     */
    private void refreshUI() {
        this.mProgressValue = (mProgress * mProgressWidth) / MAX;
        invalidate();
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

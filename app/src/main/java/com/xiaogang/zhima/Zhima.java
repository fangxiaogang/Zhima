package com.xiaogang.zhima;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaogang on 17/5/7.
 */

public class Zhima extends View {



    // 宽度
    private int width;

    // 高度
    private int height;

    // 半径
    private int radius;

    // 指针图片
    private Bitmap mBitmap;

    private Bitmap mBitmap2;

    // 指针图片宽度
    private int mBitmapWidth;

    // 指针图片高度
    private int mBitmapHeight;
    // 指针图片宽度
    private int mBitmapWidth2;

    // 指针图片高度
    private int mBitmapHeight2;

    // 最外层渐变圆环画笔
    private Paint mGradientRingPaint;

    // 大刻度画笔
    private Paint mSmallCalibrationPaint;

    // 小刻度画笔
    private Paint mBigCalibrationPaint;

    // 中间进度圆环画笔
    private Paint mMiddleRingPaint;

    // 内虚线圆环画笔
    private Paint mInnerRingPaint;
    private Paint mInnerRingPaint2;

    // 外层圆环文本画笔
    private Paint mTextPaint;

    // 中间进度圆环画笔
    private Paint mMiddleProgressPaint;

    // 指针图片画笔
    private Paint mPointerBitmapPaint;

    //中间文本画笔
    private Paint mCenterTextPaint;

    // 绘制内层圆环的矩形
    private RectF mInnerArc;

    // 绘制中间圆环的矩形
    private RectF mMiddleArc;

    // 中间进度圆环的值
    private float oval4;

    // 绘制中间进度圆环的矩形
    private RectF mMiddleProgressArc;

    // 圆环起始角度
    private static final float mStartAngle = 115f;

    // 指针全部进度
    private float mTotalAngle = 177f;

    // 指针当前进度
    private float mCurrentAngle = 0f;

    // View默认宽高值
    private int defaultSize;


    //信用等级
    private String sesameLevel = "信用极好";

    //评估时间
    private String evaluationTime = "";

    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;



    public Zhima(Context context) {
        super(context);
        init();
    }

    public Zhima(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Zhima(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        //设置默认宽高值
        defaultSize = dp2px(250);

        //设置图片线条的抗锯齿
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter
                (0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);



        //中间圆环画笔设置
        mMiddleRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleRingPaint.setStyle(Paint.Style.STROKE);
        mMiddleRingPaint.setStrokeCap(Paint.Cap.ROUND);
        mMiddleRingPaint.setStrokeWidth(5);
        mMiddleRingPaint.setColor(Color.GRAY);

        //内层圆环画笔设置
        mInnerRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerRingPaint.setStyle(Paint.Style.STROKE);
        mInnerRingPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerRingPaint.setStrokeWidth(4);
        mInnerRingPaint.setColor(Color.GRAY);
        PathEffect mPathEffect = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
        mInnerRingPaint.setPathEffect(mPathEffect);



        mInnerRingPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerRingPaint2.setStyle(Paint.Style.STROKE);
        mInnerRingPaint2.setStrokeCap(Paint.Cap.ROUND);
        mInnerRingPaint2.setStrokeWidth(4);
        mInnerRingPaint2.setColor(Color.WHITE);
        PathEffect mPathEffect2 = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
        mInnerRingPaint2.setPathEffect(mPathEffect2);
        //外层圆环文本画笔设置
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(30);

        //中间文字画笔设置
        mCenterTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setTextAlign(Paint.Align.CENTER);

        //中间圆环进度画笔设置
        mMiddleProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleProgressPaint.setColor(Color.WHITE);
        mMiddleProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mMiddleProgressPaint.setStrokeWidth(5);
        mMiddleProgressPaint.setStyle(Paint.Style.STROKE);

        //指针图片画笔
        mPointerBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerBitmapPaint.setColor(Color.GREEN);


        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhizhen);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();



        mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_circle);
        mBitmapHeight2 = mBitmap.getHeight();
        mBitmapWidth2 = mBitmap.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(resolveMeasure(widthMeasureSpec, defaultSize),
                resolveMeasure(heightMeasureSpec, defaultSize));
    }

    /**
     * 根据传入的值进行测量
     */
    public int resolveMeasure(int measureSpec, int defaultSize) {

        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {

            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;

            case MeasureSpec.AT_MOST:
                //设置warp_content时设置默认值
                result = Math.min(specSize, defaultSize);
                break;
            case MeasureSpec.EXACTLY:
                //设置math_parent 和设置了固定宽高值
                break;

            default:
                result = defaultSize;
        }

        return result;
    }
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //设置画布绘图无锯齿

        width = getWidth();
        height = getHeight();
        //圆环半径
        radius = width / 2;
         //中间和内层圆环
        float oval2 = radius * 5 / 8;
        float oval3 = radius * 3 / 4;
        //中间进度圆环
        oval4 = radius * 6 / 8;
        mMiddleProgressArc = new RectF(-oval4, -oval4, oval4, oval4);
        mInnerArc = new RectF(-oval2, -oval2, oval2, oval2);
        mMiddleArc = new RectF(-oval3, -oval3, oval3, oval3);

        canvas.save();
        canvas.translate(width / 2, height / 2);

        //画最外层的渐变圆环

        canvas.drawArc(mInnerArc, 155, 230, false, mInnerRingPaint);
        canvas.drawArc(mMiddleArc, 155, 230, false, mMiddleRingPaint);
        canvas.restore();



        drawCenterText(canvas);


        canvas.save();
        canvas.translate(radius, radius);
        canvas.rotate(270);
        canvas.drawArc(mMiddleProgressArc, -mStartAngle, mCurrentAngle, false, mMiddleProgressPaint);
        canvas.drawArc(mInnerArc, -mStartAngle, mCurrentAngle, false, mInnerRingPaint2);

        canvas.rotate(68 + mCurrentAngle);
        Matrix matrix = new Matrix();
        matrix.preTranslate(-oval4 + 80
                - mBitmapWidth * 3 / 8 , -mBitmapHeight / 2);
        canvas.drawBitmap(mBitmap, matrix, mPointerBitmapPaint);

        Matrix matrix2 = new Matrix();
        matrix2.preTranslate(-oval4 -29 - mBitmapWidth2 * 3 / 8, -mBitmapHeight2 / 2);
        canvas.drawBitmap(mBitmap2, matrix2, mPointerBitmapPaint);


        canvas.restore();
    }
    private void drawCenterText(Canvas canvas) {

        //绘制Logo
        mCenterTextPaint.setTextSize(20);
        mCenterTextPaint.setColor(Color.WHITE);
        canvas.drawText("BETA", radius, radius - 130, mCenterTextPaint);

        //绘制信用级别
        mCenterTextPaint.setColor(Color.WHITE);
        mCenterTextPaint.setTextSize(40);
        canvas.drawText(sesameLevel, radius, radius -50, mCenterTextPaint);

        //绘制信用分数
        mCenterTextPaint.setColor(Color.WHITE);
        mCenterTextPaint.setTextSize(100);
        mCenterTextPaint.setStyle(Paint.Style.STROKE);
        canvas.drawText(String.valueOf(740), radius, radius + 70, mCenterTextPaint);

        //绘制评估时间
        evaluationTime = "评估时间:" + getCurrentTime();
        mCenterTextPaint.setColor(Color.WHITE);
        mCenterTextPaint.setTextSize(25);
        canvas.drawText(evaluationTime, radius, radius + 120, mCenterTextPaint);
    }

    /**
     * dp2px
     */
    public int dp2px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

    /**
     * 开始指针旋转动画
     */
    public void startRotateAnim() {

        ValueAnimator mAngleAnim = ValueAnimator.ofFloat(mCurrentAngle, mTotalAngle);
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(3000);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mCurrentAngle = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mAngleAnim.start();

    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
        return format.format(new Date());
    }
}

package com.edwin.edwinlibrary.baseView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.edwin.edwinlibrary.R;
import com.edwin.edwinlibrary.entity.FundMode;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by hongy_000 on 2017/12/6.
 *
 * @author hongy_000
 */

public class FundCurveView extends View {
    private final static String TAG = FundCurveView.class.getSimpleName();
    //控件默认宽高
    private static final float DEF_WIDTH = 650;
    private static final float DEF_HEIGHT = 400;
    //数据源
    List<FundMode> mFundModeList;
    //控件宽高
    private int mWidth;
    private int mHeight;
    //上下左右padding
    private float leftPadding = 50;
    private float topPadding = 100;
    private float rightPadding = 50;
    private float bottomPadding = 70;

    //Y轴对应的最大值和最小值,注意，这里存的是对象
    private FundMode mMinFundMode;
    private FundMode mMaxFundMode;
    //X、Y轴每一个data对应的大小
    private float mPerX;
    private float mPerY;
    //正在加载中
    private Paint mLoadingPaint;
    private final static float LOADING_TEXT_SIZE = 20;
    private final static String LOADING_TIPS = "数据正在加载中，请稍后...";
    private boolean isLoadingPaint = true;

    // X、Y周刻度值画笔
    private Paint mXYPaint;

    private static final float M_XY_TEXT_SIZE = 14;
    // Y轴刻度值距离左侧边界的距离
    private final float leftTextPadding = 16;
    // X轴刻度值距离底部边界的距离
    private final float bottomTextPadding = 20;

    // 虚线画笔
    private Paint dashPaint;
    private final static float DASH_STROKE_WIDTH = 1;

    //折线画笔
    private Paint mBrokenLinePaint;
    private static final float M_BROKEN_WIDTH = 1;

    //长按的十字线
    private Paint mLongPressPaint;
    private boolean isLongPressPaint = false;
    //长按处理
    private long mPressTime;
    //默认多长时间算长按
    private final static long DEF_LONG_PRESS_INTERVAL = 700;
    private float mPressX;
    private float mPressY;

    //长按情况下X、Y轴要显示的文字
    private Paint mLongPressTextPaint;
    private static final float LONG_PRESS_FONT_SIZE = 20;

    //最上面默认显示累计收益金额
    private Paint mDefaultPaint;
    private final static float DEF_FONT_SIZE = 20;

    public FundCurveView(Context context) {
        this(context, null);
    }

    public FundCurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FundCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, (int) DEF_HEIGHT);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) DEF_HEIGHT);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //默认加载Loading界面
        drawLoadingSegment(canvas);
        if (mFundModeList == null || mFundModeList.size() == 0){
            return;
        }

        drawXAxisDashLineSegment(canvas);
        drawBrokenCurveSegment(canvas);
        drawXYCalibrationSegment(canvas);

        drawTopTxtPaintSegment(canvas);

        drawLongPressCrossCursorSegment(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressTime = event.getDownTime();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getEventTime() - mPressTime > DEF_LONG_PRESS_INTERVAL) {
                    Log.d(TAG, "长按了（" + event.getX() + "," + event.getY() + ")");
                    mPressX = event.getX();
                    mPressY = event.getY();
                    //长按后的逻辑
                    showLongPressSegment();
                }
                break;
            case MotionEvent.ACTION_UP:
                //处理松手后的逻辑
                hiddenLongPressSegment();
                break;
            default:
                break;
        }
        return true;
    }

    private void initAttrs() {
        initLoadingPaint();

        initXDashLinePaint();
        initXYPaint();
        initBrokenPaint();

        initLongPressPaint();
        initTopTxtPaints();
    }

    /**
     * 初始化正在加载画笔
     */
    private void initLoadingPaint() {
        mLoadingPaint = new Paint();
        mLoadingPaint.setColor(getResources().getColor(R.color.color_fundView_xyTxtColor));
        mLoadingPaint.setTextSize(LOADING_TEXT_SIZE);
        mLoadingPaint.setAntiAlias(true);
    }

    private void drawLoadingSegment(Canvas canvas) {
        if (!isLoadingPaint) {
            return;
        }
        //这里特别注意，x轴的起始点要减去文字宽度的一半
        canvas.drawText(LOADING_TIPS, mWidth / 2 - mLoadingPaint.measureText(LOADING_TIPS) / 2,
                mHeight / 2, mLoadingPaint);
    }

    // 只需要把画笔颜色置为透明即可
    private void hiddenLoadingSegment() {
        mLoadingPaint.setColor(0x00000000);
        isLoadingPaint = false;
    }

    /**
     * 初始化长按画笔，绘制线条
     */
    private void initLongPressPaint() {
        mLongPressPaint = new Paint();
        mLongPressPaint.setColor(getResources().getColor(R.color.color_fundView_longPressLineColor));
        mLongPressPaint.setStyle(Paint.Style.FILL);
        mLongPressPaint.setAntiAlias(true);
        mLongPressPaint.setTextSize(LONG_PRESS_FONT_SIZE);
    }

    //折线上面显示文字信息
    private void initTopTxtPaints() {
        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(getResources().getColor(R.color.color_fundView_defIncomeTxt));
        mDefaultPaint.setTextSize(LONG_PRESS_FONT_SIZE);
        mDefaultPaint.setAntiAlias(true);

        mLongPressTextPaint = new Paint();
        mLongPressTextPaint.setColor(getResources().getColor(R.color.color_fundView_longPressLineColor));
        mLongPressTextPaint.setTextSize(LONG_PRESS_FONT_SIZE);
        mLongPressTextPaint.setAntiAlias(true);
    }

    private void drawTopTxtPaintSegment(Canvas canvas) {
        //先画默认情况下的top文字
        //画默认情况下前面的蓝色小圆点
        Paint blueDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blueDotPaint.setColor(getResources().getColor(R.color.color_fundView_brokenLineColor));
        float r = 6;
        blueDotPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(leftPadding + r / 2, topPadding / 2 + r, r, blueDotPaint);

        float fontHeight = getFontHeight(DEF_FONT_SIZE, mDefaultPaint);

        //先画hint文字
        Paint hintPaint = new Paint();
        hintPaint.setColor(getResources().getColor(R.color.color_fundView_xyTxtColor));
        hintPaint.setAntiAlias(true);
        hintPaint.setTextSize(DEF_FONT_SIZE);
        String hintTxt = getResources().getString(R.string.string_fundView_defHintTxt);

        canvas.drawText(hintTxt, leftPadding + r + 10, topPadding / 2 + fontHeight / 2,
                mDefaultPaint);

        if (mFundModeList == null || mFundModeList.isEmpty()){
            return;
        }
        canvas.drawText(mFundModeList.get(mFundModeList.size() - 1).getDataY() + "",
                leftPadding + r + 10 + hintPaint.measureText(getResources().getString(R.string.string_fundView_defHintTxt)) + 5,
                topPadding / 2 + fontHeight / 2, mDefaultPaint);
    }

    /**
     * 这里处理画十字的逻辑:这里的十字不是手指按下的位置，这样没有意义。
     * 而是当前按下的距离x轴最近的时间（注意：并不一定按下对应的x轴就是有时间的，如果没有取最近的）。
     * 当取到x轴的值，之后算出来对应的y轴的值，这个才是十字对应的位置坐标。
     * 如何获取x轴最近的时间？我们可以在FundMode中定义x\y的位置参数，遍历对比找到最小即可。
     * (see: drawBrokenPaint(canvas);)
     *
     * @param canvas
     */
    private void drawLongPressCrossCursorSegment(Canvas canvas) {
        if (!isLongPressPaint) {
            return;
        }
        //获取距离最近按下的位置的model
        float pressX = mPressX;
        //循环遍历找到距离最短的x轴的Mode
        FundMode finalMode = mFundModeList.get(0);
        float minXLen = Integer.MAX_VALUE;
        for (int i = 0; i< mFundModeList.size(); i++) {
            FundMode f = mFundModeList.get(i);
            float abs = Math.abs(pressX - f.getFloatX());
            if (abs < minXLen) {
                finalMode = f;
                minXLen = abs;
            }
        }
        //x
        canvas.drawLine(leftPadding, finalMode.getFloatY(), mWidth - rightPadding, finalMode.getFloatY(), mLongPressPaint);
        //y
        canvas.drawLine(finalMode.getFloatX(), topPadding, finalMode.getFloatX(), mHeight - bottomPadding, mLongPressPaint);

        //处理按下之后Top的文字信息
        //先画背景
        drawTopTextBg(canvas);

        drawTopText(canvas,finalMode);

    }

    private void drawTopText(Canvas canvas, FundMode fundMode) {
        float high = topPadding - 30;
        //按下后左边的日期文字
        Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setColor(getResources().getColor(R.color.color_fundView_pressIncomeTxt));
        timePaint.setTextSize(LONG_PRESS_FONT_SIZE);
        canvas.drawText(processDateTime(fundMode.getDatetime()) + "",10,
                high/2 + getFontHeight(LONG_PRESS_FONT_SIZE, timePaint) / 2, timePaint);
        //按下后右边的红色收益文字
        canvas.drawText(fundMode.getDataY() + "",
                mWidth - rightPadding - mLongPressPaint.measureText(fundMode.getDataY() + ""),
                high / 2 + getFontHeight(LONG_PRESS_FONT_SIZE,timePaint) / 2, mLongPressPaint);

        //按下后的提示文字
        Paint hintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hintPaint.setTextSize(LONG_PRESS_FONT_SIZE);
        hintPaint.setColor(getResources().getColor(R.color.color_fundView_xyTxtColor));
        canvas.drawText(getResources().getString(R.string.string_fundView_pressHintTxt),
                mWidth - rightPadding - mLongPressPaint.measureText(fundMode.getDataY() + "")
                        - hintPaint.measureText(getResources().getString(R.string.string_fundView_pressHintTxt)),
                high / 2 + getFontHeight(LONG_PRESS_FONT_SIZE, timePaint) / 2, hintPaint);
    }

    private void drawTopTextBg(Canvas canvas) {
        float high = topPadding - 30;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(getResources().getColor(R.color.color_fundView_pressIncomeTxtBg));
        canvas.drawRect(0,0,mWidth,high,bgPaint);
    }

    /**
     * 初始化折线画笔
     */
    private void initBrokenPaint() {
        mBrokenLinePaint = new Paint();
        mBrokenLinePaint.setColor(getResources().getColor(R.color.color_fundView_brokenLineColor));
        mBrokenLinePaint.setStyle(Paint.Style.STROKE);
        mBrokenLinePaint.setAntiAlias(true);
        mBrokenLinePaint.setStrokeWidth(convertDp2Px(M_BROKEN_WIDTH));
    }

    private void drawBrokenCurveSegment(Canvas canvas) {
        //先画第一个点
        FundMode firstFundMode = mFundModeList.get(0);
        Path path = new Path();
        //这里需要说明一下，x轴的起始点，其实需要加上mPerX，但是加上之后不是从起始位置开始，不好看。
        //同理，for循环内x轴其实需要(i+1)。现在这样处理，最后会留一点空隙，其实挺好看的。
        firstFundMode.setFloatX(leftPadding);
        float firstFloatY = mHeight - bottomPadding - mPerY * (firstFundMode.getDataY() - mMinFundMode.getDataY());
        firstFundMode.setFloatY(firstFloatY);
        path.moveTo(leftPadding, firstFloatY);
        for (int i = 1; i < mFundModeList.size(); i++) {
            FundMode fundMode = mFundModeList.get(i);
            float floatXTemp = leftPadding + i * mPerX;
            float floatYTemp = mHeight - bottomPadding - mPerY * (fundMode.getDataY() - mMinFundMode.getDataY());
            fundMode.setFloatX(floatXTemp);
            fundMode.setFloatY(floatYTemp);
            path.lineTo(floatXTemp, floatYTemp);
        }
        canvas.drawPath(path, mBrokenLinePaint);
    }

    /**
     * 初始化X、Y轴刻度画笔
     */
    private void initXYPaint() {
        mXYPaint = new Paint();
        mXYPaint.setColor(getResources().getColor(R.color.color_fundView_xyTxtColor));
        mXYPaint.setTextSize(M_XY_TEXT_SIZE);
        mXYPaint.setAntiAlias(true);
    }

    private void drawXYCalibrationSegment(Canvas canvas) {
        drawYAxisCalibration(canvas);
        drawXAxisCalibration(canvas);

    }

    private void drawXAxisCalibration(Canvas canvas) {
        long beginTime = mFundModeList.get(0).getDatetime();
        long midTime = mFundModeList.get((mFundModeList.size() - 1) / 2).getDatetime();
        long endTime = mFundModeList.get(mFundModeList.size() - 1).getDatetime();
        String begin = processDateTime(beginTime);
        String mid = processDateTime(midTime);
        String end = processDateTime(endTime);

        // x轴文字的高度
        float xHeight = mHeight - bottomPadding + bottomTextPadding;
        canvas.drawText(begin, leftPadding, xHeight, mXYPaint);
        canvas.drawText(mid, (mWidth - leftPadding - rightPadding) / 2, xHeight, mXYPaint);
        //特别注意x轴的处理：- mXYPaint.measureText(end)
        canvas.drawText(end, (mWidth - rightPadding - mXYPaint.measureText(end)), xHeight, mXYPaint);
    }

    private void drawYAxisCalibration(Canvas canvas) {
        float textWidth = mXYPaint.measureText(mMinFundMode.getOriginDataY()) + leftTextPadding;
        //drawMin
        canvas.drawText(mMinFundMode.getOriginDataY() + "",
                leftPadding - textWidth, mHeight - bottomPadding, mXYPaint);
        //drawMax
        canvas.drawText(mMaxFundMode.getDataY() + "",
                leftPadding - textWidth, topPadding, mXYPaint);

        //因为横线部分是均分的，所以只需要取到最大值和最小值的差值，均分即可
        float perYData = (mMaxFundMode.getDataY() - mMinFundMode.getDataY()) / 4;
        float perYWidth = (mHeight - topPadding - bottomPadding) / 4;
        //从下往上画
        for (int i = 1; i < 4; i++) {
            canvas.drawText(mMinFundMode.getDataY() + perYData * i + "",
                    leftPadding - textWidth,
                    mHeight - bottomPadding - i * perYWidth,
                    mXYPaint);
        }
    }

    /**
     * 初始化虚线画笔
     */
    private void initXDashLinePaint() {
        dashPaint = new Paint();
        dashPaint.setColor(getResources().getColor(R.color.color_fundView_xLineColor));
        dashPaint.setStrokeWidth(convertDp2Px(DASH_STROKE_WIDTH));
        dashPaint.setStyle(Paint.Style.STROKE);
        setLayerType(LAYER_TYPE_SOFTWARE, null);//禁用硬件加速
        PathEffect effect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dashPaint.setPathEffect(effect);
    }

    /**
     * 画五条横轴曲线，首先确定最大值和最小值的位置
     *
     * @param canvas
     */
    private void drawXAxisDashLineSegment(Canvas canvas) {

        float perHeight = (mHeight - bottomPadding - topPadding) / 4;
        canvas.drawLine(0 + leftPadding, 0 + topPadding, mWidth - rightPadding, 0 + topPadding, dashPaint);
        canvas.drawLine(0 + leftPadding, 0 + topPadding + perHeight * 1, mWidth - rightPadding, 0 + topPadding + perHeight * 1, dashPaint);
        canvas.drawLine(0 + leftPadding, 0 + topPadding + perHeight * 2, mWidth - rightPadding, 0 + topPadding + perHeight * 2, dashPaint);
        canvas.drawLine(0 + leftPadding, 0 + topPadding + perHeight * 3, mWidth - rightPadding, 0 + topPadding + perHeight * 3, dashPaint);
        canvas.drawLine(0 + leftPadding, 0 + mHeight - bottomPadding, mWidth - rightPadding, 0 + mHeight - bottomPadding, dashPaint);

    }

    private void showLongPressSegment() {
        isLongPressPaint = true;
        invalidate();
    }

    private void hiddenLongPressSegment() {
        //实现蚂蚁金服延迟消失十字线
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isLongPressPaint = false;
                invalidate();
            }
        }, 1000);
    }

    private String processDateTime(long time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    private float convertDp2Px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public float getFontHeight(float fontSize, Paint paint) {
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) (Math.ceil(fm.descent - fm.top) + 2);
    }

    /**
     * 程序入口，设置数据
     */
    public void setDataList(List<FundMode> fundModeList) {
        if (fundModeList == null || fundModeList.size() == 0) return;
        this.mFundModeList = fundModeList;

        //开始获取最大值最小值；单个数据尺寸等
        mMinFundMode = fundModeList.get(0);
        mMaxFundMode = fundModeList.get(0);
        for (FundMode fundMode : fundModeList) {
            if (fundMode.getDataY() < mMinFundMode.getDataY()) {
                mMinFundMode = fundMode;
            } else if (fundMode.getDataY() > mMaxFundMode.getDataY()) {
                mMaxFundMode = fundMode;
            }
        }
        if (mMaxFundMode.equals(mMinFundMode)) {
            return;
        }
        //获取单个数据X/y轴的大小
        mPerX = (mWidth - leftPadding - rightPadding) / fundModeList.size();
        mPerY = ((mHeight - topPadding - bottomPadding) / (mMaxFundMode.getDataY() - mMinFundMode.getDataY()));
        Log.e(TAG, "setDataList: " + mMinFundMode + "," + mMaxFundMode + "..." + mPerX + "," + mPerY);

        //数据过来，隐藏加载更多
        hiddenLoadingSegment();

        //刷新界面
        invalidate();
    }
}

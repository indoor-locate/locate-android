package com.feifan.debuglib.window;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feifan.debuglib.R;

/**
 *
 * Created by xuchunlei on 2016/12/27.
 */

class DebugView extends RelativeLayout {

    @IdRes
    private static final int ID_CLOSE = 1;
//    @IdRes
//    private static final int ID_SCROLL = 2;
//    @IdRes
//    private static final int ID_LOG = 3;

    private static final ForegroundColorSpan SPAN_ERROR = new ForegroundColorSpan(Color.RED);
    private static final ForegroundColorSpan SPAN_INFO = new ForegroundColorSpan(Color.WHITE);

    // log level
    public static final int LOG_LEVEL_VERBOSE = 1;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_WARN = 4;
    public static final int LOG_LEVEL_ERROR = 5;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    // log
//    private ScrollView mScrollV;
//    private TextView mLogV;
    private Handler mHandler;
    private SpannableStringBuilder mCache = new SpannableStringBuilder();
    private static final int LOG_CACHE_SIZE = 1024;
    private static final float LOG_FREE_PERCENT = 0.2f;

    // touch
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;

    public DebugView(Context context) {
        super(context);

        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        setBackgroundColor(Color.parseColor("#64000000"));

        // size
        Point windowDimen = new Point();
        mWindowManager.getDefaultDisplay().getSize(windowDimen);
        int height = windowDimen.y / 4;

        // config
        mParams = new WindowManager.LayoutParams();
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mParams.height = height;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;  // 避免主界面失去焦点  | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        mParams.format = PixelFormat.TRANSLUCENT; // 支持半透明效果
        mParams.gravity = Gravity.LEFT | Gravity.TOP;

        // close
        ImageView closeImg = new ImageView(context);
        closeImg.setId(ID_CLOSE);
        closeImg.setImageResource(R.mipmap.ic_close);
        LayoutParams closeParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        closeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        closeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeImg.setLayoutParams(closeParams);
        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        addView(closeImg);

        // log
        final ScrollView logScroll = new ScrollView(context);
//        mScrollV.setId(ID_SCROLL);
        LayoutParams scrollParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        scrollParams.addRule(RelativeLayout.BELOW, ID_CLOSE);
        logScroll.setLayoutParams(scrollParams);

        final TextView logV = new TextView(context);
        logV.setTextColor(Color.WHITE);
//        mLogV.setId(ID_LOG);
        LayoutParams logParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        logV.setLayoutParams(logParams);
        logScroll.addView(logV);

        addView(logScroll);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                synchronized (DebugView.this) {
                    validateAndTrim(mCache);
                    logV.setText(mCache);
                    logScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        };

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY()-25;   //25是系统状态栏的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX =  event.getX();
                mTouchStartY =  event.getY();
                Log.i("startP","startX"+mTouchStartX+"====startY"+mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE:   //捕获手指触摸移动动作
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
                updateViewPosition();
                mTouchStartX=mTouchStartY=0;
                break;
        }
        return true;
    }

    private void updateViewPosition(){
        //更新浮动窗口位置参数
        mParams.x = (int)( x-mTouchStartX);
        mParams.y = (int) (y-mTouchStartY);
        mWindowManager.updateViewLayout(this, mParams);  //刷新显示
    }

    public void show() {
        mWindowManager.addView(this, mParams);
    }

    public void hide() {
        if(isAttachedToWindow()) {
            mWindowManager.removeView(this);
        }
    }

    public synchronized void addLog(int level, String log) {
        log += "\n";
        mCache.append(log);
        final int length = mCache.length();
        switch (level) {
            case LOG_LEVEL_VERBOSE:
                break;
            case LOG_LEVEL_DEBUG:
                break;
            case LOG_LEVEL_INFO:
                ForegroundColorSpan spanInfo = new ForegroundColorSpan(Color.GREEN);
                mCache.setSpan(spanInfo, length - log.length(), length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case LOG_LEVEL_WARN:
                break;
            case LOG_LEVEL_ERROR:
                ForegroundColorSpan spanError = new ForegroundColorSpan(Color.RED);
                mCache.setSpan(spanError, length - log.length(), length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }

        mHandler.sendEmptyMessage(0);
    }

    private synchronized void validateAndTrim(SpannableStringBuilder builder) {
        if(builder.length() > LOG_CACHE_SIZE) {
            int delLength = (int)(builder.length() * LOG_FREE_PERCENT);
            int endIndex = builder.toString().substring(0, delLength).lastIndexOf("\n");
            if(endIndex != -1) {
                int length = builder.length();
                builder.delete(0, endIndex);
                Log.e("DebugView", "trim happened size:" + length + "-->" + builder.length());
            }else {
                Log.w("DebugView", "we didn't find 'n' from " + LOG_FREE_PERCENT + " text");
            }

        }else {
            Log.w("DebugView", "size-->" + builder.length());
        }
    }

}

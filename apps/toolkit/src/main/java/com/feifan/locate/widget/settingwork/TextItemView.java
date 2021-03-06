package com.feifan.locate.widget.settingwork;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.locate.R;

/**
 * 设置项视图
 * Created by bianying on 2016/10/7.
 */

public class TextItemView extends RelativeLayout {

    @IdRes
    private static final int ID_TITLE = 1;

    @IdRes
    private static final int ID_SUBTITLE = 2;

    private TextView mTitleV;
    private TextView mSubTitleV;

    public TextItemView(Context context) {
        this(context, null, 0);
    }

    public TextItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    public void setSubtitle(String title) {
        mSubTitleV.setText(title);
    }

    private void initViews(Context context, AttributeSet attrs) {
        if(attrs == null) {
            throw new IllegalStateException("you should set attribs for item");
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);

        // 图标和标题
        mTitleV = new TextView(context);
        mTitleV.setId(ID_TITLE);
        mTitleV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.setting_item_title_size));
        mTitleV.setTextColor(ContextCompat.getColor(context, R.color.setting_item_title_text));
        if(a.hasValue(R.styleable.SettingItem_settingTitle)) {
            mTitleV.setText(a.getResourceId(R.styleable.SettingItem_settingTitle, -1));
        }
        LayoutParams paramsTitle = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(mTitleV, paramsTitle);

        // 箭头和副标题
        mSubTitleV = new TextView(context);
        mSubTitleV.setId(ID_SUBTITLE);
        mSubTitleV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.setting_item_subtitle_size));
        mSubTitleV.setTextColor(ContextCompat.getColor(context, R.color.setting_item_subtitle_text));
        mSubTitleV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.setting_item_arrow, 0);
        mSubTitleV.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.setting_item_drawable_padding));
        if(a.hasValue(R.styleable.SettingItem_showSubTitle)) {
            mSubTitleV.setText(a.getResourceId(R.styleable.SettingItem_showSubTitle, -1));
        }
        mSubTitleV.setPadding(0, 0, 0, 0);
        LayoutParams paramsSubTitle = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsSubTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsSubTitle.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(mSubTitleV, paramsSubTitle);

        a.recycle();
    }

    public String getTitle() {
        return mTitleV.getText().toString();
    }

    public String getSubTitle() {
        return mSubTitleV.getText().toString();
    }
}

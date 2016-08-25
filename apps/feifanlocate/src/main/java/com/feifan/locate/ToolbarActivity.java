package com.feifan.locate;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.feifan.locate.sampling.SpotPlanFragment;
import com.feifan.locate.widget.ui.BaseActivity;

public class ToolbarActivity extends BaseActivity {

    public static final String EXTRA_KEY_FRAGMENT = "fragment";
    public static final String EXTRA_KEY_ARGUMENTS = "arguments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolbar = findView(R.id.toolbar_title);
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 隐藏底部导航菜单
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        // 加载界面
        String fragmentName = getIntent().getStringExtra(EXTRA_KEY_FRAGMENT);
        Bundle fragmentArgs = getIntent().getBundleExtra(EXTRA_KEY_ARGUMENTS);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.toolbar_content, Fragment.instantiate(this, fragmentName, fragmentArgs))
                .commitAllowingStateLoss();
    }
}
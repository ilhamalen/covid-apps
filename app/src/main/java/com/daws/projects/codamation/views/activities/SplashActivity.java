package com.daws.projects.codamation.views.activities;

import android.content.Intent;
import android.os.Handler;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.databinding.ActivitySplashBinding;
import com.daws.projects.codamation.utils.ActivityUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected int attachLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        ActivityUtils.setLightIconStatusBar(getWindow());

        new Handler().postDelayed(() ->
                startActivity(new Intent(SplashActivity.this, HomeActivity.class))
                , 3000);
    }
}

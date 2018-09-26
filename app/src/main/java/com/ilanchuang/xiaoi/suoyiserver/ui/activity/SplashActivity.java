package com.ilanchuang.xiaoi.suoyiserver.ui.activity;

import android.view.View;

import com.github.florent37.viewanimator.ViewAnimator;
import com.ilanchuang.xiaoi.suoyiserver.R;

import top.jplayer.baseprolibrary.ui.activity.SuperBaseActivity;
import top.jplayer.baseprolibrary.utils.ActivityUtils;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class SplashActivity extends SuperBaseActivity {

    @Override
    protected int initRootLayout() {
        return R.layout.activity_splash;
    }


    @Override
    public void initRootData(View view) {
        super.initRootData(view);
        ViewAnimator.animate(view.findViewById(R.id.image))
                .scale(1f, 1.1f)
                .duration(1000)
                .onStop(() -> {
                    ActivityUtils.init().start(this, LoginActivity.class);
                    finish();
                })
                .start();

    }
}

package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.beilei.zz.dq.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ZengHao on 2016/9/18 0018.
 */

public class JoinUsActivity extends Activity {

    @InjectView(R.id.back)
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.back, R.id.tv_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }
}

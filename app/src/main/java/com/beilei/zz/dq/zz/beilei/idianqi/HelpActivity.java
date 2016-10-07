package com.beilei.zz.dq.zz.beilei.idianqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.beilei.zz.dq.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HelpActivity extends AppCompatActivity {

    @InjectView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}

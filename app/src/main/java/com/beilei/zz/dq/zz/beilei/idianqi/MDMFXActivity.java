package com.beilei.zz.dq.zz.beilei.idianqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MDMFXActivity extends AppCompatActivity {


    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.id_ewm)
    ImageView ewm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdmfx);
        ButterKnife.inject(this);
        Picasso.with(this).load(R.drawable.download).into(ewm);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}

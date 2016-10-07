package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.EditCheckUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.ResultBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.beilei.zz.dq.zz.beilei.idianqi.DuiHuanActivity.showToast;

/**
 * Created by ZengHao on 2016/9/21 0021.
 */

public class AddBankCard extends Activity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.et_bank)
    EditText etBank;
    @InjectView(R.id.et_bank_num)
    EditText etBankNum;
    @InjectView(R.id.et_bank_name)
    EditText etBankName;
    @InjectView(R.id.btn_ok)
    Button btnOk;
    private String nameOfBank;
    private Request<String> request;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bankcard);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.back, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_ok:
                String bank = etBank.getText().toString().trim(); //所属银行
                String bankname = etBankName.getText().toString().trim();//姓名
                String banknum = etBankNum.getText().toString().trim();//卡号

                Toast.makeText(AddBankCard.this,nameOfBank,Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(bank) || TextUtils.isEmpty(bankname) || TextUtils.isEmpty(banknum)) {
                    showToast(AddBankCard.this, "请输入银行卡信息！", Toast.LENGTH_SHORT);
                } else {
                    if (EditCheckUtil.checkBankCard(banknum)) {
                        Intent intent = new Intent();
                        intent.putExtra("bank", bank);
                        intent.putExtra("bankname", bankname);

                        intent.putExtra("banknum", banknum);
                        AddBankCard.this.setResult(RESULT_OK, intent);
                        queue = NoHttp.newRequestQueue();
                        request = NoHttp.createStringRequest(Url.add, RequestMethod.POST);
                        request.add("bank",bank);
                        request.add("name",bankname);
                        request.add("bank_num",banknum);
                        request.add("phone",getSharedPreferences("config",MODE_PRIVATE).getString("phone",""));
                        queue.add(456, request, new OnResponseListener<String>() {

                            private int result;
                            private ResultBean resultBean;
                            private String json;

                            @Override
                            public void onStart(int what) {

                            }

                            @Override
                            public void onSucceed(int what, Response<String> response) {

                                json = response.get();
                                resultBean = new Gson().fromJson(json, ResultBean.class);
                                result = resultBean.result;
                                if (result == 1) {
                                    com.beilei.zz.dq.Utils.Toast.showMessage(AddBankCard.this,"修改成功");
                                }else{
                                    com.beilei.zz.dq.Utils.Toast.showMessage(AddBankCard.this,"添加成功");
                                }
                                AddBankCard.this.finish();
                            }

                            @Override
                            public void onFailed(int what, Response<String> response) {

                            }

                            @Override
                            public void onFinish(int what) {

                            }
                        });

                    } else {
                        showToast(AddBankCard.this, "银行卡号有误！", Toast.LENGTH_SHORT);

                    }
                }
                break;
        }
    }
}

package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.BankCardBean;
import com.beilei.zz.dq.bean.JiFenBean;
import com.beilei.zz.dq.bean.ResultBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ZengHao on 2016/9/21 0021.
 */

public class DuiHuanActivity extends Activity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.tv_bank)
    TextView tvBank;
    @InjectView(R.id.ll_yh)
    RelativeLayout llYh;
    @InjectView(R.id.et_jf)
    EditText etJf;
    @InjectView(R.id.tv_je)
    TextView tvJe;
    @InjectView(R.id.btn_duihuan)
    TextView btnDuihuan;
    private String bankname;
    private String banknum;
    private RequestQueue queue;
    private Request<String> request;
    private String phone;
    private String jifen;
    private JiFenBean jiFenBean;
    private Integer anInt;
    private int huilv;
    private String exchange;
    private int intJifen;
    private String duihuanjifen;
    private int je;
    private String money;
    private int intmoney;
    private DialogWidget mDialogWidget;
    private TextView payTextView;
    private String etStr;
    private String bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_duihuan);
        ButterKnife.inject(this);
        getBankCard();
        getInfo();


        tvBank.setText("点击添加银行卡");

        etJf.setText(jifen);
//        tvJe.setText(Integer.parseInt(jifen)/Integer.parseInt(exchange) + "元");
        etJf.addTextChangedListener(watcher); //添加按键捕捉

    }

    private TextWatcher watcher = new TextWatcher() {


        private int a;

        @Override

        public void afterTextChanged(Editable s) {


        }


        @Override

        public void beforeTextChanged(CharSequence s, int start, int count,

                                      int after) {

            // TODO Auto-generated method stub

        }


        @Override

        public void onTextChanged(CharSequence s, int start, int before,

                                  int count) {

            // TODO Auto-generated method stub

            String etStr = etJf.getText().toString().trim();//获得输入的值
            if (etStr.length() <= 0) {
                Toast.makeText(DuiHuanActivity.this, "请输入您想兑换的积分", Toast.LENGTH_SHORT).show();
            } else {

                anInt = Integer.parseInt(etStr);
//                if (anInt>intJifen) {
//
//                }
            }

            huilv = Integer.parseInt(exchange);
            intmoney = Integer.parseInt(money);
            a = huilv * intmoney;

            je = anInt / huilv;
            tvJe.setText(je + "元"); //输出


        }
    };


    @OnClick({R.id.back, R.id.ll_yh, R.id.btn_duihuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_yh://点击添加银行卡
                startActivityForResult(new Intent(this, AddBankCard.class), 9999);
                break;
            case R.id.btn_duihuan:

                //获得输入的值
                etStr = etJf.getText().toString().trim();

                if (etStr.length() == 0) {
                    showToast(this, "请输入要兑换的积分", Toast.LENGTH_SHORT);
                    return;
                } else {
                    anInt = Integer.parseInt(etStr);
                }
                if (anInt == 0) {
                    showToast(this, "请输入要兑换的积分", Toast.LENGTH_SHORT);
                    return;
                } else {

                    huilv = Integer.parseInt(exchange);
                    intmoney = Integer.parseInt(money);//最少兑换money 元
                    int a = huilv * intmoney;
                    if (anInt > intJifen) {
//                    Toast.showMessage(this,"兑换积分超限！");
                        showToast(DuiHuanActivity.this, "兑换积分超限！", Toast.LENGTH_SHORT);
                        return;
                    } else if (anInt < a) {
//                    com.beilei.zz.dq.Utils.Toast.showMessage(this,"兑换积分必须大于"+a+"积分");
                        showToast(DuiHuanActivity.this, "兑换积分必须大于" + a + "积分", Toast.LENGTH_SHORT);
                        return;
                    } else {
                        if (TextUtils.isEmpty(bank) || TextUtils.isEmpty(bankname) || TextUtils.isEmpty(banknum)) {
                            showToast(DuiHuanActivity.this, "兑换换RMB请添加银行卡", Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                    //t弹出密码框
                    mDialogWidget = new DialogWidget(DuiHuanActivity.this, getDecorViewDialog());
                    mDialogWidget.show();
                }


                break;
        }
    }

    private void getInfo() {
        phone = getSharedPreferences("config", MODE_PRIVATE).getString("phone", null);
        queue = NoHttp.newRequestQueue();
        request = NoHttp.createStringRequest(Url.jifen, RequestMethod.POST);
        request.add("phone", phone);
        queue.add(9, request, new OnResponseListener<String>() {


            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                jiFenBean = new Gson().fromJson(response.get(), JiFenBean.class);

                jifen = jiFenBean.integral;
                intJifen = Integer.parseInt(jifen);
                exchange = jiFenBean.exchange;
                etJf.setHint("本次最高兑换" + jifen + "积分");
                money = jiFenBean.money;

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void getBankCard() {
        phone = getSharedPreferences("config", MODE_PRIVATE).getString("phone", null);
        queue = NoHttp.newRequestQueue();
        request = NoHttp.createStringRequest(Url.mybank, RequestMethod.POST);
        request.add("phone", phone);
        queue.add(9, request, new OnResponseListener<String>() {



            private List<BankCardBean.DataBean> data;

            private BankCardBean bankCardBean;

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                bankCardBean = new Gson().fromJson(response.get(), BankCardBean.class);
                data = bankCardBean.data;
                if (data == null) {
                    Toast.makeText(DuiHuanActivity.this,"没有银行卡，请添加",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    bank = bankCardBean.data.get(0).bank;
                    banknum = bankCardBean.data.get(0).bank_num;
                    bankname = bankCardBean.data.get(0).name;
                    tvBank.setText(DuiHuanActivity.this.bank);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private static Toast mToast = null;

    public static void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                bank = b.getString("bank");//str即为回传的值
                bankname = b.getString("bankname");//str即为回传的值
                banknum = b.getString("banknum");//str即为回传的值
                tvBank.setText(bank);
                break;
        }
    }

    protected View getDecorViewDialog() {

        return PayPasswordView.getInstance(etStr, DuiHuanActivity.this, new PayPasswordView.OnPayListener() {

            private Request<String> request;
            private RequestQueue queue;

            @Override
            public void onCancelPay() {
                mDialogWidget.dismiss();
                mDialogWidget = null;
                Toast.makeText(getApplicationContext(), "交易已取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSurePay(String password) {
                mDialogWidget.dismiss();
                mDialogWidget = null;


                queue = NoHttp.newRequestQueue();
                request = NoHttp.createStringRequest(Url.duihuan, RequestMethod.POST);
                request.add("phone", phone);
                request.add("bank", bank);
                request.add("exchange", etStr);
                request.add("money", je);
                request.add("name", bankname);
                request.add("bank_num", banknum);
                request.add("password", password);
                queue.add(555, request, new OnResponseListener<String>() {

                    private int result;
                    private ResultBean resultBean;
                    private String json;

                    @Override
                    public void onStart(int what) {

                    }

                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        json = response.get();
                        Gson gson = new Gson();
                        resultBean = gson.fromJson(json, ResultBean.class);

                        result = resultBean.result;
                        switch (result) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "兑换密码错误", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case 1:
//                                Toast.makeText(getApplicationContext(), "兑换成功", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
//                                Toast.makeText(getApplicationContext(), "兑换成功", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(getApplicationContext(), "兑换成功", Toast.LENGTH_SHORT).show();
                                finish();
                                break;

                            default:
                                break;
                        }

                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {

                    }

                    @Override
                    public void onFinish(int what) {

                    }
                });


            }
        }).getView();
    }
}


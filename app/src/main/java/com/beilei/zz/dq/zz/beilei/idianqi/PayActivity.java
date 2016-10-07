package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.PayBean;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.R.attr.path;

/**
 * Created by ZengHao on 2016/9/23 0023.
 */

public class PayActivity extends Activity {


    @InjectView(R.id.pay_ewm)
    ImageView payEwm;
    private RequestQueue queue;
    private Request<String> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = NoHttp.newRequestQueue();
        request = NoHttp.createStringRequest(Url.pay, RequestMethod.POST);
        queue.add(987, request, new OnResponseListener<String>() {

            private String code_url;
            private String json;

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                json = response.get();
                code_url = new Gson().fromJson(json, PayBean.class).data.get(0).code_url;
                Picasso.with(PayActivity.this).load(code_url).into(payEwm);
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });


        setContentView(R.layout.activity_pay);
        ButterKnife.inject(this);
        payEwm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(PayActivity.this,"保存到相册二维码",Toast.LENGTH_SHORT).show();
//                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.mipmap.pay);
//                saveImageToGallery(PayActivity.this,bm);
                return false;
            }
        });
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "pay.jpg";

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

}

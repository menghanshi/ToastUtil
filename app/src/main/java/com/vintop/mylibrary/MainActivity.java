package com.vintop.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.myq.toastutils.HeaderToast;
import com.myq.toastutils.RoundToast;
import com.myq.toastutils.ToastUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnToast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showCenterToast(MainActivity.this,"虫虫蚁蚁");
                HeaderToast.makeToast(MainActivity.this,"asdf");
                RoundToast.showBottomTips(MainActivity.this,"曰归曰归");
            }
        });
    }
}
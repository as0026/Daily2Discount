package com.hjs.daily2discount.ui.activity.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hjs.daily2discount.R;

public class QRCodeScanActivity extends AppCompatActivity {

    public static final String TAG = QRCodeScanActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrcode_scan);

    }

}

package com.hjs.daily2discount.ui.activity.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjs.daily2discount.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_close_img)
    ImageView loginCloseImg;
    @BindView(R.id.login_phone_edt)
    EditText loginPhoneEdt;
    @BindView(R.id.login_pwd_edt)
    EditText loginPwdEdt;
    @BindView(R.id.login_submit_btn)
    Button loginSubmitBtn;
    @BindView(R.id.login_register_txt)
    TextView loginRegisterTxt;
    @BindView(R.id.login_main_txt)
    TextView loginMainTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    @OnClick({R.id.login_close_img, R.id.login_submit_btn, R.id.login_register_txt, R.id.login_main_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_close_img:
                finish();
                break;
            case R.id.login_submit_btn:
                break;
            case R.id.login_register_txt:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login_main_txt:
                break;
        }
    }
}

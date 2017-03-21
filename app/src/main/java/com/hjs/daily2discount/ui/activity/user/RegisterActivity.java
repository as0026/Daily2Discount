package com.hjs.daily2discount.ui.activity.user;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjs.daily2discount.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.reg_close_img)
    ImageView regCloseImg;
    @BindView(R.id.reg_login_txt)
    TextView regLoginTxt;
    @BindView(R.id.reg_main_txt)
    TextView regMainTxt;
    @BindView(R.id.reg_nickname_edt)
    EditText regNicknameEdt;
    @BindView(R.id.reg_phone_edt)
    EditText regPhoneEdt;
    @BindView(R.id.reg_pwd_edt)
    EditText regPwdEdt;
    @BindView(R.id.reg_password_visiable)
    CheckBox regPasswordVisiable;
    @BindView(R.id.reg_submit_btn)
    Button regSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏，这个是在api21（5.0）才会有这个属性
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        regPasswordVisiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //设置EditText文本为可见的
                    regPwdEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    regPwdEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //切换后将EditText光标置于末尾
                CharSequence charSequence = regPwdEdt.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
    }

    @OnClick({R.id.reg_close_img, R.id.reg_login_txt, R.id.reg_main_txt, R.id.reg_submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_close_img:
                finish();
                break;
            case R.id.reg_login_txt:
                finish();
                break;
            case R.id.reg_main_txt:
                break;
            case R.id.reg_submit_btn:
                break;
        }
    }
}

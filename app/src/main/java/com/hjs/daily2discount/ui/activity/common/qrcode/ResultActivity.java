package com.hjs.daily2discount.ui.activity.common.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjs.daily2discount.R;
import com.hjs.daily2discount.ui.activity.base.BaseSwipeBackCompatActivity;
import com.hjs.daily2discount.utils.ClipboardUtils;
import com.hjs.daily2discount.utils.CommonUtils;
import com.hjs.daily2discount.utils.ToastUtil;
import com.hjs.daily2discount.widget.qrcode.decode.DecodeThread;
import com.hjs.daily2discount.widget.qrcode.decode.DecodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 条形码扫描结果页面
 */
public class ResultActivity extends BaseSwipeBackCompatActivity {

    public static final String BUNDLE_KEY_SCAN_RESULT = "BUNDLE_KEY_SCAN_RESULT";

    @BindView(R.id.common_toolbar)
    Toolbar toolbar;
    @BindView(R.id.result_image)
    ImageView resultImage;
    @BindView(R.id.result_type)
    TextView resultType;
    @BindView(R.id.result_content)
    TextView resultContent;

    private Bitmap mBitmap;
    private int mDecodeMode;
    private String mResultStr;
    private String mDecodeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        toolbar.setTitle("扫描结果");
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        setSupportActionBar(toolbar);
        toolbar.setOnCreateContextMenuListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                mBitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
            mResultStr = extras.getString(BUNDLE_KEY_SCAN_RESULT);
            mDecodeMode = extras.getInt(DecodeThread.DECODE_MODE);
            mDecodeTime = extras.getString(DecodeThread.DECODE_TIME);
        }
        scanResult();
    }

    private void scanResult(){
        StringBuilder sb = new StringBuilder();
        sb.append("扫描方式:\t\t");
        if (mDecodeMode == DecodeUtils.DECODE_MODE_ZBAR) {
            sb.append("ZBar扫描");
        } else if (mDecodeMode == DecodeUtils.DECODE_MODE_ZXING) {
            sb.append("ZXing扫描");
        }

        if (!CommonUtils.isEmpty(mDecodeTime)) {
            sb.append("\n\n扫描时间:\t\t");
            sb.append(mDecodeTime);
        }
        sb.append("\n\n扫描结果:");

        resultType.setText(sb.toString());
        resultContent.setText(mResultStr);

        if (null != mBitmap) {
            resultImage.setImageBitmap(mBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qrscan_result,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_copy) {
            ClipboardUtils.copy(ResultActivity.this,mResultStr);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

}

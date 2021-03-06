package com.hjs.daily2discount.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjs.daily2discount.R;
import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.entitys.DiscountBean;
import com.hjs.daily2discount.ui.activity.common.citySelected.CitySearchActivity;
import com.hjs.daily2discount.ui.activity.common.qrcode.QRCodeScanActivity;
import com.hjs.daily2discount.ui.activity.discount.SearchActivity;
import com.hjs.daily2discount.ui.activity.user.LoginActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MainActivity.class.getName();

    public static final int SELECT_CITY_RESULT_TAG = 1001;

    @BindView(R.id.home_city_img)
    TextView homeCityImg;
    @BindView(R.id.home_keyword_txt)
    TextView homeKeywordTxt;
    @BindView(R.id.home_qr_img)
    ImageView homeQrImg;
    @BindView(R.id.home_msg_img)
    ImageView homeMsgImg;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private RecyclerArrayAdapter<DiscountBean> mAdapter;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView(){
        initRecyclerView();
        fab.attachToRecyclerView(recyclerView.getRecyclerView());
    }

    private void initRecyclerView(){
        recyclerView.setAdapter(mAdapter = new RecyclerArrayAdapter<DiscountBean>(MainActivity.this){
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(parent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.app_gray_line), getResources().getDimensionPixelOffset(R.dimen.dim_line_height),0,0);
        itemDecoration.setDrawLastItem(true);
        itemDecoration.setDrawHeaderFooter(true);
        recyclerView.addItemDecoration(itemDecoration);

        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                LinearLayout headerView = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_home_category,null);
                headerView.findViewById(R.id.home_category_gouwu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                headerView.findViewById(R.id.home_category_meishi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                headerView.findViewById(R.id.home_category_qiche).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                headerView.findViewById(R.id.home_category_liren).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                headerView.findViewById(R.id.home_category_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        mAdapter.setMore(R.layout.view_more, MainActivity.this);
        mAdapter.setNoMore(R.layout.view_nomore, null);
        mAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mAdapter.resumeMore();
            }
            @Override
            public void onErrorClick() {
                mAdapter.resumeMore();
            }
        });
        recyclerView.setRefreshingColorResources(R.color.app_blue,R.color.app_orange,R.color.app_theme);
        recyclerView.setRefreshListener(this);
        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    private void initData(){
        onRefresh();

//        ProductBean productBean = new ProductBean();
//        productBean.setName("YOTAPHONE");
//        productBean.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if(e != null){
//                    ToastUtil.showShortToast(MainActivity.this,"添加数据成功");
//                }else{
//                    ToastUtil.showShortToast(MainActivity.this,"添加数据失败:"+e.getMessage());
//                }
//            }
//        });
    }

    @Override
    public void onLoadMore() {
        if (!AppGlobal.Net_state) {
            mAdapter.pauseMore();
            return;
        }
        SearchListData(++pageNum);
    }

    @Override
    public void onRefresh() {
        if (!AppGlobal.Net_state) {
            mAdapter.pauseMore();
            return;
        }
        pageNum = 1;
        SearchListData(pageNum);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<DiscountBean> list = new ArrayList<DiscountBean>();
            for (int i=0;i<10;i++){
                list.add(new DiscountBean());
            }

            initListData(list);
        }
    };

    private void SearchListData(int pageNum){
        handler.sendEmptyMessageDelayed(pageNum,2000);
    }

    private void initListData(List<DiscountBean> list){
        if(pageNum <= 1){
            mAdapter.clear();
        }else{
            if(list == null || list.size() <= 0){
                --pageNum;
            }
        }
        mAdapter.addAll(list);
    }

    class ViewHolder extends BaseViewHolder<DiscountBean> {
        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.activity_home_item);
        }
        @Override
        public void setData(final DiscountBean bean){
        }
    }

    @OnClick({R.id.home_city_img, R.id.home_keyword_txt, R.id.home_qr_img, R.id.home_msg_img, R.id.recyclerView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_city_img:
                startActivityForResult(new Intent(MainActivity.this, CitySearchActivity.class),SELECT_CITY_RESULT_TAG);
                break;
            case R.id.home_keyword_txt:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.home_qr_img:
                startActivity(new Intent(MainActivity.this, QRCodeScanActivity.class));
                break;
            case R.id.home_msg_img:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.recyclerView:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_CITY_RESULT_TAG && data != null){
                //城市选择界面 获取用户选择的城市
                homeCityImg.setText(data.getStringExtra(CitySearchActivity.CITY_SELECT_NAME_TAG));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 按键点击回调事件,当按下回退键时显示是否退出
     */
    private long exitTime;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - exitTime > 3000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}

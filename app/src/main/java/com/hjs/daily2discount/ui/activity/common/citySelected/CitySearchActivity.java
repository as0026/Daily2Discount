package com.hjs.daily2discount.ui.activity.common.citySelected;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjs.daily2discount.R;
import com.hjs.daily2discount.ui.activity.base.BaseSwipeBackCompatActivity;
import com.wavesidebar.LetterComparator;
import com.wavesidebar.PinnedHeaderDecoration;
import com.wavesidebar.WaveSideBarView;
import com.wavesidebar.adapter.CityAdapter;
import com.wavesidebar.bean.City;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.solart.turbo.OnItemClickListener;

/**
 * 城市选择
 */
public class CitySearchActivity extends BaseSwipeBackCompatActivity {

    public static final String CITY_SELECT_NAME_TAG = "select_city_name_tag";

    @BindView(R.id.common_toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.side_view)
    WaveSideBarView mSideBarView;

    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selected);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        toolbar.setTitle("城市选择");
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        setSupportActionBar(toolbar);
        toolbar.setOnCreateContextMenuListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
    }

    private void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Type listType = new TypeToken<ArrayList<City>>() {}.getType();
                Gson gson = new Gson();
                final List<City> list = gson.fromJson(City.DATA, listType);
                Collections.sort(list, new LetterComparator());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CityAdapter(CitySearchActivity.this, list);
                        adapter.addOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                                Intent intent = getIntent();
                                intent.putExtra(CITY_SELECT_NAME_TAG,adapter.getItem(position).name);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);
                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }
}

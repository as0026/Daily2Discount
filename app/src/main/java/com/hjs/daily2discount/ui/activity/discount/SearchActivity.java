package com.hjs.daily2discount.ui.activity.discount;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjs.daily2discount.R;
import com.hjs.daily2discount.ui.activity.base.BaseSwipeBackCompatActivity;
import com.hjs.daily2discount.utils.SharedPreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * 搜索
 */
public class SearchActivity extends BaseSwipeBackCompatActivity {

    @BindView(R.id.search_back_img)
    ImageView searchBackImg;
    @BindView(R.id.search_keyword_edt)
    EditText searchKeywordEdt;
    @BindView(R.id.search_txt)
    TextView searchTxt;
    @BindView(R.id.head_layout)
    FrameLayout headLayout;
    @BindView(R.id.tag_group)
    TagGroup tagGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initHeader();
        initView();
    }

    private void initHeader(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(false);
            headLayout.setPadding(0, SharedPreferencesHelper.getInstance(this).getStatusHeight(this), 0, 0);
        }
    }

    private void initView() {
        tagGroup.setTags(new String[]{"新白鹿", "宝岛眼镜", "慕斯先生","新白鹿", "宝岛眼镜", "慕斯先生","新白鹿", "宝岛眼镜", "慕斯先生","新白鹿", "宝岛眼镜", "慕斯先生","新白鹿", "宝岛眼镜", "慕斯先生"});
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {

            }
        });
    }

    @OnClick(R.id.search_back_img)
    public void onClick() {
        finish();
    }
}

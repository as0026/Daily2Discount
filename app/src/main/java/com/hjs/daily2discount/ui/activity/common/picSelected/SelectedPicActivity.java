package com.hjs.daily2discount.ui.activity.common.picSelected;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hjs.daily2discount.R;
import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.ui.activity.base.BaseSwipeBackCompatActivity;
import com.hjs.daily2discount.utils.DataAdapter;
import com.hjs.daily2discount.utils.ToastUtil;
import com.hjs.daily2discount.widget.picSelected.ImageFloder;
import com.hjs.daily2discount.widget.picSelected.ImageLoader;
import com.hjs.daily2discount.widget.picSelected.SelectPicDirPopupWindow;
import com.hjs.daily2discount.widget.picSelected.SelectPicsPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 多图选择界面
 *
 * @author Administrator
 */
public class SelectedPicActivity extends BaseSwipeBackCompatActivity {

    public static final String SURPLUS_NUMBER_TAG = "surplus_number";
    public static final String SELECTED_PICPATHS_TAG = "selected_picPath";

    @BindView(R.id.common_toolbar)
    Toolbar toolbar;
    @BindView(R.id.spic_gridview)
    GridView mGridView;
    @BindView(R.id.spic_statusbar_dir_txt)
    TextView dirTxt;
    @BindView(R.id.spic_statusbar_preview_txt)
    TextView previewTxt;
    private List<String> countImgPath = new ArrayList<String>();//所有图片
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPathsTemp = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private DataAdapter<String> mDataAdapter;
    private ArrayList<String> mSelectedImgList = new ArrayList<String>();
    private int surplusNumber = 0;

    private SelectPicDirPopupWindow mPicDirPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedpic);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        surplusNumber = this.getIntent().getIntExtra(SelectedPicActivity.SURPLUS_NUMBER_TAG, 0);
        toolbar.setTitle("选择图片");
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        setSupportActionBar(toolbar);
        toolbar.setOnCreateContextMenuListener(this);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        getImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pic_select,menu);
        changeSelectedPic();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {
            if (mSelectedImgList.size() > 0) {
                Intent intent = getIntent();
                intent.putStringArrayListExtra(SelectedPicActivity.SELECTED_PICPATHS_TAG, mSelectedImgList);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void changeSelectedPic() {
        toolbar.getMenu().getItem(0).setTitle("完成(" + mSelectedImgList.size() + "/" + surplusNumber + ")");
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        //排除经过选择而生成的缓存目录
        mDirPathsTemp.add(SelectPicsPopupWindow.picFileDir);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectedPicActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    countImgPath.add(path);
                    //获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                    continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹
                    if (mDirPathsTemp.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPathsTemp.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    try {
                        int picSize = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg")
                                        || filename.endsWith(".png")
                                        || filename.endsWith(".jpeg"))
                                    return true;
                                return false;
                            }
                        }).length;
                        imageFloder.setCount(picSize);
                        mImageFloders.add(imageFloder);
                    }catch (Exception e){

                    }
                }
                if (countImgPath.size() > 0) {
                    //设置“所有图片”
                    ImageFloder allFloder = new ImageFloder();
                    allFloder.setCount(countImgPath.size());
                    allFloder.setDir(null);
                    allFloder.setFirstImagePath(countImgPath.get(0));
                    mImageFloders.add(0, allFloder);
                }

                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPathsTemp = null;
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initGridView();
        }
    };

    private void initGridView() {
        if (mDataAdapter == null) {
            mDataAdapter = new DataAdapter<String>(SelectedPicActivity.this, countImgPath, R.layout.layout_picture_grid_item, new DataAdapter.InitViewData<String>() {
                @Override
                public void initViewData(View arg0, List<String> arg1,
                                         int arg2, boolean arg3) {
                    final ViewHolder holder = new ViewHolder(arg0);

                    final String imgPathStr = arg1.get(arg2);
                    ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(imgPathStr, holder.picImg);

                    holder.picImg.setImageResource(R.drawable.empty_photos);
                    holder.picBtn.setImageResource(R.mipmap.ic_pictures_unselect);
                    holder.picImg.setColorFilter(null);
                    //设置ImageView的点击事件
                    holder.picImg.setOnClickListener(new OnClickListener() {
                        //选择，则将图片变暗，反之则反之
                        @Override
                        public void onClick(View v) {
                            // 已经选择过该图片
                            if (mSelectedImgList.contains(imgPathStr)) {
                                mSelectedImgList.remove(imgPathStr);
                                holder.picBtn.setImageResource(R.mipmap.ic_pictures_unselect);
                                holder.picImg.setColorFilter(null);
                            } else {
                                // 未选择该图片
                                if (mSelectedImgList.size() >= surplusNumber) {
                                    ToastUtil.showShortToast(SelectedPicActivity.this.getApplicationContext(), "选择图片得超过" + surplusNumber + "张");
                                    return;
                                }
                                mSelectedImgList.add(imgPathStr);
                                holder.picBtn.setImageResource(R.mipmap.ic_pictures_selected);
                                holder.picImg.setColorFilter(Color.parseColor("#77000000"));
                            }
                            changeSelectedPic();
                        }
                    });

                    /**
                     * 已经选择过的图片，显示出选择过的效果
                     */
                    if (mSelectedImgList.contains(imgPathStr)) {
                        holder.picBtn.setImageResource(R.mipmap.ic_pictures_selected);
                        holder.picImg.setColorFilter(Color.parseColor("#77000000"));
                    } else {
                        holder.picBtn.setImageResource(R.mipmap.ic_pictures_unselect);
                    }
                }
            });
            mGridView.setAdapter(mDataAdapter);
        }
    }

    class ViewHolder {
        ImageView picImg;
        ImageButton picBtn;

        public ViewHolder(View view) {
            picImg = (ImageView) view.findViewById(R.id.pic_item_img);
            picBtn = (ImageButton) view.findViewById(R.id.pic_item_select);
            LayoutParams params = (LayoutParams) picImg.getLayoutParams();
            int width = (int) (AppGlobal.screenWidth - 2 * AppGlobal.scaledDensity * 3) / 3;
            params.width = width;
            params.height = width;
            picImg.setLayoutParams(params);
        }
    }

    @OnClick({R.id.spic_statusbar_dir_txt, R.id.spic_statusbar_preview_txt})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.spic_statusbar_dir_txt:
                //选择文件夹
                if (mImageFloders.size() > 0) {
                    if (mPicDirPop == null) {
                        mPicDirPop = new SelectPicDirPopupWindow(SelectedPicActivity.this, mImageFloders) {
                            @Override
                            public void onItemSelectedListener(ImageFloder folder) {
                                dirTxt.setText(folder.getName() + "/");
                                changeDirPics(folder.getDir());
                            }
                        };
                        mPicDirPop.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                popupShow(false);
                            }
                        });
                    }
                    popupShow(true);
                    mPicDirPop.show(dirTxt);
                }
                break;
            case R.id.spic_statusbar_preview_txt:
                //预览
                break;
        }
    }

    /**
     * 更改文件夹
     *
     * @param dir
     */
    private void changeDirPics(String dir) {
        mDataAdapter.getList().clear();
        if (TextUtils.isEmpty(dir)) {
            mDataAdapter.addList(countImgPath);
        } else {
            List<String> mImgs = Arrays.asList(new File(dir).list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                        return true;
                    return false;
                }
            }));
            for (int i = 0; i < mImgs.size(); i++) {
                mImgs.set(i, dir + "/" + mImgs.get(i));
            }
            mDataAdapter.addList(mImgs);
        }
        mDataAdapter.notifyDataSetChanged();
    }

    /**
     * popupWindow是否显示,window控制亮度
     *
     * @param isShow
     */
    private void popupShow(final boolean isShow) {
        SelectedPicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                if (isShow) {
                    // 设置背景颜色变暗
                    lp.alpha = 0.4f;
                } else {
                    lp.alpha = 1.0f;
                }
                getWindow().setAttributes(lp);
            }
        });
    }
}

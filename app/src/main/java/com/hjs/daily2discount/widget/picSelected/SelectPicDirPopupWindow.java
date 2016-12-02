package com.hjs.daily2discount.widget.picSelected;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hjs.daily2discount.R;
import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.utils.DataAdapter;

import java.util.List;

/**
 * @描述 图片文件夹列表
 * @作者 郝炯淞
 * @创建时间 2014-10-20 下午2:42:53
 */
public abstract class SelectPicDirPopupWindow extends PopupWindow implements OnItemClickListener {

	private View anchorView;
	private Activity context;
	private ListView mListView;
	private DataAdapter<ImageFloder> mAdapter;
	
	private int selectedIndex = 0;
	
	public SelectPicDirPopupWindow(Activity context,List<ImageFloder> data) {
		super(context);
		this.context = context;
		View contentView = LayoutInflater.from(context).inflate(R.layout.layout_picdir_listview, null);
		
		//设置SelectPicPopupWindow的View
		this.setContentView(contentView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight((int) (AppGlobal.screenHeight * 0.7));
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.Popup_Dir_Theme);
		//实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
		initView(contentView);
		initData(data);
	}
	
	private void initView(View view) {
		mListView = (ListView) view.findViewById(R.id.listView);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}
	
	private void initData(List<ImageFloder> data){
		mAdapter = new DataAdapter<ImageFloder>(context, data, R.layout.layout_picdir_item, new DataAdapter.InitViewData<ImageFloder>() {
			@Override
			public void initViewData(View _convertView,
					List<ImageFloder> _objList, int _position,
					boolean _convertViewIsNull) {
				ViewHolder holder;
				if(_convertViewIsNull){
					holder = new ViewHolder(_convertView);
					_convertView.setTag(holder);
				}else{
					holder = (ViewHolder) _convertView.getTag();
				}
				ImageFloder floder = _objList.get(_position);
				ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(floder.getFirstImagePath(), holder.dirImg);
				holder.dirNameTxt.setText(floder.getName());
				holder.dirCountTxt.setText(floder.getCount()+"张");
				if(selectedIndex == _position){
					holder.selectedImg.setVisibility(View.VISIBLE);
				}else{
					holder.selectedImg.setVisibility(View.GONE);
				}
			}
		});
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	public void setDataList(List<ImageFloder> data){
		mAdapter.getList().clear();
		mAdapter.addList(data);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		dismiss();
		if(selectedIndex != position){
			onItemSelectedListener(mAdapter.getItem(position));
			selectedIndex = position;
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void show(View view){
		if(!isShowing()){
			anchorView = view;
			showAsDropDown(anchorView,0, 0,Gravity.TOP|Gravity.CENTER_HORIZONTAL);
		}
	}
	
	public abstract void onItemSelectedListener(ImageFloder folder);
	
	class ViewHolder{
		ImageView dirImg,selectedImg;
		TextView dirNameTxt,dirCountTxt;
		public ViewHolder(View view){
			dirImg = (ImageView) view.findViewById(R.id.item_dir_img);
			selectedImg = (ImageView) view.findViewById(R.id.item_dirSelected_Img);
			dirNameTxt = (TextView) view.findViewById(R.id.item_dirName_txt);
			dirCountTxt = (TextView) view.findViewById(R.id.item_dirCount_txt);
		}
	}
}

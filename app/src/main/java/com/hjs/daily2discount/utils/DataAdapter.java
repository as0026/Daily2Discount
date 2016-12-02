package com.hjs.daily2discount.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @描述 公共Adapter，适合写简单UI时使用
 * @作者 郝炯淞
 * @创建时间 2014-4-18 下午10:30:20
 * @param <T>
 */
public class DataAdapter<T> extends BaseAdapter {
	private List<T> dataList = new ArrayList<T>();
	private int convertViewId;
	private LayoutInflater inflater;
	private InitViewData<T> initData;

	public DataAdapter(Context context, List<T> _dataList,
			int convertViewId, InitViewData<T> _initData) {
		initData = _initData;
		if (_dataList != null && _dataList.size() > 0) {
			this.dataList.addAll(_dataList);
		}
		this.convertViewId = convertViewId;
		this.inflater = LayoutInflater.from(context);
	}

	public void addList(List<T> _dataList) {
		if (_dataList != null && _dataList.size() > 0) {
			this.dataList.addAll(_dataList);
		}
	}

	public List<T> getList() {
		return this.dataList;
	}

	public void clearList() {
		if (this.dataList != null && this.dataList.size() > 0) {
			this.dataList.clear();
			notifyDataSetChanged();
		}
	}
	
	public void remove(int position) {
		dataList.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (dataList == null) {
			return 0;
		} else {
			return dataList.size();
		}
	}

	@Override
	public T getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}

	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//判断convertView是否为空
		boolean convertViewIsNull = false;
		if (convertView == null) {
			convertView = inflater.inflate(convertViewId, null);
			convertViewIsNull = true;
		}
		if (initData != null) {
			initData.initViewData(convertView, dataList, position,convertViewIsNull);
		}
		return convertView;
	}

	public interface InitViewData<T> {
		public void initViewData(View _convertView, List<T> _objList,
								 int _position, boolean _convertViewIsNull);
	}

}

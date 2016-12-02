package com.hjs.daily2discount.widget.picSelected;

import android.text.TextUtils;

/**
 * 文件夹
 * @author Administrator
 *
 */
public class ImageFloder {
	/**
	 * 图片的文件夹路径
	 */
	private String dir;

	/**
	 * 第一张图片的路径
	 */
	private String firstImagePath;

	/**
	 * 文件夹的名称
	 */
	private String name;

	/**
	 * 图片的数量
	 */
	private int count;

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
		if(TextUtils.isEmpty(dir)){
			this.dir = "";
			this.name = "所有图片";
		}else{
			int lastIndexOf = this.dir.lastIndexOf("/")+1;
			this.name = this.dir.substring(lastIndexOf);
		}
	}

	public String getFirstImagePath() {
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath) {
		this.firstImagePath = firstImagePath;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "dir="+dir+",firstImagePath="+firstImagePath+",name="+name+",count="+count;
	}

}

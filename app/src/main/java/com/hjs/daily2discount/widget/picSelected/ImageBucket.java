package com.hjs.daily2discount.widget.picSelected;

import java.util.LinkedList;
import java.util.List;

/**
 * 添加心情图片的容器
 * @author hjs
 *
 */
public class ImageBucket {

	public static final int max = 9;	//最大的图片数
	public static final int min = 1;	//最小图片
	
	private int size = max;	
	public List<ImageItem> ImageList = new LinkedList<ImageItem>();
	
	public ImageBucket(){
		
	}
	
	public ImageBucket(int size){
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
}

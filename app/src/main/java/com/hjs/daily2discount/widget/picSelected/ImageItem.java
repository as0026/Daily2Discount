package com.hjs.daily2discount.widget.picSelected;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 添加图片的bean
 * @author hjs
 *
 */
public class ImageItem {

	private String thumbnailPath;	//图片sd卡本地的地址
	private String imagePath = "";	//图片的url地址
	private Bitmap bitmap;	//图片的bitmap对象
	public static final int compressMultiple = 2;
	
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		
		String fileName = new File(thumbnailPath).getName();
		String cacheFilePath = SelectPicsPopupWindow.picFileDir + fileName;
		storeInSD(FileUtils.compressImg(thumbnailPath, AppGlobal.screenWidth/compressMultiple, AppGlobal.screenHeight/compressMultiple),fileName,SelectPicsPopupWindow.picFileDir);
		this.thumbnailPath = cacheFilePath;
	}
	
	public void setThumbnailPathByCarema(String cameraPath){
		int degree = readPictureDegree(cameraPath);
		Bitmap _bitmap = FileUtils.compressImg(cameraPath, AppGlobal.screenWidth/ImageItem.compressMultiple, AppGlobal.screenHeight/ImageItem.compressMultiple);
		
		Matrix m = new Matrix();
		int width = _bitmap.getWidth();
		int height = _bitmap.getHeight();
		m.setRotate(degree);
		_bitmap = Bitmap.createBitmap(_bitmap, 0, 0, width, height, m, true);
		storeInSD(_bitmap, new File(cameraPath).getName(), SelectPicsPopupWindow.picFileDir);
		
		this.thumbnailPath = cameraPath;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Bitmap getBitmap() {
		if(bitmap == null){
			try{
				bitmap = BitmapFactory.decodeFile(this.thumbnailPath);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	/**
	 * 保存图片到文件中
	 * @Bitmap path
	 * @name 图片的名字 格式 (xx.png)
	 */
	public static void storeInSD(Bitmap bitmap, String name, String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File imageFile = new File(file, name);
		try {
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
			bitmap.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取照片exif信息中的旋转角度
	 * @param path 照片路径
	 * @return角度
	 */
	private int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}

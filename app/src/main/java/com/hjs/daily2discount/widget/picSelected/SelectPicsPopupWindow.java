package com.hjs.daily2discount.widget.picSelected;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hjs.daily2discount.R;
import com.hjs.daily2discount.constants.AppGlobal;
import com.hjs.daily2discount.ui.activity.common.picSelected.SelectedPicActivity;
import com.hjs.daily2discount.utils.FileUtils;
import com.hjs.daily2discount.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @描述 图片选择弹出框,生成的图片每次都不一样
 * @作者 郝炯淞
 * @创建时间 2014-10-20 下午2:42:53
 */
public abstract class SelectPicsPopupWindow extends PopupWindow implements OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View anchorView;
	private Activity context;
	
	private String picName = "picTemp.jpg";	//图片，用于保存的
	public static String picFileDir;	//图片文件夹目录
	public static final int IMAGE_CODE = 0;
	public static final int CAREMA_CODE = 1;
	private static final int PHOTO_PICKED_WITH_DATA = 2;
	protected final int iconWidth = 640, iconHtight = 640;
	
	private int surplusNumber = 1;	//可选总数
	private boolean hasClip = false;	//是否需要修剪
	
	public SelectPicsPopupWindow(Activity context) {
		super(context);
		this.context = context;
		picFileDir = FileUtils.getCacheFilePath(this.context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mMenuView = inflater.inflate(R.layout.layout_pic_popup, null);
		btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.SelectPopupWindow_AnimationTheme);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
	
	/**
	 * 设置是否需要修改，若需要则限制选择的图片为一张
	 * @param hasClip
	 */
	public void setClip(boolean hasClip){
		if(hasClip){
			this.hasClip = true;
			this.surplusNumber = 1;
		}else{
			this.hasClip = false;
		}
	}
	
	/**
	 * 显示
	 * @param
	 */
	public void show(View view){
		show(view,this.surplusNumber);
	}
	
	public void show(View view,int surplusNumber){
		show(view,this.surplusNumber,false);
	}
	
	public void show(View view,int surplusNumber,boolean hasClip){
		if(TextUtils.isEmpty(picFileDir)){
			ToastUtil.showShortToast(view.getContext(),R.string.string_error_emptyFile);
			return;
		}
		if(!isShowing()){
			this.surplusNumber = surplusNumber;
			if(hasClip){
				this.hasClip = true;
				this.surplusNumber = 1;
			}else{
				this.hasClip = false;
			}
			anchorView = view;
			popupShow(true);
			picName = UUID.randomUUID().toString()+".jpg";
			showAtLocation(anchorView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0, 0);
		}
	}
	
	/**
	 * popupWindow是否显示,window控制亮度
	 * @param isShow
	 */
	private void popupShow(final boolean isShow){
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WindowManager.LayoutParams lp = context.getWindow().getAttributes();
				context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				if (isShow) {
					// 设置背景颜色变暗
					lp.alpha = 0.4f;
				} else {
					lp.alpha = 1.0f;
				}
				context.getWindow().setAttributes(lp);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_cancel){
			dismiss();
		} else if (v.getId() == R.id.btn_take_photo){
			//拍照
			startCarema(context);
			dismiss();
		} else if (v.getId() == R.id.btn_pick_photo){
			//从相册选择
			dismiss();
			startCustomAlbum(context);
		}
	}
	
	/**
	 * @描述: 开启拍照
	 * @作者：  郝炯淞
	 * @创建时间：  2014-10-20 下午4:19:41
	 * void
	 */
	private void startCarema(Activity context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
		}else{
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			openCameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
			Uri imageUri = Uri.fromFile(getPicFile(picFileDir,picName));
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			context.startActivityForResult(openCameraIntent, CAREMA_CODE);
		}

		/*if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(context,
					new String[]{Manifest.permission.CAMERA}, 1);
		} else {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			openCameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
			Uri imageUri = Uri.fromFile(getPicFile(picFileDir,picName));
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			context.startActivityForResult(openCameraIntent, CAREMA_CODE);
		}*/

	}

	/**
	 * @描述: 开启自定义相册
	 * @作者：  郝炯淞
	 * @创建时间：  2014-10-20 下午4:19:30
	 * void
	 */
	private void startCustomAlbum(Activity context) {
		if(hasClip){
			this.surplusNumber = 1;
		}
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent picIntent = new Intent(context,SelectedPicActivity.class);
		picIntent.putExtra(SelectedPicActivity.SURPLUS_NUMBER_TAG,this.surplusNumber);
		context.startActivityForResult(picIntent, IMAGE_CODE);
	}
	
	/**
	 * @描述 返回一个存在的文件和路径
	 * @作者 郝炯淞
	 * @创建时间 2014-12-19 下午2:46:28
	 * @param fileDir
	 * @param fileName
	 * @return
	 */
	private File getPicFile(String fileDir,String fileName){
		File dir = new File(fileDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(fileDir,fileName);
		if(!file.exists()){
			try {
				if(file.createNewFile()){
					return file;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * @描述 该方法必须写在Activity的onActivityResult（）中。
	 * @作者 郝炯淞
	 * @创建时间 2015-8-13 下午12:09:14
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == Activity.RESULT_OK) {
			
			if(requestCode == IMAGE_CODE && data != null){
				List<String> datas = data.getStringArrayListExtra(SelectedPicActivity.SELECTED_PICPATHS_TAG);
				if(hasClip){
					//需要剪切
					Uri originalUri = Uri.fromFile(new File(datas.get(0))); // 获得图片的uri
					if (originalUri != null) {
						doCropPhoto(this.context,originalUri);
					}
				}else{
					ImageBucket mImageBucket = new ImageBucket();
					for(String path:datas){
						ImageItem item = new ImageItem();
						item.setThumbnailPath(path);
						mImageBucket.ImageList.add(item);
					}
					selectedPics(mImageBucket,anchorView);
				}
			}else if(requestCode == CAREMA_CODE){
				String cameraPath = picFileDir+File.separator+picName;
				int degree = readPictureDegree(cameraPath);
				Bitmap _bitmap = FileUtils.compressImg(cameraPath, AppGlobal.screenWidth/ImageItem.compressMultiple, AppGlobal.screenHeight/ImageItem.compressMultiple);
				Matrix m = new Matrix();
				int width = _bitmap.getWidth();
				int height = _bitmap.getHeight();
				m.setRotate(degree);
				_bitmap = Bitmap.createBitmap(_bitmap, 0, 0, width, height, m, true);
				ImageItem.storeInSD(_bitmap, new File(cameraPath).getName(), SelectPicsPopupWindow.picFileDir);
				
				if(hasClip){
					//需要剪切
					Uri originalUri = Uri.fromFile(new File(cameraPath)); // 获得图片的uri
					if (originalUri != null) {
						doCropPhoto(this.context,originalUri);
					}
				}else{
					ImageBucket mImageBucket = new ImageBucket();
					ImageItem item = new ImageItem();
					item.setThumbnailPathByCarema(cameraPath);
					mImageBucket.ImageList.add(item);
					selectedPics(mImageBucket,anchorView);
				}
			}else if(requestCode == PHOTO_PICKED_WITH_DATA){
				ImageBucket mImageBucket = new ImageBucket();
				ImageItem item = new ImageItem();
				item.setThumbnailPathByCarema(picFileDir+picName);
				mImageBucket.ImageList.add(item);
				selectedPics(mImageBucket,anchorView);
			}
		}
	}


	public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
		switch (requestCode) {
			case 1: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					openCameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
					Uri imageUri = Uri.fromFile(getPicFile(picFileDir,picName));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					context.startActivityForResult(openCameraIntent, CAREMA_CODE);
					// permission was granted, yay! Do the
					// contacts-related task you need to do.

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					ToastUtil.showShortToast(context,"拍照权限被拒绝！");
				}
				return;
			}
		}

		/*if (requestCode == 1) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				openCameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
				Uri imageUri = Uri.fromFile(getPicFile(picFileDir,picName));
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				context.startActivityForResult(openCameraIntent, CAREMA_CODE);
			} else {
				// Permission Denied
				//Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
				ToastUtil.showShortToast(context,"权限被拒！");
			}
			return;
		}*/

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
	
	private void doCropPhoto(Activity context,Uri data) {
		Intent cropIntent = getCropImageIntent(data);
		context.startActivityForResult(cropIntent,PHOTO_PICKED_WITH_DATA);
	}
	
	/**
	 * 系统的裁剪图片默认对图片进行人脸识别， 当识别到有人脸时，会按aspectX和aspectY为1来处理，
	 * 如果想设置成自定义的裁剪比例，需要设置noFaceDetection为true。
	 * 即iintent.putExtra("noFaceDetection", true); 取消人脸识别功能。
	 * 
	 * 附加选项 数据类型 描述 crop String 发送裁剪信号 aspectX int X方向上的比例 aspectY int Y方向上的比例
	 * outputX int 裁剪区的宽 outputY int 裁剪区的高 scale boolean 是否保留比例 return-data
	 * boolean 是否将数据保留在Bitmap中返回 data Parcelable 相应的Bitmap数据 circleCrop String
	 * 圆形裁剪区域？ MediaStore.EXTRA_OUTPUT URI 将URI指向相应的file:///...，详见代码示例
	 * 
	 * 
	 * 方法1：如果你将return-data设置为“true”，你将会获得一个与内部数据关联的Action，并且bitmap以此方式返回：(Bitmap
	 * )extras.getParcelable("data")。注意：如果你最终要获取的图片非常大，那么此方法会给你带来麻烦，
	 * 所以你要控制outputX和outputY保持在较小的尺寸。 方法2：
	 * 如果你将return-data设置为“false”，那么在onActivityResult的Intent数据中你将不会接收到任何Bitmap
	 * ，相反，你需要将MediaStore.EXTRA_OUTPUT关联到一个Uri，此Uri是用来存放Bitmap的。
	 * android官方的文档显示，通过intent传递的文件最大不能超过1MB
	 * ，所以这种方式切割图片通常不能超过400x400，我在我的图片软件里面采用的解决办法是
	 * ，把切割的图片存储到临时文件，然后在返回的activity里面读取文件来处理，可以得到你想得到的任何尺寸！
	 * 
	 * @param data
	 * @return
	 */
	private Intent getCropImageIntent(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP"); // 调用系统相机截图功能
		intent.setDataAndType(data, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);//当裁剪的图片不足aspectX、aspectX的时候让得到的图片没有黑边
		// outputX outputY 是裁剪图片宽高
//		intent.putExtra(
//				"outputX",
//				(int) (AppGlobal.screenDensityDpiRadio * iconWidth));
//		intent.putExtra(
//				"outputY",
//				(int) (AppGlobal.screenDensityDpiRadio * iconHtight));
		
		intent.putExtra("outputX",iconWidth);
		intent.putExtra("outputY",iconHtight);
		
		intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); //关闭人脸检测
		intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(picFileDir+picName)));
		return intent;
	}
	
	/**
	 * 返回处理好的图片集合ImageBucket
	 * @param mImageBucket
	 * @param view
	 */
	public abstract void selectedPics(ImageBucket mImageBucket,View view);
	
	@Override
	public void dismiss() {
		popupShow(false);
		super.dismiss();
	}
	
}

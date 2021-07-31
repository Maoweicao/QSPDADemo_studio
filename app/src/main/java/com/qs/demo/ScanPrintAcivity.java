package com.qs.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qs.pdademo.R;
import com.qs.util.PrintUtils;

/**
 * 获取扫描信息
 * 
 * @author wusiliang
 * @time 2020年9月22日11:48:32
 * 
 */
public class ScanPrintAcivity extends Activity {

	Intent mIntent = new Intent("ismart.intent.scandown");

	//扫描信息接收广播
	private ScanBroadcastReceiver scanBroadcastReceiver;
    //内容输入编辑框
	private EditText tv;

	//自己的Activity
	private Activity myActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_layout1);
		
		//初始化打印工具
		PrintUtils.initPrintUtils(this);

		//控件初始化
		tv = (EditText) findViewById(R.id.tv);

		/**
		 * 打印文字
		 */
		findViewById(R.id.btn_printText).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String str = tv.getText().toString();

						/**
						 * 打印文字
						 * @param size  文字大小，其中1为正常字体，2位双倍宽高字体，暂不支持其他字体大小 
						 * @param concentration 打印浓度，范围25-65
						 * @param align 对齐方式，其中0为居左，1为居中，2为居右
						 * @param blackLabel 黑标功能，true 开启，false 关闭
						 * @param text 要打印的文字,不满一行时候需要添加换行符\n
						 *       
						 */
						PrintUtils.printText(1,60, 0, false,str+"\n");

					}
				});

		/**
		 * 打印一维码
		 */
		findViewById(R.id.btn_printBarcode).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String barStr = tv.getText().toString();
						if (barStr == null || barStr.length() <= 0)
							return;
						// 判断当前字符能否生成条码
						if (barStr.getBytes().length > barStr.length()) {
							Toast.makeText(ScanPrintAcivity.this, "当前数据不能生成一维码",
									Toast.LENGTH_SHORT).show();
							return;
						}
					    /**
					     * 打印一维条码
					     * @param concentration: 打印浓度 范围为25-65，超过范围后恢复至默认值25
					     * @param left：左边距，一维条码与纸张左边的距离
					     * @param width：一维条码的宽度 58纸张最大宽度为58
					     * @param height：一维条码的高度
					     * @param blackLabel:是否进行黑标检测
					     * @param str: 	一维条码对象(不能是中文，若为中文则会报错)
					     */
						PrintUtils.printBarCode(60,0,380,100,false,barStr);

					}
		});

		/**
		 * 打印二维码
		 */
		findViewById(R.id.btn_printQRcode).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String qrStr = tv.getText().toString();
						if (qrStr == null || qrStr.length() <= 0){
							Toast.makeText(ScanPrintAcivity.this, "当前数据不能为空",
									Toast.LENGTH_SHORT).show();
							return;
						}
					    /**
					     * 打印二维码
					     * @param concentration: 打印浓度 范围为25-65，超过范围后恢复至默认值25
					     * @param left：左边距，二维码与纸张左边的距离
					     * @param width：二维码的宽度 58纸张最大宽度为380
					     * @param height：二维码的高度
					     * @param blackLabel:是否进行黑标检测
					     * @param str2: 	二维码对象
					     *
					     */
						PrintUtils.printQRCode(60, 0, 300, 300,false,qrStr);
					}
				});

		/**
		 * 打印图片
		 */
		findViewById(R.id.btn_printImage).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 获取图片
						Bitmap btMap = BitmapFactory.decodeResource(getResources(),
								R.drawable.demo);
						 /**
					     * 打印二维码
					     * @param concentration: 打印浓度 范围为25-65，超过范围后恢复至默认值25
					     * @param left：左边距，二维码与纸张左边的距离
					     * @param blackLabel:是否进行黑标检测
					     * @param bitmap: 	图片对象
					     *
					     */
						PrintUtils.printBitmap(60,0,false, btMap);

					}
				});

		/**
		 * 扫描
		 */
		findViewById(R.id.btn_scan1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 触发扫描
				Toast.makeText(v.getContext(),"触发扫描",Toast.LENGTH_LONG).show();
				new IntentIntegrator(myActivity).initiateScan();

			}
		});
		
		scanBroadcastReceiver = new ScanBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.qs.scancode");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);


	}

	/**
	 * 获取扫描信息
	 * 
	 * @author wu
	 * 
	 */
	class ScanBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.w("扫描请求", "接收到扫描请求");
			String text1 = intent.getExtras().getString("code");
			tv.setText(text1);
			Toast.makeText(context, "扫描信息：" + text1, Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(scanBroadcastReceiver);
		PrintUtils.closeApp();
		super.onDestroy();
	}

	/**
	 * 获取结果
	 * @param requestCode 请求代码
	 * @param resultCode 结果代码
	 * @param data 数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
		if(result!=null){
			if(result.getContents()==null){
				Log.w("扫描结果", "没有结果");
				Toast.makeText(this,"已取消扫描",Toast.LENGTH_LONG).show();
			}else{
				Log.w("扫描结果", "结果"+result.getContents());
				Toast.makeText(this,"扫描结果："+result.getContents(),Toast.LENGTH_LONG).show();
			}
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}

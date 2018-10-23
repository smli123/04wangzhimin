package com.xiaoyu.communication2;

import java.util.ArrayList;
import com.xiaoyu.utils.ColorPickerView;
import android.R.array;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class BTColorSelectorActivity  extends Activity 
	implements OnClickListener,
	SeekBar.OnSeekBarChangeListener  {
	private Context mContext;
	String str_color_info = "";
	
	private ImageView iv_bluetooth_mode;
	private ImageView iv_ok;
	private TextView iv_show;
	
	private ImageView iv_color_01;
	private ImageView iv_color_02;
	private ImageView iv_color_03;
	private ImageView iv_color_04;
	private ImageView iv_color_05;
	private ImageView iv_color_06;
	private ImageView iv_color_07;
	private ImageView iv_color_08;
	
	private ImageView iv_preview;
	
	private TextView tv_red;
	private TextView tv_green;
	private TextView tv_blue;
	
	private SeekBar sb_red;
	private SeekBar sb_green;
	private SeekBar sb_blue;
	
	private ColorPickerView cp_colorpicker;
	
	int i_bluetooth_mode = 0;		// 0: 跳变模式， 1： 渐变模式
	int value_red = 0;
	int value_green = 0;
	int value_blue = 0;
	
	int value_play = 0;
	int value_light = 0;
	int value_flash = 0;
	
	int result_return = 0;
	
	ArrayList<Integer> array_color_int = new ArrayList<Integer>();
	ArrayList<String> array_color = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bt_selectcolor);
		mContext = this;
		
		// load Data from intent
		Intent intent = getIntent();
		str_color_info = intent.getStringExtra("DATA");
		result_return = intent.getIntExtra("NUM", 0);
		
		// string to array_color_int, array_color
		transInfo();
		
		initView();
		
		updateUI();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint({ "ResourceAsColor", "NewApi" }) 
	private void updateUI() {
		String[] cmds = str_color_info.split(",");
		if (cmds.length < 5)
			return;
		
		if (cmds.length == 5) {
			iv_show.setBackgroundColor(R.color.white);
			
		} else {
//			int count = Integer.valueOf(cmds[1]);
			int count = array_color.size();
			
			int colors[] = new int[count];
			for (int i = 0; i < array_color_int.size(); i++) {
				int now_color = array_color_int.get(i);
				int red = Color.red(now_color);
				int green = Color.green(now_color);
				int blue = Color.blue(now_color);
				
				colors[i] = Color.rgb(red, green, blue);
			}
			
//			String color = "#" + array_color.get(count- 1);
//			iv_show.setBackgroundColor(Color.parseColor(color));
			
			int now_color = array_color_int.get(count- 1);
			int red = Color.red(now_color);
			int green = Color.green(now_color);
			int blue = Color.blue(now_color);
			

			int strokeWidth = 1;     // 1dp 边框宽度
			int roundRadius = 5;     // 5dp 圆角半径
			int strokeColor = Color.parseColor("#00FF00");//边框颜色
			int fillColor = now_color; //内部填充颜色
			
			GradientDrawable gd = new GradientDrawable();//创建drawable
			gd.setCornerRadius(roundRadius);
			gd.setStroke(strokeWidth, strokeColor);
			if (count == 1) {
				gd.setColor(fillColor);
				gd.setGradientType(GradientDrawable.RECTANGLE);
				iv_show.setBackgroundColor(Color.rgb(red, green, blue));
			} else {
			
				// GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
				gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
				gd.setColors(colors);
				iv_show.setBackgroundDrawable(gd);
			
//				GradientDrawable myGrad = (GradientDrawable)iv_show.getBackground();
//				myGrad.setColor (now_color);
//				iv_show.setBackgroundDrawable(myGrad);
//			
//				GradientDrawable.Orientation[] orientations;
//				GradientDrawable myGrad = (GradientDrawable) iv_show.getBackground();
//				myGrad.setLevel(10000);
//		        orientations = GradientDrawable.Orientation.values();
			}
		}
	}
	
	private void initView() {
	    iv_bluetooth_mode = (ImageView) findViewById(R.id.iv_bluetooth_mode);
		iv_ok = (ImageView) findViewById(R.id.iv_ok);
		iv_show = (TextView) findViewById(R.id.iv_show);
		
		iv_color_01 = (ImageView) findViewById(R.id.iv_color_01);
		iv_color_02 = (ImageView) findViewById(R.id.iv_color_02);
		iv_color_03 = (ImageView) findViewById(R.id.iv_color_03);
		iv_color_04 = (ImageView) findViewById(R.id.iv_color_04);
		iv_color_05 = (ImageView) findViewById(R.id.iv_color_05);
		iv_color_06 = (ImageView) findViewById(R.id.iv_color_06);
		iv_color_07 = (ImageView) findViewById(R.id.iv_color_07);
		iv_color_08 = (ImageView) findViewById(R.id.iv_color_08);
		
		iv_preview = (ImageView) findViewById(R.id.iv_preview);
		
		tv_red = (TextView) findViewById(R.id.tv_red);
		tv_green = (TextView) findViewById(R.id.tv_green);
		tv_blue = (TextView) findViewById(R.id.tv_blue);
		
		sb_red = (SeekBar) findViewById(R.id.sb_red);
		sb_green = (SeekBar) findViewById(R.id.sb_green);
		sb_blue = (SeekBar) findViewById(R.id.sb_blue);
		
		cp_colorpicker = (ColorPickerView) findViewById(R.id.cp_colorpicker);
		
		iv_bluetooth_mode.setOnClickListener(this);
		iv_ok.setOnClickListener(this);
		iv_show.setOnClickListener(this);

		iv_color_01.setOnClickListener(this);
		iv_color_02.setOnClickListener(this);
		iv_color_03.setOnClickListener(this);
		iv_color_04.setOnClickListener(this);
		iv_color_05.setOnClickListener(this);
		iv_color_06.setOnClickListener(this);
		iv_color_07.setOnClickListener(this);
		iv_color_08.setOnClickListener(this);
		iv_preview.setOnClickListener(this);
		
		sb_red.setOnSeekBarChangeListener(this);
		sb_green.setOnSeekBarChangeListener(this);
		sb_blue.setOnSeekBarChangeListener(this);
		
		sb_red.setProgress(value_red);
		sb_green.setProgress(value_green);
		sb_blue.setProgress(value_blue);
		
		cp_colorpicker.setColorChangedListener(new ColorPickerView.OnColorChangedListener(){
			@Override
			public void onColorChanged(int color) {
//				value_red = (color >>  16) & 0xFF;
//			    value_green = (color >>   8) & 0xFF;
//			    value_blue = (color)        & 0xFF;
			    
			    value_red = Color.red(color);
			    value_green = Color.green(color);
			    value_blue = Color.blue(color);
			    
			    sb_red.setProgress(value_red);
			    sb_green.setProgress(value_green);
			    sb_blue.setProgress(value_blue);
			    
				updatePreview();
			}
		});

		iv_bluetooth_mode.setBackgroundResource(i_bluetooth_mode == 0 ? R.drawable.smp_bluetooth_mode_01 : R.drawable.smp_bluetooth_mode_02);
	}
	

	@Override
	public void onProgressChanged(SeekBar bar, int pos, boolean arg2) {
		switch (bar.getId()) {
			case R.id.sb_red :
				tv_red.setText("R:" + bar.getProgress());
				break;
			case R.id.sb_green :
				tv_green.setText("G:" + bar.getProgress());
				break;
			case R.id.sb_blue :
				tv_blue.setText("B:" + bar.getProgress());
				break;
			default :
				break;
		}
		updatePreview();
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar bar) {
		String str_time = "";
		switch (bar.getId()) {
			case R.id.sb_red :
				value_red = bar.getProgress();
				tv_red.setText("R:" + bar.getProgress());
				break;
			case R.id.sb_green :
				value_green = bar.getProgress();
				tv_green.setText("G:" + bar.getProgress());
				break;
			case R.id.sb_blue :
				value_blue = bar.getProgress();
				tv_blue.setText("B:" + bar.getProgress());
				break;
			default :
				break;
		}
		updatePreview();
	}
	
	private void updatePreview() {
		iv_preview.setBackgroundColor(Color.rgb(value_red, value_green, value_blue));
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.iv_bluetooth_mode:
			if (i_bluetooth_mode == 0) 
				i_bluetooth_mode = 1;
			else 
				i_bluetooth_mode = 0;
			
			iv_bluetooth_mode.setBackgroundResource(i_bluetooth_mode == 0 ? R.drawable.smp_bluetooth_mode_01 : R.drawable.smp_bluetooth_mode_02);
			break;
		case R.id.iv_ok:
			Intent intent = new Intent(); 
			intent.putExtra("DATA", str_color_info);
			setResult(result_return, intent);
            finish();
			break;
		case R.id.iv_show:
			array_color_int.clear();
			array_color.clear();
			genStringCommand();
			updateUI();
			break;
		case R.id.iv_color_01:
			setImageViewBackgroundColor(iv_color_01);
			break;
		case R.id.iv_color_02:
			setImageViewBackgroundColor(iv_color_02);
			break;
		case R.id.iv_color_03:
			setImageViewBackgroundColor(iv_color_03);
			break;
		case R.id.iv_color_04:
			setImageViewBackgroundColor(iv_color_04);
			break;
		case R.id.iv_color_05:
			setImageViewBackgroundColor(iv_color_05);
			break;
		case R.id.iv_color_06:
			setImageViewBackgroundColor(iv_color_06);
			break;
		case R.id.iv_color_07:
			setImageViewBackgroundColor(iv_color_07);
			break;
		case R.id.iv_color_08:
			setImageViewBackgroundColor(iv_color_08);
			break;
		case R.id.iv_preview:
			array_color_int.add(Color.rgb(value_red, value_green, value_blue));
//			array_color.add(Integer.toHexString(value_red) + Integer.toHexString(value_green) + Integer.toHexString(value_blue));
			array_color.add(IntToHexString(value_red) + IntToHexString(value_green) + IntToHexString(value_blue));
			genStringCommand();
			updateUI();
			break;
		}
	}
	
	private String IntToHexString(int value) {
		return String.format("%02x",value);
	}
	
	@SuppressLint("NewApi") 
	private boolean setImageViewBackgroundColor(ImageView iv) {
		Drawable background = iv.getBackground();
		
		// background 包括 color 和 Drawable, 这里分开取值
        if (background instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) background;
            int color = colordDrawable.getColor();
            
            value_red = Color.red(color);
            value_green = Color.green(color);
            value_blue = Color.blue(color);
            
            sb_red.setProgress(value_red);
            sb_green.setProgress(value_green);
            sb_blue.setProgress(value_blue);
            
            return true;
        }
        return false;
	}

	private void transInfo() {
		array_color_int.clear();
		array_color.clear();
		
		String[] cmds = str_color_info.split(",");
		if (cmds.length < 5)
			return;
		value_play = Integer.valueOf(cmds[0]);
		value_light = Integer.valueOf(cmds[1]);
		value_flash = Integer.valueOf(cmds[2]);
		i_bluetooth_mode = Integer.valueOf(cmds[3]);
		
		if (cmds.length == 5) {
			// do nothing...
		} else {
			int count = Integer.valueOf(cmds[4]);
			for (int i = 5; i < count + 5; i++) {
				int col = Integer.valueOf(cmds[i], 16);
				
				String value = Integer.toString(col, 16);
				array_color.add(value);
				array_color_int.add(col);
				
				if (i == (count + 4) ) {
					value_red = Color.red(col);
					value_green = Color.green(col);
					value_blue = Color.blue(col);
				}
			}
		}
	}
	
	private void genStringCommand() {
		int count = array_color.size();
		
		String colors = "";
		for (int i = 0; i < array_color.size(); i++) {
			colors += array_color.get(i);
			if (i < array_color.size() - 1) {
				colors += ",";
			}
		}
		
		if (count <= 0) {
			str_color_info = String.valueOf(value_play) + "," +
					String.valueOf(value_light) + "," +
					String.valueOf(value_flash) + "," +
					String.valueOf(i_bluetooth_mode) + "," + count;
		} else {
			str_color_info = String.valueOf(value_play) + "," +
					String.valueOf(value_light) + "," +
					String.valueOf(value_flash) + "," +
					String.valueOf(i_bluetooth_mode) + "," + count + "," + colors;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			iv_ok.performClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

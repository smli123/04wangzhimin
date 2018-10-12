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
import android.os.Bundle;
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
	private ImageView iv_show;
	
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
	
	int i_bluetooth_mode = 0;		// 0: ����ģʽ�� 1�� ����ģʽ
	int value_red = 0;
	int value_green = 0;
	int value_blue = 0;
	
	ArrayList<String> array_color = new ArrayList<String>();
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bt_selectcolor);
		mContext = this;
		
		mSharedPreferences = getSharedPreferences("SmartLight", Activity.MODE_PRIVATE);
		loadData();
		
		// load Data from intent
		Intent intent = getIntent();
		str_color_info = intent.getStringExtra("DATA");
		
		initView();
	}
	
	@SuppressLint("ResourceAsColor") 
	private void updateUI() {
		String[] cmds = str_color_info.split(",");
		if (cmds.length < 2)
			return;
		
		if (cmds.length == 2) {
			iv_show.setBackgroundColor(R.color.white);
			
		} else {
			int count = Integer.valueOf(cmds[1]);
			for (int i = 0; i < array_color.size(); i++) {
				
			}
			String col = "#" + array_color.get(count- 1);
			iv_show.setBackgroundColor(Color.parseColor(col));
		}
	}
	
	private void initView() {
	    iv_bluetooth_mode = (ImageView) findViewById(R.id.iv_bluetooth_mode);
		iv_ok = (ImageView) findViewById(R.id.iv_ok);
		iv_show = (ImageView) findViewById(R.id.iv_show);
		
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
		
		cp_colorpicker.setColorChangedListener(new ColorPickerView.OnColorChangedListener(){
			@Override
			public void onColorChanged(int color) {
 
			}
		});

		
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
	
	private void saveData() {
		editor = mSharedPreferences.edit();

		editor.putString("COLOR_01", str_color_info);

		editor.commit();
	}

	private void loadData() {
		str_color_info = mSharedPreferences.getString("COLOR_01", "1,1,ff0000");
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
			break;
		case R.id.iv_show:
			array_color.clear();
			genStringCommand();
			break;
		case R.id.iv_color_01:
			
			break;
		case R.id.iv_color_02:
			break;
		case R.id.iv_color_03:
			break;
		case R.id.iv_color_04:
			break;
		case R.id.iv_color_05:
			break;
		case R.id.iv_color_06:
			break;
		case R.id.iv_color_07:
			break;
		case R.id.iv_color_08:
			break;
		case R.id.iv_preview:
			array_color.add(Integer.toHexString(value_red) + Integer.toHexString(value_green) + Integer.toHexString(value_blue));
			genStringCommand();
			break;
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
			str_color_info = String.valueOf(i_bluetooth_mode) + "," + count;
		} else {
			str_color_info = String.valueOf(i_bluetooth_mode) + "," + count + "," + colors;
		}
		
		updateUI();
	}

}
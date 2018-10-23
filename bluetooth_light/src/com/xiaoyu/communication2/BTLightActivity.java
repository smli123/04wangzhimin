package com.xiaoyu.communication2;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyu.bluetooth.BTClient;
import com.xiaoyu.bluetooth.BTManage;
import com.xiaoyu.bluetooth.BTServer;
import com.xiaoyu.bluetooth.BluetoothMsg;

public class BTLightActivity extends Activity implements OnClickListener, SeekBar.OnSeekBarChangeListener {
	private Context mContext;
	private ListView mListView;  
    private Button sendButton;  
    private Button disconnectButton;  
    private EditText editMsgView;  
    private ArrayAdapter<String> mAdapter;  
    private List<String> msgList=new ArrayList<String>();
    
    RadioButton rb_none;
    RadioButton rb_client;
    RadioButton rb_server;
    
    private BTClient client;
    private BTServer server;
    
    // New content type
    private ImageView iv_bluetooth;
    private ImageView iv_close;
    private TextView iv_color_01;
    private TextView iv_color_02;
    private TextView iv_color_03;
    private TextView iv_color_04;
    private TextView iv_color_05;
    private TextView iv_color_06;
    private TextView iv_color_07;
    private TextView iv_color_08;
    private TextView iv_color_09;
    private TextView iv_color_10;
    private TextView iv_color_11;
    private TextView iv_color_12;
    
    private RelativeLayout rl_color_parameter;
    private RelativeLayout rl_color_parameter_01;
    
    private SeekBar sb_play;
    private SeekBar sb_light;
    private SeekBar sb_flash;
    
    private RelativeLayout rl_bottom;
    private TextView tv_log;
    
    private int value_play_pos = 0;
    private int value_light_pos = 0;
    private int value_flash_pos = 0;
    private int i_bluetooth_mode = 0;
    private int i_color_count = 1;
    
    private int i_index_color = -1;
    
	ArrayList<String> array_color = new ArrayList<String>();
    
    private String[] str_color_info = new String[12];
    
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bt_light);
		mContext = this;
		
		mSharedPreferences = getSharedPreferences("SmartLight", Activity.MODE_PRIVATE);
		loadData();
		
		initView();
		
		initViewShow();
		
		updateUI();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveData();
	}
	
	private void saveData() {
		editor = mSharedPreferences.edit();

		editor.putString("COLOR_01", str_color_info[0]);
		editor.putString("COLOR_02", str_color_info[1]);
		editor.putString("COLOR_03", str_color_info[2]);
		editor.putString("COLOR_04", str_color_info[3]);
		editor.putString("COLOR_05", str_color_info[4]);
		editor.putString("COLOR_06", str_color_info[5]);
		editor.putString("COLOR_07", str_color_info[6]);
		editor.putString("COLOR_08", str_color_info[7]);
		editor.putString("COLOR_09", str_color_info[8]);
		editor.putString("COLOR_10", str_color_info[9]);
		editor.putString("COLOR_11", str_color_info[10]);
		editor.putString("COLOR_12", str_color_info[11]);

		editor.commit();
	}

	private void loadData() {
		str_color_info[0] = mSharedPreferences.getString("COLOR_01", "0,0,0,1,1,ff0000");
		str_color_info[1] = mSharedPreferences.getString("COLOR_02", "0,0,0,1,1,966314");
		str_color_info[2] = mSharedPreferences.getString("COLOR_03", "0,0,0,1,1,0074E1");
		str_color_info[3] = mSharedPreferences.getString("COLOR_04", "0,0,0,1,1,966314");
		str_color_info[4] = mSharedPreferences.getString("COLOR_05", "0,0,0,1,1,FF5252");
		str_color_info[5] = mSharedPreferences.getString("COLOR_06", "0,0,0,1,1,FFFFFF");
		str_color_info[6] = mSharedPreferences.getString("COLOR_07", "0,0,0,1,1,FFCDD2");
		str_color_info[7] = mSharedPreferences.getString("COLOR_08", "0,0,0,1,1,FFC107");
		str_color_info[8] = mSharedPreferences.getString("COLOR_09", "0,0,0,1,1,3F51B5");
		str_color_info[9] = mSharedPreferences.getString("COLOR_10", "0,0,0,1,1,00BCD4");
		str_color_info[10] = mSharedPreferences.getString("COLOR_11", "0,0,0,1,1,388E3C");
		str_color_info[11] = mSharedPreferences.getString("COLOR_12", "0,0,0,1,1,C8E6C9");
	}

	private Handler detectedHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			msgList.add(msg.obj.toString());  
			mAdapter.notifyDataSetChanged();  
            mListView.setSelection(msgList.size() - 1);  
		};
	};
	
	
	 private void initView() {            
         
         mAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgList);  
         mListView = (ListView) findViewById(R.id.list);  
         mListView.setAdapter(mAdapter);  
         mListView.setFastScrollEnabled(true);  
         editMsgView= (EditText)findViewById(R.id.MessageText);    
         editMsgView.clearFocus();
         
         rb_none = (RadioButton)this.findViewById(R.id.radioNone);
         rb_client = (RadioButton)this.findViewById(R.id.radioClient);
         rb_server = (RadioButton)this.findViewById(R.id.radioServer);
         
         RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
         group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//             @Override
//             public void onCheckedChanged(RadioGroup arg0, int arg1) {
//                 int radioId = arg0.getCheckedRadioButtonId();
//                
//             }
        	 @Override
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		switch(checkedId){
        		case R.id.radioNone:
        			BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.NONE;
        			if(null!=client){
        				client.closeBTClient();
        				client=null;
        			}
        			if(null!=server){
        				server.closeBTServer();
        				server=null;
        			}
        			break;
        		case R.id.radioClient:
        			BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.CILENT;
        			Intent it=new Intent(getApplicationContext(),BTDeviceActivity.class);  
                    startActivityForResult(it, 100);
        			break;
        		case R.id.radioServer:
        			BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.SERVICE;
        			initConnecter();
        			break;
        		}
        	}
         });
         
         sendButton= (Button)findViewById(R.id.btn_msg_send);  
         sendButton.setOnClickListener(new OnClickListener() {  
             @Override  
             public void onClick(View arg0) {  
               
                 String msgText =editMsgView.getText().toString();  
                 if (msgText.length()>0) {  
                	 if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT){  
                		 if(null==client)
                    		 return;
                         if(client.sendmsg(msgText)){
                        	 Message msg = new Message();  
                             msg.obj = "send: "+msgText;  
                             msg.what = 1;  
                             detectedHandler.sendMessage(msg); 
                         }else{
                        	 Message msg = new Message();  
                             msg.obj = "send fail!! ";  
                             msg.what = 1;  
                             detectedHandler.sendMessage(msg); 
                         }
                      }  
                      else if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.SERVICE) {  
                    	  if(null==server)
                     		 return;
                    	  if(server.sendmsg(msgText)){
                         	 Message msg = new Message();  
                              msg.obj = "send: "+msgText;  
                              msg.what = 1;  
                              detectedHandler.sendMessage(msg); 
                          }else{
                         	 Message msg = new Message();  
                              msg.obj = "send fail!! ";  
                              msg.what = 1;  
                              detectedHandler.sendMessage(msg); 
                          }
                      }    
//                     editMsgView.setText("");  
//                     editMsgView.clearFocus();  
//                     //close InputMethodManager  
//                     InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
//                     imm.hideSoftInputFromWindow(editMsgView.getWindowToken(), 0);  
                 }else{  
                	 Toast.makeText(getApplicationContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
                 }
             }  
         });  
           
         disconnectButton= (Button)findViewById(R.id.btn_disconnect);  
         disconnectButton.setOnClickListener(new OnClickListener() {
             @Override  
             public void onClick(View arg0) {  
                 if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT){  
                	 if(null==client)
                		 return;
                    client.closeBTClient();
                 }  
                 else if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.SERVICE) { 
                	 if(null==server)
                		 return;
                     server.closeBTServer();
                 }  
                 BluetoothMsg.isOpen = false;  
                 BluetoothMsg.serviceOrCilent=BluetoothMsg.ServerOrCilent.NONE;  
                 Toast.makeText(getApplicationContext(), "已断开连接！", Toast.LENGTH_SHORT).show();  
             }  
         });

         rl_color_parameter = (RelativeLayout)findViewById(R.id.rl_color_parameter);
         rl_color_parameter.setVisibility(View.GONE);

         rl_color_parameter_01 = (RelativeLayout)findViewById(R.id.rl_color_parameter_01);
         
         sb_play = (SeekBar) findViewById(R.id.sb_play);
         sb_light = (SeekBar) findViewById(R.id.sb_light);
         sb_flash = (SeekBar) findViewById(R.id.sb_flash);
         
    	 sb_play.setProgress(value_play_pos);
    	 sb_play.setOnSeekBarChangeListener(this);

    	 sb_light.setProgress(value_light_pos);
    	 sb_light.setOnSeekBarChangeListener(this);

    	 sb_flash.setProgress(value_flash_pos);
    	 sb_flash.setOnSeekBarChangeListener(this);
     }
	 
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		switch(seekBar.getId()) {
		case R.id.sb_play:
			value_play_pos = seekBar.getProgress();
			break;
		case R.id.sb_light:
			value_light_pos = seekBar.getProgress();
			break;
		case R.id.sb_flash:
			value_flash_pos = seekBar.getProgress();
			break;
		}
		
		str_color_info[i_index_color] = toColorInfo();
		btSendCommand(str_color_info[i_index_color]);
	}
	 
	 @Override
	protected void onResume() {
		super.onResume();
		
		if (BluetoothMsg.isOpen) {
			Toast.makeText(getApplicationContext(), "连接已经打开，可以通信。如果要再建立连接，请先断开！",
					Toast.LENGTH_SHORT).show();
		}
	}
	 
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100){
			//从设备列表返回
			initConnecter();
		} else {
			 if (requestCode == 101){
			 	str_color_info[0] = data.getStringExtra("DATA");
			} else if (requestCode == 102){
				str_color_info[1] = data.getStringExtra("DATA");
			} else if (requestCode == 103){
				str_color_info[2] = data.getStringExtra("DATA");
			} else if (requestCode == 104){
				str_color_info[3] = data.getStringExtra("DATA");
			} else if (requestCode == 105){
				str_color_info[4] = data.getStringExtra("DATA");
			} else if (requestCode == 106){
				str_color_info[5] = data.getStringExtra("DATA");
			} else if (requestCode == 107){
				str_color_info[6] = data.getStringExtra("DATA");
			} else if (requestCode == 108){
				str_color_info[7] = data.getStringExtra("DATA");
			} else if (requestCode == 109){
				str_color_info[8] = data.getStringExtra("DATA");
			} else if (requestCode == 110){
				str_color_info[9] = data.getStringExtra("DATA");
			} else if (requestCode == 111){
				str_color_info[10] = data.getStringExtra("DATA");
			} else if (requestCode == 112){
				str_color_info[11] = data.getStringExtra("DATA");
			}
			 
			updateUI();
		}
	}
	 
	private void updateUI() {
		setTextViewBackgroundColor(iv_color_01, str_color_info[0]);
		setTextViewBackgroundColor(iv_color_02, str_color_info[1]);
		setTextViewBackgroundColor(iv_color_03, str_color_info[2]);
		setTextViewBackgroundColor(iv_color_04, str_color_info[3]);
		setTextViewBackgroundColor(iv_color_05, str_color_info[4]);
		setTextViewBackgroundColor(iv_color_06, str_color_info[5]);
		setTextViewBackgroundColor(iv_color_07, str_color_info[6]);
		setTextViewBackgroundColor(iv_color_08, str_color_info[7]);
		setTextViewBackgroundColor(iv_color_09, str_color_info[8]);
		setTextViewBackgroundColor(iv_color_10, str_color_info[9]);
		setTextViewBackgroundColor(iv_color_11, str_color_info[10]);
		setTextViewBackgroundColor(iv_color_12, str_color_info[11]);
	}
	
	@SuppressLint("NewApi") private void setTextViewBackgroundColor(TextView iv_color, String str_color) {
		String[] cmds = str_color.split(",");
		if (cmds.length < 5)
			return;
		
		int i_bluetooth_mode = Integer.valueOf(cmds[3]);
		
		if (cmds.length == 5) {
			// do nothing...
			return;
			
		} else {
			int count = Integer.valueOf(cmds[4]);
			
			int colors[] = new int[count];
			int now_color = 0, red = 0, green = 0, blue = 0;
			
			for (int i = 0; i < count; i++) {
				now_color = Integer.valueOf(cmds[i + 5], 16);
				red = Color.red(now_color);
				green = Color.green(now_color);
				blue = Color.blue(now_color);
				colors[i] = Color.rgb(red, green, blue);
			}
			
			int strokeWidth = 1;     						// 1dp 边框宽度
			int roundRadius = 5;     						// 5dp 圆角半径
			int strokeColor = Color.parseColor("#00FF00");	//边框颜色
			int fillColor = Color.rgb(red, green, blue); 	//内部填充颜色
			
			GradientDrawable gd = new GradientDrawable();	//创建drawable
			gd.setCornerRadius(roundRadius);
			gd.setStroke(strokeWidth, strokeColor);
			if (count == 1) {
				gd.setColor(fillColor);
				gd.setGradientType(GradientDrawable.RECTANGLE);
				iv_color.setBackgroundDrawable(gd);
//				iv_color.setBackgroundColor(Color.rgb(red, green, blue));
			} else {
				gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
				gd.setColors(colors);
				iv_color.setBackgroundDrawable(gd);
			}
		}
	}
	 
	private void initConnecter(){
		if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT) {
			String address = BluetoothMsg.BlueToothAddress;
			if (!TextUtils.isEmpty(address)) {
				if(null==client)
					client=new BTClient(BTManage.getInstance().getBtAdapter(), detectedHandler);
				client.connectBTServer(address);
				BluetoothMsg.isOpen = true;
			} else {
				Toast.makeText(getApplicationContext(), "address is empty please choose server address !",
						Toast.LENGTH_SHORT).show();
			}
		} else if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.SERVICE) {
			if(null==server)
				server=new BTServer(BTManage.getInstance().getBtAdapter(), detectedHandler);
			server.startBTServer();
			BluetoothMsg.isOpen = true;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void fromColorInfo(String color_info) {
		array_color.clear();
		
		String[] cmds = color_info.split(",");
		if (cmds.length < 5)
			return;
		value_play_pos = Integer.valueOf(cmds[0]);
		value_light_pos = Integer.valueOf(cmds[1]);
		value_flash_pos = Integer.valueOf(cmds[2]);
		i_bluetooth_mode = Integer.valueOf(cmds[3]);
		
		if (cmds.length == 5) {
			// do nothing...
		} else {
			i_color_count = Integer.valueOf(cmds[4]);
			for (int i = 5; i < i_color_count + 5; i++) {
				int col = Integer.valueOf(cmds[i], 16);
				
				String value = Integer.toString(col, 16);
				array_color.add(value);
			}
		}
	}

	private String toColorInfo() {
		String colors = "";
		for (int i = 0; i < array_color.size(); i++) {
			colors += array_color.get(i);
			if (i < array_color.size() - 1) {
				colors += ",";
			}
		}
		
		String color_info = "";
		
		if (i_color_count <= 0) {
			color_info = String.valueOf(value_play_pos) + "," +
					String.valueOf(value_light_pos) + "," +
					String.valueOf(value_flash_pos) + "," +
					String.valueOf(i_bluetooth_mode);
		} else {
			color_info = String.valueOf(value_play_pos) + "," +
					String.valueOf(value_light_pos) + "," +
					String.valueOf(value_flash_pos) + "," +
					String.valueOf(i_bluetooth_mode) + "," +
					String.valueOf(i_color_count) + "," + colors;
		}
		
		return color_info;
	}

	private void initViewShow() {
		iv_bluetooth = (ImageView) findViewById(R.id.iv_bluetooth);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_color_01 = (TextView) findViewById(R.id.iv_color_01);
		iv_color_02 = (TextView) findViewById(R.id.iv_color_02);
		iv_color_03 = (TextView) findViewById(R.id.iv_color_03);
		iv_color_04 = (TextView) findViewById(R.id.iv_color_04);
		iv_color_05 = (TextView) findViewById(R.id.iv_color_05);
		iv_color_06 = (TextView) findViewById(R.id.iv_color_06);
		iv_color_07 = (TextView) findViewById(R.id.iv_color_07);
		iv_color_08 = (TextView) findViewById(R.id.iv_color_08);
		iv_color_09 = (TextView) findViewById(R.id.iv_color_09);
		iv_color_10 = (TextView) findViewById(R.id.iv_color_10);
		iv_color_11 = (TextView) findViewById(R.id.iv_color_11);
		iv_color_12 = (TextView) findViewById(R.id.iv_color_12);
		
		tv_log = (TextView) findViewById(R.id.tv_log);
		
		iv_bluetooth.setOnClickListener(this);
		iv_close.setOnClickListener(this);

		iv_color_01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 0;
				updateUIAndSendComamnd();
			}
		});

		iv_color_01.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class); 
				it.putExtra("DATA", str_color_info[0]);
				it.putExtra("NUM", 101);
                startActivityForResult(it, 101);
				return false;
			}
		});

		iv_color_02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 1;
				updateUIAndSendComamnd();
			}
		});

		iv_color_02.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[1]);
				it.putExtra("NUM", 102);
                startActivityForResult(it, 102);
				return false;
			}
		});

		iv_color_03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 2;
				updateUIAndSendComamnd();
			}
		});

		iv_color_03.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);  
				it.putExtra("DATA", str_color_info[2]);
				it.putExtra("NUM", 103);
                startActivityForResult(it, 103);
				return false;
			}
		});

		iv_color_04.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 3;
				updateUIAndSendComamnd();
			}
		});

		iv_color_04.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[3]);
				it.putExtra("NUM", 104);
                startActivityForResult(it, 104);
				return false;
			}
		});

		iv_color_05.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 4;
				updateUIAndSendComamnd();
			}
		});

		iv_color_05.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[4]);
				it.putExtra("NUM", 105);
                startActivityForResult(it, 105);
				return false;
			}
		});

		iv_color_06.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 5;
				updateUIAndSendComamnd();
			}
		});

		iv_color_06.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[5]);
				it.putExtra("NUM", 106);
                startActivityForResult(it, 106);
				return false;
			}
		});

		iv_color_07.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 6;
				updateUIAndSendComamnd();
			}
		});

		iv_color_07.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[6]);
				it.putExtra("NUM", 107);
                startActivityForResult(it, 107);
				return false;
			}
		});

		iv_color_08.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 7;
				updateUIAndSendComamnd();
			}
		});

		iv_color_08.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[7]);
				it.putExtra("NUM", 108);
                startActivityForResult(it, 108);
				return false;
			}
		});

		iv_color_09.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 8;
				updateUIAndSendComamnd();
			}
		});

		iv_color_09.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[8]);
				it.putExtra("NUM", 109);
                startActivityForResult(it, 109);
				return false;
			}
		});

		iv_color_10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 9;
				updateUIAndSendComamnd();
			}
		});

		iv_color_10.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[9]);
				it.putExtra("NUM", 110);
                startActivityForResult(it, 110);
				return false;
			}
		});

		iv_color_11.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 10;
				updateUIAndSendComamnd();
			}
		});

		iv_color_11.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[10]);
				it.putExtra("NUM", 111);
                startActivityForResult(it, 111);
				return false;
			}
		});

		iv_color_12.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i_index_color = 11;
				updateUIAndSendComamnd();
			}
		});

		iv_color_12.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent it = new Intent(getApplicationContext(),BTColorSelectorActivity.class);
				it.putExtra("DATA", str_color_info[11]);
				it.putExtra("NUM", 112);
                startActivityForResult(it, 112);
				return false;
			}
		});
		
	}
	
	private void updateUIAndSendComamnd() {
		fromColorInfo(str_color_info[i_index_color]);
		
		rl_color_parameter.setVisibility(View.VISIBLE);
		if (i_color_count <= 1) {
			rl_color_parameter_01.setVisibility(View.GONE);
		} else {
			rl_color_parameter_01.setVisibility(View.VISIBLE);
		}
		
		sb_play.setProgress(value_play_pos);
		sb_light.setProgress(value_light_pos);
		sb_flash.setProgress(value_flash_pos);

		btSendCommand(str_color_info[i_index_color]);	// Mode, Count, color01, color02, color03
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.iv_bluetooth:
			rb_none.setChecked(true);
			rb_client.setChecked(true);
			break;
		}
	}
	
	private void btSendCommand(String command) {
		editMsgView.setText(command);
		sendButton.performClick();
		
		tv_log.setText(command);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			saveData();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

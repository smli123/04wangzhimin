package com.xiaoyu.communication2;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.xiaoyu.bluetooth.BTClient;
import com.xiaoyu.bluetooth.BTManage;
import com.xiaoyu.bluetooth.BTServer;
import com.xiaoyu.bluetooth.BluetoothMsg;

public class BTChatActivity extends Activity {

	private ListView mListView;  
    private Button sendButton;  
    private Button disconnectButton;  
    private EditText editMsgView;  
    private ArrayAdapter<String> mAdapter;  
    private List<String> msgList=new ArrayList<String>();
    
    private BTClient client;
    private BTServer server;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bt_chat);
		initView();

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
                     editMsgView.setText("");  
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
		if(requestCode==100){
			//从设备列表返回
			initConnecter();
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

}

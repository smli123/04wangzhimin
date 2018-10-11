package com.xiaoyu.bluetooth;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiaoyu.utils.ThreadPool;

public class BTClient {

	final String Tag=getClass().getSimpleName();
	private BluetoothSocket btsocket = null;
	private BluetoothDevice btdevice = null;
	private BufferedInputStream bis=null;
	private BufferedOutputStream bos=null;
	private BluetoothAdapter mBtAdapter =null;
	
	private Handler detectedHandler=null;
	
	public BTClient(BluetoothAdapter mBtAdapter,Handler detectedHandler){
		this.mBtAdapter=mBtAdapter;
		this.detectedHandler=detectedHandler;
	}
	
	public void connectBTServer(String address){
		//check address is correct
		if(BluetoothAdapter.checkBluetoothAddress(address)){
			btdevice = mBtAdapter.getRemoteDevice(address);	
				ThreadPool.getInstance().excuteTask(new Runnable() {
					public void run() {
						try {
							btsocket = btdevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
							
							Message msg2 = new Message();  
		                    msg2.obj = "���Ժ��������ӷ�����:"+BluetoothMsg.BlueToothAddress;  
		                    msg2.what = 0;  
		                    detectedHandler.sendMessage(msg2);  
		                      
		                    btsocket.connect(); 
		                    Message msg = new Message();  
		                    msg.obj = "�Ѿ������Ϸ���ˣ����Է�����Ϣ��";  
		                    msg.what = 0;  
		                    detectedHandler.sendMessage(msg);  
							receiverMessageTask();
						} catch (IOException e) {
							e.printStackTrace();
							Log.e(Tag, e.getMessage());
							
							 Message msg = new Message();  
			                 msg.obj = "���ӷ�����쳣������������Ƿ��������Ͽ�����������һ�ԡ�";  
			                 msg.what = 0;  
			                detectedHandler.sendMessage(msg);
						}
						
					}
				});
			}
	}
	
	private void receiverMessageTask(){
		ThreadPool.getInstance().excuteTask(new Runnable() {
			public void run() {
				byte[] buffer = new byte[2048];
	            int totalRead;
	            /*InputStream input = null;
	            OutputStream output=null;*/
	            try {
					bis=new BufferedInputStream(btsocket.getInputStream());
					bos=new BufferedOutputStream(btsocket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            try {
	            //	ByteArrayOutputStream arrayOutput=null;
					while((totalRead = bis.read(buffer)) > 0 ){
				//		 arrayOutput=new ByteArrayOutputStream();
						String txt = new String(buffer, 0, totalRead, "UTF-8"); 
						Message msg = new Message();  
                        msg.obj = "Receiver: "+txt;  
                        msg.what = 1;  
                        detectedHandler.sendMessage(msg);  
					}
				} catch(EOFException e){
					Message msg = new Message();  
                    msg.obj = "server has close!";  
                    msg.what = 1;  
                    detectedHandler.sendMessage(msg);
				}catch (IOException e) {
					e.printStackTrace();
					Message msg = new Message();  
                    msg.obj = "receiver message error! make sure server is ok,and try again connect!";  
                    msg.what = 1;  
                    detectedHandler.sendMessage(msg);
				}
			}
		});
	}
	
	public boolean sendmsg(String msg){
		boolean result=false;
		if(null==btsocket||bos==null)
			return false;
		try {
			bos.write(msg.getBytes());
			bos.flush();
			result=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void closeBTClient(){
		try{
			if(bis!=null)
				bis.close();
			if(bos!=null)
				bos.close();
			if(btsocket!=null)
				btsocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}

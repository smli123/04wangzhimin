package com.xiaoyu.bluetooth;

public class BluetoothMsg {

    public enum ServerOrCilent{  
        NONE,  
        SERVICE,  
        CILENT  
    };  
    //�������ӷ�ʽ  
    public static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;  
    //����������ַ  
    public static String BlueToothAddress = null,lastblueToothAddress=null;  
    //ͨ���߳��Ƿ���  
    public static boolean isOpen = false;  
}

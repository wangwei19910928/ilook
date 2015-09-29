package com.fywl.ILook.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MyUtils {
	
	/**
	 * 获取本机mac地址
	 * @return
	 */
	public static String getLocalMac(){
		InetAddress ia;
		byte[] mac = null;
		try {
			ia = InetAddress.getLocalHost();
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获取网卡，获取地址
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:"+str);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
	
	
	
	
	
	public static void main(String[] args) {
	}
}

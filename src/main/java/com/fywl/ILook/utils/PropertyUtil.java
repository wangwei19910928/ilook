package com.fywl.ILook.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertyUtil {
	
	
	/**
	 * 设置本地存储
	 * */
	public static void setValue(Properties properties,File sFile) {
		try {
			if (!sFile.exists()) {
				sFile.createNewFile();
			}
			//读出原有的
			FileInputStream fis = new FileInputStream(sFile);
			Properties old = new Properties();
			old.load(fis);
			
			old.putAll(properties);
			
			FileOutputStream fos = new FileOutputStream(sFile);
			old.store(fos, "fywl");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.fywl.ILook.utils;


import java.text.SimpleDateFormat;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class RegeditTool {
	static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	  
    /** *//** */  
    /** *//** Creates a new instance of test */  
  
    // 把信息存储到注册表HKEY_LOCAL_MACHINE下的某个节点的某一变量中，有则修改，无则创建  
    public static boolean setValue(String folder, String subKeyNode,  
            String subKeyName, String subKeyValue) {  
        try {  
            RegistryKey software = Registry.HKEY_LOCAL_MACHINE  
                    .openSubKey(folder);  
            RegistryKey subKey = software.createSubKey(subKeyNode, "");  
            subKey  
                    .setValue(new RegStringValue(subKey, subKeyName,  
                            subKeyValue));  
            subKey.closeKey();  
            return true;  
        } catch (NoSuchKeyException e) {  
            e.printStackTrace();  
        } catch (NoSuchValueException e) {  
            e.printStackTrace();  
        } catch (RegistryException e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
  
    // 删除注册表中某节点下的某个变量  
    public static boolean deleteValue(String folder, String subKeyNode,  
            String subKeyName) {  
          
        try {  
            RegistryKey software = Registry.HKEY_LOCAL_MACHINE  
                    .openSubKey(folder);  
            RegistryKey subKey = software.createSubKey(subKeyNode, "");  
            subKey.deleteValue(subKeyName);  
            subKey.closeKey();  
            return true;  
        } catch (NoSuchKeyException e) {  
            System.out.println("NOsuchKey_delete");  
        } catch (NoSuchValueException e) {  
            System.out.println("NOsuchValue_delete");  
        } catch (RegistryException e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
  
    // 删除注册表中某节点下的某节点  
    public static boolean deleteSubKey(String folder, String subKeyNode) {  
        try {  
            RegistryKey software = Registry.HKEY_LOCAL_MACHINE  
                    .openSubKey(folder);  
            software.deleteSubKey(subKeyNode);  
            software.closeKey();  
            return true;  
        } catch (NoSuchKeyException e) {  
            e.printStackTrace();  
        } catch (RegistryException e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
  
    // 打开注册表项并读出相应的变量名的值  
    public static String getValue(String folder, String subKeyNode,  
            String subKeyName) {  
        String value = "";  
        try {  
            RegistryKey software = Registry.HKEY_LOCAL_MACHINE  
                    .openSubKey(folder);  
            RegistryKey subKey = software.openSubKey(subKeyNode);  
            value = subKey.getStringValue(subKeyName);  
            subKey.closeKey();  
        } catch (NoSuchKeyException e) {  
            value = "NoSuchKey";  
            // e.printStackTrace();  
        } catch (NoSuchValueException e) {  
            value = "NoSuchValue";  
            // e.printStackTrace();  
        } catch (RegistryException e) {  
            e.printStackTrace();  
        }  
        return value;  
    }  
  
    // 测试  
    public static void main(String[] args) {  
        setValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run", "test",  
                "C:\\1.exe");  
    }  
}

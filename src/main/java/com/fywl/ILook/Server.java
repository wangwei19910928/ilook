package com.fywl.ILook;

import java.awt.Toolkit;

import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.ui.mw.impl.FunctionMainWindow;





public class Server {

	public static void main(String[] args) {
		InfoBean ib = new InfoBean();
		ib.setName("张三疯");
		ib.setSchool("北京市第三中学");
		ib.setTrainAge(3+"");
		ib.setType("体育");
		
		RecordConfig rc = RecordConfig.get();
		rc.setSingleRecording(true);
		rc.setVideoSize(Toolkit.getDefaultToolkit().getScreenSize());
		new FunctionMainWindow(ib,rc);
		

	}

}

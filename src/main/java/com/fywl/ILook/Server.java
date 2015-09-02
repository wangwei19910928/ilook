package com.fywl.ILook;

import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.ui.mw.impl.FunctionMainWindow;





public class Server {

	public static void main(String[] args) {
		InfoBean ib = new InfoBean();
		ib.setName("张三疯");
		ib.setSchool("北京市第三中学");
		ib.setTrainAge(3);
		ib.setType("体育");
		new FunctionMainWindow(ib);

	}

}

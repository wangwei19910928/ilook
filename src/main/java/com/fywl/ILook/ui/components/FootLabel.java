package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;

public class FootLabel extends Composite {

	public FootLabel(Composite parent, int style) {
		super(parent, style);
		init();
	}

	private void init() {
		Label v = new MyLabel(this, SWT.NONE,
				Constants.FOOT_LABEL_Constant.VERSION_CONTENT,
				Constants.FOOT_LABEL_Constant.VERSION_LOCATION);
		v.setForeground(SWTResourceManager
				.getColor(Constants.FOOT_LABEL_Constant.VERSION_COLOR));
		Label w = new MyLabel(this, SWT.NONE,
				Constants.Shell_Constant.WEBSITE,
				Constants.FOOT_LABEL_Constant.WEBSITE_LOCATION);
		w.setAlignment(SWT.RIGHT);
		w.setForeground(SWTResourceManager
				.getColor(Constants.FOOT_LABEL_Constant.WEBSITE_COLOR));
	}

}

package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.utils.ImageUtil;

public class InfoLaber extends Composite {
	
	private InfoBean ib;

	public InfoLaber(Composite parent, int style,InfoBean ib) {
		super(parent, style);
		this.ib = ib;
		init();
	}
	
	
	private void init(){
		Label icon = new Label(this, SWT.NONE);
		icon.setBounds(Constants.INFO_LABEL_Constant.ICON_LOCATION[0],
				Constants.INFO_LABEL_Constant.ICON_LOCATION[1],
				Constants.INFO_LABEL_Constant.ICON_LOCATION[2],
				Constants.INFO_LABEL_Constant.ICON_LOCATION[3]);
		icon.setImage(ImageUtil.getInstance().getImage(this.getDisplay(),this.getClass().getResourceAsStream(
				Constants.INFO_LABEL_Constant.ICON_URL)));
		
		new MyLabel(this, SWT.NONE, ib.getSchool(),Constants.INFO_LABEL_Constant.SCHOOL);
		new MyLabel(this, SWT.NONE, ib.getName(),Constants.INFO_LABEL_Constant.NAME);
		new MyLabel(this, SWT.NONE, ib.getType(),Constants.INFO_LABEL_Constant.TYPE);
		new MyLabel(this, SWT.NONE, "教龄： "+ib.getTrainAge()+"年",Constants.INFO_LABEL_Constant.AGE);
	}

}

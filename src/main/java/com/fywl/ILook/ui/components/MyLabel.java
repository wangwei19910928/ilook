package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class MyLabel extends Label{

	public MyLabel(Composite parent, int style,String str,int[] location) {
		super(parent, style);
		checkSubclass();
		init(str,location);
	}
	
	protected void checkSubclass(){  
        
    } 
	
	private void init(String str,int[] location){
		setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 12, SWT.BOLD));
		setBounds(location[0], location[1], location[2], location[3]);
		setText(str);
	}

}

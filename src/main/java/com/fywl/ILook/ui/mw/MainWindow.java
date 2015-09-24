package com.fywl.ILook.ui.mw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;

public  class MainWindow {

	protected Display display = Display.getDefault();

	protected Shell shell = new Shell(display, SWT.ON_TOP);
	
	
	public MainWindow() {
//		shell.setText("爱录课");
	}
	

	protected void init() {
		beforeInit();
		
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(Constants.Shell_Constant.WIDTH,
				Constants.Shell_Constant.HEIGHT);
		
		beforeComponentsInit();
		componentsInit();
		afterComponentsInit();

		shell.open();
		shell.layout();
		
		afterInit();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	protected  void beforeComponentsInit(){};
	
	protected  void componentsInit(){};
	
	protected  void afterComponentsInit(){};
	
	protected  void beforeInit(){};
	
	protected  void afterInit(){};
}

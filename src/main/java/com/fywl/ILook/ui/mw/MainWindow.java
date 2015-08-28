package com.fywl.ILook.ui.mw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;

public  class MainWindow {

	protected Display display = Display.getDefault();

	protected Shell shell = new Shell(display, SWT.ON_TOP);

	protected RecordConfig config = new RecordConfig();
	
	
	public MainWindow() {
		init();
	}
	

	private void init() {
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
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		
		afterInit();
	}
	
	
	protected  void beforeComponentsInit(){};
	
	protected  void componentsInit(){};
	
	protected  void afterComponentsInit(){};
	
	protected  void beforeInit(){};
	
	protected  void afterInit(){};
}

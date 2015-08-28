package com.fywl.ILook.ui.mw.impl;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.FootLabel;
import com.fywl.ILook.ui.components.InfoLaber;
import com.fywl.ILook.ui.components.MyLabel;
import com.fywl.ILook.ui.components.ScreenPlayBackPanel;
import com.fywl.ILook.ui.components.VideoPlayBackPanel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.Panel;
import com.fywl.ILook.ui.components.panel.impl.TitlePanel;
import com.fywl.ILook.ui.components.panel.impl.ToolPanel;
import com.fywl.ILook.ui.mw.MainWindow;
import com.fywl.ILook.utils.ImageUtil;

public class TestTimer extends MainWindow implements Closer{
	
	public static void main(String[] args) {
		new TestTimer();
	}
	
	private Panel toolPanel;
	 private Composite statusbar;
	    private Label statusbarLabel;
	    private ProgressBar progressBar;
	    private Button hideProbarButton;
	    
	    
	
	private void createMainComp(Composite parent) {
//		 toolPanel = new ToolPanel(this, shell, SWT.NONE, new VideoRecorder(new ScreenPlayBackPanel(shell, 0),new VideoPlayBackPanel(shell, new RecordConfig(), 0)));
//		toolPanel.setLocation(Constants.TOOL_Constant.LOCATION_X,
//				Constants.TOOL_Constant.LOCATION_Y);
//		toolPanel.setSize(Constants.TOOL_Constant.WIDTH,
//				Constants.TOOL_Constant.HEIGHT);
        createButton(toolPanel);
}
	
	private void createButton(Composite parent) {
        final Button b1 = new Button(parent, SWT.NONE);
        b1.setText("隐藏状态栏");
        b1.setBounds(100, 0, 60, 29);
        b1.setBackground(SWTResourceManager.getColor(5, 109, 191));
        b1.addSelectionListener(new SelectionAdapter() {
                 private boolean flag = true;
                 public void widgetSelected(SelectionEvent e) {
                          // 用statusbar.setVisible(false)来隐藏状态栏是不够的，还必须把它占用的空间也释放出来，这时应该用GridData.exclude
                          
                      
                          shell.layout();
                          b1.setText((flag ? "显示" : "隐藏") + "状态栏");
                          flag = !flag;
                 }
        });
        hideProbarButton = new Button(parent, SWT.NONE);
        hideProbarButton.setText("隐藏进度条");
        hideProbarButton.setBounds(0, 0, 60, 29);
        hideProbarButton.setEnabled(false);
        hideProbarButton.addSelectionListener(new SelectionAdapter() {
                 private boolean flag = false;
                 public void widgetSelected(SelectionEvent e) {
                          progressBar.setVisible(flag);
                          hideProbarButton.setText((flag ? "隐藏" : "显示") + "进度条");
                          flag = !flag;
                 }
        });
        final Button b3 = new Button(parent, SWT.NONE);
        b3.setText(" GO ");
        b3.setBounds(180, 0, 33, 29);
        b3.setBackground(SWTResourceManager.getColor(5, 109, 191));
        b3.addSelectionListener(new SelectionAdapter() {
                 private boolean stopFlag = true;
                 public void widgetSelected(SelectionEvent e) {
                          stopFlag = !stopFlag;
                          if (stopFlag) // 根据停止标志stopFlag来判断是停止还是运行
                                    stop();
                          else
                                    go();
                 }
                 private void stop() {
                          b3.setEnabled(false);// 停止需要时间，在完全停止前要防止再次开始。
                          b3.setText("GO");
                 }
                 private void go() {
                          b3.setText("STOP");
                          progressBar = createProgressBar(statusbar);
                          hideProbarButton.setEnabled(true);
                          new Thread() {
                                    public void run() {
                                             for (int i = 1; i < 11; i++) {
                                                      if (display.isDisposed() || stopFlag) {
                                                                disposeProgressBar();
                                                                return;
                                                      }
                                                      moveProgressBar(i);
                                                      try {  Thread.sleep(1000);          } catch (Throwable e2) {} //停一秒
                                             }
                                             disposeProgressBar();
                                    }
                                    private void moveProgressBar(final int i) {
                                             display.asyncExec(new Runnable() {
                                                      public void run() {
                                                                if (!statusbarLabel.isDisposed())
                                                                         statusbarLabel.setText("前进到第" + i + "步");
                                                                if (!progressBar.isDisposed())
                                                                         progressBar.setSelection(i * 10);
                                                      }
                                             });
                                    }
                                    private void disposeProgressBar() {
                                             if (display.isDisposed())   return;
                                             display.asyncExec(new Runnable() {
                                                      public void run() {
                                                                hideProbarButton.setEnabled(false);
                 // 这一句不能放在线程外执行，否则progressBar被创建后就立即被dispose了
                                                                progressBar.dispose();
                                                                b3.setEnabled(true);
                                                      }
                                             });
                                    }
                          }.start();
                 }
        });
}
	
	
	private void createStatusbar(Composite parent) {
        statusbar = new Composite(parent, SWT.BORDER);
        statusbar.setLocation(300,
				0);
        statusbar.setSize(200,
				30);
//        statusbar.setBackground(SWTResourceManager.getColor(5, 109, 191));
        //设置工具栏在Shell中的形状为水平抢占充满，并高19像素
        //设置为用行列式布局管理状态栏里的组件
        
        //创建一个用于显示文字的标签
        int[] a = {0,0,50,30};
        statusbarLabel = new MyLabel(statusbar, SWT.NONE, "123", a);
}
//创建进度条
private ProgressBar createProgressBar(Composite parent) {
        ProgressBar progressBar = new ProgressBar(parent, SWT.SMOOTH);
        progressBar.setMinimum(0); // 最小值
        progressBar.setMaximum(100);// 最大值
        progressBar.setLocation(50, 0);
        progressBar.setSize(200, 10);
        return progressBar;
}






private ScreenPlayBackPanel screenPlayBackPanel;

private VideoPlayBackPanel faceVideoPlayback;

private VideoPlayBackPanel otherVideoPlayback;

private VideoRecorder recorder;

// 出事最小化到系统托盘的监听
private Tray tray;

@Override
protected void beforeInit() {
	screenPlayBackPanel = new ScreenPlayBackPanel(shell, SWT.NO_BACKGROUND);

	faceVideoPlayback = new VideoPlayBackPanel(shell, config, 0);

	otherVideoPlayback = new VideoPlayBackPanel(shell, config, 1);

	recorder = new VideoRecorder(screenPlayBackPanel, otherVideoPlayback);

	tray = display.getSystemTray();
}

@Override
protected void beforeComponentsInit() {
	initSmallFunction();

	initChangeCam();

	Runtime.getRuntime().addShutdownHook(new Thread() {
		@Override
		public void run() {
			shutDown();
		}
	});
}

@Override
protected void componentsInit() {
	initTitlePanel();

	initInfoLaber();

	initVideoPlayBackPanel();

	initScreenPanel();

	initOtherVideoPlayBackPanel();

	initToolPanel();

	createMainComp(shell);//创建主面板
    createStatusbar(toolPanel);//创建工具栏
}

@Override
public void shutDown() {
	if (faceVideoPlayback.isRunning()) {
		faceVideoPlayback.stop();
	}
	if (otherVideoPlayback.isRunning()) {
		otherVideoPlayback.stop();
	}
	shell.dispose();
}

// 出事最小化到系统托盘的监听
private void initSmallFunction() {
	// 构造系统栏控件
	TrayItem trayItem = new TrayItem(tray, SWT.NONE);
	trayItem.setImage(ImageUtil.getInstance().getImage("images/record.png"));
	trayItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			toggleDisplay();
		}
	});
}

// 最小化功能
private void toggleDisplay() {
	try {
		shell.setVisible(!shell.isVisible()); // 控制窗口顯示
		tray.getItem(0).setVisible(!shell.isVisible()); // 控制托盤圖標顯示

		if (shell.getVisible()) {
			shell.setMinimized(false);
			shell.setActive();
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

// 初始化顶部logo、标题、最小化和关闭按钮
private void initTitlePanel() {
	Panel titlePanel = new TitlePanel(this, shell, SWT.NONE, recorder);
	titlePanel.setLocation(Constants.TITLE_Constant.LOCATION_X,
			Constants.TITLE_Constant.LOCATION_Y);
	titlePanel.setSize(Constants.TITLE_Constant.WIDTH,
			Constants.TITLE_Constant.HEIGHT);
}

// 初始化脸部摄像头
private void initVideoPlayBackPanel() {
	faceVideoPlayback.setBounds(Constants.Face_Constant.LOCATION_X,
			Constants.Face_Constant.LOCATION_Y,
			Constants.Face_Constant.WIDTH, Constants.Face_Constant.HEIGHT);
	faceVideoPlayback.start();
}

// 初始化屏幕监控
private void initScreenPanel() {

	screenPlayBackPanel.addImageRecorder(recorder);
	screenPlayBackPanel.setSize(Constants.SCREEN_Constant.WIDTH,
			Constants.SCREEN_Constant.HEIGHT);
	screenPlayBackPanel.setLocation(Constants.SCREEN_Constant.LOCATION_X,
			Constants.SCREEN_Constant.LOCATION_Y);
	screenPlayBackPanel.start();
}

// 初始化笔记摄像头
private void initOtherVideoPlayBackPanel() {
	otherVideoPlayback.setBounds(Constants.NOTE_Constant.LOCATION_X,
			Constants.NOTE_Constant.LOCATION_Y,
			Constants.NOTE_Constant.WIDTH, Constants.NOTE_Constant.HEIGHT);
	otherVideoPlayback.start();
}

// 初始化教师信息
private void initInfoLaber() {
	com.fywl.ILook.ui.components.panel.Label info = new InfoLaber(shell,
			SWT.INHERIT_DEFAULT, null);
	info.create();
	info.setLocation(Constants.INFO_LABEL_Constant.LOCATION_X,
			Constants.INFO_LABEL_Constant.LOCATION_Y);
	info.setSize(Constants.INFO_LABEL_Constant.WIDTH,
			Constants.INFO_LABEL_Constant.HEIGHT);
	Image infoImage = new Image(info.getDisplay(), this.getClass()
			.getResourceAsStream("/images/info.png"));
	info.setBackgroundImage(infoImage);
	info.setBackgroundMode(SWT.INHERIT_DEFAULT);
}

// 初始化监听切换摄像头
private void initChangeCam() {
	display.addFilter(SWT.KeyDown, new Listener() {
		@Override
		public void handleEvent(Event e) {
			if ((e.stateMask == SWT.CTRL) && (e.keyCode == 'a')) {
				recorder.changeSource();
			}
		}
	});
}

// 初始化底部工具条
private void initToolPanel() {
	toolPanel = new ToolPanel(this, shell, SWT.NONE, recorder);
	toolPanel.setLocation(Constants.TOOL_Constant.LOCATION_X,
			Constants.TOOL_Constant.LOCATION_Y);
	toolPanel.setSize(Constants.TOOL_Constant.WIDTH,
			Constants.TOOL_Constant.HEIGHT);
}


}

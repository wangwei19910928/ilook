package com.fywl.ILook.ui.mw.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.FootLabel;
import com.fywl.ILook.ui.components.InfoLaber;
import com.fywl.ILook.ui.components.ScreenPlayBackPanel;
import com.fywl.ILook.ui.components.VideoPlayBackPanel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.Panel;
import com.fywl.ILook.ui.components.panel.impl.TitlePanel;
import com.fywl.ILook.ui.components.panel.impl.ToolPanel;
import com.fywl.ILook.ui.mw.MainWindow;
import com.fywl.ILook.utils.ImageUtil;

public class TestMainWindow extends MainWindow implements Closer {

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

		initFootLabel();

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
		titlePanel.setLocation(Constants.TITLE_PANEL_Constant.LOCATION[0],
				Constants.TITLE_PANEL_Constant.LOCATION[1]);
		titlePanel.setSize(Constants.TITLE_PANEL_Constant.LOCATION[2],
				Constants.TITLE_PANEL_Constant.LOCATION[3]);
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
		InfoLaber info = new InfoLaber(shell, SWT.INHERIT_DEFAULT, null);

		info.setLocation(Constants.INFO_LABEL_Constant.LOCATION[0],
				Constants.INFO_LABEL_Constant.LOCATION[1]);
		info.setSize(Constants.INFO_LABEL_Constant.LOCATION[2],
				Constants.INFO_LABEL_Constant.LOCATION[3]);

		Image infoImage = ImageUtil.getInstance().getImage(
				info.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.INFO_LABEL_Constant.BACKGROUND_URL));
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
		Panel toolPanel = new ToolPanel(this, shell, SWT.NONE, recorder);
		toolPanel.setLocation(Constants.TOOL_PANEL_Constant.LOCATION[0],
				Constants.TOOL_PANEL_Constant.LOCATION[1]);
		toolPanel.setSize(Constants.TOOL_PANEL_Constant.LOCATION[2],
				Constants.TOOL_PANEL_Constant.LOCATION[3]);
		
		toolPanel.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	// 初始化底部信息
	private void initFootLabel() {
		FootLabel footLabel = new FootLabel(shell, SWT.NONE);
		footLabel.setLocation(Constants.FOOT_LABEL_Constant.LOCATION[0],
				Constants.FOOT_LABEL_Constant.LOCATION[1]);
		footLabel.setSize(Constants.FOOT_LABEL_Constant.LOCATION[2],
				Constants.FOOT_LABEL_Constant.LOCATION[3]);
		footLabel.setBackground(SWTResourceManager.getColor(
				Constants.FOOT_LABEL_Constant.BACKGROUND[0],
				Constants.FOOT_LABEL_Constant.BACKGROUND[1],
				Constants.FOOT_LABEL_Constant.BACKGROUND[2]));
	}

}

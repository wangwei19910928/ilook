package com.fywl.ILook.ui.mw.impl;

import java.awt.Dimension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.FootLabel;
import com.fywl.ILook.ui.components.InfoLaber;
import com.fywl.ILook.ui.components.MyLabel;
import com.fywl.ILook.ui.components.ScreenPlayBackPanel;
import com.fywl.ILook.ui.components.VideoPlayBackPanel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.impl.TitlePanel;
import com.fywl.ILook.ui.components.panel.impl.ToolPanel;
import com.fywl.ILook.ui.mw.MainWindow;
import com.fywl.ILook.utils.ImageUtil;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class FunctionMainWindow extends MainWindow implements Closer {

	protected RecordConfig config;

	private VideoPlayBackPanel faceVideoPlayback = new VideoPlayBackPanel(shell,  0);

	private VideoPlayBackPanel otherVideoPlayback = new VideoPlayBackPanel(shell, 1);

	private VideoRecorder recorder;

	// 出事最小化到系统托盘的监听
	private Tray tray;

	public FunctionMainWindow() {
		super();
	}

	public FunctionMainWindow(RecordConfig config) {
		this();
		this.config = config;
		recorder = new VideoRecorder(config);
		init();
	}

	@Override
	protected void beforeInit() {
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

		initOtherVideoPlayBackPanel();
		
		initScreenPanel();

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
		trayItem.setImage(ImageUtil.getInstance().getImage(
				Constants.Shell_Constant.TRAY_URL));
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
		TitlePanel titlePanel = new TitlePanel(this, shell, SWT.NONE, recorder);
		titlePanel.setLocation(Constants.TITLE_PANEL_Constant.LOCATION[0],
				Constants.TITLE_PANEL_Constant.LOCATION[1]);
		titlePanel.setSize(Constants.TITLE_PANEL_Constant.LOCATION[2],
				Constants.TITLE_PANEL_Constant.LOCATION[3]);

//		MoveableListener listener = new MoveableListener(titlePanel.getParent());
//		MyLabel title = new MyLabel(titlePanel, SWT.NONE,
//				Constants.TITLE_PANEL_Constant.TITLE_CONTENT,
//				Constants.TITLE_PANEL_Constant.TITLE_LOCATION);
//		title.setForeground(SWTResourceManager
//				.getColor(Constants.TITLE_PANEL_Constant.TITLE_COLOR));
//		title.addMouseListener(listener);
//		title.addMouseMoveListener(listener);
		titlePanel.setHeadVideoPlayBackPanel(faceVideoPlayback);
		titlePanel.setNoteVideoPlayBackPanel(otherVideoPlayback);
	}

	// 初始化脸部摄像头
	private void initVideoPlayBackPanel() {
		faceVideoPlayback.setBounds(Constants.Face_Constant.LOCATION_X,
				Constants.Face_Constant.LOCATION_Y,
				Constants.Face_Constant.WIDTH, Constants.Face_Constant.HEIGHT);
		Dimension head = config.getHead();
		if(null != head){
			faceVideoPlayback.setSize(head);
		}
		faceVideoPlayback.start();
	}

	// 初始化屏幕监控
	private void initScreenPanel() {
		ScreenPlayBackPanel screenPlayBackPanel = new ScreenPlayBackPanel(shell, SWT.NO_BACKGROUND);
		screenPlayBackPanel.setFaceVideoPlayBackPanel(faceVideoPlayback);
		screenPlayBackPanel.setOtherVideoPlayBackPanel(otherVideoPlayback);

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
		Dimension note = config.getNote();
		if(null != note){
			otherVideoPlayback.setSize(note);
		}
		otherVideoPlayback.start();
	}

	// 初始化教师信息
	private void initInfoLaber() {
		InfoLaber info = new InfoLaber(shell, SWT.INHERIT_DEFAULT);

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

		Label icon = new Label(info, SWT.NONE);
		icon.setBounds(Constants.INFO_LABEL_Constant.ICON_LOCATION[0],
				Constants.INFO_LABEL_Constant.ICON_LOCATION[1],
				Constants.INFO_LABEL_Constant.ICON_LOCATION[2],
				Constants.INFO_LABEL_Constant.ICON_LOCATION[3]);
		icon.setImage(ImageUtil.getInstance().getImage(
				info.getDisplay(),
				info.getClass().getResourceAsStream(
						Constants.INFO_LABEL_Constant.ICON_URL)));
		
		InfoBean ib = config.getIb();
		new MyLabel(info, SWT.NONE, ib.getSchool(),
				Constants.INFO_LABEL_Constant.SCHOOL).setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		new MyLabel(info, SWT.NONE, ib.getName(),
				Constants.INFO_LABEL_Constant.NAME).setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		new MyLabel(info, SWT.NONE, ib.getType(),
				Constants.INFO_LABEL_Constant.TYPE).setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		new MyLabel(info, SWT.NONE, "教龄： " + ib.getTrainAge() + "年",
				Constants.INFO_LABEL_Constant.AGE).setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

	}

	// 初始化监听切换摄像头
	private void initChangeCam() {
//		display.addFilter(SWT.KeyDown, new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				if ((e.stateMask == SWT.CTRL) && (e.keyCode == 'a')) {
//					recorder.changeSource();
//				}
//			}
//		});
		
		//注册三个切换快捷键
		JIntellitype.getInstance().registerHotKey(112, JIntellitype.MOD_CONTROL, 112);
		JIntellitype.getInstance().registerHotKey(113, JIntellitype.MOD_CONTROL, 113);
		JIntellitype.getInstance().registerHotKey(114, JIntellitype.MOD_CONTROL, 114);
		//添加对应时间
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			@Override
			public void onHotKey(int arg0) {
				switch (arg0) {
				//录制脸部摄像头
				case 112:
					config.setChangeFace(true);
					break;
				//录制笔记摄像头
				case 113:
					config.setChangeNote(true);
					break;
				//录制桌面
				case 114:
					config.setChangeScreen(true);
					break;
				default:
					break;
				}
			}
		});
	}

	// 初始化底部工具条
	private void initToolPanel() {
		ToolPanel toolPanel = new ToolPanel(this, shell, SWT.NONE, recorder);
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

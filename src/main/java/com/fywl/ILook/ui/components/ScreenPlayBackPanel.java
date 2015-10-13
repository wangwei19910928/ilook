package com.fywl.ILook.ui.components;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.utils.ImageUtil;

public class ScreenPlayBackPanel extends Canvas {

//	private boolean capting = false;

	private BufferedImage screen = ImageUtil.getInstance().screenCapture();
	
	private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	
	private Rectangle rectangle = new Rectangle(Constants.SCREEN_Constant.LOCATION_X, Constants.SCREEN_Constant.LOCATION_Y, Constants.SCREEN_Constant.WIDTH, Constants.SCREEN_Constant.HEIGHT);

	private ImageRecorder screenRecorder = null;

	private VideoPlayBackPanel faceVideoPlayBackPanel;

	private VideoPlayBackPanel otherVideoPlayBackPanel;

	public ScreenPlayBackPanel(Composite parent, int style) {
		super(parent, style);
		init();
	}

	public void addImageRecorder(ImageRecorder screenRecorder) {
		this.screenRecorder = screenRecorder;
	}

	private void init() {
		setSize(240, 180);
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				System.out.println(1111111);
				if (screen != null) {
					ImageData data = ImageUtil.getInstance().convertToSWT(
							screen);
					if (data != null) {
						Image im = new Image(getDisplay(), data);
						e.gc.drawImage(im, 0, 0, screen.getWidth(),
								screen.getHeight(), 0, 0, getSize().x,
								getSize().y);
						im.dispose();
					}
				}
			}
		});
	}

	public void start() {
//		capting = true;
		final RecordConfig config = RecordConfig.get();
		getDisplay().timerExec(5, new Runnable() {
			public void run() {

				BufferedImage fullScreen = ImageUtil.getInstance()
						.getAppFullScreen();
				if (fullScreen != null) {
					screen = fullScreen;
				} else {
					screen = ImageUtil.getInstance().screenCapture();
				}

				if (screen != null) {
					redraw();
					if (screenRecorder != null) {
						screenRecorder.recordScreen(screen);
					}
				}

				/**
				 * 切换画面
				 */
				checkChange(config);

				getDisplay().timerExec(5, this);
			}
		});
		setVisible(true);
	}

	public void stop() {
//		capting = false;
		setVisible(false);
	}

	public void setFaceVideoPlayBackPanel(
			VideoPlayBackPanel faceVideoPlayBackPanel) {
		this.faceVideoPlayBackPanel = faceVideoPlayBackPanel;
	}

	public void setOtherVideoPlayBackPanel(
			VideoPlayBackPanel otherVideoPlayBackPanel) {
		this.otherVideoPlayBackPanel = otherVideoPlayBackPanel;
	}

	/**
	 * 检查切换画面 display.asyncExec 和 display.syncExec均不起效果，暂时先放这里解决切换画面
	 * 
	 * @param config
	 */
	private void checkChange(RecordConfig config) {
//		if (config.isSingleRecording()) {
			// 录制脸部摄像头
			if (config.isChangeFace()) {
				faceVideoPlayBackPanel.setVisible(true);
				// 控制录制画面 true代表正在录制的画面
				if (config.isNoteRecording()|| config.isScreenRecording()) {
					//谁在录制中跟谁换，如果自己在录制中则不换
					if(config.isScreenRecording()){
						Rectangle r = getBounds();
						setBounds(faceVideoPlayBackPanel.getBounds());
						faceVideoPlayBackPanel.setBounds(r);
						
						config.setScreenRecording(false);
					}else if(config.isNoteRecording()){
						//还原shell 和 笔记摄像头
						getShell().setSize(Constants.Shell_Constant.WIDTH, Constants.Shell_Constant.HEIGHT);
						otherVideoPlayBackPanel.setBounds(rectangle);
						
						
						Rectangle r = otherVideoPlayBackPanel.getBounds();
						otherVideoPlayBackPanel.setBounds(faceVideoPlayBackPanel.getBounds());
						faceVideoPlayBackPanel.setBounds(r);
						
						config.setNoteRecording(false);
					}
					config.setFaceRecording(true);
					config.setChangeFlag(true);
				}
				config.setChangeFace(false);
			}

			// 录制笔记摄像头
			if (config.isChangeNote()) {
				// 控制录制画面 true代表正在录制的画面
				if (config.isFaceRecording() || config.isScreenRecording()) {
					//谁在录制中跟谁换，如果自己在录制中则不换
					if(config.isScreenRecording()){
						Rectangle r = getBounds();
						setBounds(otherVideoPlayBackPanel.getBounds());
						otherVideoPlayBackPanel.setBounds(r);
						
						config.setScreenRecording(false);
					}else if(config.isFaceRecording()){
						Rectangle r = otherVideoPlayBackPanel.getBounds();
						otherVideoPlayBackPanel.setBounds(faceVideoPlayBackPanel.getBounds());
						faceVideoPlayBackPanel.setBounds(r);
						
						config.setFaceRecording(false);
					}
					config.setNoteRecording(true);
					config.setChangeFlag(true);
					
					//放大笔记摄像头
					getShell().setBounds(0, 0, size.width, size.height);
					otherVideoPlayBackPanel.setBounds(0, 0, size.width, size.height);
					faceVideoPlayBackPanel.setVisible(false);
				}
				config.setChangeNote(false);
			}

			// 录制屏幕
			if (config.isChangeScreen()) {
				faceVideoPlayBackPanel.setVisible(true);
				// 控制录制画面 true代表正在录制的画面
				if (config.isNoteRecording() || config.isFaceRecording()) {
					//谁在录制中跟谁换，如果自己在录制中则不换
					if(config.isNoteRecording()){
						//还原shell 和 笔记摄像头
						getShell().setSize(Constants.Shell_Constant.WIDTH, Constants.Shell_Constant.HEIGHT);
						otherVideoPlayBackPanel.setBounds(rectangle);
						
						Rectangle r = getBounds();
						setBounds(otherVideoPlayBackPanel.getBounds());
						otherVideoPlayBackPanel.setBounds(r);
						
						config.setNoteRecording(false);
					}else if(config.isFaceRecording()){
						Rectangle r = getBounds();
						setBounds(faceVideoPlayBackPanel.getBounds());
						faceVideoPlayBackPanel.setBounds(r);
						
						config.setFaceRecording(false);
					}
					config.setScreenRecording(true);
					config.setChangeFlag(true);
				}
				config.setChangeScreen(false);
			}
			
			
			//摄像头变小
			if(config.isChangeNoteBigToSmall()){
				//还原shell 和 笔记摄像头
				getShell().setSize(Constants.Shell_Constant.WIDTH, Constants.Shell_Constant.HEIGHT);
				otherVideoPlayBackPanel.setBounds(rectangle);
				faceVideoPlayBackPanel.setVisible(true);
				
				config.setChangeNoteBigToSmall(false);
			}
//		}
	}
}

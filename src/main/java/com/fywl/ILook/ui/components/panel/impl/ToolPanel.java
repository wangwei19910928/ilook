package com.fywl.ILook.ui.components.panel.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.Panel;
import com.fywl.ILook.utils.ImageUtil;

public class ToolPanel extends Panel {
	private ProgressBar progressBar;
	
	private boolean pauseFlag = false;

	public ToolPanel(Closer closer, Composite parent, int style,
			VideoRecorder recorder) {
		super(closer, parent, style, recorder);
	}

	@Override
	protected void init() {
		final Label beginBtn = new Label(this, SWT.NONE);
		beginBtn.setBounds(
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[0],
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[1],
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[2],
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[3]);
		final Image beginImage = ImageUtil.getInstance().getImage(this.getDisplay(), this.getClass().getResourceAsStream(Constants.TOOL_PANEL_Constant.CAMERA_URL));
		final Image pauseImage = ImageUtil.getInstance().getImage(this.getDisplay(), this.getClass().getResourceAsStream(Constants.TOOL_PANEL_Constant.PAUSE_URL));
		final Image workingImage = ImageUtil.getInstance().getImage(this.getDisplay(), this.getClass().getResourceAsStream(Constants.TOOL_PANEL_Constant.WORKING_URL));
		beginBtn.setImage(beginImage);
		beginBtn.setToolTipText("开始录制");
		beginBtn.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (recording) {
					recorder.pause();
					pauseFlag = !pauseFlag;
					if(pauseFlag){
						beginBtn.setImage(pauseImage);
						beginBtn.setToolTipText("继续录制");
					}else{
						beginBtn.setImage(workingImage);
						beginBtn.setToolTipText("暂停录制");
					}
				} else {
					recorder.start();
					beginBtn.setImage(workingImage);
					beginBtn.setToolTipText("暂停录制");
					recording = !recording;
				}
			}
		});

		Label sBtn = new Label(this, SWT.NONE);
		Image stopImage = ImageUtil.getInstance().getImage(this.getDisplay(), this.getClass().getResourceAsStream(Constants.TOOL_PANEL_Constant.STOP_URL));
		sBtn.setImage(stopImage);
		sBtn.setToolTipText("完成录制");
		sBtn.setBounds(
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[0],
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[1],
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[2],
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[3]);
		sBtn.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				recording = false;
				initBegin(beginBtn,beginImage);
				recorder.finish();
			}
		});
		
		
		Label gBtn = new Label(this, SWT.NONE);
		Image giveupImage = ImageUtil.getInstance().getImage(this.getDisplay(), this.getClass().getResourceAsStream(Constants.TOOL_PANEL_Constant.GIVEUP_URL));
		gBtn.setImage(giveupImage);
		gBtn.setToolTipText("放弃录制");
		gBtn.setBounds(
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[0],
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[1],
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[2],
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[3]);
		gBtn.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				initBegin(beginBtn, beginImage);
				recording = false;
				recorder.finish();
			}
		});

		// progressBar = new ProgressBar(this, SWT.SMOOTH);
		// progressBar.setMinimum(0); // 最小值
		// progressBar.setMaximum(200);// 最大值
		// progressBar.setLocation(270, 0);
		// progressBar.setSize(100, 30);
	}

	// // 创建进度条
	// private ProgressBar createProgressBar() {
	// ProgressBar progressBar = new ProgressBar(this, SWT.SMOOTH);
	// progressBar.setMinimum(0); // 最小值
	// progressBar.setMaximum(200);// 最大值
	// progressBar.setLocation(270, 0);
	// progressBar.setSize(100, 30);
	// return progressBar;
	// }

	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	
	private void initBegin(Label ib,Image image){
		ib.setImage(image);
		ib.setToolTipText("开始录制");
		pauseFlag = false;
	}
}

package com.fywl.ILook.ui.components.panel.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.MessagePanel;
import com.fywl.ILook.ui.components.MyLabel;
import com.fywl.ILook.ui.components.UploadPanel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.Panel;
import com.fywl.ILook.utils.ImageUtil;

public class ToolPanel extends Panel {

	private boolean pauseFlag = false;

	public ToolPanel(Closer closer, Composite parent, int style,
			VideoRecorder recorder) {
		super(closer, parent, style, recorder);
	}

	@Override
	protected void init() {
		//显示录制时间的五个label
		Label ylzLabel = new Label(this, SWT.NONE);
		ylzLabel.setBounds(384, 5, 40, 30);
		ylzLabel.setText("已录制:");
		Label minuteLabel = new Label(this, SWT.NONE);
		minuteLabel.setBounds(424, 5, 30, 30);
		minuteLabel.setAlignment(SWT.RIGHT);
		Label minutesLabel = new Label(this, SWT.NONE);
		minutesLabel.setBounds(454, 5, 11, 30);
		minutesLabel.setText("分");
		Label secondLabel = new Label(this, SWT.NONE);
		secondLabel.setBounds(465, 5, 15, 30);
		secondLabel.setAlignment(SWT.RIGHT);
		Label secondsLabel = new Label(this, SWT.NONE);
		secondsLabel.setBounds(480, 5, 20, 30);
		secondsLabel.setText("秒");
		
		//开始按钮
		final Label beginBtn = new Label(this, SWT.NONE);
		beginBtn.setBounds(Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[0],
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[1],
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[2],
				Constants.TOOL_PANEL_Constant.CAMERA_LOCATION[3]);
		final Image beginImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.TOOL_PANEL_Constant.CAMERA_URL));
		final Image pauseImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.TOOL_PANEL_Constant.PAUSE_URL));
		final Image workingImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.TOOL_PANEL_Constant.WORKING_URL));
		beginBtn.setImage(beginImage);
		beginBtn.setToolTipText("开始录制");
		beginBtn.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (recording) {
					recorder.pause();
					pauseFlag = !pauseFlag;
					if (pauseFlag) {
						beginBtn.setImage(pauseImage);
						beginBtn.setToolTipText("继续录制");
					} else {
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
		
		//结束按钮
		Label sBtn = new Label(this, SWT.NONE);
		Image stopImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.TOOL_PANEL_Constant.STOP_URL));
		sBtn.setImage(stopImage);
		sBtn.setToolTipText("完成录制");
		sBtn.setBounds(Constants.TOOL_PANEL_Constant.STOP_LOCATION[0],
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[1],
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[2],
				Constants.TOOL_PANEL_Constant.STOP_LOCATION[3]);
		sBtn.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println(222);
				recording = false;
				if (recorder.getTime() > 0) {
					final MessagePanel mp = new MessagePanel(parent.getShell(),
							SWT.SYSTEM_MODAL);
					mp.setBounds(100, 100, 350, 290);
					int[] location = { 0, 0, 0, 0 };
					Label label = new MyLabel(mp, SWT.NONE, "  上 传  ", location);
					label.addListener(SWT.MouseUp, new Listener() {
						@Override
						public void handleEvent(Event event) {
							mp.dispose();
							UploadPanel up = new UploadPanel(parent.getShell(),
									SWT.SYSTEM_MODAL, recorder.getVideoName());
							up.moveAbove(null);
						}
					});
					mp.setLabel(label);
					int time = recorder.getTime() / 10;
					mp.init("/images/save.png", "微课已成功保存", "总时长" + time / 3600
							+ ":" + time / 60 + ":" + time % 60,
							"点击上传添加相应信息即可上传");
					mp.moveAbove(null);
				}
				initBegin(beginBtn, beginImage);
				recorder.finish();
			}
		});
		
		//放弃按钮
		Label gBtn = new Label(this, SWT.NONE);
		Image giveupImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.TOOL_PANEL_Constant.GIVEUP_URL));
		gBtn.setImage(giveupImage);
		gBtn.setToolTipText("放弃录制");
		gBtn.setBounds(Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[0],
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[1],
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[2],
				Constants.TOOL_PANEL_Constant.GIVEUP_LOCATION[3]);
		gBtn.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (recorder.getTime() > 0) {

					initBegin(beginBtn, beginImage);
					recording = false;
					recorder.finish();
					final MessagePanel mp = new MessagePanel(parent.getShell(),
							SWT.SYSTEM_MODAL);
					mp.setBounds(100, 100, 350, 290);
					int[] location = { 0, 0, 0, 0 };
					Label label = new MyLabel(mp, SWT.NONE, "  重 录  ", location);
					label.addListener(SWT.MouseUp, new Listener() {
						@Override
						public void handleEvent(Event event) {
							Event e = new Event();
							e.widget = beginBtn;
							// 主动触发button点击事件
							beginBtn.notifyListeners(SWT.MouseUp, e);
							mp.dispose();
						}
					});
					mp.setLabel(label);
					int time = recorder.getTime() / 10;
					mp.init("/images/save.png", "执行此操作将放弃当前微课！", "总时长" + time
							/ 3600 + ":" + time / 60 + ":" + time % 60,
							"您可以选择重录或者回到主页面");
					mp.moveAbove(null);
				}
			}
		});

		// 刷新录制时间
		refresh(minuteLabel, secondLabel);
	}

	private void initBegin(Label ib, Image image) {
		ib.setImage(image);
		ib.setToolTipText("开始录制");
		pauseFlag = false;
	}

	private void refresh(final Label minuteLabel, final Label secondLabel) {
		getDisplay().timerExec(800, new Runnable() {
			public void run() {
				int count = recorder.getTime() / 10;
				minuteLabel.setText(count / 60+ "");
				secondLabel.setText(count % 60 + "");
				getDisplay().timerExec(800, this);
			}
		});
	}
}

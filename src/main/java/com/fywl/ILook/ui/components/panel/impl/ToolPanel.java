package com.fywl.ILook.ui.components.panel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.bean.RecordConfig;
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
		final File file = new File("user.properties");
		final Properties properties = new Properties();
		
		// 显示录制时间的五个label
		Label ylzLabel = new Label(this, SWT.NONE);
		ylzLabel.setBounds(384, 5, 40, 30);
		ylzLabel.setText("已录制:");
		Label minuteLabel = new Label(this, SWT.NONE);
		minuteLabel.setBounds(424, 5, 30, 30);
		minuteLabel.setAlignment(SWT.RIGHT);
		minuteLabel.setText("0");
		Label minutesLabel = new Label(this, SWT.NONE);
		minutesLabel.setBounds(454, 5, 11, 30);
		minutesLabel.setText("分");
		Label secondLabel = new Label(this, SWT.NONE);
		secondLabel.setBounds(465, 5, 15, 30);
		secondLabel.setAlignment(SWT.RIGHT);
		secondLabel.setText("0");
		Label secondsLabel = new Label(this, SWT.NONE);
		secondsLabel.setBounds(480, 5, 20, 30);
		secondsLabel.setText("秒");

		// 开始按钮
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
					
					//修改最佳录制分辨率
//					GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment(); 
//					GraphicsDevice device=environment.getDefaultScreenDevice(); 
//					DisplayMode displayMode=new DisplayMode(1024,768,16,75); 
//					device.setDisplayMode(displayMode);
				}
			}
		});

		// 结束按钮
		final Label sBtn = new Label(this, SWT.NONE);
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
				recording = false;
				initBegin(beginBtn, beginImage);
				recorder.finish();
				if (recorder.getTime() > 0) {
					// 遮罩层
					final Composite zz = new Composite(beginBtn.getShell(),
							SWT.NO_BACKGROUND);
					zz.setBounds(0, 30, 500, 410);
					zz.moveAbove(null);
					final MessagePanel mp = new MessagePanel(zz, SWT.NONE);
					mp.moveAbove(null);
					mp.setBounds(100, 70, 350, 290);
					//右侧的上传按钮
					int[] location = { 0, 0, 0, 0 };
					Label label = new MyLabel(mp, SWT.NONE, "", location);
					Image rImage = ImageUtil.getInstance().getImage(
							sBtn.getDisplay(),
							this.getClass().getResourceAsStream(
									"/images/upload_upload.png"));
					label.setImage(rImage);
					label.addListener(SWT.MouseUp, new Listener() {
						@Override
						public void handleEvent(Event event) {
							UploadPanel up = new UploadPanel(parent.getShell(),
									SWT.SYSTEM_MODAL, recorder.getVideoName());
							up.moveAbove(null);
							zz.dispose();
						}
					});
					InfoBean ib = RecordConfig.get().getIb();
					if(ib.isUploadFlag()){
						mp.setRightLabel(label);
					}
					//左侧的打开文件夹按钮
					Label llabel = new MyLabel(mp, SWT.NONE, "", location);
					Image lImage = ImageUtil.getInstance().getImage(
							sBtn.getDisplay(),
							this.getClass().getResourceAsStream(
									"/images/upload_open.png"));
					llabel.setImage(lImage);
					llabel.addListener(SWT.MouseUp, new Listener() {
						@Override
						public void handleEvent(Event event) {
							try {
								FileInputStream fis;
								if (!file.exists()) {
									file.createNewFile();
								}
								fis = new FileInputStream(file);
								properties.load(fis);
								fis.close();
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							String str = Constants.SETUP_Constant.videoPath;
							String videoPath = properties.getProperty("videoPath");
							if(null != videoPath && !videoPath.equals("")){
								str = videoPath;
							}
							try {
								Runtime.getRuntime().exec("explorer " + str);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					mp.setLeftLabel(llabel);
					int time = recorder.getTime() / 10;
					mp.init("/images/saveflag.png", "微课已成功保存", "总时长" + time / 3600
							+ ":" + (time / 60)%60+ ":" + time % 60,
							" ");
				}
			}
		});

		// 放弃按钮
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
					// 遮罩层
					final Composite zz = new Composite(beginBtn.getParent()
							.getShell(), SWT.NO_BACKGROUND);
					zz.setBounds(0, 0, 500, 440);
					zz.moveAbove(null);
					final MessagePanel mp = new MessagePanel(zz, SWT.NONE);
					mp.setBounds(100, 100, 350, 290);
					int[] location = { 0, 0, 0, 0 };
					Label label = new MyLabel(mp, SWT.NONE, "  重 录  ", location);
					Image cImage = ImageUtil.getInstance().getImage(
							sBtn.getDisplay(),
							this.getClass().getResourceAsStream(
									"/images/retake.png"));
					label.setImage(cImage);
					label.addListener(SWT.MouseUp, new Listener() {
						@Override
						public void handleEvent(Event event) {
							Event e = new Event();
							e.widget = beginBtn;
							// 主动触发button点击事件
							beginBtn.notifyListeners(SWT.MouseUp, e);
							zz.dispose();
						}
					});
					mp.setRightLabel(label);
					//左侧的返回主界面按钮
					Label llabel = new MyLabel(mp, SWT.NONE, "", location);
					Image lImage = ImageUtil.getInstance().getImage(
							sBtn.getDisplay(),
							this.getClass().getResourceAsStream(
									"/images/returnmain.png"));
					llabel.setImage(lImage);
					llabel.addListener(SWT.MouseUp, new Listener() {
						@Override
						public void handleEvent(Event event) {
							zz.dispose();
						}
					});
					mp.setLeftLabel(llabel);
					int time = recorder.getTime() / 10;
					mp.init("/images/warning.png", "执行此操作将放弃当前微课！", "总时长" + time
							/ 3600 + ":" + time / 60 + ":" + time % 60,
							"您可以选择重录或者回到主页面");
					mp.moveAbove(zz);
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
		if(null != getDisplay()){
			getDisplay().timerExec(500, new Runnable() {
				public void run() {
					int count = recorder.getTime() / 10;
					minuteLabel.setText(count / 60 + "");
					secondLabel.setText(count % 60 + "");
					getDisplay().timerExec(500, this);
				}
			});
		}
	}
}

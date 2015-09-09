package com.fywl.ILook.ui.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.FormFieldKeyValuePair;
import com.fywl.ILook.bean.UploadFileItem;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.utils.HttpRequest;

public class UploadPanel extends Composite implements Closer {
	private String videoName;

	public UploadPanel(Composite parent, int style, String videoName) {
		super(parent, style);

		this.videoName = videoName;
		init();
	}

	@Override
	public void shutDown() {
		this.dispose();
	}

	private void init() {
		this.setBounds(0, 0, Constants.Shell_Constant.WIDTH,
				Constants.Shell_Constant.HEIGHT);

		// 顶部标题
		Composite title = new Composite(this, SWT.NONE);
		title.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		title.setBounds(0, 0, Constants.Shell_Constant.WIDTH, 30);
		// 标题文字
		int location[] = { 0, 5, 80, 30 };
		new MyLabel(title, SWT.NONE, "上传微课", location);
		// 关闭按钮
		final ImageButton closeBtn = new ImageButton(title,
				Constants.TITLE_PANEL_Constant.CLOSE_URL, "关闭");
		closeBtn.getButton().setBounds(470, 0, 30, 30);
		closeBtn.getButton().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				closeBtn.parent.getParent().dispose();
			}
		});

		// 标题
		int locationBT[] = { 20, 60, 40, 20 };
		new MyLabel(this, SWT.NONE, "标题", locationBT);
		final Text textBT = new Text(this, SWT.BORDER | SWT.SINGLE);
		textBT.setBounds(100, 55, 300, 30);

		// 标签
		int locationBQ[] = { 20, 100, 40, 20 };
		new MyLabel(this, SWT.NONE, "标签", locationBQ);
		final Text textBQ = new Text(this, SWT.BORDER | SWT.SINGLE);
		textBQ.setBounds(100, 95, 300, 30);

		// 科目
		int locationKM[] = { 20, 140, 40, 20 };
		new MyLabel(this, SWT.NONE, "科目", locationKM);
		final Combo combo = new Combo(this, SWT.READ_ONLY);
		combo.setBounds(100, 140, 100, 30);
		combo.setItems(Constants.UPLOAD_Constant.TYPE);
		for (int i = 0; i < Constants.UPLOAD_Constant.TYPE.length; i++) {
			combo.setData(Constants.UPLOAD_Constant.TYPE[i],
					Constants.UPLOAD_Constant.KEY[i]);
		}

		// 时间
		int locationSJ[] = { 20, 220, 40, 20 };
		new MyLabel(this, SWT.NONE, "时间", locationSJ);
		final CDateTime dateTime = new CDateTime(this, CDT.DATE_SHORT
				| CDT.CLOCK_24_HOUR | CDT.DROP_DOWN);
		dateTime.setPattern("yyyy-MM-dd HH:mm:ss");
		dateTime.setSelection(new Date(System.currentTimeMillis()));
		// DateTime calendar = new DateTime(this, SWT.CALENDAR | SWT.TIME);
		dateTime.setBounds(100, 220, 130, 200);

		final CDateTime dateTime1 = new CDateTime(this, CDT.DATE_SHORT
				| CDT.CLOCK_24_HOUR | CDT.DROP_DOWN);
		dateTime1.setPattern("yyyy-MM-dd HH:mm:ss");
		dateTime1.setSelection(new Date(System.currentTimeMillis()));
		// DateTime calendar = new DateTime(this, SWT.CALENDAR | SWT.TIME);
		dateTime1.setBounds(260, 220, 130, 200);

		// 备注
		int locationBZ[] = { 20, 260, 40, 20 };
		new MyLabel(this, SWT.NONE, "备注", locationBZ);
		final Text textBZ = new Text(this, SWT.BORDER | SWT.MULTI);
		textBZ.setBounds(100, 260, 300, 100);
		textBZ.moveAbove(null);

		// 是否自动删除
		int locationAD[] = { 20, 180, 100, 20 };
		new MyLabel(this, SWT.NONE, "是否自动删除", locationAD);
		Group group = new Group(this, SWT.NO_BACKGROUND);
		group.setLayout(new FillLayout(SWT.NO_BACKGROUND));
		group.setBounds(0, 0, 500, 500);
		// 在当前分组中创建单选钮1
		final Button radio1 = new Button(group, SWT.RADIO);
		// 为单选钮1添加说明文字
		radio1.setText("是");
		radio1.setBounds(180, 180, 30, 20);
		// 在当前分组中创建单选钮2
		final Button radio2 = new Button(group, SWT.RADIO);
		// 为单选钮2添加说明文字
		radio2.setText("否");
		radio2.setBounds(240, 180, 30, 20);
		radio2.setSelection(true);

		// 确定按钮
		final Label confirmLabel = new MyLabel(this, SWT.NONE, "  确 定   ",
				location);
		confirmLabel.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		confirmLabel.setBounds(250, 400, 80, 20);
		confirmLabel.moveAbove(null);
		confirmLabel.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String bt = textBT.getText();
				String bq = textBQ.getText();
				String km = combo.getText();
				Object a = combo.getData(km);
				String beginTime = dateTime.getText();
				String endTime = dateTime1.getText();
				String bz = textBZ.getText();

				ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();
				ffkvp.add(new FormFieldKeyValuePair("title", bt));
				ffkvp.add(new FormFieldKeyValuePair("tag", bq));
				ffkvp.add(new FormFieldKeyValuePair("key", a.toString()));
				ffkvp.add(new FormFieldKeyValuePair("type", km));
				ffkvp.add(new FormFieldKeyValuePair("flag", radio1
						.getSelection() ? true + "" : false + ""));
				ffkvp.add(new FormFieldKeyValuePair("beginTime", beginTime));
				ffkvp.add(new FormFieldKeyValuePair("endTime", endTime));
				ffkvp.add(new FormFieldKeyValuePair("info", bz));

				ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();
				videoName = "E://ssdgdfg"+File.separator+"1441772929866test.mp4";
				ufi.add(new UploadFileItem("upload1", videoName));
				String response;
				try {
					// 上传状态 遮罩层
					final Composite mp = new Composite(confirmLabel.getParent()
							.getShell(), SWT.NO_BACKGROUND);
					mp.setBounds(0, 0, 500, 440);
					mp.moveAbove(null);
					// 展现层
					Composite mp1 = new EmptyPanel(mp, SWT.NONE, "正在上传");
					mp1.setBounds(100, 100, 300, 240);
					mp1.setBackground(SWTResourceManager
							.getColor(SWT.COLOR_GRAY));
					mp1.moveAbove(null);

					// 进度条
					ProgressBar pb = new ProgressBar(mp1, SWT.INDETERMINATE);
					pb.setBounds(100, 110, 100, 20);

					// 请求服务器
					response = HttpRequest.uploadFile(
							Constants.UPLOAD_Constant.HTTP_UPLOAD_URL, videoName);
//					response = HttpRequest.sendHttpPostRequest(
//							Constants.UPLOAD_Constant.HTTP_UPLOAD_URL, ffkvp,
//							ufi);
					System.out.println("Responsefrom server is: " + response);
					// 销毁展现层
					mp1.dispose();
					if (response.contains("O")) {
						// 上传成功提示框
						final Composite successBox = new EmptyPanel(mp,
								SWT.NONE, "上传成功");
						successBox.setBounds(100, 100, 300, 120);
						successBox.setBackground(SWTResourceManager
								.getColor(SWT.COLOR_GRAY));
						successBox.moveAbove(null);

						//
						Label successLaber = new Label(successBox, SWT.NONE);
						successLaber.setBounds(120, 50, 60, 20);
						successLaber.setText("   关  闭");
						successLaber.setBackground(SWTResourceManager.getColor(
								Constants.Shell_Constant.BACKGROUND[0],
								Constants.Shell_Constant.BACKGROUND[1],
								Constants.Shell_Constant.BACKGROUND[2]));
						successLaber.addListener(SWT.MouseUp, new Listener() {
							@Override
							public void handleEvent(Event event) {
								successBox.dispose();
								mp.dispose();
								confirmLabel.getParent().dispose();
							}
						});
					} else {
						// 上传成功提示框
						final Composite failureBox = new EmptyPanel(mp,
								SWT.NONE, "上传失败");
						failureBox.setBounds(100, 100, 300, 120);
						failureBox.setBackground(SWTResourceManager
								.getColor(SWT.COLOR_GRAY));
						failureBox.moveAbove(null);

						//
						Label failureLaber = new Label(failureBox, SWT.NONE);
						failureLaber.setBounds(120, 50, 60, 20);
						failureLaber.setText("   关  闭");
						failureLaber.setBackground(SWTResourceManager.getColor(
								Constants.Shell_Constant.BACKGROUND[0],
								Constants.Shell_Constant.BACKGROUND[1],
								Constants.Shell_Constant.BACKGROUND[2]));
						failureLaber.addListener(SWT.MouseUp, new Listener() {
							@Override
							public void handleEvent(Event event) {
								failureBox.dispose();
								mp.dispose();
							}
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// 取消按钮
		final Label cancelLabel = new MyLabel(this, SWT.NONE, "  取 消    ",
				location);
		cancelLabel.setBounds(350, 400, 80, 20);
		cancelLabel.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		cancelLabel.moveAbove(null);
		cancelLabel.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				cancelLabel.getParent().dispose();
			}
		});

	}
}

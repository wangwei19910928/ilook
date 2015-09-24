package com.fywl.ILook.ui.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
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
import com.fywl.ILook.bean.UploadFileItem;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.utils.HttpRequest;
import com.fywl.ILook.utils.ImageUtil;

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

		// 顶部
		new TitleComposite(this, SWT.NONE, "上传微课");

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
		dateTime.setBounds(100, 220, 140, 200);

		final CDateTime dateTime1 = new CDateTime(this, CDT.DATE_SHORT
				| CDT.CLOCK_24_HOUR | CDT.DROP_DOWN);
		dateTime1.setPattern("yyyy-MM-dd HH:mm:ss");
		dateTime1.setSelection(new Date(System.currentTimeMillis()));
		// DateTime calendar = new DateTime(this, SWT.CALENDAR | SWT.TIME);
		dateTime1.setBounds(260, 220, 140, 200);

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
		int location[] = { 0, 5, 80, 30 };
		final Label confirmLabel = new MyLabel(this, SWT.NONE, "  确 定   ",
				location);
		Image confirmImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/upload_confirm.png"));
		confirmLabel.setImage(confirmImage);
		confirmLabel.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		confirmLabel.setBounds(250, 400, 80, 25);
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

				// ArrayList<FormFieldKeyValuePair> ffkvp = new
				// ArrayList<FormFieldKeyValuePair>();
				// ffkvp.add(new FormFieldKeyValuePair("title", bt));
				// ffkvp.add(new FormFieldKeyValuePair("tag", bq));
				// ffkvp.add(new FormFieldKeyValuePair("key", a.toString()));
				// ffkvp.add(new FormFieldKeyValuePair("type", km));
				// ffkvp.add(new FormFieldKeyValuePair("flag", radio1
				// .getSelection() ? true + "" : false + ""));
				// ffkvp.add(new FormFieldKeyValuePair("beginTime", beginTime));
				// ffkvp.add(new FormFieldKeyValuePair("endTime", endTime));
				// ffkvp.add(new FormFieldKeyValuePair("info", bz));

				ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();
				videoName = "E://ssdgdfg" + File.separator
						+ "test1-16gb.mp4";
				ufi.add(new UploadFileItem("upload1", videoName));
				final String[] response = {""};
				try {
					// 上传状态 遮罩层
					final Composite mp = new Composite(confirmLabel.getParent()
							.getShell(), SWT.NO_BACKGROUND);
					mp.setBounds(0, 0, 500, 440);
					mp.moveAbove(null);
					// 展现层
					final Composite mp1 = new EmptyPanel(mp, SWT.NONE, "正在上传");
					// // 进度条
					// ProgressBar pb = new ProgressBar(mp1, SWT.INDETERMINATE);
					// pb.setBounds(100, 110, 100, 20);

					// // 请求服务器
					new Thread(new Runnable() {
						@Override
						public void run() {
							getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									// 进度条
									ProgressBar pb = new ProgressBar(mp1, SWT.INDETERMINATE);
									pb.setBounds(100, 50, 100, 20);
									pb.moveAbove(null);
								}
							});
							response[0] = HttpRequest.uploadFile(
									Constants.UPLOAD_Constant.HTTP_UPLOAD_URL,
									videoName);
							getDisplay().asyncExec(new Runnable() {
								public void run() {
									// 销毁展现层
									mp1.dispose();
									if (response[0].contains("O")) {
										// 上传成功提示框
										new EmptyPanel(mp, SWT.NONE, "上传成功", "  关 闭 ",
												confirmLabel.getParent());
									} else {
										// 上传失败提示框
										new EmptyPanel(mp, SWT.NONE, "上传失败", "  关 闭 ", null);
									}
								}
							});
						}
							
					}).start();
					// response = HttpRequest.sendHttpPostRequest(
					// Constants.UPLOAD_Constant.HTTP_UPLOAD_URL, ffkvp,
					// ufi);
					System.out.println("Responsefrom server is: " + response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// 取消按钮
		final Label cancelLabel = new MyLabel(this, SWT.NONE, "  取 消    ",
				location);
		Image giveupImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/upload_giveup.png"));
		cancelLabel.setImage(giveupImage);
		cancelLabel.setBounds(350, 400, 80, 25);
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

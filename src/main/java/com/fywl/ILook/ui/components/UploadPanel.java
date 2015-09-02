package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;

public class UploadPanel extends Composite implements Closer {

	public UploadPanel(Composite parent, int style) {
		super(parent, style);

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
		Text textBT = new Text(this, SWT.BORDER | SWT.SINGLE);
		textBT.setBounds(100, 55, 300, 30);

		// 标签
		int locationBQ[] = { 20, 100, 40, 20 };
		new MyLabel(this, SWT.NONE, "标签", locationBQ);
		Text textBQ = new Text(this, SWT.BORDER | SWT.SINGLE);
		textBQ.setBounds(100, 95, 300, 30);

		// 科目
		int locationKM[] = { 20, 140, 40, 20 };
		new MyLabel(this, SWT.NONE, "科目", locationKM);
		Combo combo = new Combo(this, SWT.READ_ONLY);
		combo.setBounds(100, 140, 100, 30);
		combo.setItems(Constants.UPLOAD_Constant.TYPE);
		for (int i = 0; i < Constants.UPLOAD_Constant.TYPE.length; i++) {
			combo.setData(Constants.UPLOAD_Constant.TYPE[i], "" + i);
		}

		// 时间
		int locationSJ[] = { 20, 220, 40, 20 };
		new MyLabel(this, SWT.NONE, "时间", locationSJ);
		// DateTime calendar = new DateTime(this, SWT.CALENDAR | SWT.TIME);
		// calendar.setBounds(100, 220, 200, 200);

		// 备注
		int locationBZ[] = { 20, 260, 40, 20 };
		new MyLabel(this, SWT.NONE, "备注", locationBZ);
		Text textBZ = new Text(this, SWT.BORDER | SWT.MULTI);
		textBZ.setBounds(100, 260, 300, 100);

		// 确定按钮
		Label confirmLabel = new MyLabel(this, SWT.NONE, "  确 定   ", location);
		confirmLabel.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		confirmLabel.setBounds(250, 400, 80, 20);
		confirmLabel.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("11111111111");
			}
		});

		// 取消按钮
		Label cancelLabel = new MyLabel(this, SWT.NONE, "  取 消    ", location);
		cancelLabel.setBounds(350, 400, 80, 20);
		cancelLabel.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		cancelLabel.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("11111111111");
			}
		});

		// 是否自动删除
		int locationAD[] = { 20, 180, 100, 20 };
		new MyLabel(this, SWT.NONE, "是否自动删除", locationAD);
		Group group = new Group(this, SWT.NO_BACKGROUND);
		group.setLayout(new FillLayout(SWT.NO_BACKGROUND));
		group.setBounds(0, 0, 500, 500);
		// 在当前分组中创建单选钮1
		Button radio1 = new Button(group, SWT.RADIO);
		// 为单选钮1添加说明文字
		radio1.setText("是");
		radio1.setBounds(180, 180, 30, 20);
		// 在当前分组中创建单选钮2
		Button radio2 = new Button(group, SWT.RADIO);
		// 为单选钮2添加说明文字
		radio2.setText("否");
		radio2.setBounds(240, 180, 30, 20);

	}
}

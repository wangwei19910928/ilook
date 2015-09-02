package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.utils.ImageUtil;

public class MessagePanel extends Composite implements Closer {
	
	private Label label;
	public MessagePanel(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void shutDown() {
		this.dispose();
	}

	public void init(String iconUrl, String firstText, String secondText,
			String thirdText) {
		// 顶部标题
		Composite title = new Composite(this, SWT.NONE);
		title.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		title.setBounds(0, 0, 350, 30);
		// 标题文字
		int location[] = { 0, 5, 80, 30 };
		new MyLabel(title, SWT.NONE, "提示信息", location);
		// 关闭按钮
		final ImageButton closeBtn = new ImageButton(title,
				Constants.TITLE_PANEL_Constant.CLOSE_URL, "关闭");
		closeBtn.getButton().setBounds(320, 0, 30, 30);
		closeBtn.getButton().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				closeBtn.parent.getParent().dispose();
			}
		});

		// 中间左侧图标
		Label icon = new Label(this, SWT.NONE);
		Image giveupImage = ImageUtil.getInstance().getImage(this.getDisplay(),
				this.getClass().getResourceAsStream(iconUrl));
		icon.setImage(giveupImage);
		icon.setBounds(30, 90, 30, 60);

		// 第一句话
		int[] firstLocation = { 80, 80, 240, 20 };
		new MyLabel(this, SWT.NONE, firstText, firstLocation);
		// 第二句话
		int[] secondLocation = { 80, 110, 240, 20 };
		new MyLabel(this, SWT.NONE, secondText, secondLocation);
		// 第三句话
		int[] thirdLocation = { 80, 140, 240, 20 };
		new MyLabel(this, SWT.NONE, thirdText, thirdLocation);
		
		label.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		label.setBounds(240, 180, 80, 20);
	}


	public void setLabel(Label label) {
		this.label = label;
	}
}

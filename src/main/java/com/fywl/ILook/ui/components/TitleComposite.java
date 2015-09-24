package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.ui.listener.MoveableListener;

public class TitleComposite extends Composite{
	
	public TitleComposite(Composite parent,int style,String str){
		super(parent, style);
		
		init(str);
	}
	
	
	private void init(String str){
		MoveableListener listener = new MoveableListener(this.getShell());
		// 顶部标题
		Composite title = new Composite(this.getParent(), SWT.NONE);
		title.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		title.setBounds(0, 0, Constants.Shell_Constant.WIDTH, 30);
		title.addMouseListener(listener);
		title.addMouseMoveListener(listener);
		// 标题文字
		int location[] = { 0, 5, 80, 30 };
		MyLabel titleLabel =  new MyLabel(title, SWT.NONE, str, location);
		titleLabel.addMouseListener(listener);
		titleLabel.addMouseMoveListener(listener);
		
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
	}
}

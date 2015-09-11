package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;

/**
 * 提示框 父容器会被disposed
 * 
 * @author Administrator
 * 
 */
public class EmptyPanel extends Composite implements Closer {

	/**
	 * 返回一个只带标题的提示框
	 * 
	 * @param parent
	 * @param style
	 * @param titleText
	 */
	public EmptyPanel(Composite parent, int style, String titleText) {
		super(parent, style);

		init(titleText);
	}

	/**
	 * 返回一个代标题、带一个按钮的提示框
	 * 
	 * @param parent
	 * @param style
	 * @param titleText
	 * @param labelText
	 * @param disposeComposite
	 */
	public EmptyPanel(Composite parent, int style, String titleText,
			String labelText, Composite disposeComposite) {
		super(parent, style);

		init(titleText, labelText, disposeComposite);
	}

	@Override
	public void shutDown() {
		this.dispose();
		this.getParent().dispose();
	}

	private void init(String titleText, String labelText,
			final Composite disposeComposite) {
		setBounds(100, 100, 300, 120);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		moveAbove(null);
		// 顶部标题
		Composite title = new Composite(this, SWT.NONE);
		title.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		title.setBounds(0, 0, 300, 30);
		// 标题文字
		int location[] = { 0, 5, 80, 30 };
		new MyLabel(title, SWT.NONE, titleText, location);

		// 底部按钮
		final Label successLaber = new Label(this, SWT.NONE);
		successLaber.setBounds(120, 50, 60, 20);
		successLaber.setText(labelText);
		successLaber.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		successLaber.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// 销毁提示框
				// successLaber.getParent().dispose();
				// 销毁提示框后面的遮罩层
				successLaber.getParent().getParent().dispose();
				// 要销毁的对象
				if (null != disposeComposite) {
					disposeComposite.dispose();
				}
			}
		});
	}

	private void init(String titleText) {
		setBounds(100, 100, 300, 120);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		moveAbove(null);
		// 顶部标题
		Composite title = new Composite(this, SWT.NONE);
		title.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		title.setBounds(0, 0, 300, 30);
		// 标题文字
		int location[] = { 0, 5, 300, 30 };
		new MyLabel(title, SWT.NONE, titleText, location);

		// 顶部标题
		Composite panel = new Composite(this, SWT.NONE);
		panel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		panel.setBounds(0, 30, 300, 90);

		// 标题文字
		int location1[] = { 0, 0, 300, 90 };
		new MyLabel(panel, SWT.NONE, "请稍候···", location1);
		
		// 进度条
		ProgressBar pb = new ProgressBar(panel, SWT.INDETERMINATE);
		pb.setBounds(50, 40, 100, 20);
	}
}

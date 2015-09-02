package com.fywl.ILook.ui.components.panel.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.ImageButton;
import com.fywl.ILook.ui.components.SetupPanel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.Panel;
import com.fywl.ILook.ui.listener.MoveableListener;
import com.fywl.ILook.utils.ImageUtil;

public class TitlePanel extends Panel {

	public TitlePanel(Closer closer, Composite parent, int style,
			VideoRecorder recorder) {
		super(closer, parent, style, recorder);
	}

	@Override
	protected void init() {
		MoveableListener listener = new MoveableListener(parent);

		setBackground(SWTResourceManager.getColor(
				Constants.TITLE_PANEL_Constant.BACKGROUND[0],
				Constants.TITLE_PANEL_Constant.BACKGROUND[1],
				Constants.TITLE_PANEL_Constant.BACKGROUND[2]));
		addMouseListener(listener);
		addMouseMoveListener(listener);

		Label icon = new Label(this, SWT.NONE);
		icon.setBounds(Constants.TITLE_PANEL_Constant.LOGO_LOCATION[0],
				Constants.TITLE_PANEL_Constant.LOGO_LOCATION[1],
				Constants.TITLE_PANEL_Constant.LOGO_LOCATION[2],
				Constants.TITLE_PANEL_Constant.LOGO_LOCATION[3]);
		icon.setImage(ImageUtil.getInstance().getImage(this.getDisplay(),this.getClass().getResourceAsStream(
				Constants.TITLE_PANEL_Constant.LOGO_URL)));
		icon.addMouseListener(listener);
		icon.addMouseMoveListener(listener);

		ImageButton closeBtn = new ImageButton(this,
				Constants.TITLE_PANEL_Constant.CLOSE_URL, "关闭");
		closeBtn.getButton().setBounds(
				Constants.TITLE_PANEL_Constant.CLOSE_LOCATION[0],
				Constants.TITLE_PANEL_Constant.CLOSE_LOCATION[1],
				Constants.TITLE_PANEL_Constant.CLOSE_LOCATION[2],
				Constants.TITLE_PANEL_Constant.CLOSE_LOCATION[3]);
		closeBtn.getButton().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				parent.getDisplay().getSystemTray().getItem(0).setVisible(false);
				parent.setVisible(false);
				closer.shutDown();
				System.exit(0);
			}
		});

		ImageButton sBtn = new ImageButton(this,
				Constants.TITLE_PANEL_Constant.SMALL_URL, "最小化");
		sBtn.getButton().setBounds(
				Constants.TITLE_PANEL_Constant.SMALL_LOCATION[0],
				Constants.TITLE_PANEL_Constant.SMALL_LOCATION[1],
				Constants.TITLE_PANEL_Constant.SMALL_LOCATION[2],
				Constants.TITLE_PANEL_Constant.SMALL_LOCATION[3]);
		sBtn.getButton()
				.setBackground(SWTResourceManager.getColor(5, 109, 191));
		sBtn.getButton().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				parent.setVisible(false);
				parent.getDisplay().getSystemTray().getItem(0).setVisible(true);

			}
		});
		
		
		ImageButton setupBtn = new ImageButton(this,
				Constants.TITLE_PANEL_Constant.SETUP_URL, "设置");
		setupBtn.getButton().setBounds(
				Constants.TITLE_PANEL_Constant.SETUP_LOCATION[0],
				Constants.TITLE_PANEL_Constant.SETUP_LOCATION[1],
				Constants.TITLE_PANEL_Constant.SETUP_LOCATION[2],
				Constants.TITLE_PANEL_Constant.SETUP_LOCATION[3]);
		setupBtn.getButton().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				SetupPanel sp = new SetupPanel(parent.getShell(), SWT.NONE);
				sp.moveAbove(null);
			}
		});
	}

}

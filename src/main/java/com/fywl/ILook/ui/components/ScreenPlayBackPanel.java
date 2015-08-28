package com.fywl.ILook.ui.components;

import java.awt.image.BufferedImage;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.fywl.ILook.utils.ImageUtil;

public class ScreenPlayBackPanel extends Canvas {

	private boolean capting = false;

	private BufferedImage screen = ImageUtil.getInstance().screenCapture();

	private ImageRecorder screenRecorder = null;

	public ScreenPlayBackPanel(Composite parent, int style) {
		super(parent, style);
		init();
	}

	public void addImageRecorder(ImageRecorder screenRecorder) {
		this.screenRecorder = screenRecorder;
	}

	private void init() {
		setSize(240, 180);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (screen != null) {
					ImageData data = ImageUtil.getInstance().convertToSWT(screen);
					if (data != null) {
						Image im = new Image(getDisplay(), data);
						e.gc.drawImage(im, 0, 0, screen.getWidth(), screen.getHeight(), 0, 0, getSize().x, getSize().y);
						im.dispose();
					}
				}
			}
		});
	}

	public void start() {
		capting = true;
		getDisplay().timerExec(5, new Runnable() {
			public void run() {

				BufferedImage fullScreen = ImageUtil.getInstance().getAppFullScreen();
				if (fullScreen != null) {
					screen = fullScreen;
				} else {
					screen = ImageUtil.getInstance().screenCapture();
				}

				if (screen != null) {
					redraw();
					if (screenRecorder != null) {
						screenRecorder.recordScreen(screen);
					}
				}

				getDisplay().timerExec(5, this);
			}
		});
		setVisible(true);
	}

	public void stop() {
		capting = false;
		setVisible(false);
	}
}

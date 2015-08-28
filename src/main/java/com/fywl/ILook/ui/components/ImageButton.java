package com.fywl.ILook.ui.components;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ImageButton {

	protected Composite parent;
	protected Button button;
	protected Image image;

	public ImageButton(final Composite parent, String icon, String tooltip) {
		this.parent = parent;

		button = new Button(parent, SWT.FLAT);
		

		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(icon);
		image = new Image(parent.getDisplay(), is);
		setIcon();
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					proceed();
					break;
				case SWT.FocusOut:
					doWhenOutOfFocus();
					break;
				case SWT.Dispose:
					button.getImage().dispose();
					break;
				}
			}
		};
		button.setToolTipText(tooltip);
		button.addListener(SWT.Selection, listener);
		button.addListener(SWT.Dispose, listener);
		button.addListener(SWT.FocusOut, listener);
	}

	protected void setIcon() {
		button.setImage(image);
	}

	protected void proceed() {
	}

	protected void doWhenOutOfFocus() {
	}

	public Button getButton() {
		return button;
	}

	protected Image getImage(String imageName) {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(imageName);
		ImageData source = new ImageData(is);
		ImageData mask = source.getTransparencyMask();
		Image image = new Image(null, source, mask);
		return image;
	}
}

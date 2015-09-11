package com.fywl.ILook.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Test extends Shell {

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Test shell = new Test(display, SWT.SHELL_TRIM);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell
	 * 
	 * @param display
	 * @param style
	 */
	public Test(Display display, int style) {
		super(display, style);
		createContents();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(500, 375);

		// final Browser browser = new Browser(this, SWT.NONE);
		// browser.setUrl("www.google.com.hk");
		// browser.setBounds(10, 10, 374, 216);
		// browser.setText("dfsjgskljdfgklsdjfl;fkJDFKASDLFJ;SDFJGLKDSKFGD;FKG;LDSKF");

		final Text mulText = new Text(this, SWT.WRAP | SWT.MULTI);
		mulText.setBounds(30, 0, 300, 360);

		final Button button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				test(Test.this, mulText, 0, 0, 300, 300, "d:/test.jpg");
			}
		});
		button.setText("button");
		button.setBounds(400, 100, 48, 22);
		//
	}

	protected void test(Shell shell, Text browser, int xDifference,
			int yDifference, int imageWidth, int imageHeight, String imgUrl) {

		GC gc = new GC(browser);
		// 设置默认的宽度
		if (imageWidth <= 0) {
			imageWidth = 1050;
		}

		// 设置默认的高度
		if (imageWidth <= 0) {
			imageHeight = 450;
		}

		// 始终最前 (50:x轴,50:主轴,1050:图像的x轴的长度,450:图像的y轴的长度)
		// OS.SetWindowPos(shell.handle , OS.HWND_TOPMOST, 0 , 0 ,imageWidth,
		// imageHeight , SWT.NULL);
		shell.setVisible(true);
		shell.open();
		shell.layout();
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Point size = browser.getSize();

		Image image = new Image(shell.getDisplay(), size.x - xDifference,
				size.y - yDifference);
		gc.copyArea(image, 0, 0);
		// shell.setVisible(false);

		// BufferedImage bufferedImage=new
		// BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);

		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { image.getImageData() };
		imageLoader.save(imgUrl, SWT.IMAGE_JPEG);
		gc.dispose();
		image.dispose();
		// display.dispose();

		// System.out.println("captured");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

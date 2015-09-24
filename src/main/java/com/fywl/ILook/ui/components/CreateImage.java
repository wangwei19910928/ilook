package com.fywl.ILook.ui.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.utils.ImageUtil;
import com.fywl.ILook.utils.PropertyUtil;

public class CreateImage extends Composite {

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// try {
		// Display display = Display.getDefault();
		// CreateImage shell = new CreateImage(display, SWT.SHELL_TRIM);
		// shell.open();
		// shell.layout();
		// while (!shell.isDisposed()) {
		// if (!display.readAndDispatch())
		// display.sleep();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * Create the shell
	 * 
	 * @param display
	 * @param style
	 */
	public CreateImage(Composite parent, int style) {
		super(parent, style);
		createContents();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		File file = new File("user.properties");
		setSize(Constants.Shell_Constant.WIDTH, Constants.Shell_Constant.HEIGHT);
		// 顶部
		new TitleComposite(this, SWT.NONE, "设置文字");

		final Text mulText1 = new Text(this, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		mulText1.setBounds(5, 40, 240, 289);
		
		final Text mulText2 = new Text(this, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		mulText2.setBounds(250, 40, 240, 289);
		
		FileInputStream fis;
		Properties properties = new Properties();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fis = new FileInputStream(file);
			properties.load(fis);
			fis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null != properties.getProperty("personInfo1")){
			mulText1.setText(properties.getProperty("personInfo1"));
		}
		if(null != properties.getProperty("personInfo2")){
			mulText2.setText(properties.getProperty("personInfo2"));
		}
		
		int[] location = {210, 400, 80, 25};
		final Label llabel = new MyLabel(this, SWT.NONE, "", location);
		Image lImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/confirm.png"));
		llabel.setImage(lImage);
		llabel.addListener(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				test(mulText1,"myText1.jpg");
				test(mulText2,"myText2.jpg");
				File file = new File("user.properties");
				String str1 = mulText1.getText();
				String str2 = mulText2.getText();
				Properties properties = new Properties();
				properties.setProperty("personInfo1", str1);
				properties.setProperty("personInfo2", str2);
				PropertyUtil.setValue(properties, file);
				// 上传状态 遮罩层
				final Composite mp = new Composite(getShell(), SWT.NO_BACKGROUND);
				mp.setBounds(0, 0, 500, 440);
				mp.moveAbove(null);
				new EmptyPanel(mp, SWT.NONE, "保存成功","  关 闭",mulText1.getParent());
			}
		});
	}

	protected void test(Text browser,  String imgUrl) {

		GC gc = new GC(browser);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Image image = new Image(getDisplay(), 233, 282);
		gc.copyArea(image, 0, 0);

		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { image.getImageData() };
		imageLoader.save(imgUrl, SWT.IMAGE_JPEG);
		gc.dispose();
		image.dispose();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

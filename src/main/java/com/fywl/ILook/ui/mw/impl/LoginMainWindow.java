package com.fywl.ILook.ui.mw.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.FootLabel;
import com.fywl.ILook.ui.components.InfoLaber;
import com.fywl.ILook.ui.components.MyLabel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.fywl.ILook.ui.components.panel.Panel;
import com.fywl.ILook.ui.components.panel.impl.TitlePanel;
import com.fywl.ILook.ui.mw.MainWindow;
import com.fywl.ILook.utils.DESCrypt;
import com.fywl.ILook.utils.HttpRequest;
import com.fywl.ILook.utils.ImageUtil;
import com.fywl.ILook.utils.PropertyUtil;

public class LoginMainWindow extends MainWindow implements Closer {

	// 出事最小化到系统托盘的监听
	private Tray tray;

	private File sFile;
	
	private Text text;

	private Text text_1;
	
	private Button button;
	
	private Button loginButton;
	
	private Label messageLabel;
	
	public LoginMainWindow(){
		super();
		
		init();
	}
	
	@Override
	protected void beforeInit() {
		tray = display.getSystemTray();
		sFile = new File("user.properties");
	}

	@Override
	protected void beforeComponentsInit() {
		initSmallFunction();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutDown();
			}
		});
	}

	@Override
	protected void componentsInit() {
		initTitlePanel();

		initInfoLaber();

		initMiddle();

		initFootLabel();
	}

	@Override
	protected void afterInit() {
		// 判断是否有自动登录状态，或者用户名
		if (sFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(sFile);
				Properties properties = new Properties();
				properties.load(fis);
				fis.close();
				String username = properties.getProperty("username");
				if (null != username && username.length() > 0) {
					text.setText(username);
				}
				String autoflag = properties.getProperty("openSoftwareAutoLoginFlag");
				if (null != autoflag && "true".equals(autoflag)) {
					button.setSelection(true);
					// String password = properties.getProperty("password");
					DESCrypt des = new DESCrypt();// 实例化一个对像
					String strDes = des.getDesString(properties
							.getProperty("password"));// 把String 类型的密文解密
					text_1.setText(strDes);
					Event e = new Event();
					e.widget = loginButton;
					// 主动触发button点击事件
					loginButton.notifyListeners(SWT.MouseUp, e);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 初始化顶部logo、最小化和关闭按钮
	private void initTitlePanel() {
		Panel titlePanel = new TitlePanel(this, shell, SWT.NONE,
				new VideoRecorder());
		titlePanel.setLocation(Constants.TITLE_PANEL_Constant.LOCATION[0],
				Constants.TITLE_PANEL_Constant.LOCATION[1]);
		titlePanel.setSize(Constants.TITLE_PANEL_Constant.LOCATION[2],
				Constants.TITLE_PANEL_Constant.LOCATION[3]);

	}

	// 头部信息
	private void initInfoLaber() {
		InfoLaber info = new InfoLaber(shell, SWT.INHERIT_DEFAULT);

		info.setLocation(Constants.INFO_LABEL_Constant.LOCATION[0],
				Constants.INFO_LABEL_Constant.LOCATION[1]);
		info.setSize(Constants.INFO_LABEL_Constant.LOCATION[2],
				Constants.INFO_LABEL_Constant.LOCATION[3]);

		Image infoImage = ImageUtil.getInstance().getImage(
				info.getDisplay(),
				this.getClass().getResourceAsStream(
						Constants.INFO_LABEL_Constant.BACKGROUND_URL));
		info.setBackgroundImage(infoImage);
		info.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Label l = new MyLabel(info, SWT.NONE,
				Constants.LOGIN_HEAD_Constant.CONTENT,
				Constants.LOGIN_HEAD_Constant.LOCATION);
		l.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface",
				22, SWT.BOLD));
	}

	// 初始化底部信息
	private void initFootLabel() {
		FootLabel footLabel = new FootLabel(shell, SWT.NONE);
		footLabel.setLocation(Constants.FOOT_LABEL_Constant.LOCATION[0],
				Constants.FOOT_LABEL_Constant.LOCATION[1]);
		footLabel.setSize(Constants.FOOT_LABEL_Constant.LOCATION[2],
				Constants.FOOT_LABEL_Constant.LOCATION[3]);
		footLabel.setBackground(SWTResourceManager.getColor(
				Constants.FOOT_LABEL_Constant.BACKGROUND[0],
				Constants.FOOT_LABEL_Constant.BACKGROUND[1],
				Constants.FOOT_LABEL_Constant.BACKGROUND[2]));
	}

	private void initMiddle() {
		
		/*
		 * 登录部分初始化
		 */
		final Composite cp = new Composite(shell, SWT.NONE);
		cp.setBackgroundMode(SWT.INHERIT_DEFAULT);
		cp.setBounds(0, 130, 500, 280);

		Label icon = new Label(cp, SWT.NONE);
		icon.setBounds(Constants.LOGIN_HEAD_Constant.ICON_LOCATION[0],
				Constants.LOGIN_HEAD_Constant.ICON_LOCATION[1],
				Constants.LOGIN_HEAD_Constant.ICON_LOCATION[2],
				Constants.LOGIN_HEAD_Constant.ICON_LOCATION[3]);
		icon.setImage(ImageUtil.getInstance().getImage(
				display,
				this.getClass().getResourceAsStream(
						Constants.INFO_LABEL_Constant.ICON_URL)));
		
		messageLabel = new Label(cp, SWT.NONE);
		messageLabel.setBounds(Constants.LOGIN_HEAD_Constant.MESSAGE_LOCATION[0], Constants.LOGIN_HEAD_Constant.MESSAGE_LOCATION[1], Constants.LOGIN_HEAD_Constant.MESSAGE_LOCATION[2], Constants.LOGIN_HEAD_Constant.MESSAGE_LOCATION[3]);
		messageLabel.setText("用户名或密码错误");
		messageLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		messageLabel.setVisible(false);
		
		text = new Text(cp, SWT.BORDER | SWT.SINGLE);
		text.setBounds(Constants.LOGIN_HEAD_Constant.USERNAME_LOCATION[0],
				Constants.LOGIN_HEAD_Constant.USERNAME_LOCATION[1],
				Constants.LOGIN_HEAD_Constant.USERNAME_LOCATION[2],
				Constants.LOGIN_HEAD_Constant.USERNAME_LOCATION[3]);

		text_1 = new Text(cp, SWT.BORDER | SWT.PASSWORD);
		text_1.setBounds(Constants.LOGIN_HEAD_Constant.PASSWORD_LOCATION[0],
				Constants.LOGIN_HEAD_Constant.PASSWORD_LOCATION[1],
				Constants.LOGIN_HEAD_Constant.PASSWORD_LOCATION[2],
				Constants.LOGIN_HEAD_Constant.PASSWORD_LOCATION[3]);

		button = new Button(cp, SWT.CHECK);
		button.setFont(SWTResourceManager.getFont(
				".Helvetica Neue DeskInterface", 10, SWT.NORMAL));
		button.setText("自动登录");
		button.setBounds(Constants.LOGIN_HEAD_Constant.AUTO_LOGIN_LOCATION[0],
				Constants.LOGIN_HEAD_Constant.AUTO_LOGIN_LOCATION[1],
				Constants.LOGIN_HEAD_Constant.AUTO_LOGIN_LOCATION[2],
				Constants.LOGIN_HEAD_Constant.AUTO_LOGIN_LOCATION[3]);

		Label ft = new Label(cp, SWT.NORMAL);
		ft.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface",
				10, SWT.NORMAL));
		ft.setText("忘记密码");
		ft.setAlignment(SWT.RIGHT);
		ft.setBounds(Constants.LOGIN_HEAD_Constant.FORGET_LOCATION[0],
				Constants.LOGIN_HEAD_Constant.FORGET_LOCATION[1],
				Constants.LOGIN_HEAD_Constant.FORGET_LOCATION[2],
				Constants.LOGIN_HEAD_Constant.FORGET_LOCATION[3]);
		ft.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://www.baidu.com");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		/*
		 * 登录中部分初始化
		 */
		final ProgressBar pb = new ProgressBar(shell, SWT.INDETERMINATE);
		pb.setBounds(200, 210, 100, 20);

		loginButton = new Button(cp, SWT.BORDER | SWT.FLAT);
		loginButton.setFont(SWTResourceManager.getFont(
				".Helvetica Neue DeskInterface", 12, SWT.BOLD));
		loginButton.setBounds(Constants.LOGIN_HEAD_Constant.LOGIN_LOCATION[0],
				Constants.LOGIN_HEAD_Constant.LOGIN_LOCATION[1],
				Constants.LOGIN_HEAD_Constant.LOGIN_LOCATION[2],
				Constants.LOGIN_HEAD_Constant.LOGIN_LOCATION[3]);
		loginButton.setText("登录");

		loginButton.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//登录中····
				cp.setVisible(false);
				pb.moveAbove(null);
				//登录
				String str = HttpRequest.sendGet(Constants.LOGIN_HEAD_Constant.HTTP_LOGIN_URL, "username="+text.getText()+"&password="+text_1.getText());
				
				//成功
				if(str.equals("1")){
					//保存用户信息到本地
					Properties properties = new Properties();
					properties.setProperty("username", text.getText());
					properties.setProperty("openSoftwareAutoLoginFlag", button.getSelection() + "");
					if(button.getSelection()){
						DESCrypt des = new DESCrypt();// 实例化一个对像
						String strEnc = des.getEncString(text_1.getText());// 加密字符串,返回String的密文
						properties.setProperty("password", strEnc);
					}
					PropertyUtil.setValue(properties,sFile);
					
					tray.dispose();
					shell.dispose();
					InfoBean ib = new InfoBean();
					ib.setName("张三疯");
					ib.setSchool("北京市第三中学");
					ib.setTrainAge(3);
					ib.setType("体育");
					
					//打开网站
					FileInputStream fis;
					try {
						fis = new FileInputStream(sFile);
						properties.load(fis);
						fis.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String openWebsiteAfterLoginFlag = properties.getProperty("openWebsiteAfterLoginFlag");
					System.out.println(openWebsiteAfterLoginFlag);
					if("true".equals(openWebsiteAfterLoginFlag)){
						try {
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://"+Constants.Shell_Constant.WEBSITE);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					new FunctionMainWindow(ib);
				}else{
//					cp1.setVisible(false);
					messageLabel.setVisible(true);
					pb.moveBelow(null);
					cp.setVisible(true);
				}
			}
		});
	}

	@Override
	public void shutDown() {
		shell.dispose();
	}

	// 出事最小化到系统托盘的监听
	private void initSmallFunction() {
		// 构造系统栏控件
		TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		trayItem.setImage(ImageUtil.getInstance().getImage(Constants.Shell_Constant.TRAY_URL));
		trayItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toggleDisplay();
			}
		});
	}

	// 最小化功能
	private void toggleDisplay() {
		try {
			shell.setVisible(!shell.isVisible()); // 控制窗口顯示
			tray.getItem(0).setVisible(!shell.isVisible()); // 控制托盤圖標顯示

			if (shell.getVisible()) {
				shell.setMinimized(false);
				shell.setActive();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		new LoginMainWindow();
	}
}

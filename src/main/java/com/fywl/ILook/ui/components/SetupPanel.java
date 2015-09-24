package com.fywl.ILook.ui.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.utils.ImageUtil;
import com.fywl.ILook.utils.PropertyUtil;
import com.fywl.ILook.utils.RegeditTool;

public class SetupPanel extends Composite implements Closer {

	public SetupPanel(Composite parent, int style) {
		super(parent, style);

		init1();
	}

	@Override
	public void shutDown() {
		this.dispose();
	}
	
	private void init1(){
		//读取原有设置
		File file = new File("user.properties");
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
		
		this.setBounds(0, 0, Constants.Shell_Constant.WIDTH,
				Constants.Shell_Constant.HEIGHT);
		//顶部
		new TitleComposite(this, SWT.NONE, "设置");
		
		//选项卡
		final CTabFolder ctf = new CTabFolder(this, SWT.NONE);
		ctf.setBounds(0, 30, Constants.Shell_Constant.WIDTH, 350);
		ctf.setLayout(new FillLayout());
		
		Rectangle cpLocation = new Rectangle(0, 30, 400, 400);
		
		CTabItem ctb1 = new CTabItem(ctf, SWT.NONE);
		ctb1.setText("   常规      ");
		
		ctf.setSelection(ctb1);
		
		//常规设置面板
		Composite setupCP = new Composite(ctf, SWT.NONE);
		setupCP.setBounds(cpLocation);
		//录制方式
		final Button dplz = new Button(setupCP, SWT.CHECK);
		dplz.setText("单屏录制");
		dplz.setBounds(100, 50, 80, 30);
		String dplzFlag = properties.getProperty("dplzFlag");
		if("true".equals(dplzFlag)){
			dplz.setSelection(true);
		}
		// 存储
		Label mr = new Label(setupCP, SWT.CHECK);
		mr.setText("存储");
		mr.setBounds(100, 90, 30, 30);

		final Text cunchu = new Text(setupCP, SWT.BORDER);
		cunchu.setBounds(190, 90, 200, 20);
		cunchu.setEditable(false);
		String videoPath = properties.getProperty("videoPath");
		if(null != videoPath && !"".equals(videoPath)){
			cunchu.setText(videoPath);
		}else{
			cunchu.setText(Constants.SETUP_Constant.videoPath);
		}
		
		Label ggml = new Label(setupCP, SWT.NONE);
		Image changeImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/setup_change.png"));
		ggml.setImage(changeImage);
		ggml.setBounds(150, 130, 100, 25);
		ggml.setBackground(SWTResourceManager.getColor(244, 253, 209));

		ggml.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DirectoryDialog folderdlg = new DirectoryDialog(cunchu
						.getParent().getShell());
				// 设置文件对话框的标题
				folderdlg.setText("文件选择");
				// 设置初始路径
				folderdlg.setFilterPath("SystemDrive");
				// 设置对话框提示文本信息
				folderdlg.setMessage("请选择储存位置");
				// 打开文件对话框，返回选中文件夹目录
				String selecteddir = folderdlg.open();
				if(null != selecteddir && !"".equals(selecteddir)){
					cunchu.setText(selecteddir);
				}
			}
		});

		Label dkwjj = new Label(setupCP, SWT.NONE);
		Image openImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/setup_open.png"));
		dkwjj.setImage(openImage);
		dkwjj.setBounds(300, 130, 100, 25);
		dkwjj.setBackground(SWTResourceManager.getColor(244, 253, 209));
		dkwjj.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					Runtime.getRuntime().exec("explorer " + cunchu.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		ctb1.setControl(setupCP);
		
		CTabItem ctb2 = new CTabItem(ctf, SWT.NONE);
		ctb2.setText("   课程信息      ");
		//课程信息面板
		Composite classInfoCP = new Composite(ctf, SWT.NONE);
		classInfoCP.setBounds(cpLocation);
		//主题
		int[] location = {100,50,40,30};
		new MyLabel(classInfoCP, SWT.NONE, "主题", location);
		
		final Text themeText = new Text(classInfoCP, SWT.NONE | SWT.BORDER);
		themeText.setBounds(170, 50, 200, 30);
		themeText.setTextLimit(20);
		String theme = properties.getProperty("themeStr");
		if(null != theme){
			themeText.setText(theme);
		}
		//主讲
		int[] location1 = {100,90,40,30};
		new MyLabel(classInfoCP, SWT.NONE, "主讲", location1);
		
		final Text teacherText = new Text(classInfoCP, SWT.NONE | SWT.BORDER);
		teacherText.setBounds(170, 90, 200, 30);
		teacherText.setTextLimit(20);
		String teacher = properties.getProperty("teacherStr");
		if(null != teacher){
			teacherText.setText(teacher);
		}
		//学校
		int[] location2 = {100,130,40,30};
		new MyLabel(classInfoCP, SWT.NONE, "老师", location2);
		
		final Text schoolText = new Text(classInfoCP, SWT.NONE | SWT.BORDER);
		schoolText.setBounds(170, 130, 200, 30);
		schoolText.setTextLimit(20);
		String school = properties.getProperty("schoolStr");
		if(null != school){
			schoolText.setText(school);
		}
		//描述
		int[] location3 = {100,170,40,30};
		new MyLabel(classInfoCP, SWT.NONE, "描述", location3);
		
		final Text infoText = new Text(classInfoCP, SWT.NONE | SWT.BORDER);
		infoText.setBounds(170, 170, 200, 30);
		infoText.setTextLimit(20);
		String info = properties.getProperty("infoStr");
		if(null != info){
			infoText.setText(info);
		}
		
		ctb2.setControl(classInfoCP);
		
		
		//底部按鈕
		// 恢复默认
		Label hfmr = new Label(this, SWT.NONE);
		final Image restoreImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/console.png"));
		hfmr.setImage(restoreImage);
		hfmr.setBounds(80, 400, 100, 25);
		hfmr.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ctf.getParent().dispose();
			}
		});

		// 保存更改
		final Label bcgg = new Label(this, SWT.NONE);
		final Image saveImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/confirm.png"));
		bcgg.setImage(saveImage);
		bcgg.setBounds(300, 400, 80, 25);
		bcgg.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//保存
				boolean dplzFlag = dplz.getSelection();
				String videoPath = cunchu.getText();
				
				//课程信息
				String themeStr = themeText.getText();
				String schoolStr = schoolText.getText();
				String teacherStr = teacherText.getText();
				String infoStr = infoText.getText();
				
				File file = new File("user.properties");
				Properties properties = new Properties();
				properties.setProperty("dplzFlag", dplzFlag+"");
				properties.setProperty("videoPath", videoPath);
				properties.setProperty("themeStr", themeStr);
				properties.setProperty("schoolStr", schoolStr);
				properties.setProperty("teacherStr", teacherStr);
				properties.setProperty("infoStr", infoStr);
				
				PropertyUtil.setValue(properties, file);
				RecordConfig config = RecordConfig.get();
				config.setSingleRecording(dplzFlag);
				
				ctf.getParent().dispose();
			}
		});
	}

	private void init() {
		File file = new File("user.properties");
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
		
		this.setBounds(0, 0, Constants.Shell_Constant.WIDTH,
				Constants.Shell_Constant.HEIGHT);
		//顶部
		new TitleComposite(this, SWT.NONE, "设置");

		// 登录
		int locationDL[] = { 40, 60, 45, 20 };
		new MyLabel(this, SWT.NONE, "登 录", locationDL);

		final Button kjqd = new Button(this, SWT.CHECK);
		kjqd.setText("开机时自动启动");
		kjqd.setBounds(150, 50, 300, 30);
		String openComputerAutoStartFlag = properties.getProperty("openComputerAutoStartFlag");
		if("true".equals(openComputerAutoStartFlag)){
			kjqd.setSelection(true);
		}

		final Button kjdl = new Button(this, SWT.CHECK);
		kjdl.setText("开启爱录课后自动登录");
		kjdl.setBounds(150, 90, 300, 30);
		String openSoftwareAutoLoginFlag = properties.getProperty("openSoftwareAutoLoginFlag");
		if("true".equals(openSoftwareAutoLoginFlag)){
			kjdl.setSelection(true);
		}

		final Button dl = new Button(this, SWT.CHECK);
		dl.setText("登录后打开官方网站");
		dl.setBounds(150, 130, 300, 30);
		String openWebsiteAfterLoginFlag = properties.getProperty("openWebsiteAfterLoginFlag");
		if("true".equals(openWebsiteAfterLoginFlag)){
			dl.setSelection(true);
		}

		// 显示
		int locationXS[] = { 40, 180, 45, 20 };
		new MyLabel(this, SWT.NONE, "显 示", locationXS);

		final Button xxtb = new Button(this, SWT.CHECK);
		xxtb.setText("在任务通知栏区域显示爱录课图标");
		xxtb.setBounds(150, 170, 300, 30);
		String showIconFlag = properties.getProperty("showIconFlag");
		if("true".equals(showIconFlag)){
			xxtb.setSelection(true);
		}
		
		Label pinfoLabel = new MyLabel(this, SWT.NONE, "设置显示文字", locationXS);
		pinfoLabel.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 10, SWT.NONE));
		pinfoLabel.setBounds(150, 210, 300, 30);
		pinfoLabel.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Composite ci = new CreateImage(getShell(), SWT.NONE);
				ci.moveAbove(null);
			}
		});
		
		final Button dplz = new Button(this, SWT.CHECK);
		dplz.setText("单屏录制");
		dplz.setBounds(150, 250, 80, 30);
		String dplzFlag = properties.getProperty("dplzFlag");
		if("true".equals(dplzFlag)){
			dplz.setSelection(true);
		}
		
//		GraphicsDevice gd=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		// 声音
		// int locationSY[] = { 40, 220, 45, 20 };
		// new MyLabel(this, SWT.NONE, "声 音", locationSY);
		//
		// Label mkf1 = new Label(this, SWT.NONE);
		// mkf1.setText("    打开文件夹 ");
		// mkf1.setBounds(300, 220, 90, 20);
		// mkf1.setBackground(SWTResourceManager.getColor(244, 253, 209));
		// mkf1.addListener(SWT.MouseUp, new Listener() {
		// @Override
		// public void handleEvent(Event event) {
		// Port.Info.LINE_IN.getName();
		// System.out.println(Port.Info.LINE_IN.toString());
		// Info[] info = MidiSystem.getMidiDeviceInfo();
		// try {
		// Receiver r = MidiSystem.getReceiver();
		// System.out.println(r.toString());
		// } catch (MidiUnavailableException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(info.length);
		// System.out.println(info[0].getName());
		// }
		// });
		//
		// Label mkf = new Label(this, SWT.CHECK);
		// mkf.setText("麦克风");
		// mkf.setBounds(150, 210, 300, 30);

		// 存储
		int locationCC[] = { 40, 310, 45, 20 };
		new MyLabel(this, SWT.NONE, "存 储", locationCC);

		Label mr = new Label(this, SWT.CHECK);
		mr.setText("默认");
		mr.setBounds(150, 310, 30, 30);

		final Text cunchu = new Text(this, SWT.BORDER);
		cunchu.setBounds(190, 310, 200, 20);
		cunchu.setEditable(false);
		String videoPath = properties.getProperty("videoPath");
		if(null != videoPath && !"".equals(videoPath)){
			cunchu.setText(videoPath);
		}else{
			cunchu.setText(Constants.SETUP_Constant.videoPath);
		}
		
		Label ggml = new Label(this, SWT.NONE);
		Image changeImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/setup_change.png"));
		ggml.setImage(changeImage);
		ggml.setBounds(150, 340, 100, 25);
		ggml.setBackground(SWTResourceManager.getColor(244, 253, 209));

		ggml.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DirectoryDialog folderdlg = new DirectoryDialog(cunchu
						.getParent().getShell());
				// 设置文件对话框的标题
				folderdlg.setText("文件选择");
				// 设置初始路径
				folderdlg.setFilterPath("SystemDrive");
				// 设置对话框提示文本信息
				folderdlg.setMessage("请选择储存位置");
				// 打开文件对话框，返回选中文件夹目录
				String selecteddir = folderdlg.open();
				if(null != selecteddir && !"".equals(selecteddir)){
					cunchu.setText(selecteddir);
				}
			}
		});

		Label dkwjj = new Label(this, SWT.NONE);
		Image openImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/setup_open.png"));
		dkwjj.setImage(openImage);
		dkwjj.setBounds(300, 340, 100, 25);
		dkwjj.setBackground(SWTResourceManager.getColor(244, 253, 209));
		dkwjj.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					Runtime.getRuntime().exec("explorer " + cunchu.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// 恢复默认
		Label hfmr = new Label(this, SWT.NONE);
		Image restoreImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/setup_restore.png"));
		hfmr.setImage(restoreImage);
		hfmr.setBounds(80, 380, 100, 25);
		hfmr.setBackground(SWTResourceManager.getColor(Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		hfmr.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				kjqd.setSelection(Constants.SETUP_Constant.openComputerAutoStartFlag);
				kjdl.setSelection(Constants.SETUP_Constant.openSoftwareAutoLoginFlag);
				dl.setSelection(Constants.SETUP_Constant.openWebsiteAfterLoginFlag);
				xxtb.setSelection(Constants.SETUP_Constant.showIconFlag);
				cunchu.setText(Constants.SETUP_Constant.videoPath);
			}
		});

		// 保存更改
		Label bcgg = new Label(this, SWT.NONE);
		Image saveImage = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/setup_save.png"));
		bcgg.setImage(saveImage);
		bcgg.setBounds(300, 380, 100, 25);
		bcgg.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		bcgg.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// 上传状态 遮罩层
				final Composite mp = new Composite(cunchu.getParent()
						.getShell(), SWT.NO_BACKGROUND);
				mp.setBounds(0, 0, 500, 440);
				mp.moveAbove(null);
				File file = new File("user.properties");
				boolean openComputerAutoStartFlag = kjqd.getSelection();
				if(openComputerAutoStartFlag){
					RegeditTool.setValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run", "ilook", "D:\\qq\txupd.exe");
				}else{
					RegeditTool.deleteValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run", "ilook");
				}
				boolean openSoftwareAutoLoginFlag = kjdl.getSelection();
				boolean openWebsiteAfterLoginFlag = dl.getSelection();
				boolean	showIconFlag = xxtb.getSelection();
				boolean dplzFlag = dplz.getSelection();
				String videoPath = cunchu.getText();
				Properties properties = new Properties();
				properties.setProperty("openComputerAutoStartFlag", openComputerAutoStartFlag+"");
				properties.setProperty("openSoftwareAutoLoginFlag", openSoftwareAutoLoginFlag+"");
				properties.setProperty("openWebsiteAfterLoginFlag", openWebsiteAfterLoginFlag+"");
				properties.setProperty("dplzFlag", dplzFlag+"");
				properties.setProperty("showIconFlag", showIconFlag+"");
				properties.setProperty("videoPath", videoPath);
				PropertyUtil.setValue(properties, file);
				RecordConfig config = RecordConfig.get();
				config.setSingleRecording(dplzFlag);
				new EmptyPanel(mp, SWT.NONE, "保存成功","  关 闭",null);
			}
		});

	}
}

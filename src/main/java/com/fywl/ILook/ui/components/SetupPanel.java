package com.fywl.ILook.ui.components;

import java.awt.Dimension;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.mw.impl.FunctionMainWindow;
import com.fywl.ILook.utils.ImageUtil;
import com.fywl.ILook.utils.PropertyUtil;
import com.fywl.ILook.utils.RegeditTool;
import com.github.sarxos.webcam.Webcam;

public class SetupPanel extends Composite{
	private Closer closer;
	private VideoPlayBackPanel headVideoPlayBackPanel;
	
	private VideoPlayBackPanel noteVideoPlayBackPanel;

	public SetupPanel(Closer closer,Composite parent, int style,VideoPlayBackPanel face,VideoPlayBackPanel note) {
		super(parent, style);
		this.closer = closer;
		this.headVideoPlayBackPanel=face;
		this.noteVideoPlayBackPanel=note;
		init1();
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
		
		Rectangle cpLocation = new Rectangle(0, 30, 500, 400);
		/*
		 * 常规设置
		 */
		CTabItem ctb1 = new CTabItem(ctf, SWT.NONE);
		ctb1.setText("   常规      ");
		
		ctf.setSelection(ctb1);
		
		//常规设置面板
		Composite setupCP = new Composite(ctf, SWT.NONE);
		setupCP.setBounds(cpLocation);
		//录制方式
		final Button dplz = new Button(setupCP, SWT.CHECK);
		dplz.setText("单屏录制");
		dplz.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 12, SWT.BOLD));
		dplz.setBounds(100, 50, 100, 30);
		String dplzFlag = properties.getProperty("dplzFlag");
		if("true".equals(dplzFlag)){
			dplz.setSelection(true);
		}
		//帧率
		int[] zllocation = {100,90,80,30};
		new MyLabel(setupCP, SWT.NONE, "帧率", zllocation);
		
		final Combo zlcombo = new Combo(setupCP, SWT.READ_ONLY);
		zlcombo.setBounds(200, 90, 80, 30);
		zlcombo.setItems(Constants.SETUP_Constant.frame_rate);
		String frameRate = (null == properties.getProperty("frameRate") || "".equals(properties.getProperty("frameRate")) ? "50000":properties.getProperty("frameRate"));
		for (int i = 0; i < Constants.SETUP_Constant.frame_rate.length; i++) {
			if(Constants.SETUP_Constant.frame_rate_value[i].equals(frameRate)){
				zlcombo.setText(frameRate);
				zlcombo.select(i);
			}
			zlcombo.setData(Constants.SETUP_Constant.frame_rate[i],
					Constants.SETUP_Constant.frame_rate_value[i]);
		}
		// 存储
		int[] cunchulocation = {100, 130, 40, 30};
		new MyLabel(setupCP, SWT.NONE, "存储", cunchulocation);
//		Label mr = new Label(setupCP, SWT.CHECK);
//		mr.setText("存储");
//		mr.setBounds(100, 90, 30, 30);

		final Text cunchu = new Text(setupCP, SWT.BORDER);
		cunchu.setBounds(190, 130, 200, 20);
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
		ggml.setBounds(150, 170, 100, 25);
		ggml.setBackground(SWTResourceManager.getColor(244, 253, 209));

		ggml.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DirectoryDialog folderdlg = new DirectoryDialog(cunchu
						.getParent().getShell());
				// 设置文件对话框的标题
				folderdlg.setText("文件夹选择");
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
		dkwjj.setBounds(300, 170, 100, 25);
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
		
		/*
		 * 课程信息设置
		 */
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
		
		/*
		 * 水印设置
		 */
		CTabItem ctb3 = new CTabItem(ctf, SWT.NONE);
		ctb3.setText("   水印设置     ");
		//水印设置面板
		Composite waterInfoCP = new Composite(ctf, SWT.NONE);
		waterInfoCP.setBounds(cpLocation);
		
		//图片水印
		int[] tplocation = {100,50,80,30};
		new MyLabel(waterInfoCP, SWT.NONE, "图片水印", tplocation);
		
		final Text tupianPath = new Text(waterInfoCP, SWT.BORDER);
		tupianPath.setBounds(200, 50, 150, 20);
		tupianPath.setEditable(false);
		String water_img = properties.getProperty("water_img");
		if(null != water_img){
			tupianPath.setText(water_img);
		}
		Label ggml1 = new Label(waterInfoCP, SWT.NONE);
		final Image changeImage1 = ImageUtil.getInstance().getImage(
				this.getDisplay(),
				this.getClass().getResourceAsStream(
						"/images/changePic.png"));
		ggml1.setImage(changeImage1);
		ggml1.setBounds(380, 45, 100, 25);
		ggml1.setBackground(SWTResourceManager.getColor(255, 255, 0));
		

		ggml1.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				FileDialog filedlg=new FileDialog(cunchu
						.getParent().getShell(),SWT.OPEN);
				//设置文件对话框的标题
				filedlg.setText("文件选择");
				//设置初始路径
				filedlg.setFilterPath("SystemRoot");
				//打开文件对话框，返回选中文件的绝对路径
				String selected=filedlg.open();
				if(null != selected){
					tupianPath.setText(selected);
				}
			}
		});
		
		
		//文字水印
		int[] wzlocation = {100,90,80,30};
		new MyLabel(waterInfoCP, SWT.NONE, "文字水印", wzlocation);
		
		final Text wenzi = new Text(waterInfoCP, SWT.BORDER);
		wenzi.setBounds(200, 90, 200, 20);
		String water_text = properties.getProperty("water_text");
		if(null != water_text){
			wenzi.setText(water_text);
		}
		
		//字体
		int[] ztlocation = {100,130,80,30};
		new MyLabel(waterInfoCP, SWT.NONE, "字体/颜色", ztlocation);
		
		final Combo ztcombo = new Combo(waterInfoCP, SWT.READ_ONLY);
		ztcombo.setBounds(200, 130, 80, 30);
		ztcombo.setItems(Constants.SETUP_Constant.arr);
		String water_text_size = properties.getProperty("water_text_size");
		if(null != water_text_size){
			ztcombo.setText(water_text_size);
		}
		
		final Combo yscombo = new Combo(waterInfoCP, SWT.READ_ONLY);
		yscombo.setBounds(300, 130, 100, 30);
		yscombo.setItems(Constants.SETUP_Constant.color);
		String yanse = properties.getProperty("water_text_color");
		for (int i = 0; i < Constants.SETUP_Constant.color.length; i++) {
			if(Constants.SETUP_Constant.color_value[i].equals(yanse)){
				yscombo.select(i);
			}
			yscombo.setData(Constants.SETUP_Constant.color[i],
					Constants.SETUP_Constant.color_value[i]);
		}
		
		
		//水印位置
		int[] waterlocation = {100,170,80,30};
		new MyLabel(waterInfoCP, SWT.NONE, "水印位置", waterlocation);
		
		final Combo wzcombo = new Combo(waterInfoCP, SWT.READ_ONLY);
		wzcombo.setBounds(200, 170, 80, 30);
		wzcombo.setItems(Constants.SETUP_Constant.water_location);
		String weizhi = properties.getProperty("water_location");
		for (int i = 0; i < Constants.SETUP_Constant.water_location.length; i++) {
			if(Constants.SETUP_Constant.water_location_value[i].equals(weizhi)){
				wzcombo.select(i);
			}
			wzcombo.setData(Constants.SETUP_Constant.water_location[i],
					Constants.SETUP_Constant.water_location_value[i]);
		}
		ctb3.setControl(waterInfoCP);
		
		
		/*
		 * 摄像头设置
		 */
		CTabItem ctb4 = new CTabItem(ctf, SWT.NONE);
		ctb4.setText("   摄像头      ");
		//摄像头面板
		Composite webcamCP = new Composite(ctf, SWT.NONE);
		webcamCP.setBounds(cpLocation);
		
		//头部摄像头
		int[] headWebcamlocation = {100,50,100,30};
		new MyLabel(webcamCP, SWT.NONE, "头部摄像头", headWebcamlocation);
		//分辨率
		int[] headlvlocation = {100,90,100,30};
		new MyLabel(webcamCP, SWT.NONE, "分辨率", headlvlocation);
		//笔记摄像头
		int[] noteWebcamlocation = {100,130,100,30};
		new MyLabel(webcamCP, SWT.NONE, "笔记摄像头", noteWebcamlocation);
		//分辨率
		int[] notelvlocation = {100,170,100,30};
		new MyLabel(webcamCP, SWT.NONE, "分辨率", notelvlocation);
		
		final Combo headWebcamCombo = new Combo(webcamCP, SWT.READ_ONLY);
		headWebcamCombo.setBounds(200, 50, 180, 30);
		
		final Combo headlvCombo = new Combo(webcamCP, SWT.READ_ONLY);
		headlvCombo.setBounds(200, 90, 180, 30);
		
		final Combo noteWebcamCombo = new Combo(webcamCP, SWT.READ_ONLY);
		noteWebcamCombo.setBounds(200, 130, 180, 30);
		
		final Combo notelvCombo = new Combo(webcamCP, SWT.READ_ONLY);
		notelvCombo.setBounds(200, 170, 180, 30);
//		String weizhi = properties.getProperty("water_location");
		String[] webcamList = new String[Webcam.getWebcams().size()];
		for (int i = 0; i < Webcam.getWebcams().size(); i++) {
//			if(Constants.SETUP_Constant.water_location_value[i].equals(weizhi)){
//				wzcombo.select(i);
//			}
			webcamList[i] = Webcam.getWebcams().get(i).getName();
			headWebcamCombo.setData(Webcam.getWebcams().get(i).getName(),i);
			noteWebcamCombo.setData(Webcam.getWebcams().get(i).getName(),i);
		}
		headWebcamCombo.setItems(webcamList);
		noteWebcamCombo.setItems(webcamList);
		
		
		String[] lvStr = {"800X600","1024X768","1280X960","2048X1536"};
		for (int i = 0; i < lvStr.length; i++) {
//			if(Constants.SETUP_Constant.water_location_value[i].equals(weizhi)){
//				wzcombo.select(i);
//			}
			headlvCombo.setData(lvStr[i],i);
			notelvCombo.setData(lvStr[i],i);
		}
		headlvCombo.setItems(lvStr);
		notelvCombo.setItems(lvStr);
		
		ctb4.setControl(webcamCP);
		
		
		
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
				String frameRate = zlcombo.getText();
				Object zl = zlcombo.getData(frameRate);
				
				//课程信息
				String themeStr = themeText.getText();
				String schoolStr = schoolText.getText();
				String teacherStr = teacherText.getText();
				String infoStr = infoText.getText();
				
				//水印
				String water_img = tupianPath.getText();
				String water_text = wenzi.getText();
				String water_text_size = ztcombo.getText();
				String yanse = yscombo.getText();
				Object water_text_color = yscombo.getData(yanse);
				if(null == water_text_color){
					water_text_color = 1;
				}
				String weizhi = wzcombo.getText();
				Object water_location = wzcombo.getData(weizhi);
				if(null == water_location){
					water_location = 3;
				}
				
				//分辨率
				String headLV = headlvCombo.getText();
				String noteLV = notelvCombo.getText();
				
				File file = new File("user.properties");
				Properties properties = new Properties();
				//常规设置
				properties.setProperty("dplzFlag", dplzFlag+"");
				properties.setProperty("videoPath", videoPath);
				properties.setProperty("frameRate", zl.toString());
				//课程信息
				properties.setProperty("themeStr", themeStr);
				properties.setProperty("schoolStr", schoolStr);
				properties.setProperty("teacherStr", teacherStr);
				properties.setProperty("infoStr", infoStr);
				//水印
				properties.setProperty("water_img", water_img);
				properties.setProperty("water_text", water_text);
				properties.setProperty("water_text_size", water_text_size);
				properties.setProperty("water_text_color", water_text_color.toString());
				properties.setProperty("water_location", water_location.toString());
				//分辨率
				if(!"".equals(headLV)){
					properties.setProperty("headLV", headLV);
				}
				if(!"".equals(noteLV)){
					properties.setProperty("noteLV", noteLV);
				}
				PropertyUtil.setValue(properties, file);
				RecordConfig config = RecordConfig.get();
				config.setSingleRecording(dplzFlag);
				
//				// 上传状态 遮罩层
//				final Composite mp = new Composite(getShell(), SWT.NONE);
//				mp.setBounds(0, 0, 500, 440);
//				mp.moveAbove(null);
//				// 展现层
//				final Composite mp1 = new EmptyPanel(mp, SWT.NONE, "正在上传");
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						getDisplay().timerExec(5,new Runnable() {
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								// 进度条
//								ProgressBar pb = new ProgressBar(mp1, SWT.INDETERMINATE);
//								pb.setBounds(100, 50, 100, 20);
//								pb.moveAbove(null);
//							}
//						});
//						getDisplay().timerExec(5, this);
//					}
//				});
				getShell().setVisible(false);
				headVideoPlayBackPanel.stop();
				noteVideoPlayBackPanel.stop();
				//摄像头
				String headWebcamString = headWebcamCombo.getText();
				if(!"".equals(headWebcamString)){
					int i = (Integer)headWebcamCombo.getData(headWebcamString);
					headVideoPlayBackPanel.stop();
					headVideoPlayBackPanel.dispose();
					Webcam.getWebcams().get(i).close();
					headVideoPlayBackPanel = new VideoPlayBackPanel(getShell(), i);
					headVideoPlayBackPanel.setBounds(Constants.Face_Constant.LOCATION_X,
							Constants.Face_Constant.LOCATION_Y,
							Constants.Face_Constant.WIDTH, Constants.Face_Constant.HEIGHT);
					if(!"".equals(headLV)){
						String[] str = headLV.split("X");
						headVideoPlayBackPanel.setSize(new Dimension(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
					}
					headVideoPlayBackPanel.start();
					Webcam.getWebcams().get(i).open();
					System.out.println(Webcam.getWebcams().get(i).isOpen());
				}else{
					if(!"".equals(headLV)){
						headVideoPlayBackPanel.stop();
						String[] str = headLV.split("X");
						headVideoPlayBackPanel.setSize(new Dimension(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
						headVideoPlayBackPanel.start();
					}
				}
				String noteWebcamString = noteWebcamCombo.getText();
				if(!"".equals(noteWebcamString)){
					int i = (Integer)noteWebcamCombo.getData(noteWebcamString);
					noteVideoPlayBackPanel.stop();
					noteVideoPlayBackPanel.dispose();
					Webcam.getWebcams().get(i).close();
					noteVideoPlayBackPanel = new VideoPlayBackPanel(getShell(), i);
					noteVideoPlayBackPanel.setBounds(Constants.NOTE_Constant.LOCATION_X,
							Constants.NOTE_Constant.LOCATION_Y,
							Constants.NOTE_Constant.WIDTH, Constants.NOTE_Constant.HEIGHT);
					if(!"".equals(noteLV)){
						String[] str = noteLV.split("X");
						noteVideoPlayBackPanel.setSize(new Dimension(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
					}
					noteVideoPlayBackPanel.start();
					Webcam.getWebcams().get(i).open();
					System.out.println(Webcam.getWebcams().get(i).isOpen());
					
				}else{
					if(!"".equals(noteLV)){
						noteVideoPlayBackPanel.stop();
						String[] str = noteLV.split("X");
						noteVideoPlayBackPanel.setSize(new Dimension(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
						noteVideoPlayBackPanel.start();
					}
				}
				getShell().setVisible(true);
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

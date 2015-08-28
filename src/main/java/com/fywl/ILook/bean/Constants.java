package com.fywl.ILook.bean;

import org.eclipse.swt.SWT;

public class Constants {
	// 窗口大小
	public interface Shell_Constant {
		public int WIDTH = 500;
		public int HEIGHT = 440;
	}

	// 脸部摄像头
	public interface Face_Constant {
		public int LOCATION_X = 2;

		public int LOCATION_Y = 135;

		public int WIDTH = 164;

		public int HEIGHT = 123;
	}

	// 顶部标题_常量信息
	public interface TITLE_PANEL_Constant {
		// panel位置信息
		public int[] LOCATION = { 0, 0, 500, 30 };
		// panel背景色
		public int[] BACKGROUND = { 60, 131, 209 };
		// logo位置信息
		public int[] LOGO_LOCATION = { 0, 0, 60, 30 };
		// logo图片位置
		public String LOGO_URL = "/images/logo3.png";
		// 关闭按钮位置信息
		public int[] CLOSE_LOCATION = { 470, 0, 30, 30 };
		// 关闭按钮图片位置
		public String CLOSE_URL = "images/close.png";
		// 最小化按钮位置信息
		public int[] SMALL_LOCATION = { 440, 0, 30, 30 };
		// 最小化按钮图片位置
		public String SMALL_URL = "images/small.png";
		// 文字位置信息
		public int[] TITLE_LOCATION = { 60, 5, 160, 100 };
		// 文字内容
		public String TITLE_CONTENT = "欢迎登录爱录课";
		// 文字颜色
		public int TITLE_COLOR = SWT.COLOR_WHITE;
	}

	// 屏幕大小
	public interface SCREEN_Constant {
		public int LOCATION_X = 166;

		public int LOCATION_Y = 139;

		public int WIDTH = 328;

		public int HEIGHT = 246;
	}

	// 笔记摄像头
	public interface NOTE_Constant {
		public int LOCATION_X = 2;

		public int LOCATION_Y = 262;

		public int WIDTH = 164;

		public int HEIGHT = 123;
	}

	// 信息展示部分_常量
	public interface INFO_LABEL_Constant {
		// panel位置信息
		public int[] LOCATION = { 0, 30, 500, 100 };
		// 背景图片
		public String BACKGROUND_URL = "/images/info.png";
		//头像位置信息
		public int[] ICON_LOCATION = {10,10,72,72};
		//头像url
		public String ICON_URL = "/images/icon.png";
		// 四种信息的坐标位置
		public int[] SCHOOL = { 100, 10, 100, 15 };
		public int[] NAME = { 100, 45, 60, 15 };
		public int[] TYPE = { 100, 80, 35, 15 };
		public int[] AGE = { 150, 80, 100, 15 };
	}

	// 底部工具条_常量信息
	public interface TOOL_PANEL_Constant {
		//PANEL位置信息
		public int[] LOCATION = {0,390,300,30};
		//开始录制的信息
		public int[] CAMERA_LOCATION = { 5, 0, 30, 30 };
		public String CAMERA_URL = "/images/camera.png";
		//结束
		public int[] STOP_LOCATION = { 50, 0, 30, 30 };
		public String STOP_URL = "/images/stop.png";
		//放弃
		public int[] GIVEUP_LOCATION = { 95, 0, 30, 30 };
		public String GIVEUP_URL = "/images/finish.png";
		
		//正在录制图片url
		public String WORKING_URL = "/images/working.png";
		//暂停录制图片url
		public String PAUSE_URL = "/images/pause.png";
		
	}

	// 底部信息_常量
	public interface FOOT_LABEL_Constant {
		//panel位置
		public int[] LOCATION = {0,420,500,30};
		//背景颜色
		public int[] BACKGROUND = {60, 131, 209};
		//版本信息
		public int[] VERSION_LOCATION = { 0, 0, 250, 30 };
		public String VERSION_CONTENT = "爱录课V2015.1.0";
		public int VERSION_COLOR = SWT.COLOR_WHITE;
		//网站信息
		public int[] WEBSITE_LOCATION = { 250, 0, 250, 30 };
		public String WEBSITE_CONTENT = "www.ilooke.cn";
		public int WEBSITE_COLOR = SWT.COLOR_WHITE;
	}
}

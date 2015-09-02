package com.fywl.ILook.ui.components;

import java.io.IOException;

import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.spi.MidiDeviceProvider;
import javax.sound.sampled.Port;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.inter.Closer;

public class SetupPanel extends Composite implements Closer {

	public SetupPanel(Composite parent, int style) {
		super(parent, style);

		init();
	}

	@Override
	public void shutDown() {
		this.dispose();
	}

	private void init() {
		this.setBounds(0, 0, Constants.Shell_Constant.WIDTH,
				Constants.Shell_Constant.HEIGHT);

		// 顶部标题
		Composite title = new Composite(this, SWT.NONE);
		title.setBackground(SWTResourceManager.getColor(
				Constants.Shell_Constant.BACKGROUND[0],
				Constants.Shell_Constant.BACKGROUND[1],
				Constants.Shell_Constant.BACKGROUND[2]));
		title.setBounds(0, 0, Constants.Shell_Constant.WIDTH, 30);
		// 标题文字
		int location[] = { 0, 5, 80, 30 };
		new MyLabel(title, SWT.NONE, "设置", location);
		// 关闭按钮
		final ImageButton closeBtn = new ImageButton(title,
				Constants.TITLE_PANEL_Constant.CLOSE_URL, "关闭");
		closeBtn.getButton().setBounds(470, 0, 30, 30);
		closeBtn.getButton().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				closeBtn.parent.getParent().dispose();
			}
		});

		// 登录
		int locationDL[] = { 40, 60, 45, 20 };
		new MyLabel(this, SWT.NONE, "登 录", locationDL);

		Button kjqd = new Button(this, SWT.CHECK);
		kjqd.setText("开机时自动启动");
		kjqd.setBounds(150, 50, 300, 30);

		Button kjdl = new Button(this, SWT.CHECK);
		kjdl.setText("开启爱录课后自动登录");
		kjdl.setBounds(150, 90, 300, 30);

		Button dl = new Button(this, SWT.CHECK);
		dl.setText("登录后打开官方网站");
		dl.setBounds(150, 130, 300, 30);

		// 显示
		int locationXS[] = { 40, 180, 45, 20 };
		new MyLabel(this, SWT.NONE, "显 示", locationXS);

		Button xxtb = new Button(this, SWT.CHECK);
		xxtb.setText("在任务通知栏区域显示爱录课图标");
		xxtb.setBounds(150, 170, 300, 30);

		// 声音
		int locationSY[] = { 40, 220, 45, 20 };
		new MyLabel(this, SWT.NONE, "声 音", locationSY);
		
		Label mkf1 = new Label(this, SWT.NONE);
		mkf1.setText("    打开文件夹 ");
		mkf1.setBounds(300, 220, 90, 20);
		mkf1.setBackground(SWTResourceManager.getColor(244, 253, 209));
		mkf1.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Port.Info.LINE_IN.getName();
				System.out.println(Port.Info.LINE_IN.toString());
				Info[] info =  MidiSystem.getMidiDeviceInfo();
				try {
					Receiver r =  MidiSystem.getReceiver();
					System.out.println(r.toString());
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(info.length);
				System.out.println(info[0].getName());
			}
		});

		Label mkf = new Label(this, SWT.CHECK);
		mkf.setText("麦克风");
		mkf.setBounds(150, 210, 300, 30);

		// 存储
		int locationCC[] = { 40, 260, 45, 20 };
		new MyLabel(this, SWT.NONE, "存 储", locationCC);

		Label mr = new Label(this, SWT.CHECK);
		mr.setText("默认");
		mr.setBounds(150, 260, 30, 30);

		final Text cunchu = new Text(this, SWT.BORDER);
		cunchu.setBounds(190, 260, 200, 20);

		Label ggml = new Label(this, SWT.NONE);
		ggml.setText("     更改目录 ");
		ggml.setBounds(150, 290, 90, 20);
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
				cunchu.setText(selecteddir);
			}
		});

		Label dkwjj = new Label(this, SWT.NONE);
		dkwjj.setText("    打开文件夹 ");
		dkwjj.setBounds(300, 290, 90, 20);
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

	}
}

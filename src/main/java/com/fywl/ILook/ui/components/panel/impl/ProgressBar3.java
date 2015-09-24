package com.fywl.ILook.ui.components.panel.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class ProgressBar3 {
    private Display display;
    private Shell shell;
    private Composite statusbar;
    private Label statusbarLabel;
    private ProgressBar progressBar;
    private Button hideProbarButton;
    public static void main(String[] args) {      new ProgressBar3().open();     }
    private void open() {
              display = Display.getDefault();
              shell = new Shell(display,SWT.ON_TOP);
              shell.setSize(500, 170);
              // ---------创建窗口中的其他界面组件-------------
              shell.setLayout(new GridLayout());
              progressBar = createProgressBar(shell);
              createButton(shell);
//              createMainComp(shell);//创建主面板
              // -----------------END------------------------
              shell.layout();
              shell.open();
              while (!shell.isDisposed()) {
                       if (!display.readAndDispatch())
                                display.sleep();
              }
              display.dispose();
    }
    private void createMainComp(Composite parent) {
//              Composite comp = new Composite(parent, SWT.BORDER);
//              comp.setLayoutData(new GridData(GridData.FILL_BOTH));
//              comp.setLayout(new RowLayout());
    	display.timerExec(5, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println(11);
				display.timerExec(5, this);
			}
		});
              
    }
    private void createButton(Composite parent) {
            //注册三个切换快捷键
				JIntellitype.getInstance().registerHotKey(112, JIntellitype.MOD_CONTROL, 112);
				JIntellitype.getInstance().registerHotKey(113, JIntellitype.MOD_CONTROL, 113);
				JIntellitype.getInstance().registerHotKey(114, JIntellitype.MOD_CONTROL, 114);
				//添加对应时间
				JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
					private boolean stopFlag = true;
					@Override
					public void onHotKey(int arg0) {
//              b3.addSelectionListener(new SelectionAdapter() {
//                       public void widgetSelected(SelectionEvent e) {
                                stopFlag = !stopFlag;
                                if (stopFlag) // 根据停止标志stopFlag来判断是停止还是运行
                                          stop();
                                else
                                          go();
                       }
                       private void stop() {
//                                b3.setEnabled(false);// 停止需要时间，在完全停止前要防止再次开始。
//                                b3.setText("GO");
                       }
                       private void go() {
//                                b3.setText("STOP");
//                    	   		progressBar = createProgressBar(statusbar);
//                                hideProbarButton.setEnabled(true);
//                                statusbar.layout();// 重新布局一下工具栏，使进度条显示出来
                                new Thread() {
                                          public void run() {
                                                   for (int i = 1; i < 11; i++) {
                                                            if (display.isDisposed() || stopFlag) {
                                                                      disposeProgressBar();
                                                                      return;
                                                            }
                                                            moveProgressBar(i);
                                                            try {  Thread.sleep(1000);          } catch (Throwable e2) {} //停一秒
                                                   }
                                                   disposeProgressBar();
                                          }
                                          private void moveProgressBar(final int i) {
                                                   display.asyncExec(new Runnable() {
                                                            public void run() {
                                                                      if (!progressBar.isDisposed())
                                                                               progressBar.setSelection(i * 10);
                                                            }
                                                   });
                                          }
                                          private void disposeProgressBar() {
                                                   if (display.isDisposed())   return;
                                                   display.asyncExec(new Runnable() {
                                                            public void run() {
                       // 这一句不能放在线程外执行，否则progressBar被创建后就立即被dispose了
                                                                      progressBar.dispose();
                                                            }
                                                   });
                                          }
                                }.start();
                       }
              });
    }
    //创建进度条
    private ProgressBar createProgressBar(Composite parent) {
              ProgressBar progressBar = new ProgressBar(parent, SWT.SMOOTH);
              progressBar.setMinimum(0); // 最小值
              progressBar.setMaximum(100);// 最大值
              return progressBar;
    }
}

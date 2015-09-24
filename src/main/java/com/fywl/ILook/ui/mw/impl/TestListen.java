package com.fywl.ILook.ui.mw.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.ui.components.ScreenPlayBackPanel;
import com.fywl.ILook.ui.components.VideoPlayBackPanel;
import com.fywl.ILook.ui.components.VideoRecorder;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class TestListen {
	
	private Display display = Display.getDefault();

	private Shell shell = new Shell(display, SWT.ON_TOP);
	
	private ScreenPlayBackPanel screenPlayBackPanel;

	private VideoPlayBackPanel faceVideoPlayback;

	private VideoPlayBackPanel otherVideoPlayback;
	
	private VideoRecorder recorder;
	
	
	public TestListen(){
		screenPlayBackPanel = new ScreenPlayBackPanel(shell, SWT.NO_BACKGROUND);

		faceVideoPlayback = new VideoPlayBackPanel(shell,  0);

		otherVideoPlayback = new VideoPlayBackPanel(shell,  1);
		
		recorder = new VideoRecorder(faceVideoPlayback,screenPlayBackPanel, otherVideoPlayback,RecordConfig.get());
	}
	
	
	public void init(){
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
//		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(Constants.Shell_Constant.WIDTH,
				Constants.Shell_Constant.HEIGHT);
		

		
		
		faceVideoPlayback.setBounds(Constants.Face_Constant.LOCATION_X,
				Constants.Face_Constant.LOCATION_Y,
				Constants.Face_Constant.WIDTH, Constants.Face_Constant.HEIGHT);
		faceVideoPlayback.start();
		
		screenPlayBackPanel.addImageRecorder(recorder);
		screenPlayBackPanel.setSize(Constants.SCREEN_Constant.WIDTH,
				Constants.SCREEN_Constant.HEIGHT);
		screenPlayBackPanel.setLocation(Constants.SCREEN_Constant.LOCATION_X,
				Constants.SCREEN_Constant.LOCATION_Y);
//		screenPlayBackPanel.start();
		
		otherVideoPlayback.setBounds(Constants.NOTE_Constant.LOCATION_X,
				Constants.NOTE_Constant.LOCATION_Y,
				Constants.NOTE_Constant.WIDTH, Constants.NOTE_Constant.HEIGHT);
		otherVideoPlayback.start();
		
		
		//注册三个切换快捷键
				JIntellitype.getInstance().registerHotKey(112, JIntellitype.MOD_CONTROL, 112);
				JIntellitype.getInstance().registerHotKey(113, JIntellitype.MOD_CONTROL, 113);
				JIntellitype.getInstance().registerHotKey(114, JIntellitype.MOD_CONTROL, 114);
				//添加对应时间
				JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
					@Override
					public void onHotKey(int arg0) {
						switch (arg0) {
						//脸部摄像头和桌面的切换
						case 112:
							new Thread(){
								public void run(){
									System.out.println(11);
									if (display.isDisposed()) {
										System.out.println(1);
		                                return;
									}

									change();
								}
								
								public void change(){
									display.asyncExec(new Runnable() {
										@Override
										public void run() {
											System.out.println(111);
										}
									});
								}
							}.start();
							break;
						//笔记摄像头和桌面的切换
						case 113:
							break;
						//脸部摄像头和笔记本摄像头的切换
						case 114:
							break;
						default:
							break;
						}
					}
				});
		
		
		
		
		
		shell.open();
		shell.layout();
		
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	
	
	
	
	public static void main(String[] args) {
//		String filename = "E:/ssdgdfg/1442825226039test.mp4";  
		//timebase: 1/1000; coder tb: 1/2000;  format: YUV420P; frame-rate: 100.00; 
		//timebase: 1/1000; coder tb: 1/1000; format: YUV420P; frame-rate: 29.97
//		String filename = "E:/ssdgdfg/1442827797335test.mp4";  
		String filename = "E:/ssdgdfg/1442829888772test.mp4";  
		   // Create a Xuggler container object  
		   IContainer container = IContainer.make();  
		     
		   // Open up the container  
		   if (container.open(filename, IContainer.Type.READ, null) < 0)  
		     throw new IllegalArgumentException("could not open file: " + filename);  
		     
		   // query how many streams the call to open found  
		   int numStreams = container.getNumStreams();  
		   System.out.printf("file \"%s\": %d stream%s; ",  
		       filename,  
		       numStreams,  
		       numStreams == 1 ? "" : "s");  
		   System.out.printf("duration (ms): %s; ", container.getDuration() == Global.NO_PTS ? "unknown" : "" + container.getDuration()/1000);  
		   System.out.printf("start time (ms): %s; ", container.getStartTime() == Global.NO_PTS ? "unknown" : "" + container.getStartTime()/1000);  
		   System.out.printf("file size (bytes): %d; ", container.getFileSize());  
		   System.out.printf("bit rate: %d; ", container.getBitRate());  
		   System.out.printf("\n");  
		  
		   // and iterate through the streams to print their meta data  
		   for(int i = 0; i < numStreams; i++)  
		   {  
		     // Find the stream object  
		     IStream stream = container.getStream(i);  
		     // Get the pre-configured decoder that can decode this stream;  
		     IStreamCoder coder = stream.getStreamCoder();  
		       
		     // and now print out the meta data.  
		     System.out.printf("stream %d: ",    i);  
		     System.out.printf("type: %s; ",     coder.getCodecType());  
		     System.out.printf("codec: %s; ",    coder.getCodecID());  
		     System.out.printf("duration: %s; ", stream.getDuration() == Global.NO_PTS ? "unknown" : "" + stream.getDuration());  
		     System.out.printf("start time: %s; ", container.getStartTime() == Global.NO_PTS ? "unknown" : "" + stream.getStartTime());  
		     System.out.printf("language: %s; ", stream.getLanguage() == null ? "unknown" : stream.getLanguage());  
		     System.out.printf("timebase: %d/%d; ", stream.getTimeBase().getNumerator(), stream.getTimeBase().getDenominator());  
		     System.out.printf("coder tb: %d/%d; ", coder.getTimeBase().getNumerator(), coder.getTimeBase().getDenominator());  
		       
		     if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)  
		     {  
		       System.out.printf("sample rate: %d; ", coder.getSampleRate());  
		       System.out.printf("channels: %d; ",    coder.getChannels());  
		       System.out.printf("format: %s",        coder.getSampleFormat());  
		     } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)  
		     {  
		       System.out.printf("width: %d; ",  coder.getWidth());  
		       System.out.printf("height: %d; ", coder.getHeight());  
		       System.out.printf("format: %s; ", coder.getPixelType());  
		       System.out.println(coder.getFrameRate().getDouble());
		       System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());  
		     }  
		     System.out.printf("\n");  
		   }  
	}
}

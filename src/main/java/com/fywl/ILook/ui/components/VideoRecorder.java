package com.fywl.ILook.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.ui.listener.VideoListener;
import com.fywl.ILook.utils.ImageUtil;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.ferry.IBuffer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.TestAudioSamplesGenerator;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class VideoRecorder implements ImageRecorder {

	private MediaReader reader = null;

	private RecordConfig config;

	public static boolean recordVideo = true;

	private Graphics g = null;

	public VideoRecorder() {

	}

	public VideoRecorder( RecordConfig config) {
		this.config = config;
		reader = new MediaReader(config);
	}

	public void start() {
		/*
		 * 每一次开始录制都有可能切换分辨率
		 */
		if (config.isSingleRecording()) {
			config.setVideoSize(new Dimension(Toolkit.getDefaultToolkit()
					.getScreenSize().width, Toolkit.getDefaultToolkit()
					.getScreenSize().height));
		} else {
			config.setVideoSize(new Dimension(1280, 768));
		}
		//初始化屏幕参数
		ImageUtil.getInstance().initSizeAndHeight();
		g = reader.start();
		
//		Thread t1 = new Thread(new MakerVideo2());
//		t1.start();
//		Thread t2 = new Thread(new MakerVideo3());
//		t2.start();
	}

	public void stop() {
		// recordVideo = false;
		reader.stop();
	}

	public void finish() {
		reader.finish();
	}

	public void pause() {
		reader.changePause();
	}

	@Override
	public void recordScreen(BufferedImage screen) {
		draw(screen);
	}

	// 控制屏幕的输出位
	private void draw(BufferedImage screen) {
		if (g != null && screen != null) {
			if (config.isSingleRecording()) {
				if (config.isScreenRecording()) {
					g.drawImage(screen, 0, 0, config.getVideoSize().width,
							config.getVideoSize().height, null);
				}

			} else {
				if (config.isScreenRecording() || config.isFaceRecording()) {
					g.drawImage(screen, 246, 0,
							Constants.Shell_Constant.DIMENSION[0],
							Constants.Shell_Constant.DIMENSION[1], null);
				}
			}
		}
	}

	public int getTime() {
		return reader.getTime();
	}

	public boolean getPauseFlag() {
		return reader.getPauseFlag();
	}

	public String getVideoName() {
		return reader.videoName;
	}
}

class MediaReader implements Runnable {
	public ExecutorService executor = Executors.newFixedThreadPool(2);

	// public Dimension videoSize = new Dimension(1600, 900);
	public Dimension videoSize = WebcamResolution.HD720.getSize();

	private BufferedImage combined = null;

	private RecordConfig config;

	private Graphics g = null;

	private int i = 0;

	private boolean record = true;
	// 记录暂停状态
	private boolean pauseFlag = true;
	
	private Image img;

	VideoListener otherVideo;

	VideoListener faceVideo;

	VideoListener singleScreenVideo;

	VideoListener doubleScreenVideo;

	Properties properties;

	String videoName;

	public MediaReader() {

	}

	public MediaReader(RecordConfig config) {
		this.config = config;
		File file = new File("user.properties");
		FileInputStream fis;
		try {
			if (file.exists()) {
				properties = new Properties();
				fis = new FileInputStream(file);
				properties.load(fis);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 开始录制 可以多次开始录制，故要初始化所有的参数
	public Graphics start() {
		videoSize = config.getVideoSize();
		combined = new BufferedImage((int) videoSize.getWidth(),
				(int) videoSize.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

		g = combined.getGraphics();
		initParam();
		executor.submit(this);

		File file = new File("user.properties");
		FileInputStream fis;
		try {
			if (file.exists()) {
				properties = new Properties();
				fis = new FileInputStream(file);
				properties.load(fis);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return g;
	}

	// 完成录制 关掉所有资源
	public void stop() {
		record = false;
		g = null;
		combined = null;
	}

	// 每次录制的完成
	public void finish() {
		Webcam.getWebcams().get(RecordConfig.get().getNoteWebcamIndex()).removeWebcamListener(doubleScreenVideo);
		Webcam.getWebcams().get(RecordConfig.get().getNoteWebcamIndex()).removeWebcamListener(singleScreenVideo);
		Webcam.getWebcams().get(RecordConfig.get().getHeadWebcamIndex()).removeWebcamListener(singleScreenVideo);
		Webcam.getWebcams().get(RecordConfig.get().getHeadWebcamIndex()).removeWebcamListener(faceVideo);
		record = false;
		pauseFlag = false;
		g = null;
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
				channels, signed, bigEndian);
		return format;
	}

	// private BufferedImage deepCopy(BufferedImage bi) {
	// ColorModel cm = bi.getColorModel();
	// boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	// WritableRaster raster = bi.copyData(null);
	// return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	// }

	@Override
	public void run() {
		String videoSavePath = Constants.SETUP_Constant.videoPath;
		if (null != properties) {
			String videoPath = properties.getProperty("videoPath");
			if(isNotEmpty(videoPath)){
				videoSavePath = properties.getProperty("videoPath");
			}
		}
		videoName = videoSavePath + File.separator + System.currentTimeMillis()
				+ "test.mp4";
		IMediaWriter writer = ToolFactory.makeWriter(videoName);
		
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264,
				(int) videoSize.getWidth(), (int) videoSize.getHeight());

		AudioFormat format = getAudioFormat();
		writer.addAudioStream(1, 0, 1, (int) format.getSampleRate());

		TargetDataLine line = null;

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		try {
			byte[] audioBuf = new byte[line.getBufferSize() / 5];
			if (!config.isSingleRecording()) {
				Webcam.getWebcams().get(RecordConfig.get().getHeadWebcamIndex()).addWebcamListener(faceVideo);
			}

			line.start();
			BufferedImage biy = null;
			String[] themeArr = {"",""};
			String[] teacherArr = {"",""};
			String[] schoolArr = {"",""};
			String[] infoArr = {"",""};
			if (!config.isSingleRecording()) {
				biy = ImageUtil.getInstance().getBufferedImage(
						"images/separated_y.png");
				if(null != properties){
					String themeStr = properties.getProperty("themeStr");
					themeStr = (null == themeStr ? "":themeStr);
					if(themeStr.length()>12){
						themeArr[0] = themeStr.substring(0, 11);
						themeArr[1] = themeStr.substring(11);
					}else{
						themeArr[0] = themeStr;
					}
					String teacherStr = properties.getProperty("teacherStr");
					teacherStr = (null == teacherStr ? "":teacherStr);
					if(teacherStr.length()>12){
						teacherArr[0] = teacherStr.substring(0, 11);
						teacherArr[1] = teacherStr.substring(11);
					}else{
						teacherArr[0] = teacherStr;
					}
					String schoolStr = properties.getProperty("schoolStr");
					schoolStr = (null == schoolStr ? "":schoolStr);
					if(schoolStr.length()>12){
						schoolArr[0] = schoolStr.substring(0, 11);
						schoolArr[1] = schoolStr.substring(11);
					}else{
						schoolArr[0] = schoolStr;
					}
					String infoStr = properties.getProperty("infoStr");
					infoStr = (null == infoStr ? "":infoStr);
					if(infoStr.length()>12){
						infoArr[0] = infoStr.substring(0, 11);
						infoArr[1] = infoStr.substring(11);
					}else{
						infoArr[0] = infoStr;
					}
				}
			}
			
			String[] water = {"","","10","255","3"};//图片路劲，文字，大小，颜色，位置
			String framerate = null;
			if(null != properties){
				String waterImg = properties.getProperty("water_img");
				if(isNotEmpty(waterImg)){
					water[0] = waterImg;
					File file = new File(waterImg);
					if(file.exists()){
						img = ImageIO.read(file);
					}
				}
				String waterText = properties.getProperty("water_text");
				if(isNotEmpty(waterText)){
					water[1] = waterText;
				}
				String water_text_size = properties.getProperty("water_text_size");
				if(isNotEmpty(water_text_size)){
					water[2] = water_text_size;
				}
				String water_text_color = properties.getProperty("water_text_color");
				if(isNotEmpty(water_text_color)){
					water[3] = water_text_color;
				}
				String water_location = properties.getProperty("water_location");
				if(isNotEmpty(water_location)){
					water[4] = water_location;
				}
				framerate = properties.getProperty("frameRate");
			}
			
			int frameCount = (isNotEmpty(framerate) ? Integer.parseInt(framerate) : 100000);
			while (record) {
				while (pauseFlag) {
					createVideo(writer, audioBuf, format, line,biy,themeArr,teacherArr,schoolArr,infoArr,water,img,frameCount);
					// 录制中直接结束 跳出循环
					if (!record) {
						break;
					}
					i++;
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			line.close();
			writer.close();
		}

	}

	private void createVideo(IMediaWriter writer, byte[] audioBuf,
			AudioFormat format, TargetDataLine line, Image biy,String[] themeArr,String[] teacherArr,String[] schoolArr,String[] infoArr,String[] water,Image img,int frameCount) {
		changeVideo();

		if (!config.isSingleRecording()) {
			g.drawImage(biy, 240, 0, 5, 768, null);
			g.drawString("主题:", 30, 300);
				g.drawString(themeArr[0], 80, 300);
				g.drawString(themeArr[1], 80, 325);
			g.drawString("主讲:", 30, 350);
				g.drawString(teacherArr[0], 80, 350);
				g.drawString(teacherArr[1], 80, 375);
			g.drawString("学校:", 30, 400);
				g.drawString(schoolArr[0], 80, 400);
				g.drawString(schoolArr[1], 80, 425);
			g.drawString("备注:", 30, 450);
				g.drawString(infoArr[0], 80, 450);
				g.drawString(infoArr[1], 80, 475);
		}
		
//		IConverter converter = ConverterFactory.createConverter(combined,
//				IPixelFormat.Type.YUV420P);
//		IVideoPicture frame = converter.toPicture(combined,
//				line.getMicrosecondPosition());
//		frame.setKeyFrame(i == 0);
//		frame.setQuality(0);
		

		int nBytesRead = line.read(audioBuf, 0, audioBuf.length);
		IBuffer iBuf = IBuffer.make(null, audioBuf, 0, nBytesRead);
		IAudioSamples smp = IAudioSamples.make(iBuf, 1,
				IAudioSamples.Format.FMT_S16);
		long numSample = nBytesRead / smp.getSampleSize();
		smp.put(audioBuf, 0, 0, nBytesRead);
		smp.setComplete(true, numSample, (int) format.getSampleRate(), 1,
				IAudioSamples.Format.FMT_S16, line.getMicrosecondPosition());
//		while(t1<line.getMicrosecondPosition()){
////			if(null != g){
//////				g.drawImage(img, 600, 600, new Color(255,255,0,0), null);
////			}
//			drawWater(g, water, img);
//			writer.encodeVideo(0, combined,t1,Global.DEFAULT_TIME_UNIT);
//			t1 += frameCount;
//		}
//		writer.encodeVideo(0, frame);
		writer.encodeVideo(0, combined, t1, Global.DEFAULT_TIME_UNIT);
		t1 += frameCount;
		writer.encodeAudio(1, smp);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private long t1 = 0;
	void changePause() {
		pauseFlag = !pauseFlag;
	}

	public boolean getPauseFlag() {
		return pauseFlag;
	}

	// // 切换功能
	// void change() {
	// changeCount++;
	// changeFlag = true;
	// }

	// 实现画面的切
	void changeVideo() {
		if (config.isChangeFlag()) {
			int headIndex = RecordConfig.get().getHeadWebcamIndex();
			int noteIndex = RecordConfig.get().getNoteWebcamIndex();
//			Webcam webcam = Webcam.getWebcams().get(noteIndex);
//			WebcamListener[] w = webcam.getWebcamListeners();
//			System.out.println(w);
//			System.out.println(webcam.getWebcamListenersCount());
//			if(w.length>0){
//				System.out.println(webcam.removeWebcamListener(w[0]));
//			}
//			System.out.println(webcam.getWebcamListenersCount());
			// 单瓶切换
			if (config.isSingleRecording()) {
				if (config.isFaceRecording()) {
//					Webcam.getWebcams().get(1)
//							.removeWebcamListener(singleScreenVideo);
//					Webcam.getWebcams().get(0)
//							.addWebcamListener(singleScreenVideo);
					Webcam.getWebcams().get(noteIndex).removeWebcamListener(singleScreenVideo);
					Webcam.getWebcams().get(headIndex).addWebcamListener(singleScreenVideo);
				} else if (config.isNoteRecording()) {
//					Webcam.getWebcams().get(0)
//							.removeWebcamListener(singleScreenVideo);
//					Webcam.getWebcams().get(1)
//							.addWebcamListener(singleScreenVideo);
					Webcam.getWebcams().get(headIndex).removeWebcamListener(singleScreenVideo);
					Webcam.getWebcams().get(noteIndex).addWebcamListener(singleScreenVideo);
				} else if (config.isScreenRecording()) {
//					Webcam.getWebcams().get(0)
//							.removeWebcamListener(singleScreenVideo);
//					Webcam.getWebcams().get(1)
//							.removeWebcamListener(singleScreenVideo);
					Webcam.getWebcams().get(headIndex).removeWebcamListener(singleScreenVideo);
					Webcam.getWebcams().get(noteIndex).removeWebcamListener(singleScreenVideo);
				}
			} else {
				// 双屏切换
				if (config.isNoteRecording()) {
//					Webcam.getWebcams().get(1)
//							.addWebcamListener(doubleScreenVideo);
					Webcam.getWebcams().get(noteIndex).addWebcamListener(doubleScreenVideo);
				} else {
//					Webcam.getWebcams().get(1)
//							.removeWebcamListener(doubleScreenVideo);
					Webcam.getWebcams().get(noteIndex).removeWebcamListener(doubleScreenVideo);
				}
			}
			// changeFlag = false;
			config.setChangeFlag(false);
		}
	}

	// 初始化状态参数
	void initParam() {
		faceVideo = new VideoListener(g, 0, 0, 320, 240);
//		otherVideo = new VideoListener(g, 0, 210, 240, 180);
		singleScreenVideo = new VideoListener(g, 0, 0,
				config.getVideoSize().width, config.getVideoSize().height);
		doubleScreenVideo = new VideoListener(g, 325, 0,
				800, 600);
		// 录制状态
		record = true;
		// 暂停状态
		pauseFlag = true;
		// 初始化时间
		i = 0;
		t1 = 0;
		config.setChangeFlag(true);
	}

	int getTime() {
		return i;
	}
	
	
	private void drawWater(Graphics g,String[] water,Image img){
		if(null != g){
//			Graphics2D g2 = (Graphics2D) g;
//			Composite composite =  g2.getComposite();
			//判断图片
			if(null != img){
				BufferedImage bi = (BufferedImage) img;
				int w=0,h=0;
				switch (Integer.parseInt(water[4])) {
				case 2:
					w = videoSize.width-bi.getWidth();
					h = 0;
					break;
				case 3:
					w = videoSize.width - bi.getWidth();
					h = videoSize.height - bi.getHeight();
					break;
				case 4:
					w = 0;
					h = videoSize.height - bi.getHeight();
					break;
				default:
					break;
				}
//				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.1f));
				g.drawImage(img, w,h, new Color(255,255,0,0), null);
//				g2.setComposite(composite);
			}
			Font f = g.getFont();
			Color c = g.getColor();
			//文字
			if(!water[1].isEmpty()){
				Font font1=new Font("宋体",Font.BOLD,Integer.parseInt(water[2]));
				g.setFont(font1);
				Color cl = new Color(256);
				g.setColor(cl);
				int w=0,h=Integer.parseInt(water[2]);
				switch (Integer.parseInt(water[4])) {
				case 2:
					w = videoSize.width-Integer.parseInt(water[2])*(water[1].length())-10;
					break;
				case 3:
					w = videoSize.width - Integer.parseInt(water[2])*(water[1].length())-10;
					h = videoSize.height;
					break;
				case 4:
					w = 0;
					h = videoSize.height;
					break;
				default:
					break;
				}
				g.drawString(water[1], w, h);
			}
			
			g.setColor(c);
			g.setFont(f);
		}
	}
	
	
	private boolean isNotEmpty(String str){
		if(null == str){
			return false;
		}else if("".equals(str.trim())){
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void customAudioVideoStream()
	  {
	    if (!IVideoResampler.isSupported(
	        IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
	      return;

	    // video parameters

	    int videoStreamIndex = 0;
	    int videoStreamId = 0;
	    long deltaTime = 15000;
	    int w = 1600;
	    int h = 900;

	    // audio parameters

	    int audioStreamIndex = 1;
	    int audioStreamId = 0;
	    int channelCount = 2;
//	    int sampleRate = 16000;//576
	    //time base=1/90000;frame rate=0/0;sample rate=44100;channels=2;];framerate:0/0;timebase:1/90000;direction:OUTBOUND;]
	    int sampleRate = 44100;//576

	    // create the writer
	    String videoSavePath = Constants.SETUP_Constant.videoPath;
		if (null != properties) {
			videoSavePath = properties.getProperty("videoPath");
		}
		videoName = videoSavePath + File.separator + System.currentTimeMillis()
				+ "test.mp4";
		IMediaWriter writer = ToolFactory.makeWriter(videoName);

	    // add the video stream
	    ICodec videoCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_H264);
	    writer.addVideoStream(videoStreamIndex, videoStreamId, videoCodec, w, h);

	    // add the audio stream

	    ICodec audioCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_MP3);
	    IContainer container = writer.getContainer();
	    writer.addAudioStream(audioStreamIndex, audioStreamId,
	  	      audioCodec, channelCount, sampleRate);
	    IStream stream = container.getStream(audioStreamIndex);
	    int sampleCount = stream.getStreamCoder().getDefaultAudioFrameSize();
	    // create a place for audio samples and video pictures

	    IAudioSamples samples = IAudioSamples.make(sampleCount, channelCount);
	    IVideoPicture picture = IVideoPicture.make(IPixelFormat.Type.YUV420P, w, h);

	    // create the tone generator

	    TestAudioSamplesGenerator generator = new TestAudioSamplesGenerator();
	    generator.prepare(channelCount, sampleRate);

	    // make some media

	    long videoTime = 0;
	    long audioTime = 0;
	    long totalSamples = 0;
	    long totalSeconds = 6;

	    // the goal is to get 6 seconds of audio and video, in this case
	    // driven by audio, but kicking out a video frame at about the right
	    // time
	    int i=0;
	    while (totalSamples < sampleRate * totalSeconds)
	    {
	      // comput the time based on the number of samples

	      audioTime = (totalSamples * 900 * 100) / sampleRate;

	      // if the audioTime i>= videoTime then it's time for a video frame

	      if (audioTime <= videoTime)
	      {
	        BufferedImage image = new BufferedImage(w, h, 
	          BufferedImage.TYPE_3BYTE_BGR);
	      
	        Graphics2D g = image.createGraphics();
	        g.setRenderingHint(
	          RenderingHints.KEY_ANTIALIASING,
	          RenderingHints.VALUE_ANTIALIAS_ON);

	        double theta = videoTime / 1000000d;
	        g.setColor(Color.RED);
	        g.rotate(theta, w / 2, h / 2);
	        
	        g.fillRect(50, 50, 100, 100);
	        
	        picture.setPts(videoTime);
	        writer.encodeVideo(videoStreamIndex, image, videoTime,
	            Global.DEFAULT_TIME_UNIT);
	      
	        videoTime += deltaTime;
	      }

	      // generate audio
	      
	      generator.fillNextSamples(samples, sampleCount);
	      writer.encodeAudio(audioStreamIndex, samples);
	      totalSamples += samples.getNumSamples();
	    }

	    // close the writer

	    writer.close();
	  }
}












/**
 * 缓存image的线程
 * @author Administrator
 *
 */
class CacheImage implements Runnable{
	
	public ExecutorService executor = Executors.newFixedThreadPool(2);
	//存储image的队列
	private BlockingQueue<BufferedImage> bq;
	
	private RecordConfig config;

	private Graphics g = null;
	
	private BufferedImage combined = null;
	
	public CacheImage(BlockingQueue<BufferedImage> bq){
		this.bq = bq;
	}
	

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			if(null != combined){
				try {
					bq.put(deepCopy(combined));
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public Graphics start() {
		combined = new BufferedImage((int) 1600,
				(int) 900, BufferedImage.TYPE_3BYTE_BGR);
//		executor.submit(this);
		g = combined.getGraphics();
		Webcam.getWebcams().get(1)
		.addWebcamListener(new VideoListener(g, 0, 0,
				1200, 720));
		return g;
	}
	
	//clone
	 private BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	 }
	
}


/**
 * 合成视频的线程
 * @author Administrator
 *
 */
class MediaWriter implements Runnable{
	
	public ExecutorService executor = Executors.newFixedThreadPool(2);
	
	private BlockingQueue<BufferedImage> bq;
	
	public MediaWriter(BlockingQueue<BufferedImage> bq){
		this.bq = bq;
	}
	
	public void start() {
		executor.submit(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int videoStreamIndex = 0;
	    int videoStreamId = 0;
	    long deltaTime = 30000;
	    int w = 1600;
	    int h = 900;

	    // audio parameters

	    int audioStreamIndex = 1;
	    int audioStreamId = 0;
	    int channelCount = 2;
//	    int sampleRate = 16000;//576
	    //time base=1/90000;frame rate=0/0;sample rate=44100;channels=2;];framerate:0/0;timebase:1/90000;direction:OUTBOUND;]
	    int sampleRate = 44100;//576

	    // create the writer
		String videoName = "E:/ssdgdfg" + File.separator + System.currentTimeMillis()
				+ "test.mp4";
		IMediaWriter writer = ToolFactory.makeWriter(videoName);

	    // add the video stream
	    ICodec videoCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_H264);
	    writer.addVideoStream(videoStreamIndex, videoStreamId, videoCodec, w, h);

	    // add the audio stream

	    ICodec audioCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_MP3);
	    IContainer container = writer.getContainer();
	    writer.addAudioStream(audioStreamIndex, audioStreamId,
	  	      audioCodec, channelCount, sampleRate);
	    IStream stream = container.getStream(audioStreamIndex);
	    int sampleCount = stream.getStreamCoder().getDefaultAudioFrameSize();
	    // create a place for audio samples and video pictures

	    IAudioSamples samples = IAudioSamples.make(sampleCount, channelCount);
	    IVideoPicture picture = IVideoPicture.make(IPixelFormat.Type.YUV420P, w, h);

	    // create the tone generator

	    TestAudioSamplesGenerator generator = new TestAudioSamplesGenerator();
	    generator.prepare(channelCount, sampleRate);

	    // make some media

	    long videoTime = 0;
	    long audioTime = 0;
	    long totalSamples = 0;
	    long totalSeconds = 6;

	    // the goal is to get 6 seconds of audio and video, in this case
	    // driven by audio, but kicking out a video frame at about the right
	    // time
	    int i=0;
	    boolean flag = true;
			while(flag){
				
		    while (!bq.isEmpty())
		    {
		      // comput the time based on the number of samples

		      audioTime = (totalSamples * 900 * 100) / sampleRate;

		      // if the audioTime i>= videoTime then it's time for a video frame

		      if (audioTime <= videoTime)
		      {
		        
		        picture.setPts(videoTime);
		        writer.encodeVideo(videoStreamIndex, bq.poll(), videoTime,
		            Global.DEFAULT_TIME_UNIT);
		      
		        videoTime += deltaTime;
		      }

		      // generate audio
		      
		      generator.fillNextSamples(samples, sampleCount);
		      writer.encodeAudio(audioStreamIndex, samples);
		      totalSamples += samples.getNumSamples();
		    }
		    
		    if(3600 == i)
		    	break;
			}

		    // close the writer

		    writer.close();
		    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}





class MakerVideo2 implements Runnable{

	@Override
	public void run() {
		File file = new File("test.mp4");

		IMediaWriter writer = ToolFactory.makeWriter(file.getName());
		Dimension size = WebcamResolution.VGA.getSize();

		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);

		Webcam webcam = Webcam.getWebcams().get(0);
//		webcam.setViewSize(size);
		webcam.open(true);

		long start = System.currentTimeMillis();
		BufferedImage combined = new BufferedImage((int) 1200,
				(int) 800, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = combined.getGraphics();
		for (int i = 0; i < 500; i++) {

			System.out.println("Capture frame " + i);
BufferedImage bufferedImage = webcam.getImage();
g.drawImage(bufferedImage, 0, 0, 320, 240, null);
			BufferedImage image = ConverterFactory.convertToType(bufferedImage, BufferedImage.TYPE_3BYTE_BGR);
			IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

			IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
			frame.setKeyFrame(i == 0);
			frame.setQuality(0);

			writer.encodeVideo(0, frame);

			// 10 FPS
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		writer.close();

		System.out.println("Video recorded in file: " + file.getAbsolutePath());
	}
	
}

class MakerVideo3 implements Runnable{

	@Override
	public void run() {
		File file = new File("test1.mp4");

		IMediaWriter writer = ToolFactory.makeWriter(file.getName());
		Dimension size = WebcamResolution.VGA.getSize();

		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);

		Webcam webcam = Webcam.getWebcams().get(1);
		webcam.setViewSize(size);
		webcam.open(true);

		long start = System.currentTimeMillis();
		BufferedImage combined = new BufferedImage((int) 1200,
				(int) 800, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = combined.getGraphics();
		for (int i = 0; i < 500; i++) {

			System.out.println("Capture frame " + i);
			BufferedImage bufferedImage = webcam.getImage();
			g.drawImage(bufferedImage, 0, 0, 320, 240, null);

			BufferedImage image = ConverterFactory.convertToType(bufferedImage, BufferedImage.TYPE_3BYTE_BGR);
			IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

			IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
			frame.setKeyFrame(i == 0);
			frame.setQuality(0);

			writer.encodeVideo(0, frame);

			// 10 FPS
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		writer.close();

		System.out.println("Video recorded in file: " + file.getAbsolutePath());
	}
	
}
package com.fywl.ILook.ui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.ferry.IBuffer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class VideoRecorder implements ImageRecorder {

	private ScreenPlayBackPanel screenPlayBackPanel = null;

	private VideoPlayBackPanel otherVideoPlayback = null;

	private MediaReader reader = null;
	
	private RecordConfig config;

	public static boolean recordVideo = true;

	private Graphics g = null;

	public VideoRecorder() {

	}

	public VideoRecorder(ScreenPlayBackPanel screenPlayBackPanel,
			VideoPlayBackPanel otherVideoPlayback,RecordConfig config) {
		this.config = config;
		reader = new MediaReader();
		this.screenPlayBackPanel = screenPlayBackPanel;
		this.otherVideoPlayback = otherVideoPlayback;
	}

	public void start() {
		g = reader.start();
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

	public void changeSource() {
		changeHuaMian();
		recordVideo = !recordVideo;
		reader.change();
	}

	// 切换画面
	private void changeHuaMian() {
		System.out.println(recordVideo);
		if (recordVideo) {
			screenPlayBackPanel.setSize(Constants.NOTE_Constant.WIDTH,
					Constants.NOTE_Constant.HEIGHT);
			screenPlayBackPanel.setLocation(Constants.NOTE_Constant.LOCATION_X,
					Constants.NOTE_Constant.LOCATION_Y);

			otherVideoPlayback.setBounds(Constants.SCREEN_Constant.LOCATION_X,
					Constants.SCREEN_Constant.LOCATION_Y,
					Constants.SCREEN_Constant.WIDTH,
					Constants.SCREEN_Constant.HEIGHT);
		} else {
			screenPlayBackPanel.setSize(Constants.SCREEN_Constant.WIDTH,
					Constants.SCREEN_Constant.HEIGHT);
			screenPlayBackPanel.setLocation(
					Constants.SCREEN_Constant.LOCATION_X,
					Constants.SCREEN_Constant.LOCATION_Y);

			otherVideoPlayback.setBounds(Constants.NOTE_Constant.LOCATION_X,
					Constants.NOTE_Constant.LOCATION_Y,
					Constants.NOTE_Constant.WIDTH,
					Constants.NOTE_Constant.HEIGHT);
		}
	}

	@Override
	public void recordScreen(BufferedImage screen) {
		draw(screen);
	}

	// 控制屏幕的输出位
	private void draw(BufferedImage screen) {
		if (g != null && screen != null) {
			if (recordVideo) {
				g.drawImage(screen, 246, 0, 1600, 900, null);
//			} else {
//				g.drawImage(screen, 0, 240, 240, 180, null);
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

//	public Dimension videoSize = new Dimension(1600, 900);
	public Dimension videoSize = WebcamResolution.HD720.getSize();

	private BufferedImage combined = null;
	
	private RecordConfig config;

	private Graphics g = null;

	private int i = 0;

	private boolean record = true;
	// 记录暂停状态
	private boolean pauseFlag = true;

	// 记录切换按钮
	private Integer changeCount = 1;
	// 是否改变了切换按钮的状
	private Boolean changeFlag = false;

	VideoListener secVideo;

	VideoListener secVideo1;

	Properties properties;

	String videoName;
	
	public MediaReader(){
		
	}

	public MediaReader(RecordConfig config) {
		this.config = config;
		combined = new BufferedImage((int) videoSize.getWidth(),
				(int) videoSize.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
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
		g = combined.getGraphics();
		executor.submit(this);

		initParam();

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
		Webcam.getWebcams().get(1).removeWebcamListener(secVideo);
		Webcam.getWebcams().get(1).removeWebcamListener(secVideo1);
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
			videoSavePath = properties.getProperty("videoPath");
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

			VideoListener faceVideo = new VideoListener(g, 0, 0, 240, 180);
			Webcam.getWebcams().get(0).addWebcamListener(faceVideo);

			// VideoListener secVideo = new VideoListener(g, 0, 240, 320, 240);
			// // Webcam.getWebcams().get(1).addWebcamListener(secVideo);
			//
			// VideoListener secVideo1 = new VideoListener(g, 320, 90, 960,
			// 540);
			// Webcam.getWebcams().get(1).addWebcamListener(secVideo1);
			line.start();

			while (record) {
				while (pauseFlag) {
					createVideo(writer, audioBuf, format, secVideo, secVideo1,
							line);
					// 录制中直接结束 跳出循环
					if (!record) {
						break;
					}
					i++;
					// if(i == 200){
					// record = false;
					// break;
					// }
				}
				System.out.println("······暂停中");
				System.out.println("---" + i);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			line.close();
			writer.close();
			System.out.println("关闭流");
		}

	}

	private void createVideo(IMediaWriter writer, byte[] audioBuf,
			AudioFormat format, VideoListener secVideo,
			VideoListener secVideo1, TargetDataLine line) {
		changeVideo(secVideo, secVideo1);
//		g.setFont(new Font(null, Font.BOLD, 15));
//		g.drawString(
//				"这是课件这是课件这是",
//				0, 500);
		BufferedImage bix = ImageUtil.getInstance().getBufferedImage("images/separated_x.png");
		BufferedImage biy = ImageUtil.getInstance().getBufferedImage("images/separated_y.png");
		g.drawImage(bix, 0, 181, 240, 1, null);
		g.drawImage(biy, 241, 0, 5, 768, null);
		IConverter converter = ConverterFactory.createConverter(combined,
				IPixelFormat.Type.YUV420P);
		IVideoPicture frame = converter.toPicture(combined,
				line.getMicrosecondPosition());
		frame.setKeyFrame(i == 0);
		frame.setQuality(0);

		int nBytesRead = line.read(audioBuf, 0, audioBuf.length);
		IBuffer iBuf = IBuffer.make(null, audioBuf, 0, nBytesRead);
		IAudioSamples smp = IAudioSamples.make(iBuf, 1,
				IAudioSamples.Format.FMT_S16);
		long numSample = nBytesRead / smp.getSampleSize();
		smp.put(audioBuf, 0, 0, nBytesRead);
		smp.setComplete(true, numSample, (int) format.getSampleRate(), 1,
				IAudioSamples.Format.FMT_S16, line.getMicrosecondPosition());

		writer.encodeVideo(0, frame);
		writer.encodeAudio(1, smp);

		// new Thread(new Runnable() {
		// public void run() {
		// System.out.println("进入run");
		// Display.getDefault().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		// System.out.println("终于执行了");
		// infoLabel.setText("系统将在:" + i + "秒后自动重启");
		// }
		// });
		// }
		// }).start();
		// infoLabel.getParent().layout();
	}

	void changePause() {
		pauseFlag = !pauseFlag;
	}

	public boolean getPauseFlag() {
		return pauseFlag;
	}

	// 切换功能
	void change() {
		changeCount++;
		changeFlag = true;
	}

	// 实现画面的切
	private void changeVideo(VideoListener secVideo, VideoListener secVideo1) {
		if (changeFlag) {
			if (changeCount % 2 == 0) {
				Webcam.getWebcams().get(1).removeWebcamListener(secVideo);
				Webcam.getWebcams().get(1).addWebcamListener(secVideo1);
//			} else {
//				Webcam.getWebcams().get(1).removeWebcamListener(secVideo1);
//				Webcam.getWebcams().get(1).addWebcamListener(secVideo);
			}
			changeFlag = false;
		}
	}

	// 初始化状态参数
	private void initParam() {
		secVideo = new VideoListener(g, 0, 210, 240, 180);

		secVideo1 = new VideoListener(g, 240, 0, 1024, 768);
		// 录制状态
		record = true;
		// 暂停状态
		pauseFlag = true;
		// 初始化时间
		i = 0;
		// // 切换按钮的次数
		// changeCount = 1;
		// // 切换按钮的状态
		changeFlag = true;
	}

	int getTime() {
		return i;
	}
}

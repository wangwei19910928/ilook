package com.fywl.ILook;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fywl.ILook.ui.mw.impl.LoginMainWindow;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.xuggle.xuggler.video.ConverterFactory;





public class Server {

	public static void main(String[] args) {
//		InfoBean ib = new InfoBean();
//		ib.setName("张三疯");
//		ib.setSchool("北京市第三中学");
//		ib.setTrainAge(3+"");
//		ib.setType("体育");
//		
//		RecordConfig rc = RecordConfig.get();
//		rc.setSingleRecording(false);
//		rc.setVideoSize(Toolkit.getDefaultToolkit().getScreenSize());
//		rc.setIb(ib);
//		new FunctionMainWindow(rc);
		
		new LoginMainWindow();
		
//		
//		IMediaWriter writer = ToolFactory.makeWriter("test.mp4");
//		Dimension size = WebcamResolution.VGA.getSize();
//
//		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 1280, 720);
//
//		Webcam webcam = Webcam.getDefault();
//		webcam.setViewSize(size);
//		webcam.open(true);
//		
//		Webcam webcam1 = Webcam.getWebcams().get(1);
//		webcam1.setViewSize(size);
//		webcam1.open(true);
//		
//		
//		BufferedImage combined = new BufferedImage((int) 1200,
//				(int) 800, BufferedImage.TYPE_3BYTE_BGR);
//		Graphics g = combined.getGraphics();
//		Webcam.getWebcams().get(1).addWebcamListener(new VideoListener1(g, 245, 0,
//				1024, 768));
//		VideoListener1 faceVideo = new VideoListener1(g, 0, 0, 240, 180);
//		Webcam.getWebcams().get(0).addWebcamListener(faceVideo);
//		AudioFormat format = getAudioFormat();
//		writer.addAudioStream(1, 0, 1, (int) format.getSampleRate());
//
//		TargetDataLine line = null;
//
//		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
//
//		try {
//			line = (TargetDataLine) AudioSystem.getLine(info);
//			line.open(format);
//			line.start();
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//		}
//		
//		long start = System.currentTimeMillis();
//
//		for (int i = 0; i < 500; i++) {
//
//			System.out.println("Capture frame " + i);
//
//			//BufferedImage image = ConverterFactory.convertToType(combined, BufferedImage.TYPE_3BYTE_BGR);
//			IConverter converter = ConverterFactory.createConverter(combined,
//					IPixelFormat.Type.YUV420P);
//			IVideoPicture frame = converter.toPicture(combined,
//					line.getMicrosecondPosition());
//			frame.setKeyFrame(i == 0);
//			frame.setQuality(0);
//			
//			byte[] audioBuf = new byte[line.getBufferSize() / 5];
//			int nBytesRead = line.read(audioBuf, 0, audioBuf.length);
//			IBuffer iBuf = IBuffer.make(null, audioBuf, 0, nBytesRead);
//			IAudioSamples smp = IAudioSamples.make(iBuf, 1,
//					IAudioSamples.Format.FMT_S16);
//			long numSample = nBytesRead / smp.getSampleSize();
//			smp.put(audioBuf, 0, 0, nBytesRead);
//			smp.setComplete(true, numSample, (int) format.getSampleRate(), 1,
//					IAudioSamples.Format.FMT_S16, line.getMicrosecondPosition());
//
////			writer.encodeVideo(0, frame);
//			while(t1<line.getMicrosecondPosition()){
//				writer.encodeVideo(0, combined,t1,Global.DEFAULT_TIME_UNIT);
//				t1 += 50000;
//			}
////			writer.encodeVideo(0, combined,t1,Global.DEFAULT_TIME_UNIT);
//			writer.encodeAudio(1, smp);
//			// 10 FPS
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		writer.close();
//
//		System.out.println("Video recorded in file: " );
//	}
//	
//	static int t1 = 0;
//	private static AudioFormat getAudioFormat() {
//		float sampleRate = 16000;
//		int sampleSizeInBits = 8;
//		int channels = 2;
//		boolean signed = true;
//		boolean bigEndian = true;
//		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
//				channels, signed, bigEndian);
//		return format;
	}

}

class VideoListener1 implements WebcamListener {

	private Graphics g;

	private int x;

	private int y;

	private int width;

	private int height;
	
	private long start = System.currentTimeMillis();

	public VideoListener1(Graphics g, int x, int y, int width, int height) {
		this.g = g;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void change(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void webcamOpen(WebcamEvent we) {

	}

	@Override
	public void webcamClosed(WebcamEvent we) {

	}

	@Override
	public void webcamDisposed(WebcamEvent we) {

	}

	@Override
	public void webcamImageObtained(WebcamEvent we) {
		if (g != null) {
			BufferedImage image = ConverterFactory.convertToType(we.getImage(), BufferedImage.TYPE_3BYTE_BGR);
			g.drawImage(image, x, y, width, height, null);
//			g.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
//			Graphics2D g2d = (Graphics2D)g;  
//	        g2d.rotate(180,width/2,height/2);  
//	        g2d.drawImage(img,100,100,300,300,null);
//	        g2d.drawImage(image, x, y, width, height, null);
		}
	}

}

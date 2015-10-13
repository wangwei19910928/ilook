package com.fywl.ILook;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import com.fywl.ILook.bean.Constants;
import com.fywl.ILook.bean.InfoBean;
import com.fywl.ILook.bean.RecordConfig;
import com.fywl.ILook.ui.mw.impl.FunctionMainWindow;
import com.github.sarxos.webcam.Webcam;
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





public class Server {

	public static void main(String[] args) {
		InfoBean ib = new InfoBean();
		ib.setName("张三疯");
		ib.setSchool("北京市第三中学");
		ib.setTrainAge(3+"");
		ib.setType("体育");
		
		RecordConfig rc = RecordConfig.get();
		rc.setSingleRecording(true);
		rc.setVideoSize(Toolkit.getDefaultToolkit().getScreenSize());
		new FunctionMainWindow(ib,rc);
		
		
		new Server().customAudioVideoStream();

	}
	
	public static void main1(String[] args) {

		long t1 = 0;
		long t2 = 0;

		int p = 10;
		int r = 5;

		Webcam webcam = Webcam.getDefault();

		for (int k = 0; k < p; k++) {

			webcam.open();
			webcam.getImage();

			t1 = System.currentTimeMillis();
			for (int i = 0; ++i <= r; webcam.getImage()) {
			}
			t2 = System.currentTimeMillis();

			System.out.println("FPS " + k + ": " + (1000 * r / (t2 - t1 + 1)));

			webcam.close();
		}

	}
	
	public void customAudioVideoStream()
	  {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//要捕捉的屏幕显示范围，下面以全屏示例说明
		Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
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
		String videoName = videoSavePath + File.separator + System.currentTimeMillis()
				+ "test.mp4";
		System.out.println(videoName);
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
System.out.println(stream.toString());
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
//	        BufferedImage image = new BufferedImage(w, h, 
//	          BufferedImage.TYPE_3BYTE_BGR);
	      
//	        Graphics2D g = image.createGraphics();
//	        g.setRenderingHint(
//	          RenderingHints.KEY_ANTIALIASING,
//	          RenderingHints.VALUE_ANTIALIAS_ON);
//
//	        double theta = videoTime / 1000000d;
//	        g.setColor(Color.RED);
//	        g.rotate(theta, w / 2, h / 2);
//	        
//	        g.fillRect(50, 50, 100, 100);
	        BufferedImage image = ConverterFactory.convertToType(robot.createScreenCapture(rect), BufferedImage.TYPE_3BYTE_BGR);
	        System.out.println(ConverterFactory.findDescriptor(image));
	        picture.setPts(videoTime);
	        writer.encodeVideo(videoStreamIndex, image, videoTime,
	            Global.DEFAULT_TIME_UNIT);
	      
	        videoTime += deltaTime;
	        System.out.println("-----------------");
	      }
	      System.out.println("----");

	      // generate audio
	      
	      generator.fillNextSamples(samples, sampleCount);
	      writer.encodeAudio(audioStreamIndex, samples);
	      totalSamples += samples.getNumSamples();
	      
	      System.out.println(i++);
	    }

	    // close the writer

	    writer.close();
	  }
}


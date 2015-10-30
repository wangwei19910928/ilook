package com.fywl.ILook.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.xuggle.ferry.IBuffer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.TestAudioSamplesGenerator;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class ImageMaker {
	
	 public static void main(String[] args) {
		 Webcam webcam = Webcam.getWebcams().get(0);
			webcam.setViewSize(new Dimension(640, 480));
			webcam.open();
			Webcam webcam1 = Webcam.getWebcams().get(1);
			webcam1.setViewSize(new Dimension(640, 480));
			webcam1.open();
			final ImageThread it1 = new ImageThread(webcam,"E:/4");
			final ImageThread it2 = new ImageThread(webcam,"E:/5");
			final SoundThread st = new SoundThread();
			
			JFrame f = new JFrame();
			JButton jb = new JButton("开始");
			jb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(1);
					st.start();
					it1.start();
					it2.start();
				}
			});
			JButton jb1 = new JButton("结束");
			jb1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					it1.stop();
					it2.stop();
					st.stop();
					System.out.println("结束");
					
				}
			});
			 f.add(jb);
			 f.add(jb1);
			   f.setLayout(new FlowLayout());
			   f.setSize(480, 320);
			//   f.setDefaultCloseOperation(EXIT_ON_CLOSE);
			   f.setLocationRelativeTo(null);
			   f.setVisible(true);
		}
	
//	public static void main(String[] args) {
//		Webcam webcam = Webcam.getWebcams().get(0);
//		webcam.setViewSize(new Dimension(640, 480));
//		webcam.open();
//		Webcam webcam1 = Webcam.getWebcams().get(1);
//		webcam1.setViewSize(new Dimension(640, 480));
//		webcam1.open();
//		ImageThread it1 = new ImageThread(webcam,webcam1, "E:/4","E:/5");
//		SoundThread st = new SoundThread();
//		st.start();
//		it1.start();
//		try {
//			Thread.sleep(50000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		it1.stop();
//		
//		st.stop();
//		
////		DecodeAndPlayAudio da = new DecodeAndPlayAudio();
////		da.decode("E:/eclipse-jee-juno-SR1-win32/eclipse-jee-juno-SR1-win32/workspace/ILook/output.mp3","E:/4", "E:/5");
////		da.customAudioVideoStream();
//	}
}

class ImageThread implements Runnable{
	private Webcam webcam;
	
	private String savePath;
	
	private boolean workingFlag = true;
	
	public ImageThread(Webcam webcam,String savePath){
		this.webcam = webcam;
		this.savePath = savePath;
	}
	
	@Override
	public void run() {
		File webcam1Folder = new File(savePath);
		if(webcam1Folder.exists()){
			webcam1Folder.deleteOnExit();
		}
		webcam1Folder.mkdirs();
		while(workingFlag){
			 //保存图片到本地  
            File webcam1File = new File(savePath + File.separator + System.currentTimeMillis() + ".jpg");  
              
            try {
				ImageIO.write(webcam.getImage(), "jpg", webcam1File);
//				Thread.sleep(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	public void start(){
		executorService.execute(this);
	}
	
	public void stop(){
		workingFlag = false;
		executorService.shutdown();
	}
	
}


class ImageThread2 implements Runnable{
	private Webcam webcam1;
	private Webcam webcam2;
	
	private String savePath1;
	private String savePath2;
	
	private boolean workingFlag = true;
	
	public ImageThread2(Webcam webcam1,Webcam webcam2,String savePath1,String savePath2){
		this.webcam1 = webcam1;
		this.savePath1 = savePath1;
		this.webcam2 = webcam2;
		this.savePath2 = savePath2;
	}
	
	@Override
	public void run() {
		File webcam1Folder = new File(savePath1);
		if(webcam1Folder.exists()){
			webcam1Folder.deleteOnExit();
		}
		webcam1Folder.mkdirs();
		File webcam2Folder = new File(savePath2);
		if(webcam2Folder.exists()){
			webcam2Folder.deleteOnExit();
		}
		webcam2Folder.mkdirs();
		while(workingFlag){
			 //保存图片到本地  
            File webcam1File = new File(savePath1 + File.separator + System.currentTimeMillis() + ".jpg");  
            File webcam2File = new File(savePath2 + File.separator + System.currentTimeMillis() + ".jpg");  
              
            try {
//				ImageIO.write(webcam1.getImage(), "jpg", webcam1File);
				ImageIO.write(webcam2.getImage(), "jpg", webcam2File);
//				Thread.sleep(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	public void start(){
		executorService.execute(this);
	}
	
	public void stop(){
		workingFlag = false;
		executorService.shutdown();
	}
	
}


class SoundThread implements Runnable{
	
	private boolean workingFlag = true;

	@Override
	public void run() {
		IMediaWriter writer = ToolFactory.makeWriter("output.mp3");
		AudioFormat format = getAudioFormat();
		writer.addAudioStream(0, 0, 1, (int) format.getSampleRate());
		TargetDataLine line = null;

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		byte[] audioBuf = new byte[line.getBufferSize() / 5];
		line.start();
		
		 while(workingFlag)
		 {
			 int nBytesRead = line.read(audioBuf, 0, audioBuf.length);
				IBuffer iBuf = IBuffer.make(null, audioBuf, 0, nBytesRead);
				IAudioSamples smp = IAudioSamples.make(iBuf, 1,
						IAudioSamples.Format.FMT_S16);
				long numSample = nBytesRead / smp.getSampleSize();
				smp.put(audioBuf, 0, 0, nBytesRead);
				smp.setComplete(true, numSample, (int) format.getSampleRate(), 1,
						IAudioSamples.Format.FMT_S16, line.getMicrosecondPosition());
		   writer.encodeAudio(0, smp);
		 }
		 writer.close();
	}
	
	ExecutorService executorService = Executors.newFixedThreadPool(1);
	public void start(){
		executorService.submit(this);
	}
	
	
	public void stop(){
		workingFlag = false;
		executorService.shutdown();
	}
	
	private AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
				channels, signed, bigEndian);
		return format;
	}
	
}




class DecodeAndPlayAudio
{

  /**
   * The audio line we'll output sound to; it'll be the default audio device on your system if available
   */
  private static SourceDataLine mLine;

  /**
   * Takes a media container (file) as the first argument, opens it,
   * opens up the default audio device on your system, and plays back the audio.
   *  
   * @param args Must contain one string which represents a filename
   */
  public void decode(String filename,String url1,String url2)
  {
    // Create a Xuggler container object
    IContainer container = IContainer.make();
    
    // Open up the container
    if (container.open(filename, IContainer.Type.READ, null) < 0)
      throw new IllegalArgumentException("could not open file: " + filename);
    
    // query how many streams the call to open found
    int numStreams = container.getNumStreams();
    
    // and iterate through the streams to find the first audio stream
    int audioStreamId = -1;
    IStreamCoder audioCoder = null;
    for(int i = 0; i < numStreams; i++)
    {
      // Find the stream object
      IStream stream = container.getStream(i);
      // Get the pre-configured decoder that can decode this stream;
      IStreamCoder coder = stream.getStreamCoder();
      
      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
      {
        audioStreamId = i;
        audioCoder = coder;
        break;
      }
    }
    if (audioStreamId == -1)
      throw new RuntimeException("could not find audio stream in container: "+filename);
    
    /*
     * Now we have found the audio stream in this file.  Let's open up our decoder so it can
     * do work.
     */
    if (audioCoder.open(null, null) < 0)
      throw new RuntimeException("could not open audio decoder for container: "+filename);
    
    /*
     * And once we have that, we ask the Java Sound System to get itself ready.
     */
    openJavaSound(audioCoder);
    
    
    /*
     * 合成视频配置信息
     */
    int videoStreamIndex = 0;
    int videoStreamId = 0;
    long deltaTime = 50000;
    int w = 1600;
    int h = 900;

    // audio parameters

    int audioStreamIndex = 1;
     audioStreamId = 0;
    int channelCount = 1;
    int sampleRate = 16000;//576
    long time = 0;
	File file1 = new File(url1);
	File file2 = new File(url2);
	File[] fileList1 = null;
	File[] fileList2 = null;
	if(file1.exists()){
		fileList1 = file1.listFiles();
	}
	if(file2.exists()){
		fileList2 = file2.listFiles();
	}
	
	String videoName = "E:/3" + File.separator + System.currentTimeMillis()+ "test.mp4";
	IMediaWriter writer = ToolFactory.makeWriter(videoName);

    // add the video stream
    ICodec videoCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_H264);
    writer.addVideoStream(videoStreamIndex, videoStreamId, videoCodec, w, h);

    // add the audio stream

    ICodec audioCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_MP3);
    IContainer container1 = writer.getContainer();
    writer.addAudioStream(audioStreamIndex, audioStreamId,
  	      audioCodec, channelCount, sampleRate);
    IStream stream = container1.getStream(audioStreamIndex);
    int sampleCount = stream.getStreamCoder().getDefaultAudioFrameSize();
    TestAudioSamplesGenerator generator = new TestAudioSamplesGenerator();
    generator.prepare(1, 16000);
	
	BufferedImage combined = new BufferedImage((int) 1280,(int) 720, BufferedImage.TYPE_3BYTE_BGR);
	Graphics g = combined.getGraphics();
	int i =0;
	long videoTime = 0;
    long audioTime = 0;
    long totalSamples = 0;
    /*
     * Now, we start walking through the container looking at each packet.
     */
    IPacket packet = IPacket.make();
    while(container.readNextPacket(packet) >= 0)
    {
      /*
       * Now we have a packet, let's see if it belongs to our audio stream
       */
      if (packet.getStreamIndex() == audioStreamId)
      {
        /*
         * We allocate a set of samples with the same number of channels as the
         * coder tells us is in this buffer.
         * 
         * We also pass in a buffer size (1024 in our example), although Xuggler
         * will probably allocate more space than just the 1024 (it's not important why).
         */
        IAudioSamples samples = IAudioSamples.make(audioCoder.getDefaultAudioFrameSize(), audioCoder.getChannels());
        
        /*
         * A packet can actually contain multiple sets of samples (or frames of samples
         * in audio-decoding speak).  So, we may need to call decode audio multiple
         * times at different offsets in the packet's data.  We capture that here.
         */
        int offset = 0;
        
        /*
         * Keep going until we've processed all data
         */
        while(offset < packet.getSize())
        {
          int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
          if (bytesDecoded < 0)
            throw new RuntimeException("got error decoding audio in: " + filename);
          offset += bytesDecoded;
          /*
           * Some decoder will consume data in a packet, but will not be able to construct
           * a full set of samples yet.  Therefore you should always check if you
           * got a complete set of samples from the decoder
           */
          if (samples.isComplete())
          {
//            playJavaSound(samples);
//            customAudioVideoStream(samples);
            
            /*
             * 合成视频
             */
            audioTime = (totalSamples * 900 * 100) / 16000;
            
            if(audioTime >= time){
            	try {
            		g.drawImage(ImageIO.read(fileList1[i]), 0, 0, 320, 240, null);
            		g.drawImage(ImageIO.read(fileList2[i]), 320, 0, 960, 720, null);
            	} catch (IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
            	}
            	IConverter converter = ConverterFactory.createConverter(combined,
            			IPixelFormat.Type.YUV420P);
            	IVideoPicture frame = converter.toPicture(combined,time);
            	frame.setKeyFrame(i == 0);
            	frame.setQuality(0);
            	
//            	writer.encodeVideo(0, frame);
            	writer.encodeVideo(videoStreamIndex, combined, time,
        	            Global.DEFAULT_TIME_UNIT);
            	
            	time += 50000;
            	System.out.println("i="+i);
            	i++;
            }
            generator.fillNextSamples(samples, sampleCount);
            System.out.println(writer.getContainer());
//            writer.encodeAudio(audioStreamIndex, samples);
            totalSamples += samples.getNumSamples();
          }
        }
      }
      else
      {
        /*
         * This packet isn't part of our audio stream, so we just silently drop it.
         */
        do {} while(false);
      }
      
      
      writer.close();
      
    }
    /*
     * Technically since we're exiting anyway, these will be cleaned up by 
     * the garbage collector... but because we're nice people and want
     * to be invited places for Christmas, we're going to show how to clean up.
     */
    closeJavaSound();
    
    if (audioCoder != null)
    {
      audioCoder.close();
      audioCoder = null;
    }
    if (container !=null)
    {
      container.close();
      container = null;
    }
  }

  private static void openJavaSound(IStreamCoder aAudioCoder)
  {
    AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(),
        (int)IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()),
        aAudioCoder.getChannels(),
        true, /* xuggler defaults to signed 16 bit samples */
        false);
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
    try
    {
      mLine = (SourceDataLine) AudioSystem.getLine(info);
      /**
       * if that succeeded, try opening the line.
       */
      mLine.open(audioFormat);
      /**
       * And if that succeed, start the line.
       */
      mLine.start();
    }
    catch (LineUnavailableException e)
    {
      throw new RuntimeException("could not open audio line");
    }
    
    
  }

  private static void playJavaSound(IAudioSamples aSamples)
  {
    /**
     * We're just going to dump all the samples into the line.
     */
    byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
    mLine.write(rawBytes, 0, aSamples.getSize());
  }

  private static void closeJavaSound()
  {
    if (mLine != null)
    {
      /*
       * Wait for the line to finish playing
       */
      mLine.drain();
      /*
       * Close the line.
       */
      mLine.close();
      mLine=null;
    }
  }
  
  
  public void customAudioVideoStream(IAudioSamples samples)
  {
    int videoStreamIndex = 0;
    int videoStreamId = 0;
    long deltaTime = 50000;
    int w = 1600;
    int h = 900;
    int audioStreamIndex = 1;
    int audioStreamId = 0;
    int channelCount = 1;
    int sampleRate = 16000;//576
	String videoName = "E:/3" + File.separator + System.currentTimeMillis()
			+ "test.mp4";
	IMediaWriter writer = ToolFactory.makeWriter(videoName);
    ICodec videoCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_H264);
    writer.addVideoStream(videoStreamIndex, videoStreamId, videoCodec, w, h);
    ICodec audioCodec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_MP3);
    IContainer container = writer.getContainer();
    writer.addAudioStream(audioStreamIndex, audioStreamId,
  	      audioCodec, channelCount, sampleRate);
    IStream stream = container.getStream(audioStreamIndex);
    int sampleCount = stream.getStreamCoder().getDefaultAudioFrameSize();
    IVideoPicture picture = IVideoPicture.make(IPixelFormat.Type.YUV420P, w, h);
    TestAudioSamplesGenerator generator = new TestAudioSamplesGenerator();
    generator.prepare(channelCount, sampleRate);
    long videoTime = 0;
    long audioTime = 0;
    long totalSamples = 0;
    long totalSeconds = 6;
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
  
  
  private AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
				channels, signed, bigEndian);
		return format;
	}
}
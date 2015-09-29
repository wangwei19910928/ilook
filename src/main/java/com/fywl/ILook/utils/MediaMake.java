package com.fywl.ILook.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fywl.ILook.bean.RecordConfig;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.TestAudioSamplesGenerator;

public class MediaMake {

}


/**
 * 缓存image的线程
 * @author Administrator
 *
 */
class CacheImage implements Runnable{
	
	public ExecutorService executor = Executors.newFixedThreadPool(1);
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
		while(true){
			if(null != combined){
				try {
					bq.put(deepCopy(combined));
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("cache=="+bq.size());
					
			}
		}
	}
	
	public Graphics start() {
		combined = new BufferedImage((int) 1600,
				(int) 900, BufferedImage.TYPE_3BYTE_BGR);
		executor.submit(this);
		return combined.getGraphics();
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
	
	public ExecutorService executor = Executors.newFixedThreadPool(1);
	
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
	    long deltaTime = 50000;
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
		      
		      System.out.println("hecheng=="+ i++);
		    }
		    
		    if(3600 == i)
		    	break;
			}

		    // close the writer

		    writer.close();
		    System.out.println("-------------------------------------");
		    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}

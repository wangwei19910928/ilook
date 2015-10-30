import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

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
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;


public class MakerVideoByMP3 {
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
	  public static void main(String[] arfs)
	  {
	    String filename = "E:/eclipse-jee-juno-SR1-win32/eclipse-jee-juno-SR1-win32/workspace/ILook/output.mp3";
	    
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
	     * Now, we start walking through the container looking at each packet.
	     */
	    IPacket packet = IPacket.make();
	    IMediaWriter writer = ToolFactory.makeWriter("E:/3/output.mp4");
		AudioFormat format = getAudioFormat();
		writer.addAudioStream(1, 0, 1, (int) format.getSampleRate());
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264,
				(int) 1280, (int) 720);
		
		BufferedImage combined = new BufferedImage((int) 1280,(int) 720, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = combined.getGraphics();
		int i=0;
		int time=0;
		File file1 = new File("E:/4");
		File file2 = new File("E:/5");
		File[] fileList1 = null;
		File[] fileList2 = null;
		if(file1.exists()){
			fileList1 = file1.listFiles();
		}
		if(file2.exists()){
			fileList2 = file2.listFiles();
		}
		long start = System.currentTimeMillis();
		long audioTime = 0;
		long videoTime = 0;
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
	        IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
	        
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
//	            playJavaSound(samples);
	        	  if(videoTime <= audioTime){
	        		  
//	        		  try {
//	        			  g.drawImage(ImageIO.read(fileList1[i]), 0, 0, 320, 240, null);
//	        			  g.drawImage(ImageIO.read(fileList2[i]), 320, 0, 960, 720, null);
//	        		  } catch (IOException e) {
//	        			  // TODO Auto-generated catch block
//	        			  e.printStackTrace();
//	        		  }
//	        		  IConverter converter = ConverterFactory.createConverter(combined,
//	        				  IPixelFormat.Type.YUV420P);
//	        		  IVideoPicture frame = converter.toPicture(combined,time);
//	        		  frame.setKeyFrame(i == 0);
//	        		  frame.setQuality(0);
//	        		  
//	        		  writer.encodeVideo(0, frame);
//	        		  time += 50000;
//	        		  videoTime += 5000;
	        	  }
	        	  i++;
//	            	writer.encodeVideo(0, combined, time,
//	        	            Global.DEFAULT_TIME_UNIT);
	        	  writer.encodeAudio(1, samples);
	        	  audioTime += samples.getNumSamples()*90000/16000;
	        	  System.out.println(i);
	        	  try {
	        		  if(i % 10 == 0){
	        			  Thread.sleep(100);
	        		  }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	      
	    }
	    /*
	     * Technically since we're exiting anyway, these will be cleaned up by 
	     * the garbage collector... but because we're nice people and want
	     * to be invited places for Christmas, we're going to show how to clean up.
	     */
	    closeJavaSound();
	    writer.close();
	    long end = System.currentTimeMillis();
	    System.out.println((end-start)/1000);
	    
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
	  
	  private static boolean workingFlag = true;
	  private static void makerMP3(){
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
	  
	  private static AudioFormat getAudioFormat() {
			float sampleRate = 16000;
			int sampleSizeInBits = 16;
			int channels = 1;
			boolean signed = true;
			boolean bigEndian = false;
			AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
					channels, signed, bigEndian);
			return format;
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
	}

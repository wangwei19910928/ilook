import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;


public class TestVideo {
	
	public static void main(String[] args) {
		video("E:/eclipse-jee-juno-SR1-win32/eclipse-jee-juno-SR1-win32/workspace/ILook/target/test.mp4");
	}
	
	public static void video(String filename){
		
	IContainer container = IContainer.make();  
    
    //开始读流  
    if (container.open(filename, IContainer.Type.READ, null) < 0)  
        throw new IllegalArgumentException("could not open file: " + filename);  
      
    //获取流数  
    int numStreams = container.getNumStreams();  
      
    int videoStreamId = -1;  
    IStreamCoder videoCoder = null;  
    //从流中识别视频流  
    for (int i = 0; i < numStreams; i++)  
    {  
        IStream stream = container.getStream(i);  
        IStreamCoder coder = stream.getStreamCoder();  
          
        if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)  
        {  
            videoStreamId = i;  
            videoCoder = coder;  
            break;  
        }  
    }  
      
    if (videoStreamId == -1)  
        throw new RuntimeException("could not find video stream in container: " + filename);  
      
    //读取视频流  
    if (videoCoder.open() < 0)  
        throw new RuntimeException("could not open video decoder for container: " + filename);  
      
    IVideoResampler resampler = null;  
    //重新编码图片格式  
    if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)  
    {  
        resampler =  
            IVideoResampler.make(videoCoder.getWidth(),  
                videoCoder.getHeight(),  
                IPixelFormat.Type.BGR24,  
                videoCoder.getWidth(),  
                videoCoder.getHeight(),  
                videoCoder.getPixelType());  
        if (resampler == null)  
            throw new RuntimeException("could not create color space " + "resampler for: " + filename);  
    }  
      
//    循环读取  
    IPacket packet = IPacket.make();  
    long firstTimestampInStream = Global.NO_PTS;  
    long systemClockStartTime = 0;  
    while (container.readNextPacket(packet) >= 0)  
    {  
        if (packet.getStreamIndex() == videoStreamId)//识别视频流  
        {  
            //图片编码  
            IVideoPicture picture =  
                IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());  
              
            int offset = 0;  
              
            while (offset < packet.getSize())  
            {  
                int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset); 
                System.out.println(bytesDecoded);
                if (bytesDecoded < 0)  
                    throw new RuntimeException("got error decoding video in: " + filename);  
                offset += bytesDecoded;  
                  
                //图片帧已读取完，保存图片  
                if (picture.isComplete())  
                {  
                    IVideoPicture newPic = picture;  
                    if (resampler != null)  
                    {  
                        newPic =  
                            IVideoPicture.make(resampler.getOutputPixelFormat(),  
                                picture.getWidth(),  
                                picture.getHeight());  
                        if (resampler.resample(newPic, picture) < 0)  
                            throw new RuntimeException("could not resample video from: " + filename);  
                    }  
                    if (newPic.getPixelType() != IPixelFormat.Type.BGR24)  
                        throw new RuntimeException("could not decode video" + " as BGR 24 bit data in: " + filename);  
                    BufferedImage javaImage = Utils.videoPictureToImage(newPic);  
                    //保存图片到本地  
                    File file = new File("E:/2/" + System.currentTimeMillis() + ".jpg");  
                      
                    try {
						ImageIO.write(javaImage, "jpg", file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
//                    processFrame(picture, javaImage);  
                }   
               }  
            }  
        }  
    }  
  
/** 
 * 根据固定间隔采集图片 
 */  
//private static void processFrame(IVideoPicture picture, BufferedImage image) {  
//    try {  
//          
//        //mLastPtsWrite记录最近一次图片采集时间  
//        //NANO_SECONDS_BETWEEN_FRAMES，纳秒时间间隔  
//        if(mLastPtsWrite == Global.NO_PTS)  
//            mLastPtsWrite = picture.getPts() - NANO_SECONDS_BETWEEN_FRAMES;  
//          
//        //每隔NANO_SECONDS_BETWEEN_FRAMES采集  
//        if(picture.getPts() - mLastPtsWrite >= NANO_SECONDS_BETWEEN_FRAMES) {  
//              
//            File file = new File("c:/1/" + i + ".jpg");  
//            i++;  
//              
//            ImageIO.write(image, "jpg", file);  
//              
//            double seconds = ((double)picture.getPts()) / Global.DEFAULT_PTS_PER_SECOND;  
//            System.out.printf("at elapsed time of %6.3f seconds wrote: %s\n", seconds, file);  
//              
//            mLastPtsWrite += NANO_SECONDS_BETWEEN_FRAMES;  
//        }  
//    } catch (Exception e) {  
//        e.printStackTrace();  
//    }  
//}
}

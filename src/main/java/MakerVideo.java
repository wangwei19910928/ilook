import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;


public class MakerVideo {
	
	public static void main(String[] args) {
		MakerVideo mv = new MakerVideo();
		
		mv.maker("E:/4", "E:/5");
	}
	

	public void maker(String url1,String url2){
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
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264,
				(int) 1280, (int) 720);
		
		BufferedImage combined = new BufferedImage((int) 1280,(int) 720, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = combined.getGraphics();
		for(int i=0;i<fileList1.length;i++){
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
			
			writer.encodeVideo(0, frame);
			
			time += 100000;
		}
		
		writer.close();
	}
}

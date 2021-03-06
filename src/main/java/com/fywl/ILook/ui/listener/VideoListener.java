package com.fywl.ILook.ui.listener;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.xuggle.xuggler.video.ConverterFactory;

public class VideoListener implements WebcamListener {

	private Graphics g;

	private int x;

	private int y;

	private int width;

	private int height;
	
	private long start = System.currentTimeMillis();

	public VideoListener(Graphics g, int x, int y, int width, int height) {
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
			
			BufferedImage bufferedimage = image;
			
//			int w = bufferedimage.getWidth();
//            int h = bufferedimage.getHeight();
//            int[][] datas = new int[w][h];
//            for (int i = 0; i < h; i++) {
//                for (int j = 0; j < w; j++) {
//                    datas[j][i] = bufferedimage.getRGB(j, i);
//                }
//            }
//            int[][] tmps = new int[w][h];
//            for (int i = 0; i < h; i++) {
//                for (int j = 0, b = w - 1; j < w; j++, b--) {
//                    tmps[b][i] = datas[j][i];
//                }
//            }
//            for (int i = 0; i < h; i++){
//                for (int j = 0; j<w ;j++){
//                    bufferedimage.setRGB(j, i, tmps[j][i]);
//                }
//            }
			
			g.drawImage(bufferedimage, x, y, width, height, null);
//			g.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
//			Graphics2D g2d = (Graphics2D)g;  
//	        g2d.rotate(180,width/2,height/2);  
//	        g2d.drawImage(img,100,100,300,300,null);
//	        g2d.drawImage(image, x, y, width, height, null);
		}
	}

}

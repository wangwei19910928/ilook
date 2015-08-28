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
			g.drawImage(image, x, y, width, height, null);
		}
	}

}

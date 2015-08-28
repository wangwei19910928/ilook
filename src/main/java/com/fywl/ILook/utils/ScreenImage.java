package com.fywl.ILook.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ScreenImage {

	private Rectangle rect;

	private BufferedImage image;

	public ScreenImage(BufferedImage image, Rectangle rect) {
		this.image = image;
		this.rect = rect;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Rectangle getRect() {
		return rect;
	}

}

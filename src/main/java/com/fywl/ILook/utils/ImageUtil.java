package com.fywl.ILook.utils;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.WritableRaster;
import java.io.InputStream;
import java.util.Stack;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import com.fywl.ILook.win.ext.GDI32Extra;
import com.fywl.ILook.win.ext.User32Extra;
import com.fywl.ILook.win.ext.WinGDIExtra;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class ImageUtil {

	private static final User32Extra user32 = User32Extra.INSTANCE;

	private static final ImageUtil util = new ImageUtil();

	private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

	private Robot robot;

	private ImageUtil() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			;
		}
	}

	public static final ImageUtil getInstance() {
		return util;
	}

	public Image getImage(String imageName) {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(imageName);
		ImageData source = new ImageData(is);
		ImageData mask = source.getTransparencyMask();
		Image image = new Image(null, source, mask);
		return image;
	}

	public ImageData convertToSWT(BufferedImage bufferedImage) {

		if (bufferedImage.getColorModel() instanceof ComponentColorModel) {
			ComponentColorModel colorModel = (ComponentColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(0x0000FF, 0x00FF00, 0xFF0000);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = -1;
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		}
		return null;
	}

	public BufferedImage screenCapture() {

		final Stack<ScreenImage> images = new Stack<>();

		user32.EnumWindows(new WNDENUMPROC() {
			@Override
			public boolean callback(HWND hWnd, Pointer arg1) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				if (user32.IsWindowVisible(hWnd)) {
					String wText = Native.toString(windowText, "GBK");
					if (wText.isEmpty() || "开始".equals(wText)) {
						return true;
					}
					RECT bounds = new RECT();
					user32.GetWindowRect(hWnd, bounds);
					Rectangle rect = bounds.toRectangle();
					if (bounds.left > -32000) {
						BufferedImage screen = capture(hWnd);
						if (screen != null) {
							ScreenImage image = new ScreenImage(screen, rect);
							images.push(image);
						}
					}
				}
				return true;
			}
		}, null);

		if (!images.isEmpty()) {
			ScreenImage deskTopImage = images.pop();
			BufferedImage combined = new BufferedImage(deskTopImage.getRect().width, deskTopImage.getRect().height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = combined.getGraphics();
			g.drawImage(deskTopImage.getImage(), 0, 0, null);
			while (!images.isEmpty()) {
				ScreenImage image = images.pop();
				g.drawImage(image.getImage(), image.getRect().x, image.getRect().y, null);
			}
			return combined;
		}

		return null;
	}

	private BufferedImage capture(HWND hWnd) {

		HDC hdcWindow = GDI32Extra.INSTANCE.GetDCEx(hWnd, null, GDI32Extra.DCX_WINDOW);
		HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

		RECT bounds = new RECT();
		User32Extra.INSTANCE.GetWindowRect(hWnd, bounds);

		int width = bounds.right - bounds.left;
		int height = bounds.bottom - bounds.top;

		HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

		HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
		GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, WinGDIExtra.SRCCOPY);

		GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
		GDI32.INSTANCE.DeleteDC(hdcMemDC);

		BITMAPINFO bmi = new BITMAPINFO();
		bmi.bmiHeader.biWidth = width;
		bmi.bmiHeader.biHeight = -height;
		bmi.bmiHeader.biPlanes = 1;
		bmi.bmiHeader.biBitCount = 32;
		bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

		Memory buffer = new Memory(width * height * 4);
		GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

		GDI32.INSTANCE.DeleteObject(hBitmap);
		User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);

		return image;
	}

	public BufferedImage getAppFullScreen() {
		try {
			HWND hwnd = user32.GetForegroundWindow();
			RECT bounds = new RECT();
			User32Extra.INSTANCE.GetWindowRect(hwnd, bounds);
			Rectangle rect = bounds.toRectangle();

			if (rect.getWidth() == size.getWidth() && rect.getHeight() == size.getHeight()) {
				return robot.createScreenCapture(rect);
			}
		} catch (Exception e) {
			;
		}
		return null;
	}
	
	/**
	 * 设置图标
	 * @param d
	 * @param is
	 * @return
	 */
	public Image getImage(Device d,InputStream is){
		return new Image(d, is);
	}

}
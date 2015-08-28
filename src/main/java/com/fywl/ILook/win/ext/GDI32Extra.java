package com.fywl.ILook.win.ext;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HRGN;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;

public interface GDI32Extra extends GDI32 {

	GDI32Extra INSTANCE = (GDI32Extra) Native.loadLibrary(GDI32Extra.class);
	
	public static final int DIB_RGB_COLORS = 0;
	
	int SRCCOPY = 0xCC0020;
	
	public int DCX_WINDOW = 0x000001;

	public boolean BitBlt(HDC hObject, int nXDest, int nYDest, int nWidth, int nHeight, HDC hObjectSource, int nXSrc, int nYSrc, DWORD dwRop);

	HDC GetDC(HWND hWnd);

	HDC GetDCEx(HWND hWnd, HRGN hrgnClip, int flags);

	boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, byte[] pixels, BITMAPINFO bi, int usage);

	boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, short[] pixels, BITMAPINFO bi, int usage);

	boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, int[] pixels, BITMAPINFO bi, int usage);

}
package com.fywl.ILook.win.ext;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;


public interface User32Extra extends User32 {

    User32Extra INSTANCE = (User32Extra) Native.loadLibrary("user32", User32Extra.class, W32APIOptions.DEFAULT_OPTIONS);
    
    public boolean PrintWindow(HWND hwnd, HDC hdcBlt, int nFlags);
    
    public HWND GetDesktopWindow();

    public HDC GetWindowDC(HWND hWnd);

    public boolean GetClientRect(HWND hWnd, RECT rect);
    
    public int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);

}

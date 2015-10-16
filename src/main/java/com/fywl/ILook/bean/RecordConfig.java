package com.fywl.ILook.bean;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.github.sarxos.webcam.Webcam;

public class RecordConfig {

	// Water mark
	private BufferedImage watermarkImage;

	private Point watermarkLocation;

	// Cursor
	private BufferedImage cursorImage;

	// Facecam Othercam
	private Webcam faceCam, otherCam;

	// File location
	private File videoFile;

	private boolean faceRecording = false;

	private boolean screenRecording = true;
	
	private boolean noteRecording = false;

	// Video time
	private long videoLength = 300;

	// Frame rate
	private int framesRate = 30;

	private Rectangle frameDimension;
	
	//录制的尺寸
	private Dimension videoSize;
	//是否是单瓶录制
	private boolean isSingleRecording;
	//脸部摄像头
	private boolean changeFace;
	//笔记摄像头和屏幕互换
	private boolean changeNote;
	//脸部摄像头和笔记摄像头互换
	private boolean changeScreen;
	//有切换画面的动作
	private boolean changeFlag = false;
	//头部摄像头尺寸
	private Dimension head;
	//笔记摄像头尺寸
	private Dimension note;
	//个人信息
	private InfoBean ib;
	
	private RecordConfig() {

	}
	
	private static RecordConfig config;
	public static RecordConfig get(){
		if(null == config){
			config = new RecordConfig();
		}
		return config;
	}

	public long getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(long videoLength) {
		this.videoLength = videoLength;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	public int getFramesRate() {
		return framesRate;
	}

	public void setFramesRate(int framesRate) {
		this.framesRate = framesRate;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	public Rectangle getFrameDimension() {
		return frameDimension;
	}

	public void setFrameDimension(Rectangle frameDimension) {
		this.frameDimension = frameDimension;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	public BufferedImage getWatermarkImage() {
		return watermarkImage;
	}

	public void setWatermarkImage(BufferedImage watermarkImage) {
		this.watermarkImage = watermarkImage;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	public BufferedImage getCursorImage() {
		return cursorImage;
	}

	public void setCursorImage(BufferedImage cursorImage) {
		this.cursorImage = cursorImage;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	public File getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(File videoFile) {
		this.videoFile = videoFile;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	public Point getWatermarkLocation() {
		return watermarkLocation;
	}

	public void setWatermarkLocation(Point watermarkLocation) {
		this.watermarkLocation = watermarkLocation;
		propertyChangeSupport.firePropertyChange(propertyName, 0, 1);
	}

	private String propertyName = "RECORD_CONFIG_CHANGE";

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public Webcam getFaceCam() {
		return faceCam;
	}

	public void setFaceCam(Webcam faceCam) {
		this.faceCam = faceCam;
	}

	public Webcam getOtherCam() {
		return otherCam;
	}

	public void setOtherCam(Webcam otherCam) {
		this.otherCam = otherCam;
	}

	public boolean isFaceRecording() {
		return faceRecording;
	}

	public void setFaceRecording(boolean faceRecording) {
		this.faceRecording = faceRecording;
	}

	public boolean isScreenRecording() {
		return screenRecording;
	}

	public void setScreenRecording(boolean screenRecording) {
		this.screenRecording = screenRecording;
	}

	public Dimension getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Dimension videoSize) {
		this.videoSize = videoSize;
	}

	public boolean isSingleRecording() {
		return isSingleRecording;
	}

	public void setSingleRecording(boolean isSingleRecording) {
		this.isSingleRecording = isSingleRecording;
	}


	public boolean isNoteRecording() {
		return noteRecording;
	}

	public void setNoteRecording(boolean noteRecording) {
		this.noteRecording = noteRecording;
	}

	public boolean isChangeFlag() {
		return changeFlag;
	}

	public void setChangeFlag(boolean changeFlag) {
		this.changeFlag = changeFlag;
	}

	public boolean isChangeFace() {
		return changeFace;
	}

	public void setChangeFace(boolean changeFace) {
		this.changeFace = changeFace;
	}

	public boolean isChangeNote() {
		return changeNote;
	}

	public void setChangeNote(boolean changeNote) {
		this.changeNote = changeNote;
	}

	public boolean isChangeScreen() {
		return changeScreen;
	}

	public void setChangeScreen(boolean changeScreen) {
		this.changeScreen = changeScreen;
	}

	public Dimension getHead() {
		return head;
	}

	public void setHead(Dimension head) {
		this.head = head;
	}

	public Dimension getNote() {
		return note;
	}

	public void setNote(Dimension note) {
		this.note = note;
	}

	public InfoBean getIb() {
		return ib;
	}

	public void setIb(InfoBean ib) {
		this.ib = ib;
	}
	
}

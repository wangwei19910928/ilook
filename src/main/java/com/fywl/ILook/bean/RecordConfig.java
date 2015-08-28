package com.fywl.ILook.bean;

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

	private boolean screenRecording = false;

	// Video time
	private long videoLength = 300;

	// Frame rate
	private int framesRate = 30;

	private Rectangle frameDimension;

	public RecordConfig() {

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
}

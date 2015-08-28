package com.fywl.ILook.ui.components;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JRootPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import com.fywl.ILook.bean.RecordConfig;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class VideoPlayBackPanel extends Composite {

	private Frame frame;

	private WebcamPanel panel;

	private RecordConfig config;

	private JRootPane root = new JRootPane();

	private Dimension size = WebcamResolution.QVGA.getSize();

	private boolean running = false;

	private boolean mirrored = false;

	private Webcam webcam;

	public VideoPlayBackPanel(Composite parent, RecordConfig config,
			int index) {
		super(parent, SWT.EMBEDDED);
		this.config = config;
		Webcam wb = Webcam.getWebcams().get(index);
		if (null != wb) {
			webcam = wb;
		}
		init();
	}

	private void init() {
		frame = SWT_AWT.new_Frame(this);
		frame.add(root);
	}

	public void start() {
		if (webcam != null) {
			webcam.setViewSize(size);
			panel = new WebcamPanel(webcam, size, false);
			root.getContentPane().add(panel);
			frame.setVisible(true);
			panel.start();
			running = true;
		}
	}

	public void stop() {
		if (panel != null) {
			frame.remove(panel);
			panel.stop();
			panel = null;
			running = false;
		}
	}

	public void mirror() {
		if (panel != null) {
			panel.setMirrored(!mirrored);
		}
	}

	public boolean isRunning() {
		return running;
	}

}

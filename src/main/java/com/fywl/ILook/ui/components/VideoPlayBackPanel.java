package com.fywl.ILook.ui.components;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JRootPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class VideoPlayBackPanel extends Composite {

	private Frame frame;

	private WebcamPanel panel;

//	private RecordConfig config;

	private JRootPane root = new JRootPane();

	private Dimension size = WebcamResolution.VGA.getSize();

	private boolean running = false;

	private boolean mirrored = false;

	private Webcam webcam;
	public VideoPlayBackPanel(Composite parent,
			int index) {
		super(parent, SWT.EMBEDDED);
		Webcam wb = Webcam.getWebcams().get(index);
//		if(null != wb){
//			webcam = wb;
//		}
		if (null != wb) {
			webcam = wb;
			webcam.setViewSize(size);
			Dimension[] nonStandardResolutions = new Dimension[] {
					new Dimension(2048, 1536),
					new Dimension(1280, 960),
					new Dimension(1024, 768),
					new Dimension(800, 600),
			};
			webcam.setCustomViewSizes(nonStandardResolutions);
			if(1 == index){
				mirrored = true;
				webcam.setViewSize(new Dimension(1024, 768));
			}
			
		}
		init();
	}

	private void init() {
		frame = SWT_AWT.new_Frame(this);
		frame.add(root);
	}

	public void start() {
		if (webcam != null) {
//			webcam.setViewSize(size);
			System.out.println(webcam.getViewSize());
			panel = new WebcamPanel(webcam, webcam.getViewSize(), false);
			panel.setFPSLimit(25);
//			mirror();
			root.getContentPane().add(panel);
			frame.setVisible(true);
			panel.start();
			running = true;
		}
		
//		if (webcam != null) {
//			webcam.setViewSize(size);
//			panel = new WebcamPanel(webcam, size, false);
//			root.getContentPane().add(panel);
//			frame.setVisible(true);
//			panel.start();
//			running = true;
//		}
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

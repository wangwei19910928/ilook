package com.fywl.ILook.ui.components.panel;

import org.eclipse.swt.widgets.Composite;

import com.fywl.ILook.inter.Closer;
import com.fywl.ILook.ui.components.VideoRecorder;

public class Panel extends Composite{

	protected Composite parent;

	protected Closer closer;

	protected boolean recording = false;

	protected VideoRecorder recorder = null;

	public Panel(Closer closer, Composite parent, int style, VideoRecorder recorder) {
		super(parent, style);
		this.parent = parent;
		this.closer = closer;
		this.recorder = recorder;
		init();
	}
	
	protected void init(){};

}

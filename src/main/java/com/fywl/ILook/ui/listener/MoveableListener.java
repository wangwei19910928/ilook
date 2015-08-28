package com.fywl.ILook.ui.listener;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Composite;

public class MoveableListener implements MouseListener, MouseMoveListener {

	private static Boolean blnMouseDown = false;

	private static int xPos = 0;
	private static int yPos = 0;

	private Composite shell;

	public MoveableListener(Composite shell) {
		this.shell = shell;
	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		blnMouseDown = false;
	}

	@Override
	public void mouseDown(MouseEvent e) {
		blnMouseDown = true;
		xPos = e.x;
		yPos = e.y;
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (blnMouseDown) {
			shell.setLocation(shell.getLocation().x + (e.x - xPos), shell.getLocation().y + (e.y - yPos));
		}
	}

}

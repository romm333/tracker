package usertracking.prototype.gui.views;

import java.io.PrintStream;

import javax.swing.JTextArea;

import usertracking.prototype.gui.utils.TextAreaOutputStream;

public class SkeletonTextInfoView extends JTextArea {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SkeletonTextInfoView() {
		super(10,10);
		TextAreaOutputStream taos = new TextAreaOutputStream(this);
		 PrintStream ps = new PrintStream( taos );
	        System.setOut( ps );
	        System.setErr( ps );
	}

	public SkeletonTextInfoView(int rows, int cols) {
		super(cols,rows);
		TextAreaOutputStream taos = new TextAreaOutputStream(this);
		 PrintStream ps = new PrintStream( taos );
	        System.setOut( ps );
	        System.setErr( ps );
	}
}

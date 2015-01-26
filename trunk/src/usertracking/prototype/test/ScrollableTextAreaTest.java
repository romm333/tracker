package usertracking.prototype.test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ScrollableTextAreaTest {
	public static JTextArea textArea;
	public static void main(String[] args) {
		 JFrame frame = new JFrame("SSCCE");
	        frame.setLayout(new BorderLayout());
	        frame.setSize(500, 500);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
	        
	        JPanel PP = new JPanel();
	        PP.setSize(500, 500);
	        
	        textArea = new JTextArea(20,30);
	        
	        
	        JScrollPane scrollPane = new JScrollPane(textArea);
	        PP.add(scrollPane);
	        
	        frame.add(PP, BorderLayout.CENTER);	        frame.setVisible(true);
	}
}

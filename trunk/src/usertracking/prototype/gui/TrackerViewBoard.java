package usertracking.prototype.gui;


import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;

import usertracking.prototype.classes.*;


public class TrackerViewBoard extends JFrame {

	private static final long serialVersionUID = 1L;

	//private Frame this;
	private Label headerLabel;
	private Label statusLabel;
	private UserTracker userTracker;
	
	
	public TrackerViewBoard() {
		super("Tracker View Board");
		prepareGUI();
	}

	public static void main(String[] args) {
		TrackerViewBoard trackerViewBoard = new TrackerViewBoard();
		trackerViewBoard.showTopMenu();
	    while(true) {
	    	trackerViewBoard.userTracker.updateDepth();
	    	trackerViewBoard.userTracker.repaint();
	    }
   	}

	private void prepareGUI() {
	
		this.setSize(600, 800);
		this.setLayout(new GridLayout(3, 1));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		headerLabel = new Label();
		headerLabel.setAlignment(Label.CENTER);
		
		statusLabel = new Label();
		statusLabel.setAlignment(Label.CENTER);
		statusLabel.setSize(350, 100);
		
		userTracker = new UserTracker();
		
		this.add(headerLabel);
		this.add(userTracker);
		
		this.add(statusLabel);
		this.setVisible(true);
	}
	
	   // frame.dispose();
  
	
	
	private void showTopMenu() {
		// create a menu bar
		final MenuBar menuBar = new MenuBar();

		// create menus
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		final Menu aboutMenu = new Menu("About");

		// create menu items
		MenuItem newMenuItem = new MenuItem("New", new MenuShortcut(
				KeyEvent.VK_N));
		newMenuItem.setActionCommand("New");

		MenuItem openMenuItem = new MenuItem("Open");
		openMenuItem.setActionCommand("Open");

		MenuItem saveMenuItem = new MenuItem("Save");
		saveMenuItem.setActionCommand("Save");

		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setActionCommand("Exit");

		MenuItem cutMenuItem = new MenuItem("Cut");
		cutMenuItem.setActionCommand("Cut");

		MenuItem copyMenuItem = new MenuItem("Copy");
		copyMenuItem.setActionCommand("Copy");

		MenuItem pasteMenuItem = new MenuItem("Paste");
		pasteMenuItem.setActionCommand("Paste");

		MenuItemListener menuItemListener = new MenuItemListener();

		newMenuItem.addActionListener(menuItemListener);
		openMenuItem.addActionListener(menuItemListener);
		saveMenuItem.addActionListener(menuItemListener);
		exitMenuItem.addActionListener(menuItemListener);
		cutMenuItem.addActionListener(menuItemListener);
		copyMenuItem.addActionListener(menuItemListener);
		pasteMenuItem.addActionListener(menuItemListener);

		final CheckboxMenuItem showWindowMenu = new CheckboxMenuItem(
				"Show About", true);
		showWindowMenu.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (showWindowMenu.getState()) {
					menuBar.add(aboutMenu);
				} else {
					menuBar.remove(aboutMenu);
				}
			}
		});

		// add menu items to menus
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(showWindowMenu);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);

		// add menu to menubar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);

		// add menubar to the frame
		this.setMenuBar(menuBar);
		this.setVisible(true);
	}

	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			statusLabel.setText(e.getActionCommand() + " MenuItem clicked.");
		}
	}
}
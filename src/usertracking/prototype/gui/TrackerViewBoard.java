package usertracking.prototype.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import usertracking.prototype.classes.*;

public class TrackerViewBoard extends JFrame {

	private static final long serialVersionUID = 1L;

	private Label statusLabel;
	private SimpleTracker tracker;
	
	private SkeletonFrontFacade leftViewer;
	private SkeletonSideFacade centerViewer;
	private SkeletonTopFacade topViewer;
	private SkeletonTextInfoFacade infoViewer;
	
	JPanel leftPanel;
	JPanel centerPanel;
	JPanel rightPanel;
	JPanel bottomPanel;
	
	public TrackerViewBoard() {
		super("Tracker View Board");
		prepareGUI();
	}

	public static void main(String[] args) {
		TrackerViewBoard trackerViewBoard = new TrackerViewBoard();
		trackerViewBoard.showTopMenu();
		while (true) {
			trackerViewBoard.tracker.updateDepth();
			trackerViewBoard.leftViewer.repaint();
			trackerViewBoard.centerViewer.repaint();
			
			trackerViewBoard.topViewer.repaint();
			trackerViewBoard.infoViewer.repaint();
		}
	}

	private void prepareGUI() {

		this.setSize(1000, 820);
		this.setLayout(new BorderLayout());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//trackers
		tracker = new SimpleTracker();
		leftViewer = new SkeletonFrontFacade(tracker);
		centerViewer = new SkeletonSideFacade(tracker);
		
		topViewer = new SkeletonTopFacade(tracker);
		topViewer.setPreferredSize(new Dimension(1000, 400));
		
		infoViewer = new SkeletonTextInfoFacade(tracker);
		infoViewer.setPreferredSize(new Dimension(300, 250));
		// Main Panels

		// Panels
		leftPanel = new JPanel();
		leftPanel.setBackground(Color.gray);
		leftPanel.setPreferredSize(new Dimension(325, 250));

		leftPanel.setMinimumSize(new Dimension(325, 250));
		leftPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				Color.BLACK));
		leftPanel.add(leftViewer);
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.gray);
		centerPanel.setPreferredSize(new Dimension(325, 250));
		
		centerPanel.setMinimumSize(new Dimension(325, 250));
		centerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1,
				Color.BLACK));
		centerPanel.add(centerViewer);

		rightPanel = new JPanel();
		rightPanel.setBackground(Color.gray);
		rightPanel.setPreferredSize(new Dimension(300, 250));

		rightPanel.setMinimumSize(new Dimension(300, 250));
		rightPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1,
				Color.BLACK));
		rightPanel.add(infoViewer);

		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.gray);
		bottomPanel.setPreferredSize(new Dimension(1000, 400));
		bottomPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1,
				Color.BLACK));
		
		bottomPanel.add(topViewer);

		// Main window adding
		this.add(leftPanel, BorderLayout.LINE_START);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.LINE_END);
		this.add(bottomPanel, BorderLayout.PAGE_END);

		this.setVisible(true);
	}

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
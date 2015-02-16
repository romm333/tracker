package usertracking.prototype.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import usertracking.prototype.classes.*;
import usertracking.prototype.gui.views.SkeletonFrontView;
import usertracking.prototype.gui.views.SkeletonTextInfoView;
import usertracking.prototype.gui.views.SkeletonTopView;
import usertracking.prototype.profile.DummyObserver;
import usertracking.prototype.profile.UserKMeansProfileObserver;

public class TrackerViewBoardNew extends JFrame {

	private static final long serialVersionUID = 1L;

	private SimpleTracker tracker;

	private SkeletonTopView topViewer;
	private SkeletonFrontView frontViewer;
	private SkeletonTextInfoView infoViewer;

	JPanel leftPanel;
	JPanel middlePanel;
	JPanel rightPanel;

	public TrackerViewBoardNew() {
		super("Tracker View Board");
		prepareGUI();
	}

	public static void main(String[] args) {
		TrackerViewBoardNew trackerViewBoard = new TrackerViewBoardNew();
		trackerViewBoard.showTopMenu();
		while (true) {
			trackerViewBoard.tracker.updateDepth();
		}
	}

	private void prepareGUI() {

		this.setSize(1300, 600);
		this.setLayout(new BorderLayout());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// trackers
		tracker = new SimpleTracker();
		topViewer = new SkeletonTopView(tracker, 330, 590);
		tracker.addObserver(topViewer);
		
		frontViewer = new SkeletonFrontView(tracker);
		frontViewer.setSize(new Dimension(340, 590));
		frontViewer.setPreferredSize(new Dimension(340, 590));
		frontViewer.setMaximumSize(new Dimension(340, 590));
		tracker.addObserver(frontViewer);
		
		DummyObserver userProfileObserver = new DummyObserver(tracker);
		tracker.addObserver(userProfileObserver);
		
		infoViewer = new SkeletonTextInfoView(56, 34);
		infoViewer.setSize(new Dimension(590,600));
		// Main Panels

		// Panels
		leftPanel = new JPanel();
		leftPanel.setBackground(Color.gray);
		leftPanel.setPreferredSize(new Dimension(332, 600));

		leftPanel.setMinimumSize(new Dimension(332, 600));
		leftPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				Color.BLACK));
		leftPanel.add(topViewer);

		middlePanel = new JPanel();
		middlePanel.setBackground(Color.gray);
		middlePanel.setPreferredSize(new Dimension(342, 600));

		middlePanel.setMinimumSize(new Dimension(342, 600));
		middlePanel.setMaximumSize(new Dimension(342, 600));
		middlePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				Color.BLACK));
		middlePanel.setLayout(new BorderLayout(342, 600));
		middlePanel.add(frontViewer);

		JScrollPane scrollPane = new JScrollPane(infoViewer);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.gray);
		rightPanel.setPreferredSize(new Dimension(620, 600));

		rightPanel.setMinimumSize(new Dimension(620, 600));
		rightPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1,
				Color.BLACK));
	        
	     
		scrollPane.setMinimumSize(new Dimension(640, 600));
		rightPanel.add(scrollPane);
				
		// Main window adding
		this.add(leftPanel, BorderLayout.LINE_START);
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.LINE_END);
		
		this.setResizable(false);
		this.setVisible(true);
	}

	private void showTopMenu() {
		// create a menu bar
		final MenuBar menuBar = new MenuBar();

		// create menus
		Menu fileMenu = new Menu("File");
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

		
		MenuItemListener menuItemListener = new MenuItemListener();

		newMenuItem.addActionListener(menuItemListener);
		openMenuItem.addActionListener(menuItemListener);
		saveMenuItem.addActionListener(menuItemListener);
		exitMenuItem.addActionListener(menuItemListener);
		
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

		// add menu to menubar
		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);

		// add menubar to the frame
		this.setMenuBar(menuBar);
		this.setVisible(true);
	}

	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Exit")) {
				System.exit(0);
			}

		}
	}
}
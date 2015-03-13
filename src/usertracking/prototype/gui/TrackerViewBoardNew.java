package usertracking.prototype.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import usertracking.prototype.classes.*;
import usertracking.prototype.gui.views.SkeletonFrontView;
import usertracking.prototype.gui.views.SkeletonTextInfoView;
import usertracking.prototype.gui.views.SkeletonTopView;
import usertracking.prototype.profile.ProfileObserver;

public class TrackerViewBoardNew extends JFrame {

	private static final long serialVersionUID = 1L;

	private SimpleTracker tracker;

	private SkeletonTopView topViewer;
	private SkeletonFrontView frontViewer;
	private SkeletonTextInfoView infoViewer;

	JPanel leftPanel;
	JPanel middlePanel;
	JPanel rightPanel;
	JPanel controllPanel;
	JTextField profileCount;

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
		
		ProfileObserver userProfileObserver = new ProfileObserver(tracker);
		
		tracker.addObserver(userProfileObserver);
		infoViewer = new SkeletonTextInfoView(56, 30);
	
		// Main Panels

		// Panels
		leftPanel = new JPanel();
		leftPanel.setBackground(Color.black);
		leftPanel.setPreferredSize(new Dimension(332, 600));

		leftPanel.setMinimumSize(new Dimension(332, 600));
		
		TitledBorder tb = BorderFactory.createTitledBorder("Top View");
		tb.setTitleColor(Color.white);
		
		leftPanel.setBorder(tb);
		leftPanel.add(topViewer);

		middlePanel = new JPanel();
		middlePanel.setBackground(Color.black);
		middlePanel.setPreferredSize(new Dimension(342, 600));

		middlePanel.setMinimumSize(new Dimension(342, 600));
		middlePanel.setMaximumSize(new Dimension(342, 600));
		
		
		tb = BorderFactory.createTitledBorder("Front View");
		tb.setTitleColor(Color.white);
		middlePanel.setBorder(tb); 
		
		middlePanel.setLayout(new BorderLayout(342, 600));
		middlePanel.add(frontViewer);

		JScrollPane scrollPane = new JScrollPane(infoViewer);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Coordinates"));
		
		scrollPane.setPreferredSize(new Dimension(640,500));
		
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(620, 600));
		rightPanel.setLayout(new BorderLayout());
		
		rightPanel.setMinimumSize(new Dimension(620, 600));
		rightPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1,
				Color.BLACK));
	        
		controllPanel = new JPanel();
		controllPanel.setPreferredSize(new Dimension(640,50));
		controllPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
		
		 JCheckBox cbRecMode = new JCheckBox("Recording mode");
		 
		ItemListener recordingModeListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					tracker.setInRecordingMode(true);
				else
					tracker.setInRecordingMode(false);
			}
		};
		 
		cbRecMode.addItemListener(recordingModeListener);
		
		JCheckBox cbProfileMode = new JCheckBox("Profiling mode");
		profileCount = new JTextField("       2");
		JLabel lblPrCount = new JLabel("Profiles count");
		 
		ItemListener profileModeListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					int clusterCount = Integer.parseInt(profileCount.getText().trim());
					
					tracker.loadProfileData(clusterCount);
					tracker.setInProfilingMode(true);
				}
				else
					tracker.setInProfilingMode(false);
			}
		};
		 
		cbProfileMode.addItemListener(profileModeListener);
				
		controllPanel.add(cbRecMode);
		controllPanel.add(cbProfileMode);
		controllPanel.add(profileCount);
		controllPanel.add(lblPrCount);
		
		
		rightPanel.add(controllPanel, BorderLayout.NORTH);
		rightPanel.add(scrollPane, BorderLayout.SOUTH);
				
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
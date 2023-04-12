package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import javax.swing.JMenuBar;


public class MainWindow extends JFrame {
	
	// Is the default size for Content Panel
	// and not the default size for the window
	public static final int DEFAULT_HEIGHT = 600;
	public static final int DEFAULT_WIDTH = 600;
	
	GameFrame gameFrame;
	MenuBar menuBar;
	


	
	public MainWindow(String title) {
		this(title,MainWindow.DEFAULT_WIDTH,MainWindow.DEFAULT_HEIGHT);
	}
	
	public MainWindow(String title, int width, int height) {
		this.createGameFrame();
		this.add(gameFrame);
		this.setJFrameWindowConfiguration(title, width, height); // 
	}
	
	private void setJFrameWindowConfiguration (String title, int width, int height) {
		menuBar = new MenuBar();
		this.setJMenuBar(menuBar);
		
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout()); // For centering 
		// in the content pane 
		
		this.setResizable(true);
		this.getContentPane().setMinimumSize(new Dimension(width, height));
		this.getContentPane().setPreferredSize(new Dimension(width,height));
		this.pack();
		this.getContentPane().setBackground(new Color(34,65,87));
		this.requestFocus();
		this.setVisible(true);
	}
	
	
	private void createGameFrame() {
		gameFrame = new GameFrame();	
	}
	
}

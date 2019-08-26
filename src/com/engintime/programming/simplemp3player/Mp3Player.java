package com.engintime.programming.simplemp3player;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.swing.event.*;

import javazoom.jl.player.Player;

import java.util.Vector;

import javax.swing.*;

public class Mp3Player extends JFrame {
	private JButton jButtonStart;
	private JButton jButtonStop;
	private JList jListMusic; 
	private JMenuBar jMenuBarPlayer;
	private JMenu jMenuFile;
	private JMenuItem jMenuItemOpen;
	private JMenuItem jMenuItemExit;
	private JMenu jMenuPlay;
	private JMenuItem jMenuItemPrevious;
	private JMenuItem jMenuItemNext;
	private JPanel jPaneSouth;
	private JPanel jPaneControl;
	public static Mp3Player musicplayer = null;
	private FileInput fileinput = null;
	private Vector<String> playlist;
	private Play play = null;
	private States state = null;
	public Mp3Player()
	{
		super("MP3播放器");
		initComponents();
		jButtonStart.setEnabled(false);
		jButtonStop.setEnabled(false);
		playlist = new Vector<String>();
		state = new States(jButtonStart, jButtonStop);
	}

	private void initComponents() {
		jMenuBarPlayer = new JMenuBar();
		jMenuFile = new JMenu("File");
		jMenuItemOpen = new JMenuItem("Open");
		jMenuItemOpen.addActionListener(new ActionListener(){
	
			@Override
			public void actionPerformed(ActionEvent evt) {
				if(fileinput == null)
				{
					fileinput = new FileInput(musicplayer);
					fileinput.open();
					String[] list = fileinput.getFileNames();
					if(list != null)
					{
						for(int i = 0; i < list.length; i++)
						{
							playlist.add(list[i]);
							jListMusic.setListData(playlist);
						}
					}
					if(play == null)
					{				 
						play = new Play(playlist,  state);
						play.star();
					}
		
					jMenuItemPrevious.setEnabled(false);
					jMenuItemNext.setEnabled(false);
				}
				
			}
		});
		jMenuItemExit = new JMenuItem("Exit");
		jMenuItemExit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);			
			}
		});
		
		jMenuFile.add(jMenuItemOpen);
		jMenuFile.add(jMenuItemExit);
		jMenuPlay = new JMenu("Play"); 
		jMenuItemPrevious = new JMenuItem("Prev");
		jMenuItemPrevious.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				//
				// TODO: 在此添加代码
				//
				
			}
			
		});
		jMenuItemNext = new JMenuItem("Next");
		jMenuItemNext.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				//
				// TODO: 在此添加代码
				//
			
			}
			
		});
		
		jMenuItemPrevious.setEnabled(false);
		jMenuItemNext.setEnabled(false);
		jMenuPlay.add(jMenuItemPrevious);
		jMenuPlay.add(jMenuItemNext);
		jMenuBarPlayer.add(jMenuFile);
		jMenuBarPlayer.add(jMenuPlay);
		setJMenuBar(jMenuBarPlayer);
		jListMusic = new JList();
		jListMusic.setFixedCellHeight(20);
		jListMusic.setEnabled(false);

		jButtonStart = new JButton("Start");
		jButtonStart.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent evt) {
				state.setStart();		
			}		
		});
		
		jButtonStop = new JButton("Stop");
		jButtonStop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent evt) {
				state.setStop();			
			}			
		});
		
		

		//
		// TODO: 在此添加代码
		//
	
		
		jPaneSouth = new JPanel();
		jPaneSouth.setLayout(new GridLayout(2, 1));
		jPaneControl = new JPanel();
		jPaneControl.add(jButtonStart);

		jPaneControl.add(jButtonStop);
		jPaneSouth.add(jPaneControl);
		this.setBounds(200, 200, 1000, 1000);
		getContentPane().add(new JScrollPane(jListMusic),BorderLayout.CENTER);
		getContentPane().add(jPaneSouth, BorderLayout.SOUTH);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		pack();	
}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		try
		{
			musicplayer = new Mp3Player();
		}
		catch(Exception e)
		{
			System.out.println("null Point");
		}
		
		musicplayer.setVisible(true);	

	}

}
package com.engintime.programming.simplemp3player;
/* author
 *
 *
Akatsuki Sky
*
 *
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class Mp3Player extends JFrame {

	private JButton jButtonStart;
	private JButton jButtonStop;
	private JList<String> jListMusic;
	private JMenuItem jMenuItemPrevious;
	private JMenuItem jMenuItemNext;
	private Timer timer;
	private JSlider jSliderPlayProgress;
	private JSlider jSliderVolume;
	public static Mp3Player musicplayer = null;
	private FileInput fileinput = null;
	private final Vector<String> playlist;
	private Play play = null;
	private final States state;
	public Mp3Player()
	{
		super("MP3 Player");
		initComponents();
		jButtonStart.setEnabled(false);
		jButtonStop.setEnabled(false);
		playlist = new Vector<>();
		state = new States(jButtonStart, jButtonStop);
	}
	private void initComponents() {
		JMenuBar jMenuBarPlayer = new JMenuBar();
		JMenu jMenuFile = new JMenu("File");
		JMenuItem jMenuItemOpen = new JMenuItem("Open");
		jMenuItemOpen.addActionListener(evt -> {
			if(fileinput == null)
			{
				fileinput = new FileInput(musicplayer);
				fileinput.open();
				String[] list = fileinput.getFileNames();
				if(list != null)
				{
					for (String s : list) {
						playlist.add(s);
						jListMusic.setListData(playlist);
					}
				}
				if(play == null)
				{
					play = new Play(playlist,  state,jSliderVolume,jSliderPlayProgress);
					play.star();
				}
				if (playlist.size() == 1||playlist.size() == 0) {
					jMenuItemPrevious.setEnabled(false);
					jMenuItemNext.setEnabled(false);
				}

			}

		});
		JMenuItem jMenuItemExit = new JMenuItem("Exit");
		jMenuItemExit.addActionListener(evt -> System.exit(0));
		jMenuFile.add(jMenuItemOpen);
		jMenuFile.add(jMenuItemExit);
		JMenu jMenuPlay = new JMenu("Play");



		/*-------------------------prev-----------------------------**/
		jMenuItemPrevious = new JMenuItem("Prev");
		jMenuItemPrevious.addActionListener(e -> {
			if (state.getStart()){
				state.setStop();
			}
			if (playlist.size()>1){
				play.Previous();
			}
			else {
				System.out.println("it must more than 1 music");
			}
			try {
				Thread.sleep(500);
				state.setStart();

			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

		});
		/*-------------------------prev-----------------------------**/



		/*-------------------------next-----------------------------**/
		jMenuItemNext = new JMenuItem("Next");
		jMenuItemNext.addActionListener(e -> {
			if (state.getStart()){
				state.setStop();
			}
			if (playlist.size()>1){
				play.Next();
			}
			else {
				System.out.println("it must more than 1 music");
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			state.setStart();


		});
		/*-------------------------next-----------------------------**/



		jMenuItemPrevious.setEnabled(true);
		jMenuItemNext.setEnabled(true);
		jMenuPlay.add(jMenuItemPrevious);
		jMenuPlay.add(jMenuItemNext);
		jMenuBarPlayer.add(jMenuFile);
		jMenuBarPlayer.add(jMenuPlay);
		setJMenuBar(jMenuBarPlayer);


		/*-------------------------music list-----------------------------**/
		jListMusic = new JList<>();
		jListMusic.setFixedCellHeight(20);
		jListMusic.setEnabled(true);
		jListMusic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 1){
					timer = new Timer(350, e1 -> {
						System.out.println("You choose the NO."+(jListMusic.getSelectedIndex()+1)+" music,double click to play");
						play.Currindex = jListMusic.getSelectedIndex();
						timer.stop();
					});
					timer.restart();
				}
				else if (e.getClickCount() == 2&&timer.isRunning())
				{
					if (state.getStart()){
						state.setStop();
					}
					play.Currindex = jListMusic.getSelectedIndex();
					try {
						Thread.sleep(500);
						state.setStart();

					} catch (InterruptedException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					timer.stop();
				}
			}
		});
		/*-------------------------music list-----------------------------**/



		/*-------------------------pause-----------------------------**/
		JButton jButtonPause = new JButton("Pause/Continue");
		jButtonPause.addActionListener(evt -> play.setPause());
		/*-------------------------pause-----------------------------**/



		/*-------------------------volume-----------------------------**/
		jSliderVolume = new JSlider();
		jSliderVolume.setValue(0);
		jSliderVolume.addChangeListener(e -> {
			if (playlist.size() != 0)
				play.getVolume().setValue(jSliderVolume.getValue());
		});
		/*-------------------------volume-----------------------------**/



		/*-------------------------PlayProgress-----------------------------**/
		jSliderPlayProgress = new JSlider();
		jSliderPlayProgress.setOpaque(false);
		/*-------------------------PlayProgress-----------------------------**/



		/*-------------------------Start-----------------------------**/
		jButtonStart = new JButton("Start");
		jButtonStart.addActionListener(evt -> state.setStart());
		/*-------------------------Start-----------------------------**/



		/*-------------------------Stop-----------------------------**/
		jButtonStop = new JButton("Stop");
		jButtonStop.addActionListener(evt -> state.setStop());
		/*-------------------------Stop-----------------------------**/


		JTextArea jTextArea = new JTextArea("Volume");
		jTextArea.setOpaque(false);
		JPanel jPaneSouth = new JPanel();
		jPaneSouth.setLayout(new GridLayout(3, 1));
		JPanel jPaneControl = new JPanel();
		jPaneControl.add(jButtonStart);
		jPaneControl.add(jButtonPause);
		jPaneControl.add(jButtonStop);
		jPaneControl.add(jTextArea);
		jPaneControl.add(jSliderVolume);
		jPaneSouth.add(jSliderPlayProgress);
		jPaneSouth.add(jPaneControl);
		jSliderPlayProgress.setValue(0);
		this.setBounds(200, 200, 1000, 1000);
		getContentPane().add(new JScrollPane(jListMusic),BorderLayout.CENTER);
		getContentPane().add(jPaneSouth, BorderLayout.SOUTH);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(1366,768);
}
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
/* author
 *
 *
Akatsuki Sky
*
 *
 */
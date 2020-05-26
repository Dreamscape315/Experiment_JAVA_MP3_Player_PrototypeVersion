package com.engintime.programming.simplemp3player;


import java.io.*;
import java.util.Vector;

import javazoom.jl.player.Player;

import javax.swing.JOptionPane;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JSlider;

public class Play extends Thread {
	private  boolean paused = false;
	final Object lock = new Object();
	public  boolean getPause() {
		return paused;
	}
	public void setPause() {
		if (getPause()) {
			synchronized(lock) {
				paused = false;
				lock.notifyAll();
			}
			System.out.println("paused = false");
		}else {
			paused = true;
			System.out.println("paused = true");
		}

	}





	private File file = null;
	private States state = null;
	public int Currindex = 0;
	private FloatControl volume = null;
	private Vector<String> playlist = null;


	private SourceDataLine line = null;//line是sourcedata


	private JSlider jSliderPlayProgress;
	private JSlider jSliderVolume;




	private DataLine.Info info = null;
	private AudioFormat Format = null;
	private AudioInputStream audioInputStream = null;
	private long time = 0;
	public Play(Vector<String> playlist, 
			 States state,JSlider jSliderVolume,JSlider jSliderPlayProgress)
	{
		this.state = state;
		this.playlist = playlist;
		this.jSliderVolume = jSliderVolume;
		this.jSliderPlayProgress=jSliderPlayProgress;
	}
	public FloatControl getVolume()
	{
		return volume;
	}
	public void  setVolume() {
		if(line!=null)
		{
			if(line.isControlSupported(FloatControl.Type.MASTER_GAIN))
			{
				jSliderVolume.setEnabled(true);
				volume= (FloatControl)line.getControl( FloatControl.Type.MASTER_GAIN );
				jSliderVolume.setMinimum((int)volume.getMinimum());
				jSliderVolume.setMaximum((int)volume.getMaximum());
				volume.setValue((float)(volume.getMinimum()+(4*(volume.getMaximum()-volume.getMinimum()))/5));
			}
		}
		else
		{
			volume=null;
			jSliderVolume.setEnabled(false);
		}
	}


	public void init()
	{
		try{
			if(audioInputStream != null)
			{
				audioInputStream.close();
			}
			if(line != null)
			{
				line.close();
			}
			file = new File(playlist.get(Currindex));
			audioInputStream = AudioSystem.getAudioInputStream(file);
			// 获得音频格式
			Format = audioInputStream.getFormat();
			int bitrate = 0;
			
			if(Format.properties().get("bitrate") != null)
			{
				// 取得播放速度（单位：位每秒）
				bitrate = (int)((Integer)(Format.properties().get("bitrate")));
				if(bitrate != 0)
				{
					time = (file.length() * 8000000)/bitrate;
				}
			}

			jSliderPlayProgress.setMaximum((int)time);
			jSliderPlayProgress.setValue(0);

			// 音频格式转换
			Format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					Format.getSampleRate(),
					16,
					Format.getChannels(),
					Format.getChannels() * 2,
					Format.getSampleRate(),
					false);
			audioInputStream = AudioSystem.getAudioInputStream(Format, audioInputStream);
			

			info = new DataLine.Info(SourceDataLine.class, Format);
			try{
				line = (SourceDataLine)AudioSystem.getLine(info);
				line.open(Format);
				setVolume();
				line.start();	
			}
			catch(LineUnavailableException e)
			{
				e.printStackTrace();
			}


		}
		catch(Exception e)
		{
			e.printStackTrace();
			init();
		}
	}

	
	public int getCurrindex()
	{
		return Currindex;
	}
	
	public void Next()
	{
		if (Currindex+1<=playlist.size()-1){
		Currindex++;
		}
	}
	
	public void Previous()
	{
		if (Currindex - 1 >= 0) {
			Currindex--;
		}

		//
		// TODO: 在此添加代码
		//

	}
	
	public void star()
	{
		if(playlist.size() > 0)
		{
			init();
			state.setStart();
			this.start();
		}
	}
	
	public void run()
	{

		int nBytesRead = 0;
		byte[] abData = new byte[102400];
		while(true)
		{
			nBytesRead = 0;
			synchronized (lock) {
				while (nBytesRead != -1 && line != null) {
					while (getPause()){
						if(line.isRunning()) {
							line.stop();
						}
						try {
							lock.wait();
						}
						catch(InterruptedException e) {
						}
					}
					if(!line.isRunning()&&!getPause()) {
						System.out.println("Play");
						line.start();

					}
					try {
						nBytesRead = audioInputStream.read(abData, 0, abData.length);
						//System.out.println(nBytesRead+1);
					} catch (IOException e) {

						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//如果注释掉会一直循环，但是没有音频输出。

					if (state.getStop()) break;

					jSliderPlayProgress.setValue((int)line.getMicrosecondPosition());

					if (nBytesRead >= 0 && line != null) {
						//System.out.println(nBytesRead);
						line.write(abData, 0, nBytesRead);
					}

				}








				if (!state.getStop())//自动下一首
				{
					Currindex++;
					if (Currindex >= playlist.size()) {
						Currindex = 0;
					}
				}

				while (state.getStop()) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				init();
			}
		}
	}
	
}

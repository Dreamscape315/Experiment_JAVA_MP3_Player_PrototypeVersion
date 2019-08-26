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
	private File file = null;
	private States state = null;
	private int Currindex = 0;
	private FloatControl volume = null;
	private Vector<String> playlist = null;
	private SourceDataLine line = null;
	private DataLine.Info info = null;
	private AudioFormat Format = null;
	private AudioInputStream audioInputStream = null;
	private long time = 0;
	public Play(Vector<String> playlist, 
			 States state)
	{
		this.state = state;
		this.playlist = playlist;
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
	
	public FloatControl getVolume()
	{
		return volume;
	}
	
	public int getCurrindex()
	{
		return Currindex;
	}
	
	public void Next()
	{
		//
		// TODO: 在此添加代码
		//
	
	}
	
	public void Previous()
	{

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
			while(nBytesRead != -1 && line != null)
			{
				try {
					nBytesRead = audioInputStream.read(abData, 0, abData.length);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(state.getStop())break;

				if(nBytesRead >= 0 && line != null)
				{
					line.write(abData, 0, nBytesRead);
				}
			
			}
			if(!state.getStop())
			{
				Currindex++;
				if(Currindex >= playlist.size())
				{
					Currindex = 0;
				}
			}

			while(state.getStop())
			{
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

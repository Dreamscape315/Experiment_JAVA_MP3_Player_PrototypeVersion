package com.engintime.programming.simplemp3player;


import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

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


	private States state;
	public int Currindex = 0;
	private FloatControl volume = null;
	private Vector<String> playlist;


	private SourceDataLine line = null;//line是sourcedata


	private JSlider jSliderPlayProgress;
	private JSlider jSliderVolume;


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
				volume.setValue(volume.getMinimum()+(4*(volume.getMaximum()-volume.getMinimum()))/5);
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
			File file = new File(playlist.get(Currindex));
			audioInputStream = AudioSystem.getAudioInputStream(file);
			// 获得音频格式
			AudioFormat format = audioInputStream.getFormat();
			int bitrate;
			
			if(format.properties().get("bitrate") != null)
			{
				// 取得播放速度（单位：位每秒）
				bitrate = (Integer)(format.properties().get("bitrate"));
				if(bitrate != 0)
				{
					time = (file.length() * 8000000)/bitrate;
				}
			}

			jSliderPlayProgress.setMaximum((int)time);
			jSliderPlayProgress.setValue(0);

			// 音频格式转换
			format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					format.getSampleRate(),
					16,
					format.getChannels(),
					format.getChannels() * 2,
					format.getSampleRate(),
					false);
			audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);


			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			try{
				line = (SourceDataLine)AudioSystem.getLine(info);
				line.open(format);
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

		int nBytesRead;
		byte[] abData = new byte[102400];
		while(!state.getStop())
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
						catch(InterruptedException ignored) {

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
					synchronized (lock){
						try {
							lock.wait(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						lock.notifyAll();
					}
					/*try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
				init();
			}
		}
	}
	
}

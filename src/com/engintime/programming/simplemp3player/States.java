package com.engintime.programming.simplemp3player;

import javax.swing.JButton;

public class States {
	private boolean start;
	private boolean stop;
	private JButton jButtonStart;
	private JButton jButtonStop;
	public States(JButton jButtonStart, JButton jButtonStop)
	{
		start = true;
		stop = false;
		this.jButtonStart = jButtonStart;
		this.jButtonStop = jButtonStop;
	}
	public boolean getStart()
	{
		return start;
	}

	public boolean getStop()
	{
		return stop;
	}
	public void setStart()
	{
		if(start == false)
		{
			start = true;
		}

		if(stop == true)
		{
			stop = false;
		}
		jButtonStart.setEnabled(false);
		jButtonStop.setEnabled(true);
	}

	public void setStop()
	{
		if(start == true)
		{
			start = false;
		}

		if(stop == false)	
		{
			stop = true;
		}
		jButtonStart.setEnabled(true);
		jButtonStop.setEnabled(false);
	
	}
}

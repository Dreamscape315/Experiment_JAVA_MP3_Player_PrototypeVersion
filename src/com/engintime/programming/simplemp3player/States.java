package com.engintime.programming.simplemp3player;

import javax.swing.*;

public class States {
	private boolean start;
	private boolean stop;
	private final JButton jButtonStart;
	private final JButton jButtonStop;

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
		if(!start)
		{
			start = true;
		}

		if(stop)
		{
			stop = false;
		}
		jButtonStart.setEnabled(false);
		jButtonStop.setEnabled(true);
	}

	public void setStop()
	{
		if(start)
		{
			start = false;
		}

		if(!stop)
		{
			stop = true;
		}
		jButtonStart.setEnabled(true);
		jButtonStop.setEnabled(false);
	
	}



}

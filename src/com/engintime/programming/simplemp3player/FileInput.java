package com.engintime.programming.simplemp3player;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileInput {
	private final JFileChooser fdialog;
	private File[] f;
	private final JFrame jf;
	public FileInput(JFrame jf)
	{
		this.jf = jf;
		fdialog = new JFileChooser("D:");
		MyFileFilter my = new MyFileFilter();
		fdialog.setFileFilter(my);
		fdialog.setAcceptAllFileFilterUsed(false);
		fdialog.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fdialog.setMultiSelectionEnabled(true);
	}
	
	public void open()
	{
		f = null;
		int result = fdialog.showOpenDialog(jf);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			f = fdialog.getSelectedFiles();
		}
	}
	
	public String[] getFileNames()
	{
		String[] name = null;
		if(f != null)
		{
			name = new String[f.length];
			for(int i = 0; i < f.length; i++)
			{
				name[i] = f[i].getPath();
			}
		}
		return name;
	}
	
	public File[] getFiles()
	{
		return f;
	}

}

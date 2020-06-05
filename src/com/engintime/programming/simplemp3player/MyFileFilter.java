package com.engintime.programming.simplemp3player;
/* author
 *
 *
Akatsuki Sky
*
 *
 */
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())return true;
		return f.getName().endsWith(".mp3");
	
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return ".mp3";
	}

}
/* author
 *
 *
Akatsuki Sky
*
 *
 */
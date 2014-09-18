package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Album extends DisplayItem implements Serializable {	
    private static final long serialVersionUID = 4L;
	public String sort;
	public String pagi;
	public ArrayList<DisplayItem> items;
	
	public String toString(){
	    return "\n\nAlbum: begin \tsort:"+sort + " \tpagi:"+pagi + super.toString() +" \n\titems:"+items + "\n\tend\n\n\n";
	}
}

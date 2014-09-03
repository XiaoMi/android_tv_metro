package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Album extends DisplayItem implements Serializable {	
    private static final long serialVersionUID = 1L;
	public String sorti;
	public String pagei;
	public ArrayList<DisplayItem> items;
	
	public String toString(){
	    return "\n\nAlbum: begin \tsorti:"+sorti + " \tpagei:"+pagei + super.toString() +" \n\titems:"+items.toString() + "\n\tend\n\n\n";
	}
}

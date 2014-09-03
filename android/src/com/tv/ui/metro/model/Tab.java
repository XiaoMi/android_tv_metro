package com.tv.ui.metro.model;


import com.tv.ui.metro.utils.DateFormate;
import java.io.Serializable;
import java.util.Date;

public class Tab implements Serializable {
    private static final long serialVersionUID = 2L;
	public DisplayItem tab;
	public Album album;
    public long  update_time;
	
	public String toString(){
	    return "\n\nTAB: begin " +
                " update_time="+ DateFormate.dateToString(new Date(update_time)) +
                "\n\ttab:"+tab.toString() + "\n \talbum:"+album.toString() +"\n\t end\n\n\n";
	}
}

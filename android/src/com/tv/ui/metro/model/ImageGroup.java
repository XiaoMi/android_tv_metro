package com.tv.ui.metro.model;

import java.io.Serializable;

public class ImageGroup implements Serializable{	
    private static final long serialVersionUID = 1L;
    public Image back;
	public Image icon;
	public Image spirit;
	public Image text;
//	public Image thumb;
	
	public String toString(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("ImageGroup: \tback="+back);
	    sb.append(" \ticon="+icon);
        sb.append(" \tspirit="+spirit);        
        sb.append(" \ttext="+text);
//        sb.append(" \tthumb="+thumb);
	    
	    return sb.toString();
	}
}

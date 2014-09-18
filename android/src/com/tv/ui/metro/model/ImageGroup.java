package com.tv.ui.metro.model;

import java.io.Serializable;

public class ImageGroup implements Serializable{
    private static final long serialVersionUID = 4L;
    public Image back;
    public Image big;
    public Image fuzzy;
	public Image icon;
	public Image spirit;
	public Image text;
	public Image thumbnail;
    public Image screenshot;
	
	public String toString(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("ImageGroup: \tback="+back);
	    sb.append(" \ticon="+icon);
        sb.append(" \tbig="+big);
        sb.append(" \tfuzzy="+fuzzy);
        sb.append(" \tspirit="+spirit);        
        sb.append(" \ttext="+text);
        sb.append(" \tscreenshot="+screenshot);
        sb.append(" \tthumbnail="+thumbnail);
	    return sb.toString();
	}
}

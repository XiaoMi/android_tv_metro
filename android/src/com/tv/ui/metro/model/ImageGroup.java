package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.HashMap;

public class ImageGroup extends HashMap<String, Image> implements Serializable{
    private static final long serialVersionUID = 5L;
    public Image back(){return  get("back");}
    public Image big(){return get("big");}
    public Image fuzzy(){return get("fuzzy");}
	public Image icon(){return get("icon");}
	public Image spirit(){return get("spirit");}
	public Image text(){return get("text");}
	public Image thumbnail(){return get("thumbnail");}
    public Image screenshot(){return get("screenshot");}

    public ImageGroup clone(){
        ImageGroup item = new ImageGroup();
        if(back() != null)item.put("back", back().clone());
        if(big() != null)item.put("big",    big().clone());

        if(fuzzy() != null)item.put("fuzzy",    fuzzy().clone());
        if(icon() != null)item.put("icon",    icon().clone());
        if(spirit() != null)item.put("spirit",    spirit().clone());
        if(text() != null)item.put("text",    text().clone());
        if(thumbnail() != null)item.put("thumbnail",    thumbnail().clone());
        if(screenshot() != null)item.put("screenshot",    screenshot().clone());
        return item;
    }
	public String toString(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("ImageGroup: \tback="+back());
	    sb.append(" \ticon="+icon());
        sb.append(" \tbig="+big());
        sb.append(" \tfuzzy="+fuzzy());
        sb.append(" \tspirit="+spirit());
        sb.append(" \ttext="+text());
        sb.append(" \tscreenshot="+screenshot());
        sb.append(" \tthumbnail="+thumbnail());
	    return sb.toString();
	}
}

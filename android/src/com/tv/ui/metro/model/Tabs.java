package com.tv.ui.metro.model;

import com.tv.ui.metro.utils.DateFormate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Tabs implements Serializable{
    private static final long serialVersionUID = 2L;
	public ArrayList<Tab> tabs;
    public long           update_time;
	
	public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\ntabs: update_time="+DateFormate.dateToString(new Date(update_time)));
        sb.append("\n");
        for(Tab item:tabs){
            sb.append(item.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}

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

    public Album clone(){
        Album item = new Album();

        if(target != null)item.target = target.clone();
        item.ns   = this.ns;
        item.type = this.type;
        item.id   = this.id;
        item.name = this.name;
        if(images != null)item.images = this.images.clone();
        if(_ui != null)item._ui = this._ui.clone();
        if(times!= null)item.times = times.clone();

        item.sort = sort;
        item.pagi = pagi;
        item.items = new ArrayList<DisplayItem>();
        for (DisplayItem show: items){
            item.items.add(show.clone());
        }

        return item;
    }
}

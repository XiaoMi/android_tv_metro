package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuhuadong on 9/23/14.
 */
public class GenericAlbum<T>  extends DisplayItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public String sort;
    public String pagi;
    public ArrayList<T> items;

    public String toString(){
        return "\n\nAlbum: begin \tsort:"+sort + " \tpagi:"+pagi + super.toString() +" \n\titems:"+items + "\n\tend\n\n\n";
    }
}


package com.tv.ui.metro.model;


import java.io.Serializable;

public class DisplayItem  implements Serializable{	
    private static final long serialVersionUID = 1L;
    public static class UI implements  Serializable{		
        private static final long serialVersionUID = 1L;
        public static class Layout implements Serializable{            
            private static final long serialVersionUID = 1L;
            
            public int x;
            public int y;
			public int w;
			public int h;

            public String type;   //default U1x1
            public static final String U2x1 = "2x1";//2 W cell, 1 H cell
            //public static final String U2x2 = "2x2";//2x2
            public static final String U1x1 = "1x1";
            public static final String U1x2 = "1x2";
            
            public String toString(){
                return "x: "+x + " y:"+y + " w:"+w + " h:"+h  + " type:"+type;
            }

		}
		
        public static final String METRO_CELL_BANNER = "metro_cell_banner";
		public String type;
		public Layout layout;
		
		public String toString(){
            return " type:"+type + "  layout:"+layout;
        }
	}
	
	public String ns;
	public String type;
	public String id;
	public String name;
	public ImageGroup images;
	public UI _ui;
	
	public String toString(){
	    return " ns:" + ns + " type:"+type + " id:"+id + " name:"+name + "images:"+images.toString() + " _ui:"+_ui.toString();
	}
}

package com.tv.ui.metro.model;

import java.io.Serializable;

public class Image implements Serializable {
    private static final long serialVersionUID = 3L;
	public static class Position implements Serializable{		
        private static final long serialVersionUID = 1L;
        public int x;
		public int y;

        public Position clone(){
            Position item = new Position();
            item.x = x;
            item.y = y;
            return item;
        }
		public String toString(){
            return "x :"+x + "  y:"+y;
        }
	}

	public static class Size implements Serializable{
	    private static final long serialVersionUID = 1L;
		public int w;
		public int h;

        public Size clone(){
            Size item = new Size();
            item.w = w;
            item.h = h;
            return  item;
        }
		public String toString(){
		    return "w :"+w + "  h:"+h;
		}
	}

	public static class Ani implements Serializable {
		
		public static final int INTERPOLATOR_ACCELERATEDECELERATE = 0;
		public static final int INTERPOLATOR_ACCELERATE = 1;
		public static final int INTERPOLATOR_DECELERATE = 2;
		public static final int INTERPOLATOR_LINEAR = 3;
		public static final int INTERPOLATOR_BOUNCE = 5;
		
        private static final long serialVersionUID = 1L;
		public static class Translate implements Serializable {
            private static final long serialVersionUID = 1L;
			public int duration;
			public int startDelay;
			public int interpolator;
			public int x_delta;
			public int y_delta;

            public Translate clone(){
                Translate item = new Translate();

                item.duration = duration;
                item.startDelay = startDelay;
                item.interpolator = interpolator;
                item.x_delta = x_delta;
                item.y_delta = y_delta;
                return item;
            }
            public String toString(){
                return "translate: duration="+duration + " startDelay="+startDelay + " interpolator="+interpolator + " x_delta="+x_delta + " y_delta="+y_delta;
            }
		}

		public static class Scale implements Serializable {
            private static final long serialVersionUID = 1L;
			public int duration;
			public int startDelay;
			public int interpolator;
			public int pivotX;
			public int pivotY;
			public float scale_size;

            public Scale clone(){
                Scale item = new Scale();
                item.duration = duration;
                item.startDelay = startDelay;
                item.interpolator = interpolator;
                item.pivotX = pivotX;
                item.pivotY = pivotY;
                item.scale_size = scale_size;
                return  item;
            }
            public String toString(){
                return "Scale: duration="+duration + " startDelay="+startDelay + " interpolator="+interpolator + " pivotX="+pivotX + " pivotY="+pivotY + " scale_size="+scale_size;
            }
		}

		public Translate translate;
		public Scale     scale;

        public Ani clone(){
            Ani item = new Ani();

            if(translate!= null)item.translate = translate.clone();
            if(scale!= null)item.scale     = scale.clone();
            return item;
        }
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("Animation: \n\t");
            sb.append("\n\t scale: "+scale);
            sb.append("\n\t translate: "+translate );

            return  sb.toString();
        }
	}

    public String type="image";
	public String url;
	public String bgcolor;
	public Position pos;
	public Size size;
	public Ani ani;

    public Image clone(){
        Image item = new Image();
        item.url = url;
        item.type = type;
        item.bgcolor = bgcolor;
        if(pos != null)item.pos = pos.clone();
        if(size != null)item.size = size.clone();
        if(ani != null)item.ani = ani.clone();
        return  item;
    }
	public String toString(){
	    
        StringBuilder sb = new StringBuilder();
        sb.append("Image: \tbgcolor="+bgcolor);        
        
        sb.append(" \turl="+url);
        sb.append(" \tpos="+pos);
        sb.append(" \tsize="+size);
        sb.append(" \t type: "+type);
        sb.append(" \n\tani="+ani);
        
        return sb.toString();
	    
	}
}

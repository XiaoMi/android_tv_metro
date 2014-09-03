package com.tv.ui.metro.model;

import java.io.Serializable;

public class Image implements Serializable {
    private static final long serialVersionUID = 2L;
	public static class Position implements Serializable{		
        private static final long serialVersionUID = 1L;
        public int x;
		public int y;
		
		public String toString(){
            return "x :"+x + "  y:"+y;
        }
	}

	public static class Size implements Serializable{
	    private static final long serialVersionUID = 1L;
		public int w;
		public int h;
		
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
			
            public String toString(){
                return "Scale: duration="+duration + " startDelay="+startDelay + " interpolator="+interpolator + " pivotX="+pivotX + " pivotY="+pivotY + " scale_size="+scale_size;
            }
		}

		public Translate translate;
		public Scale scale;

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("Animation: \n\t");
            sb.append("\n\t scale: "+scale);
            sb.append("\n\t translate: "+translate);

            return  sb.toString();
        }
	}

	public String url;
	public String bgcolor;
	public Position pos;
	public Size size;
	public Ani ani;
	
	public String toString(){
	    
        StringBuilder sb = new StringBuilder();
        sb.append("Image: \tbgcolor="+bgcolor);        
        
        sb.append(" \turl="+url);
        sb.append(" \tpos="+pos);
        sb.append(" \tsize="+size);
        
        sb.append(" \n\tani="+ani);
        
        return sb.toString();
	    
	}
}

package com.tv.ui.metro.menu;


import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ViewSwitcher.ViewFactory;
import com.tv.ui.metro.view.TextViewWithTTF;

public class TxtViewFactoryMaker
{
    Context mContext;     //performance concern
    private static TxtViewFactoryMaker SMaker = new TxtViewFactoryMaker();
    
    public static TxtViewFactoryMaker getInstance()
    {
        return SMaker;
    }
    
    public void init(Context aContext)
    {
        mContext = aContext;
    }
    
    public TxtViewMaker getFactory(String aTTF, int aColor, float aTxtSize)
    {
       return  new TxtViewMaker(aTTF, aColor, aTxtSize, 1.0f);
    }
    
    public TxtViewMaker getFactory(String aTTF, int aColor, float aTxtSize, float alpha)
    {
       return  new TxtViewMaker(aTTF, aColor, aTxtSize, alpha);
    }
    
    public TxtViewMaker getFactory(String aTTF, int aColor, float aTxtSize, float alpha, float aRadius, float aDx, float aDy, int aShadowColor)
    {
       return  new ShadowTxtViewMaker(aTTF, aColor, aTxtSize, alpha, aRadius, aDx, aDx, aShadowColor);
    }
    
    
    class TxtViewMaker implements ViewFactory
    {
        private final String mTTF;
        private final int mColor;
        private final float mTxtSize;
        private final float mAlpha;
        private int mWidth = -1;
        
        public TxtViewMaker(String aTTF, int aColor, float aTxtSize, float alpha)
        {
            mTTF = aTTF;
            mColor = aColor;
            mTxtSize = aTxtSize;
            mAlpha = alpha;
        }
        
        public void setWidth(int aWidth)
        {
            mWidth = aWidth;
        }
        
        @Override
        public View makeView()
        {
            TextViewWithTTF res = new TextViewWithTTF(mContext, mTTF);
            res.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTxtSize);
            res.setTextColor(mColor);
            res.setAlpha(mAlpha);
            res.setLines(1);
            if(mWidth > 0)
            {
                res.setWidth(mWidth);
            }
            return res;
        }
    }
    
    
    class ShadowTxtViewMaker extends TxtViewMaker
    {
        private float mRadius;
        private float mOffsetX;
        private float mOffsetY;
        private int mShadowColor;
        
        public ShadowTxtViewMaker(String aTTF, int aColor, float aTxtSize,
                float alpha, float aRadius, float aDx, float aDy, int aShadowColor)
        {
            super(aTTF, aColor, aTxtSize, alpha);
            
            mRadius = aRadius;
            mOffsetX = aDx;
            mOffsetY = aDy;
            mShadowColor = aShadowColor;
        }
        
        public View makeView()
        {
            TextViewWithTTF res = (TextViewWithTTF) super.makeView();
            res.setShadowLayer(mRadius, mOffsetY, mOffsetX, mShadowColor);
            return res;
        }
    }
    
}
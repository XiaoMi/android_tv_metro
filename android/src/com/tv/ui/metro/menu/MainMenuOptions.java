package com.tv.ui.metro.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.tv.ui.metro.R;

public class MainMenuOptions
{
    Context mContext;
    private static final String TAG = "MainMenuOptions";
    private static int PlayerMenuItemCnt = 1; 
    public final static int[][] MenuItems =
        {
            {R.string.search, R.drawable.icon_menu_search_normal, R.drawable.icon_menu_search_normal, R.id.menu_item_search}, 
        };
    
    public MainMenuOptions(Context aContext)
    {
        mContext = aContext;
    }

    public int getCnt()
    {
        return PlayerMenuItemCnt;
    }
    
    public Drawable getIcon(int aIdx)
    {
        return mContext.getResources().getDrawable(MenuItems[aIdx][1]);
    }
    
    public String getText(int aIdx)
    {
        return mContext.getResources().getString(MenuItems[aIdx][0]);
    }
}

package com.tv.ui.metro.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher.ViewFactory;
import com.tv.ui.metro.*;

public class MainMenuController
{
    private static final String TAG = "MainMenuController";
    Context mContext;
    MainMenuOptions mOptions;
    MenuItemAdapter menuAdapter;
    public MainMenuController(Context aContext)
    {
        mContext = aContext;
        mOptions = new MainMenuOptions(aContext);
    }
    
    public boolean onMenuSelected(int aIdx)
    {
    	if(aIdx == R.id.menu_item_search) {
            Intent intent = new Intent("com.tv.ui.metro.action.SEARCH");
			mContext.sendBroadcast(intent);
	    }
			
    	return false;
    }

    public BaseAdapter getMenuAdapter()
    {
        menuAdapter = new MenuItemAdapter(); 
        return menuAdapter;
    }
    
    public MainMenuOptions getMenuOptions()
    {
        return mOptions;
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
 
    class MenuItemAdapter extends BaseAdapter
    {
        private ViewFactory menuTxtMaker;
        
        public MenuItemAdapter()
        {
        	 Resources res = mContext.getResources();
        	 menuTxtMaker = TxtViewFactoryMaker.getInstance().getFactory(
        			 res.getString(R.string.info_ttf), Color.WHITE, res.getDimensionPixelSize(R.dimen.menu_item_text_size));
        }

        @Override
        public int getCount()
        {
            return mOptions.getCnt();
        }

        @Override
        public Object getItem(int position)
        {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            android.util.Log.d(TAG, "getView position:" + position);
            LinearLayout menuItem;
            if (convertView == null) //don't reuse
            {

                menuItem = (LinearLayout) ((LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu_item, null);
                View icon = menuItem.findViewById(R.id.menu_icon);
                if(Build.VERSION.SDK_INT >= 16){
                    icon.setBackground(mOptions.getIcon(position));
                }else {
                    icon.setBackgroundDrawable(mOptions.getIcon(position));
                }
                TextSwitcher menuTxt = (TextSwitcher) menuItem.findViewById(R.id.menu_text);
                menuTxt.setFactory(menuTxtMaker);
                menuTxt.setAnimateFirstView(false);
                menuTxt.setText(mOptions.getText(position));
                menuItem.setId(position);
                if(mOptions.getCnt() - 1 == position)
                {
                    menuItem.findViewById(R.id.menu_divider).setVisibility(View.INVISIBLE);
                }
            } 
            else
            {
                menuItem = (LinearLayout) convertView;
                View icon = menuItem.findViewById(R.id.menu_icon);
                if(Build.VERSION.SDK_INT >= 16){
                    icon.setBackground(mOptions.getIcon(position));
                }else {
                    icon.setBackgroundDrawable(mOptions.getIcon(position));
                }
                TextSwitcher menuTxt = (TextSwitcher) menuItem.findViewById(R.id.menu_text);
                menuTxt.reset();
                menuTxt.setText(mOptions.getText(position));
                
                menuItem.setId(position);
                if(mOptions.getCnt() - 1 == position)
                {
                    menuItem.findViewById(R.id.menu_divider).setVisibility(View.INVISIBLE);
                }
                else
                {
                    menuItem.findViewById(R.id.menu_divider).setVisibility(View.VISIBLE);
                }
            }
            return menuItem;
        }

    }
}

package com.tv.ui.metro.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TabHost;

import com.tv.ui.metro.Utils;


public class MiTabHost extends TabHost {
    public MiTabHost(Context context) {
        super(context, null);
    }
    public MiTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if ( event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            Utils.playKeySound(this.getCurrentView(), Utils.SOUND_KEYSTONE_KEY);
            //ExpandStatusBarController.show(this.getContext());
            try{
                Intent intent = new Intent("com.xiaomi.mitv.ACTION_SHOW_STATUS_BAR");
                intent.putExtra("show", true);
                getContext().sendBroadcast(intent);
            }catch(Exception ne){}
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    
    
}

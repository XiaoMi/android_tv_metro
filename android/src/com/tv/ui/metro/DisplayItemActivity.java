package com.tv.ui.metro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tv.ui.metro.loader.BaseGsonLoader;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.view.EmptyLoadingView;

/**
 * Created by tv metro on 9/1/14.
 */
public class DisplayItemActivity extends FragmentActivity {

    protected DisplayItem item;
    protected EmptyLoadingView mLoadingView;
    protected BaseGsonLoader mLoader;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();

        item = (DisplayItem) this.getIntent().getSerializableExtra("item");

//        String itemid = data.getData().getQueryParameter("rid");
//        Toast.makeText(this, data.getData().toString() + " itemid="+itemid + " this="+this, Toast.LENGTH_LONG).show();
    }
    
    public static EmptyLoadingView makeEmptyLoadingView(Context context,  RelativeLayout parentView){
        return makeEmptyLoadingView(context, parentView,  RelativeLayout.CENTER_IN_PARENT);
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule){
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }
}

package com.xiaomi.mitv.store.game;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tv.ui.metro.MainActivity;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.view.RecommendCardView;
import com.tv.ui.metro.view.RecommendCardViewClickListenerFactory;
import com.tv.ui.metro.view.UserViewFactory;
import com.xiaomi.mitv.app.view.UserView;
import com.xiaomi.mitv.store.network.GameTabsGsonLoader;
import com.xiaomi.mitv.store.view.BluetoothView;

import java.util.ArrayList;
import com.tv.ui.metro.sampleapp.*;


public class GameMainActivity extends  MainActivity {
    private static final String TAG = GameMainActivity.class.getName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        
        RecommendCardViewClickListenerFactory.getInstance().setFactory(new RecommendCardViewClickListenerFactory.ClickCreatorFactory() {
            @Override
            public View.OnClickListener getRecommendCardViewClickListener() {
                return mRecommendCardViewClickListener;
            }
        });

        //please call this
        UserViewFactory.getInstance().setFactory(new UserViewFactory.ViewCreatorFactory(){
            @Override
            public ArrayList<View> create(Context context) {
                ArrayList<View> views = new ArrayList<View>();
                views.add(new UserView(context, getResources().getString(R.string.account_info)));
                views.add(new BluetoothView(context, getResources().getString(R.string.user_handler_helper)));
                return  views;
            }

            @Override
            public int getPadding(Context context) {
                return getResources().getDimensionPixelSize(R.dimen.user_view_padding);
            }
        });
    }
       //please override this fun
    protected void createTabsLoader(){
        mLoader = new GameTabsGsonLoader(this, null);
    }
    
    View.OnClickListener mRecommendCardViewClickListener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            if(RecommendCardView.class.isInstance(v)){
                RecommendCardView rcv = (RecommendCardView)v;               
                DisplayItem item = rcv.getContentData();
                if (null != item) {
                    
                    DisplayItem.Target target = item.target;
                    if (null != target) {
                        if (target.type.equals("item")) {
                        } else if (target.type.equals("album")) {
                        } else if (target.type.equals("billboard")) {
                        } else if (target.type.equals("category")) {
                        } else {                            
                        }
                        
                        
                            
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("micontent://" + item.ns + "/" + item.type + "?rid="
                                + item.id));
                        intent.putExtra("item", item);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                    }
                    
                }
            }           
        }
    };
}
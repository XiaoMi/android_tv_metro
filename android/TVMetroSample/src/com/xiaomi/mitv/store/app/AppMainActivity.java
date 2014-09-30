package com.xiaomi.mitv.store.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tv.ui.metro.MainActivity;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.sampleapp.R;
import com.tv.ui.metro.view.RecommendCardView;
import com.tv.ui.metro.view.RecommendCardViewClickListenerFactory;
import com.tv.ui.metro.view.UserViewFactory;
import com.xiaomi.mitv.app.view.UserView;
import com.xiaomi.mitv.store.network.AppTabsGsonLoader;

import java.util.ArrayList;


public class AppMainActivity extends MainActivity {
	private static final String TAG = AppMainActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
        RecommendCardViewClickListenerFactory.getInstance().setFactory(new RecommendCardViewClickListenerFactory.ClickCreatorFactory() {
            @Override
            public View.OnClickListener getRecommendCardViewClickListener() {
                return mRecommendCardViewClickListener;
            }
        });
	}


	   //please override this fun
    protected void createTabsLoader(){
        mLoader = new AppTabsGsonLoader(this, null);
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
    
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");		
	}
    
	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
}

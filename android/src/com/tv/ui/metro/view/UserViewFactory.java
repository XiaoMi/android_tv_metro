package com.tv.ui.metro.view;

import android.content.Context;
import android.view.View;
import com.tv.ui.metro.R;
import com.xiaomi.mitv.app.view.UserView;

import java.util.ArrayList;

/**
 * Created by tv metro on 9/3/14.
 */
public class UserViewFactory {
    private static UserViewFactory _instance;

    private UserViewFactory(){}
    public static UserViewFactory getInstance(){
        if(_instance == null)
            _instance = new UserViewFactory();

        return _instance;
    }

    ViewCreatorFactory mFactory = new DefautUserViewCreateFactory();

    public void setFactory(ViewCreatorFactory _factory){
        mFactory = _factory;
    }

    public ArrayList<View>  createUserView(Context context){
        return mFactory.create(context);
    }

    public int getPadding(Context context){
        int padding =  mFactory.getPadding(context);
        if(padding == 0){
            padding = context.getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        }
        return padding;
    }

    public interface ViewCreatorFactory{
        ArrayList<View> create(Context context);
        int             getPadding(Context context);
    }

    public class DefautUserViewCreateFactory implements ViewCreatorFactory{
        @Override
        public ArrayList<View> create(Context context) {
            ArrayList<View> views = new ArrayList<View>();
            views.add(new UserView(context, "default"));
            views.add(new UserView(context, "default"));
            return  views;
        }

        @Override
        public int getPadding(Context context) {
            return context.getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        }
    }

}

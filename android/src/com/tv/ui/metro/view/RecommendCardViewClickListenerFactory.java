package com.tv.ui.metro.view;

import android.view.View;

/**
 * Created by tv metro on 9/5/14.
 *
 */
public class RecommendCardViewClickListenerFactory {
    private static RecommendCardViewClickListenerFactory _instance;

    private RecommendCardViewClickListenerFactory(){}
    public static RecommendCardViewClickListenerFactory getInstance(){
        if(_instance == null)
            _instance = new RecommendCardViewClickListenerFactory();

        return _instance;
    }

    ClickCreatorFactory mFactory = new DefautFactory();
    public void setFactory(ClickCreatorFactory _factory){
        mFactory = _factory;
    }


    public View.OnClickListener getRecommendCardViewClickListener(){
        return mFactory.getRecommendCardViewClickListener();
    }

    public interface ClickCreatorFactory{
        View.OnClickListener getRecommendCardViewClickListener();

    }

    public class DefautFactory implements ClickCreatorFactory{
        @Override
        public View.OnClickListener getRecommendCardViewClickListener() {
            return null;
        }
    }
}

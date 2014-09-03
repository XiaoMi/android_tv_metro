package com.tv.ui.metro.menu;

import android.util.Log;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.widget.ListView;

interface MenuListListener
{
    public void onMenuItemSelected(int aID);
    public void onMenuPressed();
    public void onBackPressed();
    public void onRightPressed();
    public void onLeftPressed();
    public void onMenuMoved(int aTgtPos);
    public void onMenuMoving();
    public boolean isReady();
}

class MenuListController implements OnKeyListener, AnimationListener
{
    private final static String TAG = "MenuListController";
    public static final int KAnimTimeShort = 180;
    private final static long AnimLockTimer = KAnimTimeShort * 4; 
    private ListView mListView;
    private MenuListListener menuListListener;
    private int mLastFocusPosition;
    private final int mTopSpace;
    
    private LayoutAnimationController mUpAnim;
    private LayoutAnimationController mDownAnim;
    private TransientBoolean mAnimComplete;  //android can't handle replace LayoutAnimationController at run time use this as a hack
    
    public MenuListController(ListView aListView, MenuListListener aMenuListListener, int aTopSpace)
    {
        mTopSpace = aTopSpace;
        mListView = aListView;
        mLastFocusPosition = mListView.getCheckedItemPosition();
        menuListListener = aMenuListListener;
        Interpolator inp = new AccelerateDecelerateInterpolator();
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
                );
        anim.setDuration(KAnimTimeShort);
        anim.setInterpolator(inp);
        mUpAnim = new LayoutAnimationController(anim, 0.1f);

        anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
                );
        anim.setDuration(KAnimTimeShort);
        anim.setInterpolator(inp);
        mDownAnim = new LayoutAnimationController(anim, 0.1f);
        mDownAnim.setOrder(LayoutAnimationController.ORDER_REVERSE);
        mAnimComplete = new TransientBoolean(true);
    }
    
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        boolean res = true;
        boolean playErr = false;
       
        if(KeyEvent.ACTION_DOWN == event.getAction())
        {
            if(menuListListener.isReady())
            {
                mLastFocusPosition = mListView.getCheckedItemPosition();
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_DOWN: //press  keydown view go up do up animation
                    {
                        final int selItemsCnt = mListView.getCount() - mListView.getHeaderViewsCount() - mListView.getFooterViewsCount();
                        if(mLastFocusPosition < selItemsCnt)
                        {
    //                        menuListListener.onMenuMoving();
                            mAnimComplete.autoSetVal(true, AnimLockTimer);
                            mListView.setLayoutAnimation(mUpAnim);
                            mListView.scheduleLayoutAnimation();
                            mListView.setLayoutAnimationListener(this);
                            mListView.setItemChecked(++mLastFocusPosition, true);
                            mListView.setSelectionFromTop(mLastFocusPosition, mTopSpace);
                            menuListListener.onMenuMoved(mLastFocusPosition - mListView.getHeaderViewsCount());
                        }
                        else
                        {
                            playErr = true;
                        }
                    }
                    break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                    {
                        if(mLastFocusPosition > mListView.getHeaderViewsCount())
                        {
    //                        menuListListener.onMenuMoving();
                            mAnimComplete.autoSetVal(true, AnimLockTimer);
                            mListView.setLayoutAnimation(mDownAnim);
                            mListView.scheduleLayoutAnimation();
                            mListView.setLayoutAnimationListener(this);
                            mListView.setItemChecked(--mLastFocusPosition, true);
                            mListView.setSelectionFromTop(mLastFocusPosition, mTopSpace);
                            menuListListener.onMenuMoved(mLastFocusPosition - mListView.getHeaderViewsCount());
                        }
                        else
                        {
                            playErr = true;
                        }
                    }
                    break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if(mAnimComplete.getValue())
                        {
                            menuListListener.onLeftPressed();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if(mAnimComplete.getValue())
                        {
                            menuListListener.onRightPressed();
                        }
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        if(mAnimComplete.getValue())
                        {
                        //  menuListListener.onMenuItemSelected(mListView.getCheckedItemPosition() - mListView.getHeaderViewsCount());
                        	int idx = mListView.getCheckedItemPosition() - mListView.getHeaderViewsCount();
                        	int menuId = MainMenuOptions.MenuItems[idx][3];
                            menuListListener.onMenuItemSelected(menuId);
                        }
                        break;
                    case KeyEvent.KEYCODE_MENU:
                        mListView.clearAnimation();
                        if(mAnimComplete.getValue() && 0 == event.getRepeatCount())
                        {
                            menuListListener.onMenuPressed();
                        }
                        else
                        {
                            res = false;
                        }
                        break;
                    case KeyEvent.KEYCODE_BACK:
                        if(mAnimComplete.getValue())
                        {
                            menuListListener.onBackPressed();
                        }
                        break;
                    default:
                        res = false;
                        break;
                }
            }
            else
            {
                Log.d(TAG, "listener is not ready ignore the event");
                res = false;
            }
            
            if(playErr)
            {
                mListView.playSoundEffect(5);
            }
            else if(res)
            {
                mListView.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(View.FOCUS_UP));
            }
        }
        else 
        {
//            res = false;
        }
       
        
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
      //nothing to do just avoid confuse the list fold and expand anim
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        //nothing to do just avoid confuse the list fold and expand anim
        mAnimComplete.setValue(true);
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {
      //nothing to do just avoid confuse the list fold and expand anim
        
    }
}



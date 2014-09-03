package com.tv.ui.metro.menu;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
import com.tv.ui.metro.*;

public class MainMenuMgr implements MenuListListener, ViewFactory
{
    private final static String TAG = MainMenuMgr.class.getName();

    private final static float LayoutAnimDelay = 0.5f;
    public static final Point WinSize = new Point(1920, 1080);
    public static final int KAnimTimeShort = 180;
    private final static long AnimationBlockTimer = KAnimTimeShort * 4;
    
    private FrameLayout mainMenu;
    private ListView  mainMenuItems;

    private LayoutAnimationController mAnimIn;
    private LayoutAnimationController mAnimOut;
    private HideShowListener mHideShowListener;

    // focus anim bgn
    private FocusListener menuFocusListener;
    /*private*/ Animation[] mFocusAnims = new Animation[2]; 
    // focus anim end    
    // confirm anim bgn
    /*private*/ Animation[] mConfirmAnims = new Animation[4];
    // confirm anim bgn
    
    /*private*/ ImageSwitcher mCursor;
    private TransientBoolean mReady;
    
    ValueAnimator [] mCursorMov = new ValueAnimator[2];
    ValueAnimator [] mArrowMov = new ValueAnimator[2];
    AnimatorSet mCursorConfirm;
    AnimatorSet mArrowConfirm;
    
    MainMenuController mainMenuController;
    MainMenuOptions mOptions;
    
    OnMenuCancelListener mMenuCancelListener;
    
    public interface OnMenuCancelListener {
    	public void onMenuCancel();
    }
    
    public MainMenuMgr(Context context, FrameLayout aMenuParent, OnMenuCancelListener menuCancelListener)
    {
    	mMenuCancelListener = menuCancelListener;
    	TxtViewFactoryMaker.getInstance().init(context);;
        mainMenuController = new MainMenuController(context);
        mainMenu = (FrameLayout) View.inflate(context, R.layout.main_menu, aMenuParent);
        ViewGroup.LayoutParams lpMenu = new LayoutParams(WinSize.x/2, WinSize.y, Gravity.LEFT);
        if( aMenuParent.getParent() instanceof RelativeLayout) {
        	RelativeLayout.LayoutParams rlpMenu = new RelativeLayout.LayoutParams(WinSize.x/2, WinSize.y);
        	rlpMenu.addRule(RelativeLayout.ALIGN_LEFT);
        	rlpMenu.addRule(RelativeLayout.CENTER_VERTICAL);
        	lpMenu = rlpMenu;
        }
        mainMenu.setLayoutParams(lpMenu);

        mCursor = (ImageSwitcher)mainMenu.findViewById(R.id.cursor);
        mCursor.setFactory(this);
        mCursor.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
        mCursor.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out));
        mCursor.setImageResource(R.drawable.menu_highlight);

        mReady = new TransientBoolean(true);

        mainMenuItems = (ListView)mainMenu.findViewById(R.id.main_menu_items);
        Space header = new Space(mainMenuItems.getContext());
        header.setMinimumHeight(WinSize.y);
        mainMenuItems.addHeaderView(header, null, false);
        Space footer = new Space(mainMenuItems.getContext());
        footer.setMinimumHeight(WinSize.y);
        mainMenuItems.addFooterView(footer, null, false);
        mainMenuItems.setAdapter(mainMenuController.getMenuAdapter());
        mOptions = mainMenuController.getMenuOptions();
        mainMenuItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mainMenuItems.setItemChecked(0 + mainMenuItems.getHeaderViewsCount(), true);
        int menuToSpace = context.getResources().getDimensionPixelSize(R.dimen.menu_top_space);
        mainMenuItems.setSelectionFromTop(0 + mainMenuItems.getHeaderViewsCount(), menuToSpace);
        mainMenuItems.setOnKeyListener(new MenuListController(mainMenuItems, this, menuToSpace));
        mainMenuItems.setFocusable(true);
        mainMenuItems.setFocusableInTouchMode(true);

        View menuPanel = mainMenu.findViewById(R.id.main_menu_panel);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        menuPanel.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        View maskTop = mainMenu.findViewById(R.id.mask_top);
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        maskTop.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        View maskBottom = mainMenu.findViewById(R.id.mask_bottom);
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        maskBottom.setLayerType(View.LAYER_TYPE_HARDWARE, paint);

        intLayoutAnim();
        buildFocusAnimations();
    }

    private void updatePreFocusView(ViewGroup aParent)
    {
        View focused = null;
        View cursor = aParent.getFocusedChild();
        while(cursor != null && cursor instanceof ViewGroup)
        {
            focused = cursor;
            cursor = ((ViewGroup)cursor).getFocusedChild();
        }
    //  UIConfig.DesktopState.setLastFocus(focused);
    }


    private void buildFocusAnimations()
    {
        menuFocusListener = new FocusListener();
        mFocusAnims[0] = AnimationUtils.loadAnimation(mainMenu.getContext(), android.R.anim.fade_out);
        mFocusAnims[1] = AnimationUtils.loadAnimation(mainMenu.getContext(), android.R.anim.fade_in);
        mFocusAnims[0].setDuration(KAnimTimeShort / 2);
        mFocusAnims[0].setFillAfter(true);
        mFocusAnims[0].setAnimationListener(menuFocusListener);
        mFocusAnims[1].setDuration(KAnimTimeShort / 2);
    }

    private void intLayoutAnim()
    {
        Animation slideIn = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, -1, TranslateAnimation.ABSOLUTE, 0,
                TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);
        slideIn.setDuration(KAnimTimeShort);
        mAnimIn = new LayoutAnimationController(slideIn, LayoutAnimDelay);

        Animation slideOut = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.RELATIVE_TO_SELF, -1,
                 TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);
        slideOut.setDuration(KAnimTimeShort);

        slideOut.setFillAfter(true);
        mAnimOut = new LayoutAnimationController(slideOut, LayoutAnimDelay);
        mAnimOut.setOrder(LayoutAnimationController.ORDER_REVERSE);

        mainMenu.setLayoutAnimation(mAnimIn);
        mHideShowListener = new HideShowListener();
        mainMenu.setLayoutAnimationListener(mHideShowListener);

        mReady.autoSetVal(true, AnimationBlockTimer);
    }


    public void showMenu(FrameLayout aMenuParent)
    {
        Log.d(TAG, "isInTouchMode " + mainMenuItems.isInTouchMode());
        mainMenuController.getMenuAdapter().notifyDataSetChanged();
        mReady.autoSetVal(true, AnimationBlockTimer);
        mainMenu.setVisibility(View.VISIBLE);
        mainMenu.setLayoutAnimation(mAnimIn);
        mainMenu.startLayoutAnimation();
        mainMenu.setLayoutAnimationListener(mHideShowListener);
        mainMenuItems.requestFocus();
    }


    private void hideMenu(boolean aDoAnim, boolean aShowHomeFocus)
    {
    	mainMenuItems.clearFocus();
        if(aDoAnim)
        {
            mReady.autoSetVal(true, AnimationBlockTimer);
            mainMenu.setLayoutAnimation(mAnimOut);
            mainMenu.startLayoutAnimation();
            mainMenu.setLayoutAnimationListener(mHideShowListener);
        }
        else
        {
            mainMenu.setVisibility(View.GONE);
        }

        if(aShowHomeFocus)
        {
     //       mHomeCursor.setVisibility(View.VISIBLE);
        }
    }

    public void hideMenu()
    {
        hideMenu(true, true);
    }

    @Override
    public void onRightPressed()
    {
    	int idx = mainMenuItems.getCheckedItemPosition() - mainMenuItems.getHeaderViewsCount();
    	int menuId = MainMenuOptions.MenuItems[idx][3];
        onMenuItemSelected(menuId);
    }

    @Override
    public void onMenuItemSelected(int aID)
    {
        Log.d(TAG, "onMenuItemSelected " + aID);
        boolean showHomeCursor = mainMenuController.onMenuSelected(aID);
        hideMenu(showHomeCursor, showHomeCursor);
    }


    @Override
    public void onMenuPressed()
    {
        hideMenu();
    }

    @Override
    public void onBackPressed()
    {
        hideMenu();
        if ( mMenuCancelListener != null) {
        	mMenuCancelListener.onMenuCancel();
		}
    }

    @Override
    public void onLeftPressed()
    {
        onBackPressed();
    }


    class HideShowListener implements AnimationListener
    {

        @Override
        public void onAnimationStart(Animation animation)
        {
            Log.d(TAG, "HideShowListener onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            Log.d(TAG, "HideShowListener onAnimationEnd " + animation);
            if(mainMenu.getLayoutAnimation() == mAnimOut)
            {
                mainMenu.setVisibility(View.GONE);
            }
            mReady.setValue(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
            Log.d(TAG, "HideShowListener onAnimationRepeat");
        }
    }

    class FoldListener implements AnimationListener
    {

        @Override
        public void onAnimationStart(Animation animation)
        {
            Log.d(TAG, "FoldListener onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            Log.d(TAG, "FoldListener onAnimationEnd " + animation);
            mReady.setValue(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
            Log.d(TAG, "FoldListener onAnimationRepeat");
        }

    }

    @Override
    public void onMenuMoved(int aTgtPos)
    {
        // TODO Auto-generated method stub
        Log.d(TAG, "onMenuMov");
        mCursor.startAnimation(mFocusAnims[0]);
    }

    @Override
    public void onMenuMoving()
    {
//        ((ViewGroup)mainMenuItems.getSelectedView()).findViewById(R.id.menu_icon)
//        .setBackgroundResource(MenuItems[mainMenuItems.getSelectedItemPosition() - mainMenuItems.getHeaderViewsCount()][1]);
    }

    @Override
    public boolean isReady()
    {
        return mReady.getValue();
    }

    @Override
    public View makeView()
    {
        ImageView img = new ImageView(mainMenu.getContext());
//        img.setBackgroundColor(0xFF000000);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        return img;
    }

    class FocusListener implements AnimationListener
    {
        @Override
        public void onAnimationStart(Animation animation)
        {
        }
        
        @Override
        public void onAnimationRepeat(Animation animation)
        {
        }
        
        @Override
        public void onAnimationEnd(Animation animation)
        {
            mCursor.startAnimation(mFocusAnims[1]);
        }
    }

    public void hide()
    {
        hideMenu();
    }
    
}
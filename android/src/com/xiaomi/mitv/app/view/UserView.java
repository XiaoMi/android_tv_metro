package com.xiaomi.mitv.app.view;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.tv.ui.metro.R;
import com.tv.ui.metro.view.MineView;

/**
 * Created by tv metro on 9/2/14.
 */
public class UserView extends MineView {

    public UserView(Context context, String title) {
        super(context);

        _activity = (Activity)(context);

        initUI();
        setItemTitle(title);
        setItemSummary(getResources().getString(R.string.mine_info_account_summary_none));
    }

    private String         mXiaomiId;
    private Activity       _activity;

    public Activity getActivity(){
        return _activity;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initUI();
    }

    private void initUI(){
        setBackgroundResource(R.drawable.mine_account);
        setLogoPhoto(R.drawable.account_photo_normal);
        setFocusable(true);
        this.onCreateView(_activity);

        requestLayout();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if(visibility == View.VISIBLE){
            this.onResume();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.onStop();
        this.onDestroy();
    }

    @Override
    public void onCreateView(Activity activity){
        _activity = activity;
    }
}

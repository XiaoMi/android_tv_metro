package com.xiaomi.mitv.app;


import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import com.tv.ui.metro.DisplayItemActivity;
import com.tv.ui.metro.R;
import com.tv.ui.metro.loader.ItemLoader;
import com.tv.ui.metro.model.Item;
import com.xiaomi.mitv.app.view.TitleBar;

/**
 * Created by tv metro on 8/28/14.
 */
public class GameDetailActivity extends DisplayItemActivity implements LoaderManager.LoaderCallbacks<Item> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_game_detail);

        TitleBar tb = (TitleBar) this.findViewById(R.id.detail_title_bar);
        tb.setTitle(item.name);

        tb.setBackPressListner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameDetailActivity.this.finish();
            }
        });

        getSupportLoaderManager().initLoader(ItemLoader.GAME_LOADER_ID, null, this);
    }


    @Override
    public Loader<Item> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Item> tabsLoader, Item tabs) {

    }

    @Override
    public void onLoaderReset(Loader<Item> tabsLoader) {

    }

}

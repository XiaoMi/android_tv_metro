package com.xiaomi.mitv.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import com.tv.ui.metro.AlbumActivity;
import com.tv.ui.metro.R;
import com.tv.ui.metro.model.Album;
import com.xiaomi.mitv.app.view.TitleBar;

/**
 * Created by tv metro on 9/1/14.
 */
public class GameAlbumActivity extends AlbumActivity implements LoaderManager.LoaderCallbacks<Album>{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.album_game_layout);

        TitleBar tb = (TitleBar) this.findViewById(R.id.title_bar);
        tb.setTitle(item.name);

        tb.setBackPressListner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameAlbumActivity.this.finish();
            }
        });
    }

    @Override
    public Loader<Album> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Album> albumLoader, Album album) {

    }

    @Override
    public void onLoaderReset(Loader<Album> albumLoader) {

    }
}

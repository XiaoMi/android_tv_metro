package com.tv.ui.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by tv metro on 9/1/14.
 */
public class DisplayItemActivity extends FragmentActivity {

    protected DisplayItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();

        item = (DisplayItem) this.getIntent().getSerializableExtra("item");

        String itemid = data.getData().getQueryParameter("rid");
        Toast.makeText(this, data.getData().toString() + " itemid="+itemid + " this="+this, Toast.LENGTH_LONG).show();
    }
}

package com.xiaomi.mitv.store.view;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import com.tv.ui.metro.view.MainHandler;
import com.tv.ui.metro.view.MineView;
import com.tv.ui.metro.sampleapp.R;
import com.xiaomi.mitv.store.bluetooth.BluetoothReceiver;
import com.xiaomi.mitv.store.bluetooth.OnBluetoothHandlerListener;
import com.xiaomi.mitv.store.bluetooth.OnFindHanlderCallback;

import java.util.ArrayList;
import com.tv.ui.metro.sampleapp.R;
import com.xiaomi.mitv.store.game.HandlerHelperActivity;

/**
 * Created by game center on 9/9/14.
 */
public class BluetoothView extends MineView implements OnBluetoothHandlerListener, OnFindHanlderCallback {
    private Object mHandlerObject = new Object();
    private ArrayList<String> mHandlerList = new ArrayList<String>(8);
    private BluetoothReceiver mBlueReceiver;
    private IntentFilter mBluetoothIntentFilter;
    private OnBluetoothHandlerListener listener;

    public BluetoothView(Context context, String title) {
        super(context);

        initUI();

        setItemTitle(title);
        setItemSummary(getResources().getString(R.string.handle_connect_none));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initUI();
    }

    private void initUI(){
        setBackgroundResource(R.drawable.mine_handler);
        setItemTitle(R.string.mine_info_handle_manager_title);
        setItemSummary(R.string.handle_connect_none);
        setFocusable(true);

        setOnClickListener(clickListener);

        this.listener = this;
        mBlueReceiver = new BluetoothReceiver(listener);
        mBluetoothIntentFilter = new IntentFilter("android.bluetooth.input.profile.action.HID_INFO");
        mBluetoothIntentFilter.addAction("android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED");

        synchronized (mHandlerObject) {
            mHandlerList.clear();
        }
        //TODO
        //new HandlerCheck(getContext(), this);
        try {
            getContext().registerReceiver(mBlueReceiver, mBluetoothIntentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private Activity _activity;
    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(getContext() != null){
                Intent intent = new Intent(getContext(), HandlerHelperActivity.class);
                getContext().startActivity(intent);

//                ReportManager.getInstance().send(
//                        Report.createReport(Report.ReportType.STATISTICS, null, null),
//                        Report.MAIN_ACTION, "handle");
            }
        }
    };

    @Override
    public void onCreateView(Activity activity){
        _activity = activity;
    }

    @Override
    public void onStart(Context context){
    }

    @Override
    public void onStop(){
        if(mBlueReceiver != null){
            try{
                this.getContext().unregisterReceiver(mBlueReceiver);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void findNewHandlerDevice(String address) {
        synchronized(mHandlerObject){
            if(!mHandlerList.contains(address)){
                mHandlerList.add(address);
            }
        }
    }

    @Override
    public void onFinishedFind() {
        refreshList();
    }

    private void refreshList(){
        MainHandler.getInstance().post(new Runnable(){
            @Override
            public void run() {
                int connectedHanlderNum = 0;
                synchronized (mHandlerObject) {
                    connectedHanlderNum = mHandlerList.size();
                }

                if (0 == connectedHanlderNum) {
                    setItemSummary(R.string.handle_connect_none);
                } else {
                    setItemSummary(getResources().getString(
                            R.string.connected_handler_summary_format,
                            connectedHanlderNum));
                }
            }
        });
    }


    @Override
    public void connected(BluetoothDevice device) {
        String address = null;
        if(device != null){
            address = device.getAddress();
        }
        if(TextUtils.isEmpty(address)){
            return;
        }
        synchronized(mHandlerObject){
            if(!mHandlerList.contains(address)){
                mHandlerList.add(address);
                refreshList();
            }
        }
    }

    @Override
    public void disconnected(BluetoothDevice device) {
        String address = null;
        if(device != null){
            address = device.getAddress();
        }
        if(TextUtils.isEmpty(address)){
            return;
        }
        synchronized(mHandlerObject){
            if(mHandlerList.remove(address)){
                refreshList();
            }
        }
    }
}

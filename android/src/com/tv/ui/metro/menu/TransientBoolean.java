package com.tv.ui.metro.menu;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TransientBoolean
{
    private static final String TAG = "TransientBoolean";
    private static final Timer Scheduler = new Timer(true);
    private boolean mFlag;
    private TransientWorker mWorker;
    public TransientBoolean(boolean aInit)
    {
        mFlag = aInit;
    }
    
    
    public void setValue(boolean aVal)
    {
        mFlag = aVal;
    }
   
    
    public boolean getValue()
    {
        return mFlag;
    }
    
    
    public void autoSetVal(final boolean aVal, long aDelay)
    {
        mFlag = !aVal;
        if(mWorker != null)
        {
            mWorker.cancel();
        }
        mWorker = new TransientWorker(aVal);
        Scheduler.schedule(mWorker, aDelay);
    }


    
    class TransientWorker extends TimerTask
    {
        boolean mTgtVal;
        TransientWorker(boolean aTgtVal)
        {
            mTgtVal = aTgtVal;
        }
        @Override
        public void run()
        {
            //TODO debug assert only
            assert(mFlag != mTgtVal);
            if(mFlag != mTgtVal)
            {
                mFlag = mTgtVal;
                
                Log.d(TAG, "some thing wrong timer will set the value");
            }
            else
            {
                Log.d(TAG, "Every thing fine");
            }
        }
    }
}

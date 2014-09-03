package com.tv.ui.metro.loader;

public interface ProgressNotifiable {

    //每一阶段的载入开始时均会调用该方法
    public void startLoading(boolean hasData);

    //每一阶段的载入结束时均会调用该方法
    public void stopLoading(boolean hasData, boolean hasNext);

    //绑定时马上会被调用，用于初始化当前的载入状态
    public void init(boolean hasData, boolean isLoading);
}

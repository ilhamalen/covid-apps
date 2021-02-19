package com.daws.projects.codamation.networks;

import android.content.Context;

public abstract class BaseNetworkAPI implements INetworkAPI {

    protected Context context;

    public BaseNetworkAPI(Context context) {
        this.context = context;
    }
}

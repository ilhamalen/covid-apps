package com.daws.projects.codamation;

import android.content.Context;

import com.daws.projects.codamation.networks.BaseNetworkAPI;
import com.daws.projects.codamation.networks.NetworkAPI;

public class CodamationConfiguration {

    private static final CodamationConfiguration instance = new CodamationConfiguration();

    public static CodamationConfiguration getInstance() {
        return instance;
    }

    public BaseNetworkAPI getNetworkAPI(Context context){
        return new NetworkAPI(context);
    }
}

package com.daws.projects.codamation.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.androidnetworking.error.ANError;
import com.daws.projects.codamation.helpers.UIHelper;

import org.json.JSONArray;
import org.json.JSONObject;


public abstract class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    protected void displayEror(ANError error){
        if (error.getErrorCode() != 0) {
            // received error from server
            // error.getErrorCode() - the error code from server
            // error.getErrorBody() - the error body from server
            // error.getErrorDetail() - just an error detail
            Log.d(getClass().getCanonicalName(), "onError errorCode : " + error.getErrorCode());
            Log.d(getClass().getCanonicalName(), "onError errorBody : " + error.getErrorBody());
            Log.d(getClass().getCanonicalName(), "onError errorDetail : " + error.getErrorDetail());
            // get parsed error object (If ApiError is your class)
            UIHelper.newInstance(getApplication()).showErrorToast(error.getErrorBody());
        } else {
            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
            Log.v("ANError", error.getErrorDetail());
            UIHelper.newInstance(getApplication()).showErrorToast(error.getErrorDetail());
        }
    }

    protected void displayNetworkResponse(JSONObject response){
        Log.v("networkResponse", response.toString());
    }

    protected void displayNetworkResponse(JSONArray response){
        Log.v("networkResponse", response.toString());
    }
}

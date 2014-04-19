package com.PJAJ.vthacksapp;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by Adam on 4/19/2014.
 */
public class SendSalesforceTask extends AsyncTask<JSONObject,Void,String> {
    AsyncCallback mListener;

    @Override
    protected String doInBackground(JSONObject... params) {
        //send to salesforce
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.callback(result);
    }

    protected void setCallback(AsyncCallback callback) {
        mListener = callback;
    }
}

package com.PJAJ.vthacksapp;


import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import com.loopj.android.http.*;
import org.json.JSONObject;

/**
 * Created by Adam on 4/19/2014.
 */
public class SendSalesforceTask extends AsyncTask<String,Void,String> {
    private AsyncCallback mListener;
    private static TextReadQueue queue;
    private AsyncHttpClient client;

    public SendSalesforceTask(Context context) {
        queue = TextReadQueue.get_instance(context);
        if (client==null) {
            client.get("salesforce.com", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    System.out.println(response);
                }
            });
        }
    }

    @Override
    protected String doInBackground(String... params) {
        //send to salesforce
        if (queue.top()!=null) {
            JSONArray array = queue.dequeue();

        }
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

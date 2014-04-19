package com.PJAJ.vthacksapp;

import android.content.Context;
import android.os.AsyncTask;

import java.security.PublicKey;


/**
 * Created by Adam on 4/19/2014.
 */
public class TesseractTask extends AsyncTask<byte[],Void,String> {
    AsyncCallback mListener;


    @Override
    protected String doInBackground(byte[]... params) {
        //call tesseract
        return null;
    }

    public void setCallbackFunction(AsyncCallback callback) {
        mListener = callback;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.callback(result);
    }
}

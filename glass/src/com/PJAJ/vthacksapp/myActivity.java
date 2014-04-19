package com.PJAJ.vthacksapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKCaptureListener;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Adam on 4/19/2014.
 */
public class myActivity extends Activity implements ScanditSDKListener {
   TextReadQueue queue;
    private Resources res;
    private ScanditSDK mPicker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = new TextReadQueue();
        res = getResources();
        initializeBarcodeScanner();

    }

    @Override
    public void onResume() {
        mPicker.startScanning();
        super.onResume();
    }

    @Override
    public void onPause() {
        mPicker.stopScanning();
        super.onPause();
    }

    private void initializeBarcodeScanner() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ScanditSDKAutoAdjustingBarcodePicker picker = new
                ScanditSDKAutoAdjustingBarcodePicker(this, res.getString(R.string.ScaneditAppKey), ScanditSDK.CAMERA_FACING_FRONT);
        picker.set1DScanningEnabled(false);
        picker.set2DScanningEnabled(false);
        picker.setCode128Enabled(false);
        picker.setCode39Enabled(false);
        picker.setCode39Enabled(false);
        picker.setEan13AndUpc12Enabled(false);
        picker.setEan8Enabled(false);
        // Specify the object that will receive the callback events
        setContentView(picker);
        mPicker = picker;
        mPicker.getOverlayView().addListener(this);
        createCaptureEvent();
    }

    private void createCaptureEvent() {

        mPicker.setCaptureListener(new ScanditSDKCaptureListener() {
            @Override
            public void didCaptureImage(byte[] bytes, int i, int i2) {
                //send to tesseract
                TesseractTask task = new TesseractTask();
                task.setCallbackFunction(createTesseractCallback());
                task.doInBackground(bytes);
            }
        });
    }

    private AsyncCallback createTesseractCallback(){
        AsyncCallback callback = new AsyncCallback() {
            @Override
            public void callback(String result) {
                try {
                    isMatch(result);
                    SendSalesforceTask task = new SendSalesforceTask();
                    task.setCallback(createSalesForceCallback());
                    task.doInBackground();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return callback;
    }

    private void isMatch(String test) throws JSONException {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(test);
        JSONArray array = new JSONArray();
        while (matcher.find()) {
            JSONObject object = new JSONObject();
            object.put("type","url");
            object.put("data",matcher.group(1));
            array.put(object);
        }
        if (array.length()!=0) {
            queue.enqueue(array);
        }
        matcher = Patterns.WEB_URL.matcher(test);
        array = new JSONArray();
        while (matcher.find()) {
            JSONObject object = new JSONObject();
            object.put("type","url");
            object.put("data",matcher.group(1));
            array.put(object);
        }
        if (array.length()!=0) {
            queue.enqueue(array);
        }
    }



    private AsyncCallback createSalesForceCallback() {
        AsyncCallback callback = new AsyncCallback() {
            @Override
            public void callback(String result) {

            }
        };
        return callback;
    }

    @Override
    public void didCancel() {
        mPicker.stopScanning();
        finish();
    }

    @Override
    public void didScanBarcode(String s, String s2) {

    }

    @Override
    public void didManualSearch(String s) {

    }
}
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Adam on 4/19/2014.
 */
public class myActivity extends Activity implements ScanditSDKListener {
    private Resources res;
    private ScanditSDK mPicker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    JSONObject foundText = isMatch(result);
                    SendSalesforceTask task = new SendSalesforceTask();
                    task.setCallback(createSalesForceCallback());
                    task.doInBackground(foundText);

                    if (foundText.length()>0) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return callback;
    }

    private JSONObject isMatch(String test) throws JSONException {
        JSONObject foundText = new JSONObject();
        List<String> urls = isURLMatch(test);
        List<String> emails = isEmailMatch(test);
        foundText = addtoJSON(urls, "url", foundText, 0);
        foundText = addtoJSON(emails, "email", foundText, 0);
        return foundText;
    }

    private JSONObject addtoJSON(List<String> list, String type, JSONObject foundText, int count) throws JSONException {
        for(String item:list) {
            foundText.put(type + Integer.toString(count),item);
        }
        return foundText;
    }

    private List<String> isEmailMatch(String test) throws JSONException {
        List<String> emails = new ArrayList<String>();
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(test);
        while (matcher.find()) {
            emails.add(matcher.group(1));
        }
        return emails;
    }

    private List<String> isURLMatch(String test) throws JSONException {
        List<String> urls = new ArrayList<String>();
        Matcher matcher = Patterns.WEB_URL.matcher(test);
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return  urls;
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
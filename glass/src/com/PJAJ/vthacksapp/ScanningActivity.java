package com.PJAJ.vthacksapp;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ScanningActivity extends Activity implements SurfaceHolder.Callback
{
    private Camera camera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean previewRunning = false;
    PictureCallback mPictureCallback;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder = mSurfaceView.getHolder();
       mPictureCallback = new Camera.PictureCallback() {
           @Override
           public void onPictureTaken(byte[] imageData, Camera c) {
               Log.d("IsPicture","taken");
           }

//               final ParseFile file = new ParseFile("barcode.png", imageData);
//               file.saveInBackground(new SaveCallback() {
//                   @Override
//                   public void done(ParseException e) {
//                       Map<String, ParseFile> map = new HashMap<String, ParseFile>();
//                        map.put("picture",file);
//                        ParseCloud.callFunctionInBackground("decodePicture", map, new FunctionCallback() {
//                            @Override
//                            public void done(Object o, com.parse.ParseException e) {
//                                if (e == null) {
//                                    //processResponse(object);
//                                } else {
//                                    // handleError();
//                                }
//                            }
//                        });
//                   }
//               });
//           }
       };
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            camera.takePicture(null,null,null,mPictureCallback);
            return true;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewRunning) {
            camera.stopPreview();
        }
        Parameters params = camera.getParameters();
        params.setPreviewSize(width, height);
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
//            camera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//
//                }
//            });
            previewRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        previewRunning = false;
        camera.release();
    }
}

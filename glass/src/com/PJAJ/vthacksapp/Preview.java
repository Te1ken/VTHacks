package com.PJAJ.vthacksapp;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

/**
 * Created by Adam on 4/18/2014.
 */
public class Preview extends ViewGroup implements SurfaceHolder.Callback {
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera mCamera;

    Preview(Context context) {
        super(context);
        surfaceView = new SurfaceView(context);
        addView(surfaceView);
        surfaceHolder =  surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void setCamera(Camera camera) {
        if(camera.equals(mCamera)) {
            return;
        }
        stopReleaseCamera();
        mCamera = camera;
        if(mCamera!=null) {
            requestLayout();
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    private void stopReleaseCamera() {
        if (mCamera!=null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        params.setPictureSize(width,height);
        mCamera.setParameters(params);
        mCamera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}

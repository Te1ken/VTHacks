package com.PJAJ.vthacksapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Adam on 4/19/2014.
 */


public class TextReadQueue implements Serializable {
    private static Resources res;
    private static LinkedList<JSONArray> queue;
    private static TextReadQueue _instance;

    private TextReadQueue(Context context){
        readQueueResource(context);
    }

    public static TextReadQueue get_instance(Context context) {
        if (_instance == null)
        {
            _instance = new TextReadQueue(context);
        }
        return _instance;
    }

    private void readQueueResource(Context context) {
        res = context.getResources();
        String queueFile = res.getString(R.string.queueFile);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(queueFile));
            queue = (LinkedList<JSONArray>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected int size() {
        return queue.size();
    }

    protected JSONArray top() {
        return queue.getFirst();
    }

    protected void enqueue(JSONArray textObjectNew) {
        queue.add(textObjectNew);
    }

    protected JSONArray dequeue() {
        return queue.pop();
    }

    protected void saveQueue() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(res.getString(R.string.queueFile)))); //Select
            oos.writeObject(queue);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

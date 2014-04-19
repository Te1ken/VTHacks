package com.PJAJ.vthacksapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Adam on 4/19/2014.
 */
public class TextReadQueue {
    static LinkedList<JSONArray> queue;

    public TextReadQueue() {
        if (queue == null) {
            queue = new LinkedList<JSONArray>();
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
}

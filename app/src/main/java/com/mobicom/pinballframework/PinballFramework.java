package com.mobicom.pinballframework;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.pinball.ContextAdaptor;
import com.android.pinball.PinballRequest;
import com.android.pinball.Request;
import com.android.pinball.RequestQueue;
import com.android.pinball.Response;
import com.android.pinball.PinballError;
import com.android.pinball.toolbox.JsonObjectRequest;
import com.android.pinball.toolbox.Pinball;

import org.json.JSONObject;

public class PinballFramework implements Parcelable {

    private static PinballFramework instance = null;
    private static PinballService service = null;
    private final String TAG = "E3Framework";
    private final int MAX_DELAY = Integer.MAX_VALUE;
    private Context ctx;
    private RequestQueue queue = null;
    private Adaptation adaptation = null;


    private ContextAdaptor ctxadaptor = null;


    private PinballFramework(Context c) {
        ctx = c;
        adaptation = new Adaptation(c);
        adaptation.isForeground(c.getPackageName());
        adaptation.getNetWorkType();

//        ctxadaptor= new ContextAdaptor(ctx, );
        /**
         * 调用这个函数的时候，queue已经创建并且启动了
         */
        queue = Pinball.newRequestQueue(ctx);

        /**
         * 启动后台Service
         */
        //Log.d(TAG, "start e3 service");
        //c.startService(new Intent(c, E3Service.class));
    }

    public static PinballFramework getInstance(Context c) {
        if (instance != null) {
            return instance;
        } else {
            instance = new PinballFramework(c);
            return instance;
        }
    }

    public void addDownloadRequest(final StringDownloadRequest dr) {
        /**
         * 根据具体Request的内容，下载相应的内容
         */
        String url = dr.uri;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dr.uplistener.onFinished(response.toString());
                        System.out.println("Load successfully!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(PinballError error) {
                        // TODO Auto-generated method stub
                        System.out.println(error.getLocalizedMessage());
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Pinball.newRequestQueue(ctx);
        System.out.println("Add request to queue");
        queue.add(jsObjRequest);
    }


    /**
     * 创建一个StringRequest，同时设置这个Request的url，最大可容忍delay，并且设置一个标签
     *
     * @param url
     * @param delay
     * @param tag
     * @return
     */
    public LStringRequest createStringRequest(String url, int delay, String tag) {
        LStringRequest lsr = new LStringRequest(url, delay, tag);
        return lsr;
    }

    /**
     * 利用指定的url，最大可容忍delay，以及tag创建ObjectRequest
     * @param url
     * @param delay
     * @param tag
     * @return
     */
    public LObjectRequest createObjectRequest(String url, int delay, String tag) {
        LObjectRequest lor = new LObjectRequest(url, delay, tag);
        return lor;
    }

    /**
     * 以同步方式执行一个StringRequest，在返回之前会一直阻塞
     *
     * @param lsr
     * @return
     */
    public String perfromStringRequest(LStringRequest lsr) {
        final boolean[] stop = {false};
        final String[] result = new String[1];
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result[0] = response;
                stop[0] = true;
                System.out.println("have fetched string, set tag to true.");
            }
        };
        lsr.setListener(listener);
        queue.add(lsr);
        System.out.println("get into loop.");
        /**
         * ???怎么把异步改成同步？
         */
        System.out.println("get out of loop.");
        return result[0];
    }

    /**
     * 以同步方式执行一个ObjectRequest，在返回之前会一直阻塞
     * 感觉这两个函数写的很丑陋
     *
     * @param lor
     * @return
     */
    public byte[] performObjectRequest(LObjectRequest lor) {
        final boolean[] stop = {false};
        final byte[][] result = new byte[1][];
        Response.Listener listener = new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                result[0] = response;
                stop[0] = true;
            }
        };
        lor.setListener(listener);
        queue.add(lor);
        while (!stop[0]) {
        }
        return result[0];
    }


    public void putStringRequest(LStringRequest lsr, Response.Listener rl) {
        lsr.setListener(rl);
        queue.add(lsr);
    }

    public void putObjectRequest(LObjectRequest lor, Response.Listener rl, Response.ProgressListener pl) {
        lor.setListener(rl);
        lor.setProgressListener(pl);
        queue.add(lor);
    }

    public void deleteRequest(String tag) {
        queue.cancelAll(tag);
    }


    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


    /**
     * 2016-02-23-Revision, MobiSys paper reject, start on MobiCom (Deadline: 2016-03-15)
     */

    /**
     * 带超时时间设置的Request创建方法。
     *
     * @param url
     * @param property
     * @param delay
     * @param tag
     * @return
     */
    public PinballRequest createRequest(String url, int property, int delay, String tag) {
        PinballRequest pr = new PinballRequest(url, property, delay, tag);
        return pr;
    }

    /**
     * 不带超时时间设置的Request创建方法。
     *
     * @param url
     * @param property
     * @param tag
     * @return
     */
    public PinballRequest createRequest(String url, int property, String tag) {
        PinballRequest pr = new PinballRequest(url, property, tag);
        String AppInfo = ctx.getPackageName();
        pr.setAppInfo(AppInfo);
        return pr;
    }

    /**
     * 以同步的方式执行Request，不带超时时间设置的调用方法。
     * 同步方法会在后续工作中再补充，目前主要关注异步调用方法。
     */
    public void performERequest() {

    }

    /**
     * 不带超时设置,也不带进度条的异步调用方法。
     */
    public void putRequest(PinballRequest er, Response.Listener rl) {
        putRequest(er, MAX_DELAY, null, rl, null);
    }

    /**
     * 不带超时设置,但是带进度条的异步调用方法。
     */
    public void putRequest(PinballRequest er, Response.Listener rl, Response.ProgressListener pl) {
        putRequest(er, MAX_DELAY, null, rl, pl);
    }

    /**
     * 带超时设置,也带进度条的异步调用方法。
     */
    public void putRequest(PinballRequest er, int delay, Response.TimeoutListener tl, Response.Listener rl, Response.ProgressListener pl) {
        er.setListener(rl);
        er.setProgressListener(pl);
        er.setTimeoutListener(tl);

        queue.add(er);
    }



}

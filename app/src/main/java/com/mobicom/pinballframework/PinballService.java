package com.mobicom.pinballframework;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.android.pinball.PinballRequest;
import com.android.pinball.Request;
import com.android.pinball.Response;


public class PinballService extends Service {

    private RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<ICallback>();
    private String TAG = "PinballService";
    private int BUFFER_SIZE = 1024;
    // private IService.Stub
    private PinballFramework pf = null;
    private final PinballRemoteService.Stub pinballBinder = new PinballRemoteService.Stub() {

        /**
         * Request the process ID of this service, to do evil things with it.
         */
        @Override
        public int getPid() throws RemoteException {
            return 0;
        }

        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         *
         * @param anInt
         * @param aLong
         * @param aBoolean
         * @param aFloat
         * @param aDouble
         * @param aString
         */
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void putRequest(String url, int delay, String tag, String sProperty, final ICallback callback) throws RemoteException {
            final PinballRequest pr = pf.createRequest(url, PinballRequest.ACTIVE, tag);
            pr.setShouldCache(false);
            pr.sProperty = sProperty;
            pf.putRequest(pr, new Response.Listener<byte[]>() {
                @Override
                public void onResponse(byte[] response) {
                    try {
                        callback.CallbackByte(response, pr.url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void putStringRequest(String url, int delay, String tag, final ICallback callback) throws RemoteException {
            Log.d(TAG, url + delay + tag);
            LStringRequest sr = pf.createStringRequest(url, delay, tag);
            final PinballRequest text_er = pf.createRequest(url, PinballRequest.ACTIVE, tag);
            text_er.setShouldCache(false);
            text_er.setEndTime(text_er.getEndTime() + 1000);

            pf.putRequest(text_er, new Response.Listener<byte[]>() {
                @Override
                public void onResponse(byte[] response) {
                    try {
                        callback.CallbackByte(response,text_er.url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public void putObjectRequest(final String url, int delay, String tag, final ICallback callback) throws RemoteException {
            Log.d(TAG, url + delay + tag);
            LObjectRequest or = pf.createObjectRequest(url, delay, tag);
            pf.putObjectRequest(or, new Response.Listener<byte[]>() {
                @Override
                public void onResponse(byte[] response) {
                    try {
                        Log.d(TAG, "onResponse, get the result for Object request. length=" + response.length);
                        byte[] b = new byte[BUFFER_SIZE];
                        Log.d(TAG, "before callbackobject.");
                        callback.CallbackByte(b, url);
                        Log.d(TAG, "after callbackobject.");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }

        @Override
        public String performStringRequest() throws RemoteException {
            return null;
        }

        @Override
        public byte[] performObjectRequest() throws RemoteException {
            return new byte[0];
        }

        @Override
        public void deleteRequest(String tag) throws RemoteException {

        }

        @Override
        public void registerCallback(ICallback cb) throws RemoteException {
            if (cb != null) {
                Log.d(TAG, "registerCallback is called.");
                //mCallbacks.register(cb);
                cb.showResult(666);
            }
        }

        @Override
        public void unregisterCallback(ICallback cb) throws RemoteException {
            if (cb != null) {
                //mCallbacks.unregister(cb);
            }
        }
    };

    public PinballService() {
        PinballFramework e3 = PinballFramework.getInstance(this);
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate E3Service. create the framework");
        pf = PinballFramework.getInstance(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onBind E3Service.");
        return pinballBinder;
    }
}

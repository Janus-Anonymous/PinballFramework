package com.mobicom.pinballframework;

import com.android.pinball.Request;
import com.android.pinball.Response;

public abstract class LRequest extends Request {


    /**
     * Creates a new request with the given method (one of the values from {@link Method}),
     * URL, and error listener.  Note that the normal response listener is not provided here as
     * delivery of responses is provided by subclasses, who have a better idea of how to deliver
     * an already-parsed response.
     *
     * @param method
     * @param url
     * @param listener
     */
    public LRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }
}

package com.mobicom.pinballframework;

public interface UploadListener<T> {
    void onFinished(T data);

    void onTimeout();

    void onProgress(int percentage);
}

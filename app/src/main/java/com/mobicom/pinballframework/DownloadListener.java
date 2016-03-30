package com.mobicom.pinballframework;

public interface DownloadListener<T> {
    void onFinished(T data);

    void onTimeout();

    void onProgress(int percentage);
}

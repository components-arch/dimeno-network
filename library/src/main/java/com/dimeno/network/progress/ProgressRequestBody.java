package com.dimeno.network.progress;

import com.dimeno.network.callback.ProgressCallback;
import com.dimeno.network.util.ThreadHelper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * progress request body
 * Created by wangzhen on 2020/4/16.
 */
public class ProgressRequestBody extends RequestBody {
    private final RequestBody requestBody;
    private ProgressCallback mCallback;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, ProgressCallback callback) {
        this.requestBody = requestBody;
        this.mCallback = callback;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (mCallback != null) {
                    if (contentLength == 0) {
                        contentLength = contentLength();
                    }
                    bytesWritten += byteCount;
                    if (contentLength > 0) {
                        ThreadHelper.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onProgress((int) (bytesWritten * 100f / contentLength));
                            }
                        });
                    }
                }
            }
        };
    }
}

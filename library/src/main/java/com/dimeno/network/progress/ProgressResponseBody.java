package com.dimeno.network.progress;

import com.dimeno.network.callback.ProgressCallback;
import com.dimeno.network.type.ErrorType;
import com.dimeno.network.util.ThreadHelper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * progress response body
 * Created by wangzhen on 2020/4/16.
 */
public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private ProgressCallback mCallback;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressCallback callback) {
        this.responseBody = responseBody;
        this.mCallback = callback;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            long contentLength = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                if (mCallback != null) {
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    if (bytesRead != -1) {
                        return bytesRead;
                    }
                    if (contentLength > 0) {
                        ThreadHelper.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onProgress((int) (totalBytesRead * 100f / contentLength));
                            }
                        });
                    } else {
                        ThreadHelper.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onError(ErrorType.CONTENT_LENGTH_EMPTY, "content length is 0");
                            }
                        });
                    }
                }
                return bytesRead;
            }
        };
    }
}

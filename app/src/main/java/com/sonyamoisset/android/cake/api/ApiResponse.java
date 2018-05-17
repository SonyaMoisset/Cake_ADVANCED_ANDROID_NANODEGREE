package com.sonyamoisset.android.cake.api;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Response;

public class ApiResponse<T> {

    public final int code;
    public final T body;
    public final String errorMessage;

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;

            if (response.errorBody() != null) {
                try {
                    message = Objects.requireNonNull(response.errorBody()).string();
                } catch (IOException ignored) {

                }
            }

            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }

            errorMessage = message;
            body = null;
        }
    }

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }
}
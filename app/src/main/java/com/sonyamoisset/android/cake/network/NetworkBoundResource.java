package com.sonyamoisset.android.cake.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.sonyamoisset.android.cake.AppExecutors;
import com.sonyamoisset.android.cake.api.ApiResponse;
import com.sonyamoisset.android.cake.vo.Resource;

import java.util.Objects;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors appExecutors;
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    protected NetworkBoundResource(AppExecutors appExecutors) {

        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();

        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> setValue(Resource.success(newData)));
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {

        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {

        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(dbSource, newData -> setValue(Resource.loading(newData)));

        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            if (Objects.requireNonNull(response).isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    appExecutors.mainThread().execute(() ->
                            result.addSource(loadFromDb(),
                                    newData -> setValue(Resource.success(newData)))
                    );
                });
            } else {
                result.addSource(dbSource,
                        newData -> setValue(Resource.error(response.errorMessage, newData)));
            }
        });
    }

    public LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }

    @WorkerThread
    private RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}

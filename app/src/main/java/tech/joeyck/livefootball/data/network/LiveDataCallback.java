package tech.joeyck.livefootball.data.network;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallback<T> implements Callback<T> {

    public interface OnDataFetched<T> {
        void onNewData(T data);
    }

    private OnDataFetched<ApiResponse<T>> callback;

    public LiveDataCallback(OnDataFetched<ApiResponse<T>> callback){
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        callback.onNewData(new ApiResponse<>(response));
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onNewData(new ApiResponse<>(t));
    }

}

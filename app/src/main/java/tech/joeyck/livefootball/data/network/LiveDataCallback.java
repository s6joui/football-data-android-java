package tech.joeyck.livefootball.data.network;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallback<T> implements Callback<T> {

    MutableLiveData<ApiResponse<T>> liveData;

    public LiveDataCallback(MutableLiveData<ApiResponse<T>> liveData){
        this.liveData = liveData;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        liveData.postValue(new ApiResponse<>(response));
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        liveData.postValue(new ApiResponse<>(t));
    }

}

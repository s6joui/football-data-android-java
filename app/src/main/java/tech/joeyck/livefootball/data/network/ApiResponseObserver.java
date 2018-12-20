package tech.joeyck.livefootball.data.network;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

public class ApiResponseObserver<T> implements Observer<ApiResponse<T>> {

    private ChangeListener<T> changeListener;
    private static final int ERROR_CODE = 0;

    public ApiResponseObserver(ChangeListener<T> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void onChanged(@Nullable ApiResponse<T> tApiResponse) {
        if (tApiResponse != null) {
            if (tApiResponse.isSuccessful() && tApiResponse.body != null) {
                changeListener.onSuccess(tApiResponse.body);
            }else{
                changeListener.onException(tApiResponse.errorMessage);
            }
            return;
        }
        changeListener.onException("Error");
    }

    public interface ChangeListener<T> {
        void onSuccess(T responseBody);
        void onException(String errorMessage);
    }
}

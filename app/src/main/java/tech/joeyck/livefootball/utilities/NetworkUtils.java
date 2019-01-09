package tech.joeyck.livefootball.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.network.LiveDataCallAdapterFactory;

/**
 * Provides static methods for various network operations
 */
public class NetworkUtils {

    private static final String FOOTBALL_API_URL = "https://api.football-data.org/v2/";
    private static final String CREST_API_URL = "https://football-crest-api.herokuapp.com/crest/";
    private static final String COVER_IMAGE_URL = "https://loremflickr.com/320/240/";
    public static final String REDDIT_API_URL = "https://www.reddit.com/";

    public static final String IMAGE_QUALITY_SD = "sd";
    public static final String IMAGE_QUALITY_HD = "hd";

    /**
     * Returns the a url String pointing to an image of the given team's crest
     * @param teamId    id of team
     * @param quality   quality of requested image (IMAGE_QUALITY_SD or IMAGE_QUALITY_HD)
     * @return          url to image
     */
    public static String getCrestUrl(int teamId, String quality){
        return CREST_API_URL + quality + "/" + teamId;
    }

    /**
     * Returns a url String to a random image based on a String query
     * @param query    image query
     * @return         url to image
     */
    public static String getCoverImageUrl(String query){
        return COVER_IMAGE_URL+query;
    }

    /**
     * Returns true if the device is connected to a network, false if offline
     * @param context
     * @return true if online, false if offline
     */
    public static boolean hasNetwork(Context context) {
        boolean isConnected = false; // Initial Value
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()){
            isConnected = true;
        }
        return isConnected;
    }

    static Retrofit.Builder buildRetrofit(Context context){
        long cacheSize = (5 * 1024 * 1024);
        Cache myCache = new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.cache(myCache);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.addHeader("X-Auth-Token", LiveFootballRepository.API_KEY);
                if(!NetworkUtils.hasNetwork(context)){
                    requestBuilder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7);
                }else{
                    requestBuilder.addHeader("Cache-Control", "public, max-stale=" + 60);
                }
                return chain.proceed(requestBuilder.build());
            }
        });

        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());
            return retrofitBuilder;
    }

    static Retrofit buildFootballAPI(Context context){
        Retrofit.Builder retrofitBuilder = buildRetrofit(context);
        return retrofitBuilder.baseUrl(FOOTBALL_API_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    static Retrofit buildRedditAPI(Context context){
        Retrofit.Builder retrofitBuilder = buildRetrofit(context);
        return retrofitBuilder.baseUrl(REDDIT_API_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

}

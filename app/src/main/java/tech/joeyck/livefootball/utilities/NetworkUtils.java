package tech.joeyck.livefootball.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class NetworkUtils {

    public static final String SVG_CONVERTER_URL = "http://172.30.1.9/convert?svg=";
    public static final String COVER_IMAGE_URL = "https://loremflickr.com/320/240/";

    public static String getPngUrl(String url){
        if(url!=null && url.endsWith("svg")){
            return SVG_CONVERTER_URL+url;
        }
        return url;
    }

    public static String getCoverImageUrl(String query){
        return COVER_IMAGE_URL+TextUtils.join(",",query.toLowerCase().split(" "));
    }

    public static boolean hasNetwork(Context context) {
        boolean isConnected = false; // Initial Value
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()){
            isConnected = true;
        }
        return isConnected;
    }

}

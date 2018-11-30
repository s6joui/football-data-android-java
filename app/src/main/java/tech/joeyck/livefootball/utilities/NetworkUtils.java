package tech.joeyck.livefootball.utilities;

public class NetworkUtils {

    public static final String SVG_CONVERTER_URL = "http://172.30.1.9/convert?svg=";

    public static String getPngUrl(String url){
        if(url!=null && url.endsWith("svg")){
            return SVG_CONVERTER_URL+url;
        }
        return url;
    }

}

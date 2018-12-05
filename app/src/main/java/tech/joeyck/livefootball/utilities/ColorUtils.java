package tech.joeyck.livefootball.utilities;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseIntArray;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TeamEntity;

public class ColorUtils {

    private static final String LOG_TAG = ColorUtils.class.getSimpleName();

    public static int getDarkerColor(int color, float factor){
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.rgb(Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

}

package tech.joeyck.livefootball.utilities;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseIntArray;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TeamEntity;

public class ColorUtils {

    private static final String LOG_TAG = ColorUtils.class.getSimpleName();

    private static final SparseIntArray competitionColors;
    static
    {
        competitionColors = new SparseIntArray();
        competitionColors.put(2013, R.color.serie_a_brazil);
        competitionColors.put(2021, R.color.permier_league);
        competitionColors.put(2016, R.color.championship);
        competitionColors.put(2018, R.color.euro_cup);
        competitionColors.put(2001, R.color.champions_league);
        competitionColors.put(2015, R.color.ligue_1);
        competitionColors.put(2019, R.color.seria_A_italy);
        competitionColors.put(2002, R.color.bundesliga);
        competitionColors.put(2003, R.color.eredivise);
        competitionColors.put(2017, R.color.primeira_liga);
        competitionColors.put(2014, R.color.la_liga);
        competitionColors.put(2000, R.color.world_cup);
    }

    public static int getColorResourceId(int competitionId){
        return competitionColors.get(competitionId);
    }

    public static int getDarkerColor(int color, float factor){
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

}

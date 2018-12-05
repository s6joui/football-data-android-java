package tech.joeyck.livefootball.utilities;

import android.util.SparseIntArray;

import tech.joeyck.livefootball.R;

public class CompetitionUtils {

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
        if(competitionColors.indexOfKey(competitionId) < 0){
            return R.color.gray;
        }
        return competitionColors.get(competitionId);
    }

    private static final SparseIntArray competitionReorder;
    static
    {
        competitionReorder = new SparseIntArray();
        competitionReorder.put(0, 1);
        competitionReorder.put(1, 10);
        competitionReorder.put(2, 6);
        competitionReorder.put(3, 7);
        competitionReorder.put(4, 8);
        competitionReorder.put(5, 9);
        competitionReorder.put(6, 5);
        competitionReorder.put(7, 4);
        competitionReorder.put(8, 0);
        competitionReorder.put(9, 2);
        competitionReorder.put(10, 11);
        competitionReorder.put(11, 3);
    }

    public static int getReorderedPosition(int position) {
        if(competitionReorder.indexOfKey(position) < 0){
            return 0;
        }
        return competitionReorder.get(position);
    }
}

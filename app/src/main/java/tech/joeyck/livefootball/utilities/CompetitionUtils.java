package tech.joeyck.livefootball.utilities;

import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.joeyck.livefootball.R;

/**
 * Provides static utility methods for adjusting competition and team properties
 */
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

    /**
     * Returns a color resource int based on the id of the competition.
     * @param competitionId id of competition
     * @return int color resource, gray as default if competition is unknown
     */
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
        competitionReorder.put(0, 10);
        competitionReorder.put(1, 1);
        competitionReorder.put(2, 7);
        competitionReorder.put(3, 6);
        competitionReorder.put(4, 5);
        competitionReorder.put(5, 9);
        competitionReorder.put(6, 8);
        competitionReorder.put(7, 4);
        competitionReorder.put(8, 0);
        competitionReorder.put(9, 2);
        competitionReorder.put(10, 11);
        competitionReorder.put(11, 3);
    }

    /**
     * Returns an int defining the position in which a competition should be displayed in the competition list. This is used to reorder the default list that the API returns.
     * @param originalPosition  the original ordering of the item as returned by the API
     * @return new position in which the item should be displayed, 0 as default if no reordering is known
     */
    public static int getReorderedPosition(int originalPosition) {
        if(competitionReorder.indexOfKey(originalPosition) < 0){
            return 0;
        }
        return competitionReorder.get(originalPosition);
    }

    private static final List<String> wordsToRemove = Arrays.asList("fc","cf","rcd","de","fútbol","club","sd","balompié","cd","ud","afc","rc","ss","ssc","sc","ac","us","acf","cfc","hsc","as","bc","calcio","sv","tsg","fsv","osc","ogc","sm","sco","tsv","bv","bsc","vfl","vfb");

    /**
     * Returns a simplified version of a soccer team's name by removing common words and characters.
     * @param   teamName the name of the team
     * @return  a String with the simplified name
     * @throws  NullPointerException if teamName is null
     */
    public static String simplifyTeamName(String teamName) throws NullPointerException{
        if(teamName == null)
            throw new NullPointerException();
        String[] words = teamName.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if(!wordsToRemove.contains(word.toLowerCase())){
                sb.append(word);
                sb.append(' ');
            }
        }
        return sb.toString().trim();
    }
}

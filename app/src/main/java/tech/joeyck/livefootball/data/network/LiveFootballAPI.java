package tech.joeyck.livefootball.data.network;

import androidx.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchResponse;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;

public interface LiveFootballAPI {

    @GET("competitions?plan=TIER_ONE")
    Call<CompetitionResponse> getCompetitions();

    @GET("competitions/{id}")
    Call<CompetitionEntity> getCompetitionById(@Path("id") int id);

    @GET("competitions/{id}/standings")
    Call<StandingsResponse> getCompetitionStandings(@Path("id") int id);

    @GET("teams/{id}")
    Call<TeamEntity> getTeamById(@Path("id") int id);

    @GET("matches/{id}")
    Call<MatchResponse> getMatchById(@Path("id") int id);

    @GET("competitions/{id}/matches")
    Call<MatchesResponse> getMatchesForCompetition(@Path("id") int competitionId, @Query("matchday") int matchday);

    @GET("teams/{id}/matches")
    Call<MatchesResponse> getMatchesForTeam(@Path("id") int teamId, @Query("dateFrom") String dateFrom,@Query("dateTo") String dateTo);

}

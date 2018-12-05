package tech.joeyck.livefootball.data.network;

import android.arch.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;

public interface LiveFootballAPI {

    @GET("competitions?plan=TIER_ONE")
    LiveData<ApiResponse<CompetitionResponse>> getCompetitions();

    @GET("competitions/{id}")
    LiveData<ApiResponse<CompetitionEntity>> getCompetitionById(@Path("id") int id);

    @GET("competitions/{id}/standings")
    LiveData<ApiResponse<StandingsResponse>> getCompetitionStandings(@Path("id") int id);

    @GET("teams/{id}")
    LiveData<ApiResponse<TeamEntity>> getTeamById(@Path("id") int id);

    @GET("competitions/{id}/matches")
    LiveData<ApiResponse<MatchesResponse>> getMatchesForCompetition(@Path("id") int competitionId, @Query("matchday") int matchday);

    @GET("teams/{id}/matches")
    LiveData<ApiResponse<MatchesResponse>> getMatchesForTeam(@Path("id") int teamId, @Query("dateFrom") String dateFrom,@Query("dateTo") String dateTo);

}

package tech.joeyck.livefootball.data.database;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LiveFootballAPI {

    @GET("competitions?plan=TIER_ONE")
    Call<CompetitionResponse> getCompetitions();

    @GET("competitions/{id}")
    Call<CompetitionEntity> getCompetitionById(@Path("id") int id);

    @GET("competitions/{id}/standings")
    Call<StandingsResponse> getCompetitionStandings(@Path("id") int id);

    @GET("teams/{id}")
    Call<TeamEntity> getTeamById(@Path("id") int id);

    @GET("competitions/{id}/matches")
    Call<MatchesResponse> getMatches(@Path("id") int id,@Query("dateFrom") String dateFrom,@Query("dateTo") String dateTo);
}

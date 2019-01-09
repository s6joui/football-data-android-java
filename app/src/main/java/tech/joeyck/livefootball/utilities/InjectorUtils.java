/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.joeyck.livefootball.utilities;

import android.content.Context;

import tech.joeyck.livefootball.AppExecutors;
import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.network.LiveFootballAPI;
import tech.joeyck.livefootball.data.network.RedditAPI;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionViewModelFactory;
import tech.joeyck.livefootball.ui.competition_detail.standings.StandingsViewModelFactory;
import tech.joeyck.livefootball.ui.competition_detail.matches.MatchesViewModelFactory;
import tech.joeyck.livefootball.ui.match_detail.MatchDetailViewModelFactory;
import tech.joeyck.livefootball.ui.team_detail.TeamDetailViewModelFactory;

/**
 * Provides static methods to inject the various classes needed
 */
public class InjectorUtils {

    private static LiveFootballRepository provideRepository(Context context) {
        LiveFootballAPI footballAPI = NetworkUtils.buildFootballAPI(context).create(LiveFootballAPI.class);
        RedditAPI redditAPI = NetworkUtils.buildRedditAPI(context).create(RedditAPI.class);
        return LiveFootballRepository.getInstance(footballAPI,redditAPI);
    }

    public static StandingsViewModelFactory provideStandingsViewModelFactory(Context context) {
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new StandingsViewModelFactory(repository);
    }

    public static TeamDetailViewModelFactory provideTeamDetailViewModelFactory(Context context, int teamId) {
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new TeamDetailViewModelFactory(repository,teamId);
    }

    public static MatchesViewModelFactory provideMatchesViewModelFactory(Context context) {
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new MatchesViewModelFactory(repository);
    }

    public static CompetitionViewModelFactory provideCompetitionViewModelFactory(Context context, CompetitionEntity competition){
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new CompetitionViewModelFactory(repository,competition);
    }

    public static MatchDetailViewModelFactory provideMatchDetailViewModelFactory(Context context, int matchId, String homeTeam, String awayTeam) {
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new MatchDetailViewModelFactory(repository,matchId,homeTeam,awayTeam);
    }
}
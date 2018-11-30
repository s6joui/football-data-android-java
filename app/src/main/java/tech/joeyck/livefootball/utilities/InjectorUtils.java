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

import java.io.IOException;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.joeyck.livefootball.AppExecutors;
import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.LiveFootballAPI;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionDetailViewModelFactory;
import tech.joeyck.livefootball.ui.competitions.MainViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Sunshine
 */
public class InjectorUtils {

    public static LiveFootballRepository provideRepository(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        LiveFootballAPI service = buildRetrofit().create(LiveFootballAPI.class);
        return LiveFootballRepository.getInstance(service,executors);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context) {
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static CompetitionDetailViewModelFactory provideCompetitionDetailViewModelFactory(Context context, int competitionId) {
        LiveFootballRepository repository = provideRepository(context.getApplicationContext());
        return new CompetitionDetailViewModelFactory(repository,competitionId);
    }

    private static final Retrofit buildRetrofit(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("X-Auth-Token", LiveFootballRepository.API_KEY).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.football-data.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

}
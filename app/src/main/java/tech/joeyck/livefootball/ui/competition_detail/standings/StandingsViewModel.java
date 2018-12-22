package tech.joeyck.livefootball.ui.competition_detail.standings;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.StagesEntity;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TableEntryEntity;
import tech.joeyck.livefootball.data.network.ApiResponse;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.CompetitionTableItem;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.HeaderItem;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.TeamItem;

public class StandingsViewModel extends ViewModel {

    private MutableLiveData<ApiResponse<StandingsResponse>> mTableItems = new MutableLiveData<>();

    private int mCompetitionId;
    private LiveFootballRepository mRepository;

    StandingsViewModel(LiveFootballRepository repository){
        mRepository = repository;
    }

    public void setCompetitionId(int competitionId){
        mCompetitionId = competitionId;
        fetchCompetitionStandings();
    }

    public void fetchCompetitionStandings(){
        mRepository.fetchCompetitionStandings(mCompetitionId,(standings) -> {
            mTableItems.postValue(standings);
        });
    }

    public LiveData<ApiResponse<StandingsResponse>> getTableItems() {
        return mTableItems;
    }

    public static List<CompetitionTableItem> formatTableData(List<StagesEntity> stages){
        List<CompetitionTableItem> tableItems = new ArrayList<>();
        for (StagesEntity stage : stages) {
            if(stage.getType().equals(StagesEntity.TYPE_TOTAL)){
                String text = stage.getGroup() != null ? stage.getGroup() : stage.getStageName();
                HeaderItem tableItem = new HeaderItem(text.replace("_"," "));
                tableItems.add(tableItem);
                for (TableEntryEntity team : stage.getTable()) {
                    TeamItem teamTableItem = new TeamItem(team);
                    tableItems.add(teamTableItem);
                }
            }
        }
        return tableItems;
    }

}

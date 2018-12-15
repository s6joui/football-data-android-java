package tech.joeyck.livefootball.data.database;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalUnit;

import java.util.Date;

public abstract class BaseEntity {

    private Date lastUpdated;

    public LocalDateTime getLastUpdatedLocalDateTime() {
        return Instant.ofEpochMilli(lastUpdated.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().minusHours(1); //I substract 1 hour because the 'lastUpdated' field is returned as GMT istead of UTC by the API
    }

}

package tech.joeyck.livefootball.data.database;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.Date;

public abstract class BaseEntity {

    private Date lastUpdated;

    public LocalDateTime getLastUpdatedLocalDateTime() {
        return Instant.ofEpochMilli(lastUpdated.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}

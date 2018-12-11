package tech.joeyck.livefootball.utilities;

import android.content.Context;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import tech.joeyck.livefootball.R;

import static org.threeten.bp.temporal.ChronoUnit.HOURS;
import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

public class DateUtils {

    public static String getFormattedMatchDate(Context context, LocalDateTime dateTime){
        LocalDate today = LocalDate.now();
        if(dateTime.getDayOfYear() == today.getDayOfYear()){
            return context.getString(R.string.today) + " " + dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        }else if(dateTime.getDayOfYear() == today.getDayOfYear()+1){
            return context.getString(R.string.tomorrow) + " " +  dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        }else if(dateTime.getDayOfYear() == today.getDayOfYear()-1){
            return context.getString(R.string.yesterday) + " " +  dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("EEE, d/M h:mm a"));
    }

    public static String getLastUpdatedString(Context context, LocalDateTime lastUpdateTime){
        LocalDateTime now = LocalDateTime.now();
        long minUpdate = MINUTES.between(lastUpdateTime,now);
        if(minUpdate < 60) {
            return context.getString(R.string.updated_minutes_ago, minUpdate);
        }else if(minUpdate < 1440){
            long hourUpdate = minUpdate / 60;
            return context.getString(R.string.updated_hours_ago, hourUpdate);
        }else if(minUpdate < 2880){
            return context.getString(R.string.updated_day_ago);
        }
        long dayUpdate = minUpdate / 1440;
        return context.getString(R.string.updated_days_ago,dayUpdate);
    }

}

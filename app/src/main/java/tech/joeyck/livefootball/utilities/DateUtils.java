package tech.joeyck.livefootball.utilities;

import android.content.Context;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import tech.joeyck.livefootball.R;

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

}

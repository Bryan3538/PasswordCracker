package com.bfulton.PasswordCracker;

/**
 *
 * @author Student
 */
public class TimeFormatter {
    
    private TimeFormatter()
    {
        
    }
    
    public static String formatTime(long timeInMilli) {
        final long secondsInMilli = 1000;
        final long minutesInMilli = secondsInMilli * 60;
        final long hoursInMilli = minutesInMilli * 60;
        final long daysInMilli = hoursInMilli * 24;
        final long yearsInMilli = daysInMilli * 365;

        long years = timeInMilli / yearsInMilli;
        timeInMilli = timeInMilli % yearsInMilli;

        long days = timeInMilli / daysInMilli;
        timeInMilli = timeInMilli % daysInMilli;

        long hours = timeInMilli / hoursInMilli;
        timeInMilli = timeInMilli % hoursInMilli;

        long minutes = timeInMilli / minutesInMilli;
        timeInMilli = timeInMilli % minutesInMilli;

        long seconds = timeInMilli / secondsInMilli;

        StringBuilder b = new StringBuilder();

        if (years > 0) {
            b.append(String.format("%d years ", years));
        }

        if (days > 0) {
            b.append(String.format("%d days ", days));
        }

        if (hours > 0) {
            b.append(String.format("%d hours ", hours));
        }

        if (minutes > 0) {
            b.append(String.format("%d minutes and ", minutes));
        }

        b.append(String.format("%d seconds", seconds));

        return b.toString();
    }
}
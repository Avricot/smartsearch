package com.avricot.search.front.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 *
 */
public class DateUtils {

    /**
     * Return a timestamp in sec.
     */
    public static long roundTimestampHourly(long timestamp){
        return roundTimestamp(timestamp, ChronoUnit.HOURS);
    }

    /**
     * Return a timestamp in sec.
     */
    public static long roundTimestampToMinute(long timestamp){
        return roundTimestamp(timestamp, ChronoUnit.MINUTES);
    }

    /**
     * Return a timestamp in sec.
     */
    public static long roundTimestamp(long timestamp, ChronoUnit unit){
        Instant instant = getInstant(timestamp);
        final long milli = instant.truncatedTo(unit).toEpochMilli();
        return milli / 1000;
    }


    /**
     * Return an instant based on the timestamp, try to guess if it's in millisec or sec (we don't have small timestamp so it's safe)
     */
    private static Instant getInstant(final long timestamp) {
        return timestamp > 1000000000000L ? Instant.ofEpochMilli(timestamp) : Instant.ofEpochSecond(timestamp);
    }
}

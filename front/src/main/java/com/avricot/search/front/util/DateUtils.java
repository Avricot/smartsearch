package com.avricot.search.front.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 *
 */
public class DateUtils {

    public static long roundTimestampHourly(long timestamp){
        final Instant instant = Instant.ofEpochSecond(timestamp);
        final long milli = instant.truncatedTo(ChronoUnit.HOURS).toEpochMilli();
        return milli / 1000;
    }
}

package com.iheartradio.m3u8.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ExtXProgramDateTimeUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS+00:00";

    public static String fromUtc(Long unixTimeStamp) {
        ZonedDateTime programDateTime = ZonedDateTime
                .ofInstant(Instant.ofEpochMilli(unixTimeStamp), ZoneId.of("UTC"));

        return programDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}

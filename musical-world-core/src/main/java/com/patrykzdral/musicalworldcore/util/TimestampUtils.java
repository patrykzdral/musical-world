package com.patrykzdral.musicalworldcore.util;

import java.sql.Timestamp;
import java.time.Instant;

public class TimestampUtils {
    public Timestamp nowTimestamp() {
        return Timestamp.from(Instant.now());
    }

    public Long nowTimestampTime() {
        return Timestamp.from(Instant.now()).getTime();
    }
}

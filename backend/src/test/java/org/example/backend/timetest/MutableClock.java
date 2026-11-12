package org.example.backend.timetest;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class MutableClock extends Clock {

    private Instant instant;
    private final ZoneId zone;

    public MutableClock(Instant instant, ZoneId zone) {
        this.instant = instant;
        this.zone = zone;
    }

    public void setInstant(Instant instant) { this.instant = instant; }
    public void advance(Duration d)         { this.instant = this.instant.plus(d); }

    @Override public ZoneId getZone()       { return zone; }
    @Override public Clock withZone(ZoneId z) { return new MutableClock(instant, z); }
    @Override public Instant instant()      { return instant; }
}
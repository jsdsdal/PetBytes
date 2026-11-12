package org.example.backend.timetest;

import java.time.*;

public class MutableClock extends Clock {
    private Instant now;
    public MutableClock(Instant start) { this.now = start; }
    public void advance(Duration d) { now = now.plus(d); }
    @Override public Instant instant() { return now; }
    @Override public ZoneId getZone() { return ZoneOffset.UTC; }
    @Override public Clock withZone(ZoneId z) { return this; }
}
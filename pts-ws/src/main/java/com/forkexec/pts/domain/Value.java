package com.forkexec.pts.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Value {

    private AtomicInteger points;
    private Integer tag;

    public Value(AtomicInteger p, Integer t) {
        points = p;
        tag = t;
    }

    /**
     * @return the points
     */
    public AtomicInteger getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(AtomicInteger points) {
        this.points = points;
    }

    /**
     * @return the tag
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(Integer tag) {
        this.tag = tag;
    }

}
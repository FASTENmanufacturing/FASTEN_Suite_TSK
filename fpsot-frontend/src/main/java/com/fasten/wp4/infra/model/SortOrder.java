package com.fasten.wp4.infra.model;

/**
 * Created by rmpestano on 10/31/14.
 */
public enum SortOrder {

    ASCENDING, DESCENDING, UNSORTED;

    public boolean isAscending() {
        return ASCENDING.equals(this);
    }
}

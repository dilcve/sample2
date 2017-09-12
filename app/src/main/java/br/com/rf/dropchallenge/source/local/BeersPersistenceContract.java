package br.com.rf.dropchallenge.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class BeersPersistenceContract {

    private BeersPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "beers";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_JSON = "json";

    }
}

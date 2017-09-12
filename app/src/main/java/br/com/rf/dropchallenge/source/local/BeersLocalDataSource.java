package br.com.rf.dropchallenge.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.rf.dropchallenge.model.Beer;
import br.com.rf.dropchallenge.model.BeerWrapper;
import br.com.rf.dropchallenge.source.BeersDataSource;

import static com.google.common.base.Preconditions.checkNotNull;


public class BeersLocalDataSource implements BeersDataSource {

    private static BeersLocalDataSource INSTANCE;

    private BeersDbHelper mDbHelper;

    // Prevent direct instantiation.
    private BeersLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new BeersDbHelper(context);
    }

    public static BeersLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BeersLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getBeers(int type, @NonNull LoadBeersCallback callback) {
        List<Beer> beers = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID,
                BeersPersistenceContract.UserEntry.COLUMN_NAME_JSON,

        };

        Cursor c = db.query(
                BeersPersistenceContract.UserEntry.TABLE_NAME, projection, BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{"" + type}, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String json = c.getString(c.getColumnIndexOrThrow(BeersPersistenceContract.UserEntry.COLUMN_NAME_JSON));

                BeerWrapper userWrapper = new Gson().fromJson(json, BeerWrapper.class);
                beers.addAll(userWrapper.beers);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (beers.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onBeersLoaded(beers);
        }

    }

    @Override
    public void getMoreBeers(int type, @NonNull LoadBeersCallback callback) {
        //nothing to do on local
    }

    private BeerWrapper getBeers(int type) {
        BeerWrapper beersWrapper = new BeerWrapper();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID,
                BeersPersistenceContract.UserEntry.COLUMN_NAME_JSON,

        };

        Cursor c = db.query(
                BeersPersistenceContract.UserEntry.TABLE_NAME, projection, BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{"" + type}, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID));
                String json = c.getString(c.getColumnIndexOrThrow(BeersPersistenceContract.UserEntry.COLUMN_NAME_JSON));

                beersWrapper = new Gson().fromJson(json, BeerWrapper.class);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return beersWrapper;
    }

    @Override
    public void saveBeers(int type, @NonNull BeerWrapper beerWrapper) {
        checkNotNull(beerWrapper);

        BeerWrapper localBeers = getBeers(type);
        deleteAllBeers(type);

        if (localBeers != null && localBeers.beers != null && localBeers.beers.size() > 0) {
            beerWrapper.beers.addAll(localBeers.beers);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID, type);
        values.put(BeersPersistenceContract.UserEntry.COLUMN_NAME_JSON, new Gson().toJson(beerWrapper));

        db.insert(BeersPersistenceContract.UserEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void saveBeers(int type, @NonNull List<Beer> beers) {
        checkNotNull(beers);
        saveBeers(type, new BeerWrapper(beers));
    }

    @Override
    public void deleteAllBeers(int type) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(BeersPersistenceContract.UserEntry.TABLE_NAME, BeersPersistenceContract.UserEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{"" + type});

        db.close();
    }

}

package com.omri.countriesapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.omri.countriesapp.db.AppDatabase.DATABASE_NAME;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class DatabaseCreator {
    private static DatabaseCreator sInstance;

    private AppDatabase mDb;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    private final AtomicBoolean mInitializing = new AtomicBoolean(true);

    public synchronized static DatabaseCreator getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new DatabaseCreator();
                }
            }
        }
        return sInstance;
    }

    /**
     * Used to observe when the database initialization is done
     */
    public LiveData<Boolean> isDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    @Nullable
    public AppDatabase getDatabase() {
        return mDb;
    }

    /**
     * Creates or returns a previously-created database.
     */
    public void createDb(Context context) {

        Log.d("DatabaseCreator", "Creating DB from " + Thread.currentThread().getName());

        if (!mInitializing.compareAndSet(true, false)) {
            return; // Already initializing
        }

        // Trigger an update to show a loading screen.
        mIsDatabaseCreated.setValue(false);
        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... params) {
                Log.d("DatabaseCreator",
                        "Starting bg job " + Thread.currentThread().getName());

                Context context = params[0].getApplicationContext();

                // Reset the database to have new data on every run.
                context.deleteDatabase(DATABASE_NAME);

                // Build the database
                AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME).build();

                Log.d("DatabaseCreator",
                        "DB was populated in thread " + Thread.currentThread().getName());

                mDb = db;
                return null;
            }

            @Override
            protected void onPostExecute(Void ignored) {
                // Now on the main thread, notify observers that the db is created and ready.
                mIsDatabaseCreated.setValue(true);
            }
        }.execute(context.getApplicationContext());
    }
}

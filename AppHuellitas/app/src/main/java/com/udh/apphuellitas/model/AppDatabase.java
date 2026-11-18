package com.udh.apphuellitas.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Mascota.class,
                Vacuna.class
        },
        version = 2,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase {

    public abstract MascotaDao mascotaDao();
    public abstract VacunaDao vacunaDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context ctx) {

        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            ctx.getApplicationContext(),
                            AppDatabase.class,
                            "huellitas.db"
                    ).build();
                }
            }
        }

        return INSTANCE;
    }

}

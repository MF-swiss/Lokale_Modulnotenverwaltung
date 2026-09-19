package ch.wiss.m335.modulnotenverwaltung.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * (R) Room-Datenbankklasse für die Modulverwaltungs-App.
 * Definiert die Datenbank und stellt den Zugriff auf das ModuleDao bereit.
 */
@Database(entities = {Module.class}, version = 1, exportSchema = false)
public abstract class ModuleDatabase extends RoomDatabase {

    public abstract ModuleDao moduleDao();

    private static volatile ModuleDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ModuleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ModuleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ModuleDatabase.class, "module_database")
                            .allowMainThreadQueries() // Ermöglicht das Ausführen von Abfragen auf dem Hauptthread (für einfache Anwendungen, sonst async)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

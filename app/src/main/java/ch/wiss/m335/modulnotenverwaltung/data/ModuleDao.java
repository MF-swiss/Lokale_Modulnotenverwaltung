package ch.wiss.m335.modulnotenverwaltung.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO-Interface für den Zugriff auf die Moduldatenbank.
 * Definiert Methoden für CRUB-Operationen
 */
@Dao
public interface ModuleDao {
    @Insert
    long insert(Module module);

    @Update
    void update(Module module);

    @Delete
    void delete(Module module);

    @Query("SELECT * FROM modules")
    List<Module> getAll();

    @Query("SELECT * FROM modules WHERE id = :moduleId LIMIT 1")
    Module findById(int moduleId);
}

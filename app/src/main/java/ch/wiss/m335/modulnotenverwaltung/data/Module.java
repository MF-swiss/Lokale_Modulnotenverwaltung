package ch.wiss.m335.modulnotenverwaltung.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Speichert Modulnummer, Modultitel, ZP-Note und LB-Note.
 */
@Entity(tableName = "modules")
public class Module {
    @PrimaryKey(autoGenerate = true)
    public int id; // Eindeutiger Bezeichner für das Modul

    public String moduleNumber; // Die Nummer des Moduls (z.B. M335), Pflichtfeld
    public String moduleTitle;  // Der Titel des Moduls (z.B. "Mobile Apps entwickeln"), Pflichtfeld
    public Double zpNote;       // Die ZP-Note (optional, kann null sein)
    public Double lbNote;       // Die LB-Note (optional, kann null sein)

    /**
     * Konstruktor für die Module-Entität.
     * @param moduleNumber Die Modulnummer.
     * @param moduleTitle Der Modultitel.
     * @param zpNote Die ZP-Note, kann null sein.
     * @param lbNote Die LB-Note, kann null sein.
     */
    public Module(String moduleNumber, String moduleTitle, Double zpNote, Double lbNote) {
        this.moduleNumber = moduleNumber;
        this.moduleTitle = moduleTitle;
        this.zpNote = zpNote;
        this.lbNote = lbNote;
    }

    // Getter-Methoden
    public int getId() {
        return id;
    }

    public String getModuleNumber() {
        return moduleNumber;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public Double getZpNote() {
        return zpNote;
    }

    public Double getLbNote() {
        return lbNote;
    }

    /**
     * Berechnet die Durchschnittsnote, falls beide Noten vorhanden sind.
     * @return Die Durchschnittsnote als Double oder null, wenn nicht beide Noten vorhanden sind.
     */
    public Double getAverageNote() {
        if (zpNote != null && lbNote != null) {
            return (zpNote + lbNote) / 2.0;
        }
        return null;
    }

    // Setter-Methoden
    public void setId(int id) {
        this.id = id;
    }

    public void setModuleNumber(String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public void setZpNote(Double zpNote) {
        this.zpNote = zpNote;
    }

    public void setLbNote(Double lbNote) {
        this.lbNote = lbNote;
    }
}

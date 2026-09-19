package ch.wiss.m335.modulnotenverwaltung;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ch.wiss.m335.modulnotenverwaltung.data.Module;
import ch.wiss.m335.modulnotenverwaltung.data.ModuleDatabase;

/**
 * Aktivität zum Hinzufügen oder Bearbeiten eines Moduls.
 * Handhabt die Benutzereingabe, Validierung und Speicherung in der Datenbank.
 */
public class ModuleFormActivity extends AppCompatActivity {

    private static final String TAG = "ModuleFormActivity";
    public static final String EXTRA_MODULE_ID = "MODULE_ID";

    private TextInputLayout tilModuleNumber, tilModuleTitle, tilZpNote, tilLbNote;
    private TextInputEditText etModuleNumber, etModuleTitle, etZpNote, etLbNote;
    private Button btnSaveModule;

    private int moduleId = 0; // 0 für neues Modul, sonst ID des zu bearbeitenden Moduls

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_form);

        tilModuleNumber = findViewById(R.id.tilModuleNumber);
        tilModuleTitle = findViewById(R.id.tilModuleTitle);
        tilZpNote = findViewById(R.id.tilZpNote);
        tilLbNote = findViewById(R.id.tilLbNote);

        etModuleNumber = findViewById(R.id.etModuleNumber);
        etModuleTitle = findViewById(R.id.etModuleTitle);
        etZpNote = findViewById(R.id.etZpNote);
        etLbNote = findViewById(R.id.etLbNote);
        btnSaveModule = findViewById(R.id.btnSaveModule);

        moduleId = getIntent().getIntExtra(EXTRA_MODULE_ID, 0);

        if (moduleId != 0) {
            loadModuleData(moduleId);
        }

        btnSaveModule.setOnClickListener(v -> saveModule());
    }

    /**
     * Lädt die Daten eines vorhandenen Moduls in das Formular, wenn ein Modul zur Bearbeitung geöffnet wird.
     * @param id Die ID des zu ladenden Moduls.
     */
    private void loadModuleData(int id) {
        Module module = ModuleDatabase.getDatabase(this).moduleDao().findById(id);
        if (module != null) {
            etModuleNumber.setText(module.getModuleNumber());
            etModuleTitle.setText(module.getModuleTitle());
            if (module.getZpNote() != null) {
                etZpNote.setText(String.valueOf(module.getZpNote()));
            }
            if (module.getLbNote() != null) {
                etLbNote.setText(String.valueOf(module.getLbNote()));
            }
        }
    }

    /**
     * Speichert das Modul in der Datenbank.
     * Führt eine Validierung der Eingabefelder durch.
     */
    private void saveModule() {
        if (!validateForm()) {
            Log.d(TAG, "Form validation failed.");
            return;
        }

        String moduleNumber = etModuleNumber.getText().toString().trim();
        String moduleTitle = etModuleTitle.getText().toString().trim();
        Double zpNote = parseDouble(etZpNote.getText().toString().trim());
        Double lbNote = parseDouble(etLbNote.getText().toString().trim());

        Module module = new Module(moduleNumber, moduleTitle, zpNote, lbNote);

        if (moduleId == 0) {
            // Neues Modul hinzufügen
            long newId = ModuleDatabase.getDatabase(this).moduleDao().insert(module);
            Log.d(TAG, "New module inserted with ID: " + newId);
        } else {
            // Bestehendes Modul aktualisieren
            module.setId(moduleId);
            ModuleDatabase.getDatabase(this).moduleDao().update(module);
            Log.d(TAG, "Module updated with ID: " + moduleId);
        }
        finish(); // Zurück zur Hauptaktivität
    }

    /**
     * Kontrolliert die Eingabefelder des Formulars.
     * @return true, wenn alle Felder gültig sind, sonst false.
     */
    private boolean validateForm() {
        boolean isValid = true;

        // Modulnummer Validierung
        if (TextUtils.isEmpty(etModuleNumber.getText().toString().trim()) || etModuleNumber.getText().toString().trim().length() < 4) {
            tilModuleNumber.setError(getString(R.string.error_min_length));
            isValid = false;
        } else {
            tilModuleNumber.setError(null);
        }

        // Modultitel Validierung
        if (TextUtils.isEmpty(etModuleTitle.getText().toString().trim()) || etModuleTitle.getText().toString().trim().length() < 4) {
            tilModuleTitle.setError(getString(R.string.error_min_length));
            isValid = false;
        } else {
            tilModuleTitle.setError(null);
        }

        // ZP-Note Validierung (optional, aber wenn eingegeben, dann gültige Zahl)
        if (!TextUtils.isEmpty(etZpNote.getText().toString().trim())) {
            try {
                Double.parseDouble(etZpNote.getText().toString().trim());
                tilZpNote.setError(null);
            } catch (NumberFormatException e) {
                tilZpNote.setError(getString(R.string.error_invalid_number));
                isValid = false;
            }
        } else {
            tilZpNote.setError(null);
        }

        // LB-Note Validierung (optional, aber wenn eingegeben, dann gültige Zahl)
        if (!TextUtils.isEmpty(etLbNote.getText().toString().trim())) {
            try {
                Double.parseDouble(etLbNote.getText().toString().trim());
                tilLbNote.setError(null);
            } catch (NumberFormatException e) {
                tilLbNote.setError(getString(R.string.error_invalid_number));
                isValid = false;
            }
        } else {
            tilLbNote.setError(null);
        }

        return isValid;
    }

    /**
     * Hilfsmethode zum Parsen eines Strings in ein Double. Gibt null zurück, wenn der String leer ist.
     */
    private Double parseDouble(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

package ch.wiss.m335.modulnotenverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ch.wiss.m335.modulnotenverwaltung.data.Module;
import ch.wiss.m335.modulnotenverwaltung.data.ModuleDatabase;

/**
 * Die Hauptaufgabe der Modulverwaltungs-App.
 * Zeigt eine Liste der Module an und ermöglicht das Hinzufügen neuer Einträge.
 */
public class MainActivity extends AppCompatActivity {

    private LinearLayout llModules;
    private FloatingActionButton fabAddModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // R.layout sollte jetzt funktionieren

        llModules = findViewById(R.id.llModules);
        fabAddModule = findViewById(R.id.fabAddModule);

        fabAddModule.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ModuleFormActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadModules();
    }

    /**
     * Lädt alle Module aus der Datenbank und zeigt sie in der Liste an.
     */
    private void loadModules() {
        llModules.removeAllViews(); // Vor dem erneuten Laden alle bestehenden Views entfernen
        List<Module> modules = ModuleDatabase.getDatabase(this).moduleDao().getAll();

        if (modules != null) {
            for (Module module : modules) {
                addModuleToView(module);
            }
        }
    }

    /**
     * Fügt ein einzelnes Modul zur Liste in der View hinzu.
     */
    private void addModuleToView(Module module) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View moduleView = inflater.inflate(R.layout.item_module, llModules, false);

        TextView tvModuleNumber = moduleView.findViewById(R.id.tvModuleNumber);
        TextView tvModuleTitle = moduleView.findViewById(R.id.tvModuleTitle);
        TextView tvAverageNote = moduleView.findViewById(R.id.tvAverageNote);

        tvModuleNumber.setText(module.getModuleNumber());
        tvModuleTitle.setText(module.getModuleTitle());

        Double averageNote = module.getAverageNote();
        if (averageNote != null) {
            tvAverageNote.setText(getString(R.string.average_grade_format, averageNote));
            tvAverageNote.setVisibility(View.VISIBLE);
        } else {
            tvAverageNote.setVisibility(View.GONE);
        }

        // Klick-Listener für Bearbeitung
        moduleView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ModuleFormActivity.class);
            intent.putExtra("MODULE_ID", module.getId());
            startActivity(intent);
        });

        llModules.addView(moduleView);
    }
}

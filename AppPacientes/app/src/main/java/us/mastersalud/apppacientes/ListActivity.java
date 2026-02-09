package us.mastersalud.apppacientes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity {
    Spinner spinner;
    FirebaseDatabase database;

    DatabaseReference puntoAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recuperamos el Spinner (El desplegable de selección del
        // grupo del que se quiere listar los pacientes).
        spinner = findViewById(R.id.spinnerGrupos);
        // Hacemos el array con todos los elementos que queremos que tenga el desplegable:
        ArrayList<String> grupos = new ArrayList<>();
        grupos.add(Constantes.pacientes);
        grupos.addAll(Arrays.asList(Constantes.grupo));
        // Adaptamos el array para poder usarlo en el Spinner:
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                grupos);
        spinner.setAdapter(adapter);



        // Recuperamos el Recycler View que mostrará todos los pacientes
        RecyclerView recycler = findViewById(R.id.recyclerPacientes);
        // Creamos el adapter que usará el RecyclerView.
        PacientesAdapter recyclerAdapter = new PacientesAdapter();
        // Asociamos el manager y el adapter.
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(recyclerAdapter);


        //Recupero una instancia de mi base de datos
        database= FirebaseDatabase.getInstance("https://apppacientes-c6f8b-default-rtdb.europe-west1.firebasedatabase.app/");
        //Referencio a la raíz
        puntoAcceso = database.getReference();

        // Listener del Spinner: cuando cambie la selección, actualizamos el RecyclerView
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Utilizar el grupo seleccionado en el Spinner:
                String grupo_seleccionado = spinner.getSelectedItem().toString();
                // Se limpia el Adapter previo
                recyclerAdapter.clear();

                // Creamos un listener para ver si hay datos en el grupo seleccionado
                puntoAcceso.child(grupo_seleccionado).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Paciente p = snapshot.getValue(Paciente.class);
                        if (p != null) {
                            recyclerAdapter.add(p);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }
}
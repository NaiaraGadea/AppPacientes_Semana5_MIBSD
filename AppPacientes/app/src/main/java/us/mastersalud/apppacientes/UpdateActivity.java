package us.mastersalud.apppacientes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {
    //Atributos de la clase
    EditText nombre, apellidos, grupoSanguineo;
    String nuhsa;
    //TextView nombre,apellidos,grupoSanguineo;
    FirebaseDatabase database;
    DatabaseReference puntoAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Recupero el String de nuhsa que me pasó la anterior activity
        nuhsa=getIntent().getStringExtra(Constantes.nuhsa);

        // Recupero los distintos edit text con los nuevos nombres, apellidos y grupo sanguineo.
        nombre = findViewById(R.id.eTNombre);
        apellidos = findViewById(R.id.eTApellidos);
        grupoSanguineo = findViewById(R.id.eTGrupoSanguineo);

    }
    public void actualizarDatos(View view) {
        //Recupero una instancia de mi base de datos
        database=FirebaseDatabase.getInstance("https://apppacientes-c6f8b-default-rtdb.europe-west1.firebasedatabase.app/");
        //Referencio a la raíz
        puntoAcceso = database.getReference();

        //Listener que escucha si hay algún elemento colgando de ese punto de acceso
        puntoAcceso.child(Constantes.pacientes).child(nuhsa).addListenerForSingleValueEvent(new ValueEventListener() {
            // Se usa un addListenerForSingleValueEvent para que no se quede en bucle.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newNombre;
                String newApellidos;
                String newGrupoSanguineo;

                //Si no existe la dirección del nuhsa introducido rompemos el bucle e indicamos que no se ha encontrado.
                /*if (!dataSnapshot.exists()) {
                    Toast.makeText(UpdateActivity.this, "Paciente no existe", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                //Si no es un nuhsa vacío y hay algo colgando..
                if(!nuhsa.isEmpty() && dataSnapshot.getValue()!=null) {//Nuhsa encontrado
                    //Recuperamos el paciente y lo mostramos en cada uno de los text view
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);
                    // Obtengo la cadena de texto de los distintos edit text si no está vacía,
                    // y obtengo los nuevos parámetros del paciente. Si está vacía se mantienen los datos antiguos.
                    if(!nombre.getText().toString().equals("")){
                        newNombre = nombre.getText().toString();
                    }else{
                        newNombre = paciente.getNombre();
                    }

                    if(!apellidos.getText().toString().equals("")){
                        newApellidos = apellidos.getText().toString();
                    }else{
                        newApellidos = paciente.getApellidos();
                    }

                    if(!grupoSanguineo.getText().toString().equals("")){
                        newGrupoSanguineo = grupoSanguineo.getText().toString();
                    }else{
                        newGrupoSanguineo = paciente.getGrupoSanguineo();
                    }

                    //Genero un nuevo paciente con los datos modificados:
                    Paciente newPaciente = new Paciente(newNombre, newApellidos,newGrupoSanguineo,nuhsa);

                    // Primero borro de la lista general de pacientes
                    puntoAcceso.child(Constantes.pacientes).child(nuhsa).removeValue();

                    // Eliminamos el paciente de la base de datos por grupo sanguíneo
                    puntoAcceso.child(paciente.getGrupoSanguineo()).child(nuhsa).removeValue();

                    // Volvemos a crear el paciente en la base de datos con los datos actualizados:
                    //Navego con child y establezco como valor el objeto paciente ¡directamente!
                    puntoAcceso.child(Constantes.pacientes).child(nuhsa).setValue(newPaciente);
                    //Ahora navego para incluir ese mismo paciente colgando de su grupo sanguíneo
                    puntoAcceso.child(newGrupoSanguineo).child(nuhsa).setValue(newPaciente);

                    Toast.makeText(UpdateActivity.this, "Paciente actualizado: " + nuhsa, Toast.LENGTH_LONG).show();

                }else{
                    // Mostramos un mensaje de no encontrado y volvemos eliminando la activity
                    Toast.makeText(UpdateActivity.this,"Paciente no encontrado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
}
package us.mastersalud.apppacientes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuscaActivity extends AppCompatActivity {

    //Atributos de la clase
    String nuhsa;
    TextView nombre,apellidos,grupoSanguineo;
    FirebaseDatabase database;
    DatabaseReference puntoAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        //Recupero el String de nuhsa que me pasó la anterior activity
        nuhsa=getIntent().getStringExtra(Constantes.nuhsa);

        //Busco las vistas que tengo que modificar en mi interfaz
        nombre=findViewById(R.id.tvNombre);
        apellidos=findViewById(R.id.tvApellidos);
        grupoSanguineo=findViewById(R.id.tvGrupoSanguineo);

        //Instancia de mi base de datos
        database=FirebaseDatabase.getInstance("https://apppacientes-c6f8b-default-rtdb.europe-west1.firebasedatabase.app/");


        //Punto de acceso al nuhsa que se nos ha pasado
        puntoAcceso = database.getReference(Constantes.pacientes+"/"+nuhsa);

        //Listener que escucha si hay algún elemento colgando de ese punto de acceso
        puntoAcceso.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Si no es un nuhsa vacío (se podría controlar en la activity anterior) Y hay algo colgando..
                if(!nuhsa.equals("") && dataSnapshot.getValue()!=null) {//Nuhsa encontrado
                    //Recuperamos el paciente y lo mostramos en cada uno de los text view
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);
                    nombre.setText(paciente.getNombre());
                    apellidos.setText(paciente.getApellidos());
                    grupoSanguineo.setText(paciente.getGrupoSanguineo());
                }
                else{
                    //Mostramos un mensaje de no encontrado y volvemos eliminando la activity
                    Toast.makeText(BuscaActivity.this,"Paciente no encontrado",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
}

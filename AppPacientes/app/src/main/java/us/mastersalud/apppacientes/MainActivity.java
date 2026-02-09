package us.mastersalud.apppacientes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Atributos de la clase:
    //EditText para el texto de búsqueda de paciente por Nuhsa
    EditText ETnuhsa;
    //Atributos para la base de datos de Firebase
    FirebaseDatabase database;
    //y el punto de acceso del fichero JSON
    DatabaseReference puntoAcceso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lo único que voy a hacer es recuperar de la interfaz el EditText
        ETnuhsa=findViewById(R.id.editText);


    }

    //OnClick asignado desde el botón del Layout: Buscar Paciente
    //En vez de butonBusca.setOnClickListener...
    public void buscaPaciente(View view) {
        //Recupero la cadena de texto del EditText
        String nuhsa= ETnuhsa.getText().toString();

        //Voy a lanzar la activity BuscaActivity con  el parámetro del nuhsa recuperado mediante putExtra
        Intent intent=new Intent(MainActivity.this,BuscaActivity.class);
        intent.putExtra(Constantes.nuhsa,nuhsa);
        startActivity(intent);
    }


    //OnClick asignado desde el botón del Layout: Nuevo Paciente
    public void insertaPaciente(View view) {
        //Genero un paciente aleatorio
        Paciente paciente=generaPacienteAleatorio();

        //Recupero una instancia de mi base de datos
        database=FirebaseDatabase.getInstance("https://apppacientes-c6f8b-default-rtdb.europe-west1.firebasedatabase.app/");
        //Referencio a la raíz
        puntoAcceso = database.getReference();

        //Navego con child y establezco como valor el objeto paciente ¡directamente!
        puntoAcceso.child(Constantes.pacientes).child(paciente.getNuhsa()).setValue(paciente);
        //Ahora navego para incluir ese mismo paciente colgando de su grupo sanguíneo
        puntoAcceso.child(paciente.getGrupoSanguineo()).child(paciente.getNuhsa()).setValue(paciente);
        //Muestro un mensaje para indicar que ha ido bien
        Toast.makeText(this,paciente.toString(),Toast.LENGTH_LONG).show();

    }

    //OnClick asignado desde el botón del Layout: Borrar Paciente
    public void eliminaPaciente(View view) {
        // Se recupera la cadena de texto del EditText donde está el Nuhsa del paciente que se quiere eliminar.
        String nuhsa= ETnuhsa.getText().toString(); // O lo recuperas de un EditText

        // Se recupera la instancia de la base de datos y se referencia su raiz.
        database = FirebaseDatabase.getInstance("https://apppacientes-c6f8b-default-rtdb.europe-west1.firebasedatabase.app/");
        puntoAcceso = database.getReference();

        //Listener que escucha si hay algún elemento colgando de ese punto de acceso
        puntoAcceso.child(Constantes.pacientes).child(nuhsa).addListenerForSingleValueEvent(new ValueEventListener() {
        // Se usa un addListenerForSingleValueEvent para que no se quede en bucle.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Si no es un nuhsa vacío y hay algo colgando..
                if(!nuhsa.equals("") && dataSnapshot.getValue()!=null) {//Nuhsa encontrado
                    //Recuperamos el paciente y lo mostramos en cada uno de los text view
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);

                    // Primero borro de la lista general de pacientes
                    puntoAcceso.child(Constantes.pacientes).child(nuhsa).removeValue();

                    // Eliminamos el paciente de la base de datos por grupo sanguíneo
                    puntoAcceso.child(paciente.getGrupoSanguineo()).child(nuhsa).removeValue();


                    Toast.makeText(MainActivity.this, "Paciente eliminado: " + nuhsa, Toast.LENGTH_LONG).show();
                }else{
                    //Mostramos un mensaje de no encontrado y volvemos eliminando la activity
                    Toast.makeText(MainActivity.this,"Paciente no encontrado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void actualizarPaciente(View view) {
        //Recupero la cadena de texto del EditText para obtener el Nusha.
        String nuhsa= ETnuhsa.getText().toString();

        //Voy a lanzar la activity UpdateActivity con  el parámetro del nuhsa recuperado mediante putExtra.
        Intent intent=new Intent(MainActivity.this,UpdateActivity.class);
        intent.putExtra(Constantes.nuhsa,nuhsa);
        startActivity(intent);
    }


    private Paciente generaPacienteAleatorio(){
        final Random random = new Random();
        String nuhsa=Constantes.nuhsa+random.nextInt(9999);
        String grupo=Constantes.grupo[random.nextInt(8)];
        return(new Paciente(Constantes.nombre[random.nextInt(8)],
                Constantes.apellido[random.nextInt(8)]+" "+Constantes.apellido[random.nextInt(8)],
                grupo,
                nuhsa));
    }

    public void listarPacientes(View view) {
        //Voy a lanzar la activity BuscaActivity con  el parámetro del nuhsa recuperado mediante putExtra
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        //intent.putExtra(Constantes.nuhsa,nuhsa);
        startActivity(intent);

    }

}

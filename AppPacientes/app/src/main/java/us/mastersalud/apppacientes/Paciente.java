package us.mastersalud.apppacientes;

import com.google.firebase.database.Exclude;

public class Paciente {
    private String nombre;
    private String apellidos;
    private String grupoSanguineo;
    private String nuhsa;

    //Creamos constructor vacío (importante), constructor, getters y toString

    public Paciente() {
    }

    public Paciente(String nombre, String apellidos, String grupoSanguineo, String nuhsa) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.grupoSanguineo = grupoSanguineo;
        this.nuhsa = nuhsa;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public String getNuhsa() {
        return nuhsa;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", grupoSanguineo='" + grupoSanguineo + '\'' +
                ", nuhsa='" + nuhsa + '\'' +
                '}';
    }
}

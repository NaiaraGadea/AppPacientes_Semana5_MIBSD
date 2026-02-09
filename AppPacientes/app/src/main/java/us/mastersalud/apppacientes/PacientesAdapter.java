package us.mastersalud.apppacientes;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PacientesAdapter extends RecyclerView.Adapter<PacientesAdapter.PacienteVH> {
    
    private final List<Paciente> items = new ArrayList<>();

    public static class PacienteVH extends RecyclerView.ViewHolder {
        TextView nombre, apellidos, grupo, nuhsa;
        public PacienteVH(View v) {
            super(v);
            nombre = v.findViewById(R.id.tvItemNombre);
            apellidos = v.findViewById(R.id.tvItemApellidos);
            grupo = v.findViewById(R.id.tvItemGrupoSanguineo);
            nuhsa = v.findViewById(R.id.tvItemNuhsa);
        }
    }

    @Override
    public PacienteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pacientes, parent, false);
        return new PacienteVH(v);
    }

    @Override
    public void onBindViewHolder(PacienteVH holder, int position) {
        Paciente p = items.get(position);
        holder.nombre.setText(p.getNombre());
        holder.apellidos.setText(p.getApellidos());
        holder.grupo.setText(p.getGrupoSanguineo());
        holder.nuhsa.setText(p.getNuhsa());
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void add(Paciente p) { items.add(p); notifyItemInserted(items.size()-1); }
    public void clear() { items.clear(); notifyDataSetChanged(); }
}

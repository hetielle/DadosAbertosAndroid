package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.R;

import java.util.List;

import models.Partido;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private List<Partido> partidoList;
    private OnItemClickListener listener;

    public PartidoAdapter(List<Partido> partidoList, OnItemClickListener listener) {
        this.partidoList = partidoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partido, parent, false);
        return new PartidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        Partido partido = partidoList.get(position);

        holder.txtNome.setText(partido.getNome());
        holder.txtSigla.setText(partido.getSigla());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(partido);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return partidoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Partido partido);
    }

    public class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        TextView txtSigla;

        public PartidoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomePartido);
            txtSigla = itemView.findViewById(R.id.txtPartidoSigla);
        }
    }
}
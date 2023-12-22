package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.R;

import java.util.List;

import models.Deputado;

public class DeputadoAdapter extends RecyclerView.Adapter<DeputadoAdapter.DeputadoViewHolder> {

    private final List<Deputado> deputados;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Deputado deputado);
    }

    public DeputadoAdapter(List<Deputado> deputados, OnItemClickListener listener) {
        this.deputados = deputados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeputadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View deputadoView = inflater.inflate(com.example.trabalhofinal.R.layout.item_deputado, parent, false);
        return new DeputadoViewHolder(deputadoView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeputadoViewHolder holder, int position) {
        Deputado deputado = deputados.get(position);
        holder.bind(deputado, listener);
    }

    @Override
    public int getItemCount() {
        return deputados.size();
    }

    static class DeputadoViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNome;
        private final TextView txtPartidoSigla;

        DeputadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeDeputado);
            txtPartidoSigla = itemView.findViewById(R.id.txtDeputadoSigla);
        }

        void bind(final Deputado deputado, final OnItemClickListener listener) {
            txtNome.setText(deputado.getNome());
            txtPartidoSigla.setText(deputado.getSiglaPartido());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(deputado);
                }
            });
        }
    }
}

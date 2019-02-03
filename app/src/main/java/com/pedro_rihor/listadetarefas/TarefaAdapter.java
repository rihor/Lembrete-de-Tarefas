package com.pedro_rihor.listadetarefas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TarefaAdapter extends ListAdapter<Tarefa, TarefaAdapter.TarefaHolder> {

    private CallbackInterface mCallback;
    private OnCheckboxListener listener;

    TarefaAdapter(Context context) {
        super(DIFF_CALLBACK);

        // coloca a interface
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }

    }

    private static final DiffUtil.ItemCallback<Tarefa> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tarefa>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tarefa oldItem, @NonNull Tarefa newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tarefa oldItem, @NonNull Tarefa newItem) {
//            return oldItem.isEstado() == newItem.isEstado();
            return false;
        }
    };

    @Override
    public void onBindViewHolder(@NonNull final TarefaHolder tarefaHolder, final int position) {
        Tarefa tarefaAtual = getItem(position);
        tarefaHolder.textViewDescricao.setText(tarefaAtual.getDescricao());
        tarefaHolder.checkBoxEstado.setChecked(tarefaAtual.isEstado());

        // clique no item da lista
        tarefaHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getTarefaAt(position), position, tarefaHolder);
                }
            }
        });
    }

    @NonNull
    @Override
    public TarefaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarefa_item, viewGroup, false);

        return new TarefaHolder(itemView);
    }

    public interface CallbackInterface {
        void onHandleSelection(Tarefa tarefa, int position, TarefaHolder tarefaHolder);
    }

    Tarefa getTarefaAt(int position) {
        return getItem(position);
    }

    void setOnCheckboxListener(OnCheckboxListener listener) {
        this.listener = listener;
    }

    public interface OnCheckboxListener {
        void onCheckboxClick(Tarefa tarefa, boolean estado);
    }

    class TarefaHolder extends RecyclerView.ViewHolder {
        TextView textViewDescricao;
        CheckBox checkBoxEstado;
        RelativeLayout layoutItem;
        CardView cardView;

        TarefaHolder(@NonNull final View itemView) {
            super(itemView);
            textViewDescricao = itemView.findViewById(R.id.text_view_descricao);
            checkBoxEstado = itemView.findViewById(R.id.checkbox_estado);
            layoutItem = itemView.findViewById(R.id.layout_item);
            cardView = itemView.findViewById(R.id.card_view_item);

            checkBoxEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCheckboxClick(getItem(position), checkBoxEstado.isChecked());
                    }
                }
            });
        }
    }
}

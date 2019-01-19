package com.pedro_rihor.listadetarefas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TarefaAdapter extends ListAdapter<Tarefa, TarefaAdapter.TarefaHolder> {
    private int previousExpandedPosition = -1;
    private int mExpandedPosition = -1;

    private static final DiffUtil.ItemCallback<Tarefa> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tarefa>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tarefa oldItem, @NonNull Tarefa newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tarefa oldItem, @NonNull Tarefa newItem) {
            return oldItem.getDescricao().equals(newItem.getDescricao());
        }
    };
    private OnCheckboxListener listener;

    TarefaAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TarefaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarefa_item, viewGroup, false);

        return new TarefaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaHolder tarefaHolder, int position) {
        Tarefa tarefaAtual = getItem(position);
        tarefaHolder.textViewNumero.setText(String.valueOf(position + 1)); // é a posição que fica visivel, não o id do elemento
        tarefaHolder.textViewDescricao.setText(tarefaAtual.getDescricao());
        tarefaHolder.checkBoxEstado.setChecked(tarefaAtual.isEstado());
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
        private TextView textViewNumero;
        private TextView textViewDescricao;
        private CheckBox checkBoxEstado;
        private RelativeLayout layoutItem;

        TarefaHolder(@NonNull final View itemView) {
            super(itemView);
            textViewNumero = itemView.findViewById(R.id.text_view_number);
            textViewDescricao = itemView.findViewById(R.id.text_view_descricao);
            checkBoxEstado = itemView.findViewById(R.id.checkbox_estado);
            layoutItem = itemView.findViewById(R.id.layout_item);

            checkBoxChange();
            clickItem();
        }

        void clickItem() {
            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("*****************" + textViewDescricao.getText());
                }
            });
        }

        // listener para checkbox de cada item
        void checkBoxChange() {
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

package com.pedro_rihor.listadetarefas;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TarefaAdapter extends ListAdapter<Tarefa, TarefaAdapter.TarefaHolder> {
    final static String EXTRA_DESCRICAO = "com.pedro_rihor.listadetarefas.text_descricao";
    final static String EXTRA_CHECKBOX = "com.pedro_rihor.listadetarefas.checkbox";
    final static String EXTRA_NUMERO = "com.pedro_rihor.listadetarefas.text_numero";

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
    private final Context context;
    private OnCheckboxListener listener;

    TarefaAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        private CardView cardView;
        private FloatingActionButton fab;

        TarefaHolder(@NonNull final View itemView) {
            super(itemView);
            textViewNumero = itemView.findViewById(R.id.text_view_number);
            textViewDescricao = itemView.findViewById(R.id.text_view_descricao);
            checkBoxEstado = itemView.findViewById(R.id.checkbox_estado);
            layoutItem = itemView.findViewById(R.id.layout_item);
            cardView = itemView.findViewById(R.id.card_view_item);
            if (context instanceof MainActivity) {
                fab = ((MainActivity) context).getFab();
            }

            checkBoxChange();
            clickItem();
        }

        void clickItem() {
            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.putExtra(EXTRA_DESCRICAO, textViewDescricao.getText());
                    intent.putExtra(EXTRA_CHECKBOX, checkBoxEstado.isChecked());
                    intent.putExtra(EXTRA_NUMERO, textViewNumero.getText());
                    // define os pares
                    Pair<View, String> pairNumero = Pair.create((View) textViewNumero, textViewNumero.getTransitionName());
                    Pair<View, String> pairText = Pair.create((View) textViewDescricao, textViewDescricao.getTransitionName());
                    Pair<View, String> pairCard = Pair.create((View) cardView, cardView.getTransitionName());
                    Pair<View, String> pairFab = Pair.create((View) fab, fab.getTransitionName());

                    ActivityOptions transitionAnimation = ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) itemView.getContext(),
                            pairNumero,
                            pairText,
                            pairCard,
                            pairFab);

                    // inicia a activity
                    itemView.getContext().startActivity(intent, transitionAnimation.toBundle());
                    // para o brilho branco que ocorre antes no meio da transição
                    ((Activity) itemView.getContext()).getWindow().setExitTransition(null);
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

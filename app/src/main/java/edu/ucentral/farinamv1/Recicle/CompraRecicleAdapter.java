package edu.ucentral.farinamv1.Recicle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.ListaCompras;

public class CompraRecicleAdapter extends RecyclerView.Adapter<CompraRecicleAdapter.CompraRecicleViewHolder> {

    private List<ListaCompras> listaComprasList;
    private ItemListener mItemListener;

    @NonNull
    @Override
    public CompraRecicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        return new CompraRecicleViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recetas_list_compra,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CompraRecicleViewHolder  holder, int position) {
        ListaCompras item = listaComprasList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return listaComprasList == null ? 0 : listaComprasList.size();
    }

    public void setItems(List<ListaCompras> items) {
        listaComprasList = items;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener listener) {
        mItemListener = listener;
    }

    interface ItemListener {

        void onClick(ListaCompras shoppingList);

        void onDeleteIconClicked(ListaCompras shoppingList);
    }

    public class CompraRecicleViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitulo;
        private final TextView mFecha;
        private final TextView btnEliminar;

        public CompraRecicleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.compra_titulo);
            mFecha = itemView.findViewById(R.id.compra_fecha);
            btnEliminar = itemView.findViewById(R.id.compra_btn_eliminar);

            // Setear eventos
            btnEliminar.setOnClickListener(this::manageEvents);
            itemView.setOnClickListener(this::manageEvents);
        }

        private void manageEvents(View view) {
            if (mItemListener != null) {
                ListaCompras clickedItem = listaComprasList.get(getAdapterPosition());
                // Manejar evento de click en Eliminar
                if (view.getId() == R.id.compra_btn_eliminar) {
                    mItemListener.onDeleteIconClicked(clickedItem);
                    return;
                }
                mItemListener.onClick(clickedItem);
            }
        }

        public void bind(ListaCompras item) {
            mTitulo.setText(item.getTitulo());
            mFecha.setText(String.valueOf(item.getFecha()));
        }
    }
}

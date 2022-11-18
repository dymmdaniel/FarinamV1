package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.Recicle.CompraRecicleAdapter;
import edu.ucentral.farinamv1.model.ListaCompras;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoListaCompras extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private RecyclerView toDoList;
    private CompraRecicleAdapter compraRecicleAdapter;
    private FloatingActionButton btnNuevaLista;

    private String usuarioId;

    public FragmentoListaCompras() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmento_lista_compras, container, false);
        iniciarFirebase();
        Bundle arguments=getArguments();
        usuarioId=arguments.getString("usuarioId");
        toDoList=view.findViewById(R.id.to_do_list_compras);
        btnNuevaLista=view.findViewById(R.id.add_new_item);
        compraRecicleAdapter = new CompraRecicleAdapter();
        toDoList.setAdapter(compraRecicleAdapter);
        /*compraRecicleAdapter.setItemListener(new CompraRecicleAdapter.ItemListener() {
            @Override
            public void onClick(ListaCompras shoppingList) {

            }

            @Override
            public void onDeleteIconClicked(ListaCompras shoppingList) {
                eliminar(shoppingList);
            }
        });*/


        return view;
    }

    public void eliminar(ListaCompras shoppingList){

    }

    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
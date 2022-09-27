package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.Recicle.RecetaRecicle;
import edu.ucentral.farinamv1.model.Receta;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoMiLista extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerViewRecetas;
    private RecetaRecicle recetaRecicle;
    private ArrayList<Receta> recetas;
    private String usuarioId;

    public FragmentoMiLista() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragmento_mi_lista, container, false);
        iniciarFirebase();
        
        return view;
    }
    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private RecetaRecicle recetaRecicle;
    private ArrayList<Receta> recetas;
    private String usuarioId;
    private RecyclerView list_view_mi_lista;

    public FragmentoMiLista() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragmento_mi_lista, container, false);
        iniciarFirebase();
        Bundle arguments=getArguments();
        usuarioId=arguments.getString("usuarioId");
        recetas=new ArrayList<>();
        list_view_mi_lista=view.findViewById(R.id.list_view_mi_lista);
        recetaRecicle =new RecetaRecicle(recetas);
        list_view_mi_lista.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recetaRecicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result=new Bundle();
                result.putString("recetaId",recetas.get(list_view_mi_lista.getChildAdapterPosition(view)).getRecetaId());
                result.putInt("Enable",0);
                Fragment fragment = new FragmentoReceta();
                fragment.setArguments(result);
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        list_view_mi_lista.setAdapter(recetaRecicle);

        databaseReference.child("Receta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Receta receta=objSnapshot.getValue(Receta.class);
                    if(receta.getUsuarioId().equals(usuarioId)){
                        recetas.add(receta);
                        recetaRecicle.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
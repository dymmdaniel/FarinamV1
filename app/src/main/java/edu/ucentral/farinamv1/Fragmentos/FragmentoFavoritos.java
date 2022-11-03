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
import edu.ucentral.farinamv1.model.Favoritos;
import edu.ucentral.farinamv1.model.Receta;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class FragmentoFavoritos extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerViewRecetas;
    private RecetaRecicle recetaRecicle;
    private ArrayList<Receta> recetas;
    private String usuarioId;

    public FragmentoFavoritos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragmento_favoritos, container, false);
        // Inflate the layout for this fragment
        Bundle arguments=getArguments();
        usuarioId=arguments.getString("usuarioId");
        recetas=new ArrayList<>();
        iniciarFirebase();
        recyclerViewRecetas=view.findViewById(R.id.list_view_main_recetas);
        recetaRecicle =new RecetaRecicle(recetas);
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recetaRecicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result=new Bundle();
                result.putString("recetaId",recetas.get(recyclerViewRecetas.getChildAdapterPosition(view)).getRecetaId());
                result.putInt("Enable",0);
                result.putInt("editable",0);
                Fragment fragment = new FragmentoReceta();
                fragment.setArguments(result);
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerViewRecetas.setAdapter(recetaRecicle);

        databaseReference.child("Favoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recetas.clear();
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Favoritos favoritos=objSnapshot.getValue(Favoritos.class);
                    if(favoritos.getUsuarioId().equals(usuarioId)){
                        for(int i=0;i<favoritos.getRecetas().size();i++){
                            recetas.add(favoritos.getRecetas().get(i));
                            recetaRecicle.notifyDataSetChanged();
                        }
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
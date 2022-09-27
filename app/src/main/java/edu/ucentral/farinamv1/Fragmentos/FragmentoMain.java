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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.Recicle.RecetaRecicle;
import edu.ucentral.farinamv1.model.Receta;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoMain extends Fragment {

    private RecyclerView listViewRecetas;
    private List<Receta> recetas;
    private RecetaRecicle recetaRecicle;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public FragmentoMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragmento_main,container,false);

        recetas=new ArrayList<>();


        listViewRecetas=view.findViewById(R.id.list_view_favoritos);

        recetaRecicle =new RecetaRecicle(recetas);
        listViewRecetas.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recetaRecicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result=new Bundle();
                result.putString("recetaId",recetas.get(listViewRecetas.getChildAdapterPosition(view)).getRecetaId());
                result.putInt("Enable",1);
                Fragment fragment = new FragmentoReceta();
                fragment.setArguments(result);
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        listViewRecetas.setAdapter(recetaRecicle);

        iniciarFirebase();

        databaseReference.child("Receta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recetas.clear();
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Receta receta=objSnapshot.getValue(Receta.class);
                    recetas.add(receta);
                    recetaRecicle.notifyDataSetChanged();
                }
                //recetaRecicle =new RecetaRecicle(recetas);
                //listViewRecetas.setAdapter(recetaRecicle);
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
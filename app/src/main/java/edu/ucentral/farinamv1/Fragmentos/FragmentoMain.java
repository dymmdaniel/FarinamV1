package edu.ucentral.farinamv1.Fragmentos;

import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.Recicle.RecetaRecicle;
import edu.ucentral.farinamv1.model.Categoria;
import edu.ucentral.farinamv1.model.Receta;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoMain extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView listViewRecetas;
    private List<Receta> recetas;
    private List<Receta> recetas_original;
    private RecetaRecicle recetaRecicle;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private SearchView buscador;
    private Spinner spinnerCategorias;
    List<Receta> recetas_temp=new ArrayList<>();

    private ArrayList<Categoria> categorias=new ArrayList<>();
    private ArrayAdapter<Categoria> categoriaArrayAdapter;

    public FragmentoMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragmento_main,container,false);

        recetas=new ArrayList<>();
        recetas_original=new ArrayList<>();

        spinnerCategorias=view.findViewById(R.id.spinner_main_categoria);
        buscador=view.findViewById(R.id.buscador);
        listViewRecetas=view.findViewById(R.id.list_view_main_recetas);
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
                    recetas_original.add(receta);
                    recetas_temp.add(receta);
                    recetaRecicle.notifyDataSetChanged();
                }
                //recetaRecicle =new RecetaRecicle(recetas);
                //listViewRecetas.setAdapter(recetaRecicle);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Categoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categorias.clear();
                Categoria categoria_temp=new Categoria();
                categoria_temp.setNombre("Selecciona una categoria");
                categorias.add(categoria_temp);
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Categoria categoria=objSnapshot.getValue(Categoria.class);
                    categorias.add(categoria);
                }
                categoriaArrayAdapter=new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,categorias){
                    @Override
                    public boolean isEnabled(int position){
                        return position != 0;
                    }
                    @Override
                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
                        View view=super.getDropDownView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        if(position == 0){
                            textView.setTextColor(Color.GRAY);
                        }else{
                            textView.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                categoriaArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerCategorias.setAdapter(categoriaArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buscador.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams layoutParams = buscador.getLayoutParams();
                layoutParams.width = (int) (348 * buscador.getResources().getDisplayMetrics().density);
                view.setLayoutParams(layoutParams);
            }
        });
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ViewGroup.LayoutParams layoutParams = buscador.getLayoutParams();
                layoutParams.width = (int) (55 * buscador.getResources().getDisplayMetrics().density);
                buscador.setLayoutParams(layoutParams);
                return false;
            }
        });
        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    filtrarCategoria(String.valueOf(adapterView.getItemAtPosition(i)));
                    //Toast.makeText(view.getContext(),""+adapterView.getItemAtPosition(i),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buscador.setOnQueryTextListener(this);
        return view;
    }
    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void filtrarCategoria(String categoria){
        //Toast.makeText(getView().getContext(),""+recetas.size(),Toast.LENGTH_SHORT).show();
        recetas.clear();
        recetas.addAll(recetas_original);
        recetas_temp=new ArrayList<>();
        for(int i=0;i<recetas.size();i++){
            if(recetas.get(i).getCategoriaId().equals(categoria)) {
                recetas_temp.add(recetas.get(i));
            }
        }
        recetas.clear();
        recetas.addAll(recetas_temp);
        recetaRecicle.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(s.equals("")){
            recetas.clear();
            recetas.addAll(recetas_temp);
        }else{
            //recetas.clear();
            ArrayList<Receta> recetas_temp_2=new ArrayList<>();
            for(Receta obj:recetas){
                StringBuilder sb=new StringBuilder();
                String ingredientes="";
                //Toast.makeText(getView().getContext(),"Oe "+obj.getIngredient(),Toast.LENGTH_SHORT).show();
                if(!obj.getIngredient().isEmpty()){
                    int i=0;
                    for(i=0;i<obj.getIngredient().size();i++){
                        sb.append(obj.getIngredient().get(i).getNombre().toLowerCase());
                        sb.append(", ");
                    }
                    ingredientes= String.valueOf(sb);
                    //sb.append(obj.getIngredient().get(i).getNombre());
                    if(ingredientes.contains(s)){
                        recetas_temp_2.add(obj);
                        //Toast.makeText(getView().getContext(),ingredientes,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            recetas.clear();
            recetas.addAll(recetas_temp_2);
        }
        recetaRecicle.notifyDataSetChanged();
        return false;
    }
}
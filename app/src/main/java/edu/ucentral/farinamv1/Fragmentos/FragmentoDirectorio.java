package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucentral.farinamv1.CrearReceta;
import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.Directorio;
import edu.ucentral.farinamv1.model.Ingrediente;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoDirectorio extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private Button btnAgregar;
    private ListView listViewDirectorio;
    private List<Directorio> directorioList=new ArrayList<>();
    private ArrayAdapter<Directorio> directorioArrayAdapter;

    private EditText txNombre;
    private EditText txNumero;

    public FragmentoDirectorio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragmento_directorio, container, false);
        iniciarFirebase();
        btnAgregar=view.findViewById(R.id.btn_agregar_directorio);
        listViewDirectorio=view.findViewById(R.id.list_directorio);
        txNombre=view.findViewById(R.id.txt_ingresar_nombre_dt);
        txNumero=view.findViewById(R.id.txt_ingresar_numero_dt);

        databaseReference.child("Directorio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                directorioList.clear();
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Directorio directorio=objSnapshot.getValue(Directorio.class);
                    if(directorio.getUsuarioId().equals(mAuth.getCurrentUser().getUid())){
                        directorioList.add(directorio);
                    }
                }
                directorioArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.simple_list_delete,directorioList);
                listViewDirectorio.setAdapter(directorioArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txNombre.getText().toString().isEmpty() || !txNumero.getText().toString().isEmpty()){
                    Directorio directorio=new Directorio();
                    directorio.setUsuarioId(mAuth.getCurrentUser().getUid());
                    directorio.setNombre(txNombre.getText().toString());
                    directorio.setNumero(txNumero.getText().toString());
                    directorioList.add(directorio);
                    txNumero.setText("");
                    txNombre.setText("");
                    directorioArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.simple_list_delete,directorioList);
                    listViewDirectorio.setAdapter(directorioArrayAdapter);
                    for(int i=0;i<directorioList.size();i++){
                        databaseReference.child("Directorio").child(mAuth.getCurrentUser().getUid()+"|"+i).setValue(directorioList.get(i));
                    }
                }else{

                }
            }
        });

        listViewDirectorio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Directorio directorio=new Directorio();
                directorio= (Directorio) adapterView.getItemAtPosition(i);
                directorioList.remove(directorio);
                directorioArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.simple_list_delete,directorioList);
                listViewDirectorio.setAdapter(directorioArrayAdapter);
                databaseReference.child("Directorio").child(mAuth.getCurrentUser().getUid()+"|"+i).removeValue();
            }
        });
        return view;
    }
    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
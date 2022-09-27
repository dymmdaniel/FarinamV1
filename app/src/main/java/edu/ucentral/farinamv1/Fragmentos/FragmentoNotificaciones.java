package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.Notificacion;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoNotificaciones extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ListView listViewNotificaciones;
    private ArrayAdapter<Notificacion> notificacionArrayAdapter;
    private ArrayList<Notificacion> notificaciones=new ArrayList<>();

    public FragmentoNotificaciones() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragmento_notificaciones, container, false);
        // Inflate the layout for this fragment
        iniciarFirebase();
        String usuarioId= mAuth.getCurrentUser().getUid();
        listViewNotificaciones=view.findViewById(R.id.list_view_notificaciones);

        databaseReference.child("Notificacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Notificacion notificacion=objSnapshot.getValue(Notificacion.class);
                    if(notificacion.getUsuarioId().equals(usuarioId)){
                        notificaciones.add(notificacion);
                    }
                }
                notificacionArrayAdapter=new ArrayAdapter<>(view.getContext(),R.layout.simple_list_delete,notificaciones);
                listViewNotificaciones.setAdapter(notificacionArrayAdapter);
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
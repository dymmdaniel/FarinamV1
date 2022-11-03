package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.Directorio;
import edu.ucentral.farinamv1.model.Notificacion;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoNotificaciones extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ListView listViewNotificaciones;
    private TextView backgroundNotificacion;
    private ArrayAdapter<Notificacion> notificacionArrayAdapter;
    private ArrayList<Notificacion> notificaciones=new ArrayList<>();
    private String usuarioId;
    public FragmentoNotificaciones() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragmento_notificaciones, container, false);
        // Inflate the layout for this fragment
        iniciarFirebase();
        Bundle arguments=getArguments();
        usuarioId=arguments.getString("usuarioId");

        listViewNotificaciones=view.findViewById(R.id.list_view_notificaciones);
        backgroundNotificacion=view.findViewById(R.id.background_notificacion);
        backgroundNotificacion.setVisibility(View.GONE);
        notificaciones.clear();
        if(notificaciones.isEmpty()){
            backgroundNotificacion.setVisibility(View.VISIBLE);
        }
        databaseReference.child("Notificacion").orderByChild("usuarioReceta").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Notificacion notificacion=snapshot.getValue(Notificacion.class);
                //Toast.makeText(getContext(), snapshot.toString(), Toast.LENGTH_SHORT).show();
                if(notificacion.getUsuarioReceta().equals(usuarioId)){
                   notificaciones.add(notificacion);
                }
                if(notificaciones.isEmpty()){
                    backgroundNotificacion.setVisibility(View.VISIBLE);
                }else{
                    notificaciones.clear();
                }
                notificacionArrayAdapter=new ArrayAdapter<>(view.getContext(),R.layout.simple_list_delete,notificaciones);
                listViewNotificaciones.setAdapter(notificacionArrayAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listViewNotificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notificacion notificacion=new Notificacion();
                notificacion= (Notificacion) adapterView.getItemAtPosition(i);
                notificaciones.remove(notificacion);
                notificacionArrayAdapter=new ArrayAdapter<>(view.getContext(),R.layout.simple_list_delete,notificaciones);
                //Toast.makeText(getContext(), notificacion.toString(), Toast.LENGTH_SHORT).show();
                listViewNotificaciones.setAdapter(notificacionArrayAdapter);
                databaseReference.child("Notificacion").child(notificacion.getId()).removeValue();
                //databaseReference.child("Notificacion").child().removeValue();
            }
        });
        return view;
    }
    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
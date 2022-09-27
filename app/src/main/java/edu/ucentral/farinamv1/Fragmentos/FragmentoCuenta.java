package edu.ucentral.farinamv1.Fragmentos;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucentral.farinamv1.Perfil;
import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.Favoritos;
import edu.ucentral.farinamv1.model.Receta;
import edu.ucentral.farinamv1.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoCuenta extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private CircleImageView imagenUsuario;
    private TextView nombre;
    private TextView countRecetas;
    private TextView countFavoritos;

    private int countFav=0;
    private int countRec=0;


    public FragmentoCuenta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmento_cuenta, container, false);
        iniciarFirebase();
        imagenUsuario=view.findViewById(R.id.usuario_imagen_perfil);
        nombre=view.findViewById(R.id.usuario_nombre_perfil);
        countRecetas=view.findViewById(R.id.txt_count_recetas);
        countFavoritos=view.findViewById(R.id.txt_count_favoritos);

        databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                nombre.setText(usuario.getNombre());
                if (!usuario.getImage().equals("")) {
                    Glide.with(view.getContext())
                            .load(Uri.parse(usuario.getImage()))
                            .into(imagenUsuario);
                }else{
                    imagenUsuario.setBackgroundResource(R.drawable.icon_photo);
                }
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

        databaseReference.child("Favoritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Favoritos favoritos=objSnapshot.getValue(Favoritos.class);
                    if(favoritos.getUsuarioId().equals(mAuth.getCurrentUser().getUid())){
                        for(int i=0;i<favoritos.getRecetas().size();i++){
                            countFav++;
                        }
                    }
                }
                countFavoritos.setText(String.valueOf(countFav));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Receta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Receta receta=objSnapshot.getValue(Receta.class);
                    if(receta.getUsuarioId().equals(nombre.getText().toString())){
                        countRec++;
                    }
                }
                countRecetas.setText(String.valueOf(countRec));
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
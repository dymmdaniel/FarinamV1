package edu.ucentral.farinamv1.Fragmentos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ucentral.farinamv1.CrearReceta;
import edu.ucentral.farinamv1.MainActivity;
import edu.ucentral.farinamv1.Perfil;
import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.Favoritos;
import edu.ucentral.farinamv1.model.Ingrediente;
import edu.ucentral.farinamv1.model.Notificacion;
import edu.ucentral.farinamv1.model.Receta;
import edu.ucentral.farinamv1.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoReceta extends Fragment {

    private List<Ingrediente> ingredientes;
    private ArrayAdapter<Ingrediente> ingredienteArrayAdapter;
    private ListView listViewIngredientes;
    private ImageView imagenViewReceta;
    private TextView titulo;
    private TextView descripcion;
    private RatingBar dificultad;
    private RatingBar valoracion;
    private TextView tiempo;
    private TextView calorias;
    private Button siguiete;
    private Button regresar;
    private TextView pasos;
    private Button btnAgregarFavoritos;
    private Button btnEditarReceta;

    private TextView texView11;
    private TextView texView14;
    private TextView texView15;
    private TextView texView17;
    private TextView texView20;
    private String usuarioNombre;


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Receta receta;

    public FragmentoReceta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments=getArguments();
        String recetaId=arguments.getString("recetaId");
        int enable=arguments.getInt("Enable");
        int editable=arguments.getInt("editable");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receta, container, false);
        iniciarFirebase();
        listViewIngredientes = view.findViewById(R.id.list_receta_ingredientes);
        imagenViewReceta = view.findViewById(R.id.image_view_receta);
        titulo = view.findViewById(R.id.txt_receta_titulo);
        descripcion = view.findViewById(R.id.txt_receta_descripcion);
        dificultad = view.findViewById(R.id.rating_receta_dificultad);
        valoracion = view.findViewById(R.id.rating_receta_valoracion);
        tiempo = view.findViewById(R.id.txt_receta_tiempo);
        calorias = view.findViewById(R.id.txt_receta_calorias);
        siguiete = view.findViewById(R.id.btn_receta_siguiente);
        regresar = view.findViewById(R.id.btn_receta_regresar);
        pasos = view.findViewById(R.id.txt_receta_pasos);
        texView11 = view.findViewById(R.id.text_view_11);
        texView14 = view.findViewById(R.id.textView14);
        texView15 = view.findViewById(R.id.textView15);
        texView17 = view.findViewById(R.id.textView17);
        texView20 = view.findViewById(R.id.textView20);
        btnAgregarFavoritos = view.findViewById(R.id.btn_agregar_favoritos);
        btnEditarReceta = view.findViewById(R.id.btn_editar_receta);
        btnEditarReceta.setVisibility(View.GONE);
        if(enable==0){
            btnAgregarFavoritos.setVisibility(View.GONE);
        }
        if(editable==1){
            btnEditarReceta.setVisibility(View.VISIBLE);
        }
        primerView(View.GONE);
        siguiete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segundaView(View.GONE);
                primerView(View.VISIBLE);
            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primerView(View.GONE);
                segundaView(View.VISIBLE);
            }
        });
        btnAgregarFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarFavoritos(view);
            }
        });
        btnEditarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarReceta();
            }
        });
        cargarDatos(recetaId, view);
        return view;
    }

    public void editarReceta(){

    }

    public void agregarFavoritos(View view) {
        String usuarioId = mAuth.getCurrentUser().getUid();
        databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                usuarioNombre=usuario.getNombre();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query findFavorito = databaseReference.child("Favoritos").orderByChild("usuarioId").equalTo(usuarioId);
        findFavorito.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Favoritos favoritos = snapshot.getValue(Favoritos.class);
                    ArrayList<Receta> recetaArrayList;
                    if(favoritos.getRecetas()==null){
                        recetaArrayList=new ArrayList<>();
                    }else{
                        recetaArrayList=favoritos.getRecetas();
                    }
                    recetaArrayList.add(receta);
                    Notificacion notificacion=new Notificacion();
                    notificacion.setUsuarioId(usuarioId);
                    notificacion.setNotificacion("El usuario "+usuarioNombre+ " ha guardado la receta "+receta.getTitulo()+"!");
                    notificacion.setUsuarioReceta(receta.getUsuarioId());
                    String idNotificacion=UUID.randomUUID().toString();
                    notificacion.setId(idNotificacion);
                    databaseReference.child("Notificacion").child(idNotificacion).setValue(notificacion);
                    favoritos.setRecetas(recetaArrayList);
                    databaseReference.child("Favoritos").child(usuarioId).setValue(favoritos);
                }else{
                    Favoritos favoritos = new Favoritos();
                    favoritos.setUsuarioId(usuarioId);
                    ArrayList<Receta> recetaArrayList = new ArrayList<>();
                    recetaArrayList.add(receta);
                    favoritos.setRecetas(recetaArrayList);
                    Notificacion notificacion=new Notificacion();
                    notificacion.setUsuarioId(receta.getUsuarioId());
                    notificacion.setNotificacion("El usuario "+usuarioNombre+ " ha guardado la receta "+receta.getTitulo()+"!");
                    databaseReference.child("Notificacion").child(usuarioId).setValue(notificacion);
                    Toast.makeText(getContext(), notificacion.getNotificacion(), Toast.LENGTH_SHORT).show();
                    databaseReference.child("Favoritos").child(usuarioId).setValue(favoritos);
                }
                Snackbar.make(
                        view.findViewById(R.id.btn_agregar_favoritos),
                        "Se ha agregado a tus favoritos",
                        BaseTransientBottomBar.LENGTH_SHORT
                ).show();
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
    }

    public void cargarDatos(String recetaId, View view) {
        databaseReference.child("Receta").orderByChild("recetaId").equalTo(recetaId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                receta = snapshot.getValue(Receta.class);
                Glide.with(view.getContext())
                        .load(Uri.parse(receta.getImage()))
                        .into(imagenViewReceta);
                titulo.setText(receta.getTitulo());
                descripcion.setText(receta.getDescripcion());
                dificultad.setRating((float) receta.getDificultad());
                dificultad.setEnabled(false);
                valoracion.setRating((float) receta.getValoracion());
                valoracion.setEnabled(false);
                tiempo.setText(String.valueOf(receta.getTiempo()) + " Min");
                calorias.setText(receta.getCalorias());
                pasos.setText(receta.getPasos());
                ingredienteArrayAdapter = new ArrayAdapter<Ingrediente>(view.getContext(), android.R.layout.simple_list_item_1, receta.getIngredient());
                listViewIngredientes.setAdapter(ingredienteArrayAdapter);
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
    }

    public void segundaView(int view) {
        imagenViewReceta.setVisibility(view);
        titulo.setVisibility(view);
        descripcion.setVisibility(view);
        texView11.setVisibility(view);
        texView14.setVisibility(view);
        texView15.setVisibility(view);
        texView17.setVisibility(view);
        dificultad.setVisibility(view);
        valoracion.setVisibility(view);
        tiempo.setVisibility(view);
        calorias.setVisibility(view);
        siguiete.setVisibility(view);
    }

    public void primerView(int view) {
        pasos.setVisibility(view);
        listViewIngredientes.setVisibility(view);
        texView20.setVisibility(view);
        regresar.setVisibility(view);
    }

    public void iniciarFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }
}
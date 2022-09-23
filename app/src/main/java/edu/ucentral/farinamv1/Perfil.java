package edu.ucentral.farinamv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucentral.farinamv1.model.Usuario;

public class Perfil extends AppCompatActivity {

    private EditText Txcorreo;
    private EditText Txnombre;
    private EditText Txtelefono;
    private EditText Txcontrasena;
    private EditText Txcontrasena2;
    private TextView btnAgregarImagen;
    private Button btnEditarPerfil;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();

        Txcorreo=findViewById(R.id.txt_correo);
        Txnombre=findViewById(R.id.txt_nombre);
        Txtelefono=findViewById(R.id.txt_telefono);
        Txcontrasena=findViewById(R.id.txt_contrasena);
        Txcontrasena2=findViewById(R.id.txt_contrasena_2);
        btnAgregarImagen=findViewById(R.id.btn_agregar_imagen);
        btnEditarPerfil=findViewById(R.id.btn_editar_perfil);

        cargarDatos();
    }
    public void cargarDatos(){
        String usuarioId=mAuth.getCurrentUser().getUid();
        /*databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                Txcorreo.setText(usuario.getEmail());
                Txnombre.setText(usuario.getNombre());
                Txtelefono.setText(String.valueOf(usuario.getNumero()));
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
        });*/
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            ViewLogin();
        }
    }
    private void ViewLogin() {
        Intent intent = new Intent(Perfil.this,Login.class);
        startActivity(intent);
        finish(); // terminar esta view
    }
}
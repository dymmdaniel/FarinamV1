package edu.ucentral.farinamv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import edu.ucentral.farinamv1.model.Usuario;

public class Login extends AppCompatActivity {

    private TextView btnRegistrarme;
    private Button btnIngresar;
    private TextView btnOlvidar;
    private EditText Txcorreo;
    private EditText Txcontrasena;
    private ProgressBar barraProgreso;
    private boolean esTienda=false;


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Txcorreo=findViewById(R.id.tx_correo_recuperar);
        Txcontrasena=findViewById(R.id.tx_contrasena);
        btnRegistrarme=findViewById(R.id.btn_registrarme);
        btnIngresar=findViewById(R.id.btn_iniciar_sesion);
        barraProgreso=findViewById(R.id.progressBarContra);
        btnOlvidar=findViewById(R.id.btn_olvidar);

        barraProgreso.setVisibility(View.INVISIBLE);

        iniciarFirebase();


        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRegistro();
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Txcorreo.getText().toString();
                String contrasena = Txcontrasena.getText().toString();
                if(validar(correo,contrasena) && iniciarSesion()){
                    barraProgreso.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                MainTienda(mAuth.getCurrentUser().getUid());
                                if(esTienda){
                                    viewMainTienda();
                                }else{
                                    viewMain();
                                }
                            }else{
                                String error = task.getException().getMessage();
                                if(error.equals("The email address is badly formatted.")){
                                    error="El correo electrónico no tiene el formato correcto";
                                }else{
                                    error= "El correo y/o contraseña no coinciden";
                                }
                                Snackbar.make(
                                        findViewById(R.id.textView2),
                                        error,
                                        BaseTransientBottomBar.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });

                }else {
                    //fail
                }
                barraProgreso.setVisibility(View.INVISIBLE);
            }
        });
        btnOlvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewContra();
            }
        });

    }
    public boolean MainTienda(String usuarioId){
        databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                if(usuario.isTienda()){
                    esTienda=true;
                }else{
                    esTienda=false;
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
        return false;
    }
    public boolean iniciarSesion(){
        return true;
    }

    public boolean validar(String correo,String contrasena){
        int cont=0;
        if(TextUtils.isEmpty(correo)){
            Txcorreo.setError("Requerido");
            cont++;
        }
        if(TextUtils.isEmpty(contrasena)){
            Txcontrasena.setError("Requerido");
            cont++;
        }
        if(cont>0){
            return false;
        }
        return true;
    }
    public void viewRegistro(){
        Intent intent = new Intent(this,RegistroUsuario.class);
        startActivity(intent);
    }
    public void viewMain(){
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        finish(); // terminar esta view
    }
    public void viewMainTienda(){
        Intent intent = new Intent(Login.this,Tienda.class);
        startActivity(intent);
        finish(); // terminar esta view
    }
    public void viewContra(){
        Intent intent = new Intent(Login.this,OlvidarContra.class);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser != null){
            viewMain();
        }
    }

    @Override
    public void onBackPressed (){
        // Para que bloquee el boton de devolver
    }

    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

}
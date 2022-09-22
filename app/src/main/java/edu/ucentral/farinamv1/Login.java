package edu.ucentral.farinamv1;

import androidx.annotation.NonNull;
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

public class Login extends AppCompatActivity {

    private TextView btnRegistrarme;
    private TextView btnIngresar;
    private EditText Txcorreo;
    private EditText Txcontrasena;
    private ProgressBar barraProgreso;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Txcorreo=findViewById(R.id.tx_correo);
        Txcontrasena=findViewById(R.id.tx_contrasena);
        btnRegistrarme=findViewById(R.id.btn_registrarme);
        btnIngresar=findViewById(R.id.btn_ingresar);
        barraProgreso=findViewById(R.id.progressBar);

        barraProgreso.setVisibility(View.INVISIBLE);


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
                                viewMain();
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

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser != null){
            viewMain();
        }
    }

}
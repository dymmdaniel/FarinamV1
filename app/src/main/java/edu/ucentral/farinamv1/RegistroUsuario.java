package edu.ucentral.farinamv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ucentral.farinamv1.model.Usuario;

public class RegistroUsuario extends AppCompatActivity {
    private EditText txt_registro_contrasena;
    private EditText txt_registro_contrasena_2;
    private EditText txt_registro_correo;
    private EditText txt_registro_telefono;
    private EditText txt_registro_nombre;
    private Button btn_registrarme_primary;
    private ProgressBar progressBarRegistro;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        setContentView(R.layout.activity_registro_usuario);
        txt_registro_nombre=findViewById(R.id.txt_registro_nombre);
        txt_registro_telefono=findViewById(R.id.txt_registro_telefono);
        txt_registro_correo=findViewById(R.id.txt_registro_correo);
        txt_registro_contrasena_2=findViewById(R.id.txt_registro_contrasena_2);
        txt_registro_contrasena=findViewById(R.id.txt_registro_contrasena);
        btn_registrarme_primary=findViewById(R.id.btn_registrarme_primary);
        progressBarRegistro=findViewById(R.id.progressBarRegistro);
        progressBarRegistro.setVisibility(View.GONE);


        btn_registrarme_primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    progressBarRegistro.setVisibility(View.VISIBLE);
                    String email=txt_registro_correo.getText().toString();
                    String password=txt_registro_contrasena.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String error="";
                            if(task.isSuccessful()){
                                Usuario usuario=new Usuario();
                                usuario.setUsuarioId(mAuth.getCurrentUser().getUid());
                                usuario.setNombre(txt_registro_nombre.getText().toString());
                                usuario.setNumero(Long.parseLong(txt_registro_telefono.getText().toString()));
                                usuario.setEmail(txt_registro_correo.getText().toString());
                                usuario.setPassword(txt_registro_contrasena.getText().toString());
                                usuario.setRole("USER");

                                //databaseReference.child("User").child(usuario.getUsuarioId()).setValue(usuario);
                                error="Se creó la cuenta correctamente, por favor ingrese";

                            }else{
                                error="Hubo un error al crear la cuenta correctamente";
                            }
                            Thread thread=new Thread(){
                                @Override
                                public void run(){
                                    try {
                                        sleep(3500);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally{
                                        if(task.isSuccessful()) {
                                            viewMain();
                                            finish();
                                        }
                                    }
                                }
                            };
                            Snackbar.make(
                                    findViewById(R.id.textView4),
                                    error,
                                    BaseTransientBottomBar.LENGTH_SHORT
                            ).show();
                            thread.start();
                        }
                    });
                    progressBarRegistro.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean validar(){
        int contador=0;
        if(txt_registro_nombre.getText().toString().isEmpty()){
            txt_registro_nombre.setError("Requerido");
            contador++;
        }
        if(txt_registro_telefono.getText().toString().isEmpty()){
            txt_registro_telefono.setError("Requerido");
            contador++;
        }
        if(txt_registro_correo.getText().toString().isEmpty()){
            txt_registro_correo.setError("Requerido");
            contador++;

        }
        if(txt_registro_contrasena.getText().toString().isEmpty()){
            txt_registro_contrasena.setError("Requerido");
            contador++;
        }
        if(txt_registro_contrasena_2.getText().toString().isEmpty()){
            txt_registro_contrasena_2.setError("Requerido");
            contador++;
        }
        if(!txt_registro_contrasena.getText().toString().equals(txt_registro_contrasena_2.getText().toString())){
            txt_registro_contrasena_2.setError("No coinciden las contraseñas");
            txt_registro_contrasena.setError("No coinciden las contraseñas");
            contador++;
        }
        if(contador>0){
            return false;
        }
        return true;
    }

    public void viewMain(){
        Intent intent = new Intent(RegistroUsuario.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
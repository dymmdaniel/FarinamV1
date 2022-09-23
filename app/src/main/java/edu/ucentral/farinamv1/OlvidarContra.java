package edu.ucentral.farinamv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidarContra extends AppCompatActivity {

    private EditText txCorreoRecuperar;
    private Button btnRecuperar;
    private TextView mensajeEnviado;
    private ProgressBar progressBarContra;



    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_contra);
        txCorreoRecuperar=findViewById(R.id.tx_correo_recuperar);
        btnRecuperar=findViewById(R.id.btn_iniciar_sesion);
        progressBarContra=findViewById(R.id.progressBarContra);
        mensajeEnviado=findViewById(R.id.nensaje_enviado);
        progressBarContra.setVisibility(View.GONE);
        mensajeEnviado.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    progressBarContra.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(txCorreoRecuperar.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                String error = "El correo no está registrado en Farinam";
                                Snackbar.make(
                                        findViewById(R.id.textView3),
                                        error,
                                        BaseTransientBottomBar.LENGTH_SHORT
                                ).show();
                            }else{
                                txCorreoRecuperar.setVisibility(View.GONE);
                                btnRecuperar.setVisibility(View.GONE);
                                mensajeEnviado.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    progressBarContra.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean validar(){
        if(txCorreoRecuperar.getText().toString().isEmpty()){
            txCorreoRecuperar.setError("Requerido");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        ViewLogin();
    }
    private void ViewLogin() {
        Intent intent = new Intent(OlvidarContra.this,Login.class);
        startActivity(intent);
        finish(); // terminar esta view
    }
}
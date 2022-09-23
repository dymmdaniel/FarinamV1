package edu.ucentral.farinamv1;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucentral.farinamv1.model.Usuario;

public class Perfil extends AppCompatActivity {

    private EditText Txcorreo;
    private EditText Txnombre;
    private EditText Txtelefono;
    private EditText Txcontrasena;
    private EditText Txcontrasena2;
    private TextView btnAgregarImagen;
    private Button btnEditarPerfil;
    private CircleImageView iconUsuario;

    private ActivityResultLauncher<String> mGetContent;
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
        iconUsuario=findViewById(R.id.usuarioImagen);

        cargarDatos();

        btnAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(Perfil.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        //Toast.makeText(Perfil.this,"Permiso denegado",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(Perfil.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        mGetContent.launch("image/*");
                        //Toast.makeText(Perfil.this,"Permiso concedido",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent=new Intent(Perfil.this,CrooperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==-1 && requestCode==101){
            String result=data.getStringExtra("RESULT");
            Uri resultUri=null;
            if(result!=null){
                resultUri=Uri.parse(result);
            }
            iconUsuario.setImageURI(resultUri);
            //agregarImagenUsuario(String.valueOf(resultUri));
        }
    }

    private void agregarImagenUsuario(String url) {
        String usuarioId=mAuth.getCurrentUser().getUid();
        /*databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                usuario.setImage(url);
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
}
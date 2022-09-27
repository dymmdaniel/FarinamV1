package edu.ucentral.farinamv1;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private ProgressBar progressBarPerfil;

    private ActivityResultLauncher<String> mGetContent;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iniciarFirebase();

        Txcorreo = findViewById(R.id.txt_correo);
        Txnombre = findViewById(R.id.txt_nombre);
        Txtelefono = findViewById(R.id.txt_telefono);
        Txcontrasena = findViewById(R.id.txt_contrasena);
        Txcontrasena2 = findViewById(R.id.txt_contrasena_2);
        btnAgregarImagen = findViewById(R.id.btn_agregar_imagen);
        btnEditarPerfil = findViewById(R.id.btn_editar_perfil);
        iconUsuario = findViewById(R.id.usuario_imagen_perfil);
        progressBarPerfil = findViewById(R.id.progress_perfil);
        progressBarPerfil.setVisibility(View.GONE);


        cargarDatos(false);

        btnAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Perfil.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(Perfil.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        Toast.makeText(Perfil.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    } else {
                        mGetContent.launch("image/*");

                        //Toast.makeText(Perfil.this,"Permiso concedido",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                Intent intent = new Intent(Perfil.this, CrooperActivity.class);
                intent.putExtra("DATA", result.toString());
                intent.putExtra("RESOLUTION_X",320);
                intent.putExtra("RESOLUTION_Y",320);
                startActivityForResult(intent, 101);
            }
        });
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarDatos(true);
            }
        });
    }


    public void cargarDatos(boolean enable) {
        progressBarPerfil.setVisibility(View.VISIBLE);
        String usuarioId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getCurrentUser();
        StorageReference image_path = storageReference.child("perfil_imagenes").child(usuarioId + ".jpg");
        databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                if (enable) {
                    if (validar()) {
                        usuario.setNombre(Txnombre.getText().toString());
                        usuario.setEmail(Txcorreo.getText().toString());
                        usuario.setNumero(Long.parseLong(Txtelefono.getText().toString()));
                        usuario.setPassword(Txcontrasena.getText().toString());
                        if (validarEmail(usuario.getEmail())) {
                            user.updateEmail(usuario.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Perfil.this, "Email actualizado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        if (validarContra()) {
                            user.updatePassword(usuario.getPassword()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Perfil.this, "Contrasena acutalizada", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        databaseReference.child("Usuario").child(usuario.getUsuarioId()).setValue(usuario);

                    }
                } else {
                    Txcorreo.setText(usuario.getEmail());
                    Txnombre.setText(usuario.getNombre());
                    Txtelefono.setText(String.valueOf(usuario.getNumero()));
                    if (!usuario.getImage().equals("")) {
                        btnAgregarImagen.setBackgroundResource(R.drawable.btn_editar_foto);
                        Glide.with(Perfil.this)
                                .load(Uri.parse(usuario.getImage()))
                                .into(iconUsuario);
                    }else{
                        iconUsuario.setBackgroundResource(R.drawable.icon_photo);
                    }
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
        progressBarPerfil.setVisibility(View.GONE);
    }

    private boolean validarEmail(String email) {
        if (Txcorreo.getText().toString().equals(email)){
            return false;
        }
        return true;
    }

    private boolean validarContra() {
        if (Txcontrasena.getText().toString().isEmpty() ||
                Txcontrasena2.getText().toString().isEmpty() ||
                !Txcontrasena.getText().toString().equals(Txcontrasena.getText().toString())) {
            if(!Txcontrasena.getText().toString().equals(Txcontrasena.getText().toString())){
                Txcontrasena.setError("No coinciden");
                Txcontrasena2.setError("No coinciden");
            }
            return false;
        }
        return true;
    }

    private boolean validar() {
        int contador = 0;
        if (Txnombre.getText().toString().isEmpty()) {
            Txnombre.setError("Requerido");
            contador++;
        }
        if (Txcorreo.getText().toString().isEmpty()) {
            Txcorreo.setError("Requerido");
            contador++;
        }
        if (Txtelefono.getText().toString().isEmpty()) {
            Txtelefono.setError("Requerido");
            contador++;

        }
        if (contador > 0) {
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            ViewLogin();
        }
    }

    private void ViewLogin() {
        Intent intent = new Intent(Perfil.this, Login.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);
            }
            iconUsuario.setImageURI(resultUri);
            agregarImagenUsuario(resultUri);
        }
    }

    private void agregarImagenUsuario(Uri uri) {
        String usuarioId = mAuth.getCurrentUser().getUid();
        progressBarPerfil.setVisibility(View.VISIBLE);
        StorageReference image_path = storageReference.child("perfil_imagenes").child(usuarioId + ".jpg");
        databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                image_path.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //String download_url = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                            image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    usuario.setImage(String.valueOf(uri));
                                    databaseReference.child("Usuario").child(usuario.getUsuarioId()).setValue(usuario);
                                }
                            });
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Perfil.this, "Error al cargar la imagen " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                progressBarPerfil.setVisibility(View.GONE);
                Snackbar.make(
                        findViewById(R.id.textView10),
                        "Foto actualizada correctamente",
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

    private void iniciarFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Perfil.this, MainActivity.class);
        startActivity(intent);
        finish(); // terminar esta view
    }
}
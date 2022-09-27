package edu.ucentral.farinamv1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import edu.ucentral.farinamv1.model.Categoria;
import edu.ucentral.farinamv1.model.Ingrediente;
import edu.ucentral.farinamv1.model.Receta;
import edu.ucentral.farinamv1.model.Usuario;

public class CrearReceta extends AppCompatActivity {

    private ImageView btnAgregarImagenRecetaF;
    private EditText txNombreReceta;
    private EditText txDescripcion;
    private Spinner spinnerCategoria;
    private Button btnContinuar1;

    //segunda fase
    private TextView textView5;
    private TextView txtIngresarIngrediente;
    private RatingBar dificultad;
    private EditText txIngresarIngrediente;
    private Button btnAgregarIngrediente;
    private ListView listViewIngredientes;
    private Button btnContinuar2;
    private ImageView imageView2;
    private Button btnRegresarReceta2;

    //Tercera Fase
    private EditText txPasos;
    private EditText txTiempo;
    private TextView textView7;
    private TextView textView8;
    private Button btnCrearRecetaFinal;
    private Button btnRegresarReceta3;



    private ArrayList<Categoria> categoriaList=new ArrayList<>();
    private ArrayAdapter<Categoria> categoriaArrayAdapter;
    private ArrayList<Ingrediente> ingredientes=new ArrayList<>();
    private ArrayAdapter<Ingrediente> ingredienteArrayAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ActivityResultLauncher<String> mGetContent;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciarFirebase();
        setContentView(R.layout.activity_crear_receta);
        btnAgregarImagenRecetaF=findViewById(R.id.btn_agregar_imagen_receta_f);
        txNombreReceta=findViewById(R.id.txt_nombre_receta);
        txDescripcion=findViewById(R.id.txt_descripcion);
        spinnerCategoria=findViewById(R.id.spinner_categoria);
        btnContinuar1=findViewById(R.id.btn_receta_1);
        cargarDatosSpinner();

        //Segunda vista
        textView5=findViewById(R.id.textView5);
        dificultad=findViewById(R.id.rating_bar);
        txIngresarIngrediente=findViewById(R.id.txt_ingresar_ingrediente);
        btnAgregarIngrediente=findViewById(R.id.btn_agregar_ingrediente);
        listViewIngredientes=findViewById(R.id.list_ingredientes);
        btnContinuar2=findViewById(R.id.btn_receta_2);
        btnRegresarReceta2=findViewById(R.id.btn_regresar_receta_2);
        segundaVista(View.GONE);

        //Tercera vista
        txPasos=findViewById(R.id.txt_pasos);
        txTiempo=findViewById(R.id.txt_tiempo_preparacion);
        textView7=findViewById(R.id.textView7);
        textView8=findViewById(R.id.textView8);
        imageView2=findViewById(R.id.imageView2);
        btnCrearRecetaFinal=findViewById(R.id.btn_crear_receta_final);
        btnRegresarReceta3=findViewById(R.id.btn_regresar_receta_3);
        terceraVista(View.GONE);

        btnAgregarImagenRecetaF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarImagen();
            }
        });

        btnAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txIngresarIngrediente.getText().toString().isEmpty()){
                    Ingrediente ingrediente=new Ingrediente();
                    ingrediente.setIngredienteId(UUID.randomUUID().toString());
                    ingrediente.setNombre(txIngresarIngrediente.getText().toString());
                    ingrediente.setEnable(true);

                    ingredientes.add(ingrediente);
                    txIngresarIngrediente.setText("");
                    Snackbar.make(
                            findViewById(R.id.text_view_crear),
                            "Ingrediente agregado",
                            BaseTransientBottomBar.LENGTH_SHORT
                    ).show();
                    ingredienteArrayAdapter = new ArrayAdapter<Ingrediente>(CrearReceta.this,R.layout.simple_list_delete,ingredientes);
                    listViewIngredientes.setAdapter(ingredienteArrayAdapter);
                }else{
                    Snackbar.make(
                            findViewById(R.id.text_view_crear),
                            "Llena el campo, antes de agregar un ingrediente",
                            BaseTransientBottomBar.LENGTH_SHORT
                    ).show();
                }
            }
        });

        listViewIngredientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingrediente ingrediente=new Ingrediente();
                ingrediente = (Ingrediente) adapterView.getItemAtPosition(i);
                ingredientes.remove(ingrediente);
                ingredienteArrayAdapter = new ArrayAdapter<Ingrediente>(CrearReceta.this,R.layout.simple_list_delete,ingredientes);
                listViewIngredientes.setAdapter(ingredienteArrayAdapter);
            }
        });



        btnContinuar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primeraVista(View.GONE);
                Snackbar.make(
                        findViewById(R.id.text_view_crear),
                        "Selecciona un item de la lista para eliminarlo",
                        BaseTransientBottomBar.LENGTH_SHORT
                ).show();
                segundaVista(View.VISIBLE);
                terceraVista(View.GONE);
            }
        });
        btnContinuar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primeraVista(View.GONE);
                segundaVista(View.GONE);
                terceraVista(View.VISIBLE);
            }
        });
        btnRegresarReceta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primeraVista(View.VISIBLE);
                segundaVista(View.GONE);
                terceraVista(View.GONE);
            }
        });
        btnRegresarReceta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primeraVista(View.GONE);
                segundaVista(View.VISIBLE);
                terceraVista(View.GONE);
            }
        });

        btnCrearRecetaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearReceta();
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                Intent intent = new Intent(CrearReceta.this, CrooperActivity.class);
                intent.putExtra("DATA", result.toString());
                intent.putExtra("RESOLUTION_X",300);
                intent.putExtra("RESOLUTION_Y",300);
                startActivityForResult(intent, 101);
            }
        });

    }

    public void crearReceta(){
        if(validar()){
            Receta receta=new Receta();
            receta.setRecetaId(UUID.randomUUID().toString());
            receta.setTitulo(txNombreReceta.getText().toString());
            databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Usuario usuario=snapshot.getValue(Usuario.class);
                    receta.setUsuarioNombre(usuario.getNombre());
                    receta.setUsuarioId(mAuth.getCurrentUser().getUid());
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
            receta.setCategoriaId(spinnerCategoria.getSelectedItem().toString());
            receta.setDificultad(dificultad.getRating());
            receta.setDescripcion(txDescripcion.getText().toString());
            receta.setIngredient(ingredientes);
            receta.setPasos(txPasos.getText().toString());
            receta.setTiempo(Integer.parseInt(txTiempo.getText().toString()));
            receta.setValoracion(0);
            receta.setEnable(true);
            receta.setLike(0);
            receta.setDislike(0);
            StorageReference image_path = storageReference.child("recetas_imagenes").child(receta.getRecetaId()+".jpg");
            image_path.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                receta.setImage(String.valueOf(uri));
                                databaseReference.child("Receta").child(receta.getRecetaId()).setValue(receta);
                            }
                        });
                    }else{
                        Snackbar.make(
                                findViewById(R.id.text_view_crear),
                                "Error al cargar la imagen. ",
                                BaseTransientBottomBar.LENGTH_SHORT
                        ).show();
                    }
                }
            });
            Snackbar.make(
                    findViewById(R.id.text_view_crear),
                    "Se ha creado tu receta.",
                    BaseTransientBottomBar.LENGTH_SHORT
            ).show();
            viewMain();
        }else{
            Snackbar.make(
                    findViewById(R.id.text_view_crear),
                    "Fallo al guardar la receta, llena todos los datos por requeridos",
                    BaseTransientBottomBar.LENGTH_SHORT
            ).show();
        }
    }



    private void cargarDatosSpinner() {
        databaseReference.child("Categoria").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriaList.clear();
                Categoria categoria_temp=new Categoria();
                categoria_temp.setNombre("Selecciona una categoria");
                categoriaList.add(categoria_temp);
                for(DataSnapshot objSnapshot: snapshot.getChildren()){
                    Categoria categoria=objSnapshot.getValue(Categoria.class);
                    categoriaList.add(categoria);
                }
                categoriaArrayAdapter=new ArrayAdapter<>(CrearReceta.this, android.R.layout.simple_list_item_1,categoriaList){
                    @Override
                    public boolean isEnabled(int position){
                        return position != 0;
                    }
                    @Override
                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
                        View view=super.getDropDownView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        if(position == 0){
                            textView.setTextColor(Color.GRAY);
                        }else{
                            textView.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                categoriaArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerCategoria.setAdapter(categoriaArrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void primeraVista(int view){
        btnAgregarImagenRecetaF.setVisibility(view);
        txNombreReceta.setVisibility(view);
        txDescripcion.setVisibility(view);
        spinnerCategoria.setVisibility(view);
        btnContinuar1.setVisibility(view);
    }

    public void segundaVista(int view){
        dificultad.setVisibility(view);
        txIngresarIngrediente.setVisibility(view);
        btnAgregarIngrediente.setVisibility(view);
        listViewIngredientes.setVisibility(view);
        btnContinuar2.setVisibility(view);
        btnRegresarReceta2.setVisibility(view);
        textView5.setVisibility(view);
    }
    public void terceraVista(int view) {
        txPasos.setVisibility(view);
        txTiempo.setVisibility(view);
        textView7.setVisibility(view);
        imageView2.setVisibility(view);
        textView8.setVisibility(view);
        btnCrearRecetaFinal.setVisibility(view);
        btnRegresarReceta3.setVisibility(view);
    }
    public void agregarImagen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(CrearReceta.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(CrearReceta.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Toast.makeText(CrearReceta.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            } else {
                mGetContent.launch("image/*");
            }
        }

    }



    public boolean validar(){
        if(spinnerCategoria.getSelectedItem().toString().isEmpty()){
            Snackbar.make(
                    findViewById(R.id.text_view_crear),
                    "Selecciona una categoria. ",
                    BaseTransientBottomBar.LENGTH_SHORT
            ).show();
            return false;
        }
        if (txDescripcion.getText().toString().toString().isEmpty()){
            txDescripcion.setError("Requerido");
            return false;
        }
        if(ingredientes.isEmpty()){
            Snackbar.make(
                    findViewById(R.id.text_view_crear),
                    "Agrega al menos un ingrediente",
                    BaseTransientBottomBar.LENGTH_SHORT
            ).show();
            return false;
        }
        if(txPasos.getText().toString().isEmpty()){
            txPasos.setError("Requerido");
            return false;
        }
        if(txTiempo.getText().toString().isEmpty()){
            txTiempo.setError("Requerido");
            return false;
        }
        return true;
    }

    public void iniciarFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
    }
    private void viewMain() {
        Intent intent = new Intent(CrearReceta.this,MainActivity.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    @Override
    public void onBackPressed (){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Guarda tu receta");
        dialogo1.setMessage("¿Quieres volver al inicio? Se perderá todo el progreso.");
        dialogo1.setCancelable(true);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                viewMain();
            }
        });
        dialogo1.setNegativeButton("No, regresar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
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
            /*ViewGroup.LayoutParams layoutParams = btnAgregarImagenRecetaF.getLayoutParams();
            layoutParams.width = (int) (415 * btnAgregarImagenRecetaF.getResources().getDisplayMetrics().density);
            layoutParams.height = (int) (210 * btnAgregarImagenRecetaF.getResources().getDisplayMetrics().density);
            btnAgregarImagenRecetaF.setLayoutParams(layoutParams);*/
            btnAgregarImagenRecetaF.setImageURI(resultUri);
            uri=resultUri;
        }
    }

}
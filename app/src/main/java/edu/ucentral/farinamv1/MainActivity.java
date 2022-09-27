package edu.ucentral.farinamv1;




import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.ucentral.farinamv1.Fragmentos.FragmentoCuenta;
import edu.ucentral.farinamv1.Fragmentos.FragmentoDirectorio;
import edu.ucentral.farinamv1.Fragmentos.FragmentoFavoritos;
import edu.ucentral.farinamv1.Fragmentos.FragmentoMain;
import edu.ucentral.farinamv1.Fragmentos.FragmentoNotificaciones;
import edu.ucentral.farinamv1.model.Categoria;
import edu.ucentral.farinamv1.model.Usuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressBar progressBarBusqueda;
    private SearchView buscador;
    private ImageView image_view_fondo;
    private Spinner spinnerCategorias;

    private TextView nombreUsuario;
    private CircleImageView iconUsuario;
    private FloatingActionButton btn_crear_receta;
    private BottomNavigationView mainBottom;
    private DrawerLayout drawerLayout;

    private ArrayList<Categoria> categorias=new ArrayList<>();
    private ArrayAdapter<Categoria> categoriaArrayAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private FragmentoMain fragmentoMain;
    private FragmentoCuenta fragmentoCuenta;
    private FragmentoNotificaciones fragmentoNotificaciones;
    private FragmentoFavoritos fragmentoFavoritos;
    private FragmentoDirectorio fragmentoDirectorio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarFirebase();

        progressBarBusqueda=findViewById(R.id.progress_busqueda);
        buscador=findViewById(R.id.buscador);
        btn_crear_receta=findViewById(R.id.btn_crear_receta);
        mainBottom=findViewById(R.id.bottom_navigator);
        image_view_fondo=findViewById(R.id.image_view_fondo);
        spinnerCategorias=findViewById(R.id.spinner_main_categoria);

        databaseReference.child("Categoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categorias.clear();
                Categoria categoria_temp=new Categoria();
                categoria_temp.setNombre("Selecciona una categoria");
                categorias.add(categoria_temp);
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    Categoria categoria=objSnapshot.getValue(Categoria.class);
                    categorias.add(categoria);
                }
                categoriaArrayAdapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,categorias){
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
                spinnerCategorias.setAdapter(categoriaArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buscador.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams layoutParams = buscador.getLayoutParams();
                layoutParams.width = (int) (348 * buscador.getResources().getDisplayMetrics().density);
                view.setLayoutParams(layoutParams);
            }
        });
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ViewGroup.LayoutParams layoutParams = buscador.getLayoutParams();
                layoutParams.width = (int) (55 * buscador.getResources().getDisplayMetrics().density);
                buscador.setLayoutParams(layoutParams);
                return false;
            }
        });

        btn_crear_receta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewReceta();
            }
        });

        drawerLayout=findViewById(R.id.drawer_activity);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView= navigationView.getHeaderView(0);
        nombreUsuario=headerView.findViewById(R.id.usuarioNombrePerfil);
        iconUsuario=headerView.findViewById(R.id.usuario_imagen_perfil);

        //Fragments
        fragmentoMain=new FragmentoMain();
        fragmentoCuenta=new FragmentoCuenta();
        fragmentoNotificaciones=new FragmentoNotificaciones();
        fragmentoFavoritos=new FragmentoFavoritos();
        fragmentoDirectorio=new FragmentoDirectorio();

        mainBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_inicio:
                        replaceFragment(fragmentoMain);
                        buscador.setVisibility(View.VISIBLE);
                        spinnerCategorias.setVisibility(View.VISIBLE);
                        progressBarBusqueda.setVisibility(View.GONE);
                        image_view_fondo.setVisibility(View.GONE);
                        return true;
                    case R.id.bottom_notificaciones:
                        buscador.setVisibility(View.GONE);
                        spinnerCategorias.setVisibility(View.GONE);
                        replaceFragment(fragmentoNotificaciones);
                        return true;
                    case R.id.bottom_perfil:
                        buscador.setVisibility(View.GONE);
                        spinnerCategorias.setVisibility(View.GONE);
                        replaceFragment(fragmentoCuenta);
                        return true;
                    default:
                        return false;
                }
            }
        });
        replaceFragment(fragmentoMain);
    }

    private void buscarNombre() {
        String usuarioId=mAuth.getCurrentUser().getUid();
        databaseReference.child("Usuario").orderByChild("usuarioId").equalTo(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                nombreUsuario.setText(usuario.getNombre());
                if(!usuario.getImage().equals("")){
                    Glide.with(MainActivity.this)
                            .load(Uri.parse(usuario.getImage()))
                            .into(iconUsuario);
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
                nombreUsuario.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.nav_perfilU) {
            perfil();
        }
        else if (id == R.id.nav_favoritos) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Bundle result=new Bundle();
            result.putString("usuarioId",mAuth.getCurrentUser().getUid());
            fragmentoFavoritos.setArguments(result);
            replaceFragment(fragmentoFavoritos);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else if (id == R.id.nav_lista) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            replaceFragment(fragmentoFavoritos);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else if (id == R.id.nav_cronometro) {
            viewCronometro();
        }
        else if(id == R.id.nav_agregar_receta){
            viewReceta();
        }
        else if(id==R.id.nav_directorio){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            replaceFragment(fragmentoDirectorio);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else if (id == R.id.nav_salirU) {
            cerrarSesion();
        }
        return true;
    }
    public void perfil(){
        Intent intent = new Intent(MainActivity.this,Perfil.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    public void cerrarSesion(){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Cerrar Sesion");
        dialogo1.setMessage("¿Desea cerrar su sesion?");
        dialogo1.setCancelable(true);
        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                mAuth.signOut();
                viewLogin();
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }


    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            viewLogin();
        }else{
            buscarNombre();
        }
    }

    private void iniciarFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
    }

    private void viewLogin() {
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    private void viewCronometro() {
        Intent intent = new Intent(MainActivity.this,Cronometro.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    private void viewReceta() {
        Intent intent = new Intent(MainActivity.this,CrearReceta.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer,fragment);
        fragmentTransaction.commit();
        progressBarBusqueda.setVisibility(View.GONE);
        image_view_fondo.setVisibility(View.GONE);
    }
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


}
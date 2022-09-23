package edu.ucentral.farinamv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressBar progressBarBusqueda;
    private SearchView buscador;
    private Button btnMenuMain;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        progressBarBusqueda=findViewById(R.id.progressBarBusqueda);
        buscador=findViewById(R.id.buscador);
        btnMenuMain=findViewById(R.id.btn_menu_main);
        progressBarBusqueda.setVisibility(View.GONE);

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

        DrawerLayout drawerLayout=findViewById(R.id.drawer_activity);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView= navigationView.getHeaderView(0);




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
        }
        else if (id == R.id.nav_lista) {

        }
        else if (id == R.id.nav_cronometro) {

        }else if(id==R.id.nav_directorio){
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
        mAuth.signOut();
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
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish(); // terminar esta view
    }
}
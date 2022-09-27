package edu.ucentral.farinamv1.Recicle;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import edu.ucentral.farinamv1.Perfil;
import edu.ucentral.farinamv1.R;
import edu.ucentral.farinamv1.model.Receta;
import edu.ucentral.farinamv1.model.Usuario;

public class RecetaRecicle extends RecyclerView.Adapter<RecetaRecicle.ViewHolder> implements View.OnClickListener{

    public List<Receta> recetas;
    public List<Receta> recetas_filtrado;
    public Context context;
    private View.OnClickListener listener;
    public RecetaRecicle(List<Receta> recetas){
        this.recetas=recetas;
        recetas_filtrado=new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recetas_list_item,parent,false);
        context=parent.getContext();
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nombreUsuario=recetas.get(position).getUsuarioId();
        String sDescripcion=recetas.get(position).getDescripcion();
        int sTiempo=recetas.get(position).getTiempo();
        String sCategoria=recetas.get(position).getCategoriaId();
        String sTitulo=recetas.get(position).getTitulo();
        float sValoracion= (float) recetas.get(position).getValoracion();
        String sImagen=recetas.get(position).getImage();

        holder.setData(nombreUsuario,sDescripcion,sTiempo,sCategoria,sTitulo,sValoracion,sImagen);

    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View viewMain;


        private TextView usuarioId;
        private TextView descripcion;
        private TextView tiempo;
        private TextView categoria;
        private TextView titulo;
        private RatingBar valoracion;
        private ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewMain = itemView;
        }

        public void setData(String nombreUsuario,
                            String sDescripcion,
                            int sTiempo,
                            String sCategoria,
                            String sTitulo,
                            float sValoracion,
                            String sImagen){

            //Toca buscar el name
            usuarioId= viewMain.findViewById(R.id.receta_nombre_usuario); //String
            descripcion = viewMain.findViewById(R.id.receta_descripcion); //String
            tiempo = viewMain.findViewById(R.id.receta_tiempo); //Int
            categoria = viewMain.findViewById(R.id.receta_categoria); // String
            titulo = viewMain.findViewById(R.id.receta_titulo); // String
            valoracion = viewMain.findViewById(R.id.receta_valoracion); //Double
            image = viewMain.findViewById(R.id.receta_imagen); //String

            usuarioId.setText(nombreUsuario);
            descripcion.setText(sDescripcion);
            tiempo.setText(String.valueOf(sTiempo)+" Min");
            categoria.setText(sCategoria);
            titulo.setText(sTitulo);
            valoracion.setRating(sValoracion);
            if(!sImagen.isEmpty()){
                Glide.with(context)
                        .load(Uri.parse(sImagen))
                        .into(image);
                image.setBackgroundResource(0);
            }
        }

    }
}

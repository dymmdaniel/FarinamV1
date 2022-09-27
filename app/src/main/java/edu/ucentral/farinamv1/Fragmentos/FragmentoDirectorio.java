package edu.ucentral.farinamv1.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ucentral.farinamv1.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentoDirectorio extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public FragmentoDirectorio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragmento_directorio, container, false);
        iniciarFirebase();

        return view;
    }
    public void iniciarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
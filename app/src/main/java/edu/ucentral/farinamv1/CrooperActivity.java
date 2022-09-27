package edu.ucentral.farinamv1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CrooperActivity extends AppCompatActivity {

    private String result;
    private Uri fileUri;

    private int resolutionX;
    private int resolutionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crooper);

        readIntent();

        String dest_uri=new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop.Options options=new UCrop.Options();

        UCrop.of(fileUri,Uri.fromFile(new File(getCacheDir(),dest_uri)))
                .withOptions(options)
                .withAspectRatio(0,0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(resolutionX,resolutionY)
                .start(CrooperActivity.this);
    }
    public void readIntent(){
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
            result=intent.getStringExtra("DATA");
            resolutionX = intent.getExtras().getInt("RESOLUTION_X");
            resolutionY = intent.getExtras().getInt("RESOLUTION_Y");
            fileUri=Uri.parse(result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode== UCrop.REQUEST_CROP){
            final Uri resultUri=UCrop.getOutput(data);
            Intent returnIntent=new Intent();
            returnIntent.putExtra("RESULT",resultUri+"");
            setResult(-1,returnIntent);
            finish();
        }else if(resultCode== UCrop.RESULT_ERROR){
            final Throwable cropError=UCrop.getError(data);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CrooperActivity.this,Perfil.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

}
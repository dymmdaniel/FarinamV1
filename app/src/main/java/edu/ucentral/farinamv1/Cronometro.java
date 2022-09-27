package edu.ucentral.farinamv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Cronometro extends AppCompatActivity {
    private EditText txt_cronometro_tiempo;
    private TextView txt_contador;
    private ProgressBar progress_cronometro;
    private Button btn_comenzar_pausar;
    private Button btn_reiniciar;
    private boolean mTimerRunning;

    private static long START_TIME_IN_MILLIS=0;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);
        txt_cronometro_tiempo=findViewById(R.id.txt_cronometro_tiempo);
        txt_contador=findViewById(R.id.txt_contador);
        progress_cronometro=findViewById(R.id.progress_cronometro);
        btn_comenzar_pausar=findViewById(R.id.btn_comenzar_pausar);
        btn_reiniciar=findViewById(R.id.btn_reiniciar);
        START_TIME_IN_MILLIS =(long) 0;
        mTimeLeftInMillis=START_TIME_IN_MILLIS;
        btn_comenzar_pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTimerRunning){
                    pausar();
                }else{
                    comenzar();
                }
            }
        });

        btn_reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetear();
            }
        });

        updateCountDownText();
    }

    private void pausar() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        btn_comenzar_pausar.setText("Comenzar");
        btn_reiniciar.setVisibility(View.VISIBLE);
        txt_cronometro_tiempo.setEnabled(false);
    }
    private void comenzar(){
        if(!txt_cronometro_tiempo.getText().toString().isEmpty()){
            mTimeLeftInMillis=Long.parseLong(txt_cronometro_tiempo.getText().toString()) * 60000;
            progress_cronometro.setMax((int)mTimeLeftInMillis);
            txt_cronometro_tiempo.setEnabled(false);
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {
                    mTimerRunning = false;
                    btn_comenzar_pausar.setText("Comenzar");
                    btn_comenzar_pausar.setVisibility(View.INVISIBLE);
                    btn_reiniciar.setVisibility(View.VISIBLE);
                }
            }.start();

            mTimerRunning = true;
            btn_comenzar_pausar.setText("Pausar");
            btn_reiniciar.setVisibility(View.INVISIBLE);
        }else{

        }
    }
    private void resetear(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        btn_reiniciar.setVisibility(View.INVISIBLE);
        btn_comenzar_pausar.setVisibility(View.VISIBLE);
        txt_cronometro_tiempo.setEnabled(true);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        i++;
        txt_contador.setText(timeLeftFormatted);
        progress_cronometro.setProgress(50);
    }

    private void viewMain() {
        Intent intent = new Intent(Cronometro.this,MainActivity.class);
        startActivity(intent);
        finish(); // terminar esta view
    }

    @Override
    public void onBackPressed (){
        viewMain();
    }
}
package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sigbe.ticketproject2.Models.Usuario;

public class HomeEstudiante extends AppCompatActivity {

    Button btnTicket;
    public static final long DURATION_TRANSITION = 450;
    private static final int DURACION= 450;
    private static final int TIEMPO_DESPUES=200;
    private Transition transition;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_estudiante);

        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(MainActivity.DURATION_TRANSITION);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(fadeIn);

        //Importamos texto de Act.1
        bundle = getIntent().getExtras();
        Toast.makeText(HomeEstudiante.this, "Nombre estudiante " + bundle.getString("nombre"), Toast.LENGTH_SHORT).show();

        btnTicket = (Button) findViewById(R.id.buttonComprar);

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarActividadTicket(bundle);
            }
        });

    }

    public void iniciarActividadTicket(Bundle bundle){
        Intent intent = new Intent (HomeEstudiante.this, Comprarticket.class);
        //Exportar parametro
        intent.putExtra("nombre", bundle.getString("nombre"));
        intent.putExtra("apellido", bundle.getString("apellido"));
        intent.putExtra("correo", bundle.getString("correo"));
        intent.putExtra("contrasena", bundle.getString("contrasena"));
        intent.putExtra("codigoestudiante", bundle.getInt("codigoestudiante"));
        intent.putExtra("saldo", bundle.getInt("saldo"));
        intent.putExtra("rol", bundle.getInt("rol"));
        intent.putExtra("identificacion", bundle.getInt("identificacion"));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}

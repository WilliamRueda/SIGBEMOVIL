package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Fade;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class HomeEstudiante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_estudiante);
/*
        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(MainActivity.DURATION_TRANSITION);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(fadeIn);
*/
        //Importamos texto de Act.1
        Bundle bundle = getIntent().getExtras();
        String fraseimportada=bundle.getString("nombre");

    }
}

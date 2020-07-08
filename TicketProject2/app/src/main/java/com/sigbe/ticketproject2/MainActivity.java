package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final long DURATION_TRANSITION = 450;
    EditText nombre,email,password;
    Button btGuardar, btBuscar;
    ImageView imageUv;

    private static final int DURACION= 450;
    private static final int TIEMPO_DESPUES=200;
    private Transition transition;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btGuardar = (Button) findViewById(R.id.btnRegistrar);
        btBuscar = (Button) findViewById(R.id.btnBuscar);
        imageUv = (ImageView) findViewById(R.id.imageviewuv);

        imageUv.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        btBuscar.setVisibility(View.INVISIBLE);
        btGuardar.setVisibility(View.INVISIBLE);


        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);
        fadein.setDuration(DURACION);
        fadein.setStartOffset(TIEMPO_DESPUES);
        fadein.setFillAfter(true);



        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageUv.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                btBuscar.setVisibility(View.VISIBLE);
                btGuardar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageUv.startAnimation(fadein);
        email.startAnimation(fadein);
        password.startAnimation(fadein);
        btBuscar.startAnimation(fadein);
        btGuardar.startAnimation(fadein);

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doregister("http://ticketproject.000webhostapp.com/webservicephpticket/webservices/insertarpersona.php");
            }
        });

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar("http://ticketproject.000webhostapp.com/webservicephpticket/webservices/buscarpersona.php?email=" + email.getText() + "");
            }
        });


        transition = new Fade(Fade.OUT);

    }


    public void Doregister(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Registro exitoso" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("nombre", nombre.getText().toString());
                parametros.put("email", email.getText().toString());
                parametros.put("password", password.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void iniciarActividadHomeEstudiante(String nombrebase){
        transition.setDuration(DURATION_TRANSITION);
        transition.setInterpolator(new DecelerateInterpolator());

        getWindow().setExitTransition(transition);
        Intent intent = new Intent (MainActivity.this, HomeEstudiante.class);
        //Exportar parametro
        intent.putExtra("nombre", nombrebase);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @SuppressWarnings("unchecked")
    private void Buscar(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                String emailrespaldo, passwordrespaldo,contraseñabasededatos,nombrebase;
                nombrebase = null;
                contraseñabasededatos = null;
                emailrespaldo = email.getText().toString();
                passwordrespaldo = password.getText().toString();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        contraseñabasededatos = jsonObject.getString("password");
                        nombrebase = jsonObject.getString("nombre");
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "ERROR" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                if(passwordrespaldo.equals(contraseñabasededatos)){
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    iniciarActividadHomeEstudiante(nombrebase);

                }else{
                    Toast.makeText(MainActivity.this, "Email/Contraseña invalida", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR DE CONEXION" , Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}

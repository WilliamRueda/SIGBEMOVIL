package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sigbe.ticketproject2.Models.Usuario;

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(this,"camera permission granted",Toast.LENGTH_LONG).show();
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }

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
                Doregister("sigbebackend.herokuapp.com/webservices/insertarpersona.php");
            }
        });

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar("http://sigbebackend.herokuapp.com/app/componentes/usuarios/list_useremail.php?email=" + email.getText() + "");
            }
        });


        transition = new Fade(Fade.OUT);

    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Toast.makeText(this, "Camera access is required to Scan The Barcode.",
                    Toast.LENGTH_LONG).show();


            // Request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);



        } else {
            Toast.makeText(this,
                    "<b>Camera could not be opened.</b>\\nThis occurs when the camera is not available (for example it is already in use) or if the system has denied access (for example when camera access has been disabled).", Toast.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
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


    public void iniciarActividadHomeEstudiante(Usuario usuariobuscado){
        transition.setDuration(DURATION_TRANSITION);
        transition.setInterpolator(new DecelerateInterpolator());

        getWindow().setExitTransition(transition);
        Intent intent = new Intent (MainActivity.this, HomeEstudiante.class);
        //Exportar parametro
        intent.putExtra("nombre", usuariobuscado.getNombre());
        intent.putExtra("apellido", usuariobuscado.getApellido());
        intent.putExtra("correo", usuariobuscado.getCorreo());
        intent.putExtra("contrasena", usuariobuscado.getPassword());
        intent.putExtra("codigoestudiante", usuariobuscado.getCodigoestudiante());
        intent.putExtra("saldo", usuariobuscado.getSaldo());
        intent.putExtra("rol", usuariobuscado.getRoles());
        intent.putExtra("identificacion", usuariobuscado.getIdentificacion());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    public void iniciarActividadHomeRestaurante(Usuario usuariobuscado){
        transition.setDuration(DURATION_TRANSITION);
        transition.setInterpolator(new DecelerateInterpolator());

        getWindow().setExitTransition(transition);
        Intent intent = new Intent (MainActivity.this, HomeRestaurante.class);
        //Exportar parametro
        intent.putExtra("nombre", usuariobuscado.getNombre());
        intent.putExtra("apellido", usuariobuscado.getApellido());
        intent.putExtra("correo", usuariobuscado.getCorreo());
        intent.putExtra("contrasena", usuariobuscado.getPassword());
        intent.putExtra("codigoestudiante", Integer.toString(usuariobuscado.getCodigoestudiante()));
        intent.putExtra("saldo", Integer.toString(usuariobuscado.getSaldo()));
        intent.putExtra("rol", Integer.toString(usuariobuscado.getRoles()));
        intent.putExtra("identificacion", Integer.toString(usuariobuscado.getIdentificacion()));
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @SuppressWarnings("unchecked")
    private void Buscar(String URL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String passwordrespaldo = password.getText().toString();
                        Usuario usuariobuscado = new Usuario();

                        try {
                            usuariobuscado.setNombre(response.getString("nombre"));
                            usuariobuscado.setApellido(response.getString("apellido"));
                            usuariobuscado.setCodigoestudiante(Integer.parseInt(response.getString("codigoestudiante")));
                            usuariobuscado.setCorreo(response.getString("correo"));
                            usuariobuscado.setIdentificacion(Integer.parseInt(response.getString("identificacion")));
                            usuariobuscado.setRoles(Integer.parseInt(response.getString("roles")));
                            usuariobuscado.setSaldo(Integer.parseInt(response.getString("saldo")));
                            usuariobuscado.setPassword(response.getString("contrasena"));
                            if(passwordrespaldo.equals(response.getString("contrasena"))){
                                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                if(Integer.parseInt(response.getString("roles")) == 1){
                                    System.out.println(response);
                                    iniciarActividadHomeEstudiante(usuariobuscado);
                                }else{
                                    System.out.println(response);
                                    iniciarActividadHomeRestaurante(usuariobuscado);
                                }

                            }else{
                                Toast.makeText(MainActivity.this, "Email/Contraseña invalida", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Email/Contraseña invalida - ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}

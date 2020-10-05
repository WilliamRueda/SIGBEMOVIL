package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;
import com.sigbe.ticketproject2.Models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class HomeEstudiante extends AppCompatActivity {

    Button btnTicket,btnSalir;
    ListView listTicketss;
    public static final long DURATION_TRANSITION = 450;
    private static final int DURACION= 450;
    private static final int TIEMPO_DESPUES=200;
    private Transition transition;
    Bundle bundle;
    int tamanoArreglo;
    String[][] arregloTicketsbuscados;
    ArrayList<String> arraylistsconcecutivo;
    RequestQueue requestQueue;
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
        btnSalir = (Button) findViewById(R.id.buttonSalir);
        listTicketss = (ListView) findViewById(R.id.listTickets);
        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarActividadTicket(bundle);
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        buscarTicketsById("http://sigbebackend.herokuapp.com/app/componentes/Tickets/list_tickets.php?identificacion=" + bundle.getInt("identificacion"));


        }

    public void cerrarSesion(){
        finishAffinity();
        startActivity(new Intent(HomeEstudiante.this, MainActivity.class));
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

    public void iniciarActividadCodigoQR(int concest){
        Intent intent = new Intent (HomeEstudiante.this, Codigoqr.class);
        intent.putExtra("codigo",concest);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    public void buscarTicketsById(String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            tamanoArreglo = response.length();
                            arregloTicketsbuscados = new String[tamanoArreglo][4];
                            for(int i = 0;i< response.length();i++){

                                    JSONObject json = response.getJSONObject(i);
                                    JSONObject fechajson = json.getJSONObject("fecha_compra");
                                    arregloTicketsbuscados[i][0] = json.getString("consecutivoticket");
                                    arregloTicketsbuscados[i][1] = fechajson.getString("date");
                                    arregloTicketsbuscados[i][2] = json.getString("estado");
                                    arregloTicketsbuscados[i][3] = json.getString("tipoticket");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        arraylistsconcecutivo = new ArrayList<>();

//                        Toast.makeText(HomeEstudiante.this,listfirst , Toast.LENGTH_SHORT).show();
                        for (int i = 0;i < arregloTicketsbuscados.length;i++){
                            String listfirst = arregloTicketsbuscados[i][0] + " - " + arregloTicketsbuscados[i][3];
//                            Toast.makeText(HomeEstudiante.this,arregloTicketsbuscados[i][0] + " - " + arregloTicketsbuscados[i][3] , Toast.LENGTH_SHORT).show();
                            arraylistsconcecutivo.add(listfirst);
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(HomeEstudiante.this,R.layout.list_item,arraylistsconcecutivo);
                        listTicketss.setAdapter(arrayAdapter);
                        listTicketss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int concest = Integer.parseInt(arregloTicketsbuscados[position][0]);
                                iniciarActividadCodigoQR(concest);
                            }
                        });
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeEstudiante.this, "ERROR AL BUSCAR", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}

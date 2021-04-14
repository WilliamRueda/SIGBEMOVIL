package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sigbe.ticketproject2.Models.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuInfoEst extends AppCompatActivity {

    Button btnSalir, btnQR;
    TextView tipoTicket,nombreEst,codigoest,fechaCompra,valorapgar;
    ImageView good,err;
    RequestQueue requestQueue;
    Bundle bundle;
    String codigoesc;
    Usuario uscbusc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info_est);

        tipoTicket = (TextView) findViewById(R.id.tipoTicket);
        nombreEst = (TextView) findViewById(R.id.nombreEst);
        fechaCompra = (TextView) findViewById(R.id.fechaCompra);
        valorapgar = (TextView) findViewById(R.id.valorpagar);
        btnSalir = (Button) findViewById(R.id.buttonSalir);
        btnQR = (Button) findViewById(R.id.buttonQR);
        good = (ImageView) findViewById(R.id.good);
        err = (ImageView) findViewById(R.id.error);

        good.setVisibility(View.INVISIBLE);
        err.setVisibility(View.INVISIBLE);

        bundle = getIntent().getExtras();
        codigoesc = bundle.getString("codigoEscaneado");
        Buscar("http://192.168.0.11/sigbeweb/app/componentes/Tickets/buscarReservaTicket.php?codigoesc=" + codigoesc);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leerQR();
            }
        });
    }


    public void cerrarSesion(){
        finishAffinity();
        startActivity(new Intent(MenuInfoEst.this, MainActivity.class));
    }

    public void leerQR(){
        finishAffinity();
        startActivity(new Intent(MenuInfoEst.this, HomeRestaurante.class));
    }


    @SuppressWarnings("unchecked")
    private void Buscar(String URL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String codigoEscaneado = codigoesc;
                        try {
                            tipoTicket.setText(response.getString("tipoTicket"));
                            nombreEst.setText(response.getString("nombreest"));
                            fechaCompra.setText(response.getString("fechaticket"));
                            Toast.makeText(MenuInfoEst.this, "BENEFICIARIO " + response.getString("beneficiario"), Toast.LENGTH_SHORT).show();
                            if(response.getInt("beneficiario") == 0){
                                err.setVisibility(View.VISIBLE);
                                valorapgar.setText("No es beneficiario");
                            }else{
                                good.setVisibility(View.VISIBLE);
                                valorapgar.setText("Si es beneficiario");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MenuInfoEst.this, "Email/Contraseña invalida - ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
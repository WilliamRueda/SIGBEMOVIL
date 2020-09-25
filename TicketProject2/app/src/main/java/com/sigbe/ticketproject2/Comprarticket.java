package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Comprarticket extends AppCompatActivity {


    Button btGuardar,btnHome;
    EditText nombre,saldo,codigoestudiante,tipoticket;
    Bundle bundle;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprarticket);

        bundle = getIntent().getExtras();

        btnHome  = (Button) findViewById(R.id.buttonHome);
        btGuardar = (Button) findViewById(R.id.btnGuardar);
        nombre = (EditText) findViewById(R.id.textNombre);
        saldo =  (EditText) findViewById(R.id.textSaldo);
        codigoestudiante =  (EditText) findViewById(R.id.textCodigoest);
        tipoticket =  (EditText) findViewById(R.id.textTipoticket);

        nombre.setText(bundle.getString("nombre") +  " " + bundle.getString("apellido"));
        saldo.setText(Integer.toString(bundle.getInt("saldo")));
        codigoestudiante.setText(Integer.toString(bundle.getInt("codigoestudiante")));
        tipoticket.setText("Ticket Refrigerio");
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarActividadHome(bundle);
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarTicket("http://sigbebackend.herokuapp.com/app/componentes/Tickets/crearTicket.php");
            }
        });
    }

    public void iniciarActividadHome(Bundle bundle){
          super.onBackPressed();
    }

    public void comprarTicket(String URL){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", bundle.getInt("identificacion") );
            jsonObject.put("valorticket", 1500 );
            jsonObject.put("tipoTicket", "Ticket Refrigerio" );
        } catch (JSONException e) {
            Toast.makeText(Comprarticket.this, "Error al asignar valores " , Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Toast.makeText(Comprarticket.this, "Compra del ticket exitoso", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Comprarticket.this, "ERROR AL COMPRAR", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}
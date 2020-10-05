package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Comprarticket extends AppCompatActivity {


    Button btGuardar,btnHome, btnSalir;
    EditText nombre,saldo,codigoestudiante,tipoticket;
    Bundle bundle;
    RequestQueue requestQueue;
    Date horaInicioAlmuerzo;
    Date horaFinAlmuerzo;
    Date horaInicioRefrigerio;
    Date horaFinRefrigerio;
    Calendar calendario = Calendar.getInstance();
    Date horaactual;
    List<String> listconfig;
    SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
    String horaactuals = calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE);
    int valorticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprarticket);

        bundle = getIntent().getExtras();
        buscarConfiguracion("https://sigbebackend.herokuapp.com/app/componentes/configuracion/listarConfiguracion.php");

        btnHome  = (Button) findViewById(R.id.buttonHome);
        btGuardar = (Button) findViewById(R.id.btnGuardar);
        btnSalir = (Button) findViewById(R.id.buttonSalir);
        nombre = (EditText) findViewById(R.id.textNombre);
        saldo =  (EditText) findViewById(R.id.textSaldo);
        codigoestudiante =  (EditText) findViewById(R.id.textCodigoest);
        tipoticket =  (EditText) findViewById(R.id.textTipoticket);

        nombre.setText(bundle.getString("nombre") +  " " + bundle.getString("apellido"));
        saldo.setText(Integer.toString(bundle.getInt("saldo")));
        codigoestudiante.setText(Integer.toString(bundle.getInt("codigoestudiante")));


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarActividadHome(bundle);
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });



        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarTicket("https://sigbebackend.herokuapp.com/app/componentes/Tickets/crearTicket.php");
            }
        });
    }

    public void cerrarSesion(){
        finishAffinity();
        startActivity(new Intent(Comprarticket.this, MainActivity.class));
    }

    public void iniciarActividadHome(Bundle bundle){
          super.onBackPressed();
    }

    @SuppressWarnings("unchecked")
    private void buscarConfiguracion(String URL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listconfig = Arrays.asList(response.getString("valorticketalmuerzo"),
                                    response.getString("valorticketrefrigerio"),
                                    response.getString("horainicioVentaAlmuerzo"),
                                    response.getString("horaFinVentaAlmuerzo"),
                                    response.getString("horainicioVentaRefrigerio"),
                                    response.getString("horaFinVentaRefrigerio"));

                                horaInicioAlmuerzo = hora.parse(listconfig.get(2));
                                horaFinAlmuerzo = hora.parse(listconfig.get(3));
                                horaInicioRefrigerio = hora.parse(listconfig.get(4));
                                horaFinRefrigerio = hora.parse(listconfig.get(5));
//                            Toast.makeText(Comprarticket.this, "HORA ACTUAL "  + hora.parse(horaactuals), Toast.LENGTH_SHORT).show();
                                horaactual = hora.parse(horaactuals);
                            if((horaactual.compareTo(horaInicioAlmuerzo) > 0) && (horaactual.compareTo(horaFinAlmuerzo) <= 0)){
                                tipoticket.setText("Ticket Almuerzo");
                                valorticket = Integer.parseInt(listconfig.get(0));
                                if(bundle.getInt("saldo") < valorticket){
                                    btGuardar.setEnabled(false);
                                    Toast.makeText(Comprarticket.this, "No tiene saldo suficiente, por favor recargue." , Toast.LENGTH_SHORT).show();
                                }
                            }else if ((horaactual.compareTo(horaInicioRefrigerio) > 0) && (horaactual.compareTo(horaFinRefrigerio) <= 0)){
                                tipoticket.setText("Ticket Refrigerio");
                                valorticket = Integer.parseInt(listconfig.get(1));
                                if(bundle.getInt("saldo") < valorticket){
                                    btGuardar.setEnabled(false);
                                    Toast.makeText(Comprarticket.this, "No tiene saldo suficiente, por favor recargue." , Toast.LENGTH_SHORT).show();
                                }
                            } else{
                                btGuardar.setEnabled(false);
                                tipoticket.setText("Hora invalida");
                                Toast.makeText(Comprarticket.this, "Actualmente no es una hora valida para comprar." , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Comprarticket.this, "Error al actualizar saldo - ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    @SuppressWarnings("unchecked")
    private void actualizarUsuario(String URL){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", bundle.getInt("identificacion") );
            jsonObject.put("saldo", saldo.getText() );
        } catch (JSONException e) {
            Toast.makeText(Comprarticket.this, "Error al asignar valores " , Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, URL, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(Comprarticket.this, "Actualizado el saldo correctamente ", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Comprarticket.this, "Error al actualizar saldo - ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void comprarTicket(String URL){
        final JSONObject jsonObject = new JSONObject();
        final String url = "http://sigbebackend.herokuapp.com/app/componentes/usuarios/updateSaldo.php";
        try {
            jsonObject.put("idUser", bundle.getInt("identificacion") );
            jsonObject.put("valorticket", valorticket);
            jsonObject.put("tipoTicket", tipoticket.getText());
        } catch (JSONException e) {
            Toast.makeText(Comprarticket.this, "Error al asignar valores " , Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int saldoDespuesCompra = Integer.parseInt(String.valueOf(saldo.getText())) - valorticket;
                        saldo.setText(Integer.toString(saldoDespuesCompra));
                        actualizarUsuario(url);

                        Toast.makeText(Comprarticket.this, "Compra del ticket exitoso", Toast.LENGTH_SHORT).show();
                        //bundle.getString("saldo") = ;

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
package com.sigbe.ticketproject2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Comprarticket extends AppCompatActivity {


    Button btGuardar,btnHome, btnSalir;
    EditText nombre,identificacion,fechacompra,tarifa,tipoticket;
    Bundle bundle;
    RequestQueue requestQueue;
    Date horaInicioAlmuerzo;
    Date horaFinAlmuerzo;
    Date horaInicioRefrigerio;
    Date horaFinRefrigerio;
    Calendar calendario = Calendar.getInstance();
    Boolean valtickettoday = false;
    Date horaactual;
    List<String> listconfig;
    Date date = new Date();
    SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
    SimpleDateFormat fechaticket = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String horaactuals = calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE);
    int valorticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprarticket);

        bundle = getIntent().getExtras();
        buscarConfiguracion("http://192.168.0.11/sigbeweb/app/componentes/configuracion/listarConfiguracion.php");

        btnHome  = (Button) findViewById(R.id.buttonQR);
        btGuardar = (Button) findViewById(R.id.btnGuardar);
        btnSalir = (Button) findViewById(R.id.buttonSalir);

        nombre = (EditText) findViewById(R.id.textNombre);
        identificacion = (EditText) findViewById(R.id.tetIdentificacion);
        fechacompra = (EditText) findViewById(R.id.textFecha);
//        tarifa = (EditText) findViewById(R.id.textTarifa);
        tipoticket =  (EditText) findViewById(R.id.textTipoticket2);

        nombre.setText(bundle.getString("nombre") +  " " + bundle.getString("apellido"));
        String identificacionval = Integer.toString(bundle.getInt("identificacion"));
        System.out.println(bundle.getInt("identificacion"));
        identificacion.setText(identificacionval);
        fechacompra.setText(fechaticket.format(date));





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
                comprarTicket("http://192.168.0.11/sigbeweb/app/componentes/Tickets/crearTicketmobile.php");
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
    private void buscarTicketByFechaUser(final String URL, final String URL2){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        valtickettoday = true;
                        Toast.makeText(Comprarticket.this, "Error, ya compraste ese tipo de ticket hoy" , Toast.LENGTH_SHORT).show();//
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        compraEfectiva(URL2);

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
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
                                    response.getString("horaFinVentaRefrigerio"),
                                    response.getString("horaactualserver"));

                                horaInicioAlmuerzo = hora.parse(listconfig.get(2));
                                horaFinAlmuerzo = hora.parse(listconfig.get(3));
                                horaInicioRefrigerio = hora.parse(listconfig.get(4));
                                horaFinRefrigerio = hora.parse(listconfig.get(5));
//                            Toast.makeText(Comprarticket.this, "HORA ACTUAL "  + hora.parse(horaactuals), Toast.LENGTH_SHORT).show();
                                horaactual = hora.parse(listconfig.get(6));
                            Toast.makeText(Comprarticket.this, "Hora actual " + horaactuals, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Comprarticket.this, "Hora inicio refrigerio "+ horaInicioRefrigerio, Toast.LENGTH_SHORT).show();
                            if((horaactual.compareTo(horaInicioAlmuerzo) >= 0) && (horaactual.compareTo(horaFinAlmuerzo) <= 0)){
                                tipoticket.setText("Ticket Almuerzo");
                                valorticket = Integer.parseInt(listconfig.get(0));
                            }else if ((horaactual.compareTo(horaInicioRefrigerio) >= 0) && (horaactual.compareTo(horaFinRefrigerio) <= 0)){
                                tipoticket.setText("Ticket Refrigerio");
                                valorticket = Integer.parseInt(listconfig.get(1));
                            } else{
                                btGuardar.setEnabled(false);
                                tipoticket.setText("hora invalida");
                                Toast.makeText(Comprarticket.this, "Actualmente no es una hora valida para comprar." , Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void compraEfectiva(String URL){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser", bundle.getInt("identificacion") );
            jsonObject.put("valorticket", 0);
            jsonObject.put("tipoTicket", tipoticket.getText());
            System.out.print(jsonObject);
        } catch (JSONException e) {
            Toast.makeText(Comprarticket.this, "Error al asignar valores " , Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(Comprarticket.this, "Compra del ticket exitoso", Toast.LENGTH_SHORT).show();
                        Bitmap largeIcon = BitmapFactory.decodeResource(Comprarticket.this.getResources(),
                                R.mipmap.logosigbe3);

                        Intent notificationIntent = new Intent(Comprarticket.this, MainActivity.class);

                        PendingIntent pendingIntent = PendingIntent.getActivity(Comprarticket.this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationManager notificationManager = (NotificationManager) Comprarticket.this
                                .getSystemService(Context.NOTIFICATION_SERVICE);

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Comprarticket.this)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle("Piensa en los demás - SIGBE")
                                .setContentText("Recuerda.")
                                .setSmallIcon(R.mipmap.logosigbe3)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("No te olvides que alguien más necesita puede necesitar tu mesa."))
                                .setContentIntent(pendingIntent)
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
                        notificationManager =
                                (NotificationManager) Comprarticket.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            String channelId = "1";
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "SIGBERESERVATICKET",
                                    NotificationManager.IMPORTANCE_HIGH);
                            notificationManager.createNotificationChannel(channel);
                            notificationBuilder.setChannelId(channelId);
                        }

                        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
                        //bundle.getString("saldo") = ;

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Comprarticket.this, "ERROR AL COMPRAR" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void comprarTicket(String URL){
        final JSONObject jsonObject = new JSONObject();
        final String url1 = "http://192.168.0.11/sigbeweb/app/componentes/Tickets/ticketbyfechauser.php?idUser="+ bundle.getInt("identificacion")  +"&tipoticket=" + tipoticket.getText();

        try {
            jsonObject.put("idUser", bundle.getInt("identificacion") );
            jsonObject.put("valorticket", 0);
            jsonObject.put("tipoTicket", tipoticket.getText());
        } catch (JSONException e) {
            Toast.makeText(Comprarticket.this, "Error al asignar valores " , Toast.LENGTH_SHORT).show();
        }

        buscarTicketByFechaUser(url1, URL);


    }


}
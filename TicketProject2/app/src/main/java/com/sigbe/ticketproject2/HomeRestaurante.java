package com.sigbe.ticketproject2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeRestaurante extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String codigoEscaneado;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);        // Start camera on resume


    }

//    public void btnEscanear(View v){
//
//        myScanerView = new ZXingScannerView(this);
//        setContentView(myScanerView);
//        myScanerView.setResultHandler(this);
//        myScanerView.startCamera();
//
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("HandleResult", rawResult.getText()); // Prints scan results
        Log.v("HandleResult", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Toast.makeText(HomeRestaurante.this, "CODIGO DETECTADO " + rawResult.getText(), Toast.LENGTH_SHORT).show();
        // If you would like to resume scanning, call this method below:
        codigoEscaneado = rawResult.getText();
        actualizarEstadoTicket("http://192.168.0.11/sigbeweb/app/componentes/Tickets/actualizarEstado.php");
        mScannerView.stopCamera();
        finishAffinity();
        Intent intent = new Intent (HomeRestaurante.this, MenuInfoEst.class);
        intent.putExtra("codigoEscaneado",codigoEscaneado);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @SuppressWarnings("unchecked")
    private void actualizarEstadoTicket(String URL){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codigoTicket", codigoEscaneado );
        } catch (JSONException e) {
            Toast.makeText(HomeRestaurante.this, "Error al asignar valores " , Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, URL, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Toast.makeText(HomeRestaurante.this, "Actualización del codigo " + codigoEscaneado + " con exito", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeRestaurante.this, "ERROR AL ACTUALIZAR", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}

package com.sigbe.ticketproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Codigoqr extends AppCompatActivity {
    Bundle bundle;
    ImageView qrImage;
    Button btGuardar,btnHome,btnSalir;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    int codigosl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigoqr);
        bundle = getIntent().getExtras();
        codigosl = bundle.getInt("codigo");
        qrImage = (ImageView) findViewById(R.id.imageCodigoQR);
        btGuardar = (Button) findViewById(R.id.btGuardarCodigo);
        btnHome = (Button) findViewById(R.id.buttonHome);
        btnSalir = (Button) findViewById(R.id.buttonSalir);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        qrgEncoder = new QRGEncoder(Integer.toString(codigosl), null, QRGContents.Type.TEXT,smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
        }
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Codigoqr.super.onBackPressed();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    public void cerrarSesion(){
        finishAffinity();
        startActivity(new Intent(Codigoqr.this, MainActivity.class));
    }

//    public void iniciarActividadHome(){
//        super.onBackPressed();
//    }
}
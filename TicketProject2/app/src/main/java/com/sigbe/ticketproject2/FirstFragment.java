package com.sigbe.ticketproject2;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class FirstFragment extends AppCompatActivity {

    Bundle bundle;
    ImageView qrImage;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);
//        qrImage = (ImageView) findViewById(R.id.imageCodigoQR);
//        bundle = getIntent().getExtras();
//        String codigosl = bundle.getString("codigo");
//        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display display = manager.getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//        int width = point.x;
//        int height = point.y;
//        int smallerDimension = width < height ? width : height;
//        smallerDimension = smallerDimension * 3 / 4;
//        qrgEncoder = new QRGEncoder(codigosl, null, QRGContents.Type.TEXT,smallerDimension);
//        try {
//            bitmap = qrgEncoder.encodeAsBitmap();
//            qrImage.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//        }

    }
}
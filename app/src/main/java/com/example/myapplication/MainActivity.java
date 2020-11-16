package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.ImagePrintable;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PrintingCallback {
    Button image_b,pairing_b;
    Printing printing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image_b=findViewById(R.id.image);
        pairing_b=findViewById(R.id.pairing);

        if(printing!=null)
        printing.setPrintingCallback(this);

        pairing_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Printooth.INSTANCE.hasPairedPrinter()){
                    Printooth.INSTANCE.removeCurrentPrinter();
                }
                else {
                    startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                    changePairAndUnPair();
                }
            }
        });
        image_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Printooth.INSTANCE.hasPairedPrinter()){
                    startActivityForResult(new Intent(MainActivity.this,ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                }
                else {
                    printImage();
//                    printText();
                }
            }
        });
    }

    private void printText() {

    }

    private void printImage() {
        ArrayList<Printable>printable=new ArrayList<>();
        Picasso.get().load("https://www.google.com/imgres?imgurl=https%3A%2F%2Fimages-na.ssl-images-amazon.com%2Fimages%2FI%2F81CZQfGz6UL._SL1500_.jpg&imgrefurl=https%3A%2F%2Fwww.amazon.in%2FOhhSome-Flower-Seeds-Fortune-Greenary%2Fdp%2FB07WQJBW4D&tbnid=Wl0I73gmXLF3JM&vet=12ahUKEwi9uqews4btAhWyQ30KHThxD9QQMygAegUIARDhAQ..i&docid=6cyGFRIcOgRGwM&w=1500&h=1000&q=greenary&ved=2ahUKEwi9uqews4btAhWyQ30KHThxD9QQMygAegUIARDhAQ")
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        printable.add(new ImagePrintable.Builder(bitmap).build());
                        printing.print(printable);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private void changePairAndUnPair() {
        if(Printooth.INSTANCE.hasPairedPrinter()){
            pairing_b.setText(new StringBuilder("UnPair")
                    .append(Objects.requireNonNull(Printooth.INSTANCE.getPairedPrinter()).getName()).toString());
        }
        else {
            pairing_b.setText("Pairing with Printer");
        }
    }

    @Override
    public void connectingWithPrinter() {

        Toast.makeText(this, "connecting", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionFailed(String s) {

        Toast.makeText(this, "connection failed "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String s) {

        Toast.makeText(this, "error in connecting "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessage(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printingOrderSentSuccessfully() {

        Toast.makeText(this, "printing order sent successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ScanningActivity.SCANNING_FOR_PRINTER&& resultCode==RESULT_OK)
        {
            initPrinting();
            changePairAndUnPair();
        }
    }

    private void initPrinting() {
        if(!Printooth.INSTANCE.hasPairedPrinter()){
            printing=Printooth.INSTANCE.printer();
        }
        if(printing!=null){
            printing.setPrintingCallback(this);
        }
    }
}
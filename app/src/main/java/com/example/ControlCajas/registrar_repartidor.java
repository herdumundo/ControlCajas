package com.example.ControlCajas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import Utilidades.CRUD;

public class registrar_repartidor extends AppCompatActivity {
    public static EditText nombre;
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("VOLVER ATRAS.")
                .setMessage("CONTINUAR?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        Intent List = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(List);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_repartidor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CRUD.conexion_sqlite(this);
        nombre= (EditText) findViewById(R.id.txt_nombre);
     }

    public void registrar_repar(View view)
    {
        CRUD.registrar_repartidor(this);
    }







}
package com.example.ControlCajas;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import Utilidades.CRUD;

public class clientes extends AppCompatActivity
{
   public static EditText ruc_ci,razon_social;

     @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        razon_social= (EditText) findViewById(R.id.txt_razon_social);
        ruc_ci = (EditText) findViewById(R.id.txt_ruc_ci);
    }

    public void onClick(View view)
    {
        CRUD.registrarClientes(this);
    }



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

}



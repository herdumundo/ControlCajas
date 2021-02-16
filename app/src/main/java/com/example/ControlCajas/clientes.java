package com.example.ControlCajas;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Utilidades.Utilidades;
import entidades.Clientes;

public class clientes extends AppCompatActivity
{
    EditText ruc_ci,razon_social;
    ConexionSQLiteHelper conn;
    ArrayList<String> lista_clientes;
    ArrayList<Clientes> ClientesList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        razon_social= (EditText) findViewById(R.id.txt_razon_social);
        ruc_ci = (EditText) findViewById(R.id.txt_ruc_ci);
    }

    public void onClick(View view)
    {
        registrarClientes();
    }

    private void registrarClientes()
    {
        SQLiteDatabase db=conn.getWritableDatabase();
        Integer id=0;
        Integer band=0;
        Cursor cursor = db.rawQuery("select case  when count(*) = 0 then 1 else max(cod_cliente) +1  end as d from clientes", null);
        while (cursor.moveToNext())
        {
            id = cursor.getInt(0);
        }

        Cursor cursor1 = db.rawQuery("select * from clientes where cod_cliente = " + id + " or razon_social = '" + razon_social.getText().toString() + "' or ruc = '" + ruc_ci.getText().toString() + "'", null);
        while (cursor1.moveToNext())
        {
            band = 1;
            Toast.makeText(getApplicationContext(), "Registros duplicados", Toast.LENGTH_SHORT).show();
        }
        if (band == 0)
        {
            ContentValues values=new ContentValues();
            values.put("cod_cliente", id);
            values.put("razon_social",razon_social.getText().toString().toUpperCase());
            values.put("estado", "A");
            values.put("ruc", ruc_ci.getText().toString());

            Long idResultante=db.insert(Utilidades.TABLA_CLIENTES,null,values);

            Toast.makeText(getApplicationContext(),"Id Registro: "+idResultante,Toast.LENGTH_LONG).show();
            db.close();
            limpiar_form();
        }
    }
    private void limpiar_form()
    {
        razon_social.setText("");
        ruc_ci.setText("");
        razon_social.requestFocus();
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



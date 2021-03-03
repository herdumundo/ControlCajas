package com.example.ControlCajas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import Utilidades.CRUD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.Utilidades;

public class login extends AppCompatActivity
{
    Button btn_sincro,btn_login;
    TextView txt_usuario,txt_pass;
    int contador=0;
    private ProgressDialog prodialog;
    String mensaje="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btn_sincro=(Button)findViewById(R.id.btn_sincronizar);
        btn_login=(Button)findViewById(R.id.btn_login);
        txt_usuario=(TextView)findViewById(R.id.txt_usuario);
        txt_pass=(TextView)findViewById(R.id.password);

        CRUD.conexion_sqlite(this);

        btn_sincro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(login.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("SINCRONIZACION DE USUARIOS.")
                        .setMessage("DESEA ACTUALIZAR USUARIOS DISPONIBLES?.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {


                                try {
                                    CRUD.connect =  CRUD.conexion.Connections();
                                    Statement stmt3 = CRUD.connect.createStatement();
                                    ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  usuarios");
                                    while (rs3.next())
                                    {
                                        contador=rs3.getInt("contador");
                                    }

                                    rs3.close();
                                }catch(Exception e)
                                {

                                }
                                prodialog =  new ProgressDialog(login.this);
                                LayerDrawable progressBarDrawable = new LayerDrawable(
                                        new Drawable[]{
                                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                        new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                                new ClipDrawable(
                                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                                new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                        Gravity.START,
                                                        ClipDrawable.HORIZONTAL),
                                                new ClipDrawable(
                                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                                new int[]{Color.parseColor("#ffff0000"),Color.parseColor("#ffff0000")}),
                                                        Gravity.START,
                                                        ClipDrawable.HORIZONTAL)
                                        });
                                progressBarDrawable.setId(0,android.R.id.background);
                                progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                                progressBarDrawable.setId(2,android.R.id.progress);
                                prodialog.setMax(contador);
                                prodialog.setTitle("SINCRONIZANDO USUARIOS");
                                prodialog.setMessage("ESPERE...");
                                prodialog.setProgressDrawable(progressBarDrawable);
                                prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                prodialog.show();
                                prodialog.setCanceledOnTouchOutside(false);
                                prodialog.setCancelable(false);
                                new login.Hilo_sincro_usuarios().start();



                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
    }

     class Hilo_sincro_usuarios extends Thread
     {
         @Override
         public void run()
         {
             try
             {
                 sincronizar_usuarios();
                 runOnUiThread(new Runnable()
                 {
                     @Override
                     public void run()
                     {
                         prodialog.dismiss();
                         Toast.makeText(login.this, mensaje, Toast.LENGTH_SHORT).show();


                     }
                 });
             }
             catch ( Exception e)
             {
                 e.printStackTrace();
             }
         }
     }





    private void sincronizar_usuarios()
    {
        try
        {
            Integer id;
            int c =0;
            String usuario="";String pass="";
             SQLiteDatabase db=CRUD.conn.getReadableDatabase();

            SQLiteDatabase db1=CRUD.conn.getReadableDatabase();
            String strSQL = "delete from usuarios";
            db1.execSQL(strSQL);
            //db1.close();

            CRUD.connect = CRUD.conexion.Connections();
            String query = "select *  from  usuarios";
            Statement stmt = CRUD.connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {
                id = rs.getInt("codUsuario");
                usuario= rs.getString("usuario");
                pass= rs.getString("password");
                ContentValues values=new ContentValues();
                values.put("codUsuario",id);
                values.put("usuario",usuario.toString());
                values.put("password",pass.toString().trim());
                Long idResultante=db.insert(Utilidades.TABLA_USUARIOS, "codUsuario",values);
                c++;
                prodialog.setProgress(c);
            }
          db.close();
            mensaje="DATOS SINCRONIZADOS CORRECTAMENTE.";
        }
        catch(Exception e)
        {
            mensaje=e.toString();
        }
    }



    private void login ()
    {
        SQLiteDatabase db=CRUD.conn.getReadableDatabase();
        String respuesta="0";

        Cursor cursor=db.rawQuery("SELECT * FROM  usuarios where usuario='"+txt_usuario.getText().toString().trim()+"' and password='"+txt_pass.getText()+"'" ,null);
        while (cursor.moveToNext())
        {
            respuesta=(cursor.getString(0));
        }
        if (respuesta.equals("0"))
        {
            new AlertDialog.Builder(login.this)
                    .setTitle("ATENCION!!!")
                    .setMessage("USUARIO INCORRECTO")
                    .setNegativeButton("CERRAR", null).show();
        }
        else
            {
                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
            }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        moveTaskToBack(true);
    }
}

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
import entidades.contenedor_tipo_envio;
import Utilidades.CRUD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import Utilidades.Utilidades;

public class MainActivity extends AppCompatActivity
{
    private ProgressDialog prodialog;
    Button btn_movimiento,btn_sincro;
    int contador=0;
    String mensaje_sincro="";
    String mensaje ="";
    int color_mensaje=0;
    int total_pendientes_envio=0;
    TextView txt_estado_conexion,txt_total_pendientes;
    private volatile boolean flag = true;
    private volatile boolean bandera_hilo_pendiente = true;
    int contador_maples_cajas=0;
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("DESEA CERRAR SESION.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(getApplicationContext(), login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btn_movimiento= (Button)findViewById(R.id.idmovimiento) ;
        btn_sincro= (Button)findViewById(R.id.btnSincro) ;
        txt_estado_conexion=(TextView)findViewById(R.id.txt_estado);
        txt_total_pendientes=(TextView)findViewById(R.id.txt_total_pendientes);
        CRUD.conexion_sqlite(this);
        final MyThread_pendientes thread_pendientes = new MyThread_pendientes();
        thread_pendientes.start();
        final MyThread thread = new MyThread();
        thread.start();

         btn_sincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("SINCRONIZACION DE DATOS.")
                        .setMessage("DESEA ACTUALIZAR LOS DATOS DISPONIBLES?.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                try {
                                    CRUD.connect = CRUD.conexion.Connections();
                                    Statement stmt3 = CRUD.connect.createStatement();
                                    ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  repartidores with(nolock)");
                                    while (rs3.next())
                                    {
                                        contador=rs3.getInt("contador");
                                    }
                                    rs3.close();
                                }catch(Exception e)
                                {

                                }
                        prodialog =  new ProgressDialog(MainActivity.this);
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
                                prodialog.setTitle("SINCRONIZANDO REPARTIDORES");
                                prodialog.setMessage("REGISTRANDO...");
                                prodialog.setProgressDrawable(progressBarDrawable);
                                prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                prodialog.show();
                                prodialog.setCanceledOnTouchOutside(false);
                                prodialog.setCancelable(false);
                                new MainActivity.hilo_importar_repartidor().start();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                        }
        });
    }

    public  void ir_registro_cajas(View view) {
        contenedor_tipo_envio.nombre_envio="DEVOLUCIONES DE CAJAS";
        contenedor_tipo_envio.tipo_envio=2;
        flag=false;
        Intent i=new Intent(this, registro_cajas.class);
        startActivity(i);
    }
    public  void ir_registro_cajas_salida(View view) {
        contenedor_tipo_envio.nombre_envio="ENVIO DE CAJAS";
        contenedor_tipo_envio.tipo_envio=1;
        flag=false;
        Intent i=new Intent(this, registro_cajas.class);
        startActivity(i);
    }
    public  void ir_registro_clientes(View view) {
        flag=false;
        Intent i=new Intent(this, clientes.class);
        startActivity(i);
    }
    public  void ir_registro_repartidor(View view) {
        flag=false;
        Intent i=new Intent(this, registrar_repartidor.class);
        startActivity(i);
    }
    public  void ir_informe_menu(View view) {
        flag=false;
        Intent i=new Intent(this, informe_menu.class);
        startActivity(i);
    }
    public  void ir_registro_maples(View view) {
        contenedor_tipo_envio.nombre_envio="DEVOLUCIONES DE MAPLES";
        contenedor_tipo_envio.tipo_envio=2;
        flag=false;
        Intent i=new Intent(this, recepcion_maples.class);
        startActivity(i);
    }
    public  void ir_registro_maples_salida(View view) {
        contenedor_tipo_envio.nombre_envio="ENVIO DE MAPLES";
        contenedor_tipo_envio.tipo_envio=1;
        flag=false;
        Intent i=new Intent(this, recepcion_maples.class);
        startActivity(i);
    }
    public  void exportar_datos(View view) {
        conexion c = new conexion();


        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("EXPORTACION DE DATOS.")
                .setMessage("DESEA ENVIAR LOS DATOS REGISTRADOS?.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        try {
                            SQLiteDatabase db=CRUD.conn.getReadableDatabase();
                            Cursor cursor=db.rawQuery("select count(*) from (SELECT   nro_movimiento   FROM movimientos   where   estado ='P'" +
                                    " union all SELECT   nro_movimiento   FROM mov_maples   where   estado ='P' " +
                                    "union all SELECT   cod_cliente   FROM clientes    where   estado ='A' " +
                                    "union all SELECT   cod_repartidor   FROM repartidores    where   estado ='A') t  " ,null);

                            while (cursor.moveToNext())
                            {
                                contador=cursor.getInt(0);
                            }

                        }catch(Exception e)
                        {

                        }

                        prodialog =  new ProgressDialog(MainActivity.this);
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
                                                        new int[]{Color.parseColor("#f9ff00"),Color.parseColor("#f9ff00")}),
                                                Gravity.START,
                                                ClipDrawable.HORIZONTAL)
                                });
                        progressBarDrawable.setId(0,android.R.id.background);
                        progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                        progressBarDrawable.setId(2,android.R.id.progress);
                        prodialog.setMax(contador);
                        prodialog.setTitle("EXPORTANDO DATOS");
                        prodialog.setMessage("ENVIANDO...");
                        prodialog.setProgressDrawable(progressBarDrawable);
                        prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        prodialog.show();
                        prodialog.setCanceledOnTouchOutside(false);
                        prodialog.setCancelable(false);
                        new MainActivity.Hilo_exportar_registro().start();
                    }
                })
                .setNegativeButton("NO", null)
                .show();


    }
    private void exportar_cajas_movimiento(){
    try {
        contador_maples_cajas=0;
        SQLiteDatabase db=CRUD.conn.getReadableDatabase();
        SQLiteDatabase db_detalle=CRUD.conn.getReadableDatabase();
         CRUD.connect = CRUD.conexion.Connections();
        Cursor cursor=db.rawQuery("SELECT fecha,repartidor,tipo_mov,cod_cliente,sucursal,ayudante,nro_movimiento  FROM movimientos where estado='P'" ,null);
         while (cursor.moveToNext())
        {
            int res_final_trans=1;
            int res_trans=0;
            int id_cab=0;
            int id_cab_cursor=0;
            CRUD.connect.setAutoCommit(false);

            id_cab_cursor=cursor.getInt(6);
            CallableStatement callableStatement=null;
            callableStatement = CRUD.connect.prepareCall("{call pa_alta_exportar_cajas( ?,?,?,?,?,?,?,? )}");
            callableStatement .setString("@fecha_t",cursor.getString(0));
            callableStatement .setInt("@repartidor",cursor.getInt(1));
            callableStatement .setInt("@tipo_mov", cursor.getInt(2));
            callableStatement .setString("@cod_cliente", cursor.getString(3) );
            callableStatement .setString("@sucursal", cursor.getString(4));
            callableStatement .setInt("@ayudante",cursor.getInt(5) );
            callableStatement .setInt("@mensaje",0);
            callableStatement .setInt("@out_nro_movimiento",0);
            callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
            callableStatement.registerOutParameter("@out_nro_movimiento", Types.INTEGER);
            callableStatement.execute();
            res_trans = callableStatement.getInt("@mensaje");
            id_cab  = callableStatement.getInt("@out_nro_movimiento");


            if(res_trans==1)
            {
                Cursor cursor_detalle=db_detalle.rawQuery("SELECT cod_caja  FROM det_movimientos where  nro_movimiento="+id_cab_cursor+"" ,null);
                while (cursor_detalle.moveToNext())
                {
                    int cod_caja= (cursor_detalle.getInt(0));
                    CallableStatement call_det=null;
                    call_det = CRUD.connect.prepareCall("{call pa_alta_exportar_cajas_det( ?, ?,?  )}");
                    call_det .setInt("@nro_movimiento",id_cab);
                    call_det .setInt("@cod_barra",cod_caja);
                    call_det.registerOutParameter("@mensaje", Types.INTEGER);
                    call_det.execute();
                    res_trans = call_det.getInt("@mensaje");
                    res_final_trans=res_final_trans*res_trans;
                }
                cursor_detalle.close();
                if(res_final_trans==1)
                {
                    CRUD.connect.commit();
                    SQLiteDatabase db_upd_cab=CRUD.conn.getReadableDatabase();
                    SQLiteDatabase db_upd_det=CRUD.conn.getReadableDatabase();
                    db_upd_cab.execSQL("UPDATE movimientos SET estado='C' WHERE nro_movimiento='"+id_cab_cursor+"'");
                    db_upd_det.execSQL("UPDATE det_movimientos SET estado='C' WHERE nro_movimiento='"+id_cab_cursor+"'");
                }
                else {
                    CRUD.connect.rollback();
                }
             }
           else {
                CRUD.connect.rollback();
                }
            contador_maples_cajas++;
            prodialog.setProgress(contador_maples_cajas);
        }
        cursor.close();
        db.close();
        db_detalle.close();
        pendientes_envio();
        mensaje="DATOS ENVIADOS CON EXITO.";

        exportar_maples();

    }
    catch (Exception e)
    {
    mensaje=e.toString()+" ERROR AL EXPORTAR REGISTROS DE CAJAS. ";
    }
        }
    private void test_conexion(){
        conexion c = new conexion();

        if(c.getConexion()!=null){
            try {
                mensaje_sincro="EN LINEA";
                color_mensaje=0xFF00FF00;

            }catch(Exception e)
            {
                mensaje_sincro="SIN CONEXION";
                color_mensaje=0xFFFF0000;
            }

        }
        else {
            mensaje_sincro="SIN CONEXION";
            color_mensaje=0xFFFF0000;
        }

    }
    private void pendientes_envio() {
        // int c=0;
        try {
             SQLiteDatabase db=CRUD.conn.getReadableDatabase();
            CRUD.connect = CRUD.conexion.Connections();

            Cursor cursor=db.rawQuery("select count(*) from (SELECT   nro_movimiento   FROM movimientos   where   estado ='P'" +
                    " union all SELECT   nro_movimiento   FROM mov_maples   where   estado ='P' " +
                    "union all SELECT   cod_cliente   FROM clientes    where   estado ='A' " +
                    "union all SELECT   cod_repartidor   FROM repartidores    where   estado ='A') t  " ,null);
            while (cursor.moveToNext()){
                total_pendientes_envio=cursor.getInt(0);
            }

        }catch(Exception e)
        {
        }
    }
    private void sincroniza_repartidores() {
        try
        {
            CRUD.connect = CRUD.conexion.Connections();

            SQLiteDatabase db1=CRUD.conn.getReadableDatabase();
            String strSQL = "delete from repartidores where estado='C'";
            db1.execSQL(strSQL);
             db1.close();

            SQLiteDatabase db=CRUD.conn.getReadableDatabase();
            Statement stmt = CRUD.connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  repartidores ");
            int c=0;
            while (rs.next())
            {
                ContentValues values=new ContentValues();
                values.put("cod_repartidor",rs.getString("cod_repartidor"));
                values.put("repartidor",rs.getString("repartidor"));
                values.put("estado","C");
                db.insert("repartidores", null,values);

                c++;
                prodialog.setProgress(c);

            }
            rs.close();
            db1.close();
            db.close();
            CRUD. connect.close();
            mensaje="REPARTIDORES SINCRONIZADOS.";

        }
        catch(Exception e)
        {
        mensaje=e.toString();
        }
    }
    private void sincronizar_cliente() {
        try
        {
            int c=0;
            Integer id; String cliente="";String ruc="";
            SQLiteDatabase db=CRUD.conn.getReadableDatabase();
            SQLiteDatabase db1=CRUD.conn.getReadableDatabase();
            db1.execSQL("delete from clientes where estado='C'");
            CRUD.connect = CRUD.conexion.Connections();
            String query = "select cod_cliente,razon_social,isnull(ruc,'') as ruc  from  clientes  with(nolock)";
            Statement stmt = CRUD.connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {
                id = rs.getInt("cod_cliente");
                cliente= rs.getString("razon_social");
                ruc= rs.getString("ruc");
                ContentValues values=new ContentValues();
                values.put("cod_cliente",id);
                values.put("razon_social",cliente.toString());
                values.put("ruc",ruc.toString().trim());
                values.put("estado","C");
                db.insert(Utilidades.TABLA_CLIENTES, null, values);
                c++;
                prodialog.setProgress(c);
            }
            mensaje="DATOS SINCRONIZADOS CORRECTAMENTE";

            db.close();
            db1.close();
        }
        catch(Exception e)
        {
            mensaje= e.toString();
        }
    }

    class Hilo_sincronizar_clientes extends Thread {
        @Override
        public void run()
        {
            try
            {
                sincronizar_cliente();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        prodialog.dismiss();

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("DETALLE")
                                .setMessage(mensaje)
                                .setNegativeButton("CERRAR", null).show();


                    }
                });
            }
            catch ( Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class Hilo_exportar_registro extends Thread {
        @Override
        public void run()
        {
            try
            {
                conexion c=new conexion();
                if(c.getConexion()!=null){
                    exportar_cajas_movimiento();
                    exportar_maples();
                    pendientes_envio();

                }
                else {
                    mensaje="ERROR AL INTENTAR CONECTARSE AL SERVIDOR, FAVOR VERIFIQUE WIFI.";
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        txt_total_pendientes.setText(String.valueOf(total_pendientes_envio));
                        prodialog.dismiss();

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("DETALLE")
                                .setMessage(mensaje)
                                .setNegativeButton("CERRAR", null).show();
                    }
                });
            }
            catch ( Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class hilo_importar_repartidor extends Thread {

        @Override
        public void run() {

            try {
                sincroniza_repartidores();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();


                            prodialog.dismiss();
                            try {
                                CRUD.connect = CRUD.conexion.Connections();
                                Statement stmt3 = CRUD.connect.createStatement();
                                ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  clientes");
                                while (rs3.next())
                                {
                                    contador=rs3.getInt("contador");
                                }

                                rs3.close();

                            prodialog =  new ProgressDialog(MainActivity.this);
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
                            prodialog.setTitle("SINCRONIZANDO CLIENTES");
                            prodialog.setMessage("ESPERE...");
                            prodialog.setProgressDrawable(progressBarDrawable);
                            prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            prodialog.show();
                            prodialog.setCanceledOnTouchOutside(false);
                            prodialog.setCancelable(false);
                            new MainActivity.Hilo_sincronizar_clientes().start();

                            }
                         catch (Exception e){

                        }
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyThread extends Thread {


        @Override
        public void run()
        {
            while (flag)
        {
            try {


                test_conexion();
                Thread.sleep((long) 5000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        txt_estado_conexion.setText(mensaje_sincro);
                        txt_estado_conexion.setTextColor(color_mensaje);
                     }
                });

            } catch (InterruptedException e) {
            }
        }
        }
    }

    class MyThread_pendientes extends Thread {


        @Override
        public void run()
        {
            while (bandera_hilo_pendiente)
            {
                try {

                   pendientes_envio();
                    System.out.println("EJECUTADO");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bandera_hilo_pendiente=false;
                            txt_total_pendientes.setText(String.valueOf(total_pendientes_envio));
                            System.out.println("HILO MUERTO");

                        }
                    });

                } catch (Exception e) {

                    System.out.println(e.toString());
                }
            }
        }
    }








    private void exportar_maples(){
        try {
            SQLiteDatabase db=CRUD.conn.getReadableDatabase();
            SQLiteDatabase db_detalle=CRUD.conn.getReadableDatabase();
            CRUD.connect = CRUD.conexion.Connections();

            Cursor cursor=db.rawQuery("SELECT fecha,repartidor,tipo_mov,cod_cliente,sucursal,ayudante,nro_movimiento " +
                    "FROM mov_maples where estado='P'" ,null);
            String fecha,repartidor,tipo_mov,cod_cliente,sucursal,ayudante,nro_movimiento;
            while (cursor.moveToNext())
            {
                int res_final_trans=1;
                int res_trans=0;
                int id_cab=0;
                int id_cab_cursor=0;
                CRUD.connect.setAutoCommit(false);

                fecha=cursor.getString(0);
                repartidor=cursor.getString(1);
                tipo_mov=cursor.getString(2);
                cod_cliente=cursor.getString(3);
                sucursal=cursor.getString(4);
                ayudante=cursor.getString(5);
                id_cab_cursor=cursor.getInt(6);
                CallableStatement callableStatement=null;
                callableStatement = CRUD.connect.prepareCall("{call pa_alta_exportar_maples( ?,?,?,?,?,?,?,? )}");
                callableStatement .setString("@fecha_t",fecha);
                callableStatement .setInt("@repartidor",Integer.parseInt(repartidor));
                callableStatement .setInt("@tipo_mov", Integer.parseInt(tipo_mov));
                callableStatement .setString("@cod_cliente",  cod_cliente );
                callableStatement .setString("@sucursal", sucursal);
                callableStatement .setInt("@ayudante",Integer.parseInt(ayudante) );
                callableStatement .setInt("@mensaje",0);
                callableStatement .setInt("@out_nro_movimiento",0);
                callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
                callableStatement.registerOutParameter("@out_nro_movimiento", Types.INTEGER);
                callableStatement.execute();
                res_trans = callableStatement.getInt("@mensaje");
                id_cab  = callableStatement.getInt("@out_nro_movimiento");


                if(res_trans==1)
                {
                    Cursor cursor_detalle=db_detalle.rawQuery("SELECT tipo_m,cantidad " +
                            "FROM det_mov_maples where  nro_movimiento="+id_cab_cursor+"" ,null);
                    while (cursor_detalle.moveToNext())
                    {
                        int tipo_m= (cursor_detalle.getInt(0));
                        int cantidad= (cursor_detalle.getInt(1));
                        CallableStatement call_det=null;
                        call_det = CRUD.connect.prepareCall("{call pa_alta_exportar_maples_det( ?, ?,?,?  )}");
                        call_det .setInt("@nro_movimiento",id_cab);
                        call_det .setInt("@tipo_m",tipo_m);
                        call_det .setInt("@cantidad",cantidad);
                        call_det.registerOutParameter("@mensaje", Types.INTEGER);
                        call_det.execute();
                        res_trans = call_det.getInt("@mensaje");
                        res_final_trans=res_final_trans*res_trans;
                    }
                    cursor_detalle.close();
                    if(res_final_trans==1)
                    {
                        CRUD.connect.commit();
                        SQLiteDatabase db_upd_cab=CRUD.conn.getReadableDatabase();
                        SQLiteDatabase db_upd_det=CRUD.conn.getReadableDatabase();
                        db_upd_cab.execSQL("UPDATE mov_maples SET estado='C' WHERE nro_movimiento='"+id_cab_cursor+"'");
                        db_upd_det.execSQL("UPDATE det_mov_maples SET estado='C' WHERE nro_movimiento='"+id_cab_cursor+"'");
                    }
                    else {
                        CRUD.connect.rollback();
                    }
                }
                else {
                    CRUD.connect.rollback();
                }
                contador_maples_cajas++;
                prodialog.setProgress(contador_maples_cajas);
            }
            cursor.close();
            db.close();
            db_detalle.close();
            mensaje="DATOS ENVIADOS CON EXITO.";
            exportar_clientes_creados();

        }
        catch (Exception e)
        {
            mensaje=e.toString()+" ERROR EXPORTANDO MAPLES.";
        }
    }
    private void exportar_clientes_creados(){
        try {


            SQLiteDatabase db=CRUD.conn.getReadableDatabase();
            CRUD.connect = CRUD.conexion.Connections();
            Cursor cursor=db.rawQuery("SELECT cod_cliente , razon_social  , ruc    FROM clientes where estado='A'" ,null);


            while (cursor.moveToNext())
            {
                int res_trans=0;
                CRUD.connect.setAutoCommit(false);


                CallableStatement callableStatement=null;
                callableStatement = CRUD.connect.prepareCall("{call pa_alta_exportar_clientes_sqlite( ?,?,?,?  )}");
                callableStatement .setInt("@cod_cliente",cursor.getInt(0));
                callableStatement .setString("@razon",cursor.getString(1));
                callableStatement .setString("@ruc", cursor.getString(2));
                callableStatement .setInt("@mensaje",0);

                callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
                callableStatement.execute();
                res_trans = callableStatement.getInt("@mensaje");

                if(res_trans==1){
                    CRUD.connect.commit();

                    SQLiteDatabase db_upd_cliente=CRUD.conn.getReadableDatabase();
                    db_upd_cliente.execSQL("UPDATE clientes SET estado='C' WHERE cod_cliente='"+cursor.getInt(0)+"'");
                }
                contador_maples_cajas++;
                prodialog.setProgress(contador_maples_cajas);
            }



            cursor.close();
            db.close();
            exportar_repartidores_creados();

            mensaje="DATOS ENVIADOS CON EXITO.";
        }
        catch (Exception e)
        {
            mensaje=e.toString()+" ERROR EXPORTANDO CLIENTES";
        }
    }
    private void exportar_repartidores_creados(){
        try {


            SQLiteDatabase db=CRUD.conn.getReadableDatabase();
            CRUD.connect = CRUD.conexion.Connections();
            Cursor cursor=db.rawQuery("SELECT cod_repartidor , repartidor  FROM repartidores where estado='A'" ,null);


            while (cursor.moveToNext())
            {
                int res_trans=0;
                CRUD.connect.setAutoCommit(false);


                CallableStatement callableStatement=null;
                callableStatement = CRUD.connect.prepareCall("{call pa_alta_exportar_repartidor_sqlite( ?,?,?  )}");
                callableStatement .setInt("@cod_repartidor",cursor.getInt(0));
                callableStatement .setString("@nombre",cursor.getString(1));
                 callableStatement .setInt("@mensaje",0);

                callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
                callableStatement.execute();
                res_trans = callableStatement.getInt("@mensaje");

                if(res_trans==1){
                    CRUD.connect.commit();

                    SQLiteDatabase db_upd_cliente=CRUD.conn.getReadableDatabase();
                    db_upd_cliente.execSQL("UPDATE repartidores SET estado='C' WHERE cod_repartidor='"+cursor.getInt(0)+"'");
                }
                contador_maples_cajas++;
                prodialog.setProgress(contador_maples_cajas);
            }



            cursor.close();
            db.close();

            mensaje="DATOS ENVIADOS CON EXITO.";
        }
        catch (Exception e)
        {
            mensaje=e.toString()+"ERROR EXPORTANDO REPARTIDORES";
        }
    }

}

package Utilidades;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.ControlCajas.ConexionSQLiteHelper;
import com.example.ControlCajas.ConnectionGrupoMaehara;
import com.example.ControlCajas.registrar_repartidor;
import com.example.ControlCajas.clientes;

import java.sql.Connection;

public class CRUD {
    public static ConexionSQLiteHelper conn;
    public static ConnectionGrupoMaehara conexion = new ConnectionGrupoMaehara();
    public static Connection connect;

    public static void conexion_sqlite(Context context) {
        conn=      new ConexionSQLiteHelper(context,"bd_usuarios",null,2);
     }


    public static void registrar_repartidor(Context context)
    {
        SQLiteDatabase db= conn.getWritableDatabase();
        Integer id=0;
        Cursor cursor = db.rawQuery("select case  when count(*) = 0 then 1 else max(cod_repartidor) +1  end as d from repartidores", null);
        while (cursor.moveToNext())
        {
            id = cursor.getInt(0);
        }

        ContentValues values=new ContentValues();
        values.put("cod_repartidor", id);
        values.put("repartidor",registrar_repartidor.nombre.getText().toString().toUpperCase());
        values.put("estado", "A");

        db.insert("repartidores" ,null,values);

        db.close();
        registrar_repartidor.nombre.setText("");

        new AlertDialog.Builder(context)
                .setTitle("ATENCION!!!")
                .setMessage("REGISTRADO CON EXITO!")
                .setNegativeButton("CERRAR", null).show();
    }


    public static void registrarClientes(Context context)
    {
        SQLiteDatabase db=conn.getWritableDatabase();
        Integer id=0;
        Cursor cursor = db.rawQuery("select case  when count(*) = 0 then 1 else max(cod_cliente) +1  end as d from clientes", null);
        if (cursor.moveToNext())
        {
            id = cursor.getInt(0);
        }

        Cursor cursor1 = db.rawQuery("select * from clientes where cod_cliente = " + id + " or razon_social = '" + clientes.razon_social.getText().toString() + "' or ruc = '" + clientes.ruc_ci.getText().toString() + "'", null);
        if (cursor1.moveToNext())
        {
            Toast.makeText(context, "ERROR, YA EXISTE EL CLIENTE.", Toast.LENGTH_LONG).show();
        }
        else
        {
            ContentValues values=new ContentValues();
            values.put("cod_cliente", id);
            values.put("razon_social",clientes.razon_social.getText().toString().toUpperCase());
            values.put("estado", "A");
            values.put("ruc", clientes.ruc_ci.getText().toString());
            Long idResultante=db.insert(Utilidades.TABLA_CLIENTES,null,values);
            Toast.makeText(context,"REGISTRO REALIZADO, NRO DE REGISTRO : "+idResultante,Toast.LENGTH_LONG).show();

            db.close();
            clientes.razon_social.setText("");
            clientes.ruc_ci.setText("");
            clientes.razon_social.requestFocus();
        }
    }
}

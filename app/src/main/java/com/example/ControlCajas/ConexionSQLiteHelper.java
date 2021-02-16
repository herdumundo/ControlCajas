package com.example.ControlCajas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Utilidades.Utilidades;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }




    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Utilidades.CREAR_TABLA_USUARIOS);
        db.execSQL(Utilidades.CREAR_TABLA_CLIENTES);
        db.execSQL(Utilidades.CREAR_TABLA_CAJAS);
        db.execSQL(Utilidades.CREAR_TABLA_REPARTIDORES);
        db.execSQL(Utilidades.CREAR_TABLA_MOVIMIENTOS);
        db.execSQL(Utilidades.CREAR_TABLA_DET_MOVIMIENTOS);
        db.execSQL(Utilidades.CREAR_TABLA_CABECERA_MAPLES);
        db.execSQL(Utilidades.CREAR_TABLA_DET_MOVIMIENTO_MAPLES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS repartidores");
        db.execSQL("DROP TABLE IF EXISTS cajas");
        db.execSQL("DROP TABLE IF EXISTS movimientos");
        db.execSQL("DROP TABLE IF EXISTS det_movimientos");
        db.execSQL("DROP TABLE IF EXISTS mov_maples");
        db.execSQL("DROP TABLE IF EXISTS det_mov_maples");


        onCreate(db);
    }




}





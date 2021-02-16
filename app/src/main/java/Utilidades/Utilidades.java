package Utilidades;

public class Utilidades {

    //Constantes campos tabla usuario
    public static final String TABLA_CLIENTES="clientes";
    public static final String TABLA_REPARTIDORES="repartidores";
    public static final String TABLA_CAJAS="cajas";
    public static final String TABLA_MOVIMIENTOS="movimientos";
    public static final String TABLA_DET_MOVIMIENTOS="det_movimientos";
    public static final String TABLA_USUARIOS="usuarios";
    //////////////////////////////////////////////////////

    public static final String CREAR_TABLA_CLIENTES="CREATE TABLE " +
            ""+TABLA_CLIENTES+" (cod_cliente INTEGER PRIMARY KEY, razon_social TEXT, ruc TEXT, estado TEXT)";

    public static final String CREAR_TABLA_REPARTIDORES="CREATE TABLE " + TABLA_REPARTIDORES+" (cod_repartidor INTEGER PRIMARY KEY, "
            +  "repartidor TEXT, estado TEXT )";

    public static final String CREAR_TABLA_CAJAS="CREATE TABLE " + TABLA_CAJAS +" (cod_caja INTEGER PRIMARY KEY, " +
            " cod_tipo INTEGER, cod_barra TEXT, estado TEXT  )";


    public static final String CREAR_TABLA_MOVIMIENTOS="CREATE TABLE " + TABLA_MOVIMIENTOS+" (nro_movimiento INTEGER PRIMARY KEY, " +
            "fecha TEXT, repartidor INTEGER, tipo_mov INTEGER, cod_cliente INTEGER, sucursal TEXT, ayudante INTEGER,estado TEXT )";

    public static final String CREAR_TABLA_CABECERA_MAPLES="CREATE TABLE mov_maples (nro_movimiento INTEGER PRIMARY KEY, " +
            "fecha TEXT, repartidor INTEGER, tipo_mov INTEGER, cod_cliente INTEGER, sucursal TEXT, ayudante INTEGER,estado TEXT )";

    public static final String CREAR_TABLA_DET_MOVIMIENTOS="CREATE TABLE " + TABLA_DET_MOVIMIENTOS+" (nro_movimiento INTEGER, " +
            "cod_caja INTEGER,estado TEXT )";

    public static final String CREAR_TABLA_DET_MOVIMIENTO_MAPLES="CREATE TABLE  det_mov_maples (nro_movimiento INTEGER, " +
            "tipo_m INTEGER, cantidad INTEGER,estado TEXT )";

    public static final String CREAR_TABLA_USUARIOS= "CREATE TABLE " + TABLA_USUARIOS + " (codUsuario INTEGER PRIMARY KEY, "+
            " usuario TEXT, password TEXT )";


}
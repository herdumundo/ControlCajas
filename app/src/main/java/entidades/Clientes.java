package entidades;

import java.io.Serializable;

public class Clientes implements Serializable
{
    private Integer cod_cliente;
    private String razon_social;

    public Clientes(Integer cod_cliente, String razon_social)
    {
        this.cod_cliente = cod_cliente;
        this.razon_social = razon_social;
    }

    public Clientes() {}

    public Integer getCod_cliente(){return cod_cliente;}
    public void setCod_cliente(Integer cod_cliente){this.cod_cliente = cod_cliente;}

    public String getRazon_social(){return razon_social;}
    public void setRazon_social(String razon_social){this.razon_social = razon_social;}


     public String toString() {
        return String.valueOf(razon_social);
    }
}

package entidades;

import java.io.Serializable;

public class Repartidores implements Serializable
{
    private Integer cod_repartidor;
    private String repartidor;

    public Repartidores(Integer cod_repartidor, String repartidor)
    {
        this.cod_repartidor = cod_repartidor;
        this.repartidor = repartidor;
    }

    public Repartidores() {}

    public Integer getCod_repartidor(){return cod_repartidor;}
    public void setCod_repartidor(Integer cod_repartidor) {this.cod_repartidor = cod_repartidor;}

    public String getRepartidor() {return repartidor;}
    public void setRepartidor(String repartidor){this.repartidor = repartidor;}

    public String toString() {
        return String.valueOf(repartidor);
    }

}

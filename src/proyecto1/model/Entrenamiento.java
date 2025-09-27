package proyecto1.model;

import java.time.LocalDate;

public class Entrenamiento {
    private final LocalDate fecha;
    private final String tipo;
    private final double valor;

    //NUEVA CONDICION SOBRE LA UBICACION
    private final boolean internacional;
    private final String pais; // AGREGUE UN BOOLEAN PARA SABER SI ES NACIONAL O INTERNACIONAL

    public Entrenamiento(LocalDate fecha, String tipo, double valor, boolean internacional, String pais) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
        this.internacional = internacional;
        this.pais = pais == null ? "" : pais;
    }

    public LocalDate getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public double getValor() { return valor; }
    public boolean isInternacional() { return internacional; }
    public String getPais() { return pais; }

    @Override
    public String toString() {
        String ubi = internacional ? ("Internacional: " + pais) : "Nacional";
        return fecha + " - " + tipo + ": " + valor + " (" + ubi + ")";
    }
}

package proyecto1.model;

import java.time.LocalDate;

public class Entrenamiento {
    private final LocalDate fecha;
    private final String tipo;
    private final double valor;
    // ACA VA LA PARTE NUEVA DE UBICACION
    private final boolean internacional; // FORMA MAS FACIL UN BOLEEAN
    private final String pais;

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

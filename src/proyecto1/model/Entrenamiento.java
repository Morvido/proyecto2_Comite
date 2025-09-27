package proyecto1.model;

import java.time.LocalDate;

public class Entrenamiento {
    private final LocalDate fecha;   // YYYY-MM-DD
    private final String tipo;       // ej. "Goles", "Tiempo (segundos)", etc.
    private final double valor;      // ej. número asociado
    // nuevo: ubicación
    private final boolean internacional; // false = nacional, true = internacional
    private final String pais; // país si internacional (vacío si nacional)

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

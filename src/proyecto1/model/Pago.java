package proyecto1.model;

import java.time.YearMonth;

// NUEVA CLASE QUE DICTA COMO SERA EL PAGO PARA LOS ATLETAS
//EN ESTA CLASE TODAVIA NO SE HACE EL AJUSTE PARA VERIFICAR EL PAGO, SOLO SE REGISTRAN
//SEGUN LOS ENTRENAMIENTOS
public class Pago {
    private final Atleta atleta;
    private final YearMonth mes;
    private final double monto;
    private final String descripcion;

    public Pago(Atleta atleta, YearMonth mes, double monto, String descripcion) {
        this.atleta = atleta;
        this.mes = mes;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public Atleta getAtleta() { return atleta; }
    public YearMonth getMes() { return mes; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return mes + " - " + atleta.getNombre() + " " + atleta.getApellido() + " : Q" + monto + " (" + descripcion + ")";
    }
}

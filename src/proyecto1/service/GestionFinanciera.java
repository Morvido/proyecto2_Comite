package proyecto1.service;

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;
import proyecto1.model.Pago;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestionFinanciera {
    private final RegistroService registroService;
    private final List<Pago> historialPagos;

    private static final double VALOR_ENTRENO_NACIONAL = 200.0; // Q200 por entrenamiento nacional
    private static final double VALOR_ENTRENO_INTERNACIONAL = 250.0; // adicional? spec says Q250 if in extranjero — interpretamos Q250 por entreno internacional
    private static final double BONO_SUPERAR_MEJOR = 300.0;

    public GestionFinanciera(RegistroService registroService) {
        this.registroService = registroService;
        this.historialPagos = new ArrayList<>();
    }

    // ACA PUSE MIS REGLAS PARA CALCULAR LOS PAGOS
    //Calcula pago mensual para un atleta en año/mes
    //Reglas:
    //Cada entrenamiento registrado en el mes = Q200 si nacional y Q250 si es internacional
    // si en el mes supera su mejor marca recibira una bonificación Q300 (solo una vez por mes)

    public double calcularPagoMensual(Atleta atleta, int year, int month) {
        List<Entrenamiento> entrenosMes = registroService.obtenerEntrenamientos(atleta).stream()
                .filter(e -> e.getFecha().getYear() == year && e.getFecha().getMonthValue() == month)
                .toList();
        double total = 0.0;
        for (Entrenamiento e : entrenosMes) {
            if (e.isInternacional()) total += VALOR_ENTRENO_INTERNACIONAL;
            else total += VALOR_ENTRENO_NACIONAL;
        }
        // Aca se hace el analisis para la bonificación del mes
        AnalisisService analisisService = new AnalisisService();
        // Si de todos los registros que se obtienen, se nota que se supera la marca, eso significa que es digno de bonificación
        List<Entrenamiento> todos = registroService.obtenerEntrenamientos(atleta);
        if (todos.isEmpty()) return total;
        Entrenamiento mejorHistorica = analisisService.mejorMarca(todos, atleta.getDisciplina());
        boolean otorgarBono = false;
        if (mejorHistorica != null) {
            for (Entrenamiento e : entrenosMes) {
                // Acá esta el analisis para ver como se puede dar la bonificacion
                String d = atleta.getDisciplina().toLowerCase();
                if (d.equals("carrera") || d.equals("natación") || d.equals("natacion") || d.equals("ciclismo")) {
                    if (e.getValor() < mejorHistorica.getValor()) { otorgarBono = true; break; }
                } else {
                    if (e.getValor() > mejorHistorica.getValor()) { otorgarBono = true; break; }
                }
            }
        }
        if (otorgarBono) total += BONO_SUPERAR_MEJOR;
        return total;
    }

    public Pago registrarPago(Atleta atleta, YearMonth mes, double monto, String descripcion) {
        Pago p = new Pago(atleta, mes, monto, descripcion);
        historialPagos.add(p);
        return p;
    }

    public List<Pago> getHistorialPagos() {
        return historialPagos;
    }

    // Aca paso el historial a CSV así se guarda en Excel y como pide el programa
    public void exportHistorialCSV(String archivo, CSVService csvService) {
        List<String> filas = new ArrayList<>();
        for (Pago p : historialPagos) filas.add(p.toString());
        csvService.exportReport(archivo, filas);
    }
}

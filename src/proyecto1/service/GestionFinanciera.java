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

    // MI CONDICION FUE LA SIGUIENTE PARA DETERMINAR LOS PAGOS
    // POR ENTRENAMIENTO NACIONAL SON 200
    // POR ENTRENO INTERNACIONAL SON 250
    //POR SUPERAR LA MEJOR MARCA SON 300

    private static final double VALOR_ENTRENO_NACIONAL = 200.0;
    private static final double VALOR_ENTRENO_INTERNACIONAL = 250.0;
    private static final double BONO_SUPERAR_MEJOR = 300.0;

    public GestionFinanciera(RegistroService registroService) {
        this.registroService = registroService;
        this.historialPagos = new ArrayList<>();
    }

    //ESTA ES LA PARTE DONDE SE CALCULA EL PAGO MENSUAL DEL ATLETA POR AÑO Y MES
    //OSEA QUE PARA REGISTRAR EL PAGO SE SOLICITA EL AÑO DEL PAGO Y LUEGO EL MES

    public double calcularPagoMensual(Atleta atleta, int year, int month) {
        List<Entrenamiento> entrenosMes = registroService.obtenerEntrenamientos(atleta).stream()
                .filter(e -> e.getFecha().getYear() == year && e.getFecha().getMonthValue() == month)
                .toList();
        double total = 0.0;
        for (Entrenamiento e : entrenosMes) {
            if (e.isInternacional()) total += VALOR_ENTRENO_INTERNACIONAL;
            else total += VALOR_ENTRENO_NACIONAL;
        }

        //ACA ESTA EL ANALISSIS DE LA BONIFICACION POR SUPERAR LA MARCA EN LOS ENTRENOS
        //SE TOMA LA MEJOR MARCA HISTORICA FUERA DEL MES (YA SEA EN NACIONAL E INTERNACIONA)
        //LUEGO SE TOMA LA DECISIÓN SI SE MERECE EL BONO

        AnalisisService analisisService = new AnalisisService();
        List<Entrenamiento> todos = registroService.obtenerEntrenamientos(atleta);
        if (todos.isEmpty()) return total;
        Entrenamiento mejorHistorica = analisisService.mejorMarca(todos, atleta.getDisciplina());
        boolean otorgarBono = false;
        if (mejorHistorica != null) {
            for (Entrenamiento e : entrenosMes) {
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

    public void exportHistorialCSV(String archivo, CSVService csvService) {
        List<String> filas = new ArrayList<>();
        for (Pago p : historialPagos) filas.add(p.toString());
        csvService.exportReport(archivo, filas);
    }
}

package proyecto1.service;

import proyecto1.model.Atleta;
import proyecto1.model.Entrenamiento;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class CSVService {

    private final DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

    // Guardar detalles (cada línea = entrenamiento)
    public void guardar(String archivo, Map<Atleta, List<Entrenamiento>> registros) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (Atleta atleta : registros.keySet()) {
                for (Entrenamiento e : registros.get(atleta)) {
                    writer.println(String.join(",",
                            atleta.getNombre(),
                            atleta.getApellido(),
                            String.valueOf(atleta.getEdad()),
                            atleta.getDisciplina(),
                            atleta.getNacionalidad(),
                            atleta.getDepartamento(),
                            atleta.getFechaIngreso().toString(),
                            e.getFecha().format(df),
                            e.getTipo(),
                            String.valueOf(e.getValor()),
                            e.isInternacional() ? "Internacional" : "Nacional",
                            e.getPais()
                    ));
                }
            }
        } catch (IOException ex) {
            System.out.println("ERRR AL GUARDAR EN CSV: " + ex.getMessage());
        }
    }

    // Export simple: resumen pagos o estadísticas (ejemplo generico)
    public void exportReport(String archivo, List<String> filas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (String f : filas) writer.println(f);
        } catch (IOException ex) {
            System.out.println("Error export report CSV: " + ex.getMessage());
        }
    }
}

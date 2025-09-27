package proyecto1.service;

import proyecto1.model.Entrenamiento;

import java.util.Comparator;
import java.util.List;

//AQUI SOLO SE HACE EL ANALISIS DE ENTRENAMIENTOS
//LAS MEDIDAS Y MARCAS QUE REALIZAN LOS ATLETAS
//DEPENDERA DE LA DISCIPLINA
public class AnalisisService {

    public double calcularPromedio(List<Entrenamiento> entrenamientos) {
        if (entrenamientos == null || entrenamientos.isEmpty()) return 0.0;
        double suma = 0;
        for (Entrenamiento e : entrenamientos) suma += e.getValor();
        return suma / entrenamientos.size();
    }

    public Entrenamiento mejorMarca(List<Entrenamiento> entrenamientos, String disciplina) {
        if (entrenamientos == null || entrenamientos.isEmpty()) return null;
        String d = disciplina.trim().toLowerCase();

        if (d.equals("carrera") || d.equals("natación") || d.equals("natacion") || d.equals("ciclismo")) {
            return entrenamientos.stream().min(Comparator.comparingDouble(Entrenamiento::getValor)).orElse(null);
        }
        return entrenamientos.stream().max(Comparator.comparingDouble(Entrenamiento::getValor)).orElse(null);
    }

    public List<Entrenamiento> evolucion(List<Entrenamiento> entrenamientos, String disciplina) {
        if (entrenamientos == null || entrenamientos.isEmpty()) return entrenamientos;
        String d = disciplina.trim().toLowerCase();

        if (d.equals("carrera") || d.equals("natación") || d.equals("natacion") || d.equals("ciclismo")) {
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor).reversed());
        } else if (d.equals("marcha")) {
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor));
        } else if (d.contains("pesas")) {
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor));
        } else {
            entrenamientos.sort(Comparator.comparingDouble(Entrenamiento::getValor));
        }
        return entrenamientos;
    }
}

package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Carrera extends Disciplina {

    public Carrera() {
        super("Carrera");
    }

    @Override
    public String getTipoMedida() {
        return "Tiempo (segundos)";
    }
}


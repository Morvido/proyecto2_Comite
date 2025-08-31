package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Marcha extends Disciplina {
    public Marcha() {
        super("Marcha");
    }

    @Override
    public String getTipoMedida() {
        return "Distancia (kilómetros)";
    }
}

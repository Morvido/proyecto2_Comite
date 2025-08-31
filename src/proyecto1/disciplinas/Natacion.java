package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Natacion extends Disciplina {
    public Natacion() {
        super("Natación");
    }

    @Override
    public String getTipoMedida() {
        return "Tiempo (segundos)";
    }
}

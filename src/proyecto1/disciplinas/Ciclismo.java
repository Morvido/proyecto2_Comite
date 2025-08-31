package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Ciclismo extends Disciplina {
    public Ciclismo() {
        super("Ciclismo");
    }

    @Override
    public String getTipoMedida() {
        return "Tiempo (segundos)";
    }
}

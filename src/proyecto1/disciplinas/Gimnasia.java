package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Gimnasia extends Disciplina {
    public Gimnasia() {
        super("Gimnasia");
    }

    @Override
    public String getTipoMedida() {
        return "Puntaje (0-10)";
    }
}

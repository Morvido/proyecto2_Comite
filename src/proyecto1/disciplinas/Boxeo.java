package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Boxeo extends Disciplina {
    public Boxeo() {
        super("Boxeo");
    }

    @Override
    public String getTipoMedida() {
        return "Rounds completados";
    }
}

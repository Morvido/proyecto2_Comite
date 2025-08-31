package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Judo extends Disciplina {
    public Judo() {
        super("Judo");
    }

    @Override
    public String getTipoMedida() {
        return "Puntaje obtenido";
    }
}

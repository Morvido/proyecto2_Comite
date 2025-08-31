package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class Futbol extends Disciplina {
    public Futbol() {
        super("Fútbol");
    }

    @Override
    public String getTipoMedida() {
        return "Goles marcados";
    }
}

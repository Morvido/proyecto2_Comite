package proyecto1.disciplinas;

import proyecto1.model.Disciplina;

public class LevantamientoPesas extends Disciplina {
    public LevantamientoPesas() {
        super("Levantamiento de Pesas");
    }

    @Override
    public String getTipoMedida() {
        return "Peso (kg)";
    }
}

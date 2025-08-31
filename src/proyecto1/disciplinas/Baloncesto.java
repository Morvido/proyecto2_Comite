package proyecto1.disciplinas;

import proyecto1.model.Disciplina;
//Acá solo son clases extendidas que se conectan con disciplina
//donde es la verdadera clase
//Esto esta hecho de esta forma para no tener que editar todo y agregar
//con facilidad más disciplinas
//además el usuario puede agregar disciplinas
//todas las disciplinas del package disciplinas son lo mismo.
public class Baloncesto extends Disciplina {
    public Baloncesto() {
        super("Baloncesto");
    }

    @Override
    public String getTipoMedida() {
        return "Puntos anotados";
    }
}

package proyecto1.model;

//ESta parte es crucial, ya que se pueden crear nuevas disciplinas sin afectar el resto del codigo
public abstract class Disciplina {
    private final String nombre;

    public Disciplina(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

   //Acá describimos el tio de medida que se va a usar, tipo tiempo, distancia, peso, etc.
    public abstract String getTipoMedida();
}

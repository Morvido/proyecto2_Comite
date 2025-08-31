package proyecto1.model;

import java.time.LocalDate;

//todos los atributos son inmutables por eso uso: "final"
public class Entrenamiento {
    private final LocalDate fecha; // Con este metodo va a pedir ingresar: YYYY-MM-DD
    private final String tipo;     // De momento lo coloco así, ya en el menú ya va a ser algo más entendible para el usuario
    //pero es la forma en la que se vera si es unentrenamiento de velocidad, fuerza, etc.
    private final double valor; //El dato que ingresa el usuario

    //constructor

    public Entrenamiento(LocalDate fecha, String tipo, double valor) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
    }
//Métodos Getters
    public LocalDate getFecha() { return fecha; }
    public String getTipo()     { return tipo; }
    public double getValor()    { return valor; }
//Método tostring
    @Override
    public String toString() {
        return fecha + " - " + tipo + ": " + valor;
    }
}

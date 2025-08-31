package proyecto1;

// Su unica funcion es iniciar la aplicacion
// cuando la inicio se ejecuta menu y lo llama para inciar

import proyecto1.ui.Menu;

public class App {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.iniciar();
    }
}

//Esta es una nueva forma que aprendi ya que ayuda a ser mas testeagble osea puedo hacer pruebas unitarias
//es extensible agregar nuevas features
//Y lo más improtantes es que es mantenible para cada componente haciendolo independiente

package pushpop;


public class Pushpop {

    public static void main(String[] args) {
        // TODO code application logic here
        Pila pila=new Pila();
        
        pila.push(6);
        pila.push(3);
        pila.push(9);
        
        System.out.println("Valor eliminado "+pila.pop());
        pila.push(5);
        System.out.println("Valor en la cima "+pila.peek());
        pila.printAllinStack();
    }
    //fofo gay
    //david y fofo mis papis
}

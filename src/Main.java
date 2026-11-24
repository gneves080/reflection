public class Main {

    public static void main(String[] args) {
        ListaReflection lista = new ListaReflection();

        lista.adicionar("Maria");
        lista.adicionar("João");
        lista.adicionar("Ana");

        System.out.println("Valores da lista (iterados via reflection):");
        lista.exibir();
    }
}
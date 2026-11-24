import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Classe que mantém uma lista criada, preenchida e percorrida somente por reflection:
 * não há "new ArrayList", "lista.add(...)" nem "for (x : lista)" no código.
 */
public class ListaReflection {

    private final List<String> nomes;

    public ListaReflection() {
        this.nomes = instanciarLista();
    }

    /** Passo 2: instancia a lista com Class.forName + Constructor.newInstance. */
    @SuppressWarnings("unchecked")
    private static List<String> instanciarLista() {
        try {
            Class<?> classe = Class.forName("java.util.ArrayList");
            Constructor<?> construtor = classe.getDeclaredConstructor();
            return (List<String>) construtor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Não foi possível instanciar a lista via reflection", e);
        }
    }

    /** Passo 3: invoca o método add(Object) da lista via Method.invoke. */
    public void adicionar(String valor) {
        try {
            Method add = nomes.getClass().getMethod("add", Object.class);
            add.invoke(nomes, valor);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Não foi possível adicionar o valor via reflection", e);
        }
    }

    /**
     * Passo 4: percorre a lista chamando iterator(), hasNext() e next() via reflection.
     *
     * Os métodos hasNext/next são buscados na interface Iterator (pública) e não na classe
     * concreta do iterador, que é interna e não acessível, o que causaria IllegalAccessException.
     */
    public void exibir() {
        try {
            Method iterator = nomes.getClass().getMethod("iterator");
            Method hasNext = Iterator.class.getMethod("hasNext");
            Method next = Iterator.class.getMethod("next");

            Object it = iterator.invoke(nomes);
            int posicao = 0;
            while ((Boolean) hasNext.invoke(it)) {
                Object valor = next.invoke(it);
                System.out.println("[" + posicao++ + "] " + valor);
            }
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("O método invocado lançou uma exceção", e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Não foi possível iterar a lista via reflection", e);
        }
    }
}
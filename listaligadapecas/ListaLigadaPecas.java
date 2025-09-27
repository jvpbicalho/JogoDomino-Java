package listaligadapecas;

import java.util.ArrayList;
import java.util.List;
import jogodomino.PecaDomino;

/**
 * Implementa uma lista duplamente ligada para representar a mesa de um jogo de dominó.
 * Permite adicionar peças nas duas extremidades (esquerda e direita) da corrente de dominós.
 */
public class ListaLigadaPecas {
    /** O primeiro nó (cabeça) da lista, representando a extremidade esquerda da mesa. */
    private NoPeca cabeca;
    /** O último nó (cauda) da lista, representando a extremidade direita da mesa. */
    private NoPeca cauda;

    /**
     * Adiciona uma peça de dominó na extremidade esquerda da mesa.
     *
     * @param peca A peça de dominó a ser adicionada.
     */
    public void adicionarNaEsquerda(PecaDomino peca) {
        NoPeca novo = new NoPeca(peca);
        if (cabeca == null) {
            cabeca = cauda = novo;
        } else {
            novo.setProximo(cabeca);
            cabeca.setAnterior(novo);
            cabeca = novo;
        }
    }

    /**
     * Adiciona uma peça de dominó na extremidade direita da mesa.
     *
     * @param peca A peça de dominó a ser adicionada.
     */
    public void adicionarNaDireita(PecaDomino peca) {
        NoPeca novo = new NoPeca(peca);
        if (cauda == null) {
            cabeca = cauda = novo;
        } else {
            novo.setAnterior(cauda);
            cauda.setProximo(novo);
            cauda = novo;
        }
    }

    /**
     * Retorna o valor numérico da extremidade esquerda da corrente de dominós.
     *
     * @return O valor do lado esquerdo da primeira peça, ou -1 se a mesa estiver vazia.
     */
    public int getValorEsquerdo() {
        if (cabeca != null) return cabeca.getPeca().getLadoEsquerdo();
        return -1;
    }

    /**
     * Retorna o valor numérico da extremidade direita da corrente de dominós.
     *
     * @return O valor do lado direito da última peça, ou -1 se a mesa estiver vazia.
     */
    public int getValorDireito() {
        if (cauda != null) return cauda.getPeca().getLadoDireito();
        return -1;
    }

    /**
     * Imprime uma representação textual das peças na mesa no console.
     * Útil para depuração.
     */
    public void imprimirMesa() {
        NoPeca atual = cabeca;
        while (atual != null) {
            System.out.print(atual.getPeca() + " ");
            atual = atual.getProximo();
        }
        System.out.println();
    }

    /**
     * Converte a lista de peças em uma lista de suas representações como String.
     *
     * @return Uma lista de Strings, onde cada string representa uma peça na mesa.
     */
    public java.util.List<String> toStringList() {
        java.util.List<String> lst = new java.util.ArrayList<>();
        NoPeca atual = cabeca;
        while (atual != null) {
            lst.add(atual.getPeca().toString());
            atual = atual.getProximo();
        }
        return lst;
    }

    /**
     * Retorna todas as peças da mesa como uma lista de objetos PecaDomino.
     * Este método é útil para renderizar as peças na interface gráfica.
     *
     * @return Uma {@code List<PecaDomino>} contendo todas as peças na ordem em que estão na mesa.
     */
    public List<PecaDomino> getPecas() {
        List<PecaDomino> lista = new ArrayList<>();
        NoPeca atual = cabeca;
        while (atual != null) {
            lista.add(atual.peca);
            atual = atual.proximo;
        }
        return lista;
    }

}
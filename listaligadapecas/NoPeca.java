package listaligadapecas;

import jogodomino.PecaDomino;

/**
 * Representa um nó em uma lista duplamente ligada de peças de dominó.
 * Cada nó contém a peça de dominó e referências para o nó anterior e o próximo na lista.
 */
public class NoPeca {
    /** A peça de dominó armazenada neste nó. */
    public PecaDomino peca;
    /** Referência para o próximo nó na lista. */
    public NoPeca proximo;
    /** Referência para o nó anterior na lista. */
    public NoPeca anterior;

    /**
     * Constrói um novo nó com a peça de dominó especificada.
     *
     * @param peca A peça de dominó a ser armazenada neste nó.
     */
    public NoPeca(PecaDomino peca) {
        this.peca = peca;
    }

    /**
     * Retorna a peça de dominó contida neste nó.
     *
     * @return A instância de PecaDomino.
     */
    public PecaDomino getPeca() {
        return peca;
    }

    /**
     * Retorna o próximo nó na lista.
     *
     * @return O próximo NoPeca, ou null se este for o último nó.
     */
    public NoPeca getProximo() {
        return proximo;
    }

    /**
     * Define o próximo nó na lista.
     *
     * @param proximo O nó a ser definido como o próximo.
     */
    public void setProximo(NoPeca proximo) {
        this.proximo = proximo;
    }

    /**
     * Retorna o nó anterior na lista.
     *
     * @return O NoPeca anterior, ou null se este for o primeiro nó.
     */
    public NoPeca getAnterior() {
        return anterior;
    }

    /**
     * Define o nó anterior na lista.
     *
     * @param anterior O nó a ser definido como o anterior.
     */
    public void setAnterior(NoPeca anterior) {
        this.anterior = anterior;
    }
}
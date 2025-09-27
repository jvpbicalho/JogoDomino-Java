package jogodomino;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um jogador no jogo de dominó.
 * Cada jogador tem um nome e uma "mão" de peças.
 */
public class Jogador {
    /** O nome do jogador. */
    private String nome;
    /** A lista de peças de dominó que o jogador possui. */
    private List<PecaDomino> mao;

    /**
     * Constrói um novo jogador com um nome e uma mão vazia.
     *
     * @param nome O nome do jogador.
     */
    public Jogador(String nome) {
        this.nome = nome;
        this.mao = new ArrayList<>();
    }

    /**
     * Retorna o nome do jogador.
     *
     * @return O nome do jogador.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a lista de peças na mão do jogador.
     *
     * @return Uma lista de {@code PecaDomino}.
     */
    public List<PecaDomino> getMao() {
        return mao;
    }

    /**
     * Adiciona uma peça à mão do jogador.
     *
     * @param peca A peça a ser adicionada.
     */
    public void adicionarPeca(PecaDomino peca) {
        mao.add(peca);
    }

    /**
     * Remove uma peça da mão do jogador.
     *
     * @param peca A peça a ser removida.
     */
    public void removerPeca(PecaDomino peca) {
        mao.remove(peca);
    }

    /**
     * Verifica se o jogador tem alguma peça que possa ser jogada,
     * dadas as extremidades livres da mesa.
     *
     * @param valorEsquerdo O valor na extremidade esquerda da mesa.
     * @param valorDireito  O valor na extremidade direita da mesa.
     * @return true se o jogador tiver uma jogada possível, false caso contrário.
     */
    public boolean podeJogar(int valorEsquerdo, int valorDireito) {
        for (PecaDomino p : mao) {
            if (p.podeConectar(valorEsquerdo) || p.podeConectar(valorDireito)) {
                return true;
            }
        }
        return false;
    }
}
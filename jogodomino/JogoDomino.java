package jogodomino;

import java.util.*;
import listaligadapecas.ListaLigadaPecas;

/**
 * Classe principal que gerencia a lógica e o fluxo de um jogo de dominó.
 * Controla os jogadores, a mesa, o monte de peças e os turnos.
 */
public class JogoDomino {
    /** A lista de jogadores participantes. */
    private List<Jogador> jogadores;
    /** A mesa do jogo, implementada como uma lista ligada de peças. */
    public ListaLigadaPecas mesa;
    /** O "monte", de onde os jogadores compram peças, implementado como uma pilha. */
    private Stack<PecaDomino> monte;
    /** O índice do jogador atual na lista de jogadores. */
    private int indiceJogadorAtual;

    /**
     * Constrói uma nova instância do jogo de dominó.
     * Inicializa o monte, distribui as peças e determina a primeira jogada.
     *
     * @param jogadores A lista de jogadores que participarão do jogo.
     */
    public JogoDomino(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        this.mesa = new ListaLigadaPecas();
        this.monte = new Stack<>();
        this.indiceJogadorAtual = 0;
        gerarMonte();
        distribuirPecas();
        iniciarPrimeiraJogada();
    }

    /**
     * Gera o conjunto padrão de 28 peças de dominó e as embaralha no monte.
     */
    private void gerarMonte() {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                monte.push(new PecaDomino(i, j));
            }
        }
        Collections.shuffle(monte);
    }

    /**
     * Distribui 7 peças do monte para cada jogador.
     */
    private void distribuirPecas() {
        for (Jogador j : jogadores) {
            for (int i = 0; i < 7; i++) {
                j.adicionarPeca(monte.pop());
            }
        }
    }

    /**
     * Tenta realizar uma jogada, colocando uma peça na mesa.
     * A peça é invertida se necessário para conectar corretamente.
     *
     * @param peca A peça a ser jogada.
     * @param naEsquerda true para jogar na extremidade esquerda, false para a direita.
     * @return true se a jogada foi bem-sucedida, false caso contrário.
     */
    public boolean realizarJogada(PecaDomino peca, boolean naEsquerda) {
        int valorReferencia = naEsquerda ? mesa.getValorEsquerdo() : mesa.getValorDireito();
        if (mesa.getValorEsquerdo() == -1 || peca.podeConectar(valorReferencia)) { // Se a mesa está vazia ou a peça conecta
            // Inverte a peça se a orientação estiver errada para o encaixe
            if ((peca.getLadoDireito() == valorReferencia && !naEsquerda) ||
                (peca.getLadoEsquerdo() == valorReferencia && naEsquerda)) {
                peca.inverter();
            }
            
            if (naEsquerda) {
                mesa.adicionarNaEsquerda(peca);
            } else {
                mesa.adicionarNaDireita(peca);
            }
            getJogadorAtual().removerPeca(peca);
            return true;
        }
        return false;
    }

    /**
     * Passa o turno para o próximo jogador na ordem cíclica.
     */
    public void passarTurno() {
        indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
    }

    /**
     * Retorna o jogador cujo turno está ativo.
     *
     * @return O jogador atual.
     */
    public Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    /**
     * Imprime o estado atual da mesa no console (para depuração).
     */
    public void imprimirMesa() {
        mesa.imprimirMesa();
    }

    /**
     * Identifica qual jogador tem a maior peça (carroça mais alta ou peça de maior soma)
     * e inicia o jogo com essa peça na mesa. O turno é então passado para o próximo jogador.
     */
    private void iniciarPrimeiraJogada() {
        PecaDomino maiorPeca = null;
        Jogador jogadorQueComeca = null;

        // Encontra a peça de maior soma na mão de todos os jogadores
        for (Jogador jogador : jogadores) {
            for (PecaDomino peca : jogador.getMao()) {
                if (maiorPeca == null || (peca.getLadoEsquerdo() + peca.getLadoDireito() >
                    maiorPeca.getLadoEsquerdo() + maiorPeca.getLadoDireito())) {
                    maiorPeca = peca;
                    jogadorQueComeca = jogador;
                }
            }
        }

        if (maiorPeca != null && jogadorQueComeca != null) {
            mesa.adicionarNaDireita(maiorPeca);
            jogadorQueComeca.removerPeca(maiorPeca);
            System.out.println("🔰 Primeira peça: " + maiorPeca + " jogada por " + jogadorQueComeca.getNome());
            
            // Define o índice para o próximo jogador
            int indice = jogadores.indexOf(jogadorQueComeca);
            this.indiceJogadorAtual = (indice + 1) % jogadores.size();
        }
    }

    /**
     * Permite que um jogador compre uma peça do monte, se não estiver vazio.
     *
     * @param jogador O jogador que está comprando a peça.
     * @return true se uma peça foi comprada, false se o monte estava vazio.
     */
    public boolean comprarPeca(Jogador jogador) {
        if (!monte.isEmpty()) {
            PecaDomino comprada = monte.pop();
            jogador.adicionarPeca(comprada);
            System.out.println(jogador.getNome() + " comprou a peça " + comprada);
            return true;
        } else {
            System.out.println("Monte vazio! Não há mais peças para comprar.");
            return false;
        }
    }

    /**
     * Retorna a instância da mesa do jogo.
     *
     * @return A {@code ListaLigadaPecas} que representa a mesa.
     */
     public ListaLigadaPecas getMesa() {
        return mesa;
    }
    /**
     * Retorna a lista de jogadores da partida.
     * @return A lista de jogadores.
     */
    public List<Jogador> getJogadores() {
        return jogadores;
    }
}
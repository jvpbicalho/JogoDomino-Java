package jogodomino;

/**
 * Representa uma única peça de dominó, com um lado esquerdo e um lado direito.
 * Cada lado tem um valor numérico.
 */
public class PecaDomino {
    /** O valor numérico do lado esquerdo da peça. */
    private int ladoEsquerdo;
    /** O valor numérico do lado direito da peça. */
    private int ladoDireito;

    /**
     * Constrói uma nova peça de dominó com os valores especificados.
     *
     * @param ladoEsquerdo O valor do lado esquerdo.
     * @param ladoDireito  O valor do lado direito.
     */
    public PecaDomino(int ladoEsquerdo, int ladoDireito) {
        this.ladoEsquerdo = ladoEsquerdo;
        this.ladoDireito = ladoDireito;
    }

    /**
     * Verifica se esta peça pode se conectar a um determinado valor na mesa.
     * Uma conexão é possível se um dos lados da peça for igual ao valor fornecido.
     *
     * @param valor O valor na extremidade da mesa para conectar.
     * @return true se a peça puder ser conectada, false caso contrário.
     */
    public boolean podeConectar(int valor) {
        return ladoEsquerdo == valor || ladoDireito == valor;
    }

    /**
     * Inverte os valores dos lados da peça.
     * O lado esquerdo se torna o direito e vice-versa.
     */
    public void inverter() {
        int temp = ladoEsquerdo;
        ladoEsquerdo = ladoDireito;
        ladoDireito = temp;
    }

    /**
     * Retorna o valor do lado esquerdo da peça.
     *
     * @return O valor do lado esquerdo.
     */
    public int getLadoEsquerdo() {
        return ladoEsquerdo;
    }

    /**
     * Retorna o valor do lado direito da peça.
     *
     * @return O valor do lado direito.
     */
    public int getLadoDireito() {
        return ladoDireito;
    }

    /**
     * Retorna uma representação em String da peça no formato "[X|Y]".
     *
     * @return A string formatada da peça.
     */
    @Override
    public String toString() {
        return "[" + ladoEsquerdo + "|" + ladoDireito + "]";
    }
}
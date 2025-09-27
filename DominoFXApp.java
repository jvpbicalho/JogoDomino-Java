import jogodomino.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.*;

/**
 * Classe principal da aplicação de jogo de dominó com interface gráfica JavaFX.
 * Gerencia a interface do usuário (UI), a lógica de interação do jogador e o fluxo do jogo.
 */
public class DominoFXApp extends Application {

    /** Flag para determinar se o modo de jogo é contra a CPU. */
    private boolean contraCPU = true;

    /** Instância do motor de jogo de dominó, que gerencia a lógica principal. */
    private JogoDomino jogo;
    /** Label que exibe o nome do jogador da vez. */
    private Label lblCurrent;
    /** Painel horizontal que exibe as peças de dominó na mesa. */
    private HBox mesaPane;
    /** Painel que exibe as peças na mão do jogador atual. */
    private FlowPane maoPane;
    /** Botão para jogar uma peça à esquerda da mesa. */
    private Button btnLeft;
    /** Botão para jogar uma peça à direita da mesa. */
    private Button btnRight;
    /** Botão para o jogador comprar uma peça do monte. */
    private Button btnBuy;
    /** Botão para o jogador passar a vez. */
    private Button btnPass;
    /** Botão para retornar ao menu principal. */
    private Button btnMenu;
    /** Grupo de botões de alternância para garantir que apenas uma peça da mão possa ser selecionada. */
    private final ToggleGroup grupoMao = new ToggleGroup();
    /** Flag para controlar se o jogador já comprou uma peça nesta rodada. */
    private boolean comprouEstaRodada = false;

    /**
     * Ponto de entrada da aplicação JavaFX.
     * Inicia a aplicação exibindo o menu inicial.
     *
     * @param stage O palco principal (janela) da aplicação.
     */
    @Override
    public void start(Stage stage) {
        mostrarMenuInicial(stage);
    }

    /**
     * Exibe o menu inicial onde o jogador pode escolher o modo de jogo.
     * As opções são Jogador vs Jogador ou Jogador vs CPU.
     *
     * @param stage O palco onde a cena do menu será exibida.
     */
    private void mostrarMenuInicial(Stage stage) {
        this.jogo = null; // Reseta o jogo ao voltar para o menu
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(30));
        menu.setStyle("-fx-alignment: center;");

        Label titulo = new Label("Jogo de Domino");
        titulo.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnPvP = new Button("Jogador vs Jogador");
        Button btnPvCPU = new Button("Jogador vs CPU");

        btnPvP.setOnAction(e -> iniciarJogo(stage, false));
        btnPvCPU.setOnAction(e -> iniciarJogo(stage, true));

        menu.getChildren().addAll(titulo, btnPvP, btnPvCPU);
        Scene menuScene = new Scene(menu, 400, 300);
        menuScene.getStylesheets().add(getClass().getResource("/estilo/estilo.css").toExternalForm());
        stage.setScene(menuScene);
        stage.setTitle("Domino - Menu");
        stage.show();
    }

    /**
     * Inicia o jogo de dominó, configurando o tabuleiro, os jogadores e a interface principal.
     *
     * @param stage O palco principal do jogo.
     * @param vsCPU true se o jogo for contra a CPU, false se for Jogador vs Jogador.
     */
    private void iniciarJogo(Stage stage, boolean vsCPU) {
        this.comprouEstaRodada = false;
        this.contraCPU = vsCPU;
        Jogador j1 = new Jogador("Você");
        Jogador j2 = new Jogador(vsCPU ? "CPU" : "Jogador 2");
        jogo = new JogoDomino(Arrays.asList(j1, j2));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        lblCurrent = new Label();
        lblCurrent.setFont(Font.font(18));
        root.setTop(lblCurrent);

        mesaPane = new HBox(5);
        mesaPane.setPadding(new Insets(10));
        mesaPane.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID,
                    new CornerRadii(5), BorderWidths.DEFAULT)));
        root.setCenter(mesaPane);

        VBox bottom = new VBox(8);
        maoPane = new FlowPane(5, 5);
        maoPane.setPadding(new Insets(5));
        bottom.getChildren().addAll(new Label("Sua mão:"), maoPane);

        HBox bar = new HBox(10);
        btnLeft  = new Button("Jogar à esquerda");
        btnRight = new Button("Jogar à direita");
        btnBuy   = new Button("Comprar");
        btnPass  = new Button("Passar");
        btnMenu = new Button("Menu");
        bar.getChildren().addAll(btnLeft, btnRight, btnBuy, btnPass, btnMenu);
        bottom.getChildren().add(bar);

        root.setBottom(bottom);

        configurarAcoes();
        atualizarUI();

        Scene scene = new Scene(root, 850, 600);
        scene.getStylesheets().add(getClass().getResource("/estilo/estilo.css").toExternalForm());
        stage.setTitle("Jogo de Dominó");
        stage.setScene(scene);
        stage.show();

        if (this.contraCPU && "CPU".equals(jogo.getJogadorAtual().getNome())) {
            turnoCPU();    // A CPU faz sua jogada
            atualizarUI(); // A UI é atualizada para refletir a jogada da CPU
        }
    }

    /**
     * Configura os manipuladores de eventos (event handlers) para os botões de ação do jogo.
     */
    private void configurarAcoes() {
        btnLeft.setOnAction(e -> tentarJogar(true));
        btnRight.setOnAction(e -> tentarJogar(false));

        btnBuy.setOnAction(e -> {
                    if (comprouEstaRodada) {
                        alert("Você só pode comprar uma peça por rodada.");
                        return;
                    }
                    if (jogo.comprarPeca(jogo.getJogadorAtual())) {
                        comprouEstaRodada = true;
                        btnBuy.setDisable(true); // Desabilita o botão após a compra na mesma rodada
                        atualizarUI();
                    } else {
                        alert("Monte vazio.");
                    }
            });

        btnPass.setOnAction(e -> {
                    jogo.passarTurno();
                    comprouEstaRodada = false; // Reseta a flag de compra ao passar
                    btnBuy.setDisable(false); // Habilita o botão de compra para o próximo jogador
                    turnoCPU();
                    atualizarUI();
            });

        btnMenu.setOnAction(e -> {
                    mostrarMenuInicial((Stage) btnMenu.getScene().getWindow());
            });
    }

    /**
     * Tenta realizar uma jogada com a peça selecionada pelo jogador.
     * Valida se uma peça foi selecionada e se a jogada é válida.
     * Se a jogada for bem-sucedida, verifica a condição de vitória e passa o turno.
     *
     * @param esquerda true para jogar à esquerda da mesa, false para jogar à direita.
     */
    private void tentarJogar(boolean esquerda) {
        ToggleButton sel = (ToggleButton) grupoMao.getSelectedToggle();
        if (sel == null) {
            alert("Selecione uma peça primeiro.");
            return;
        }
        PecaDomino p = (PecaDomino) sel.getUserData();

        boolean ok = jogo.realizarJogada(p, esquerda);
        if (!ok) {
            alert("Peça não encaixa nesse lado.");
            return;
        }

        if (jogo.getJogadorAtual().getMao().isEmpty()) {
            alert("Parabéns, você venceu!");
            System.exit(0); // Encerra a aplicação em caso de vitória
        }

        jogo.passarTurno();
        comprouEstaRodada = false;
        turnoCPU(); // Permite que a CPU jogue, se for a vez dela
        atualizarUI();
    }

    /**
     * Executa a lógica de jogada para o jogador CPU.
     * A CPU tenta jogar qualquer peça válida. Se não puder, tenta comprar uma.
     * Se ainda não puder jogar, passa a vez.
     */
    private void turnoCPU() {
        Jogador cpu = jogo.getJogadorAtual();
        if (!contraCPU || !"CPU".equals(cpu.getNome())) return;

        // Tenta jogar uma peça
        for (PecaDomino p : new ArrayList<>(cpu.getMao())) {
            if (jogo.realizarJogada(p, true) || jogo.realizarJogada(p, false)) {
                alert("CPU jogou " + p);
                if (cpu.getMao().isEmpty()) {
                    alert("CPU venceu! :(");
                    System.exit(0);
                }
                jogo.passarTurno();
                return;
            }
        }

        // Se não pôde jogar, tenta comprar
        if (jogo.comprarPeca(cpu)) {
            int e = jogo.getMesa().getValorEsquerdo();
            int d = jogo.getMesa().getValorDireito();
            alert("CPU Comprou");
            if (cpu.podeJogar(e, d)) {
                turnoCPU(); // Tenta jogar novamente com a nova peça
                return;
            }
        }

        // Se não pôde jogar nem comprar uma peça útil, passa a vez
        alert("CPU passou a vez.");
        jogo.passarTurno();
    }

    /**
     * Atualiza todos os componentes da interface gráfica para refletir o estado atual do jogo.
     * Isso inclui a mesa, a mão do jogador, o indicador de turno e o estado dos botões.
     */
    private void atualizarUI() {
        // --- Lógica para o jogador da vez e a mesa (permanece a mesma) ---
        Jogador jogadorDaVez = jogo.getJogadorAtual();
        lblCurrent.setText("Vez de: " + jogadorDaVez.getNome());

        mesaPane.getChildren().clear();
        for (PecaDomino p : jogo.getMesa().getPecas()) {
            ImageView img = getImagemPeca(p, false);
            mesaPane.getChildren().add(img);
        }

        maoPane.getChildren().clear();
        grupoMao.getToggles().clear();

        Jogador jogadorHumano = jogo.getJogadores().get(0); 
        for (PecaDomino p : jogadorHumano.getMao()) {
            ToggleButton tb = new ToggleButton();
            tb.setUserData(p);
            tb.setGraphic(getImagemPeca(p, true));
            tb.setToggleGroup(grupoMao);
            maoPane.getChildren().add(tb);
        }

        boolean turnoDoHumano = "Você".equals(jogadorDaVez.getNome()) || (!this.contraCPU);

        // Habilita ou desabilita os botões com base no turno
        btnLeft.setDisable(!turnoDoHumano);
        btnRight.setDisable(!turnoDoHumano);
        btnBuy.setDisable(!turnoDoHumano || comprouEstaRodada);
        btnPass.setDisable(!turnoDoHumano);
    }

    /**
     * Carrega e retorna a imagem de uma peça de dominó específica.
     * O método lida com peças com números invertidos (ex: 1-2 e 2-1) e aplica rotação opcional.
     *
     * @param peca A peça de dominó para a qual a imagem é necessária.
     * @param rotacionada Se true, a imagem da peça será rotacionada em 90 graus.
     * @return um ImageView contendo a imagem da peça.
     */
    private ImageView getImagemPeca(PecaDomino peca, boolean rotacionada) {
        int a = peca.getLadoEsquerdo();
        int b = peca.getLadoDireito();
        String nomeArquivo = String.format("/recursos/pecas/domino_%d_%d.png", a, b);
        java.net.URL recurso = getClass().getResource(nomeArquivo);

        // Tenta encontrar a imagem com os lados invertidos se a primeira tentativa falhar
        if (recurso == null) {
            nomeArquivo = String.format("/recursos/pecas/domino_%d_%d.png", b, a);
            recurso = getClass().getResource(nomeArquivo);
        }

        Image img = new Image(recurso.toString());
        ImageView iv = new ImageView(img);
        iv.setFitWidth(60);
        iv.setPreserveRatio(true);

        if (a > b) {
            iv.setScaleX(-1); // Inverte horizontalmente se a > b para manter a consistência
        }

        if (rotacionada) iv.setRotate(90);
        return iv;
    }

    /**
     * Exibe uma caixa de diálogo de alerta com uma mensagem para o usuário.
     *
     * @param msg A mensagem a ser exibida no alerta.
     */
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    /**
     * O método main, ponto de entrada para a execução do programa Java.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
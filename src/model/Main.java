package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        PokemonApiService api = new PokemonApiService();
        CapturaService capturaService = new CapturaService();
        Inventario mochila = new Inventario();
        Random random = new Random();
        Scanner leitor = new Scanner(System.in);
        List<Pokemon> pokedex = new ArrayList<>();
        String bolaSelecionada = "Comum";

        System.out.println("=== BEM-VINDO AO MUNDO POKÉMON! ===");

        while (true) {
            System.out.println("\n[1] Explorar [2] Pokedex/Professor [3] Sair");
            String menu = leitor.nextLine();

            // ==========================================
            // OPÇÃO 1: EXPLORAR E CAPTURAR
            // ==========================================
            if (menu.equals("1")) {
                int id = random.nextInt(1010) + 1;
                Pokemon selvagem = api.buscarPokemon(String.valueOf(id));

                boolean emCombate = true;
                while (emCombate && selvagem != null) {
                    System.out.println("\n====================================================");
                    System.out.println("   UM " + selvagem.getNome().toUpperCase() + " SELVAGEM APARECEU!");
                    System.out.println("====================================================");
                    System.out.println(" TIPO: " + selvagem.getTipo().toUpperCase());
                    System.out.println(" VIDA: " + selvagem.getVida() + " HP");
                    System.out.println(" RARIDADE: " + selvagem.getRaridade());
                    System.out.println(" BIOMA: " + selvagem.getBioma().toUpperCase());
                    System.out.println("----------------------------------------------------");
                    System.out.println(" DESCRIÇÃO: " + selvagem.getDescricao());
                    System.out.println("====================================================");
                    System.out.println(" BOLA SELECIONADA: " + bolaSelecionada + " (Restante: " + obterQtd(mochila, bolaSelecionada) + ")");
                    System.out.println("----------------------------------------------------");
                    System.out.println("[1] Lançar " + bolaSelecionada);
                    System.out.println("[2] Abrir Mochila (Trocar Bola)");
                    System.out.println("[3] Fugir");

                    String acao = leitor.nextLine();

                    if (acao.equals("1")) {
                        if (obterQtd(mochila, bolaSelecionada) > 0) {
                            mochila.usarBola(bolaSelecionada);
                            System.out.println("\nLançando " + bolaSelecionada + "... ⚾");

                            if (capturaService.tentarCapturar(selvagem, bolaSelecionada)) {
                                System.out.println("✨ SUCESSO! " + selvagem.getNome().toUpperCase() + " foi capturado!");
                                selvagem.setDataCaptura(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                                pokedex.add(selvagem);
                                emCombate = false;
                            } else {
                                System.out.println("❌ O Pokémon escapou da bola!");
                            }
                        } else {
                            System.out.println("\n⚠️ Você não tem " + bolaSelecionada + " suficiente!");
                        }
                    } else if (acao.equals("2")) {
                        System.out.println("\n--- MOCHILA DE BOLAS ---");
                        System.out.println("[1] Comum (" + mochila.getPokebolaComum() + ")");
                        System.out.println("[2] Great (" + mochila.getGreatBall() + ")");
                        System.out.println("[3] Ultra (" + mochila.getUltraBall() + ")");
                        System.out.println("[4] Master (" + mochila.getMasterBall() + ")");
                        System.out.println("Escolha qual deseja equipar:");

                        String troca = leitor.nextLine();
                        if (troca.equals("1")) bolaSelecionada = "Comum";
                        else if (troca.equals("2")) bolaSelecionada = "Great";
                        else if (troca.equals("3")) bolaSelecionada = "Ultra";
                        else if (troca.equals("4")) bolaSelecionada = "Master";

                        System.out.println("Você equipou a " + bolaSelecionada + "!");
                    } else if (acao.equals("3")) {
                        System.out.println("Você fugiu da batalha!");
                        emCombate = false;
                    }
                }
            }
            // ==========================================
            // OPÇÃO 2: POKEDEX E PROFESSOR
            // ==========================================
            else if (menu.equals("2")) {
                menuProfessor(pokedex, mochila, leitor, random);
            }
            // ==========================================
            // OPÇÃO 3: SAIR DO JOGO
            // ==========================================
            else if (menu.equals("3")) {
                System.out.println("Saindo do jogo... Você terminou com " + pokedex.size() + " Pokémon.");
                break; // Quebra o loop while(true) e encerra
            }
        } // Fim do while(true)
    } // Fim do public static void main


    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================
    private static int obterQtd(Inventario m, String tipo) {
        if (tipo.equals("Great")) return m.getGreatBall();
        if (tipo.equals("Ultra")) return m.getUltraBall();
        if (tipo.equals("Master")) return m.getMasterBall();
        return m.getPokebolaComum();
    }

    private static void menuProfessor(List<Pokemon> pokedex, Inventario mochila, Scanner leitor, Random random) {
        System.out.println("\n======= SUA POKEDEX (" + pokedex.size() + ") =======");
        if (pokedex.isEmpty()) {
            System.out.println("Sua Pokedex está vazia. Vá caçar alguns Pokémon!");
            return; // Já encerra aqui se não tiver nada
        } else {
            for (int i = 0; i < pokedex.size(); i++) {
                Pokemon p = pokedex.get(i);
                System.out.println((i + 1) + ". " + p.getNome().toUpperCase() + " | TIPO: " + p.getTipo() + " | RARIDADE: " + p.getRaridade());
            }
        }

        System.out.println("=========================================");
        System.out.println("[E] Enviar para o Professor Carvalho");
        System.out.println("[V] Voltar ao Menu Principal");
        String op = leitor.nextLine();

        if (op.equalsIgnoreCase("E")) {
            System.out.println("Digite os números dos Pokémon que deseja enviar separados por vírgula (ex: 1, 2, 5):");
            String entrada = leitor.nextLine();

            try {
                // O "split" corta o texto toda vez que achar uma vírgula ou espaço em branco
                String[] numerosDigitados = entrada.split("[,\\s]+");
                List<Pokemon> pokemonsParaEnviar = new ArrayList<>();

                // Passa por cada número digitado e separa os Pokémon
                for (String numStr : numerosDigitados) {
                    if (numStr.trim().isEmpty()) continue;

                    int idx = Integer.parseInt(numStr.trim()) - 1;

                    if (idx >= 0 && idx < pokedex.size()) {
                        Pokemon p = pokedex.get(idx);
                        // Evita que o jogador digite "1, 1, 1" e tente enviar o mesmo bicho 3 vezes
                        if (!pokemonsParaEnviar.contains(p)) {
                            pokemonsParaEnviar.add(p);
                        }
                    } else {
                        System.out.println("⚠️ O número " + (idx + 1) + " foi ignorado (não existe na lista).");
                    }
                }

                if (pokemonsParaEnviar.isEmpty()) {
                    System.out.println("❌ Nenhum Pokémon válido foi selecionado para envio.");
                    return;
                }

                System.out.println("Tem certeza que deseja enviar " + pokemonsParaEnviar.size() + " Pokémon ao laboratório? (S/N)");
                String confirmacao = leitor.nextLine();

                if (confirmacao.equalsIgnoreCase("S")) {
                    System.out.println("\nEnviando " + pokemonsParaEnviar.size() + " Pokémon ao laboratório...");

                    // Um loop que repete a recompensa para CADA Pokémon enviado
                    for (Pokemon p : pokemonsParaEnviar) {
                        pokedex.remove(p); // Tira da sua Pokedex

                        // Sorteio de Recompensa (1 a 100)
                        int sorte = random.nextInt(100) + 1;

                        System.out.print("Pelos dados de " + p.getNome().toUpperCase() + " -> ");

                        if (sorte == 1) {
                            mochila.adicionarBola("Master", 1);
                            System.out.println("🎁 1x MASTER BALL!");
                        } else if (sorte <= 6) {
                            mochila.adicionarBola("Ultra", 5);
                            System.out.println("🎁 5x ULTRA BALLS!");
                        } else if (sorte <= 16) {
                            mochila.adicionarBola("Great", 10);
                            System.out.println("🎁 10x GREAT BALLS!");
                        } else {
                            mochila.adicionarBola("Comum", 15);
                            System.out.println("🎁 15x POKÉBOLAS COMUNS!");
                        }
                    }
                    System.out.println("\nSua mochila foi atualizada!");
                } else {
                    System.out.println("Envio cancelado. Os Pokémon continuam com você.");
                }
            } catch (Exception e) {
                System.out.println("❌ Por favor, digite apenas números e vírgulas (ex: 1, 2, 3).");
            }
        }
    }
}

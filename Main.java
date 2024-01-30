import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        Scanner scanner = new Scanner(System.in);
        agenda.carregarContatosDoArquivo("src/dados/contatos.txt");

        int opcao;
        do {
            System.out.println("""
                    ##################
                    ##### AGENDA #####
                    ##################
                    """);
            System.out.println(">>>> Contatos <<<<");
            System.out.println("Id | Nome");
            for (Contato contato : agenda.getContatos()) {
                System.out.println(contato.getId() + " | " + contato.getNome() + " " + contato.getSobrenome());
            }
            System.out.println(">>>> Menu <<<<");
            System.out.println("1 - Adicionar Contato");
            System.out.println("2 - Remover Contato");
            System.out.println("3 - Editar Contato");
            System.out.println("4 - Exibir Detalhes do Contato");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Criar um novo contato:");
                    System.out.print("Nome: ");
                    String nomeNovoContato = scanner.nextLine();
                    System.out.print("Sobrenome: ");
                    String sobrenomeNovoContato = scanner.nextLine();

                    List<Telefone> telefonesNovoContato = new ArrayList<>();
                    boolean numeroTelefoneUnico = false;
                    do {
                        try {
                            System.out.print("DDD: ");
                            String dddTelefone = scanner.nextLine();
                            System.out.print("Número de telefone: ");
                            String numeroTelefoneString = scanner.nextLine();

                            Long numeroTelefone = Long.valueOf(numeroTelefoneString);

                            boolean numeroTelefoneJaExiste = agenda.getContatos().stream()
                                    .anyMatch(contato -> contato.getTelefones().stream()
                                            .anyMatch(telefone -> telefone.getNumero().equals(numeroTelefone)));

                            if (numeroTelefoneJaExiste) {
                                throw new IllegalArgumentException("Este número de telefone já está em uso por outro contato. Por favor, escolha outro número.");
                            }

                            Telefone novoTelefone = new Telefone(dddTelefone, numeroTelefone);
                            telefonesNovoContato.add(novoTelefone);

                            System.out.print("Deseja adicionar outro número de telefone? (S/N): ");
                            String opcao1 = scanner.nextLine();
                            if (opcao1.equalsIgnoreCase("N")) {
                                numeroTelefoneUnico = true;
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (!numeroTelefoneUnico);

                    int novoId = Contato.getProximoId();
                    agenda.criarContato(novoId, nomeNovoContato, sobrenomeNovoContato, telefonesNovoContato);

                    agenda.salvarContatosEmArquivo("src/dados/contatos.txt");
                    System.out.println("Novo contato criado com sucesso!");
                    break;


                case 2:
                    System.out.println("Remover um contato:");
                    System.out.print("Digite o ID do contato que deseja remover: ");

                    try {
                        int idContatoRemover = scanner.nextInt();
                        scanner.nextLine();


                        boolean contatoEncontrado = false;
                        for (Contato contato : agenda.getContatos()) {
                            if (contato.getId() == idContatoRemover) {
                                contatoEncontrado = true;
                                break;
                            }
                        }

                        if (contatoEncontrado) {

                            agenda.removerContato(idContatoRemover);

                            agenda.salvarContatosEmArquivo("src/dados/contatos.txt");
                            System.out.println("Contato removido com sucesso!");
                        } else {
                            System.out.println("Contato não encontrado com o ID fornecido.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Por favor, digite um ID válido.");
                        scanner.nextLine();
                    }
                    break;
                case 3:
                    try {

                        System.out.print("Digite o ID do contato que deseja editar: ");
                        int idContatoEditar = scanner.nextInt();
                        scanner.nextLine();


                        Contato contatoEditar = null;
                        for (Contato contato : agenda.getContatos()) {
                            if (contato.getId() == idContatoEditar) {
                                contatoEditar = contato;
                                break;
                            }
                        }

                        if (contatoEditar != null) {

                            System.out.println("Detalhes do Contato:");
                            System.out.println(contatoEditar.detalhesContato());


                            System.out.println("O que deseja editar?");
                            System.out.println("1 - Informações de Contato");
                            System.out.println("2 - Telefones");
                            System.out.print("Escolha uma opção: ");
                            int opcaoEditar = scanner.nextInt();
                            scanner.nextLine();

                            switch (opcaoEditar) {
                                case 1:
                                    System.out.print("Novo nome: ");
                                    String novoNome = scanner.nextLine().trim();
                                    System.out.print("Novo sobrenome: ");
                                    String novoSobrenome = scanner.nextLine().trim();


                                    agenda.atualizarContato(idContatoEditar, novoNome, novoSobrenome);
                                    break;

                                case 2:
                                    System.out.println("1 - Editar número existente");
                                    System.out.println("2 - Adicionar novo número");
                                    System.out.println("3 - Deletar número existente");
                                    System.out.print("Escolha uma opção: ");
                                    int opcaoEditarTelefones = scanner.nextInt();
                                    scanner.nextLine();

                                    try {
                                        switch (opcaoEditarTelefones) {
                                            case 1:


                                                contatoEditar = null;
                                                for (Contato contato : agenda.getContatos()) {
                                                    if (contato.getId() == idContatoEditar) {
                                                        contatoEditar = contato;
                                                        break;
                                                    }
                                                }

                                                if (contatoEditar != null) {
                                                    System.out.print("Digite o ID do telefone que deseja editar: ");
                                                    int idTelefoneEditar = scanner.nextInt();
                                                    scanner.nextLine();


                                                    Telefone telefoneEditar = null;
                                                    for (Telefone telefone : contatoEditar.getTelefones()) {
                                                        if (telefone.getId() == idTelefoneEditar) {
                                                            telefoneEditar = telefone;
                                                            break;
                                                        }
                                                    }

                                                    if (telefoneEditar != null) {
                                                        System.out.println("Editar número de telefone:");
                                                        System.out.print("Novo DDD (ou pressione Enter para manter o mesmo): ");
                                                        String novoDdd = scanner.nextLine().trim();
                                                        scanner.nextLine();

                                                        System.out.print("Novo número de telefone (ou pressione Enter para manter o mesmo): ");
                                                        String novoNumeroStr = scanner.nextLine().trim();
                                                        Long novoNumero = novoNumeroStr.isEmpty() ? telefoneEditar.getNumero() : Long.parseLong(novoNumeroStr);


                                                        telefoneEditar.setDdd(novoDdd.isEmpty() ? telefoneEditar.getDdd() : novoDdd);
                                                        telefoneEditar.setNumero(novoNumero);

                                                        System.out.println("Telefone editado com sucesso!");
                                                    } else {
                                                        System.out.println("Telefone não encontrado com o ID fornecido.");
                                                    }
                                                } else {
                                                    System.out.println("Contato não encontrado com o ID fornecido.");
                                                }
                                                break;
                                            case 2:
                                                System.out.println("Adicionar novo número de telefone:");


                                                System.out.print("DDD do novo telefone: ");
                                                String novoDdd = scanner.nextLine().trim();


                                                System.out.print("Número do novo telefone: ");
                                                Long novoNumero = Long.parseLong(scanner.nextLine().trim());


                                                Telefone novoTelefone = new Telefone(novoDdd, novoNumero);


                                                agenda.adicionarTelefoneAContato(idContatoEditar, novoTelefone);

                                                System.out.println("Novo número de telefone adicionado com sucesso!");
                                                break;
                                            case 3:
                                                System.out.println("Remover número de telefone existente:");


                                                System.out.print("Digite o ID do telefone que deseja remover: ");
                                                int idTelefoneRemover = scanner.nextInt();
                                                scanner.nextLine();


                                                Contato contatoRemoverTelefone = null;
                                                for (Contato contato : agenda.getContatos()) {
                                                    if (contato.getId() == idContatoEditar) {
                                                        contatoRemoverTelefone = contato;
                                                        break;
                                                    }
                                                }

                                                if (contatoRemoverTelefone != null) {

                                                    Telefone telefoneRemover = null;
                                                    for (Telefone telefone : contatoRemoverTelefone.getTelefones()) {
                                                        if (telefone.getId() == idTelefoneRemover) {
                                                            telefoneRemover = telefone;
                                                            break;
                                                        }
                                                    }

                                                    if (telefoneRemover != null) {

                                                        contatoRemoverTelefone.removerTelefone(telefoneRemover);
                                                        System.out.println("Número de telefone removido com sucesso!");
                                                    } else {
                                                        System.out.println("Telefone não encontrado com o ID fornecido.");
                                                    }
                                                } else {
                                                    System.out.println("Contato não encontrado com o ID fornecido.");
                                                }
                                                break;
                                            default:
                                                System.out.println("Opção inválida.");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Entrada inválida. Certifique-se de fornecer um número válido.");
                                        scanner.nextLine();
                                    }
                                    break;

                                default:
                                    System.out.println("Opção inválida.");
                            }


                            agenda.salvarContatosEmArquivo("src/dados/contatos.txt");
                        } else {
                            System.out.println("Contato não encontrado com o ID fornecido.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Por favor, digite um ID válido.");
                        scanner.nextLine();
                    }
                    break;


                case 4:
                    try {

                        System.out.print("Digite o ID do contato que deseja ler: ");
                        int idContatoLer = scanner.nextInt();
                        scanner.nextLine();


                        Contato contatoLer = null;
                        for (Contato contato : agenda.getContatos()) {
                            if (contato.getId() == idContatoLer) {
                                contatoLer = contato;
                                break;
                            }
                        }

                        if (contatoLer != null) {

                            System.out.println("Detalhes do Contato:");
                            System.out.println(contatoLer.detalhesContato());
                            scanner.nextLine();

                        } else {
                            System.out.println("Contato não encontrado com o ID fornecido.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada inválida. Por favor, digite um ID válido.");
                        scanner.nextLine();
                    }
                    break;

                case 5:
                    System.out.println("Saindo da aplicação...");
                    break;

                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }
        } while (opcao != 5);

        scanner.close();
    }
}

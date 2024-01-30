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

public class Contato {
    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;

    public Contato(Long id, String nome, String sobreNome, List<Telefone> telefones) {
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.telefones = telefones;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }
}

public class Telefone {
    private Long id;
    private String ddd;
    private Long numero;

    public Telefone(Long id, String ddd, Long numero) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public String getDdd() {
        return ddd;
    }

    public Long getNumero() {
        return numero;
    }
}

public class Agenda {
    private Map<Long, Contato> contatos;
    private Map<Long, Telefone> telefones;

    public Agenda() {
        contatos = new HashMap<>();
        telefones = new HashMap<>();
        carregarDados();
    }

    public void adicionarContato(Contato contato) {
        if (contatos.containsKey(contato.getId())) {
            System.out.println("Contato já existe.");
            return;
        }
        for (Telefone telefone : contato.getTelefones()) {
            if (telefones.containsKey(telefone.getId())) {
                System.out.println("Telefone já cadastrado.");
                return;
            }
        }
        contatos.put(contato.getId(), contato);
        for (Telefone telefone : contato.getTelefones()) {
            telefones.put(telefone.getId(), telefone);
        }
        salvarDados();
    }

    public void removerContato(Long id) {
        if (!contatos.containsKey(id)) {
            System.out.println("Contato não encontrado.");
            return;
        }
        Contato contato = contatos.get(id);
        contatos.remove(id);
        for (Telefone telefone : contato.getTelefones()) {
            telefones.remove(telefone.getId());
        }
        salvarDados();
    }

    public void editarContato(Long id, String nome, String sobreNome) {
        if (!contatos.containsKey(id)) {
            System.out.println("Contato não encontrado.");
            return;
        }
        Contato contato = contatos.get(id);
        contato.setNome(nome);
        contato.setSobreNome(sobreNome);
        salvarDados();
    }

    public void listarContatos() {
        System.out.println(">>>> Contatos <<<<");
        System.out.println("Id | Nome | SobreNome");
        for (Contato contato : contatos.values()) {
            System.out.println(contato.getId() + " | " + contato.getNome() + " | " + contato.getSobreNome());
        }
    }
}

package model;

public class Pokemon {
    private String nome;
    private String tipo;
    private int vida;
    private String raridade;
    private String bioma;
    private String descricao;
    private String dataCaptura; // NOVO: Campo para a data

    // Atualiza o construtor para incluir a raridade (que faltava mostrar)
    public Pokemon(String nome, String tipo, int vida, String raridade, String bioma, String descricao) {
        this.nome = nome;
        this.tipo = tipo;
        this.vida = vida;
        this.raridade = raridade;
        this.bioma = bioma;
        this.descricao = descricao;
        this.dataCaptura = "Ainda não capturado"; // Valor inicial
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public int getVida() { return vida; }
    public String getRaridade() { return raridade; }
    public String getBioma() { return bioma; }
    public String getDescricao() { return descricao; }

    public String getDataCaptura() { return dataCaptura; }
    public void setDataCaptura(String dataCaptura) { this.dataCaptura = dataCaptura; }
}
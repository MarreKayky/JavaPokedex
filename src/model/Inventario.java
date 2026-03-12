package model;

public class Inventario {
    private int pokebolaComum = 20; // Começa com 20
    private int greatBall = 0;
    private int ultraBall = 0;
    private int masterBall = 0;

    // Getters
    public int getPokebolaComum() { return pokebolaComum; }
    public int getGreatBall() { return greatBall; }
    public int getUltraBall() { return ultraBall; }
    public int getMasterBall() { return masterBall; }

    // ATUALIZADO: Agora pede o tipo E a quantidade
    public void adicionarBola(String tipo, int quantidade) {
        if (tipo.equals("Comum")) pokebolaComum += quantidade;
        else if (tipo.equals("Great")) greatBall += quantidade;
        else if (tipo.equals("Ultra")) ultraBall += quantidade;
        else if (tipo.equals("Master")) masterBall += quantidade;
    }

    public void usarBola(String tipo) {
        if (tipo.equals("Comum")) pokebolaComum--;
        else if (tipo.equals("Great")) greatBall--;
        else if (tipo.equals("Ultra")) ultraBall--;
        else if (tipo.equals("Master")) masterBall--;
    }
}
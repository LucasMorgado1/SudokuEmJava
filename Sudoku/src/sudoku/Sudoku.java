package sudoku;

/*
Entrega de trabalho
Eu,
Lucas Fernando Soares Morgado de Souza
declaro que
todas as respostas são fruto de meu próprio trabalho,
não copiei respostas de colegas externos à equipe,
não disponibilizei minhas respostas para colegas externos à equipe e
não realizei quaisquer outras atividades desonestas para me beneficiar ou prejudicar
outros.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author lucasmorgado
 *
 */
public class Sudoku {

    public static void main(String[] args) throws IOException {

        game();

    }

    public static void game() throws IOException {
        Scanner ler = new Scanner(System.in);

        int linha, coluna, decisao;

        boolean ativo = true;

        char tabuleiro[][] = initialize();
        char tabuleirotemp[][] = initialize();

        print(tabuleiro);//Print Início

        while (ativo) {
            
            char jogador = '_';

            System.out.println("O que desejas fazer ?\n 1 - Jogar\n "
                    + "2 - Remover\n");
            decisao = ler.nextInt();

            if (decisao == 1) {

                System.out.println("\n Prezado jogador, digite um numero em X\n");
                linha = ler.nextInt();

                System.out.println("\n Prezado jogador, digite um numero em Y\n");
                coluna = ler.nextInt();

                System.out.println("\n Prezado jogador, agora coloque o numero que"
                        + " desejas inserir.\n");
                jogador = ler.next().charAt(0);

                switch (step(tabuleiro, tabuleirotemp, linha, coluna, jogador, decisao)) {
                    case 1:
                        tabuleiro[linha][coluna] = jogador;
                        print(tabuleiro);
                        System.out.println("\nNumero colocado com sucesso!\n");
                        if (status(tabuleiro) == true) {
                            System.out.println("\nJogo Finalizado, Parabéns!!\n");
                            ativo = false;
                        }
                        break;
                    case 0:
                        print(tabuleiro);
                        System.out.println("\nNumero ja existente na linha, coluna ou "
                                + "quadrante!\n"); 
                        break;
                    case -1:
                        print(tabuleiro);
                        System.out.println("\nPosição inválida, tente novamente!\n");
                        break;
                }

            } else if (decisao == 2) {

                System.out.println("\n Prezado jogador, digite um numero em X\n");
                linha = ler.nextInt();
                System.out.println("\n Prezado jogador, digite um numero em Y\n");
                coluna = ler.nextInt();

                if (step(tabuleiro, tabuleirotemp, linha, coluna, jogador, decisao) == 1) {
                    tabuleiro[linha][coluna] = jogador;
                    print(tabuleiro);
                    System.out.println("Numero removido com sucesso!");
                } else {
                    print(tabuleiro);
                    System.out.println("Ação não permitida.");
                }
            }
        }
    }

    /*
            A função initialize() deverá fazer a leitura da grade armazenada em um 
        arquivo texto e devolver uma matriz 9x9 já com os valores iniciais. 
     */
    public static char[][] initialize() throws FileNotFoundException, IOException {
        char t[][] = new char[9][9];

        FileReader le = new FileReader("tabuleiro.txt");
        BufferedReader leBuffer = new BufferedReader(le);

        for (int i = 0; i < 9; i++) {
            String grade = leBuffer.readLine();
            String vetorString[] = grade.split(" ");
            for (int j = 0; j < vetorString.length; j++) {
                t[i][j] = vetorString[j].charAt(0);
            }
        }
        return t;
    }

    /*
        Essa função imprime o grade do Sudoku na tela em modo texto
     */
    public static void print(char t[][]) {
        System.out.print("  Y  ");
        /*Este <for> serve para que imprima os numeros horizontais para
            *o usuario nao se perder no tamanho da tabela
         */
        for (int k = 0; k < t.length; k++) {
            if (k == 3 || k == 6) {
                System.out.print(" "); //espaco para que o numero horizontal
                //nao fique em cima da barra vertical
            }
            System.out.print(k + " "); //parte que imprime os numeros 
            //com um espaco
        }
        System.out.println(); //pula a linha para começar a imprimir o tabuleiro
        System.out.println("X");
        for (int i = 0; i < t.length; i++) {
            System.out.print(i + " - |"); //imprime os numeros que estao na vertical
            for (int j = 0; j < t[0].length; j++) {
                System.out.print(t[i][j] + " "); //imprime todo o tabuleiro
                if (j == 2 || j == 5) {
                    System.out.print("|"); //imprime barras para separar o tabuleiro
                    //a cada 3 casas, igual um sudoku de papel
                }
            }
            System.out.println();
            //<If> para fazer a divisória a cada tres casas, separando horizontalmente
            if (i == 2 || i == 5) {
                System.out.println("    -------+------+-----");
            }
        }
    }

    /*
     Essa função recebe uma posição e um número que o usuário deseja 
    inserir na solução do Sudoku,caso a posição esteja ocupada ou inválida 
    ( linha coluna que não existe) a função deve retornar -1, se o valor 
    ser inserido já estiver presente na linha, coluna ou região a função retorna 
    0, e por fim, se for possível colocar o número na posição solicitada a função 
    retorna 1. Ao final a matriz atualizada é retornada. Essa função também é 
    responsável em limpar uma posição na matriz do Sudoku e retorna 1 caso tenha 
    sucesso, caso não consiga a função retorna -1.
     */
    public static int step(char t[][], char tabuleirotemp[][], int linha, int coluna, char jogador, int decisao) {

        char m3[][] = new char[3][3];

        int linhaTemp, colunaTemp;

        for (int i = 0; i < m3.length; i++) {
            for (int j = 0; j < m3[0].length; j++) {
                linhaTemp = Math.abs(((linha / 3) * 3)) + i;
                colunaTemp = Math.abs(((coluna / 3) * 3)) + j;
                m3[i][j] = t[linhaTemp][colunaTemp];
            }
        }

        if (decisao == 1) {

            if (t[linha][coluna] != '_' || linha < 0 || coluna < 0 || linha > t.length || coluna > t[0].length) {
                return -1;
            }
            for (int i = 0; i < t.length; i++) {
                //verifica se o numero que o jogador colocou tem na linha/coluna
                if (jogador == t[i][coluna] || jogador == t[linha][i] || quadrantes(m3, jogador) == true) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

        if (decisao == 2) {
            if (tabuleirotemp[linha][coluna] != '_') {
                return -1;
            } else {
                return 1;
            }
        }
        return -1;
    }

    /*
        A função status() verifica se o jogador já solucionou o 
        quebra-cabeça, ou seja, não existe posições vazias na matriz. Caso o 
        jogador tenha cumprido o objetivo do jogo a função retorna true, ou 
        false caso contrário
     */
    public static boolean status(char t[][]) {

        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                //nesse if, verificamos se o jogador já completou todos os espaços e se n tem numeros repetidos na linha/coluna
                if (t[i][j] == '_') {
                    return false;
                }
            }
        }
        //retorna falso caso o jogador n tenha preenchido os requisistos
        return true;
    }

    public static boolean quadrantes(char m3[][], char jogador) {

        for (int i = 0; i < m3.length; i++) {
            for (int j = 0; j < m3[0].length; j++) {
                if (jogador == m3[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}

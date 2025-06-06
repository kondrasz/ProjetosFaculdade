package aplicacao;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
/**
 * Sistema de Monitoramento de Encostas de Rodovias - RotaSegura
 * 
 * Grupo:
 * Arthur Macedo Gouvea - RM: 556499
 * Clara Jullia Kondrasovas Costa e Silva - RM: 556064
 * Juliana Barbosa Sandes - RM: 555605
 */

public class RotaSegura {
   public static Scanner le;
    // Vetor para armazenar os pontos de monitoramento
    private static Monitora[] pontos = new Monitora[20];
    // Contador de pontos no vetor
    private static int qtdPontos = 0;
    // Flag para indicar se o vetor foi alterado desde a última ordenação
    private static boolean vetorAlterado = true;

    static {
        le = new Scanner(System.in);
    }

    public RotaSegura() {
    }

    /*******************************************************************
	 * 
	 * TAREFA 1: Defina a classe Sensor que implemente o registro Monitora
	 * 
	 *******************************************************************/
	 public static class Monitora {
         private int hashCode;
         private String coordenadas;
         private double inclinacao;
         private double umidade;
         //construtor
         public Monitora(String coordenadas, int hashCode, double inclinacao, double umidade) {
             this.hashCode = hashCode;
             this.coordenadas = coordenadas;
             this.inclinacao = inclinacao;
             this.umidade = umidade;
         }
         // Métodos para acessar e modificar os atributos
         public String getCoordenadas() {
             return coordenadas;
         }
         public void setCoordenadas(String coordenadas) {
             this.coordenadas = coordenadas;
         }
         public int getHashCode() {
             return hashCode;
         }  
         public void setHashCode(int hashCode) {
             this.hashCode = hashCode;
         }
         public double getInclinacao() {
             return inclinacao;
         }
         public void setInclinacao(double inclinacao) {
             this.inclinacao = inclinacao;
         }
         public double getUmidade() {
             return umidade;
         }
         public void setUmidade(double umidade) {
             this.umidade = umidade;
         }
         // Método para exibir os dados formatados
         public String toString() {
             return "Coordenadas: " + coordenadas + 
                    "\nCodigo Hash: " + hashCode + 
                    "\nInclinacao: " + inclinacao + " mm/mes" + 
                    "\nUmidade: " + umidade + "%";
         }
    }

	/*******************************************************************
	 * 
	 * TAREFA 2: Implemente uma funcao que usando as coordenadas informadas gere o
	 * hash code de um ponto de monitoramento
	 * 
	 *******************************************************************/
	public static int gerarHashCode(String coordenadas) {
        // Verifica se as coordenadas possuem o tamanho correto
        if (coordenadas.length() < 22) {
            System.out.println("Coordenadas invalidas, deve conter 22 caracteres");
            return -1;
        }
        //Extrais os caracteres relevantes para o hash
        char c1 = coordenadas.charAt(6);
        char c2 = coordenadas.charAt(7);
        char c3 = coordenadas.charAt(16);
        char c4 = coordenadas.charAt(17);
        char c5 = coordenadas.charAt(20);
        char c6 = coordenadas.charAt(21);
        //Cria a string com os caracteres relevantes
        String hashString = "0" + c1 + c2 + c3 + c4 + c5 + c6;
        // Converte a string para um inteiro
        try {
            int hashCode = Integer.parseInt(hashString);
            return hashCode;
        } catch (NumberFormatException e) {
            System.out.println("Erro ao gerar hashCode: " + e.getMessage());
            return -1;
        }
    }

	/*******************************************************************
	 * 
	 * TAREFA 3: Altere a funcao geraVetor para que atraves da leitura de cada linha do arquivo 
	 * coordenadasCotas.txt obtenha as coordenadas onde cada ponto foi  instalado
	 *
	 * A função da Tarefa 2 deve ser chamada para gerar o codigo hash code de cada
	 * ponto e este codigo deve ser armazenado do atributo correspondente.
	 * 
	 *******************************************************************/
	public static int geraVetor() {
        String caminhoDoArquivo = "src/aplicacao/CoordenadasCotas.txt";
        int n = 0;

        try {
            File arquivo = new File(caminhoDoArquivo);

            for (Scanner leArq = new Scanner(arquivo); leArq.hasNextLine(); ++n) {
                String linha = leArq.nextLine();
                System.out.println(linha);

                //Gerar o hashCode a partir da linha lida
                int hashCode = gerarHashCode(linha);
                //Cria um novo ponto com as coordenadas e o hashCode,inicializando inclinacao e umidade com 0
                pontos[n] = new Monitora(linha, hashCode, 0, 0); // Inicializa com valores padrão
            }
            qtdPontos = n; // Atualiza a quantidade de pontos lidos
        } catch (FileNotFoundException var5) {
            System.out.println("Arquivo nao encontrado: " + var5.getMessage());
        }

        return n;
    }
	/*******************************************************************
	 * 
	 * TAREFA 4: Implemente uma funcao que percorre todo o vetor de registros
	 * Monitora lendo do arquivo medidas.txt as medidas dos parametros. O arquivo
	 * esta nesse projeto a 1a medida e' de inclinacao e deve ser convertida para
	 * double a 2a medida e' de umidade e deve ser convertida para int Cada medida
	 * depois de convertida deve ser armazenada no atributo correspondente
	 * 
	 *******************************************************************/
	 public static void lerMedidas() {
        String caminhoDoArquivo = "src/aplicacao/medidas.txt";
        try {
            File arquivo = new File(caminhoDoArquivo);
            Scanner leArq = new Scanner(arquivo);
            int i = 0;
            // Lê as linhas do arquivo até atingir a quantidade de pontos
            while (leArq.hasNextLine() && i < qtdPontos) {
                String linha = leArq.nextLine();
                // Divide a linha pelo separador ";"
                int posicaoSeparador = linha.indexOf(";");
                if (posicaoSeparador > 0) {
                    String parte1 = linha.substring(0, posicaoSeparador);
                    String parte2 = linha.substring(posicaoSeparador + 1);
                    //Converte as partes para os tipos apropriados
                    double inclinacao = Double.parseDouble(parte1);
                    double umidade = Double.parseDouble(parte2);
                    // Atualiza o ponto
                    pontos[i].setInclinacao(inclinacao);
                    pontos[i].setUmidade(umidade);
                    i++;
                }
            }
            leArq.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter medidas: " + e.getMessage());
        }
    }

	/*******************************************************************
	 * 
	 * TAREFA 5: Implemente um dos metodos de ordenacao para ordenar em ordem
	 * crescente de codigo hash. Obrigatoriamente adaptada de 1 dos que foram
	 * estudados no assunto10_Metodos_Ordenacao
	 * 
	 *******************************************************************/
public static void ordenarPorHash() {
       //Método de ordenação por inserção
        for (int i = 1; i < qtdPontos; i++) {
            Monitora temp = pontos[i];
            int j = i - 1;
            //Move os elementos maiores que o elemento atual para a direita
            while (j >= 0 && pontos[j].getHashCode() > temp.getHashCode()) {
                pontos[j + 1] = pontos[j];
                j--;
            }
            pontos[j + 1] = temp; //Inserindo o elemento na posição correta
        }
        vetorAlterado = false; // Indica que o vetor foi ordenado
    }
	/*******************************************************************
	 * 
	 * TAREFA 6: Implemente o metodo de busca binaria para pesquisar as medidas de 1
	 * sensor a partir do hash code. Obrigatoriamente adaptada na implementa��o
	 * estudada no assunto8_Metodos_Busca.
	 * 
	 *******************************************************************/
	 public static int buscarPorHash(int hashCode) {
        if (vetorAlterado) {
            ordenarPorHash(); // Ordena o vetor se ele foi alterado
        }
        int inicio = 0;
        int fim = qtdPontos - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            if (pontos[meio].getHashCode() == hashCode) {
                return meio; // Ponto encontrado
            } else if (pontos[meio].getHashCode() < hashCode) {
                inicio = meio + 1; // Busca na metade direita
            } else {
                fim = meio - 1; // Busca na metade esquerda
            }
        }
        return -1; // Ponto não encontrado
    }
    //Opcao 1
    public static void apresentarPontos() {
        System.out.println("Pontos de Monitoramento:");
        if (qtdPontos == 0) {
            System.out.println("Nenhum ponto cadastrado.");
            return;
        } else {
            for (int i = 0; i < qtdPontos; i++) {
                System.out.println(pontos[i]);
                System.out.println("-----------------------------");
            }
        }
    }
    // Opcao 2
    public static void inserirPonto(String coordenadas) {
        // Verifica se o vetor está cheio
        if (qtdPontos >= pontos.length) {
            System.out.println("Limite de pontos atingido. Não é possível inserir mais pontos.");
            return;
        }
        // Verifica se o ponto já existe
        for (int i = 0; i < qtdPontos; i++) {
            if (pontos[i].getCoordenadas().equals(coordenadas)) {
                System.out.println("Ponto já cadastrado.");
                return;
            }
        }
        // Gera o hashCode para as coordenadas
        int hashCode = gerarHashCode(coordenadas);
        if (hashCode == -1) {
            System.out.println("Erro ao gerar hashCode. Ponto não inserido.");
            return;
        }
        //Lê as medidas de inclinacao e umidade
        System.out.println("Informe a inclinacao (mm/mes)");
        double inclinacao = le.nextDouble();
        System.out.println("Informe a umidade (%)");
        double umidade = le.nextDouble();
        // Cria um novo ponto de monitoramento
        pontos[qtdPontos] = new Monitora(coordenadas, hashCode, inclinacao, umidade);
        qtdPontos++; // Incrementa a quantidade de pontos
        vetorAlterado = true; // Indica que o vetor foi alterado
        System.out.println("Ponto inserido com sucesso!");
    }
    // Opcao 3
    public static void atualizarMedidas() {
        if (qtdPontos == 0) {
            System.out.println("Nenhum ponto cadastrado para atualizar.");
            return;
        }
        System.out.println("Atualizando medidas de todos os pontos...");
        for (int i = 0; i < qtdPontos; i++) {
            System.out.println("Ponto " + (i + 1) + ": " + pontos[i].getCoordenadas());
            System.out.println("Informe a inclinacao (mm/mes)");
            double inclinacao = le.nextDouble();
            System.out.println("Informe a umidade (%)");
            double umidade = le.nextDouble();
            // Atualiza as medidas do ponto
            pontos[i].setInclinacao(inclinacao);
            pontos[i].setUmidade(umidade);
        }
        System.out.println("Medidas atualizadas com sucesso!");
    }
    //Opcao 4
    public static void pesquisarMedidas() {
        if (qtdPontos == 0) {
            System.out.println("Nenhum ponto cadastrado para pesquisar.");
            return;
        }
        System.out.print("Informe o hashCode do ponto a ser pesquisado: ");
        int hashCode = le.nextInt();
        // Busca o ponto pelo hashCode
        int index = buscarPorHash(hashCode);
        if (index == -1) {
            System.out.println("Ponto não encontrado.");
        } else {
            // Exibe as medidas do ponto encontrado
            System.out.println("Ponto encontrado: " + pontos[index]);
        }
    }
    // Opcao 5
    public static void apresentarPontosComAlerta() {
        if (qtdPontos == 0) {
            System.out.println("Nenhum ponto cadastrado.");
            return;
        }
        System.out.println("Pontos com alerta de risco:");
        boolean encontrouAlerta = false;
        //Verifica cada ponto para ver se está em alerta
        for (int i = 0; i < qtdPontos; i++) {
            // Define os critérios de alerta
            if (pontos[i].getInclinacao() > 10 && pontos[i].getUmidade() > 30) {
                System.out.println("\nPonto " + (i + 1) + ":");
                System.out.println(pontos[i].toString());
                System.out.println("ALERTA: Risco de deslizamento de terra!");
                System.out.println("-----------------------------");
                encontrouAlerta = true; // Indica que pelo menos um ponto com alerta foi encontrado
            }
        }
        if (!encontrouAlerta) {
            System.out.println("Nenhum ponto com alerta de risco encontrado.");
        }
    }
    
 // Opcao 6
    public static void apresentarPontosRiscoModerado() {
        if (qtdPontos == 0) {
            System.out.println("Nenhum ponto cadastrado.");
            return;
        }
        // Critérios de Risco Moderado 
        final double UMIDADE_MIN_MODERADO = 30.0;
        final double UMIDADE_MAX_MODERADO = 40.0; 
        final double INCLINACAO_MIN_MODERADO = 5.0;
        final double INCLINACAO_MAX_MODERADO = 20.0; 

        System.out.println("\nPontos com Risco Moderado (Umidade 30-40% OU Inclinacao 5-10 mm/mes):");
        boolean encontrouRiscoModerado = false;

        // Verifica cada ponto
        for (int i = 0; i < qtdPontos; i++) {
            boolean riscoUmidade = (pontos[i].getUmidade() >= UMIDADE_MIN_MODERADO && pontos[i].getUmidade() < UMIDADE_MAX_MODERADO);
            boolean riscoInclinacao = (pontos[i].getInclinacao() >= INCLINACAO_MIN_MODERADO && pontos[i].getInclinacao() < INCLINACAO_MAX_MODERADO);

            // Verifica se atende a pelo menos um dos critérios de risco moderado
            if (riscoUmidade || riscoInclinacao) {
                System.out.println("\nPonto " + (i + 1) + " (Hash: " + pontos[i].getHashCode() + "):");
                System.out.println(pontos[i].toString());
                System.out.print("RISCO MODERADO: ");
                if (riscoUmidade && riscoInclinacao) {
                    System.out.println("Umidade alta E Movimentação moderada.");
                } else if (riscoUmidade) {
                    System.out.println("Umidade alta (exige monitoramento contínuo)." + " [Umidade: " + pontos[i].getUmidade() + "%]");
                } else { // riscoInclinacao must be true
                    System.out.println("Movimentação moderada (atenção e possível intervenção)." + " [Inclinacao: " + pontos[i].getInclinacao() + " mm/mes]");
                }
                System.out.println("-----------------------------");
                encontrouRiscoModerado = true;
            }
        }

        if (!encontrouRiscoModerado) {
            System.out.println("Nenhum ponto com risco moderado (Umidade 30-40% ou Inclinacao 5-10 mm/mes) encontrado.");
        }
    }

	public static void main(String[] args) {

		final int N = 20;
		/*
		 * Declaracao de variaveis necessarias para aplicacao
		 */
		int n = geraVetor();
        lerMedidas();
        ordenarPorHash();

		// Criação do Menu
		int opcao;
		/*
		 * Chama a funcao geraVetor (Tarefa 3) que gera como resultado o vetor (com
		 * coordenadas e codigo hash de cada ponto de monitoramento atribuidos) e
		 * quantidade de pontos de monitoramento lidos do arquivo (n).
		 * 
		 * A chamada tem que ser alterada atualizar vetor de pontos de monitoramento
		 * declarado na funcao main()
		 */
	
		
		/*
		 * Chama a funcao para ler medidas de cada ponto (Tarefa 4).
		 */
		do {
            System.out.println("0 - Sair");
            System.out.println("1 - Apresentacao dos pontos de monitoramento");
            System.out.println("2 - Insere mais 1 ponto de monitoramento:");
            System.out.println("3 - Atualiza medidas de todos os pontos de monitoramento");
            System.out.println("4 - Pesquisa medidas de um ponto ");
            System.out.println("5 - Apresenta todos pontos com alerta de risco");
            System.out.println("6 - Apresenta pontos com RISCO MODERADO (Umidade 30-40% OU Inclinacao 5-10 mm/mes)");
            opcao = le.nextInt();
            switch (opcao) {
                case 0:
                    System.out.println("\n\n Fechando o sistema.... ");
                    System.out.println("Sistema fechado com sucesso!");
                    break;
                case 1:
                    apresentarPontos();
                    break;
                case 2:
                    System.out.print("Informe coordenadas e altitude exemplo (23o46'09\"S45o41'15\"W47m): ");
                    String var4 = le.next();
                    inserirPonto(var4);
                    break;
                case 3:
                    atualizarMedidas();
                    break;
                case 4:
                    pesquisarMedidas();
                    break;
                case 5:
                    System.out.println("Apresentando pontos com alerta de risco:");
                    apresentarPontosComAlerta();
                    break;
                case 6:
                	apresentarPontosRiscoModerado();
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        } while (opcao != 0);

        le.close();
    }
}

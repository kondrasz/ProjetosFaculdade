package aplicacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Ache {/* 
	 * IDENTIFICACAO DO GRUPO
	 * Juliana Barbosa Sandes - RM: 555605 Turma:2ECR
     *Arthur Macedo Gouvea - RM: 556499 Turma:2ECA
     *Nicolas Moreira Grippe - RM: 556880 Turma:2ECR
     *Lucas do Carmo Cima - RM: 564964 Turma:2ECR
     *Pedro Gonçalves Schulz - RM: 557936 Turma:2ECR
     *Clara Jullia Kondrasovas Costa e Silva - RM: 556064 Turma:2ECR
     */
	public static final int N = 30;

    public static void main(String[] args) {
		String diretorio = System.getProperty("user.dir");
		String caminhoDoArquivo = diretorio + "/src/arquivos/planilhaProducao.csv";
		Produto[] produtos = new Produto[N];
		try { 
            File arquivo = new File(caminhoDoArquivo);
            Scanner leArq = new Scanner(arquivo);
            int i = 0;
            while (leArq.hasNextLine() && i < N) {
                String linha = leArq.nextLine();
                String[] partes = linha.split(";");
                int codigo = Integer.parseInt(partes[0]);
                String fabrica = partes[1];
                int prodMax = Integer.parseInt(partes[2]);
                int mes1 = Integer.parseInt(partes[3]);
                int mes2 = Integer.parseInt(partes[4]);
                int mes3 = Integer.parseInt(partes[5]);
                // Cria um novo objeto Produto e o adiciona ao vetor
                produtos[i] = new Produto(codigo, fabrica, prodMax, mes1, mes2, mes3);
                i++;
            }
            leArq.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
            return;
        }
        Scanner le =new Scanner(System.in);

       calculaMediaPorcentagemProducao(produtos);
        for (Produto p : produtos) {
            if (p != null) {
                System.out.printf("Produto %d: Média = %.1f | Não Produzido = %.1f%%\n",
                        p.codigo, p.mediaProducao, p.percentualNaoProducao);
            }
        }
        int opcao = 0;
        do {
            System.out.println("0 - Encerrar");
			System.out.println("1 - Procura fabrica com maior capacidade de producao de um produto");
			System.out.println("2 - Procura produtos fabricados em uma especifica unidade");
			System.out.println("3 - Seleciona e apresenta produtos com percentual de nao producao acima de um limite");
			System.out.print("Opcao: ");
			opcao = le.nextInt();
			le.nextLine();
			switch (opcao) {
			case 0:
				System.out.println("Encerrado o programa.");
				break;
			case 1:
				System.out.print("Qual o codigo do produto a ser pesquisado? ");
				int codProcurado = le.nextInt();
					fabricaMaiorProducao(codProcurado,produtos);
				break;
			case 2:
				System.out.print("Nome da fabrica para pesquisar produtos:");
				String fab = le.nextLine();
				buscaPorFabrica(fab, produtos);
				break;
			case 3:
				System.out.print("Qual o valor limite de percentual nao produzido que se deseja pesquisar: ");
				double porcLimite = le.nextDouble();
				selecionaProdAbaixo(porcLimite, produtos);
				break;
			default:
				System.out.println("Opcao invalida");
			}
		} while (opcao != 0);

		le.close();
	}
	public static void calculaMediaPorcentagemProducao(Produto[] produtos) {
    for (Produto p : produtos) {
        if (p != null) {
            p.mediaProducao = (p.prodMes1 + p.prodMes2 + p.prodMes3) / 3.0;
            p.percentualNaoProducao = ((p.prodMaxima - p.mediaProducao) / p.prodMaxima) * 100;
        }
    }
}

    public static void fabricaMaiorProducao(int codProcurado, Produto[] produtos) {
    int maiorProd = -1;
    Produto escolhido = null;

    for (Produto p : produtos) {
        if (p != null && p.codigo == codProcurado && p.prodMaxima > maiorProd) {
            maiorProd = p.prodMaxima;
            escolhido = p;
        }
    }

    if (escolhido != null) {
        System.out.printf("Fábrica com maior capacidade: %s | Capacidade Máxima: %d\n",
                escolhido.fabrica, escolhido.prodMaxima);
    } else {
        System.out.println("Produto não encontrado.");
    }
}

   public static void buscaPorFabrica(String fab, Produto[] produtos) {
    boolean encontrado = false;
    for (Produto p : produtos) {
        if (p != null && p.fabrica.equalsIgnoreCase(fab)) {
            System.out.printf("Produto %d | Capacidade: %d | Meses: %d, %d, %d\n",
                    p.codigo, p.prodMaxima, p.prodMes1, p.prodMes2, p.prodMes3);
            encontrado = true;
        }
    }

    if (!encontrado) {
        System.out.println("Fábrica não encontrada.");
    }
}

    public static void selecionaProdAbaixo(double porcLimite, Produto[] produtos) {
    boolean encontrou = false;
    for (Produto p : produtos) {
        if (p != null && p.percentualNaoProducao > porcLimite) {
            System.out.printf("Produto %d | Fábrica: %s | Percentual Não Produzido: %.2f%%\n",
                    p.codigo, p.fabrica, p.percentualNaoProducao);
            encontrou = true;
        }
    }

    if (!encontrou) {
        System.out.println("Nenhum produto encontrado com percentual acima do limite.");
    }
    
    	
    }

}

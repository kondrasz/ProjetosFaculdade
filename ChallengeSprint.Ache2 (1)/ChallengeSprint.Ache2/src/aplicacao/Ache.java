package aplicacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Ache {/* 
	 * IDENTIFICACAO DO GRUPO
	 * Juliana Barbosa Sandes - RM: 555605R Turma:2ECR
     *Arthur Macedo Gouvea - RM: 556499 Turma:2ECA
     *Nicolas Moreira Grippe - RM: 556880 Turma:2ECR
     *Lucas do Carmo Cima - RM: 564964 Turma:2ECR
     *Pedro Gonçalves Schulz - RM: 557936 Turma:2ECR
     *Clara Jullia Kondrasovas Costa e Silva - RM: 556064 Turma:2ECR
     */
	public static final int N = 100;

    public static void main(String[] args) {
		String diretorio = System.getProperty("user.dir");
		String caminhoDoArquivo = diretorio + "/src/arquivos/planilhaProducao.csv";
		int totalProdutos=0;
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
                produtos[totalProdutos] = new Produto(codigo, fabrica, prodMax, mes1, mes2, mes3);
                totalProdutos++;
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
			System.out.println("4 – Insere mais um produto no vetor de produtos");
			System.out.println("5 – Classifica e apresenta os produtos com 10 piores porcentagens de produção");
			System.out.println("6 – Ordena e apresenta produtos em ordem alfabética das fábricas");
			System.out.println("7 - Busca sequencial por código do produto");
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
			case 4:
			    if (totalProdutos < N) {
			        produtos[totalProdutos] = inserirProduto(le);
			        calculaMediaPorcentagemProducao(produtos);
			        totalProdutos++;
			    } else {
			        System.out.println("Limite máximo de produtos atingido.");
			    }
			    break;

			case 5:
			    ordenarPorPercentualNaoProducao(produtos, totalProdutos);
			    System.out.println("10 produtos com pior percentual de produção:");
			    for (int i = 0; i < 10 && i < totalProdutos; i++) {
			        Produto p = produtos[i];
			        System.out.printf("Produto %d | Fábrica: %s | Percentual Não Produzido: %.2f%%\n",
			                p.codigo, p.fabrica, p.percentualNaoProducao);
			    }
			    break;

			case 6:
			    ordenarPorNomeFabrica(produtos, totalProdutos);
			    System.out.println("Produtos ordenados por nome da fábrica:");
			    for (int i = 0; i < totalProdutos; i++) {
			        Produto p = produtos[i];
			        System.out.printf("Produto %d | Fábrica: %s | Média: %.2f | Não Produzido: %.2f%%\n",
			                p.codigo, p.fabrica, p.mediaProducao, p.percentualNaoProducao);
			    }
			    break;
			case 7:
			    System.out.print("Informe o código do produto para buscar: ");
			    int codigoBusca = le.nextInt();
			    buscaSequencialPorCodigo(codigoBusca, produtos);
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
    public static Produto inserirProduto(Scanner le) {
        System.out.print("Código do produto: ");
        int codigo = le.nextInt();
        le.nextLine(); // limpar buffer

        System.out.print("Nome da fábrica: ");
        String fabrica = le.nextLine();

        System.out.print("Produção máxima: ");
        int prodMax = le.nextInt();

        System.out.print("Produção mês 1: ");
        int mes1 = le.nextInt();
        System.out.print("Produção mês 2: ");
        int mes2 = le.nextInt();
        System.out.print("Produção mês 3: ");
        int mes3 = le.nextInt();

        return new Produto(codigo, fabrica, prodMax, mes1, mes2, mes3);
    }
    public static void ordenarPorPercentualNaoProducao(Produto[] produtos, int total) {
        quickSort(produtos, 0, total - 1);
    }

    public static void quickSort(Produto[] produtos, int inicio, int fim) {
        if (inicio < fim) {
            int p = particionar(produtos, inicio, fim);
            quickSort(produtos, inicio, p - 1);
            quickSort(produtos, p + 1, fim);
        }
    }

    public static int particionar(Produto[] produtos, int inicio, int fim) {
        double pivo = produtos[fim].percentualNaoProducao;
        int i = inicio - 1;

        for (int j = inicio; j < fim; j++) {
            if (produtos[j].percentualNaoProducao >= pivo) {
                i++;
                Produto temp = produtos[i];
                produtos[i] = produtos[j];
                produtos[j] = temp;
            }
        }

        Produto temp = produtos[i + 1];
        produtos[i + 1] = produtos[fim];
        produtos[fim] = temp;

        return i + 1;
    }

    public static void ordenarPorNomeFabrica(Produto[] produtos, int total) {
        for (int i = 1; i < total; i++) {
            Produto atual = produtos[i];
            int j = i - 1;
            while (j >= 0 && produtos[j].fabrica.compareToIgnoreCase(atual.fabrica) > 0) {
                produtos[j + 1] = produtos[j];
                j--;
            }
            produtos[j + 1] = atual;
        }
    }
    public static void buscaSequencialPorCodigo(int codProcurado, Produto[] produtos) {
        boolean encontrado = false;

        for (Produto p : produtos) {
            if (p != null && p.codigo == codProcurado) {
                System.out.printf("Produto encontrado: Código: %d | Fábrica: %s | Capacidade: %d | Meses: %d, %d, %d\n",
                        p.codigo, p.fabrica, p.prodMaxima, p.prodMes1, p.prodMes2, p.prodMes3);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Produto com o código " + codProcurado + " não foi encontrado.");
        }
    }

}

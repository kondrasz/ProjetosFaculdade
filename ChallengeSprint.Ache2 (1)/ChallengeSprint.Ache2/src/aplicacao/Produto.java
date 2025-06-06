package aplicacao;

public class Produto {
	public static final int N=100;
	int codigo;
	String fabrica;
	int prodMaxima;
	int prodMes1;
	int prodMes2;
	int prodMes3;
	double mediaProducao;
	double percentualNaoProducao;
	
	public Produto (int codigo, String fabrica, int prodMaxima, int prodMes1, int prodMes2,int prodMes3) {
		this.codigo = codigo;
		this.fabrica = fabrica;
		this.prodMaxima = prodMaxima;
		this.prodMes1 = prodMes1;
		this.prodMes2 = prodMes2;
		this.prodMes3 = prodMes3;
		this.mediaProducao = 0;
		this.percentualNaoProducao = 0;
	}

	
}

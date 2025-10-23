package ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;

public class CompraServiceTest
{
	@Test
	public void calcularCustoTotal()
	{
		CompraService service = new CompraService(null, null, null, null);

		// Criando um cliente
		Cliente cliente = new Cliente(1L, "João Silva", Regiao.NORDESTE, TipoCliente.OURO);

		CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
		carrinho.setCliente(cliente);

		List<ItemCompra> itens = new ArrayList<>();

		// Criando produtos com valores específicos
		Produto produto1 = new Produto(
			1L,
			"Notebook",
			"Notebook Dell",
			new BigDecimal("2000.00"),  // preço
			new BigDecimal("2.5"),       // peso físico (kg)
			new BigDecimal("35"),        // comprimento (cm)
			new BigDecimal("25"),        // largura (cm)
			new BigDecimal("3"),         // altura (cm)
			false,                        // não é frágil
			TipoProduto.ELETRONICO
		);

		Produto produto2 = new Produto(
			2L,
			"Mouse",
			"Mouse Logitech",
			new BigDecimal("50.00"),     // preço
			new BigDecimal("0.2"),       // peso físico (kg)
			new BigDecimal("10"),        // comprimento (cm)
			new BigDecimal("6"),         // largura (cm)
			new BigDecimal("4"),         // altura (cm)
			false,                        // não é frágil
			TipoProduto.ELETRONICO
		);

		Produto produto3 = new Produto(
			3L,
			"Teclado",
			"Teclado Mecânico",
			new BigDecimal("150.00"),    // preço
			new BigDecimal("0.8"),       // peso físico (kg)
			new BigDecimal("45"),        // comprimento (cm)
			new BigDecimal("15"),        // largura (cm)
			new BigDecimal("4"),         // altura (cm)
			false,                        // não é frágil
			TipoProduto.ELETRONICO
		);

		// Configurando os itens de compra
		ItemCompra item1 = new ItemCompra();
		item1.setProduto(produto1);
		item1.setQuantidade(1L);

		ItemCompra item2 = new ItemCompra();
		item2.setProduto(produto2);
		item2.setQuantidade(2L);

		ItemCompra item3 = new ItemCompra();
		item3.setProduto(produto3);
		item3.setQuantidade(1L);

		itens.add(item1);
		itens.add(item2);
		itens.add(item3);
		carrinho.setItens(itens);

		BigDecimal custoTotal = service.calcularCustoTotal(carrinho, Regiao.NORDESTE, TipoCliente.OURO);

		// Cálculo esperado:
		// Subtotal: 2000 + (50*2) + 150 = 2250
		// Desconto múltiplos itens (4 eletrônicos, 3-4 = 5%): 2250 * 0.05 = 112.50
		// Subtotal com desconto: 2250 - 112.50 = 2137.50
		// Desconto valor carrinho (> 1000, 20%): 2137.50 * 0.20 = 427.50
		// Subtotal final: 2137.50 - 427.50 = 1710.00
		// Peso total: 2.5 + (0.2*2) + 0.8 = 3.9 kg (< 5kg, frete = 0)
		// Taxa frágil: 0
		// Frete por peso: 0
		// Frete com região NORDESTE (1.10): 0
		// Desconto cliente OURO (100%): 0
		// Total: 1710.00

		// Ao trabalhar com BigDecimal, evite comparar com equals() -- método que o
		// assertEquals usa,
		// pois ela leva em conta escala (ex: 10.0 != 10.00).
		// Use o método compareTo().
		BigDecimal esperado = new BigDecimal("1710.00");
		assertEquals(0, custoTotal.compareTo(esperado), "Valor calculado incorreto: " + custoTotal);

		// Uma alternativa mais elegante, é usar a lib AssertJ
		// O método isEqualByComparingTo não leva em conta escala
		// e não precisa instanciar um BigDecimal para fazer a comparação
		assertThat(custoTotal).as("Custo Total da Compra").isEqualByComparingTo("1710.00");
	}
}

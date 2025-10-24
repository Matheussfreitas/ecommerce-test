package ecommerce.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.dto.CompraDTO;
import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.dto.PagamentoDTO;
import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import jakarta.transaction.Transactional;

@Service
public class CompraService {

	private final CarrinhoDeComprasService carrinhoService;
	private final ClienteService clienteService;

	private final IEstoqueExternal estoqueExternal;
	private final IPagamentoExternal pagamentoExternal;

	@Autowired
	public CompraService(CarrinhoDeComprasService carrinhoService, ClienteService clienteService,
			IEstoqueExternal estoqueExternal, IPagamentoExternal pagamentoExternal) {
		this.carrinhoService = carrinhoService;
		this.clienteService = clienteService;

		this.estoqueExternal = estoqueExternal;
		this.pagamentoExternal = pagamentoExternal;
	}

	@Transactional
	public CompraDTO finalizarCompra(Long carrinhoId, Long clienteId) {
		Cliente cliente = clienteService.buscarPorId(clienteId);
		CarrinhoDeCompras carrinho = carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		List<Long> produtosIds = carrinho.getItens().stream().map(i -> i.getProduto().getId())
				.collect(Collectors.toList());
		List<Long> produtosQtds = carrinho.getItens().stream().map(i -> i.getQuantidade()).collect(Collectors.toList());

		DisponibilidadeDTO disponibilidade = estoqueExternal.verificarDisponibilidade(produtosIds, produtosQtds);

		if (!disponibilidade.disponivel()) {
			throw new IllegalStateException("Itens fora de estoque.");
		}

		BigDecimal custoTotal = calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

		PagamentoDTO pagamento = pagamentoExternal.autorizarPagamento(cliente.getId(), custoTotal.doubleValue());

		if (!pagamento.autorizado()) {
			throw new IllegalStateException("Pagamento não autorizado.");
		}

		EstoqueBaixaDTO baixaDTO = estoqueExternal.darBaixa(produtosIds, produtosQtds);

		if (!baixaDTO.sucesso()) {
			pagamentoExternal.cancelarPagamento(cliente.getId(), pagamento.transacaoId());
			throw new IllegalStateException("Erro ao dar baixa no estoque.");
		}

		CompraDTO compraDTO = new CompraDTO(true, pagamento.transacaoId(), "Compra finalizada com sucesso.");

		return compraDTO;
	}

	public BigDecimal calcularCustoTotal(CarrinhoDeCompras carrinho, Regiao regiao, TipoCliente tipoCliente) {
		// Calcula o subtotal dos itens
		BigDecimal subtotalItens = carrinho.getItens().stream()
				.map(item -> item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Calcula o desconto por múltiplos itens do mesmo tipo
		BigDecimal descontoMultiplosItens = calcularDescontoPorMultiplosItensMesmoTipo(carrinho);

		// Aplica o desconto ao subtotal
		BigDecimal subtotalComDescontoMultiplosItens = subtotalItens.subtract(descontoMultiplosItens);

		BigDecimal descontoValorCarrinho = calcularDescontoPorValorCarrinho(subtotalComDescontoMultiplosItens);

		BigDecimal subtotalComDescontoFinal = subtotalComDescontoMultiplosItens.subtract(descontoValorCarrinho);

		// Calcula o frete total
		BigDecimal freteTotal = calcularFreteTotal(carrinho);

		// Calcula o custo total (subtotal com desconto + frete)
		BigDecimal custoTotal = subtotalComDescontoFinal.add(freteTotal);

		// Retorna o custo total arredondado para 2 casas decimais
		return custoTotal.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal calcularDescontoPorMultiplosItensMesmoTipo(CarrinhoDeCompras carrinho) {
		// Agrupa os itens por tipo de produto e soma as quantidades
		Map<TipoProduto, Long> quantidadePorTipo = carrinho.getItens().stream()
				.collect(Collectors.groupingBy(
						item -> item.getProduto().getTipo(),
						Collectors.summingLong(ItemCompra::getQuantidade)));

		BigDecimal descontoTotal = BigDecimal.ZERO;

		// Para cada tipo de produto, calcula o desconto baseado na quantidade total
		for (Map.Entry<TipoProduto, Long> entrada : quantidadePorTipo.entrySet()) {
			TipoProduto tipo = entrada.getKey();
			Long quantidadeTotal = entrada.getValue();

			// Determina a porcentagem de desconto baseado na quantidade
			BigDecimal percentualDesconto = BigDecimal.ZERO;
			if (quantidadeTotal >= 8) {
				percentualDesconto = BigDecimal.valueOf(0.15); // 15%
			} else if (quantidadeTotal >= 5) {
				percentualDesconto = BigDecimal.valueOf(0.10); // 10%
			} else if (quantidadeTotal >= 3) {
				percentualDesconto = BigDecimal.valueOf(0.05); // 5%
			}

			// Calcula o valor do desconto para esse tipo de produto
			if (percentualDesconto.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal subtotalTipo = carrinho.getItens().stream()
						.filter(item -> item.getProduto().getTipo() == tipo)
						.map(item -> item.getProduto().getPreco()
								.multiply(BigDecimal.valueOf(item.getQuantidade())))
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				BigDecimal descontoTipo = subtotalTipo.multiply(percentualDesconto);
				descontoTotal = descontoTotal.add(descontoTipo);
			}
		}

		return descontoTotal;
	}

	public BigDecimal calcularDescontoPorValorCarrinho(BigDecimal subtotal) {
    BigDecimal MIL_REAIS = BigDecimal.valueOf(1000.0);
    BigDecimal QUINHENTOS_REAIS = BigDecimal.valueOf(500.0);
    
    BigDecimal desconto = BigDecimal.ZERO;
    
    if (subtotal.compareTo(MIL_REAIS) > 0) {
        desconto = subtotal.multiply(BigDecimal.valueOf(0.20));
    } else if (subtotal.compareTo(QUINHENTOS_REAIS) > 0) {
        desconto = subtotal.multiply(BigDecimal.valueOf(0.10));
    }
    
    return desconto;
}

	public BigDecimal calcularPesoTributavelTotal(ItemCompra item) {
		BigDecimal pesoFisico = item.getProduto().getPesoFisico();
		BigDecimal comprimento = item.getProduto().getComprimento();
		BigDecimal largura = item.getProduto().getLargura();
		BigDecimal altura = item.getProduto().getAltura();

		BigDecimal pesoVolumetrico = (comprimento.multiply(largura).multiply(altura))
				.divide(BigDecimal.valueOf(6000), 2, RoundingMode.HALF_UP);

		BigDecimal pesoTributavel = pesoFisico.max(pesoVolumetrico).multiply(BigDecimal.valueOf(item.getQuantidade()));

		return pesoTributavel;
	}

	public BigDecimal calcularFretePorRegiao(BigDecimal freteTotal, Regiao regiao) {
		BigDecimal multiplicador = BigDecimal.ZERO;

		switch (regiao) {
			case SUDESTE -> multiplicador = BigDecimal.valueOf(1.00);
			case SUL -> multiplicador = BigDecimal.valueOf(1.05);
			case NORDESTE -> multiplicador = BigDecimal.valueOf(1.10);
			case CENTRO_OESTE -> multiplicador = BigDecimal.valueOf(1.20);
			case NORTE -> multiplicador = BigDecimal.valueOf(1.30);
		}

		return freteTotal.multiply(multiplicador);
	}

	public BigDecimal calcularFretePorPeso(BigDecimal pesoTotal) {
		BigDecimal CINCO_KG = BigDecimal.valueOf(5);
    BigDecimal DEZ_KG = BigDecimal.valueOf(10);
    BigDecimal CINQUENTA_KG = BigDecimal.valueOf(50);

		BigDecimal taxaMinima = BigDecimal.valueOf(12.0);

		if (pesoTotal.compareTo(CINCO_KG) <= 0) {
			return BigDecimal.ZERO;
		} 
		else if (pesoTotal.compareTo(DEZ_KG) <= 0) {
			BigDecimal valorPorKG = BigDecimal.valueOf(2.0);
			return valorPorKG.multiply(pesoTotal).add(taxaMinima);
		} 
		else if (pesoTotal.compareTo(CINQUENTA_KG) <= 0) {
			BigDecimal valorPorKG = BigDecimal.valueOf(4.0);
			return valorPorKG.multiply(pesoTotal).add(taxaMinima);
		} 
		else {
			BigDecimal valorPorKG = BigDecimal.valueOf(7.0);
			return valorPorKG.multiply(pesoTotal).add(taxaMinima);
		}
	}

	public BigDecimal calcularTaxaProdutoFragil(CarrinhoDeCompras carrinho) {
		BigDecimal taxaFragil = carrinho.getItens().stream()
				.filter(item -> item.getProduto().isFragil())
				.map(item -> BigDecimal.valueOf(5.0).multiply(BigDecimal.valueOf(item.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return taxaFragil;
	}

	public BigDecimal aplicarDescontoPorTipoCliente(BigDecimal freteTotal, TipoCliente tipoCliente) {
		BigDecimal desconto = BigDecimal.ZERO;

		switch (tipoCliente) {
			case BRONZE -> desconto = BigDecimal.ZERO;
			case PRATA -> desconto = freteTotal.multiply(BigDecimal.valueOf(0.5));
			case OURO -> desconto = freteTotal.multiply(BigDecimal.valueOf(1.0));
		}

		return freteTotal.subtract(desconto);
	}

	public BigDecimal calcularFreteTotal(CarrinhoDeCompras carrinho) {
		BigDecimal pesoTotal = carrinho.getItens().stream()
				.map(this::calcularPesoTributavelTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal fretePorPeso = calcularFretePorPeso(pesoTotal);

		BigDecimal taxaProdutoFragil = calcularTaxaProdutoFragil(carrinho);

		BigDecimal freteTotal = fretePorPeso.add(taxaProdutoFragil);

		BigDecimal freteComTaxaPorRegiao = calcularFretePorRegiao(freteTotal, carrinho.getCliente().getRegiao());

		BigDecimal freteFinal = aplicarDescontoPorTipoCliente(freteComTaxaPorRegiao,
				carrinho.getCliente().getTipo());

		return freteFinal;
	}
}

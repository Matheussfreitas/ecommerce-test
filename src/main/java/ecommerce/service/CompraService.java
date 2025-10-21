package ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
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
		// To-Do
		return BigDecimal.ZERO;
	}

	public BigDecimal calcularPesoTributavelTotal(ItemCompra item) {
		BigDecimal pesoFisico = item.getProduto().getPesoFisico();
		BigDecimal comprimento = item.getProduto().getComprimento();
		BigDecimal largura = item.getProduto().getLargura();
		BigDecimal altura = item.getProduto().getAltura();

		BigDecimal pesoVolumetrico = (comprimento.multiply(largura).multiply(altura))
				.divide(BigDecimal.valueOf(6000));

		BigDecimal pesoTributavel = pesoFisico.max(pesoVolumetrico).multiply(BigDecimal.valueOf(item.getQuantidade()));

		return pesoTributavel;
	}

	public BigDecimal calcularFretePorRegiao(BigDecimal freteTotal, Regiao regiao) {
		BigDecimal multiplicador = BigDecimal.ZERO;

		switch (regiao) {
			case SUDESTE:
				multiplicador = BigDecimal.valueOf(1.00);
				break;
			case SUL:
				multiplicador = BigDecimal.valueOf(1.05);
				break;
			case NORDESTE:
				multiplicador = BigDecimal.valueOf(1.10);
				break;
			case CENTRO_OESTE:
				multiplicador = BigDecimal.valueOf(1.20);
				break;
			case NORTE:
				multiplicador = BigDecimal.valueOf(1.30);
				break;
		}

		return freteTotal.multiply(multiplicador);
	}

	public BigDecimal calcularFretePorPeso(BigDecimal pesoTotal) {
		BigDecimal valorPorKG = BigDecimal.ZERO;
		BigDecimal taxaMinima = BigDecimal.valueOf(12.0);

		if (pesoTotal.compareTo(pesoTotal) >= 0 && pesoTotal.compareTo(pesoTotal) <= 5) {
			return valorPorKG.multiply(pesoTotal);
		} else if (pesoTotal.compareTo(pesoTotal) > 5 && pesoTotal.compareTo(pesoTotal) <= 10) {
			valorPorKG = BigDecimal.valueOf(2.0);
			return valorPorKG.multiply(pesoTotal).add(taxaMinima);
		} else if (pesoTotal.compareTo(pesoTotal) > 10 && pesoTotal.compareTo(pesoTotal) <= 50) {
			valorPorKG = BigDecimal.valueOf(4.0);
			return valorPorKG.multiply(pesoTotal).add(taxaMinima);
		} else if (pesoTotal.compareTo(pesoTotal) > 50) {
			valorPorKG = BigDecimal.valueOf(7.0);
			return valorPorKG.multiply(pesoTotal).add(taxaMinima);
		}
		return null;
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
			case BRONZE:
				desconto = BigDecimal.ZERO;
				break;
			case PRATA:
				desconto = freteTotal.multiply(BigDecimal.valueOf(0.5));
				break;
			case OURO:
				desconto = freteTotal.multiply(BigDecimal.valueOf(1.0));
				break;
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

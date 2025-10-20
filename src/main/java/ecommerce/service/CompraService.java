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
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.external.IEstoqueExternal;
import ecommerce.external.IPagamentoExternal;
import jakarta.transaction.Transactional;

@Service
public class CompraService
{

	private final CarrinhoDeComprasService carrinhoService;
	private final ClienteService clienteService;

	private final IEstoqueExternal estoqueExternal;
	private final IPagamentoExternal pagamentoExternal;

	@Autowired
	public CompraService(CarrinhoDeComprasService carrinhoService, ClienteService clienteService,
			IEstoqueExternal estoqueExternal, IPagamentoExternal pagamentoExternal)
	{
		this.carrinhoService = carrinhoService;
		this.clienteService = clienteService;

		this.estoqueExternal = estoqueExternal;
		this.pagamentoExternal = pagamentoExternal;
	}

	@Transactional
	public CompraDTO finalizarCompra(Long carrinhoId, Long clienteId)
	{
		Cliente cliente = clienteService.buscarPorId(clienteId);
		CarrinhoDeCompras carrinho = carrinhoService.buscarPorCarrinhoIdEClienteId(carrinhoId, cliente);

		List<Long> produtosIds = carrinho.getItens().stream().map(i -> i.getProduto().getId())
				.collect(Collectors.toList());
		List<Long> produtosQtds = carrinho.getItens().stream().map(i -> i.getQuantidade()).collect(Collectors.toList());

		DisponibilidadeDTO disponibilidade = estoqueExternal.verificarDisponibilidade(produtosIds, produtosQtds);

		if (!disponibilidade.disponivel())
		{
			throw new IllegalStateException("Itens fora de estoque.");
		}

		BigDecimal custoTotal = calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

		PagamentoDTO pagamento = pagamentoExternal.autorizarPagamento(cliente.getId(), custoTotal.doubleValue());

		if (!pagamento.autorizado())
		{
			throw new IllegalStateException("Pagamento não autorizado.");
		}

		EstoqueBaixaDTO baixaDTO = estoqueExternal.darBaixa(produtosIds, produtosQtds);

		if (!baixaDTO.sucesso())
		{
			pagamentoExternal.cancelarPagamento(cliente.getId(), pagamento.transacaoId());
			throw new IllegalStateException("Erro ao dar baixa no estoque.");
		}

		CompraDTO compraDTO = new CompraDTO(true, pagamento.transacaoId(), "Compra finalizada com sucesso.");

		return compraDTO;
	} 

	public BigDecimal calcularCustoTotal(CarrinhoDeCompras carrinho, Regiao regiao, TipoCliente tipoCliente)
	{
		// To-Do
		return BigDecimal.ZERO;
	}

	public BigDecimal calcularFretePorPeso(BigDecimal peso)
	{
		BigDecimal valorPorKG = BigDecimal.ZERO;
		BigDecimal taxaMinima = BigDecimal.valueOf(12.0);

		if (peso.compareTo(peso) >= 0 && peso.compareTo(peso) <= 5) {
			return valorPorKG.multiply(peso);
		}
		else if (peso.compareTo(peso) > 5 && peso.compareTo(peso) <= 10) {
			valorPorKG = BigDecimal.valueOf(2.0);
			return valorPorKG.multiply(peso).add(taxaMinima);
		}
		else if (peso.compareTo(peso) > 10 && peso.compareTo(peso) <= 50) {
			valorPorKG = BigDecimal.valueOf(4.0);
			return valorPorKG.multiply(peso).add(taxaMinima);
		}
		else if (peso.compareTo(peso) > 50) {
			valorPorKG = BigDecimal.valueOf(7.0);
			return valorPorKG.multiply(peso).add(taxaMinima);
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
}

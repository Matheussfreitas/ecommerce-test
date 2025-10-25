package ecommerce.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ecommerce.entity.CarrinhoDeCompras;
import ecommerce.entity.Cliente;
import ecommerce.entity.ItemCompra;
import ecommerce.entity.Produto;
import ecommerce.entity.Regiao;
import ecommerce.entity.TipoCliente;
import ecommerce.entity.TipoProduto;

/**
 * Testes de Tabela de Decisão / Combinações Complexas (Caixa Preta)
 * Cobre cenários onde múltiplas regras interagem e Robustez.
 */
@DisplayName("Testes de Regras e Decisões - CompraService")
public class DecisoesTest {

    private CompraService compraService;

    // Constantes de cálculo
    private static final BigDecimal TAXA_MINIMA_FRETE = new BigDecimal("12.00");
    private static final BigDecimal VALOR_PESO_FAIXA_B = new BigDecimal("2.00"); // 5 < peso <= 10
    private static final BigDecimal VALOR_PESO_FAIXA_C = new BigDecimal("4.00"); // 10 < peso <= 50
    private static final BigDecimal VALOR_PESO_FAIXA_D = new BigDecimal("7.00"); // > 50
    private static final BigDecimal TAXA_FRAGIL = new BigDecimal("5.00");

    // Constantes de produto base
    private static final BigDecimal DIMENSAO_PADRAO = new BigDecimal("10.0");

    @BeforeEach
    void setUp() {
        // Assume que CompraService requer injeção de dependência para outros serviços/repos
        compraService = new CompraService(null, null, null, null);
    }

    // ============================================================================
    // IDs 65, 66, 67: TESTES DE ROBUSTEZ
    // ============================================================================

    @Test
    @DisplayName("ID 65 - Robustez: Qtd <= 0 (Lançar Exceção)")
    void testRobustezQuantidadeInvalida() {
        // Arrange: Qtd=-1, Sub=100, Peso=10
        Cliente cliente = criarCliente(TipoCliente.OURO, Regiao.NORTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("100.00"), new BigDecimal("10.0"), true, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, -1); // Qtd <= 0

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());
        }, "ID 65: Deve lançar exceção se a quantidade de itens for inválida.");
    }

    @Test
    @DisplayName("ID 66 - Robustez: Subtotal < 0 (Preço < 0, Lançar Exceção)")
    void testRobustezSubtotalNegativo() {
        // Arrange: Qtd=3, Sub=-1 (Preço -0.33), Peso=5
        Cliente cliente = criarCliente(TipoCliente.OURO, Regiao.NORTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        // Preço unitário negativo para gerar Subtotal negativo (-1/3)
        BigDecimal precoNegativo = new BigDecimal("-0.33333").setScale(2, RoundingMode.HALF_UP); // -0.33

        Produto produto = criarProduto("P", precoNegativo, new BigDecimal("1.66667").setScale(2, RoundingMode.HALF_UP), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 3); // Subtotal bruto ~ -1.00

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());
        }, "ID 66: Deve lançar exceção se o subtotal (e o preço) for negativo.");
    }

    @Test
    @DisplayName("ID 67 - Robustez: Peso Total < 0 (Lançar Exceção)")
    void testRobustezPesoNegativo() {
        // Arrange: Qtd=5, Sub=200, Peso=-0.1
        Cliente cliente = criarCliente(TipoCliente.OURO, Regiao.NORDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        // Peso unitário negativo para gerar Peso Total negativo
        Produto produto = criarProduto("P", new BigDecimal("40.00"), new BigDecimal("-0.02"), true, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 5); // Peso Total = -0.1

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());
        }, "ID 67: Deve lançar exceção se o peso total for negativo.");
    }

    // ============================================================================
    // IDs 68 a 71: TESTES DE COMBINAÇÃO DE REGRAS
    // ============================================================================

    @Test
    @DisplayName("ID 68 - Combinação Básica: Sub 10% Desc. + Frete Isento/Bronze")
    void testCombinacaoBasica() {
        // Arrange: Qtd=1, Sub=600, Peso=3, Sudeste, Bronze, Não Frágil
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("P", new BigDecimal("600.00"), new BigDecimal("3.00"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Cálculo:
        // 1. Subtotal Bruto: 600.00
        // 2. Desconto Valor (10%): 600 * 0.10 = 60.00
        // 3. Subtotal Final: 540.00
        // 4. Frete (Peso 3kg): Isento (R$ 0.00)
        // 5. Total: 540.00
        assertThat(custoTotal)
                .as("ID 68: Deve aplicar 10% de desconto por valor, e Frete Isento/Bronze")
                .isEqualByComparingTo("540.00");
    }

    @Test
    @DisplayName("ID 69 - Complexo Qtd 10% + Sub 10% + Frete Faixa D/Ouro/Nordeste")
    void testComplexoFaixaDOuroNordeste() {
        // Arrange: Qtd=6 (10% desc), Sub=600, Peso=51 (Faixa D), Nordeste (x1.10), Ouro (0% frete), Não Frágil
        Cliente cliente = criarCliente(TipoCliente.OURO, Regiao.NORDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        // Item: Qtd 6, Preço 100/un, Peso 8.5/un -> Sub bruto 600, Peso total 51kg
        Produto produto = criarProduto("P", new BigDecimal("100.00"), new BigDecimal("8.50"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 6);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Cálculo:
        // 1. Desconto Qtd (10%): 600 * 0.10 = 60.00. Sub após Qtd: 540.00
        // 2. Desconto Valor (10%): 540 * 0.10 = 54.00. Subtotal Final: 486.00
        // 3. Frete Base (Peso 51kg, Faixa D): (51 * 7.00) + 12.00 = 369.00
        // 4. Multiplicador (Nordeste x1.10): 369.00 * 1.10 = 405.90
        // 5. Desconto Cliente (Ouro): 100% (Frete Final R$ 0.00)
        // 6. Total: 486.00 + 0.00 = 486.00
        assertThat(custoTotal)
                .as("ID 69: Deve aplicar 10% Qtd e 10% Sub, e zerar Frete (Cliente Ouro)")
                .isEqualByComparingTo("486.00");
    }

    @Test
    @DisplayName("ID 70 - Complexo Qtd 5% + Sub 20% + Frete Faixa C/Prata/Sul/Frágil")
    void testComplexoPrataFragemFaixaC() {
        // Arrange: Qtd=4 (5% desc), Sub=1500, Peso=20 (Faixa C), Sul (x1.05), Prata (50% frete), Frágil
        Cliente cliente = criarCliente(TipoCliente.PRATA, Regiao.SUL);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        // Item: Qtd 4, Preço 375/un, Peso 5/un -> Sub bruto 1500, Peso total 20kg, Frágil=T
        Produto produto = criarProduto("P", new BigDecimal("375.00"), new BigDecimal("5.00"), true, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 4);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Cálculo:
        // 1. Desc. Qtd (5%): 1500 * 0.95 = 1425.00
        // 2. Desc. Valor (20%): 1425 * 0.80 = 1140.00 (Subtotal Final)
        // 3. Frete Base (Peso 20kg, Faixa C): (20 * 4.00) + 12.00 = 92.00
        // 4. Taxa Frágil: 4 * 5.00 = 20.00. Frete Bruto: 92.00 + 20.00 = 112.00
        // 5. Multiplicador (Sul x1.05): 112.00 * 1.05 = 117.60
        // 6. Desconto Cliente (Prata 50%): 117.60 * 0.50 = 58.80 (Frete Final)
        // 7. Total: 1140.00 + 58.80 = 1198.80
        assertThat(custoTotal)
                .as("ID 70: Deve combinar Desc. Qtd, Desc. Sub, Frete Faixa C, Taxa Frágil, Mult. Sul e Desc. Prata")
                .isEqualByComparingTo("1198.80");
    }

    @Test
    @DisplayName("ID 71 - Combinação Máxima Penalização: Qtd 15% + Sub 20% + Frete Faixa D/CO/Bronze")
    void testMaximoDescontoEFrete() {
        // Arrange: Qtd=9 (15% desc), Subtotal Bruto=1350.00, Peso Total=54.00kg (Faixa D), Centro-Oeste (x1.20), Bronze (0% frete), Não Frágil
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.CENTRO_OESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        // Valores ajustados para evitar dízimas periódicas:
        // Subtotal Bruto: 1350.00 / 9 = R$ 150.00 (limpo)
        // Peso Total: 54.00kg / 9 = 6.00kg (limpo)

        // Subtotal Bruto é R$ 1350.00 e Peso Total é 54.00kg
        BigDecimal precoUnitario = new BigDecimal("150.00");
        BigDecimal pesoUnitario = new BigDecimal("6.00");

        Produto produto = criarProduto("Produto Limpo", precoUnitario, pesoUnitario, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 9);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Novo Cálculo em Etapas:
        // 1. Subtotal Bruto: 1350.00
        // 2. Desc. Qtd (15%): 1350.00 * 0.85 = 1147.50 (Sub após Qtd)
        // 3. Desc. Valor (20%): 1147.50 * 0.80 = 918.00 (Subtotal Final com Descontos)
        // 4. Frete Base (Peso 54kg, Faixa D): (54 * 7.00) + 12.00 = 390.00
        // 5. Multiplicador (C-Oeste x1.20): 390.00 * 1.20 = 468.00 (Frete Final)
        // 6. Total: 918.00 + 468.00 = 1386.00
        assertThat(custoTotal)
                .as("ID 71: Máxima penalização deve combinar Qtd 15%, Sub 20%, Faixa D, Mult. Centro-Oeste e Bronze")
                .isEqualByComparingTo("1386.00");
    }

    // ============================================================================
    // MÉTODOS AUXILIARES (BUILDERS) - REPLICADOS
    // ============================================================================

    private Cliente criarCliente(TipoCliente tipo, Regiao regiao) {
        return new Cliente(1L, "Cliente Teste", regiao, tipo);
    }

    private CarrinhoDeCompras criarCarrinho(Cliente cliente) {
        CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
        carrinho.setCliente(cliente);
        carrinho.setItens(new ArrayList<>());
        return carrinho;
    }

    private Produto criarProduto(String nome, BigDecimal preco, BigDecimal peso,
                                 boolean fragil, TipoProduto tipo) {
        return new Produto(
                1L,
                nome,
                "Descrição do " + nome,
                preco,
                peso,
                DIMENSAO_PADRAO, // comprimento
                DIMENSAO_PADRAO, // largura
                DIMENSAO_PADRAO, // altura
                fragil,
                tipo
        );
    }

    private void adicionarItem(CarrinhoDeCompras carrinho, Produto produto, long quantidade) {
        ItemCompra item = new ItemCompra();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        carrinho.getItens().add(item);
    }
}
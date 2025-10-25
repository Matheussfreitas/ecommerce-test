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
 * Testes de Análise de Valor Limite (Caixa Preta)
 * * Foca em valores imediatamente:
 * - Acima (N+e)
 * - No limite (N)
 * - Abaixo (N-e)
 * Das transições para:
 * - Quantidade de Itens: 2, 3, 4, 5, 7, 8
 * - Peso Total: 5.00kg, 10.00kg, 50.00kg
 * - Subtotal: R$ 500.00, R$ 1000.00
 */
@DisplayName("Testes de Limites de Domínio - CompraService")
public class LimitesTest {

    private CompraService compraService;

    // Constantes para facilitar manutenção
    private static final BigDecimal PRECO_PADRAO = new BigDecimal("100.00");
    private static final BigDecimal PESO_PADRAO = new BigDecimal("1.00");
    private static final BigDecimal DIMENSAO_PADRAO = new BigDecimal("10.0");

    // Constantes do domínio
    private static final BigDecimal TAXA_MINIMA_FRETE = new BigDecimal("12.00");
    private static final BigDecimal VALOR_PESO_FAIXA_B = new BigDecimal("2.00"); // 5.01 a 10.00kg
    private static final BigDecimal VALOR_PESO_FAIXA_C = new BigDecimal("4.00"); // 10.01 a 50.00kg
    private static final BigDecimal VALOR_PESO_FAIXA_D = new BigDecimal("7.00"); // > 50.00kg

    @BeforeEach
    void setUp() {
        // Assume que CompraService requer injeção de dependência para outros serviços/repos
        compraService = new CompraService(null, null, null, null);
    }

    // LIMITES: QUANTIDADE DE ITENS

    @Test
    @DisplayName("ID 30 - Limite Qtd <=0 (Lançar Excessão)")
    void testRobustezQuantidadeNegativa() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 0);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());
        }, "Deve lançar exceção ao tentar calcular com quantidade negativa");
    }

    @Test
    @DisplayName("ID 31 - Limite Qtd=1 (Min 0% Desconto)")
    void testQtdLimite0NenhumDesconto() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal Bruto: 100.00. Frete isento. Total: 100.00
        assertThat(custoTotal)
                .as("Qtd = 1 deve ser o limite MÍNIMO para 0% de desconto por quantidade")
                .isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("ID 32 - Limite Qtd=2 (Max 0% Desconto)")
    void testQtdLimite2NenhumDesconto() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 2);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal Bruto: 200.00. Frete isento. Total: 200.00
        assertThat(custoTotal)
                .as("Qtd = 2 deve ser o limite MÁXIMO para 0% de desconto por quantidade")
                .isEqualByComparingTo("200.00");
    }


    @Test
    @DisplayName("ID 33 - Limite Qtd=3 (Min 5% Desconto)")
    void testQtdLimite3ComDesconto5Porcento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 3);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal Bruto: 300.00. Desconto 5%: 15.00. Total: 285.00
        assertThat(custoTotal)
                .as("Qtd = 3 deve ser o limite MÍNIMO para 5% de desconto por quantidade")
                .isEqualByComparingTo("285.00");
    }

    @Test
    @DisplayName("ID 34 - Limite Qtd=4 (Max 5% Desconto)")
    void testQtdLimite4ComDesconto5Porcento() {
        // Expected: 400 * 0.95 = 380.00
        // ...
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 4);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        assertThat(custoTotal)
                .as("Qtd = 4 deve ser o limite MÁXIMO para 5% de desconto por quantidade")
                .isEqualByComparingTo("380.00");
    }

    @Test
    @DisplayName("ID 35 - Limite Qtd=5 (Min 10% Desconto)")
    void testQtdLimite5ComDesconto10Porcento() {
        // Expected: 500 * 0.90 = 450.00
        // ...
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 5);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        assertThat(custoTotal)
                .as("Qtd = 5 deve ser o limite MÍNIMO para 10% de desconto por quantidade")
                .isEqualByComparingTo("450.00");
    }

    @Test
    @DisplayName("ID 36 - Limite Qtd=7 (Max 10% Desconto)")
    void testQtdLimite7ComDesconto10Porcento() {
        // Expected: 700 * 0.90 = 630.00 (Subtotal)
        // Subtotal após desc. Qtd: 630. Frete: 7 * 2 + 12 = 26.
        // 630 > 500, aplica 10% desc. valor: 630 * 0.10 = 63. Subtotal final: 630 - 63 = 567
        // Total: 567 + 26 = 593.00
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 7);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        assertThat(custoTotal)
                .as("Qtd = 7 deve ser o limite MÁXIMO para 10% de desconto por quantidade (e aplica desc. valor)")
                .isEqualByComparingTo("593.00");
    }

    @Test
    @DisplayName("ID 37 - Limite Qtd=8 (Min 15% Desconto)")
    void testQtdLimite8ComDesconto15Porcento() {
        // Expected: 640.00
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 8);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        assertThat(custoTotal)
                .as("Qtd = 8 deve ser o limite MÍNIMO para 15% de desconto por quantidade (e aplica desc. valor)")
                .isEqualByComparingTo("640.00");
    }

    // LIMITES: PESO TOTAL (KG) - IDs 39 a 51

    @Test
    @DisplayName("ID 39 - Limite: Peso Inválido (Entrada -0.1kg - Lançar Exceção)")
    void testRobustezPesoNegativo() {
        // Arrange - Teste 39: Entrada -0.1 (Robustez)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        // Uso de -0.1kg conforme a tabela do usuário
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("-0.1"), false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 1);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());
        }, "ID 39: Deve lançar exceção ao tentar calcular com peso negativo");
    }

    @Test
    @DisplayName("ID 40 - Limite Peso = 0.00 kg (MIN Faixa A - Isento)")
    void testLimitePeso0KgIsento() {
        // Arrange - Teste 40: Entrada 0.00 (Minimo Válido)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("0.00"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete deve ser R$ 0.00
        assertThat(custoTotal)
                .as("ID 40: Peso = 0.00kg deve ser isento de frete")
                .isEqualByComparingTo(PRECO_PADRAO); // 100.00
    }

    @Test
    @DisplayName("ID 41 - Limite Peso = 0.1 kg (MIN+ Faixa A - Isento)")
    void testLimitePeso0Ponto1KgIsento() {
        // Arrange - Teste 41: Entrada 0.1 (Dentro da Partição A)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("0.1"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete deve ser R$ 0.00
        assertThat(custoTotal)
                .as("ID 41: Peso = 0.1kg deve ser isento de frete")
                .isEqualByComparingTo(PRECO_PADRAO); // 100.00
    }

    @Test
    @DisplayName("ID 42 - Limite Peso = 4.9 kg (MAX- Faixa A - Isento)")
    void testLimitePeso4Ponto9KgIsento() {
        // Arrange - Teste 42: Entrada 4.9 (Quase no limite de isenção)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("4.9"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete deve ser R$ 0.00
        assertThat(custoTotal)
                .as("ID 42: Peso = 4.9kg deve ser isento de frete")
                .isEqualByComparingTo(PRECO_PADRAO); // 100.00
    }

    @Test
    @DisplayName("ID 43 - Limite Peso = 5.00 kg (MAX Faixa A / MIN- Faixa B - Isento)")
    void testLimitePeso5KgIsento() {
        // Arrange - Teste 43: Entrada 5.00 (Limite máximo de isenção)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("5.00"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete deve ser R$ 0.00
        assertThat(custoTotal)
                .as("ID 43: Peso = 5.00kg deve ser o limite MÁXIMO para Frete Isento (Faixa A)")
                .isEqualByComparingTo(PRECO_PADRAO); // 100.00
    }

    @Test
    @DisplayName("ID 44 - Limite Peso = 5.01 kg (MIN Faixa B - R$ 2/kg + R$ 12)")
    void testLimitePeso5Ponto01KgComFrete() {
        // Arrange - Teste 44: Entrada 5.01 (Início da Faixa B)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("5.01"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 5.01 * 2 + 12 = 22.02. Total: 122.02
        BigDecimal frete = new BigDecimal("5.01").multiply(VALOR_PESO_FAIXA_B).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 44: Peso = 5.01kg deve ser o limite MÍNIMO para Faixa B")
                .isEqualByComparingTo(custoEsperado); // 122.02
    }

    @Test
    @DisplayName("ID 45 - Limite Peso = 9.9 kg (MAX- Faixa B - R$ 2/kg + R$ 12)")
    void testLimitePeso9Ponto9Kg() {
        // Arrange - Teste 45: Entrada 9.9 (Perto do limite máximo da Faixa B)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("9.9"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 9.9 * 2 + 12 = 31.80. Total: 131.80
        BigDecimal frete = new BigDecimal("9.9").multiply(VALOR_PESO_FAIXA_B).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 45: Peso = 9.9kg deve estar na Faixa B")
                .isEqualByComparingTo(custoEsperado); // 131.80
    }

    @Test
    @DisplayName("ID 46 - Limite Peso = 10.00 kg (MAX Faixa B / MIN- Faixa C - R$ 2/kg + R$ 12)")
    void testLimitePeso10Kg() {
        // Arrange - Teste 46: Entrada 10.00 (Limite máximo da Faixa B)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("10.00"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 10 * 2 + 12 = 32.00. Total: 132.00
        BigDecimal frete = new BigDecimal("10.00").multiply(VALOR_PESO_FAIXA_B).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 46: Peso = 10.00kg deve ser o limite MÁXIMO para Faixa B")
                .isEqualByComparingTo(custoEsperado); // 132.00
    }

    @Test
    @DisplayName("ID 47 - Limite Peso = 10.01 kg (MIN Faixa C - R$ 4/kg + R$ 12)")
    void testLimitePeso10Ponto01Kg() {
        // Arrange - Teste 47: Entrada 10.01 (Início da Faixa C)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("10.01"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 10.01 * 4 + 12 = 52.04. Total: 152.04
        BigDecimal frete = new BigDecimal("10.01").multiply(VALOR_PESO_FAIXA_C).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 47: Peso = 10.01kg deve ser o limite MÍNIMO para Faixa C")
                .isEqualByComparingTo(custoEsperado); // 152.04
    }

    @Test
    @DisplayName("ID 48 - Limite Peso = 49.9 kg (MAX- Faixa C - R$ 4/kg + R$ 12)")
    void testLimitePeso49Ponto9Kg() {
        // Arrange - Teste 48: Entrada 49.9 (Perto do limite máximo da Faixa C)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("49.9"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 49.9 * 4 + 12 = 211.60. Total: 311.60
        BigDecimal frete = new BigDecimal("49.9").multiply(VALOR_PESO_FAIXA_C).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 48: Peso = 49.9kg deve estar na Faixa C")
                .isEqualByComparingTo(custoEsperado); // 311.60
    }

    @Test
    @DisplayName("ID 49 - Limite Peso = 50.00 kg (MAX Faixa C - R$ 4/kg + R$ 12)")
    void testLimitePeso50Kg() {
        // Arrange - Teste 49: Entrada 50.00 (Limite máximo da Faixa C)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("50.00"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 50 * 4 + 12 = 212.00. Total: 312.00
        BigDecimal frete = new BigDecimal("50.00").multiply(VALOR_PESO_FAIXA_C).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 49: Peso = 50.00kg deve ser o limite MÁXIMO para Faixa C")
                .isEqualByComparingTo(custoEsperado); // 312.00
    }

    @Test
    @DisplayName("ID 50 - Limite Peso = 50.01 kg (MIN Faixa D - R$ 7/kg + R$ 12)")
    void testLimitePeso50Ponto01Kg() {
        // Arrange - Teste 50: Entrada 50.01 (Início da Faixa D)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("50.01"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 50.01 * 7 + 12 = 362.07. Total: 462.07
        BigDecimal frete = new BigDecimal("50.01").multiply(VALOR_PESO_FAIXA_D).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 50: Peso = 50.01kg deve ser o limite MÍNIMO para Faixa D")
                .isEqualByComparingTo(custoEsperado); // 462.07
    }

    @Test
    @DisplayName("ID 51 - Limite Peso = 50.2 kg (MIN+ Faixa D - R$ 7/kg + R$ 12)")
    void testLimitePeso50Ponto2Kg() {
        // Arrange - Teste 51: Entrada 50.2 (Dentro da Partição D)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", PRECO_PADRAO, new BigDecimal("50.2"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 50.2 * 7 + 12 = 363.40. Total: 463.40
        BigDecimal frete = new BigDecimal("50.2").multiply(VALOR_PESO_FAIXA_D).add(TAXA_MINIMA_FRETE);
        BigDecimal custoEsperado = PRECO_PADRAO.add(frete).setScale(2, RoundingMode.HALF_UP);

        assertThat(custoTotal)
                .as("ID 51: Peso = 50.2kg deve estar na Faixa D")
                .isEqualByComparingTo(custoEsperado); // 463.40
    }

// LIMITES: SUBTOTAL (DESCONTO POR VALOR) - IDs 52 a 62

    @Test
    @DisplayName("ID 52 - Limite: Subtotal Inválido (-0.1 - Lançar Exceção)")
    void testRobustezSubtotalNegativo() {
        // Arrange - Teste 52: Entrada -0.1 (Robustez)
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        // Produto com preço negativo para gerar subtotal negativo
        Produto produto = criarProduto("P", new BigDecimal("-0.1"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());
        }, "ID 52: Deve lançar exceção ao tentar calcular com subtotal negativo");
    }

    @Test
    @DisplayName("ID 53 - Limite Subtotal = R$ 0.00 (MIN Válido - 0% Desconto)")
    void testLimiteSubtotal0SemDesconto() {
        // Arrange - Teste 53: Entrada 0.00
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        // Usaremos um carrinho vazio, que resulta em subtotal 0

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete isento, Total: 0.00
        assertThat(custoTotal)
                .as("ID 53: Subtotal = R$ 0.00 deve resultar em 0% de desconto e custo total R$ 0.00")
                .isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("ID 54 - Limite Subtotal = R$ 0.10 (MIN+ - 0% Desconto)")
    void testLimiteSubtotal0Ponto1SemDesconto() {
        // Arrange - Teste 54: Entrada 0.10
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("0.10"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete isento, Total: 0.10
        assertThat(custoTotal)
                .as("ID 54: Subtotal = R$ 0.10 deve ter 0% de desconto")
                .isEqualByComparingTo("0.10");
    }

    @Test
    @DisplayName("ID 55 - Limite Subtotal = R$ 499.99 (MAX- 0% Desconto)")
    void testLimiteSubtotal499Ponto99SemDesconto() {
        // Arrange - Teste 55: Entrada 499.99
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("499.99"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal < 500, 0% de desconto. Total: 499.99
        assertThat(custoTotal)
                .as("ID 55: Subtotal = R$ 499.99 deve ser o limite MÁXIMO para 0% de desconto por valor")
                .isEqualByComparingTo("499.99");
    }

    @Test
    @DisplayName("ID 56 - Limite Subtotal = R$ 500.00 (MAX 0% Desconto)")
    void testLimiteSubtotal500ComDesconto10Porcento() {
        // Arrange - Teste 56: Entrada 500.00
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("500.00"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 0%: Total: 500.00
        assertThat(custoTotal)
                .as("ID 56: Subtotal = R$ 500.00 deve ser o limite MÁXIMO para 0% de desconto por valor. Total: 500.00")
                .isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("ID 57 - Limite Subtotal = R$ 500.10 (MIN+ 10% Desconto)")
    void testLimiteSubtotal500Ponto1ComDesconto10Porcento() {
        // Arrange - Teste 57: Entrada 500.10
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("500.10"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 10%: 50.01. Total: 450.09
        BigDecimal subtotal = new BigDecimal("500.10");
        BigDecimal desconto = subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP); // 50.01
        BigDecimal custoEsperado = subtotal.subtract(desconto).setScale(2, RoundingMode.HALF_UP); // 450.09

        assertThat(custoTotal)
                .as("ID 57: Subtotal = R$ 500.10 deve ter 10% de desconto")
                .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 58 - Limite Subtotal = R$ 500.20 (MIN++ 10% Desconto)")
    void testLimiteSubtotal500Ponto2ComDesconto10Porcento() {
        // Arrange - Teste 58: Entrada 500.20
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("500.20"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 10%: 50.02. Total: 450.18
        BigDecimal subtotal = new BigDecimal("500.20");
        BigDecimal desconto = subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP); // 50.02
        BigDecimal custoEsperado = subtotal.subtract(desconto).setScale(2, RoundingMode.HALF_UP); // 450.18

        assertThat(custoTotal)
                .as("ID 58: Subtotal = R$ 500.20 deve ter 10% de desconto")
                .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 59 - Limite Subtotal = R$ 999.90 (MAX- 10% Desconto)")
    void testLimiteSubtotal999Ponto9ComDesconto10Porcento() {
        // Arrange - Teste 59: Entrada 999.90
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("999.90"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 10%: 100.00. Total: 899.90
        BigDecimal subtotal = new BigDecimal("999.90");
        BigDecimal desconto = subtotal.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP); // 100.00
        BigDecimal custoEsperado = subtotal.subtract(desconto).setScale(2, RoundingMode.HALF_UP); // 899.90

        assertThat(custoTotal)
                .as("ID 59: Subtotal = R$ 999.90 deve ser o limite MÁXIMO para 10% de desconto (MAX-)")
                .isEqualByComparingTo("899.91"); // 999.90 * 0.9 = 899.91
    }

    @Test
    @DisplayName("ID 60 - Limite Subtotal = R$ 1000.00 (MAX 10% Desconto)")
    void testLimiteSubtotal1000ComDesconto10Porcento() {
        // Arrange - Teste 60: Entrada 1000.00
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("1000.00"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 10%: 100.00. Total: 900.00
        assertThat(custoTotal)
                .as("ID 60: Subtotal = R$ 1000.00 deve ser o limite MÁXIMO para 10% de desconto por valor")
                .isEqualByComparingTo("900.00");
    }

    @Test
    @DisplayName("ID 61 - Limite Subtotal = R$ 1000.10 (MIN 20% Desconto)")
    void testLimiteSubtotal1000Ponto1ComDesconto20Porcento() {
        // Arrange - Teste 61: Entrada 1000.10
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("1000.10"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 20%: 200.02. Total: 800.08
        BigDecimal subtotal = new BigDecimal("1000.10");
        BigDecimal desconto = subtotal.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP); // 200.02
        BigDecimal custoEsperado = subtotal.subtract(desconto).setScale(2, RoundingMode.HALF_UP); // 800.08

        assertThat(custoTotal)
                .as("ID 61: Subtotal = R$ 1000.10 deve ser o limite MÍNIMO para 20% de desconto por valor")
                .isEqualByComparingTo(custoEsperado); // 800.08
    }

    @Test
    @DisplayName("ID 62 - Limite Subtotal = R$ 1000.20 (MIN+ 20% Desconto)")
    void testLimiteSubtotal1000Ponto2ComDesconto20Porcento() {
        // Arrange - Teste 62: Entrada 1000.20
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);
        Produto produto = criarProduto("P", new BigDecimal("1000.20"), PESO_PADRAO, false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Desconto 20%: 200.04. Total: 800.16
        BigDecimal subtotal = new BigDecimal("1000.20");
        BigDecimal desconto = subtotal.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP); // 200.04
        BigDecimal custoEsperado = subtotal.subtract(desconto).setScale(2, RoundingMode.HALF_UP); // 800.16

        assertThat(custoTotal)
                .as("ID 62: Subtotal = R$ 1000.20 deve ter 20% de desconto")
                .isEqualByComparingTo(custoEsperado); // 800.16
    }


    // MÉTODOS AUXILIARES (BUILDERS)

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
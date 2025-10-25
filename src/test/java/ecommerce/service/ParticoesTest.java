package ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
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
 * Testes de Partições de Domínio (Caixa Preta) - Baseado na Tabela de Partições
 *
 * Cobre as seguintes partições:
 * - Quantidade de Itens (0, 1-2, 3-4, 5-7, ≥8, negativa)
 * - Peso Total (< 0, 0-5kg, 5-10kg, 10-50kg, > 50kg)
 * - Subtotal (≤ 0, 0-500, 500-1000, > 1000)
 * - Região (Sudeste, Sul, Nordeste, Centro-Oeste, Norte)
 * - Tipo de Cliente (Bronze, Prata, Ouro)
 * - Produto Frágil (Sim, Não)
 */
@DisplayName("Testes de Partições de Domínio - CompraService")
public class ParticoesTest {

    private CompraService compraService;

    // Constantes para facilitar manutenção
    private static final BigDecimal PRECO_PADRAO = new BigDecimal("100.00");
    private static final BigDecimal PESO_PADRAO = new BigDecimal("1.0");
    private static final BigDecimal DIMENSAO_PADRAO = new BigDecimal("10.0");

    @BeforeEach
    void setUp() {
        compraService = new CompraService(null, null, null, null);
    }

    // ============================================================================
    // PARTIÇÕES: QUANTIDADE DE ITENS
    // ============================================================================

    @Test
    @DisplayName("ID 1 - Carrinho com 0 itens deve retornar custo zero")
    void testCarrinhoVazio() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        assertThat(custoTotal)
            .as("Carrinho vazio deve ter custo total zero")
            .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("ID 2 - Qtd = 1-2 (sem desconto por quantidade)")
    void testQuantidade1a2SemDesconto() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto A", PRECO_PADRAO, PESO_PADRAO, false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 2); // 2 itens do mesmo tipo

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        BigDecimal subtotalEsperado = PRECO_PADRAO.multiply(new BigDecimal("2")); // 200.00
        assertThat(custoTotal)
            .as("Com 2 itens do mesmo tipo, não deve haver desconto por múltiplos itens")
            .isGreaterThanOrEqualTo(subtotalEsperado);
    }

    @Test
    @DisplayName("ID 3 - Qtd = 3-4 (desconto de 5%)")
    void testQuantidade3a4ComDesconto5Porcento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto A", PRECO_PADRAO, new BigDecimal("1.0"), false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 3); // 3 itens do mesmo tipo

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Cálculo:
        // 1. Subtotal bruto: 300
        // 2. Desconto por quantidade (5%): 15
        // 3. Subtotal: 285 (< 500, sem desconto por valor)
        // 4. Peso: 3kg (isento de frete)
        // 5. Total: 285
        BigDecimal subtotalBruto = PRECO_PADRAO.multiply(new BigDecimal("3")); // 300
        BigDecimal desconto = subtotalBruto.multiply(new BigDecimal("0.05")); // 15
        BigDecimal custoEsperado = subtotalBruto.subtract(desconto); // 285

        assertThat(custoTotal)
            .as("Com 3 itens do mesmo tipo, deve aplicar 5%% de desconto")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 4 - Qtd = 5-7 (desconto de 10%)")
    void testQuantidade5a7ComDesconto10Porcento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto A", PRECO_PADRAO, new BigDecimal("1.0"), false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 5); // 5 itens do mesmo tipo

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Cálculo:
        // 1. Subtotal bruto: 500
        // 2. Desconto por quantidade (10%): 50
        // 3. Subtotal: 450 (< 500, sem desconto por valor)
        // 4. Peso: 5kg (isento de frete)
        // 5. Total: 450
        BigDecimal subtotalBruto = PRECO_PADRAO.multiply(new BigDecimal("5")); // 500
        BigDecimal desconto = subtotalBruto.multiply(new BigDecimal("0.10")); // 50
        BigDecimal custoEsperado = subtotalBruto.subtract(desconto); // 450

        assertThat(custoTotal)
            .as("Com 5 itens do mesmo tipo, deve aplicar 10%% de desconto")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 5 - Qtd ≥ 8 (desconto de 15%)")
    void testQuantidade8OuMaisComDesconto15Porcento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto A", PRECO_PADRAO, new BigDecimal("1.0"), false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 8); // 8 itens do mesmo tipo

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Cálculo em etapas:
        // 1. Subtotal bruto: 800
        // 2. Desconto por quantidade (15%): 800 * 0.15 = 120
        // 3. Subtotal após desconto quantidade: 800 - 120 = 680
        // 4. Desconto por valor (10% pois 680 > 500): 680 * 0.10 = 68
        // 5. Subtotal final: 680 - 68 = 612
        // 6. Peso total: 8kg, Frete: (8 * 2) + 12 = 28
        // 7. Total: 612 + 28 = 640
        BigDecimal subtotalBruto = PRECO_PADRAO.multiply(new BigDecimal("8")); // 800
        BigDecimal descontoQuantidade = subtotalBruto.multiply(new BigDecimal("0.15")); // 120
        BigDecimal subtotalAposDescontoQtd = subtotalBruto.subtract(descontoQuantidade); // 680
        BigDecimal descontoValor = subtotalAposDescontoQtd.multiply(new BigDecimal("0.10")); // 68
        BigDecimal subtotalFinal = subtotalAposDescontoQtd.subtract(descontoValor); // 612
        BigDecimal frete = new BigDecimal("8").multiply(new BigDecimal("2")).add(new BigDecimal("12")); // 28
        BigDecimal custoEsperado = subtotalFinal.add(frete); // 640

        assertThat(custoTotal)
            .as("Com 8 ou mais itens do mesmo tipo, deve aplicar 15%% de desconto por quantidade E 10%% por valor")
            .isEqualByComparingTo(custoEsperado);
    }

    // ============================================================================
    // PARTIÇÕES: PESO TOTAL
    // ============================================================================

    @Test
    @DisplayName("ID 8 - Peso ≤ 5 kg (frete isento)")
    void testPesoAte5KgFreteIsento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto Leve", new BigDecimal("100.00"),
                                       new BigDecimal("5.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Apenas o subtotal, sem frete
        assertThat(custoTotal)
            .as("Peso ≤ 5kg deve ter frete isento")
            .isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("ID 9 - Peso 5 < peso ≤ 10 kg (R$ 2,00/kg + taxa R$ 12,00)")
    void testPeso5a10Kg() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto Médio", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal: 100 + Frete: (6 * 2) + 12 = 24
        BigDecimal custoEsperado = new BigDecimal("124.00");
        assertThat(custoTotal)
            .as("Peso entre 5 e 10kg deve calcular frete corretamente")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 10 - Peso 10 < peso ≤ 50 kg (R$ 4,00/kg + taxa R$ 12,00)")
    void testPeso10a50Kg() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto Pesado", new BigDecimal("100.00"),
                                       new BigDecimal("20.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal: 100 + Frete: (20 * 4) + 12 = 92
        BigDecimal custoEsperado = new BigDecimal("192.00");
        assertThat(custoTotal)
            .as("Peso entre 10 e 50kg deve calcular frete corretamente")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 11 - Peso > 50 kg (R$ 7,00/kg + taxa R$ 12,00)")
    void testPesoMaior50Kg() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto Muito Pesado", new BigDecimal("100.00"),
                                       new BigDecimal("60.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal: 100 + Frete: (60 * 7) + 12 = 432
        BigDecimal custoEsperado = new BigDecimal("532.00");
        assertThat(custoTotal)
            .as("Peso > 50kg deve calcular frete corretamente")
            .isEqualByComparingTo(custoEsperado);
    }

    // ============================================================================
    // PARTIÇÕES: SUBTOTAL (DESCONTO POR VALOR DO CARRINHO)
    // ============================================================================

    @Test
    @DisplayName("ID 13 - Subtotal ≤ R$ 500 (sem desconto)")
    void testSubtotalAte500SemDesconto() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("400.00"),
                                       new BigDecimal("1.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert
        assertThat(custoTotal)
            .as("Subtotal ≤ R$ 500 não deve ter desconto por valor")
            .isEqualByComparingTo(new BigDecimal("400.00"));
    }

    @Test
    @DisplayName("ID 14 - Subtotal > R$ 500 e ≤ R$ 1000 (desconto 10%)")
    void testSubtotal500a1000ComDesconto10Porcento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("600.00"),
                                       new BigDecimal("1.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal: 600, Desconto 10%: 60, Final: 540
        BigDecimal custoEsperado = new BigDecimal("540.00");
        assertThat(custoTotal)
            .as("Subtotal > R$ 500 deve ter 10%% de desconto")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 15 - Subtotal > R$ 1000 (desconto 20%)")
    void testSubtotalMaior1000ComDesconto20Porcento() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto Caro", new BigDecimal("1200.00"),
                                       new BigDecimal("1.0"), false, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal: 1200, Desconto 20%: 240, Final: 960
        BigDecimal custoEsperado = new BigDecimal("960.00");
        assertThat(custoTotal)
            .as("Subtotal > R$ 1000 deve ter 20%% de desconto")
            .isEqualByComparingTo(custoEsperado);
    }

    // ============================================================================
    // PARTIÇÕES: REGIÃO
    // ============================================================================

    @Test
    @DisplayName("ID 16 - Região Sudeste (multiplicador 1.00)")
    void testRegiaoSudeste() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete base sem multiplicador adicional
        BigDecimal custoEsperado = new BigDecimal("124.00"); // 100 + (6*2 + 12)
        assertThat(custoTotal)
            .as("Região Sudeste deve ter multiplicador 1.00")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 17 - Região Sul (multiplicador 1.05)")
    void testRegiaoSul() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUL);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 24 * 1.05 = 25.20, Total: 125.20
        BigDecimal custoEsperado = new BigDecimal("125.20");
        assertThat(custoTotal)
            .as("Região Sul deve ter multiplicador 1.05")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 18 - Região Nordeste (multiplicador 1.10)")
    void testRegiaoNordeste() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.NORDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 24 * 1.10 = 26.40, Total: 126.40
        BigDecimal custoEsperado = new BigDecimal("126.40");
        assertThat(custoTotal)
            .as("Região Nordeste deve ter multiplicador 1.10")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 19 - Região Centro-Oeste (multiplicador 1.20)")
    void testRegiaoCentroOeste() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.CENTRO_OESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 24 * 1.20 = 28.80, Total: 128.80
        BigDecimal custoEsperado = new BigDecimal("128.80");
        assertThat(custoTotal)
            .as("Região Centro-Oeste deve ter multiplicador 1.20")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 20 - Região Norte (multiplicador 1.30)")
    void testRegiaoNorte() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.NORTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 24 * 1.30 = 31.20, Total: 131.20
        BigDecimal custoEsperado = new BigDecimal("131.20");
        assertThat(custoTotal)
            .as("Região Norte deve ter multiplicador 1.30")
            .isEqualByComparingTo(custoEsperado);
    }

    // ============================================================================
    // PARTIÇÕES: TIPO DE CLIENTE
    // ============================================================================

    @Test
    @DisplayName("ID 22 - Cliente Ouro (desconto 100% no frete)")
    void testClienteOuroFreteGratis() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.OURO, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("20.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Apenas subtotal, sem frete
        assertThat(custoTotal)
            .as("Cliente Ouro deve ter frete grátis (100%% de desconto)")
            .isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("ID 23 - Cliente Prata (desconto 50% no frete)")
    void testClientePrataDesconto50PorcentoFrete() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.PRATA, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete: 24 / 2 = 12, Total: 112
        BigDecimal custoEsperado = new BigDecimal("112.00");
        assertThat(custoTotal)
            .as("Cliente Prata deve ter 50%% de desconto no frete")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 24 - Cliente Bronze (sem desconto no frete)")
    void testClienteBronzeSemDescontoFrete() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produto = criarProduto("Produto", new BigDecimal("100.00"),
                                       new BigDecimal("6.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produto, 1);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Frete integral: 24, Total: 124
        BigDecimal custoEsperado = new BigDecimal("124.00");
        assertThat(custoTotal)
            .as("Cliente Bronze não deve ter desconto no frete")
            .isEqualByComparingTo(custoEsperado);
    }

    // ============================================================================
    // PARTIÇÕES: PRODUTO FRÁGIL
    // ============================================================================

    @Test
    @DisplayName("ID 26 - Produto Frágil (taxa adicional R$ 5,00/unidade)")
    void testProdutoFragilComTaxaAdicional() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produtoFragil = criarProduto("Produto Frágil", new BigDecimal("100.00"),
                                             new BigDecimal("2.0"), true, TipoProduto.ELETRONICO);
        adicionarItem(carrinho, produtoFragil, 2); // 2 unidades

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Subtotal: 200, Frete isento (4kg), Taxa frágil: 2 * 5 = 10
        BigDecimal custoEsperado = new BigDecimal("210.00");
        assertThat(custoTotal)
            .as("Produto frágil deve adicionar R$ 5,00 por unidade")
            .isEqualByComparingTo(custoEsperado);
    }

    @Test
    @DisplayName("ID 27 - Produto Não Frágil (sem taxa adicional)")
    void testProdutoNaoFragilSemTaxa() {
        // Arrange
        Cliente cliente = criarCliente(TipoCliente.BRONZE, Regiao.SUDESTE);
        CarrinhoDeCompras carrinho = criarCarrinho(cliente);

        Produto produtoNormal = criarProduto("Produto Normal", new BigDecimal("100.00"),
                                             new BigDecimal("2.0"), false, TipoProduto.LIVRO);
        adicionarItem(carrinho, produtoNormal, 2);

        // Act
        BigDecimal custoTotal = compraService.calcularCustoTotal(carrinho, cliente.getRegiao(), cliente.getTipo());

        // Assert - Apenas subtotal, sem taxa de fragilidade
        BigDecimal custoEsperado = new BigDecimal("200.00");
        assertThat(custoTotal)
            .as("Produto não frágil não deve ter taxa adicional")
            .isEqualByComparingTo(custoEsperado);
    }

    // ============================================================================
    // MÉTODOS AUXILIARES (BUILDERS)
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

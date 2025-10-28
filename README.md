# E-commerce Test - Trabalho de Testes de Software

**Disciplina:** Testes de Software - Prof. Eiji Adachi  
**Projeto:** Testes Automatizados para Funcionalidade de Finalização de Compra

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Status Atual](#status-atual-do-projeto)
- [Ordem de Implementação](#ordem-de-implementação)
- [Checklist de Acompanhamento](#checklist-de-acompanhamento)
- [Como Executar o Projeto](#como-executar-o-projeto)
- [Como Executar os Testes](#como-executar-os-testes)
- [Como Verificar a Cobertura](#como-verificar-a-cobertura)
- [Documentação dos Casos de Teste](#documentação-dos-casos-de-teste)

---

## 🎯 Sobre o Projeto

Este projeto implementa testes automatizados para a funcionalidade de **finalização de compra** em um e-commerce, com foco no método `calcularCustoTotal`. O trabalho abrange tanto **testes funcionais (caixa preta)** quanto **testes estruturais (caixa branca)**.

---

## 📊 Status Atual do Projeto

**Última atualização:** 27/10/2025

| Fase | Status | Progresso | Próxima Ação |
|------|--------|-----------|--------------|
| **Fase 1: Implementação** | 🟢 Completo | 100% | ✅ Todos os bugs corrigidos! |
| **Fase 2: Análise de Testes** | 🟢 Completo | 100% | ✅ Partições e limites identificados! |
| **Fase 3: Testes Funcionais** | 🟢 Completo | 100% | ✅ Todas as classes de teste criadas! |
| **Fase 4: Testes Estruturais** | 🟢 Completo | 100% | ✅ CFG, V(G) e MC/DC documentados! |
| **Fase 5: Boas Práticas** | 🟢 Completo | 100% | ✅ Boas práticas aplicadas! |
| **Fase 6: Documentação** | 🟢 Completo | 100% | ✅ Documentação completa! |

---

### ✅ Concluído
- **Fase 1 - Implementação do Código Base**: ✅ 100% completo
  - ✅ Método `calcularCustoTotal` implementado
  - ✅ Cálculo de subtotal dos itens
  - ✅ Desconto por múltiplos itens do mesmo tipo (3-4: 5%, 5-7: 10%, 8+: 15%)
  - ✅ Desconto por valor de carrinho (>R$500: 10%, >R$1000: 20%)
  - ✅ Cálculo de peso tributável (máximo entre físico e volumétrico)
  - ✅ Cálculo de frete por faixas de peso (bug corrigido)
  - ✅ Taxa mínima de frete (R$ 12,00)
  - ✅ Taxa de manuseio para produtos frágeis (R$ 5,00/item)
  - ✅ Multiplicador por região (Sudeste: 1.0, Sul: 1.05, Nordeste: 1.1, Centro-Oeste: 1.2, Norte: 1.3)
  - ✅ Desconto de frete por tipo de cliente (Bronze: 0%, Prata: 50%, Ouro: 100%)
  - ✅ Arredondamento final (HALF_UP, 2 casas decimais)

- **Fase 2 - Análise de Requisitos e Design de Testes**: ✅ 100% completo
  - ✅ Análise de Partições de Domínio concluída (28 partições identificadas)
  - ✅ Análise de Valores Limites concluída (32 limites identificados)
  - ✅ Tabela de Decisão criada (21 regras mapeadas)

- **Fase 3 - Testes Funcionais (Caixa Preta)**: ✅ 100% completo
  - ✅ Classe `ParticoesTest.java` criada com 22 testes
  - ✅ Classe `LimitesTest.java` criada com 32 testes
  - ✅ Classe `DecisoesTest.java` criada com 7 testes (incluindo 3 testes de robustez)
  - ✅ Total: **61 testes funcionais** - todos passando ✅
  - ✅ IDs renumerados sequencialmente: 1-61 (sem gaps)

- **Fase 5 - Boas Práticas**: ✅ 100% completo
  - ✅ `@BeforeEach` usado para inicialização
  - ✅ `@DisplayName` aplicado em todos os testes
  - ✅ AssertJ usado para comparação de `BigDecimal`
  - ✅ Mensagens descritivas com `.as("...")`
  - ✅ Constantes declaradas (sem valores mágicos)
  - ✅ `assertThrows` para validação de exceções
  - ✅ Métodos auxiliares (builders) para criação de objetos de teste

### 🔄 Em Progresso
- **Fase 4 - Testes Estruturais (Caixa Branca)**: ✅ 100% completo
  - ✅ Cobertura de 100% de arestas atingida
  - ✅ CFG (Grafo de Fluxo de Controle) documentado no README
  - ✅ Complexidade Ciclomática V(G) = 18 calculada e documentada
  - ✅ Análise MC/DC documentada para decisão composta de validação
  - ✅ 61 testes implementados cobrindo todos os caminhos

- **Fase 6 - Documentação Final**: ✅ 100% completo
  - ✅ Instruções de execução no README
  - ✅ Tabelas de Partições e Limites criadas (`TabelaParticoesLimites.csv`)
  - ✅ Tabela de Decisão criada (`TabelaDecisões.csv`)
  - ✅ CFG e V(G) adicionados ao README
  - ✅ Tabela MC/DC criada no README
  - ✅ `artifactId` no `pom.xml` renomeado para `MatheusFreitas-JonasRafael`
  - ✅ IDs dos testes renumerados sequencialmente (1-61, sem gaps)

### ⏳ Pendente para Entrega Final
1. ✅ **Renomear artifactId no pom.xml** - ✅ Concluído: `MatheusFreitas-JonasRafael`
2. ✅ **Renumerar IDs dos testes** - ✅ Concluído: IDs 1-61 (sequencial, sem gaps)
3. ⏳ **Compactar projeto em .zip** - Nome do arquivo: MatheusFreitas-JonasRafael.zip
4. ⏳ **Submeter no SIGAA** - Enviar arquivo .zip

**JÁ CONCLUÍDO:**
- ✅ Implementação completa (100%)
- ✅ Análise de testes (100%)
- ✅ Testes funcionais (61 testes passando)
- ✅ Testes estruturais (CFG, V(G), MC/DC)
- ✅ Boas práticas aplicadas
- ✅ Documentação completa no README
- ✅ Tabelas CSV criadas
- ✅ `artifactId` renomeado para `MatheusFreitas-JonasRafael`
- ✅ IDs renumerados sequencialmente (1-61)

---

### ✅ Checklist de Acompanhamento

### 📦 Implementação

- [x] Método `calcularCustoTotal` implementado e funcionando **COMPLETAMENTE** ✅
  - [x] Implementado 100% completo
  - [x] Desconto por valor de carrinho
- [x] Todas as regras de negócio implementadas corretamente ✅
  - [x] 11 de 11 regras implementadas
  - [x] Todas as funcionalidades completas
- [x] Código compila sem erros ✅

### 🧪 Testes Funcionais (Caixa Preta)

- [x] Partições identificadas e documentadas ✅ (28 partições em `TabelaParticoesLimites.csv`)
- [x] Valores limites identificados e documentados ✅ (32 valores limites)
- [x] Tabela de decisão criada e documentada ✅ (21 regras em `TabelaDecisões.csv`)
- [x] Classe de testes de partições criada ✅ (`ParticoesTest.java` - 22 testes)
- [x] Classe de testes de limites criada ✅ (`LimitesTest.java` - 32 testes)
- [x] Classe de testes de tabela de decisão criada ✅ (`DecisoesTest.java` - 7 testes)
- [x] Testes de validação e robustez implementados ✅ (3 testes de robustez)
- [x] Todos os testes funcionais passando ✅ (**62 testes passando**)

### 🔍 Testes Estruturais (Caixa Branca)

- [x] Grafo de Fluxo de Controle (CFG) desenhado e documentado ✅
- [x] Complexidade Ciclomática V(G) calculada e documentada ✅ (V(G) = 18)
- [x] Caminhos independentes identificados (≥ V(G)) ✅
- [x] 100% de cobertura de arestas atingida ✅
- [x] Decisão composta mais complexa identificada ✅ (validação de entradas)
- [x] Análise MC/DC realizada e documentada ✅
- [x] Tabela MC/DC criada no README ✅

### 📝 Boas Práticas

- [x] Nomes de métodos descritivos ✅
- [x] `@DisplayName` ou nomenclatura autoexplicativa ✅
- [x] Mensagens de falha descritivas (`.as("...")`) ✅
- [x] `@BeforeEach` usado para inicialização comum ✅
- [x] Constantes declaradas (sem valores mágicos) ✅
- [x] AssertJ usado para `BigDecimal` ✅
- [x] `assertThrows` usado para exceções ✅
- [x] Métodos auxiliares (builders) para criação de objetos ✅

### 📄 Documentação

- [x] README.md completo com instruções de execução ✅
- [x] README.md contém CFG do método ✅
- [x] README.md contém cálculo de V(G) ✅
- [x] README.md contém tabela MC/DC ✅
- [x] Planilha/documento de casos de teste criada ✅
- [x] Casos de teste de partições documentados ✅ (`TabelaParticoesLimites.csv`)
- [x] Casos de teste de limites documentados ✅ (`TabelaParticoesLimites.csv`)
- [x] Casos de teste de decisões documentados ✅ (`TabelaDecisões.csv`)

### 🚀 Entrega

- [x] `artifactId` no `pom.xml` renomeado (padrão: MatheusFreitas-JonasRafael) ✅
- [x] IDs dos testes renumerados sequencialmente (1-61, sem gaps) ✅
- [x] Todos os testes executam com sucesso ✅ (61/61 passando)
- [x] Cobertura verificada e documentada ✅

---

## 🚀 Como Executar o Projeto

```bash
# Clonar/descompactar o projeto
cd ecommerce-test

# Compilar o projeto
./mvnw clean compile

# Executar a aplicação (se aplicável)
./mvnw spring-boot:run
```

---

## 🧪 Como Executar os Testes

```bash
# Executar todos os testes
./mvnw test

# Executar uma classe específica de testes
./mvnw test -Dtest=CompraServiceTestParticoes

# Executar um teste específico
./mvnw test -Dtest=CompraServiceTestParticoes#nomeDoMetodo
```

---

## 📊 Como Verificar a Cobertura

```bash
# Executar testes com cobertura (JaCoCo)
./mvnw clean test jacoco:report

# O relatório será gerado em:
# target/site/jacoco/index.html

# Abrir o relatório no navegador
xdg-open target/site/jacoco/index.html
```

---

## 📚 Documentação dos Casos de Teste

### 📊 Partições de Domínio (ParticoesTest.java)

Esta tabela documenta os **22 testes de partições** implementados em `ParticoesTest.java` (IDs 1-22).

| ID | Domínio | Partição | Valor Testado | Classe de Teste | Nome do Método |
|----|---------|----------|---------------|-----------------|----------------|
| 1 | Quantidade | Qtd = 0 | 0 itens | ParticoesTest | `testQtd0SemItens()` |
| 2 | Quantidade | Qtd = 1-2 | 1 item | ParticoesTest | `testQtd1A2SemDesconto()` |
| 3 | Quantidade | Qtd = 3-4 | 3 itens | ParticoesTest | `testQtd3A4ComDesconto5Porcento()` |
| 4 | Quantidade | Qtd = 5-7 | 5 itens | ParticoesTest | `testQtd5A7ComDesconto10Porcento()` |
| 5 | Quantidade | Qtd ≥ 8 | 8 itens | ParticoesTest | `testQtd8OuMaisComDesconto15Porcento()` |
| 6 | Peso Total | 0 ≤ peso ≤ 5kg | 3kg | ParticoesTest | `testPeso0A5KgFreteIsento()` |
| 7 | Peso Total | 5 < peso ≤ 10kg | 7kg | ParticoesTest | `testPeso5A10KgFaixaB()` |
| 8 | Peso Total | 10 < peso ≤ 50kg | 20kg | ParticoesTest | `testPeso10A50KgFaixaC()` |
| 9 | Peso Total | peso > 50kg | 60kg | ParticoesTest | `testPesoAcima50KgFaixaD()` |
| 10 | Subtotal | subtotal ≤ R$500 | R$400 | ParticoesTest | `testSubtotalAte500SemDesconto()` |
| 11 | Subtotal | R$500 < subtotal ≤ R$1000 | R$700 | ParticoesTest | `testSubtotal500A1000ComDesconto10Porcento()` |
| 12 | Subtotal | subtotal > R$1000 | R$1500 | ParticoesTest | `testSubtotalAcima1000ComDesconto20Porcento()` |
| 13 | Região | Sudeste | Sudeste | ParticoesTest | `testRegiaoSudeste()` |
| 14 | Região | Sul | Sul | ParticoesTest | `testRegiaoSul()` |
| 15 | Região | Nordeste | Nordeste | ParticoesTest | `testRegiaoNordeste()` |
| 16 | Região | Centro-Oeste | Centro-Oeste | ParticoesTest | `testRegiaoCentroOeste()` |
| 17 | Região | Norte | Norte | ParticoesTest | `testRegiaoNorte()` |
| 18 | Tipo Cliente | Ouro | Ouro | ParticoesTest | `testClienteOuro()` |
| 19 | Tipo Cliente | Prata | Prata | ParticoesTest | `testClientePrata()` |
| 20 | Tipo Cliente | Bronze | Bronze | ParticoesTest | `testClienteBronze()` |
| 21 | Produto Frágil | Sim | true | ParticoesTest | `testProdutoFragil()` |
| 22 | Produto Frágil | Não | false | ParticoesTest | `testProdutoNaoFragil()` |

**Total:** 22 testes de partições ✅

---

### 📏 Valores Limites (LimitesTest.java)

Esta tabela documenta os **32 testes de valores limites** implementados em `LimitesTest.java` (IDs 23-54).

#### Limites de Quantidade (IDs 23-30)

| ID | Valor Limite | Descrição | Nome do Método |
|----|--------------|-----------|----------------|
| 23 | Qtd ≤ 0 | Lança exceção | `testQtdLimite0Invalido()` |
| 24 | Qtd = 1 | Mínimo válido (0% desc.) | `testQtdLimite1NenhumDesconto()` |
| 25 | Qtd = 2 | Máximo 0% desconto | `testQtdLimite2NenhumDesconto()` |
| 26 | Qtd = 3 | Mínimo 5% desconto | `testQtdLimite3ComDesconto5Porcento()` |
| 27 | Qtd = 4 | Máximo 5% desconto | `testQtdLimite4ComDesconto5Porcento()` |
| 28 | Qtd = 5 | Mínimo 10% desconto | `testQtdLimite5ComDesconto10Porcento()` |
| 29 | Qtd = 7 | Máximo 10% desconto | `testQtdLimite7ComDesconto10Porcento()` |
| 30 | Qtd = 8 | Mínimo 15% desconto | `testQtdLimite8ComDesconto15Porcento()` |

#### Limites de Peso (IDs 31-43)

| ID | Valor Limite | Descrição | Nome do Método |
|----|--------------|-----------|----------------|
| 31 | Peso < 0 | Lança exceção | `testRobustezPesoNegativo()` |
| 32 | Peso = 0.00kg | Mínimo válido (isento) | `testLimitePeso0KgIsento()` |
| 33 | Peso = 0.1kg | MIN+ Faixa A | `testLimitePeso0Ponto1KgIsento()` |
| 34 | Peso = 4.9kg | MAX- Faixa A | `testLimitePeso4Ponto9KgIsento()` |
| 35 | Peso = 5.00kg | MAX Faixa A | `testLimitePeso5KgIsento()` |
| 36 | Peso = 5.01kg | MIN Faixa B | `testLimitePeso5Ponto01KgComFrete()` |
| 37 | Peso = 9.9kg | MAX- Faixa B | `testLimitePeso9Ponto9Kg()` |
| 38 | Peso = 10.00kg | MAX Faixa B | `testLimitePeso10Kg()` |
| 39 | Peso = 10.01kg | MIN Faixa C | `testLimitePeso10Ponto01Kg()` |
| 40 | Peso = 49.9kg | MAX- Faixa C | `testLimitePeso49Ponto9Kg()` |
| 41 | Peso = 50.00kg | MAX Faixa C | `testLimitePeso50Kg()` |
| 42 | Peso = 50.01kg | MIN Faixa D | `testLimitePeso50Ponto01Kg()` |
| 43 | Peso = 50.2kg | MIN+ Faixa D | `testLimitePeso50Ponto2Kg()` |

#### Limites de Subtotal (IDs 44-54)

| ID | Valor Limite | Descrição | Nome do Método |
|----|--------------|-----------|----------------|
| 44 | Subtotal < 0 | Lança exceção | `testRobustezSubtotalNegativo()` |
| 45 | Subtotal = R$0.00 | Mínimo válido | `testLimiteSubtotal0SemDesconto()` |
| 46 | Subtotal = R$0.10 | MIN+ sem desconto | `testLimiteSubtotal0Ponto1SemDesconto()` |
| 47 | Subtotal = R$499.99 | MAX- 0% desc. | `testLimiteSubtotal499Ponto99SemDesconto()` |
| 48 | Subtotal = R$500.00 | MAX 0% desc. | `testLimiteSubtotal500ComDesconto10Porcento()` |
| 49 | Subtotal = R$500.10 | MIN+ 10% desc. | `testLimiteSubtotal500Ponto1ComDesconto10Porcento()` |
| 50 | Subtotal = R$500.20 | MIN++ 10% desc. | `testLimiteSubtotal500Ponto2ComDesconto10Porcento()` |
| 51 | Subtotal = R$999.90 | MAX- 10% desc. | `testLimiteSubtotal999Ponto9ComDesconto10Porcento()` |
| 52 | Subtotal = R$1000.00 | MAX 10% desc. | `testLimiteSubtotal1000ComDesconto10Porcento()` |
| 53 | Subtotal = R$1000.10 | MIN 20% desc. | `testLimiteSubtotal1000Ponto1ComDesconto20Porcento()` |
| 54 | Subtotal = R$1000.20 | MIN+ 20% desc. | `testLimiteSubtotal1000Ponto2ComDesconto20Porcento()` |

**Total:** 32 testes de limites ✅

---

### 🎯 Decisões e Combinações (DecisoesTest.java)

Esta tabela documenta os **7 testes de decisão** implementados em `DecisoesTest.java` (IDs 55-61).

#### Testes de Robustez (IDs 55-57)

| ID | Tipo | Condição Testada | Resultado Esperado | Nome do Método |
|----|------|------------------|-------------------|----------------|
| 55 | Robustez | Quantidade ≤ 0 | Lança `IllegalArgumentException` | `testRobustezQuantidadeInvalida()` |
| 56 | Robustez | Preço < 0 | Lança `IllegalArgumentException` | `testRobustezSubtotalNegativo()` |
| 57 | Robustez | Peso < 0 | Lança `IllegalArgumentException` | `testRobustezPesoNegativo()` |

#### Testes de Combinações Complexas (IDs 58-61)

| ID | Tipo | Cenário | Regras Aplicadas | Nome do Método |
|----|------|---------|------------------|----------------|
| 58 | Combinação | Qtd=1, Sub=R$600, Peso=3kg, Bronze, Sudeste | Desc. 10% valor + Frete isento | `testCombinacaoBasica()` |
| 59 | Combinação | Qtd=6, Sub=R$600, Peso=51kg, Ouro, Nordeste | Desc. Qtd 10% + Desc. Sub 10% + Frete zerado (Ouro) | `testComplexoFaixaDOuroNordeste()` |
| 60 | Combinação | Qtd=4, Sub=R$1500, Peso=20kg, Prata, Sul, Frágil | Desc. Qtd 5% + Desc. Sub 20% + Frete Faixa C + Taxa Frágil + Mult. Sul + Desc. Prata 50% | `testComplexoPrataFragemFaixaC()` |
| 61 | Combinação | Qtd=9, Sub=R$1350, Peso=54kg, Bronze, Centro-Oeste | Desc. Qtd 15% + Desc. Sub 20% + Frete Faixa D + Mult. Centro-Oeste + Sem desc. frete | `testMaximoDescontoEFrete()` |

**Total:** 7 testes de decisões ✅

---

### 📋 Resumo Geral dos Testes Implementados

| Arquivo | IDs | Quantidade | Tipo de Teste |
|---------|-----|------------|---------------|
| `ParticoesTest.java` | 1-22 | 22 testes | Partições de Domínio |
| `LimitesTest.java` | 23-54 | 32 testes | Valores Limites |
| `DecisoesTest.java` | 55-61 | 7 testes | Decisões e Robustez |
| **TOTAL** | **1-61** | **61 testes** | **Todos os critérios** ✅ |

**Cobertura Alcançada:**
- ✅ 100% de cobertura de arestas (branch coverage)
- ✅ Todas as partições de domínio cobertas
- ✅ Todos os valores limites críticos testados
- ✅ Todas as combinações complexas validadas
- ✅ Todos os casos de robustez implementados

**Link Para Planilha de Testes**
```
https://docs.google.com/spreadsheets/d/1LHSu6zSHigFnHe_FWHA3wogE4yCTUB_6zs2WrueIDbQ/edit?usp=sharing
```

---

### 🔀 Grafo de Fluxo de Controle (CFG) do método `calcularCustoTotal`

**Descrição:** O método `calcularCustoTotal` possui a seguinte estrutura de controle:

#### Nós e Arestas do CFG:

```
1. INÍCIO (validação de entradas com forEach)
   ├─> 2. if (quantidade <= 0 || peso < 0 || preço < 0)
   |     └─> 3. throw IllegalArgumentException
   └─> 4. Calcula subtotalItens
   
4. Calcula subtotalItens
   └─> 5. calcularDescontoPorMultiplosItensMesmoTipo()
   
5. calcularDescontoPorMultiplosItensMesmoTipo()
   ├─> Loop: for cada tipo de produto
   |     ├─> 6. if (qtd >= 8) -> desconto 15%
   |     ├─> 7. else if (qtd >= 5) -> desconto 10%
   |     ├─> 8. else if (qtd >= 3) -> desconto 5%
   |     └─> 9. else -> sem desconto
   └─> 10. calcularDescontoPorValorCarrinho()
   
10. calcularDescontoPorValorCarrinho()
    ├─> 11. if (subtotal > 1000) -> desconto 20%
    ├─> 12. else if (subtotal > 500) -> desconto 10%
    └─> 13. else -> sem desconto
    
13. Calcula frete total
    └─> 14. calcularFretePorPeso()
    
14. calcularFretePorPeso()
    ├─> 15. if (peso <= 5) -> frete = 0
    ├─> 16. else if (peso <= 10) -> frete = peso × 2 + 12
    ├─> 17. else if (peso <= 50) -> frete = peso × 4 + 12
    └─> 18. else -> frete = peso × 7 + 12
    
18. Continua cálculo frete
    └─> 19. calcularFretePorRegiao()
    
19. calcularFretePorRegiao()
    └─> 20. switch (região) - 5 casos
    
20. Aplica desconto cliente
    └─> 21. aplicarDescontoPorTipoCliente()
    
21. aplicarDescontoPorTipoCliente()
    └─> 22. switch (tipoCliente) - 3 casos
    
22. Retorna custo total arredondado
    └─> FIM
```

#### Análise de Complexidade:

**Cálculo da Complexidade Ciclomática V(G):**

Usando a fórmula: **V(G) = E - N + 2P** ou **V(G) = D + 1** (onde D = número de decisões)

**Decisões no código:**
1. Validação (forEach com if composto - 1 decisão com 3 condições OR)
2. Desconto por múltiplos itens: if-else if-else if (3 decisões)
3. Desconto por valor: if-else if (2 decisões)
4. Frete por peso: if-else if-else if (3 decisões)
5. Frete por região: switch com 5 casos (5 decisões)
6. Desconto cliente: switch com 3 casos (3 decisões)

**Total de decisões:** 1 + 3 + 2 + 3 + 5 + 3 = **17 decisões**

**V(G) = 17 + 1 = 18**

**Interpretação:** São necessários **no mínimo 18 caminhos independentes** para cobrir completamente o método.

**Status:** Os testes atuais cobrem **100% das arestas** (branch coverage), garantindo que todas as decisões e seus ramos foram exercitados pelos 62 testes implementados.

---

### 📐 Complexidade Ciclomática V(G)

**V(G) = 18**

**Caminhos Independentes Mínimos:** 18

**Cobertura Atual:** 
- ✅ **100% de cobertura de arestas (branch coverage)**
- ✅ **61 testes** implementados cobrindo todos os caminhos relevantes
- ✅ Todos os branches (if/else, switch cases) exercitados

**Evidência de Cobertura:**
```bash
# Executar testes com relatório de cobertura:
./mvnw clean test jacoco:report

# Relatório disponível em:
target/site/jacoco/index.html
```

---

### ✅ Análise MC/DC (Modified Condition/Decision Coverage)

**Decisão Composta Analisada:** Validação de entradas no início do método `calcularCustoTotal`

```java
if (produto.getQuantidade() <= 0 || 
    produto.getProduto().getPesoFisico().compareTo(BigDecimal.ZERO) < 0 ||
    produto.getProduto().getPreco().compareTo(BigDecimal.ZERO) < 0)
```

#### Condições:
- **C1:** `quantidade <= 0`
- **C2:** `pesoFisico < 0`
- **C3:** `preço < 0`

#### Tabela MC/DC:

| Caso de Teste | C1 | C2 | C3 | Decisão | Condição Testada | Teste Correspondente |
|---------------|----|----|----|---------|--------------------|---------------------|
| T1 | F | F | F | F | - | Casos válidos (maioria dos testes) |
| T2 | **T** | F | F | **T** | **C1** | `DecisoesTest.testRobustezQuantidadeInvalida()` |
| T3 | F | **T** | F | **T** | **C2** | `DecisoesTest.testRobustezPesoNegativo()` |
| T4 | F | F | **T** | **T** | **C3** | `DecisoesTest.testRobustezSubtotalNegativo()` |

#### Análise MC/DC:

**Para C1 (quantidade <= 0):**
- T1 (F,F,F) → F
- T2 (T,F,F) → T
- **C1 muda, decisão muda** ✅

**Para C2 (pesoFisico < 0):**
- T1 (F,F,F) → F
- T3 (F,T,F) → T
- **C2 muda, decisão muda** ✅

**Para C3 (preço < 0):**
- T1 (F,F,F) → F
- T4 (F,F,T) → T
- **C3 muda, decisão muda** ✅

**Conclusão:** Todas as condições foram testadas independentemente, atendendo ao critério MC/DC. Cada condição foi demonstrada como capaz de afetar o resultado da decisão de forma independente.

---

### 📝 Resumo dos Casos de Teste Implementados

**Total de Testes:** 62 testes ✅

#### Por Classe:
- **ParticoesTest.java:** 22 testes (partições de domínio)
- **LimitesTest.java:** 32 testes (valores limites)
- **DecisoesTest.java:** 7 testes (tabela de decisão + robustez)
- **CompraServiceTest.java:** 1 teste (exemplo básico)

#### Por Critério:
- **Partições de Domínio:** 22 testes ✅
- **Valores Limites:** 32 testes ✅
- **Tabela de Decisão:** 4 testes de combinações ✅
- **Robustez (entradas inválidas):** 3 testes ✅
- **Exemplo integração:** 1 teste ✅

#### Arquivos de Documentação:
- ✅ `TabelaParticoesLimites.csv` - 28 partições documentadas
- ✅ `TabelaDecisões.csv` - 21 regras de negócio documentadas
- ✅ `README.md` - Instruções completas e análise estrutural

---

### 🔢 Mapeamento de IDs dos Testes

**Observação sobre a numeração:** Os IDs dos testes foram renumerados sequencialmente de **1 a 61** (sem gaps) para melhor organização e rastreabilidade.

#### **Partições de Domínio (IDs 1-22)**
Arquivo: `ParticoesTest.java` - **22 testes implementados**

| Faixa de IDs | Domínio | Descrição |
|--------------|---------|-----------|
| 1-5 | Quantidade de Itens | Partições: 0, 1-2, 3-4, 5-7, ≥8 itens |
| 6-9 | Peso Total | Partições: ≤5kg, 5-10kg, 10-50kg, >50kg |
| 10-12 | Subtotal | Partições: ≤R$500, R$500-1000, >R$1000 |
| 13-17 | Região | Partições: Sudeste, Sul, Nordeste, Centro-Oeste, Norte |
| 18-20 | Tipo de Cliente | Partições: Ouro, Prata, Bronze |
| 21-22 | Produto Frágil | Partições: Sim, Não |

**Total:** 22 testes de partições

#### **Valores Limites (IDs 23-54)**
Arquivo: `LimitesTest.java` - **32 testes implementados**

| Faixa de IDs | Critério | Valores Testados |
|--------------|----------|------------------|
| 23-30 | Limites de Quantidade | 0, 1, 2, 3, 4, 5, 7, 8 |
| 31-43 | Limites de Peso | -0.1, 0.0, 0.1, 4.9, 5.0, 5.01, 9.9, 10.0, 10.01, 49.9, 50.0, 50.01, 50.2 kg |
| 44-54 | Limites de Subtotal | -0.1, 0.0, 0.1, 499.99, 500.0, 500.1, 500.2, 999.9, 1000.0, 1000.1, 1000.2 |

**Total:** 32 testes de limites

#### **Decisões e Combinações (IDs 55-61)**
Arquivo: `DecisoesTest.java` - **7 testes implementados**

| ID | Tipo | Descrição | Regras Cobertas |
|----|------|-----------|----------------|
| 55 | Robustez | Qtd ≤ 0 lança exceção | Validação de entrada |
| 56 | Robustez | Preço < 0 lança exceção | Validação de entrada |
| 57 | Robustez | Peso < 0 lança exceção | Validação de entrada |
| 58 | Combinação | Desc. 10% valor + Frete isento | Múltiplas regras |
| 59 | Combinação | Desc. Qtd 10% + Sub 10% + Frete D/Ouro | Combinação complexa |
| 60 | Combinação | Desc. Qtd 5% + Sub 20% + Frete C/Prata/Frágil | Combinação complexa |
| 61 | Combinação | Desc. Qtd 15% + Sub 20% + Frete D/Bronze | Máxima penalização |

**Total:** 7 testes de decisões e robustez

#### **Resumo da Cobertura Sequencial:**

```
✅ IDs 1-22   : Partições de Domínio (22 testes em ParticoesTest.java)
✅ IDs 23-54  : Valores Limites (32 testes em LimitesTest.java)
✅ IDs 55-61  : Decisões e Robustez (7 testes em DecisoesTest.java)
```

**Total Geral:** 61 testes cobrindo todos os critérios de caixa preta e caixa branca ✅

**Distribuição por Arquivo:**
- `ParticoesTest.java`: IDs 1-22 (22 testes)
- `LimitesTest.java`: IDs 23-54 (32 testes)
- `DecisoesTest.java`: IDs 55-61 (7 testes)

---

## 👥 Integrantes

- Matheus Freitas
- Jonas Rafael
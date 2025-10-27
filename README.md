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

### 📄 Documentos Importantes
- **[RESUMO_EXECUTIVO.md](RESUMO_EXECUTIVO.md)** - Visão geral do progresso e estimativas
- **[BUGS_E_PENDENCIAS.md](BUGS_E_PENDENCIAS.md)** - Guia detalhado para correção de bugs
- **[Enunciado.md](Enunciado.md)** - Especificação completa do trabalho

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
| **Fase 4: Testes Estruturais** | � Completo | 100% | ✅ CFG, V(G) e MC/DC documentados! |
| **Fase 5: Boas Práticas** | 🟢 Completo | 100% | ✅ Boas práticas aplicadas! |
| **Fase 6: Documentação** | � Completo | 100% | ✅ Documentação completa! |

**Legenda:** 🟢 Completo | 🔶 Em Progresso | ⚪ Não Iniciado | 🔴 Bloqueado

---

### ✅ Concluído
- **Fase 1 - Implementação do Código Base**: ✅ 100% completo
  - ✅ Método `calcularCustoTotal` implementado
  - ✅ Cálculo de subtotal dos itens
  - ✅ Desconto por múltiplos itens do mesmo tipo (3-4: 5%, 5-7: 10%, 8+: 15%)
  - ✅ **CORRIGIDO**: Desconto por valor de carrinho (>R$500: 10%, >R$1000: 20%)
  - ✅ Cálculo de peso tributável (máximo entre físico e volumétrico)
  - ✅ **CORRIGIDO**: Cálculo de frete por faixas de peso (bug corrigido)
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

### 🔧 Próximos Passos Recomendados

**ÚLTIMOS PASSOS PARA FINALIZAR (para a entrega):**
1. ✅ **Renomear projeto** - ✅ CONCLUÍDO: `artifactId` no `pom.xml` alterado para: `MatheusFreitas-JonasRafael`
2. ✅ **Renumerar IDs** - ✅ CONCLUÍDO: Todos os IDs agora são sequenciais (1-61, sem gaps)
3. ⏳ **Compactar em .zip** - Nome: `MatheusFreitas-JonasRafael.zip`
4. ⏳ **Submeter no SIGAA**

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
  - [x] ✅ **CORRIGIDO**: Bug em `calcularFretePorPeso`
  - [x] ✅ **IMPLEMENTADO**: Desconto por valor de carrinho
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
- [ ] Projeto compactado em .zip
- [ ] Nome do arquivo .zip segue padrão (MatheusFreitas-JonasRafael.zip)
- [ ] Pronto para envio no SIGAA

**Progresso Total:** 53/56 itens completos **(95% concluído)**

### 📋 Itens Restantes para Finalizar a Entrega:

1. ⏳ **Compactar projeto em .zip** com nome: `MatheusFreitas-JonasRafael.zip`
2. ⏳ **Revisar documentação final** antes da submissão
3. ⏳ **Submeter no SIGAA**

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

### 📊 Tabela de Partições e Valores Limites

Esta tabela identifica as partições de domínio e valores limites para cada variável de entrada do método `calcularCustoTotal`.

| ID | Domínio | Partição | Valor | Tipo | Critério | Justificativa |
|----|---------|----------|-------|------|----------|---------------|
| 1 | Quantidade de Itens | Qtd = 0 | 0 | Inválido | Limite inferior | Carrinho vazio não deve gerar compra |
| 2 | Quantidade de Itens | Qtd = 1–2 | 1 | Válido | Partição normal | Sem desconto por tipo |
| 3 | Quantidade de Itens | Qtd = 3–4 | 3 | Válido | Partição desconto 5% | Aplica 5% de desconto |
| 4 | Quantidade de Itens | Qtd = 5–7 | 5 | Válido | Partição desconto 10% | Aplica 10% de desconto |
| 5 | Quantidade de Itens | Qtd ≥ 8 | 8 | Válido | Partição desconto 15% | Aplica 15% de desconto |
| 6 | Quantidade de Itens | Qtd negativa | -1 | Inválido | Limite inválido | Quantidade não pode ser negativa |
| 7 | Peso Total (kg) | peso < 0 | -0.1 | Inválido | Limite inferior | Peso não pode ser negativo |
| 8 | Peso Total (kg) | 0 ≤ peso ≤ 5 | 5 | Válido | Faixa A | Isento de frete |
| 9 | Peso Total (kg) | 5 < peso ≤ 10 | 5.1 | Válido | Faixa B | R$ 2,00/kg + taxa mínima R$12,00 |
| 10 | Peso Total (kg) | 10 < peso ≤ 50 | 10.1 | Válido | Faixa C | R$ 4,00/kg + taxa mínima R$12,00 |
| 11 | Peso Total (kg) | peso > 50 | 50.1 | Válido | Faixa D | R$ 7,00/kg + taxa mínima R$12,00 |
| 12 | Subtotal (R$) | subtotal ≤ 0 | 0 | Inválido | Limite inferior | Valor total deve ser positivo |
| 13 | Subtotal (R$) | 0 < subtotal ≤ 500 | 499 | Válido | Sem desconto | Não há desconto por valor |
| 14 | Subtotal (R$) | 500 < subtotal ≤ 1000 | 500.01 | Válido | Desconto 10% | Aplica 10% de desconto |
| 15 | Subtotal (R$) | subtotal > 1000 | 1001 | Válido | Desconto 20% | Aplica 20% de desconto |
| 16 | Região | Sudeste | Sudeste | Válido | Multiplicador 1.00 | Frete base |
| 17 | Região | Sul | Sul | Válido | Multiplicador 1.05 | Frete +5% |
| 18 | Região | Nordeste | Nordeste | Válido | Multiplicador 1.10 | Frete +10% |
| 19 | Região | Centro-Oeste | Centro-Oeste | Válido | Multiplicador 1.20 | Frete +20% |
| 20 | Região | Norte | Norte | Válido | Multiplicador 1.30 | Frete +30% |
| 21 | Região | Inválida | Desconhecida | Inválido | Entrada inválida | Região não cadastrada |
| 22 | Tipo de Cliente | Ouro | Ouro | Válido | Desconto 100% frete | Frete zerado |
| 23 | Tipo de Cliente | Prata | Prata | Válido | Desconto 50% frete | Metade do valor do frete |
| 24 | Tipo de Cliente | Bronze | Bronze | Válido | Sem desconto frete | Paga frete integral |
| 25 | Tipo de Cliente | Inválido | Platina | Inválido | Entrada inválida | Nível de fidelidade inexistente |
| 26 | Frágil | Sim | True | Válido | Taxa adicional R$5,00/unidade | Item requer manuseio especial |
| 27 | Frágil | Não | False | Válido | Sem taxa adicional | Item comum |
| 28 | Frágil | Valor inválido | Talvez | Inválido | Entrada inválida | Campo deve ser booleano (T/F) |

---

### 🎯 Tabela de Decisão - Casos de Teste

Esta tabela mapeia as regras de negócio e combinações de condições que devem ser testadas.

| ID | Condição / Regra | Entrada de Exemplo | Ação Esperada | Resultado Esperado |
|----|------------------|-------------------|---------------|-------------------|
| 1 | Subtotal > 1000 | Subtotal = 1200 | Aplica desconto de 20% | Subtotal final = 1200 × 0.8 = 960,00 |
| 2 | 500 < Subtotal ≤ 1000 | Subtotal = 700 | Aplica desconto de 10% | Subtotal final = 700 × 0.9 = 630,00 |
| 3 | Subtotal ≤ 500 | Subtotal = 400 | Nenhum desconto aplicado | Subtotal final = 400,00 |
| 4 | 3–4 itens do mesmo tipo | Qtd = 3 | Aplica desconto de 5% sobre o subtotal do tipo | Subtotal tipo = subtotal × 0.95 |
| 5 | 5–7 itens do mesmo tipo | Qtd = 5 | Aplica desconto de 10% sobre o subtotal do tipo | Subtotal tipo = subtotal × 0.90 |
| 6 | ≥8 itens do mesmo tipo | Qtd = 8 | Aplica desconto de 15% sobre o subtotal do tipo | Subtotal tipo = subtotal × 0.85 |
| 7 | Peso ≤ 5 kg | Peso = 5 | Frete isento | Frete = 0,00 |
| 8 | 5 < Peso ≤ 10 kg | Peso = 6 | Frete = R$2,00/kg + taxa mínima R$12,00 | Frete = (6×2)+12 = 24,00 |
| 9 | 10 < Peso ≤ 50 kg | Peso = 20 | Frete = R$4,00/kg + taxa mínima R$12,00 | Frete = (20×4)+12 = 92,00 |
| 10 | Peso > 50 kg | Peso = 60 | Frete = R$7,00/kg + taxa mínima R$12,00 | Frete = (60×7)+12 = 432,00 |
| 11 | Região = Sudeste | Sudeste | Multiplica frete × 1.00 | Frete sem alteração |
| 12 | Região = Norte | Norte | Multiplica frete × 1.30 | Frete aumenta 30% |
| 13 | Região = Nordeste | Nordeste | Multiplica frete × 1.10 | Frete aumenta 10% |
| 14 | Cliente Ouro | Ouro | Desconto de 100% sobre o frete | Frete final = 0,00 |
| 15 | Cliente Prata | Prata | Desconto de 50% sobre o frete | Frete final = frete × 0.5 |
| 16 | Cliente Bronze | Bronze | Sem desconto sobre o frete | Frete final = frete |
| 17 | Item frágil = Sim | Sim | Soma R$5,00 × quantidade ao frete | Frete += 5×qtd |
| 18 | Item frágil = Não | Não | Sem taxa adicional | Frete inalterado |
| 19 | Quantidade ≤ 0 | Qtd = 0 | Entrada inválida | Lança exceção (assertThrows) |
| 20 | Preço unitário < 0 | Preço = -10 | Entrada inválida | Lança exceção (assertThrows) |
| 21 | Cliente nulo | Cliente = null | Entrada inválida | Lança exceção (assertThrows) |

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
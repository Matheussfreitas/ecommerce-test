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

**Última atualização:** 21/10/2025

| Fase | Status | Progresso | Próxima Ação |
|------|--------|-----------|--------------|
| **Fase 1: Implementação** | � Completo | 100% | ✅ Todos os bugs corrigidos! |
| **Fase 2: Análise de Testes** | ⚪ Não Iniciado | 0% | Identificar partições e limites |
| **Fase 3: Testes Funcionais** | ⚪ Não Iniciado | 0% | Criar classes de teste |
| **Fase 4: Testes Estruturais** | ⚪ Não Iniciado | 0% | Desenhar CFG e calcular V(G) |
| **Fase 5: Boas Práticas** | ⚪ Não Iniciado | 0% | Refatorar e aplicar padrões |
| **Fase 6: Documentação** | ⚪ Não Iniciado | 0% | Completar README e planilha |

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

### 🔄 Em Progresso
- **Fase 2 - Análise de Requisitos e Design de Testes**: Não iniciado
- **Fase 3 - Testes Funcionais (Caixa Preta)**: Parcialmente iniciado (1 teste básico criado)

### ⏳ Pendente
- Análise de Partições de Domínio
- Análise de Valores Limites
- Tabela de Decisão
- Análise Estrutural (CFG, Complexidade Ciclomática)
- Testes MC/DC
- Cobertura de 100% de arestas
- Documentação completa

#### 🔧 **PENDENTE:**

- **Teste básico incompleto**: O teste em `CompraServiceTest.java` precisa ser completado com produtos e itens configurados corretamente

### 🔧 Próximos Passos Recomendados

**PRIORIDADE ALTA (fazer agora):**
1. ✅ ~~Corrigir o bug em `calcularFretePorPeso`~~ **CONCLUÍDO**
2. ✅ ~~Implementar "desconto por valor de carrinho"~~ **CONCLUÍDO**
3. 🎯 **PRÓXIMO**: Testar manualmente se os cálculos estão corretos após as correções
4. 🎯 Iniciar Fase 2: Análise de Partições e Valores Limites

**PRIORIDADE MÉDIA:**
5. Criar tabela de decisão
6. Desenhar o Grafo de Fluxo de Controle (CFG)
7. Implementar classes de testes estruturados (Partições, Limites, Tabela de Decisão)

**PRIORIDADE NORMAL:**
8. Atingir 100% de cobertura de arestas
9. Implementar testes MC/DC
10. Documentar todos os casos de teste

---

## 🔢 Ordem de Implementação

### **Fase 1: Implementação do Código Base** 
*Esforço: Alto | Importância: Crítica | Prioridade: 1*

- [x] **1.1** Implementar o método `calcularCustoTotal` no `CompraService` ✅ **CONCLUÍDO**
  - [x] Implementar cálculo do subtotal dos itens
  - [x] Implementar desconto por múltiplos itens do mesmo tipo
  - [x] Desconto por valor de carrinho (>R$500: 10%, >R$1000: 20%)
  - [x] Implementar cálculo do peso tributável (físico vs cúbico)
  - [x] Cálculo do frete base por faixas de peso
  - [x] Implementar taxa mínima de frete (R$ 12,00)
  - [x] Implementar taxa de manuseio para itens frágeis
  - [x] Implementar multiplicador por região
  - [x] Implementar desconto de frete por nível do cliente
  - [x] Implementar arredondamento final (half-up, 2 casas decimais)

### **Fase 2: Análise de Requisitos e Design de Testes**
*Esforço: Médio | Importância: Crítica | Prioridade: 2*

- [ ] **2.1** Análise de Partições de Domínio (Caixa Preta)
  - Identificar partições para quantidade de itens
  - Identificar partições para peso (físico e cúbico)
  - Identificar partições para valor do carrinho
  - Identificar partições para tipo de cliente (Bronze/Prata/Ouro)
  - Identificar partições para região (Sudeste/Sul/Nordeste/Centro-Oeste/Norte)
  - Identificar partições para tipos de produto
  - Identificar partições para produtos frágeis vs não-frágeis

- [ ] **2.2** Análise de Valores Limites (Caixa Preta)
  - Definir limites para desconto por múltiplos itens (3, 4, 5, 7, 8)
  - Definir limites para desconto por valor (R$ 500,00, R$ 1000,00)
  - Definir limites para faixas de peso (5kg, 10kg, 50kg)
  - Definir limites para cálculo de peso cúbico

- [ ] **2.3** Criar Tabela de Decisão (Caixa Preta)
  - Mapear condições: valor do carrinho, quantidade de itens por tipo, peso total, nível do cliente, região
  - Definir ações/resultados para cada combinação de regras
  - Identificar regras de negócio complexas

- [ ] **2.4** Análise Estrutural (Caixa Branca)
  - Desenhar o **Grafo de Fluxo de Controle (CFG)** do método `calcularCustoTotal`
  - Calcular a **Complexidade Ciclomática V(G)**
  - Identificar caminhos independentes (≥ V(G))
  - Identificar a decisão composta mais complexa para análise MC/DC

### **Fase 3: Implementação dos Testes Funcionais (Caixa Preta)**
*Esforço: Alto | Importância: Alta | Prioridade: 3*

- [ ] **3.1** Criar classe `CompraServiceTestParticoes.java`
  - Implementar testes para cada partição identificada
  - Usar `@DisplayName` ou nomenclatura descritiva
  - Adicionar mensagens de falha com `.as("descrição")`
  - Documentar casos na planilha/tabela

- [ ] **3.2** Criar classe `CompraServiceTestLimites.java`
  - Implementar testes para valores limites (on-point, off-point)
  - Testar valores exatamente nos limites e adjacentes
  - Documentar casos na planilha/tabela

- [ ] **3.3** Criar classe `CompraServiceTestTabelaDecisao.java`
  - Implementar testes para cada regra da tabela de decisão
  - Garantir cobertura de todas as combinações relevantes
  - Documentar casos na planilha/tabela

- [ ] **3.4** Criar testes de validação e robustez
  - Testar quantidade ≤ 0
  - Testar preços negativos
  - Testar cliente nulo
  - Testar carrinho vazio ou nulo
  - Usar `assertThrows` para exceções esperadas

### **Fase 4: Implementação dos Testes Estruturais (Caixa Branca)**
*Esforço: Alto | Importância: Alta | Prioridade: 4*

- [ ] **4.1** Atingir 100% de cobertura de arestas (branch coverage)
  - Executar testes com coverage
  - Identificar branches não cobertos
  - Criar testes adicionais para cobrir todos os branches

- [ ] **4.2** Implementar testes MC/DC
  - Criar testes para a decisão composta mais complexa
  - Demonstrar que cada condição individual influencia o resultado
  - Documentar tabela "decisão × condições × casos de teste" no README

- [ ] **4.3** Validar caminhos independentes
  - Garantir que V(G) caminhos independentes são cobertos
  - Documentar no README a relação entre casos de teste e caminhos

### **Fase 5: Boas Práticas e Refatoração**
*Esforço: Médio | Importância: Média | Prioridade: 5*

- [ ] **5.1** Aplicar boas práticas
  - Usar `@BeforeEach` para inicialização comum
  - Declarar constantes para valores mágicos
  - Usar AssertJ para comparar `BigDecimal`
  - Aplicar **JUnit 5 Parameterized Tests** onde adequado

- [ ] **5.2** Revisar nomenclatura e organização
  - Validar nomes descritivos de métodos de teste
  - Organizar imports e estrutura de classes
  - Adicionar comentários quando necessário

### **Fase 6: Documentação Final**
*Esforço: Médio | Importância: Alta | Prioridade: 6*

- [ ] **6.1** Completar o README.md
  - Instruções de como executar o projeto
  - Instruções de como executar os testes
  - Instruções de como verificar cobertura
  - Incluir CFG, V(G) e análise de caminhos
  - Incluir tabela MC/DC

- [ ] **6.2** Criar planilha/documento de casos de teste
  - Colunas: ID, Entrada, Resultado Esperado, Critério Coberto
  - Incluir todos os casos de teste (partições, limites, decisão)
  - Relacionar cada teste com o critério correspondente

- [ ] **6.3** Validação final
  - Executar todos os testes
  - Verificar cobertura de 100% de arestas
  - Revisar documentação
  - Renomear projeto no `pom.xml` (artifactId)

- [ ] **6.4** Preparar entrega
  - Compactar projeto em formato .zip
  - Validar que todos os arquivos necessários estão incluídos
  - Nomear arquivo seguindo padrão: nome1-nome2.zip

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
- [x] Código compila sem erros

### 🧪 Testes Funcionais (Caixa Preta)

- [ ] Partições identificadas e documentadas
- [ ] Valores limites identificados e documentados
- [ ] Tabela de decisão criada e documentada
- [ ] Classe de testes de partições criada
- [ ] Classe de testes de limites criada
- [ ] Classe de testes de tabela de decisão criada
- [ ] Testes de validação e robustez implementados
- [ ] Todos os testes funcionais passando

### 🔍 Testes Estruturais (Caixa Branca)

- [ ] Grafo de Fluxo de Controle (CFG) desenhado
- [ ] Complexidade Ciclomática V(G) calculada
- [ ] Caminhos independentes identificados (≥ V(G))
- [ ] 100% de cobertura de arestas atingida
- [ ] Decisão composta mais complexa identificada
- [ ] Análise MC/DC realizada e documentada
- [ ] Tabela MC/DC criada no README
- [ ] Todos os testes estruturais passando

### 📝 Boas Práticas

- [ ] Nomes de métodos descritivos
- [ ] `@DisplayName` ou nomenclatura autoexplicativa
- [ ] Mensagens de falha descritivas (`.as("...")`)
- [ ] `@BeforeEach` usado para inicialização comum
- [ ] Constantes declaradas (sem valores mágicos)
- [ ] AssertJ usado para `BigDecimal`
- [ ] `assertThrows` usado para exceções
- [ ] Testes parametrizados aplicados onde adequado

### 📄 Documentação

- [ ] README.md completo com instruções de execução
- [ ] README.md contém CFG do método
- [ ] README.md contém cálculo de V(G)
- [ ] README.md contém tabela MC/DC
- [ ] Planilha/documento de casos de teste criada
- [ ] Todos os casos de teste documentados (ID, entrada, resultado, critério)
- [ ] Relação entre testes e critérios clara

### 🚀 Entrega

- [ ] `artifactId` no `pom.xml` renomeado (padrão: nome1-nome2)
- [ ] Todos os testes executam com sucesso
- [ ] Cobertura verificada e documentada
- [ ] Projeto compactado em .zip
- [ ] Nome do arquivo .zip segue padrão (nome1-nome2.zip)
- [ ] Pronto para envio no SIGAA

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

### 🔀 Grafo de Fluxo de Controle (CFG)

*[A ser incluído após implementação]*

---

### 📐 Complexidade Ciclomática V(G)

*[A ser calculado após implementação]*

---

### ✅ Tabela MC/DC

*[A ser incluída após análise da decisão composta mais complexa]*

---

### 📝 Casos de Teste Detalhados

*[Link para planilha/documento separado com todos os casos de teste]*

---

## 👥 Integrantes

- Matheus Freitas
- Jonas Rafael

---

## 📅 Cronograma Sugerido

| Fase | Descrição | Prazo Sugerido |
|------|-----------|----------------|
| 1 | Implementação do código base | Semana 1 |
| 2 | Análise e design de testes | Semana 1-2 |
| 3 | Testes funcionais (caixa preta) | Semana 2 |
| 4 | Testes estruturais (caixa branca) | Semana 3 |
| 5 | Boas práticas e refatoração | Semana 3 |
| 6 | Documentação final e entrega | Semana 4 |
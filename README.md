# E-commerce Test - Trabalho de Testes de Software

**Disciplina:** Testes de Software - Prof. Eiji Adachi  
**Projeto:** Testes Automatizados para Funcionalidade de Finalização de Compra

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
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

## 🔢 Ordem de Implementação

### **Fase 1: Implementação do Código Base** 
*Esforço: Alto | Importância: Crítica | Prioridade: 1*

- [ ] **1.1** Implementar o método `calcularCustoTotal` no `CompraService`
  - Implementar cálculo do subtotal dos itens
  - Implementar desconto por múltiplos itens do mesmo tipo
  - Implementar desconto por valor de carrinho
  - Implementar cálculo do peso tributável (físico vs cúbico)
  - Implementar cálculo do frete base por faixas de peso
  - Implementar taxa mínima de frete (R$ 12,00)
  - Implementar taxa de manuseio para itens frágeis
  - Implementar multiplicador por região
  - Implementar desconto de frete por nível do cliente
  - Implementar arredondamento final (half-up, 2 casas decimais)

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

## ✅ Checklist de Acompanhamento

### 📦 Implementação

- [ ] Método `calcularCustoTotal` implementado e funcionando
- [ ] Todas as regras de negócio implementadas corretamente
- [ ] Código compila sem erros

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

### Grafo de Fluxo de Controle (CFG)

*[A ser incluído após implementação]*

### Complexidade Ciclomática V(G)

*[A ser calculado após implementação]*

### Tabela MC/DC

*[A ser incluída após análise da decisão composta mais complexa]*

### Casos de Teste Detalhados

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
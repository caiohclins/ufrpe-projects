# Análise estatística da qualidade da água das praias de SP

Projeto designado a primeira VA da disciplina de Estatística Exploratória I (2022.1), do curso de Licenciatura em Computação pela UFRPE.

## Configurando o Ambiente da Aplicação [Windows]

É necessário instalar os seguintes programas:

### **Linguagem R**

[R-4.2.2](https://cran.rstudio.com/bin/windows/base/R-4.2.2-win.exe)

### **RSTUDIO**

[RStudio-2022_1](https://posit.co/download/rstudio-desktop/)


# Lista de Atividades para o projeto

## Preparação dos dados (RStudio):
* Importe o arquivo de xlsx no Rstudio (usando a função read_excel).
* As instruções a seguir devem ser realizados num único bloco (use %>%).
* Filtre os dados pela cidade atribuída a você (filter).
* Modifique a última coluna para numérica (mutate e as.numeric).
* Agrupe os dados por praia (group_by).

## Questão 1:
Encontre média, desvio-padrão, mediana, Q1, Q3, mínimo e máximo dos enterococos de cada praia (summarise)

## Questão 2:
* Faça um gráfico de barras com a variável Beach, ordenando da praia com maior quantidades de amostras para a menor.
* Colorir gráfico com base na praia. Anotar as porcentagens no topo das barras (ggplot2).

## Questão 3:
* Repita a questão 2, fazendo desta vez um gráfico de pizza (ggplot2).

## Questão 4:
* Fazer um histograma com todos os dados de enterococos das praias da sua cidade (ggplot2).

## Questão 5:
* Fazer box-plots de todas as praias da sua cidade num único gráfico (ggplot2).

### Relatório:
* Faça um pequeno relatório apresentado as estatística e cada um dos gráficos e explicando os resultados.
Use o R markdown.

### Publicação:
* Fazer upload do código de todos os arquivos no seu github/gitlab pessoal e torná-lo público.
* Fazer um arquive Readme.md explicando a atividade.
* Postar o link da repositório no campo de mensagens abaixo.
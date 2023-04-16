library(readxl)
library(dplyr)
library(stringr)
library(ggplot2)
library(OpenStreetMap)
library(sp)

library(shiny)

# - - - - - - - - - -  D A D O S  - - - - - - - - - -

# Lê os arquivos da planilha
base_data <- read_excel("ESTATISTICA_EXPLORATORIA_I-2022.1/projeto-segunda-va/dados_de_caminhada_corrida.xlsx", sheet = 1, col_names = TRUE)

# Dados utilizados para a plotagem do Mapa
# Considerando todas as coordenadas, separando lat e long e convertendo em número.
dados_mapa <- data.frame(
  lat = as.numeric(str_sub(base_data$Coordenadas, 0, 8)),
  long = as.numeric(str_sub(base_data$Coordenadas, 12, 20))
)

# Dados utilizados para o Teste de Hipóteses
# Considerando os dados corrida entre 18:40:53 e 18:45:12
dados_teste_hipotese <- base_data %>%
  mutate(Velocidade = str_replace(Velocidade, " km/h", "")) %>%
  mutate_at(c("Velocidade"), as.numeric) %>%
  mutate_at(c("Hora"), as.POSIXct) %>%
  filter(Hora >= as.POSIXct("2023-03-23 18:40:53", tz = "GMT") & Hora <= as.POSIXct("2023-03-23 18:45:12", tz = "GMT"))

# Dados utilizados para o Intervalo de Confiança
# Considerando os dados de caminhada entre 18:45:18 e 18:49:23
dados_intervalo_confianca <- base_data %>%
  mutate(Velocidade = str_replace(Velocidade, " km/h", "")) %>%
  mutate_at(c("Velocidade"), as.numeric) %>%
  mutate_at(c("Hora"), as.POSIXct) %>%
  filter(Hora >= as.POSIXct("2023-03-23 18:45:18", tz = "GMT") & Hora <= as.POSIXct("2023-03-23 18:49:23", tz = "GMT"))


# - - - - - - - - - -  D A S H B O A R D - - - - - - - - - -
# Define UI for application
ui <- fluidPage(

  # Application title
  titlePanel("Projeto de Estatística - UFRPE"),


  # Sidebar
  sidebarLayout(
    sidebarPanel(

      # Input numérico para determinar a Variância
      numericInput(
        "variancia",
        "Variancia",
        sd(c(dados_teste_hipotese$Velocidade)),
        min = 0,
        max = NA
      ),

      # Radio Buttons para determinar o Tipo de Teste
      radioButtons(
        "tipo", "Tipo do teste",
        c(
          "Bilateral" = "bi",
          "Unilateral a Esquerda" = "esq",
          "Unilateral a Direita" = "dir"
        )
      ),

      # Slide Input para determinar mu0
      sliderInput("mu0", "Selecione mu0",
        min = 4, max = 12, value = 8
      ),

      # Slide Input para determinar o Nível de Significância
      sliderInput("alfa", "Selecione alfa",
        min = 0.01, max = 1, value = 0.05
      )
    ),

    # Painel Principal, com as abas a serem exibidas
    mainPanel(
      tabsetPanel(
        type = "tabs",

        # Aba do Mapa
        tabPanel("Mapa", plotOutput("plot_map")),

        # Aba do Testes de hipóteses
        tabPanel(
          "Testes de hipóteses",
          tableOutput("table_teste_hipotese"),
          plotOutput("plot_teste_hipoteses")
        ),

        # Aba do Intervalo de Confiança
        tabPanel(
          "Intervalo de Confiança",
          tableOutput("table_intervalo_confianca")
        ),

        # Aba da Regressão Linear
        tabPanel(
          "Regressão linear",
          tableOutput("table_regressao_linear"),
          plotOutput("plot_regressao_linear")
        )
      )
    )
  )
)


# - - - - - - - - - -  TRABALHANDO COM OS DADOS - - - - - - - - - -

# Define server logic
server <- function(input, output) {
  x <- reactive({
    c(dados_teste_hipotese$Velocidade)
  })

  n <- reactive(length(x()))
  xbarra <- reactive(mean(x()))
  # sig = reactive(sd(x())) #input$variancia
  sig <- reactive(sqrt(as.numeric(input$variancia)))
  sig_xbar <- reactive(sig() / sqrt(n()))

  mu0 <- reactive({
    as.integer(input$mu0)
  })

  alfa <- reactive(as.numeric(input$alfa))

  tipo <- reactive(input$tipo)
  teste <- renderText(tipo())

  p <- reactive({
    if (teste() == "bi") {
      1 - alfa() + alfa() / 2
    } else if (teste() == "esq") {
      alfa()
    } else {
      1 - alfa()
    }
  })

  ztab <- reactive(
    as.numeric(qnorm(p()))
  )

  zcalc <- reactive(
    as.numeric((xbarra() - mu0()) / sig_xbar())
  )


  # - - - - - - - - - -  O U T P U T S - - - - - - - - - -

  output$table_teste_hipotese <- renderTable(
    if (teste() == "bi" & zcalc() < ztab() & zcalc() > -ztab() |
      teste() == "esq" & zcalc() < ztab() |
      teste() == "dir" & zcalc() > ztab()
    ) {
      data.frame(Resultado = paste0("Aceitamos H0 ao nível de sig. = ", alfa() * 100, "%"))
    } else {
      data.frame(Resultado = paste0("Rejeitamos H0 ao nível de sig. = ", alfa() * 100, "%"))
    }
  )

  # Intervalo de Confiança
  output$table_intervalo_confianca <- renderTable({
    ICx <- c(dados_intervalo_confianca$Velocidade)
    ICn <- length(ICx)
    ICz <- qnorm(alfa() / 2 + 1 - alfa())
    ICmin <- round(mean(ICx) - ICz * sd(ICx) / sqrt(ICn), 2)
    ICmax <- round(mean(ICx) + ICz * sd(ICx) / sqrt(ICn), 2)
    data.frame(Resultado = paste0(
      "Intervalo de Confiança com Nível de Confiança de ",
      alfa() * 100, "%", " é de IC(mu) = ", "[ ", ICmin, " ; ", ICmax, " ]"
    ))
  })

  # Criação do Mapa
  output$plot_map <- renderPlot({
    bb <- matrix(c(
      -34.952, -34.9435,
      -8.012, -8.022
    ), 2, 2, byrow = T)
    rownames(bb) <- c("long", "lat")
    colnames(bb) <- c("min", "max")

    crs <- CRS("+proj=utm +zone=25 +south +datum=WGS84")

    lonr <- bb[1, 2]
    latu <- bb[2, 2]
    lonl <- bb[1, 1]
    latd <- bb[2, 1]

    sa_map <- openmap(c(latu + 0.001, lonl - 0.001),
      c(latd - 0.001, lonr + 0.001),
      type = "osm", mergeTiles = TRUE, minNumTiles = 9L
    )

    sa_map2 <- openproj(sa_map)

    sa_map2_plt <- OpenStreetMap::autoplot.OpenStreetMap(sa_map2) +
      geom_point(
        data = dados_mapa,
        aes(x = long, y = lat), # slightly shift the points
        colour = "red", size = 0.5
      ) +
      xlab("Longitude") + ylab("Latitude")
    sa_map2_plt
  })

  # Histograma do Teste de Hipóteses
  output$plot_teste_hipoteses <- renderPlot({
    hist(x(), main = "", freq = FALSE)
    abline(v = mu0(), col = "red")
    abline(v = xbarra(), col = "blue")
  })

  # Resultados da Regressão Linear
  output$table_regressao_linear <- renderTable({
    data(cars)
    reg <- lm(speed ~ dist, data = cars)
    R <- cor(cars$speed, cars$dist)
    R2 <- summary(reg)$r.squared
    reta <- coef(reg)
    data.frame(
      R = paste0(R),
      R2 = paste0(R2),
      Reta = paste0("y = ", reta[1], " + ", reta[2], "x")
    )
  })

  # Gráfico de Disperssão da Regressão Linear
  output$plot_regressao_linear <- renderPlot({
    ggplot(mapping = aes(cars$dist, cars$speed)) +
      geom_point() +
      geom_smooth(method = "lm") +
      geom_hline(yintercept = mean(cars$speed)) +
      xlab("Velocidade") +
      ylab("Disância") +
      theme_minimal()
  })
}

# Run the application
shinyApp(ui = ui, server = server)


# Jogo 2D Top-Down

Um simples jogo 2D top-down desenvolvido em Java 17 com Swing.


## Requisitos

- **Java 17** ou superior
- **Maven** para gerenciar dependências e compilar o projeto

## Como executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/andrerochasouza/demo-2d.git
   cd demo-2d
   ```

2. Compile o projeto usando Maven:

   ```bash
   mvn clean package
   ```

3. Execute o jogo:

   - Para executar diretamente o `.jar`:
   
     ```bash
     java -jar target/JavaDemo.jar
     ```

   - Ou, para usar o instalador (Windows):

     Após compilar, um instalador MSI estará disponível no diretório `target/output`. Execute o instalador para instalar o jogo no seu sistema.

## Funcionalidades

- **Movimentação do jogador**: O jogador pode se mover pelo mapa usando as teclas de direção.
- **Interação com NPCs**: O jogador pode interagir com NPCs no mapa. (Em desenvolvimento)
- **Inimigos**: O jogo inclui inimigos que perseguem o jogador e podem atacá-lo. (Em desenvolvimento)
- **Carregamento de Mapas**: Os mapas do jogo são carregados dinamicamente a partir de arquivos usando a classe `MapLoader`.

## Estrutura do Código

- `Game.java`: Classe principal do jogo que inicia a janela e o loop de jogo.
- `GameLoop.java`: Implementa o loop principal do jogo, responsável pela atualização do estado e renderização.
- `Window.java`: Gerencia a janela do jogo e as operações de renderização.
- `Player.java`: Representa o jogador e contém a lógica de movimentação e interação.
- `Enemy.java`: Define os inimigos e sua lógica de perseguição ao jogador.
- `NPC.java`: Define os NPCs e suas interações.
- `GameMap.java`: Carrega e armazena o mapa do jogo.
- `MapLoader.java`: Responsável por carregar os mapas do jogo a partir de arquivos externos.

## Licença

Este projeto está licenciado sob a licença MIT.

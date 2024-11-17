import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class GameView extends Application {
    private static final int TILE_SIZE = 50;
    private static  int GRID_WIDTH = 20;
    private static  int GRID_HEIGHT = 16;

    private Canvas canvas;
    private GraphicsContext gc;
    private Player player;
    private TileState[][] levelTiles;
    private boolean gameWon = false;
    private Stage primaryStage;
    private int level_courant;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartScreen();
    }

    private void showStartScreen(){
        Pane startPane = new Pane();
        Scene startScene= new Scene(startPane,GRID_WIDTH*TILE_SIZE,GRID_HEIGHT*TILE_SIZE);
        //Ajouter le logo
        Image logo = new Image(getClass().getResource("/logo.png").toExternalForm());
        javafx.scene.image.ImageView logoView = new javafx.scene.image.ImageView(logo);
        logoView.setFitWidth(300);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX((GRID_WIDTH*TILE_SIZE -300)/2); //Centrer le logo
        logoView.setLayoutY(50);


        //Ajouter le bouton Start
        Button startButton = new Button("Start");
        startButton.setLayoutX((GRID_WIDTH*TILE_SIZE-100)/2); //Center le bouton
        startButton.setLayoutY(360);
        startButton.setOnAction(e->showLevelSelectionScreen());
        startPane.getChildren().addAll(logoView,startButton);

        primaryStage.setTitle("Sokoban");
        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    //Méthode pour afficher l'écran de séléction de niveau
    private void showLevelSelectionScreen(){
        VBox levelSelectionPane = new VBox(10);//Espacement de 10 pixels entre les boutons
        levelSelectionPane.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Scene levelSelectionScene = new Scene(levelSelectionPane,GRID_WIDTH*TILE_SIZE,GRID_HEIGHT*TILE_SIZE);
        // Ajouter des boutons pour chaque niveau
        for (int i=0; i<Level.getNumberofLevels();i++){
            int levelIndex=i;
            Button levelButton = new Button("Level " + (i+1));
            levelButton.setOnAction(e->startLevel(levelIndex));
            levelSelectionPane.getChildren().add(levelButton);
        }
        primaryStage.setScene(levelSelectionScene);
    }

private Position findPlayerDebut(char[][] levelLayout){
        for (int i=0; i<levelLayout.length; i++){
            for (int j=0; j<levelLayout[i].length; j++){
                if (levelLayout[i][j] == 'P'){
                    return new Position(j,i);
                }
            }
        }
    return null;
}

    //Méthode pour démmarer un niveau particulier
    private void startLevel(int levelIndex){
        level_courant=levelIndex;
        char[][] levelLayout = Level.getLevel(levelIndex);
        initialiseLevelTiles(levelLayout);
        Position startPosition = findPlayerDebut(levelLayout);
        player = new Player(startPosition,levelTiles);
        canvas = new Canvas((levelLayout[0].length)*TILE_SIZE, (levelLayout.length)*TILE_SIZE); // Dimension de la grille
        gc = canvas.getGraphicsContext2D();

        Button restartButton = new Button("Restart");
        restartButton.setLayoutX(levelLayout[0].length * TILE_SIZE + 4
        ); // Déplacer à droite de la grille
        restartButton.setLayoutY(20); // Position en haut à droite
        restartButton.setFocusTraversable(false); // Désactive le focus sur le bouton
        restartButton.setOnAction(e -> restarLevel()); // Action de redémarrage



        Pane gamePane = new Pane();
        gamePane.getChildren().addAll(canvas,restartButton);

        Scene gameScene = new Scene(gamePane);

        gameScene.setOnKeyPressed(event -> {
            if (gameWon) return;
            MoveCommand moveCommand = null;
            switch (event.getCode()) {
                case UP -> moveCommand= new MoveCommand(player,Direction.UP);
                case DOWN -> moveCommand= new MoveCommand(player,Direction.DOWN);
                case LEFT -> moveCommand= new MoveCommand(player,Direction.LEFT);
                case RIGHT -> moveCommand= new MoveCommand(player,Direction.RIGHT);}
            // Exécuter la commande si elle est créée
            if (moveCommand != null) {
                moveCommand.execute();
                drawLevel();
                if(isLevelCompleted()){
                    System.out.println("Félicitations ! Vous avez complété le niveau. ");
                    //Affiche l'option de redémarrage
                    showRestartOption();
                }
            }

        });

       // primaryStage.setTitle("Sokoban");
        primaryStage.setScene(gameScene);
        //Afficher le niveau initial
        drawLevel();

    }





 private void initialiseLevelTiles(char[][] levelLayout) {

        levelTiles = new TileState[levelLayout.length][levelLayout[0].length];
        for (int y=0;y<levelLayout.length;y++){
            for (int x=0;x<levelLayout[y].length;x++){
                levelTiles[y][x]=(TileState) ElementFactory.createElement(levelLayout[y][x]);
            }
        }

}
    private void drawLevel(){
        //Effacer le canvas pour redissiner la grille
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(javafx.scene.paint.Color.rgb(131, 157, 160));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // dessiner les cases du niveau
        for (int y=0;y<levelTiles.length;y++){
            for (int x=0;x<levelTiles[y].length;x++){
                TileState tile = levelTiles[y][x];
                if (tile instanceof BoxTile){
                    gc.drawImage(new Image(getClass().getResource("/box.png").toExternalForm()),x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
                }else if(tile instanceof BoxOnGoalTile){
                    gc.drawImage(new Image(getClass().getResource("/box_on_goal.png").toExternalForm()),x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
                }
                else if (tile instanceof EmptyTile){
                    gc.drawImage(new Image(getClass().getResource("/floor.png").toExternalForm()),x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
                } else if (tile instanceof WallTile) {
                    gc.drawImage(new Image(getClass().getResource("/wall.png").toExternalForm()),x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);

                }else if (tile instanceof GoalTile){
                    gc.drawImage(new Image(getClass().getResource("/goal.png").toExternalForm()),x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);

                }
            }
        }
        // Déssiner l'image du joueur à sa position actuelle
        Image playerImage = new Image(getClass().getResource("/player.png").toExternalForm());
        gc.drawImage(player.getPlayerImage(),player.getPosition().getX()*TILE_SIZE,player.getPosition().getY()*TILE_SIZE,TILE_SIZE,TILE_SIZE);
    }

    private boolean isLevelCompleted(){
        for (int y=0;y<levelTiles.length;y++){
            for (int x=0;x<levelTiles[y].length;x++){
                TileState tile = levelTiles[y][x];
                // Vérifie si chaque GoalTile a une BoxTile
                if (tile instanceof GoalTile ) {
                return false;                }
            }
        }
        gameWon=true;
        showVictoryMessage();// Marque le jeu comme gangé
        return true; // toutes les goalTiles sont couvertes par des BoxTiles
    }

    private void showVictoryMessage(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoire ! ");
        alert.setHeaderText(null);
        alert.setContentText("Félicitation ! Vous avez terminé le niveau .");
        alert.showAndWait();
    }

    //////////////// essayer de mettre un bouton de redémarraage
    private void showRestartOption(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Niveau complété ! ");
        alert.setHeaderText(null);
        alert.setContentText("Félictation ! voulez vous redémarrer le niveau ? ");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            // restar level
            restarLevel();
        }
    }

    private void restarLevel(){
        Level level = new Level();
        //récupérer le layout du niveau
        char [][] levelLayout=  level.getLevel(level_courant);
        initialiseLevelTiles(levelLayout);
        player.setPosition(findPlayerDebut(levelLayout));
        player.setLevelTiles(levelTiles);
        player.setRestarImage();
        gameWon=false;
        drawLevel();

    }


    public static void main(String[] args) {
        launch(args);
    }


}

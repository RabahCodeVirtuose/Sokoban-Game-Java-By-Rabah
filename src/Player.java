import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import javafx.scene.image.Image;


public class Player {
    private Position position;
    private TileState[][] levelTiles; // la matrice des cases du niveau
    private Image playerImage;
    private Image prince,upImage1,upImage2, downImage1,downImage2,leftImage1,leftImage2,leftImage3, rightImage1,rightImage2,rightImage3,imageUpGle,imageDownGle,imageRightGle,imageLeftGle; // Image différentes
    public Player(Position startposition, TileState[][] levelTiles) {
        this.position = startposition;
        this.levelTiles = levelTiles;
        //charger les images :
        prince= new Image(getClass().getResource("/player.png").toExternalForm());
        upImage1= new Image(getClass().getResource("/up_Image1.png").toExternalForm());
        upImage2= new Image(getClass().getResource("/up_Image2.png").toExternalForm());
        downImage1= new Image(getClass().getResource("/down_Image1.png").toExternalForm());
        downImage2= new Image(getClass().getResource("/down_Image2.png").toExternalForm());
        leftImage1= new Image(getClass().getResource("/left_Image1.png").toExternalForm());
        leftImage2= new Image(getClass().getResource("/left_Image2.png").toExternalForm());
        leftImage3= new Image(getClass().getResource("/left_Image3.png").toExternalForm());
        rightImage1= new Image(getClass().getResource("/right_Image1.png").toExternalForm());
        rightImage2= new Image(getClass().getResource("/right_Image2.png").toExternalForm());
        rightImage3= new Image(getClass().getResource("/right_Image3.png").toExternalForm());

        imageUpGle=upImage1;
        imageDownGle=downImage1;
        imageRightGle=rightImage1;
        imageLeftGle=leftImage1;


        playerImage = prince;
    }
    Image imageUpChange(){
        if (imageUpGle==upImage1){
            imageUpGle=upImage2;
        }else {
            imageUpGle=upImage1;
        }
        return imageUpGle;
    }

    Image imageDownChange(){
        if (imageDownGle==downImage1){
            imageDownGle=downImage2;
        }else{
            imageDownGle=downImage1;
        }
        return imageDownGle;
    }

    Image imageRightChange(){
        if (imageRightGle==rightImage1){
            imageRightGle=rightImage2;
        }else if (imageRightGle==rightImage2){
            imageRightGle=rightImage3;
        }else{
            imageRightGle=rightImage1;
        }
        return imageRightGle;
    }

    Image imageLeftChange(){
        if (imageLeftGle==leftImage1){
            imageLeftGle=leftImage2;
        }else if (imageLeftGle==leftImage2){
            imageLeftGle=leftImage3;
        }else{
            imageLeftGle=leftImage1;
        }
        return imageLeftGle;
    }
    public void  setRestarImage(){
        playerImage = prince;
    }
    public void move(Direction direction) {
        switch (direction){
            case UP -> playerImage = imageUpChange();
            case DOWN -> playerImage = imageDownChange();
            case LEFT -> playerImage = imageLeftChange();
            case RIGHT -> playerImage = imageRightChange();

        }
        Position newPosition = position.positionTowards(direction,1);
        // Vérifier si la case suivante contient une caisse
        if (isBoxTile(newPosition)) {
            Position boxNewPosition = newPosition.positionTowards(direction,1);
            if(canMoveBox(boxNewPosition)) {
                //dépalcer la caisse
                moveBox(newPosition, boxNewPosition);
                //déplacer le joueur
                position = newPosition;
            }else{
                System.out.println("Dépalcement de la caisse impossible ! ");
            }
        }else if (isValidMove(newPosition)) {
            position = newPosition;
            System.out.println("Le joueur a bougé vers "+ direction);
        }else{
            System.out.println("Déplacement impossible dans cette direction ! ");
        }
    }

    public Image getPlayerImage() {
        return playerImage;
    }


    private void moveBox(Position boxPos, Position newBoxPos) {
    TileState currentBoxTile = levelTiles[boxPos.getY()][boxPos.getX()];
    TileState targetTile = levelTiles[newBoxPos.getY()][newBoxPos.getX()];

    //Vérifier su la caisse est déplacée sur un point de dépôt
        if (targetTile instanceof GoalTile){
            levelTiles[newBoxPos.getY()][newBoxPos.getX()] = new BoxOnGoalTile();
        }else{
            levelTiles[newBoxPos.getY()][newBoxPos.getX()] = new BoxTile();
        }

        //Restaurer le point de dépôt si la caisse quitte le point de dépôt
        if (currentBoxTile instanceof BoxOnGoalTile){
            levelTiles[boxPos.getY()][boxPos.getX()] = new GoalTile();
        }else{
            levelTiles[boxPos.getY()][boxPos.getX()] = new EmptyTile();
        }

        System.out.println("Caisse déplacée de "+boxPos+"à"+newBoxPos);

    }
    private  boolean isValidMove(Position newPosition) {
        int x=newPosition.getX();
        int y=newPosition.getY();
        return levelTiles[y][x].isMovable();
    }

    private boolean isBoxTile(Position pos) {
        return levelTiles[pos.getY()][pos.getX()] instanceof BoxTile || levelTiles[pos.getY()][pos.getX()] instanceof BoxOnGoalTile;

    }
    public Position getPosition() {
        return position;
    }
    private boolean canMoveBox( Position newBoxPos){
        //Met à jour l'état des cases : l'ancienne case deivnet vide et la nouvelle devient une caisse
    int x = newBoxPos.getX();
    int y = newBoxPos.getY();

    return levelTiles[y][x].isMovable() && !(levelTiles[y][x] instanceof BoxTile) && !(levelTiles[y][x] instanceof BoxOnGoalTile);
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public void setLevelTiles (TileState[][] levelTiles) {
        this.levelTiles = levelTiles;
    }
}

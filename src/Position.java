public class Position {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    // Calculer la nouvelle position vers une direction donnée avec un offset
    public Position positionTowards (Direction direction, int offset){
        int newX = this.x;
        int newY = this.y;

        switch (direction){
            case UP -> newY -= offset;
            case DOWN -> newY += offset;
            case LEFT -> newX -= offset;
            case RIGHT -> newX += offset;
        }
        return new Position(newX, newY);
    }
    @Override
    public String toString() {
        return "Position [x=" + x + ", y=" + y + "]";
    }

}

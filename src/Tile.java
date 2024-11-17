public class Tile {
    private TileState state;
    public Tile(TileState initialstate) {
        this.state = initialstate;
    }
    public void setState(TileState newState) {
        this.state = newState;
    }
    public boolean isMovable(){
        return this.state.isMovable();
    }
    public boolean isGoal(){
        return this.state.isGoal();
    }

}

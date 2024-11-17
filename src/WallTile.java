public class WallTile implements TileState {
    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public boolean isGoal() {
        return false;
    }
}

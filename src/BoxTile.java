public class BoxTile implements TileState {
    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return false;
    }
}

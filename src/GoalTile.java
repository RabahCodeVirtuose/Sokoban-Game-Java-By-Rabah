public class GoalTile implements TileState {
    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return true;
    }
}
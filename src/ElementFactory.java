public class ElementFactory {
    public static TileState createElement (char type){
        switch (type){
            case 'W':
                return new WallTile();
            case 'B':
                return new BoxTile();
            case 'G':
                return new GoalTile();
            case 'F':
                return new EmptyTile();
            case 'P':
                return new EmptyTile();
            case 'Z':
                return new BoxOnGoalTile();
            default:
                throw new IllegalArgumentException("Type d'élément inconnu :"+type);

        }
    }
}

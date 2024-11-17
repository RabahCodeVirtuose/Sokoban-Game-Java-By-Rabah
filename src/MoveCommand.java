public class MoveCommand implements Command {
    private Player player;
    private Direction direction;

    public MoveCommand(Player player, Direction direction) {
        this.player = player;
        this.direction = direction;
    }

    @Override
    public void execute() {
    player.move(direction); // déplacer le joueur dans la direction donnée
    }
}

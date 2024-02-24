public class Subtask extends Task{
    private int epicId;

    public Subtask(String name, String description, int id, int epicId) {
        super(name, description, id);

        this.epicId = epicId;
    }
}

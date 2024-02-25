public class Subtask extends Task{
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);

        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description) {
        super(name, description);
        this.setId(id);
    }

    public Subtask(String name, String description) {
        super(name, description);

    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}

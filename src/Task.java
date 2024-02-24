public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(String name, String description, int id){
        this.name = name;
        this.description = description;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

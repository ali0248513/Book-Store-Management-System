

public class Member {
    private String name;
    private int id;
    private String role; // "Admin" or "User"

    public Member(String name, int id, String role) {
        this.name = name;
        this.id = id;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}

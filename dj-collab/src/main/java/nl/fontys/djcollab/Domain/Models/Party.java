package nl.fontys.djcollab.Domain.Models;

public class Party {
    public int id;
    public int ownerId = 0; //make user
    public String name = "";

    public Party(int id, int ownerId, String name) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
    }
}

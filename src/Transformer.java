import java.math.BigDecimal;

public class Transformer {
    private int addedBy;
    private String name;
    private String scale;
    private String faction;
    private String description;


    public Transformer(int addedBy, String name, String scale, String faction, String description) { // declare a new instance of Transformer
        this.addedBy = addedBy;
        this.name = name;
        this.scale = scale;
        this.faction = faction;
        this.description = description;
    }

    // getters
    public String getName() {
        return name;
    }

    public String getScale() {
        return scale;
    }

    public String getFaction() { return faction; }

    public String getDescription() { return description; }
    public int getAddedBy() { return addedBy; }

    // setters
    public void setName(String name) { this.name = name; }

    public void setScale(String scale) { this.scale = scale; }

    public void setFaction(String faction) {this.faction = faction; }

    public void setDescription(String description) { this.description = description; }


    public void setAddedBy(int addedBy) {
        addedBy = CollectionAssistant.currentUser.getUserId();
    }
}


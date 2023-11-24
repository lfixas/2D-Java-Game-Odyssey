package odyssey.game.Infos;

public abstract class InfoPlanets {

    private String Name;
    private int ID;
    private String Environment;
    private String Lore;

    public void setName(String Name) { this.Name = Name; }
    public void setID(int ID) { this.ID = ID; }
    public void setEnvironment(String Environment) { this.Environment = Environment; }
    public void setLore(String Lore) { this.Lore = Lore; }

    public String getName() { return Name; }
    public int getID() { return ID; }
    public String getEnvironment() { return Environment; }
    public String getLore() { return Lore; }
}

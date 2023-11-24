package odyssey.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.XmlReader;
import odyssey.game.entity.craftdata.CraftData;
import odyssey.game.entity.ship.Starship;

public class Inventory
{
    private CraftData craftd = new CraftData();
    private Preferences inventorySave = null;

    public Inventory()
    {
        try
        {
            this.inventorySave = Gdx.app.getPreferences("Odyssey.game.inventory");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        loadFromSave();
    }
    public void addRessourceItem(int itemCode, int quantity)
    {
            this.ressourcesInv.getAndIncrement(itemCode, 0, quantity);

            saveValues(true);
    }
    public int getRessourceCount(int itemCode) { return this.ressourcesInv.get(itemCode, 0); }
    public int getCraftCount(CraftData.CraftItem item) { return this.craftInv.get(item.ordinal(), 0); }
    public void addCraftItem(CraftData.CraftItem item, int quantity)
    {
        this.craftInv.getAndIncrement(item.ordinal(), 0, quantity);

        saveValues(false);
    }
    public void saveShipState()
    {
        this.inventorySave.putInteger("SHIPSTATE", Starship.getState());
        this.inventorySave.flush();
    }
    public void loadShipState()
    {
        Starship.setState(this.inventorySave.getInteger("SHIPSTATE", 5));
    }
    public void printInventoryDebug()
    {
        System.out.println("Print inventaire : ");

        for (RessourceItem key : RessourceItem.values())
        {
            System.out.println(getItemCode(key.ordinal()).name() + " : " + this.ressourcesInv.get(key.ordinal(), 0));
        }
    }
    public RessourceItem getItemCode(int itemCode)
    {
        switch (itemCode)
        {
            case 0 : return RessourceItem.COPPER;
            case 1 : return RessourceItem.PETROL;
            case 2 : return RessourceItem.CARBON;
            case 3 : return RessourceItem.STEEL;
            case 4 : return RessourceItem.GOLD;
            default : return RessourceItem.COPPER;
        }
    }
    public enum RessourceItem
    {
        COPPER, PETROL, CARBON, STEEL, GOLD
    }

    private void loadFromSave()
    {
        int copper = this.inventorySave.getInteger("COPPER", 0);
        int petrol = this.inventorySave.getInteger("PETROL", 0);
        int carbon = this.inventorySave.getInteger("CARBON", 0);
        int steel = this.inventorySave.getInteger("STEEL", 0);
        int gold = this.inventorySave.getInteger("GOLD", 0);

        int weapon = this.inventorySave.getInteger("WEAPON", 0);
        int health = this.inventorySave.getInteger("HEALTH", 0);

        this.ressourcesInv.put(0, copper);
        this.ressourcesInv.put(1, petrol);
        this.ressourcesInv.put(2, carbon);
        this.ressourcesInv.put(3, steel);
        this.ressourcesInv.put(4, gold);

        this.craftInv.put(1, health);
        this.craftInv.put(2, weapon);
    }
    private void saveValues(boolean bIsRessource)
    {
        if (bIsRessource)
        {
            this.inventorySave.putInteger("COPPER", this.getRessourceCount(RessourceItem.COPPER.ordinal()));
            this.inventorySave.putInteger("PETROL", this.getRessourceCount(RessourceItem.PETROL.ordinal()));
            this.inventorySave.putInteger("CARBON", this.getRessourceCount(RessourceItem.CARBON.ordinal()));
            this.inventorySave.putInteger("STEEL", this.getRessourceCount(RessourceItem.STEEL.ordinal()));
            this.inventorySave.putInteger("GOLD", this.getRessourceCount(RessourceItem.GOLD.ordinal()));
        }
        else
        {
            this.inventorySave.putInteger("HEALTH", this.getCraftCount(CraftData.CraftItem.Potion));
            this.inventorySave.putInteger("WEAPON", this.getCraftCount(CraftData.CraftItem.Weapon));
        }

        this.inventorySave.flush();
    }
    private IntIntMap ressourcesInv = new IntIntMap();
    private IntIntMap craftInv = new IntIntMap();
}

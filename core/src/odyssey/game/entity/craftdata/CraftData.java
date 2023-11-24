package odyssey.game.entity.craftdata;

import odyssey.game.entity.player.Inventory;
import java.util.HashMap;

public class CraftData
{
    public CraftData()
    {
        HashMap<Inventory.RessourceItem, Integer> upSch = new HashMap ();
        upSch.put(Inventory.RessourceItem.COPPER, 2);
        upSch.put(Inventory.RessourceItem.CARBON, 2);
        upSch.put(Inventory.RessourceItem.PETROL, 2);
        upSch.put(Inventory.RessourceItem.GOLD, 2);
        upSch.put(Inventory.RessourceItem.STEEL, 2);

        HashMap<Inventory.RessourceItem, Integer> weapSch = new HashMap ();
        weapSch.put(Inventory.RessourceItem.COPPER, 0);
        weapSch.put(Inventory.RessourceItem.CARBON, 1);
        weapSch.put(Inventory.RessourceItem.PETROL, 0);
        weapSch.put(Inventory.RessourceItem.GOLD, 1);
        weapSch.put(Inventory.RessourceItem.STEEL, 3);

        HashMap<Inventory.RessourceItem, Integer> potSch = new HashMap ();
        potSch.put(Inventory.RessourceItem.COPPER, 1);
        potSch.put(Inventory.RessourceItem.CARBON, 0);
        potSch.put(Inventory.RessourceItem.PETROL, 0);
        potSch.put(Inventory.RessourceItem.GOLD, 1);
        potSch.put(Inventory.RessourceItem.STEEL, 0);

        schemes.put(CraftItem.Upgrade, upSch);
        schemes.put(CraftItem.Weapon, weapSch);
        schemes.put(CraftItem.Potion, potSch);
    }
    public enum CraftItem
    {
        Upgrade, Potion, Weapon
    }
    public static HashMap<Inventory.RessourceItem, Integer> getSchemes(CraftItem item)
    {
        return schemes.get(item);
    }
    private static HashMap <CraftItem, HashMap <Inventory.RessourceItem, Integer>> schemes = new HashMap <> (CraftItem.values().length);
}

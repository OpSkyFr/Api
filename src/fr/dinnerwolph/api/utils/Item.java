package fr.dinnerwolph.api.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * @author Dinnerwolph
 */

public class Item {

    private Material material;
    private String name;
    private int count;
    private byte data;
    private Enchantment enchantment;
    private int level;
    private String[] lore;

    public Item(Material material, String name, int count, byte data, Enchantment enchantment, int level, String... lore) {
        this.material = material;
        this.name = name;
        this.count = count;
        this.data = data;
        this.lore = lore;
        this.enchantment = enchantment;
        this.level = level;
    }

    public Item(Material material, String name, int count, byte data, String... lore) {
        this(material, name, count, data, null, 0, lore);
    }

    public Item(Material material, String name, int count, byte data) {
        this(material, name, count, data, null, 0, "");
    }

    public Item(Material material, String name, byte data) {
        this(material, name, 1, data, null, 0, "");
    }

    public Item(Material material, String name, int count) {
        this(material, name, count, (byte) 0, null, 0, "");
    }

    public Item(Material material, String name, int count, String... lore) {
        this(material, name, count, (byte) 0, null, 0, lore);
    }

    public Item(Material material, String name, String... lore) {
        this(material, name, 1, (byte) 0, null, 0, lore);
    }

    public Item(Material material, String name) {
        this(material, name, 1, (byte) 0, null, 0, "");
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, count, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        if (enchantment != null || level != 0) {
            itemMeta.addEnchant(enchantment, level, true);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}

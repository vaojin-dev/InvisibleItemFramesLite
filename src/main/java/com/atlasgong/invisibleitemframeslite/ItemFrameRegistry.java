package com.atlasgong.invisibleitemframeslite;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;


/**
 * Singleton registry responsible for creating and storing custom invisible item frames.
 * <p>
 * Access using {@link #getInstance()}.
 */
public class ItemFrameRegistry {
    private static ItemFrameRegistry instance;

    private ItemStack regInvisibleFrame;
    private ItemStack glowInvisibleFrame;

    /** Private constructor to enforce the singleton pattern. */
    private ItemFrameRegistry() {
    }

    /**
     * Returns the existing singleton instance of the registry, or creates one if it does not yet exist.
     *
     * @return the initialized {@link ItemFrameRegistry}
     */
    public static ItemFrameRegistry getInstance() {
        if (instance == null) {
            instance = new ItemFrameRegistry();
        }
        return instance;
    }

    /**
     * Registers the regular (non-glowing) invisible item frame using the factory.
     *
     * @param isInvisibleKey   key used to mark the item frame as invisible
     * @param name             display name of the item
     * @param lore             lore lines for the item
     * @param enchantmentGlint whether to apply an enchantment glint effect
     */
    public void registerRegularInvisibleItemFrame(NamespacedKey isInvisibleKey, Component name, List<Component> lore,
                                                  boolean enchantmentGlint) {
        regInvisibleFrame = create(isInvisibleKey, name, lore, enchantmentGlint, false);
    }

    /**
     * Registers the glowing invisible item frame using the factory.
     *
     * @param isInvisibleKey   key used to mark the item frame as invisible
     * @param name             display name of the item
     * @param lore             lore lines for the item
     * @param enchantmentGlint whether to apply an enchantment glint effect
     */
    public void registerGlowInvisibleItemFrame(NamespacedKey isInvisibleKey, Component name, List<Component> lore,
                                               boolean enchantmentGlint) {
        glowInvisibleFrame = create(isInvisibleKey, name, lore, enchantmentGlint, true);
    }

    /**
     * Gets a clone of the registered regular invisible item frame.
     *
     * @return cloned {@link ItemStack} of the regular invisible item frame
     */
    public ItemStack getRegularInvisibleFrame() {
        return regInvisibleFrame.clone();
    }

    /**
     * Gets a clone of the registered glowing invisible item frame.
     *
     * @return cloned {@link ItemStack} of the glowing invisible item frame
     */
    public ItemStack getGlowInvisibleFrame() {
        return glowInvisibleFrame.clone();
    }

    /**
     * Creates an invisible item frame.
     *
     * @param isInvisibleKey   The namespaced key to store invisibility status.
     * @param name             The configurable display name for the item.
     * @param lore             The configurable optional lore for the item.
     * @param enchantmentGlint Whether the item should have an enchantment glint.
     * @param glow             Whether to create a glow item frame instead of a regular item frame.
     * @return An invisible item frame.
     */
    public ItemStack create(NamespacedKey isInvisibleKey, Component name, List<Component> lore,
                            boolean enchantmentGlint, boolean glow) {
        Material type = glow ? Material.GLOW_ITEM_FRAME : Material.ITEM_FRAME;
        ItemStack item = new ItemStack(type, 1);

        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta, "ItemMeta was unexpectedly null.");

        meta.customName(name);
        meta.lore(lore);
        meta.getPersistentDataContainer().set(isInvisibleKey, PersistentDataType.BYTE, (byte) 1);
        meta.setEnchantmentGlintOverride(enchantmentGlint);

        item.setItemMeta(meta);
        return item;
    }

}

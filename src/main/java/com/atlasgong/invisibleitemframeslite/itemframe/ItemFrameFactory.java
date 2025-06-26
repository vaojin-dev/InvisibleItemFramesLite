package com.atlasgong.invisibleitemframeslite.itemframe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

/** Factory for creating invisible item frames. */
public class ItemFrameFactory {

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
    public ItemStack create(NamespacedKey isInvisibleKey, String name, List<String> lore,
                            boolean enchantmentGlint, boolean glow) {
        Material type = glow ? Material.GLOW_ITEM_FRAME : Material.ITEM_FRAME;
        ItemStack item = new ItemStack(type, 1);

        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta, "ItemMeta was unexpectedly null.");

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.getPersistentDataContainer()
                .set(isInvisibleKey, PersistentDataType.BYTE, (byte) 1);
        meta.setEnchantmentGlintOverride(enchantmentGlint);

        item.setItemMeta(meta);
        return item;
    }
}

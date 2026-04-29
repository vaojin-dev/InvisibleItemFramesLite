package com.atlasgong.invisibleitemframeslite.listeners;

import com.atlasgong.invisibleitemframeslite.Utils;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.util.RayTraceResult;

/** Listener that handles interactions with invisible item frames. */
public class ItemFrameInteractionListener implements Listener, PluginMessageListener {

    /** Key used to tag item frames as invisible via persistent data. */
    private final NamespacedKey isInvisibleKey;

    /**
     * Constructs a new ItemFrameInteractionListener.
     *
     * @param isInvisibleKey The {@link NamespacedKey} used to identify invisible item frames.
     */
    public ItemFrameInteractionListener(NamespacedKey isInvisibleKey) {
        this.isInvisibleKey = isInvisibleKey;
    }

    /**
     * Toggles visibility of invisible item frames s.t. they are visible when empty and invisible when filled.
     * Allows "passing through" the item frame to open the container behind it, if the player is not sneaking.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemFrameChange(PlayerItemFrameChangeEvent e) {
        ItemFrame frame = e.getItemFrame();
        if (!Utils.isInvisibleItemFrame(frame, isInvisibleKey)) {
            return;
        }

        switch (e.getAction()) {
            case PLACE -> frame.setVisible(false);
            case REMOVE -> frame.setVisible(true);
            case ROTATE -> { // pass-through logic
                Player p = e.getPlayer();
                if (p.isSneaking()) return; // allow rotations when player sneaking

                BlockFace attachedFace = e.getItemFrame().getAttachedFace();
                Block mount = e.getItemFrame().getLocation().getBlock().getRelative(attachedFace);
                if (mount.getState() instanceof Container container) {
                    e.setCancelled(true);
                    p.openInventory(container.getInventory());
                }
            }
        }
    }

    /**
     * handles vanilla out of range interactions (when the player is holding an item)
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractOutOfRange(PlayerInteractEvent e) {
        // tntercept right clicks that fall through when the entity is out of the client reach limit
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();

            if (p.isSneaking()) return;

            executeRayTraceAndOpenContainer(p);
        }
    }

    /**
     * Handles custom interaction messages sent by the fabric mod (when the player is not holding an item)
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("atlas:empty_click")) {
            if (player.isSneaking()) return;

            org.bukkit.Bukkit.getScheduler().runTask(
                org.bukkit.Bukkit.getPluginManager().getPlugin("InvisibleItemFramesLite"),
                () -> executeRayTraceAndOpenContainer(player)
            );
        }
    }

    private void executeRayTraceAndOpenContainer(Player p) {
        RayTraceResult rayTrace = p.getWorld().rayTraceEntities(
                p.getEyeLocation(),
                p.getEyeLocation().getDirection(),
                5, // distance
                0.0, // thickness
                entity -> entity instanceof ItemFrame && Utils.isInvisibleItemFrame(entity, isInvisibleKey)
        );
    
        if (rayTrace != null && rayTrace.getHitEntity() != null) {
            ItemFrame frame = (ItemFrame) rayTrace.getHitEntity();
            BlockFace attachedFace = frame.getAttachedFace();
            Block mount = frame.getLocation().getBlock().getRelative(attachedFace);
    
            if (mount.getState() instanceof Container container) {
                p.swingMainHand();
    
                p.openInventory(container.getInventory());
            }
        }
    }
}
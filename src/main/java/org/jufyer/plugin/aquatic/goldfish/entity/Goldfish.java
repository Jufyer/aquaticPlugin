package org.jufyer.plugin.aquatic.goldfish.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.Salmon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.aquatic.Main;

public class Goldfish extends Salmon {

  public static final NamespacedKey GOLDFISH_KEY = new NamespacedKey(Main.getInstance(), "Goldfish");

  public Goldfish(Location loc) {
    super(EntityType.SALMON, ((CraftWorld) loc.getWorld()).getHandle());

    this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
    this.getBukkitEntity().getPersistentDataContainer().set(GOLDFISH_KEY, PersistentDataType.BOOLEAN, true);
    this.setInvulnerable(false);
    this.setCustomName(Component.nullToEmpty("Goldfish"));
    this.setCustomNameVisible(false);

    this.persist = true;


    ((CraftWorld) loc.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

  }
}

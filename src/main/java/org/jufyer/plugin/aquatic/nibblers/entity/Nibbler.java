package org.jufyer.plugin.aquatic.nibblers.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.aquatic.Main;

import java.util.List;

public class Nibbler extends net.minecraft.world.entity.animal.fish.Cod {

  public static final NamespacedKey NIBBLER_KEY = new NamespacedKey(Main.getInstance(), "Nibbler");

  public Nibbler(Location loc) {
    super(EntityType.COD, ((CraftWorld) loc.getWorld()).getHandle());

    this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
    this.getBukkitEntity().getPersistentDataContainer().set(NIBBLER_KEY, PersistentDataType.BOOLEAN, true);
    this.setInvulnerable(false);
    this.setCustomName(Component.nullToEmpty("Nibbler"));
    this.setCustomNameVisible(false);

    this.persist = true;

    this.goalSelector.addGoal(1, new SwimTowardsNearestPlayerGoal(this));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(2 , new RandomSwimmingGoal(this, 1.0, 20));

    ((CraftWorld) loc.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
  }

  @Override
  public AttributeMap getAttributes() {
    return new AttributeMap(Zombie.createAttributes().build());
  }

  /**
   * Custom Goal to make the Nibbler swim towards the nearest player.
   */
  static class SwimTowardsNearestPlayerGoal extends Goal {
    private final Cod nibbler;
    private Player targetPlayer;

    public SwimTowardsNearestPlayerGoal(Cod nibbler) {
      this.nibbler = nibbler;
    }

    @Override
    public boolean canUse() {
      List<Player> players = nibbler.level().getEntitiesOfClass(
        Player.class,
        nibbler.getBoundingBox().inflate(20),
        EntitySelector.NO_SPECTATORS
      );
      if (players.isEmpty()) {
        return false;
      }
      targetPlayer = players.get(0);
      return true;
    }

    @Override
    public boolean canContinueToUse() {
      return targetPlayer != null && targetPlayer.isAlive() && nibbler.distanceTo(targetPlayer) > 1.0;
    }

    @Override
    public void tick() {
      if (targetPlayer != null) {
        nibbler.getNavigation().moveTo(targetPlayer, 5.0);

        if (nibbler.distanceTo(targetPlayer) <= 1.0) {
          // Deal damage to the player
          targetPlayer.hurt(nibbler.damageSources().mobAttack(nibbler), 4.0F);
        }
      }
    }

    @Override
    public void stop() {
      targetPlayer = null;
    }
  }

}

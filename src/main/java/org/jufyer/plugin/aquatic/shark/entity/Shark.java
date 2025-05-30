package org.jufyer.plugin.aquatic.shark.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.aquatic.Main;

public class Shark extends net.minecraft.world.entity.animal.Dolphin {

  public static final NamespacedKey SHARK_KEY = new NamespacedKey(Main.getInstance(), "SHARK");
  public static final NamespacedKey DOLPHIN_SHARK_KEY = new NamespacedKey(Main.getInstance(), "DOLPHIN_SHARK");

  private ArmorStand armorStand;

  public Shark(Location loc) {
    super(EntityType.DOLPHIN, ((CraftWorld) loc.getWorld()).getHandle());

    /*this.armorStand = loc.getWorld().spawn(loc, ArmorStand.class, as -> {
      as.getPersistentDataContainer().set(SHARK_KEY, PersistentDataType.BOOLEAN, true);
      as.setInvulnerable(true);
      as.setPersistent(true);
      as.setVisible(false);
      as.setHelmet(new ItemStack(Material.DIAMOND));
    });*/

    this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
    this.getBukkitEntity().getPersistentDataContainer().set(SHARK_KEY, PersistentDataType.BOOLEAN, true);
    this.setCustomName(Component.nullToEmpty("shark"));
    this.setCustomNameVisible(false);
    this.setInvulnerable(false);
    this.setInvisible(true);

    ((CraftWorld) loc.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

   // this.getBukkitEntity().addPassenger(armorStand);
  }

  @Override
  public void setTreasurePos(BlockPos treasurePos) {
  }

  @Override
  public boolean gotFish() {
    return (Boolean) true;
  }

  @Override
  public void setGotFish(boolean hasFish) {

  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
    this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2000000476837158D, true));
    this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0D, 1.0D));
    this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Guardian.class})).setAlertOthers());
  }

  @Override
  protected int increaseAirSupply(int air) {
    return this.getMaxAirSupply();
  }

  @Override
  public void tick() {
    super.tick();

    if (armorStand != null && !armorStand.isDead()) {
      Location sharkLocation = this.getBukkitEntity().getLocation();
      armorStand.teleport(sharkLocation);
    }

    if (armorStand != null && !armorStand.isDead()) {
      Location sharkLoc = this.getBukkitEntity().getLocation();
      armorStand.setRotation(sharkLoc.getYaw(), sharkLoc.getPitch());
    }

    if (!this.isInWaterRainOrBubble()) {
      this.setMoisntessLevel(this.getMoistnessLevel() - 1);
      if (this.getMoistnessLevel() <= 0) {
        this.hurt(this.damageSources().dryOut(), 1.0F);
      }
    }

    if (this.onGround()) {
      this.setDeltaMovement(this.getDeltaMovement().add((double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
      this.setYRot(this.random.nextFloat() * 360.0F);
      this.setOnGround(false);
      this.hasImpulse = true;
    }

    if (this.level().isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
      Vec3 vec3d = this.getViewVector(0.0F);
      float f = Mth.cos(this.getYRot() * 0.017453292F) * 0.3F;
      float f1 = Mth.sin(this.getYRot() * 0.017453292F) * 0.3F;
      float f2 = 1.2F - this.random.nextFloat() * 0.7F;

      for (int i = 0; i < 2; ++i) {
        this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3d.x * (double) f2 + (double) f, this.getY() - vec3d.y, this.getZ() - vec3d.z * (double) f2 + (double) f1, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3d.x * (double) f2 - (double) f, this.getY() - vec3d.y, this.getZ() - vec3d.z * (double) f2 - (double) f1, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  public boolean canBeLeashed() {
    return false;
  }
}

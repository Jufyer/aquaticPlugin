package org.jufyer.plugin.aquatic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.aquatic.commands.GiveOceanGlider;
import org.jufyer.plugin.aquatic.commands.SpawnOceanGlider;
import org.jufyer.plugin.aquatic.oceanGlider.entity.listeners.OceanGliderListeners;
import org.jufyer.plugin.aquatic.oceanGlider.item.OceanGliderItem;
import org.jufyer.plugin.aquatic.whale.entity.Whale;
import org.jufyer.plugin.aquatic.whale.listeners.customBlockListeners;
import org.jufyer.plugin.aquatic.whale.listeners.generateStructure;
import org.jufyer.plugin.aquatic.whale.listeners.rightClickListener;
import org.jufyer.plugin.aquatic.whale.listeners.spawnListener;

public final class Main extends JavaPlugin implements Listener {
  private static Main instance;

  public static final int CMDBarnacle = 21;
  public static final int CMDBarnacleSpike = 219;
  public static final int CMDBarnacleSpikeExtended = 2195;
  public static final int CMDOceanGlider = 157;
  public static final int CMDOceanGliderEntity = 1575;

  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    new OceanGliderItem();

    getCommand("spawnOceanGlider").setExecutor(new SpawnOceanGlider());
    getCommand("giveOceanGlider").setExecutor(new GiveOceanGlider());
    getCommand("spawnWhale").setExecutor(this);

    Bukkit.getPluginManager().registerEvents(new spawnListener(), this);
    Bukkit.getPluginManager().registerEvents(new rightClickListener(), this);
    Bukkit.getPluginManager().registerEvents(new generateStructure(), this);
    Bukkit.getPluginManager().registerEvents(new customBlockListeners(), this);
    Bukkit.getPluginManager().registerEvents(new OceanGliderListeners(), this);

    //Custom Recipe:
    ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Barnacles.getItemMeta();
    if (meta != null) {
      meta.setDisplayName("§rBarnacle Spike");
      meta.setCustomModelData(CMDBarnacleSpike);
      Barnacles.setItemMeta(meta);
    }

    ItemStack Barnacle = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta Bmeta = Barnacle.getItemMeta();
    Bmeta.setDisplayName("§rBarnacle");
    Bmeta.setCustomModelData(CMDBarnacle);
    Barnacle.setItemMeta(Bmeta);

    ShapedRecipe barnacles_spike = new ShapedRecipe(Barnacles);
    barnacles_spike.shape(" B ", "BIB", " R ");

    barnacles_spike.setIngredient('B', new RecipeChoice.ExactChoice(Barnacle)); // Custom Item
    barnacles_spike.setIngredient('I', Material.IRON_INGOT);
    barnacles_spike.setIngredient('R', Material.REDSTONE);

    getServer().addRecipe(barnacles_spike);
  }

  @Override
  public void onDisable(){

  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player player = (Player) sender;
    boolean isOP = player.isOp();

    if (isOP){
      new Whale(player.getLocation());
    }

    return false;
  }
}

package org.jufyer.plugin.aquatic;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jufyer.plugin.aquatic.bannerOnBoats.listeners.BannerOnBoatsListeners;
import org.jufyer.plugin.aquatic.brewing.BrewingControler;
import org.jufyer.plugin.aquatic.brewing.BrewingRecipe;
import org.jufyer.plugin.aquatic.debug.commands.*;
import org.jufyer.plugin.aquatic.goldfish.listeners.GoldfishListeners;
import org.jufyer.plugin.aquatic.nibblers.listeners.NibblerListeners;
import org.jufyer.plugin.aquatic.oyster.listener.FurnaceListeners;
import org.jufyer.plugin.aquatic.oyster.listener.OysterListeners;
import org.jufyer.plugin.aquatic.oyster.listener.PotionOfLuckListeners;
import org.jufyer.plugin.aquatic.prismarineOceanRuin.GeneratePrismarineOceanRuin;
import org.jufyer.plugin.aquatic.oceanGlider.entity.listeners.OceanGliderListeners;
import org.jufyer.plugin.aquatic.recpies.api.RecipeType;
import org.jufyer.plugin.aquatic.recpies.api.RecipesAPI;
import org.jufyer.plugin.aquatic.recpies.impl.domains.ItemRecipe;
import org.jufyer.plugin.aquatic.recpies.impl.domains.recipes.RecipeBuilder;
import org.jufyer.plugin.aquatic.shark.entity.Shark;
import org.jufyer.plugin.aquatic.shark.listeners.SharkListeners;
import org.jufyer.plugin.aquatic.spikyPiston.listeners.SpikyPistonListeners;
import org.jufyer.plugin.aquatic.whale.listeners.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jufyer.plugin.aquatic.oyster.listener.OysterListeners.COOKED_OYSTER_KEY;
import static org.jufyer.plugin.aquatic.oyster.listener.OysterListeners.RAW_OYSTER_KEY;

public final class Main extends JavaPlugin implements Listener {

  /*
  Tested versions:
    -

   */

  private File customConfigFile;
  private FileConfiguration customConfig;

  private static Main instance;
  public static BrewingControler bc;

  public static final int CMDBarnacle = 21;
  public static final int CMDBarnacleSpike = 219;
  public static final int CMDBarnacleSpikeExtended = 2195;

  public static final int CMDOceanGlider = 157;
  public static final int CMDOceanGliderEntity = 1575;

  public static final int CMDNibblerBucket = 149221;

  public static final int CMDSharkTooth = 1982015;
  public static final int CMDSpikyPistonItem = 191619;

  public static final int CMDSpikyPistonNorth = 191641;
  public static final int CMDSpikyPistonEast = 19165;
  public static final int CMDSpikyPistonSouth = 191619;
  public static final int CMDSpikyPistonWest = 191623;
  public static final int CMDSpikyPistonUp = 191621;
  public static final int CMDSpikyPistonDown = 19164;

  public static final int CMDSpikyPistonExtendedNorth = 1916514;
  public static final int CMDSpikyPistonExtendedEast = 191655;
  public static final int CMDSpikyPistonExtendedSouth = 1916519;
  public static final int CMDSpikyPistonExtendedWest = 1916523;
  public static final int CMDSpikyPistonExtendedUp = 1916521;
  public static final int CMDSpikyPistonExtendedDown = 191654;

  public static final int CMDOyster = 1525;
  public static final int CMDOysterWithPearl = 15252316;
  public static final int CMDRawOyster = 1811525;
  public static final int CMDDeadOyster = 451525;
  public static final int CMDPearl = 165;
  public static final int CMDPotionOfLuck = 161515;
  public static final int CMDCookedOyster = 3151525;

  public static final int CMDWhiteBoatBanner = 23821521;

  public static final int CMDGoldfishBucket = 715221;

  public static Main getInstance() {
    return instance;
  }


  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    createCustomConfig();
    RecipesAPI recipesAPI = new RecipesAPI(Main.getInstance(), false);
    Bukkit.getPluginManager().registerEvents(new ResourcePackListeners(), this);

    getLogger().info("The following features are enabled: ");

    if (getCustomConfig().getBoolean("Banners on boats")){
      getLogger().info("Banners on boats");

      Bukkit.getPluginManager().registerEvents(new BannerOnBoatsListeners(), this);
    }
    if (getCustomConfig().getBoolean("Goldfishes")){
      getLogger().info("Goldfishes");

      Bukkit.getPluginManager().registerEvents(new GoldfishListeners(), this);
    }
    if (getCustomConfig().getBoolean("Nibblers")){
      getLogger().info("Nibblers");

      Bukkit.getPluginManager().registerEvents(new GeneratePrismarineOceanRuin(), this);
      Bukkit.getPluginManager().registerEvents(new NibblerListeners(), this);
    }
    if (getCustomConfig().getBoolean("Ocean Glider")){
      getLogger().info("Ocean Glider");

      Bukkit.getPluginManager().registerEvents(new OceanGliderListeners(), this);

      ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta OceanGlidermeta = oceanGlider.getItemMeta();
      OceanGlidermeta.setCustomModelData(Main.CMDOceanGlider);
      OceanGlidermeta.setDisplayName("§rOcean Glider");
      oceanGlider.setItemMeta(OceanGlidermeta);

      ItemRecipe oceanGliderRecipe = new RecipeBuilder()
        .setType(RecipeType.CRAFTING_SHAPED)
        .setName("ocean_glider")
        .setResult(oceanGlider)
        .setAmount(1)
        .setPattern("N N", "PPP", "NHN")
        .addIngredient(Material.NAUTILUS_SHELL, 'N')
        .addIngredient(Material.PRISMARINE_SHARD, 'P')
        .addIngredient(Material.HEART_OF_THE_SEA,'H')
        .build();

      recipesAPI.addRecipe(oceanGliderRecipe);

//      ShapedRecipe OceanGlider = new ShapedRecipe(oceanGlider);
//      OceanGlider.shape("N N", "PPP", "NHN");
//
//      OceanGlider.setIngredient('N', Material.NAUTILUS_SHELL);
//      OceanGlider.setIngredient('P', Material.PRISMARINE_SHARD);
//      OceanGlider.setIngredient('H', Material.HEART_OF_THE_SEA);
//
//      getServer().addRecipe(OceanGlider);
    }
    if (getCustomConfig().getBoolean("Oysters")){
      getLogger().info("Oysters");

      Bukkit.getPluginManager().registerEvents(new OysterListeners(), this);
      Bukkit.getPluginManager().registerEvents(new PotionOfLuckListeners(), this);
      Bukkit.getPluginManager().registerEvents(new FurnaceListeners(), this);

      ItemStack raw_oyster = new org.bukkit.inventory.ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta rawOysterItemMetameta = raw_oyster.getItemMeta();
      rawOysterItemMetameta.setCustomModelData(CMDRawOyster);
      rawOysterItemMetameta.setDisplayName("§rRaw Oyster");
      rawOysterItemMetameta.getPersistentDataContainer().set(RAW_OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
      raw_oyster.setItemMeta(rawOysterItemMetameta);

      ItemStack cooked_oyster = new org.bukkit.inventory.ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta cookedOysterItemMeta = cooked_oyster.getItemMeta();
      cookedOysterItemMeta.setCustomModelData(CMDCookedOyster);
      cookedOysterItemMeta.setDisplayName("§rCooked Oyster");
      cookedOysterItemMeta.getPersistentDataContainer().set(COOKED_OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
      cooked_oyster.setItemMeta(cookedOysterItemMeta);

      //getServer().addRecipe(new FurnaceRecipe(cooked_oyster, raw_oyster.getType()));

      ItemRecipe oysterFurnace = new RecipeBuilder()
        .setType(RecipeType.SMELTING)
        .setName("oyster_to_cooked_oyster")
        .setResult(cooked_oyster)
        .setAmount(1)
        .addIngredient(raw_oyster.getType())
        .setCookingTime(200)
        .build();

      recipesAPI.addRecipe(oysterFurnace);

      addBrewingRecipe();
    }
    if (getCustomConfig().getBoolean("Sharks")){
      getLogger().info("Sharks");

      Bukkit.getPluginManager().registerEvents(new SharkListeners(), this);
      Bukkit.getPluginManager().registerEvents(new SpikyPistonListeners(), this);

      ItemStack sharkTooth = new ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta SharkToothMeta = sharkTooth.getItemMeta();
      SharkToothMeta.setDisplayName("§rShark Tooth");
      SharkToothMeta.setCustomModelData(Main.CMDSharkTooth);
      sharkTooth.setItemMeta(SharkToothMeta);

      ItemStack spikyPiston = new ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta SpikyPistonMeta = spikyPiston.getItemMeta();
      SpikyPistonMeta.setDisplayName("§rSpiky Piston");
      SpikyPistonMeta.setCustomModelData(CMDSpikyPistonItem);
      spikyPiston.setItemMeta(SpikyPistonMeta);

      ItemRecipe spikyPistonRecipe = new RecipeBuilder()
        .setType(RecipeType.CRAFTING_SHAPED)
        .setName("spiky_piston")
        .setResult(spikyPiston)
        .setAmount(1)
        .setPattern("TTT", "TPT", "   ")
        .addIngredient(sharkTooth, 'T')
        .addIngredient(Material.PISTON, 'P')
        .build();

      recipesAPI.addRecipe(spikyPistonRecipe);

//      ShapedRecipe SpikyPiston = new ShapedRecipe(spikyPiston);
//      SpikyPiston.shape("TTT", "TPT", "   ");
//
//      SpikyPiston.setIngredient('T', new RecipeChoice.ExactChoice(sharkTooth)); // Custom Item
//      SpikyPiston.setIngredient('P', Material.PISTON);
//
//      getServer().addRecipe(SpikyPiston);
    }
    if (getCustomConfig().getBoolean("Whales")){
      getLogger().info("Whales");

      Bukkit.getPluginManager().registerEvents(new WhaleListeners(), this);
      Bukkit.getPluginManager().registerEvents(new spawnListener(), this);
      Bukkit.getPluginManager().registerEvents(new rightClickListener(), this);
      Bukkit.getPluginManager().registerEvents(new generateStructure(), this);
      Bukkit.getPluginManager().registerEvents(new customBlockListeners(), this);

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

      ItemRecipe barnaclesSpikeRecipe = new RecipeBuilder()
        .setType(RecipeType.CRAFTING_SHAPED)
        .setName("barnacles_spike")
        .setResult(Barnacles)
        .setAmount(1)
        .setPattern(" B ", "BIB", " R ")
        .addIngredient(Barnacle, 'B')
        .addIngredient(Material.IRON_INGOT, 'I')
        .addIngredient(Material.REDSTONE, 'R')
        .build();

      recipesAPI.addRecipe(barnaclesSpikeRecipe);

//      ShapedRecipe barnacles_spike = new ShapedRecipe(Barnacles);
//      barnacles_spike.shape(" B ", "BIB", " R ");
//
//      barnacles_spike.setIngredient('B', new RecipeChoice.ExactChoice(Barnacle)); // Custom Item
//      barnacles_spike.setIngredient('I', Material.IRON_INGOT);
//      barnacles_spike.setIngredient('R', Material.REDSTONE);
//
//      getServer().addRecipe(barnacles_spike);
    }
    if (getCustomConfig().getBoolean("Debug")){
      getLogger().info("Debug mode on!");
      getCommand("spawnShark").setExecutor(new spawnShark());
      getCommand("spawnNibbler").setExecutor(new spawnNibbler());
      getCommand("spawnGoldfish").setExecutor(new spawnGoldfish());
      getCommand("spawnWhale").setExecutor(new spawnWhale());
      getCommand("spawnOyster").setExecutor(new spawnOyster());
    }
  }

  @Override
  public void onDisable(){

  }

  private void saveBlockData(Player player) {
    Map<String, String> blockData = new HashMap<>();
    List<Material> targetMaterials = List.of(
      Material.PRISMARINE,
      Material.PRISMARINE_SLAB,
      Material.PRISMARINE_STAIRS,
      Material.DARK_PRISMARINE,
      Material.DARK_PRISMARINE_STAIRS,
      Material.PRISMARINE_BRICKS,
      Material.CHEST
    );

    int radius = 15;
    for (int x = -radius; x <= radius; x++) {
      for (int y = -radius; y <= radius; y++) {
        for (int z = -radius; z <= radius; z++) {
          Block block = player.getLocation().add(x, y, z).getBlock();
          if (targetMaterials.contains(block.getType())) {
            String relativeCoordinates = x + ", " + y + ", " + z;
            String blockType = block.getType().name();
            String rotation = "NONE";

            if (block.getBlockData() instanceof Directional) {
              Directional directional = (Directional) block.getBlockData();
              rotation = directional.getFacing().name();
            } else if (block.getBlockData() instanceof Orientable) {
              Orientable orientable = (Orientable) block.getBlockData();
              rotation = orientable.getAxis().name();
            } else if (block.getBlockData() instanceof Bisected) {
              Bisected bisected = (Bisected) block.getBlockData();
              rotation = bisected.getHalf().name();
            } else if (block.getBlockData() instanceof Slab) {
              Slab slab = (Slab) block.getBlockData();
              rotation = slab.getType().name();
            }

            blockData.put(relativeCoordinates, "Type: " + blockType + ", Rotation: " + rotation);
          }
        }
      }
    }

    blockData.forEach((coordinates, data) -> {
      getLogger().info("Coordinates: " + coordinates + " | " + data);
    });
  }

  public static void addBrewingRecipe() {
    ItemStack resetPotion = new ItemStack(Material.POTION);
    resetPotion.setAmount(1);
    ItemMeta meta = resetPotion.getItemMeta();
    meta.setCustomModelData(CMDPotionOfLuck);
    meta.setDisplayName("§rPotion of Luck");
    ((PotionMeta) meta).setColor(Color.LIME);
    ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 900, 0), true);

    resetPotion.setItemMeta(meta);

    ItemStack pearl = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta pearlMeta = pearl.getItemMeta();
    pearlMeta.setCustomModelData(Main.CMDPearl);
    pearlMeta.setDisplayName("§rPearl");
    pearl.setItemMeta(pearlMeta);

    ItemStack bottle = new ItemStack(Material.POTION, 1);
    PotionMeta pmeta = (PotionMeta) bottle.getItemMeta();
    pmeta.setBasePotionType(PotionType.WATER);
    bottle.setItemMeta(pmeta);

    bc = new BrewingControler(Main.getInstance());
    BrewingRecipe recipe = new BrewingRecipe(
      new NamespacedKey(Main.getInstance(), "customPotion"),
      resetPotion,
      pearl,
      bottle
    );
    bc.addRecipe(recipe);
  }

  public FileConfiguration getCustomConfig() {
    return this.customConfig;
  }

  private void createCustomConfig() {
    customConfigFile = new File(getDataFolder(), "config.yml");
    if (!customConfigFile.exists()) {
      getDataFolder().mkdirs();
      try {
        customConfigFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    customConfig = new YamlConfiguration();
    try {
      customConfig.load(customConfigFile);

      customConfig.options().copyDefaults(true);
      customConfig.addDefault("Banners on boats", true);
      customConfig.addDefault("Goldfishes", true);
      customConfig.addDefault("Nibblers", true);
      customConfig.addDefault("Ocean Glider", true);
      customConfig.addDefault("Oysters", true);
      customConfig.addDefault("Sharks", true);
      customConfig.addDefault("Whales", true);
      customConfig.addDefault("Debug", false);

      customConfig.save(customConfigFile);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }
}

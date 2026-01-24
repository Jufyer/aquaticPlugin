package org.jufyer.plugin.aquatic;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResourcePackListeners implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (!player.isOnline()) return;

      List<ResourcePackInfo> packs = new ArrayList<>();

      packs.add(createPack(
        "https://download.mc-packs.net/pack/d934a87434bf852ac16d9abbf563b29734c8f1ae.zip",
        "d934a87434bf852ac16d9abbf563b29734c8f1ae"
      ));

      if (!packs.isEmpty()) {
        ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
          .packs(packs)
          .required(true)
          .prompt(Component.text("This server requires a custom resource pack to play!", NamedTextColor.GREEN))
          .build();

        player.sendResourcePacks(request);
      }

    }, 10L);
  }

  private ResourcePackInfo createPack(String url, String hash) {
    return ResourcePackInfo.resourcePackInfo()
      .id(UUID.randomUUID())
      .uri(URI.create(url))
      .hash(hash)
      .build();
  }
}

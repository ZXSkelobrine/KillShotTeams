package com.github.ZXSkelobrine.KillShot.teams.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;

public class KillShotListeners extends TeamsPlugin implements Listener {

	public KillShotListeners(Plugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (player.hasMetadata("killshotteam.hasteam.hasteamchat.ison")) {
			if (player.getMetadata("killshotteam.hasteam.hasteamchat.ison").get(0).asBoolean()) {
				String teamKey = "teams." + plugin.getConfig().getString("killshotteams.hasteam.team") + ".members";
				List<String> players = plugin.getConfig().getStringList(teamKey);
				plugin.getLogger().info(players.size() + "");
				for (String s : players) {
					Bukkit.getPlayer(UUID.fromString(s)).sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KSTChat]" + player.getDisplayName() + ": " + event.getMessage());
				}
				event.setCancelled(true);
			} else {
			}
		} else {
		}
	}
}

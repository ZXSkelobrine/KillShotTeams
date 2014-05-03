package com.github.ZXSkelobrine.KillShot.teams.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class KillShotListeners extends TeamsPlugin implements Listener {

	public KillShotListeners(Plugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (plugin.getConfig().getBoolean("addTeam")) event.setMessage(ChatColor.BLACK + "" + ChatColor.ITALIC + "[" + SimpleMeta.getPlayerTeam(player) + "]" + ":    " + ChatColor.RESET + event.getMessage());
		if (player.hasMetadata("killshotteam.hasteam.hasteamchat.ison")) {
			if (player.getMetadata("killshotteam.hasteam.hasteamchat.ison").get(0).asBoolean()) {
				String teamKey = "teams." + SimpleMeta.getPlayerTeam(player) + ".members";
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

	@EventHandler
	public void onPlayerDamageEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if (event.getEntity() instanceof Player) {
				Player damagee = (Player) event.getEntity();
				String team1 = SimpleMeta.getPlayerTeam(damager);
				String team2 = SimpleMeta.getPlayerTeam(damagee);
				if (team1.equals(team2)) {
					if (!plugin.getConfig().getBoolean("teams." + team1 + ".ff")) {
						super.message(damager, "You cannot attack people in your team right now.");
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
/**
 * 
 * <pre>
 * List<String> teams = getConfig().getStringList("allteams");
 * 		if (teams.size() > 1) {
 * 			Player secondOwner = Bukkit.getPlayer(UUID.fromString(getConfig().getString("teams." + teams.get(1) + ".owner")));
 * 			if (!secondOwner.hasMetadata("killshotteams.hasteam.ownsteam")) {
 * 				getLogger().warning("Updated Detected: setting metadatas. Prepare for lag - this could take a while!");
 * 				for (String team : teams) {
 * 					String baseAddress = "teams." + team;
 * 					Player player = Bukkit.getPlayer(UUID.fromString(getConfig().getString(baseAddress + ".owner")));
 * 					String name = getConfig().getString(baseAddress + ".name");
 * 					String password = getConfig().getString(baseAddress + ".pass");
 * 					SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam", true, this);
 * 					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.team", name, this);
 * 					SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam.ownsteam", true, this);
 * 					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.name", name, this);
 * 					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.pass", password, this);
 * 					List<String> players = getConfig().getStringList(baseAddress + ".members");
 * 					for (String memberUUID : players) {
 * 						Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
 * 						SimpleMeta.setStringnMetadata(member, "killshotteams.hasteam.team", name, this);
 * 					}
 * 					getLogger().info("Set team: " + team + " metadata to owner: " + player.getDisplayName());
 * 				}
 * 			}
 * }
 * 
 * <pre>
 */

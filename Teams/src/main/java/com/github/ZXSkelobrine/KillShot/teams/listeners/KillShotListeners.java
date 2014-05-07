package com.github.ZXSkelobrine.KillShot.teams.listeners;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
				if (SimpleMeta.isProtected(damagee)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerCommandEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("spawn")) {
			if (SimpleMeta.booleanMetaCheck(event.getPlayer(), "killshotteams.spawn.allowCommandProtection")) {
				if (!SimpleMeta.isAccountSuspended(event.getPlayer())) {
					if (!SimpleMeta.isProtected(event.getPlayer())) {
						SimpleMeta.setBooleanMetadata(event.getPlayer(), "killshotteams.spawn.protection", true);
						super.message(event.getPlayer(), "You have spawn protection");
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if (!new File(new File("plugins/KillShotTeams/players/" + event.getPlayer().getUniqueId().toString() + ".txt").getAbsolutePath()).exists()) {
			try {
				new File(new File("plugins/KillShotTeams/players/" + event.getPlayer().getUniqueId().toString() + ".txt").getAbsolutePath()).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (SimpleMeta.isTeamKicked(event.getPlayer())) {
			long current = System.nanoTime();
			long seconds = SimpleMeta.getTeamKickTime(event.getPlayer());
			if (current > seconds) {
				SimpleMeta.removeTeamKick(event.getPlayer());
				super.message(event.getPlayer(), "Your kick has ended");
				super.joinTeam(event.getPlayer(), SimpleMeta.getPlayerTeam(event.getPlayer()));
			}
		}
		if (SimpleMeta.isAccountKicked(event.getPlayer())) {
			super.message(event.getPlayer(), "Your account is suspended.");
			long current = System.nanoTime();
			long seconds = SimpleMeta.getAccountKickTime(event.getPlayer());
			if (current > seconds) {
				SimpleMeta.removeAccountKick(event.getPlayer());
				super.message(event.getPlayer(), "Your kick has ended");
				super.joinTeam(event.getPlayer(), SimpleMeta.getPlayerTeam(event.getPlayer()));
			}
		}
		if (SimpleMeta.isAccountBanned(event.getPlayer())) {
			super.message(event.getPlayer(), "Your account is suspended.");
		}
		if (SimpleMeta.booleanMetaCheck(event.getPlayer(), "killshotteams.spawn.protection")) {
			if (!SimpleMeta.isProtected(event.getPlayer())) {
				super.message(event.getPlayer(), "You have lost spawn protection");
			}
		}
	}

}

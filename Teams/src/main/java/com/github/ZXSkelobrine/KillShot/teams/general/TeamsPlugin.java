package com.github.ZXSkelobrine.KillShot.teams.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeamsPlugin {
	protected static Plugin plugin;

	public TeamsPlugin(Plugin plugin) {
		TeamsPlugin.plugin = plugin;
	}

	public void message(Player player, String message) {
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShotTeams] " + ChatColor.RESET + ChatColor.GREEN + message);
	}

	public void message(CommandSender player, String message) {
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShotTeams] " + ChatColor.RESET + ChatColor.GREEN + message);
	}

	public void broadcast(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShotTeams] " + ChatColor.RESET + ChatColor.GREEN + message);
		}
	}
}

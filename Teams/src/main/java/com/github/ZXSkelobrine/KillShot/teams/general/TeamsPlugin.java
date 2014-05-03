package com.github.ZXSkelobrine.KillShot.teams.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeamsPlugin {
	protected static Plugin plugin;
	String[] help = new String[] { ChatColor.GREEN + "KillShot Teams Help: (Light gray are player commands, Dark gray are owner commands.", ChatColor.GRAY + "/t - Prints this message.", ChatColor.GRAY + "/t create [name] [password] - Creates a team with the given name and password.", ChatColor.GRAY + "/t info [name] - Shows info about a given team.", ChatColor.GRAY + "/t chat - Toggles team chat.", ChatColor.GRAY + "/t hq - Teleports to the team hq.", ChatColor.GRAY + "/t rally - Teleports  to the rally point.", ChatColor.DARK_GRAY + "/t password [password] - Sets the password for the the team.", ChatColor.DARK_GRAY + "/t kick [player] - Kicks a player from the team.", ChatColor.DARK_GRAY + "/t ff [on/off] - Toggles " + ChatColor.ITALIC + "'friendly fire'" + ChatColor.RESET + ChatColor.GRAY + " (team damage).", ChatColor.DARK_GRAY + "/t promote [player] - Promotes a player to manager.", ChatColor.DARK_GRAY + "/t demote [player] - Demotes a player from manager.", ChatColor.DARK_GRAY + "/t sethq - Sets the HQ point for the team.", ChatColor.DARK_GRAY + "/t setrally - Sets the rally point for the team." };

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

	public void printHelp(Player player) {
		player.sendMessage(help);
	}

	public boolean checkPermissions(Player player, String command) {
		if (player.hasPermission("killshotteams.*")) return true;
		switch (command) {
		case "t":
			return player.hasPermission("killshotteams.help");
		case "create":
			return player.hasPermission("killshotteams.create");
		case "join":
			return player.hasPermission("killshotteams.join");
		case "leave":
			return player.hasPermission("killshotteams.leave");
		case "info":
			return player.hasPermission("killshotteams.info");
		case "chat":
			return player.hasPermission("killshotteams.chat");
		case "hq":
			return player.hasPermission("killshotteams.hq");
		case "rally":
			return player.hasPermission("killshotteams.rally");
		case "disband":
			return player.hasPermission("killshotteams.disband");
		case "sethq":
			return player.hasPermission("killshotteams.sethq");
		case "setrally":
			return player.hasPermission("killshotteams.setrally");
		case "password":
			return player.hasPermission("killshotteams.password");
		case "ff":
			return player.hasPermission("killshotteams.ff");
		}
		return false;
	}
}

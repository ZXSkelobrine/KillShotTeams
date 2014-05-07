package com.github.ZXSkelobrine.KillShot.teams.general;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class TeamsPlugin {
	protected static Plugin plugin;
	String[] p1_players = new String[] { ChatColor.GREEN + "[KST] KillShot Teams Help: Basic Player Commands", ChatColor.GRAY + "/t create [Team Name] [Password] - Creates a team with the given name and password.", ChatColor.GRAY + "/t - Shows this message.", ChatColor.GRAY + "/t join [Team Name] [Password] - Joins the given team.", ChatColor.GRAY + "/t leave - Leaves your current team.", ChatColor.GRAY + "/t info [Team Name] - Shows info about a given team.", ChatColor.GRAY + "/t chat - Toggles team chat.", ChatColor.GRAY + "/t hq - Teleports to the team hq.", ChatColor.GRAY + "/t rally - Teleports  to the rally point.", ChatColor.GRAY + "/t spawnprotection - Grants spawn protection next time you teleport to spawn." };
	String[] p2_managers = new String[] { ChatColor.GREEN + "[KST] KillShot Teams Help: Team Manager Commands", "/t sethq - Sets the HQ point for your team to your location", "/t setrally - Sets the rally point for your team to your location", "/t password [New Password] - Sets the teams password", "/t kick [Player] - Kicks the given player." };
	String[] p3_owners = new String[] { ChatColor.GREEN + "[KST] KillShot Teams Help: Team Owner Commands: ", ChatColor.GRAY + "/t disband [Teams Name] [Password] - Disbands the team if you are the owner.", ChatColor.GRAY + "/t sethq - Sets the HQ point for your team to your location", ChatColor.GRAY + "/t setrally - Sets the rally point for your team to your location", ChatColor.GRAY + "/t password [New Password] - Sets the teams password", ChatColor.GRAY + "/t ff - Toggle 'friendly fire'", ChatColor.GRAY + "/t promote [Player] - Promotes the given player to manager", ChatColor.GRAY + "/t demote [Player] - Demotes the given player from manager", ChatColor.GRAY + "/t kick [Player] - Kicks the given player." };
	String[] p4_admins = new String[] { ChatColor.GREEN + "[KST] KillShot Teams Help: KST Admin Commands: ", ChatColor.GRAY + "/t akick [Player] [Time] [Reason] - Kicks the players account for the given time.", ChatColor.GRAY + "/t details [Player] - Shows details on all kicks and bans for the player.", ChatColor.GRAY + "/t ban [Player] [Reason] - Bans the players account for the given reason.", ChatColor.GRAY + "/t admin - Changes your mode from play (Survival) to admin (Creative) and vice-versa.", };

	public TeamsPlugin(Plugin plugin) {
		TeamsPlugin.plugin = plugin;
	}

	protected static void message(Player player, String message) {
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShotTeams] " + ChatColor.RESET + ChatColor.GREEN + message);
	}

	protected static void message(CommandSender player, String message) {
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShotTeams] " + ChatColor.RESET + ChatColor.GREEN + message);
	}

	protected static void broadcast(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShotTeams] " + ChatColor.RESET + ChatColor.GREEN + message);
		}
	}

	public void printHelp(Player player) {
		if (SimpleMeta.isManager(player)) {
			String[] help = concatenate(p1_players, p2_managers);
			player.sendMessage(help);

		} else if (SimpleMeta.ownsTeam(player)) {
			String[] help1 = concatenate(p1_players, p2_managers);
			String[] help = concatenate(help1, p3_owners);
			player.sendMessage(help);

		} else if (SimpleMeta.isAdmin(player)) {
			String[] help1 = concatenate(p1_players, p2_managers);
			String[] help2 = concatenate(help1, p3_owners);
			String[] help = concatenate(help2, p4_admins);
			player.sendMessage(help);

		} else {
			player.sendMessage(p1_players);
		}
	}

	public void joinTeam(Player player, String teamName) {
		List<String> current = plugin.getConfig().getStringList("teams." + teamName + ".members");
		current.add(player.getUniqueId().toString());
		SimpleMeta.addStringListToConfig("teams." + teamName + ".members", current);
		SimpleMeta.setPlayerTeams(player, teamName);
		message(player, "You have successfully joined the " + teamName + " team!");
	}

	public void leaveTeam(Player player) {
		String team = SimpleMeta.getPlayerTeam(player);
		List<String> uuids = plugin.getConfig().getStringList("teams." + team + ".members");
		uuids.remove(player.getUniqueId().toString());
		plugin.getConfig().set("teams." + team + ".members", uuids);
		SimpleMeta.addStringListToConfig("teams." + team + ".members", uuids);
		for (String uuid : uuids) {
			message(Bukkit.getPlayer(UUID.fromString(uuid)), "Player " + player.getName() + " has left your team!");
		}
		message(player, "You have successfully left " + team);
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
		case "kick":
			return player.hasPermission("killshotteams.kick");
		case "ovr:kick":
			return player.hasPermission("killshotteams.admin.kick");
		case "ovr:ban":
			return player.hasPermission("killshotteams.admin.ban");
		case "ovr:admin":
			return player.hasPermission("killshotteams.admin.admin");
		}
		return false;
	}

	public <T> T[] concatenate(T[] A, T[] B) {
		int aLen = A.length;
		int bLen = B.length;

		@SuppressWarnings("unchecked")
		T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);

		return C;
	}
}

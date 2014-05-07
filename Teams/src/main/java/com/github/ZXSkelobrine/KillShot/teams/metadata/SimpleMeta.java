package com.github.ZXSkelobrine.KillShot.teams.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.DoNotUse;
import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class SimpleMeta extends TeamsPlugin {
	public static final long NANO_MODIFIER = 1000000000;

	public SimpleMeta(Plugin plugin) {
		super(plugin);
	}

	// Meta stuff

	/**
	 * This method adds a boolean value to a players metadata.
	 * 
	 * @param player
	 *            to add the metadata to.
	 * @param key
	 *            to set the metadata to.
	 * @param value
	 *            to set the metadata.
	 */
	@DoNotUse
	public static void setBooleanMetadata(Player player, String key, boolean value) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	/**
	 * This methods checks if a player has a boolean metadata and if so returns
	 * its value.
	 * 
	 * @param player
	 *            to get the metadata from.
	 * @param key
	 *            to the metadata.
	 * @return the value of the metadata or false if the player does not have
	 *         the metadata.
	 */
	@DoNotUse
	public static boolean booleanMetaCheck(Player player, String key) {
		if (player.hasMetadata(key)) {
			return player.getMetadata(key).get(0).asBoolean();
		} else {
			return false;
		}
	}

	// Config stuff

	/**
	 * This method adds a {@link String} to the {@link FileConfiguration} and
	 * then saves it.
	 * 
	 * @param key
	 *            to set the value to.
	 * @param value
	 *            to set.
	 */
	public static void addStringToConfig(String key, String value) {
		if (!plugin.getConfig().contains(key)) plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, value);
		saveConfig();
	}

	/**
	 * This method adds a String {@link List} to the {@link FileConfiguration}
	 * and then saves it.
	 * 
	 * @param key
	 *            to set the value to.
	 * @param value
	 *            to set.
	 */
	public static void addStringListToConfig(String key, List<String> values) {
		if (!plugin.getConfig().contains(key)) plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, values);
		saveConfig();
	}

	/**
	 * This method adds a {@link Boolean} to the {@link FileConfiguration} and
	 * then saves it.
	 * 
	 * @param key
	 *            to set the value to.
	 * @param value
	 *            to set.
	 */
	public static void addBooleanToConfig(String key, boolean value) {
		if (!plugin.getConfig().contains(key)) plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, value);
		saveConfig();
	}

	/**
	 * This method saves the {@link FileConfiguration}.
	 */
	public static void saveConfig() {
		try {
			plugin.getConfig().save(new File(new File("plugins/KillShotTeams/config.yml").getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Simple team stuff

	/**
	 * This changes the players team chat status to the inverse of what it was.<br>
	 * Uses:<br>
	 * {@link SimpleMeta#booleanMetaCheck(Player, String)}<br>
	 * {@link SimpleMeta#setBooleanMetadata(Player, String, boolean)}
	 * 
	 * @param player
	 *            to change the chat status
	 */
	public static void changeChatStatus(Player player) {
		if (booleanMetaCheck(player, "killshotteam.hasteam.hasteamchat.ison")) {
			SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", false);
			message(player, "Team chat has been disabled");
		} else {
			SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", true);
			message(player, "Team chat has been enabled");
		}
	}

	/**
	 * This returns the player team is they have one.<br>
	 * Uses:<br>
	 * {@link #readFromPlayerFile(Player, int)}
	 * 
	 * @param player
	 *            to get the team from.
	 * @return players team.
	 */
	public static String getPlayerTeam(Player player) {
		return readFromPlayerFile(player, 1);
	}

	/**
	 * This returns whether the player is a manager of their team or not.<br>
	 * Uses:<br>
	 * {@link #getPlayerTeam(Player)}
	 * 
	 * 
	 * @param player
	 *            to check.
	 * @return whether the player is a manager.
	 */
	public static boolean isManager(Player player) {
		List<String> managers = plugin.getConfig().getStringList("teams." + getPlayerTeam(player) + ".managers");
		boolean retur = false;
		String playerUUID = player.getUniqueId().toString();
		for (String s : managers) {
			if (s.equals(playerUUID)) retur = true;
		}
		return retur;
	}

	/**
	 * This sets the players team.<br>
	 * Uses:<br>
	 * {@link #ownsTeam(Player)}<br>
	 * {@link #isTeamKicked(Player)}<br>
	 * {@link #getTeamKickTime(Player)}<br>
	 * {@link #isAccountKicked(Player)}<br>
	 * {@link #getAccountKickTime(Player)}<br>
	 * {@link #isAccountBanned(Player)}<br>
	 * {@link #getAccountBanReason(Player)}<br>
	 * {@link #getAccountBanner(Player)}<br>
	 * {@link #writeToPlayerFile(Player, boolean, boolean, String, boolean, long, boolean, long, String, String, boolean, String, String)}
	 * 
	 * @param player
	 *            to set the team to.
	 * @param team
	 *            to set.
	 */
	public static void setPlayerTeams(Player player, String team) {
		boolean ownsTeam = ownsTeam(player);
		boolean isKicked = isTeamKicked(player);
		long getKickTime = getTeamKickTime(player);
		boolean isAccountKicked = isAccountKicked(player);
		long accountKickTime = getAccountKickTime(player);
		boolean accountBanned = isAccountBanned(player);
		String accountBanReason = getAccountBanReason(player);
		String accountBanner = getAccountBanner(player);
		writeToPlayerFile(player, true, ownsTeam, team, isKicked, getKickTime, isAccountKicked, accountKickTime, getAccountKickReason(player), getAccountKicker(player), accountBanned, accountBanReason, accountBanner, isAdmin(player));
	}

	/**
	 * This returns if the player has a team.<br>
	 * Uses:<br>
	 * {@link #readFromPlayerFile(Player, int)}
	 * 
	 * @param player
	 *            to check.
	 * @return if the player has a team.
	 */
	public static boolean hasTeam(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 4));
	}

	/**
	 * This player returns if the player owns a team.<br>
	 * Uses:<br>
	 * {@link #readFromPlayerFile(Player, int)}
	 * 
	 * @param player
	 *            to check.
	 * @return if the player owns a team.
	 */
	public static boolean ownsTeam(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 0));
	}

	/**
	 * 
	 * @param player
	 */
	public static void removeTeam(Player player) {
		boolean isAccountKicked = isAccountKicked(player);
		long accountKickTime = getAccountKickTime(player);
		boolean accountBanned = isAccountBanned(player);
		String accountBanReason = getAccountBanReason(player);
		String accountBanner = getAccountBanner(player);
		writeToPlayerFile(player, false, false, getPlayerTeam(player), isTeamKicked(player), getTeamKickTime(player), isAccountKicked, accountKickTime, getAccountKickReason(player), getAccountKicker(player), accountBanned, accountBanReason, accountBanner, isAdmin(player));
	}

	// Team Kick section

	/**
	 * 
	 * @param kicker
	 * @param kicked
	 * @param seconds
	 */
	public static void setTeamKicked(Player kicker, Player kicked, int seconds) {
		message(kicked, ChatColor.DARK_RED + "You have been kicked by " + kicker.getDisplayName() + ChatColor.DARK_RED + " for " + seconds + " seconds.");
		boolean ownsTeam = ownsTeam(kicked);
		String team = getPlayerTeam(kicked);
		long future = System.nanoTime() + (seconds * NANO_MODIFIER);
		boolean isAccountKicked = isAccountKicked(kicked);
		long accountKickTime = getAccountKickTime(kicked);
		boolean accountBanned = isAccountBanned(kicked);
		String accountBanReason = getAccountBanReason(kicked);
		String accountBanner = getAccountBanner(kicked);
		writeToPlayerFile(kicked, hasTeam(kicked), ownsTeam, team, true, future, isAccountKicked, accountKickTime, getAccountKickReason(kicked), getAccountKicker(kicked), accountBanned, accountBanReason, accountBanner, isAdmin(kicked));
	}

	/**
	 * 
	 * @param player
	 */
	public static void removeTeamKick(Player player) {
		boolean isAccountKicked = isAccountKicked(player);
		long accountKickTime = getAccountKickTime(player);
		boolean accountBanned = isAccountBanned(player);
		String accountBanReason = getAccountBanReason(player);
		String accountBanner = getAccountBanner(player);
		writeToPlayerFile(player, hasTeam(player), ownsTeam(player), getPlayerTeam(player), false, 0, isAccountKicked, accountKickTime, getAccountKickReason(player), getAccountKicker(player), accountBanned, accountBanReason, accountBanner, isAdmin(player));
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isTeamKicked(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 2));
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static long getTeamKickTime(Player player) {
		return Long.parseLong(readFromPlayerFile(player, 3));
	}

	// Read write section.

	/**
	 * 
	 * @param player
	 * @param lineNumber
	 * @return
	 */
	private static String readFromPlayerFile(Player player, int lineNumber) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath())))) {
			int current = 0;
			for (String line; (line = br.readLine()) != null;) {
				if (current == lineNumber) {
					return line;
				}
				current++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param player
	 * @param joined
	 * @param ownsTeam
	 * @param team
	 * @param isTeamKicked
	 * @param teamKickTime
	 * @param isAccountKicked
	 * @param accountKickTime
	 * @param accountKickReason
	 * @param accountKicker
	 * @param accountBanned
	 * @param accountBanReason
	 * @param accountBanner
	 */
	private static void writeToPlayerFile(Player player, boolean joined, boolean ownsTeam, String team, boolean isTeamKicked, long teamKickTime, boolean isAccountKicked, long accountKickTime, String accountKickReason, String accountKicker, boolean accountBanned, String accountBanReason, String accountBanner, boolean isAdmin) {
		try {
			File file = new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath());
			if (!new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).exists()) new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).mkdirs();
			if (file.exists()) file.delete();
			file.createNewFile();
			PrintWriter pw = new PrintWriter(file);
			pw.println(ownsTeam);
			pw.println(team);
			pw.println(isTeamKicked);
			pw.println(teamKickTime);
			pw.println(joined);
			pw.println(isAccountKicked);
			pw.println(accountKickTime);
			pw.println(accountKickReason);
			pw.println(accountKicker);
			pw.println(isAdmin);
			pw.close();
		} catch (IOException e) {
			plugin.getLogger().severe("ERROR: IOException. (com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta.setPlayerTeam(62-74)" + e.getMessage());
		}
	}

	// Account Kick Section.

	/**
	 * 
	 * @param player
	 * @param kicker
	 * @param reason
	 * @param time
	 */
	public static void setAccountKicked(Player player, Player kicker, String reason, long time) {
		boolean joinedTeam = hasTeam(player);
		boolean ownsTeam = ownsTeam(player);
		String teamName = getPlayerTeam(player);
		boolean teamKicked = isTeamKicked(player);
		long teamKickTime = getTeamKickTime(player);
		boolean isAccountKicked = true;
		long accountKickTime = System.nanoTime() + (time * NANO_MODIFIER);
		String accountKickReason = reason;
		String accountKicker = kicker.getName();
		boolean accountBanned = isAccountBanned(player);
		String accountBanReason = getAccountBanReason(player);
		String accountBanner = getAccountBanner(player);
		writeToPlayerFile(player, joinedTeam, ownsTeam, teamName, teamKicked, teamKickTime, isAccountKicked, accountKickTime, accountKickReason, accountKicker, accountBanned, accountBanReason, accountBanner, isAdmin(player));
		broadcast(ChatColor.DARK_RED + "Player " + player.getDisplayName() + ChatColor.DARK_RED + " has been kicked by: " + kicker.getDisplayName() + ChatColor.DARK_RED + " for " + time + ChatColor.DARK_RED + " seconds for " + reason);
	}

	/**
	 * 
	 * @param player
	 */
	public static void removeAccountKick(Player player) {
		boolean joinedTeam = hasTeam(player);
		boolean ownsTeam = ownsTeam(player);
		String teamName = getPlayerTeam(player);
		boolean teamKicked = isTeamKicked(player);
		long teamKickTime = getTeamKickTime(player);
		boolean isAccountKicked = false;
		long accountKickTime = 0;
		String accountKickReason = "";
		String accountKicker = "";
		boolean accountBanned = isAccountBanned(player);
		String accountBanReason = getAccountBanReason(player);
		String accountBanner = getAccountBanner(player);
		writeToPlayerFile(player, joinedTeam, ownsTeam, teamName, teamKicked, teamKickTime, isAccountKicked, accountKickTime, accountKickReason, accountKicker, accountBanned, accountBanReason, accountBanner, isAdmin(player));
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAccountKicked(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 5));
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static long getAccountKickTime(Player player) {
		return Integer.parseInt(readFromPlayerFile(player, 6));
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static String getAccountKickReason(Player player) {
		return readFromPlayerFile(player, 7);
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static String getAccountKicker(Player player) {
		return readFromPlayerFile(player, 8);
	}

	// Account ban stuff.

	/**
	 * 
	 * @param kicker
	 * @param kickee
	 * @param reason
	 */
	public static void setAccountBanned(Player kicker, Player kickee, String reason) {
		boolean joinedTeam = hasTeam(kickee);
		boolean ownsTeam = ownsTeam(kickee);
		String teamName = getPlayerTeam(kickee);
		boolean teamKicked = isTeamKicked(kickee);
		long teamKickTime = getTeamKickTime(kickee);
		boolean isAccountKicked = isAccountKicked(kickee);
		long accountKickTime = getAccountKickTime(kickee);
		String accountKickReason = getAccountKickReason(kickee);
		String accountKicker = getAccountKicker(kickee);
		boolean accountBanned = true;
		String accountBanReason = reason;
		String accountBanner = kicker.getName();
		writeToPlayerFile(kickee, joinedTeam, ownsTeam, teamName, teamKicked, teamKickTime, isAccountKicked, accountKickTime, accountKickReason, accountKicker, accountBanned, accountBanReason, accountBanner, isAdmin(kickee));
		broadcast(ChatColor.DARK_RED + "Player " + kickee.getDisplayName() + ChatColor.DARK_RED + " has been banned by: " + kicker.getDisplayName() + ChatColor.DARK_RED + " for " + reason);
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAccountBanned(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 9));
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static String getAccountBanner(Player player) {
		return readFromPlayerFile(player, 10);
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static String getAccountBanReason(Player player) {
		return readFromPlayerFile(player, 11);
	}

	// WG Stuff

	/**
	 * 
	 * @return
	 */
	private static WorldGuardPlugin getWorldGuard() {
		Plugin wg = plugin.getServer().getPluginManager().getPlugin("WorldGuards");
		if (plugin == null || !(wg instanceof WorldGuardPlugin)) {
			return null;
		}
		return (WorldGuardPlugin) wg;
	}

	// Protection stuff

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isProtected(Player player) {
		if (player.hasMetadata("killshotteams.spawn.protection")) {
			if (player.getMetadata("killshotteams.spawn.protection").get(0).asBoolean()) {
				WorldGuardPlugin wgp = getWorldGuard();
				if (wgp != null) {
					BlockVector max = wgp.getRegionManager(player.getWorld()).getRegion("spawn").getMaximumPoint();
					BlockVector min = wgp.getRegionManager(player.getWorld()).getRegion("spawn").getMinimumPoint();
					Location loc = player.getLocation();
					if (loc.getBlockX() > min.getBlockX() && loc.getBlockY() > min.getBlockY() && loc.getBlockZ() > min.getBlockZ()) {
						if (loc.getBlockX() < max.getBlockX() && loc.getBlockY() < max.getBlockY() && loc.getBlockZ() < max.getBlockZ()) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAccountSuspended(Player player) {
		return isAccountBanned(player) || isAccountKicked(player);
	}

	public static boolean isAdmin(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 12));
	}
}
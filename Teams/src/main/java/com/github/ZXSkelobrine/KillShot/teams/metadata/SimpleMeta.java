package com.github.ZXSkelobrine.KillShot.teams.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;

public class SimpleMeta extends TeamsPlugin {
	public static final long NANO_MODIFIER = 1000000000;

	public SimpleMeta(Plugin plugin) {
		super(plugin);
	}

	public static void changeChatStatus(Player player) {
		if (player.hasMetadata("killshotteam.hasteam.hasteamchat.ison")) {
			if (player.getMetadata("killshotteam.hasteam.hasteamchat.ison").get(0).asBoolean()) {
				SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", false);
				message(player, "Team chat has been disabled");
			} else {
				SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", true);
				message(player, "Team chat has been enabled");
			}
		} else {
			SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", true);
			message(player, "Team chat has been enabled");
		}
	}

	public static boolean isManager(Player player) {
		List<String> managers = plugin.getConfig().getStringList("teams." + getPlayerTeam(player) + ".managers");
		boolean retur = false;
		String playerUUID = player.getUniqueId().toString();
		for (String s : managers) {
			if (s.equals(playerUUID)) retur = true;
		}
		return retur;
	}

	private static void setBooleanMetadata(Player player, String key, boolean value) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static void addConfig(String key, String value) {
		if (!plugin.getConfig().contains(key)) plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, value);
		saveConfig();
	}

	public static void addListToConfig(String key, List<String> values) {
		if (!plugin.getConfig().contains(key)) plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, values);
		saveConfig();
	}

	public static void addBooleanToConfig(String key, boolean value) {
		if (!plugin.getConfig().contains(key)) plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, value);
		saveConfig();
	}

	public static void saveConfig() {
		try {
			plugin.getConfig().save(new File(new File("plugins/KillShotTeams/config.yml").getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getPlayerTeam(Player player) {
		return readFromPlayerFile(player, 1);
	}

	private static void writeToPlayerFile(Player player, boolean joined, boolean ownsTeam, String team, boolean isKicked, long kickTime) {
		try {
			File file = new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath());
			if (!new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).exists()) new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).mkdirs();
			if (file.exists()) file.delete();
			file.createNewFile();
			PrintWriter pw = new PrintWriter(file);
			pw.println(ownsTeam);
			pw.println(team);
			pw.println(isKicked);
			pw.println(kickTime);
			pw.println(joined);
			pw.close();
		} catch (IOException e) {
			plugin.getLogger().severe("ERROR: IOException. (com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta.setPlayerTeam(62-74)" + e.getMessage());
		}
	}

	public static void setPlayerTeams(Player player, String team) {
		boolean ownsTeam = ownsTeam(player);
		boolean isKicked = isKicked(player);
		long getKickTime = getKickTime(player);
		writeToPlayerFile(player, true, ownsTeam, team, isKicked, getKickTime);
	}

	public static boolean hasTeam(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 4));
	}

	public static void removeTeam(Player player) {
		writeToPlayerFile(player, false, false, getPlayerTeam(player), isKicked(player), getKickTime(player));
	}

	public static void setKicked(Player kicker, Player kicked, int seconds) {
		message(kicked, ChatColor.DARK_RED + "You have been kicked by " + kicker.getDisplayName() + ChatColor.DARK_RED + " for " + seconds + " seconds.");
		boolean ownsTeam = ownsTeam(kicked);
		String team = getPlayerTeam(kicked);
		long future = System.nanoTime() + (seconds * NANO_MODIFIER);
		writeToPlayerFile(kicked, hasTeam(kicked), ownsTeam, team, true, future);
	}

	public static void removeKick(Player player) {
		writeToPlayerFile(player, hasTeam(player), ownsTeam(player), getPlayerTeam(player), false, 0);
	}

	public static boolean isKicked(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 2));
	}

	public static long getKickTime(Player player) {
		return Long.parseLong(readFromPlayerFile(player, 3));
	}

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

	public static boolean ownsTeam(Player player) {
		return Boolean.parseBoolean(readFromPlayerFile(player, 0));
	}
}
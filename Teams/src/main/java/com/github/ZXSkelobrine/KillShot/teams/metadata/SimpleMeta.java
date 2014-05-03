package com.github.ZXSkelobrine.KillShot.teams.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;

public class SimpleMeta extends TeamsPlugin {

	public SimpleMeta(Plugin plugin) {
		super(plugin);
	}

	public static void changeChatStatus(Player player) {
		if (player.getMetadata("killshotteam.hasteam.hasteamchat.ison").get(0).asBoolean()) {
			SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", false);
			message(player, "Team chat has been disabled");
		} else {
			SimpleMeta.setBooleanMetadata(player, "killshotteam.hasteam.hasteamchat.ison", true);
			message(player, "Team chat has been enabled");
		}
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
		try (BufferedReader br = new BufferedReader(new FileReader(new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath())))) {
			int current = 0;
			for (String line; (line = br.readLine()) != null;) {
				if (current == 1) {
					return line;
				}
				current++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setPlayerTeams(Player player, String team) {
		try {
			File file = new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath());
			if (!new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).exists()) new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).mkdirs();
			if (file.exists()) file.delete();
			file.createNewFile();
			PrintWriter pw = new PrintWriter(file);
			pw.println(ownsTeam(player));
			pw.println(team);
			pw.close();
		} catch (IOException e) {
			plugin.getLogger().severe("ERROR: IOException. (com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta.setPlayerTeam(62-74)" + e.getMessage());
		}
	}

	public static boolean hasTeam(Player player) {
		return new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath()).exists();
	}

	public static void removeTeam(Player player) {
		File file = new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath());
		file.delete();
	}

	public static boolean ownsTeam(Player player) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(new File("plugins/KillShotTeams/players/" + player.getUniqueId().toString() + ".txt").getAbsolutePath())))) {
			int current = 0;
			for (String line; (line = br.readLine()) != null;) {
				if (current == 0) {
					return Boolean.parseBoolean(line);
				}
				current++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
/**
 * <pre>
 * public static void setBooleanMetadata(Player player, String key, boolean value, Plugin plugin) {
 * 	player.setMetadata(key, new FixedMetadataValue(plugin, value));
 * }
 * 
 * public static void setStringnMetadata(Player player, String key, String value, Plugin plugin) {
 * 	player.setMetadata(key, new FixedMetadataValue(plugin, value));
 * }
 * 
 * public static void setIntMetadata(Player player, String key, int value, Plugin plugin) {
 * 	player.setMetadata(key, new FixedMetadataValue(plugin, value));
 * }
 * </pre>
 */

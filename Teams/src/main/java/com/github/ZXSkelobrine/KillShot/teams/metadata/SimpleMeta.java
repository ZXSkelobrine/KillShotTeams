package com.github.ZXSkelobrine.KillShot.teams.metadata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;

public class SimpleMeta extends TeamsPlugin {

	public SimpleMeta(Plugin plugin) {
		super(plugin);
	}

	public static void setBooleanMetadata(Player player, String key, boolean value, Plugin plugin) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static void setStringnMetadata(Player player, String key, String value, Plugin plugin) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static void setIntMetadata(Player player, String key, int value, Plugin plugin) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static void addConfig(String key, String value) {
		plugin.getConfig().createSection(key);
		plugin.getConfig().set(key, value);
		try {
			plugin.getConfig().save(new File(new File("plugins/KillShotTeams/config.yml").getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addListToConfig(String key, List<String> values) {
		plugin.getConfig().set(key, values);
	}

	public static void saveConfig() {
		try {
			plugin.getConfig().save(new File(new File("plugins/KillShotTeams/config.yml").getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.github.ZXSkelobrine.KillShot.teams.metadata;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class SimpleMeta {
	public static void setBooleanMetadata(Player player, String key, boolean value, Plugin plugin) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static void setStringnMetadata(Player player, String key, String value, Plugin plugin) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}

	public static void setIntMetadata(Player player, String key, int value, Plugin plugin) {
		player.setMetadata(key, new FixedMetadataValue(plugin, value));
	}
}

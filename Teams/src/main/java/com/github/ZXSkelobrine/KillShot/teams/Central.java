package com.github.ZXSkelobrine.KillShot.teams;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ZXSkelobrine.KillShot.teams.commands.AdminCommands;
import com.github.ZXSkelobrine.KillShot.teams.commands.PlayerCommands;
import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class Central extends JavaPlugin {
	PlayerCommands playerComs;
	AdminCommands adminComs;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		new SimpleMeta(this);
		playerComs = new PlayerCommands(this);
		adminComs = new AdminCommands(this);
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("t")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length > 0) {
					if (checkPermissions(player, "t")) {
						if (player.hasMetadata("killshotteams.hasteam")) {
							if (player.getMetadata("killshotteams.hasteam.ownsteam").get(0).asBoolean()) {
								adminComs.onCommand(sender, command, label, args);
								return true;
							} else {
								playerComs.onCommand(sender, command, label, args);
								return true;
							}
						} else {
							playerComs.onCommand(sender, command, label, args);
							return true;
						}
					}
				} else {
					new TeamsPlugin(this).printHelp(player);
				}
			}
		}
		return false;
	}

	public boolean checkPermissions(Player player, String command) {
		if (player.hasPermission("killshotteams.*")) return true;
		switch (command) {
		case "t":
			return player.hasPermission("killshotteams.help");
		case "create":
			return player.hasPermission("killshotteams.create");
		}
		return false;
	}
}
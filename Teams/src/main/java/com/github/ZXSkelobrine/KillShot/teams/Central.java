package com.github.ZXSkelobrine.KillShot.teams;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ZXSkelobrine.KillShot.teams.commands.AdminCommands;
import com.github.ZXSkelobrine.KillShot.teams.commands.PlayerCommands;

public class Central extends JavaPlugin {
	PlayerCommands playerComs;
	AdminCommands adminComs;

	@Override
	public void onEnable() {
		playerComs = new PlayerCommands(this);
		adminComs = new AdminCommands(this);
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("t")) {
			if (args.length > 0) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasMetadata("killshotteams.hasteam")) {
						if (player.getMetadata("killshotteams.hasteam.ownsteam").get(0).asBoolean()) {
							adminComs.onCommand(sender, command, label, args);
							player.sendMessage("Transfered Main --> admin");
							return true;
						} else {
							playerComs.onCommand(sender, command, label, args);
							player.sendMessage("Transfered Main --> player");
							return true;
						}
					} else {
						playerComs.onCommand(sender, command, label, args);
						player.sendMessage("Transfered Main --> player");
						return true;
					}
				}
			}
		}
		return false;
	}
}

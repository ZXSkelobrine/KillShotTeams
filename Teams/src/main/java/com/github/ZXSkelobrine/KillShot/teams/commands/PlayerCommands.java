package com.github.ZXSkelobrine.KillShot.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class PlayerCommands extends TeamsPlugin implements CommandExecutor {

	public PlayerCommands(Plugin plugin) {
		super(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equalsIgnoreCase("create")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 3) {
					String name = args[1];
					String password = args[2];
					SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam", true, plugin);
					SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam.ownsteam", true, plugin);
					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.name", name, plugin);
					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.pass", password, plugin);
					super.message(player, "Team " + name + " has been create with the password: " + password);
				} else {
					super.message(player, "You must specify and name and password as so: /t create [Team Name] [Password]");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		}
		return false;
	}

}

package com.github.ZXSkelobrine.KillShot.teams.commands;

import java.util.ArrayList;
import java.util.List;

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
					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.team", name, plugin);
					SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam.ownsteam", true, plugin);
					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.name", name, plugin);
					SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.pass", password, plugin);
					SimpleMeta.addConfig("teams." + name + ".name", name);
					SimpleMeta.addConfig("teams." + name + ".pass", password);
					SimpleMeta.addConfig("teams." + name + ".owner", player.getName());
					List<String> names = new ArrayList<>();
					names.add(player.getName());
					SimpleMeta.addListToConfig("teams." + name + ".members", names);
					super.message(player, "Team " + name + " has been create with the password: " + password);
				} else {
					super.message(player, "You must specify and name and password as so: /t create [Team Name] [Password]");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("join")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 3) {
					String name = args[1];
					String password = args[2];
					try {
						String offPassword = (String) plugin.getConfig().get("teams." + name + ".pass");
						if (password.equals(offPassword)) {
							List<String> current = plugin.getConfig().getStringList("teams." + name + ".members");
							current.add(player.getName());
							SimpleMeta.addListToConfig("teams." + name + ".members", current);
							SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.team", name, plugin);
							super.message(player, "You have successfully joined the " + name + " team!");
							plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "nick " + player.getName() + " [" + name + "]" + player.getCustomName());
						}
					} catch (Exception e) {
						super.message(player, "There doesn't seem to be a team with that name!");
					}
				} else {
					super.message(player, "You must specify and name and password as so: /t join [Team Name] [Password]");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("leave")) {
			super.message(sender, ((Player) sender).getMetadata("killshotteams.hasteam.team").get(0).asString());
		}
		return false;
	}
}

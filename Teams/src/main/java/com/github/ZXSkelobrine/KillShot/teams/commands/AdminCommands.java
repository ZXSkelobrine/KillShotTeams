package com.github.ZXSkelobrine.KillShot.teams.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.Central;
import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class AdminCommands extends TeamsPlugin implements CommandExecutor {

	public AdminCommands(Plugin plugin) {
		super(plugin);
	}

	@SuppressWarnings("static-access")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("create")) {
				if (super.checkPermissions(player, "create")) {
					if (sender instanceof Player) {
						super.message(sender, "Sorry, you are already the owner of a team. If you would like to create a new one please disband your team first");
						return true;
					}
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.create)");
				}
			} else if (args[0].equalsIgnoreCase("disband")) {
				if (args.length == 3) {
					if (super.checkPermissions(player, "disband")) {
						String name = args[1];
						String password = args[2];
						try {
							String offPassword = (String) plugin.getConfig().get("teams." + name + ".pass");
							if (password.equals(offPassword) && ((Player) sender).getName().equals(plugin.getConfig().get("teams." + name + ".owner"))) {
								super.plugin.getConfig().set("teams." + name + ".owner", null);
								super.plugin.getConfig().set("teams." + name + ".pass", null);
								super.plugin.getConfig().set("teams." + name + ".name", null);
								super.plugin.getConfig().set("teams." + name, null);
								SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam", false, plugin);
								SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.team", null, plugin);
								SimpleMeta.setBooleanMetadata(player, "killshotteams.hasteam.ownsteam", false, plugin);
								SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.name", null, plugin);
								SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.ownsteam.pass", null, plugin);
								List<String> current = plugin.getConfig().getStringList("teams." + name + ".members");
								for (String pl : current) {
									Player now = Bukkit.getPlayer(UUID.fromString(pl));
									SimpleMeta.setBooleanMetadata(now, "killshotteams.hasteam", false, plugin);
									SimpleMeta.setStringnMetadata(now, "killshotteams.hasteam.team", null, plugin);
								}
								SimpleMeta.saveConfig();
								super.message(sender, "You have successfully disbanded team " + name);

							} else {
								super.message(sender, "You have either enter the name or password incorrectly or you are not the owner of this team.");
							}

						} catch (Exception e) {
							super.message(sender, "Something has gone wrong!");
						}
					} else {
						super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.disband)");
					}
				} else {
					super.message(sender, "Invalid arguments. Please format command as so /t disband [Team Name] [Password]");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("sethq")) {
				if (super.checkPermissions(player, "sethq")) {
					String team = player.getMetadata("killshotteams.hasteam.ownsteam.name").get(0).asString();
					Location newHQ = player.getLocation();
					String x = String.valueOf(newHQ.getX());
					String y = String.valueOf(newHQ.getY());
					String z = String.valueOf(newHQ.getZ());
					List<String> location = new ArrayList<String>();
					location.add(x);
					location.add(y);
					location.add(z);
					SimpleMeta.addListToConfig("teams." + team + ".hqloc", location);
					String world = newHQ.getWorld().getName();
					SimpleMeta.addConfig("teams." + team + ".hqworld", world);
					super.message(player, "Successfully set the team HQ point!");
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.sethq)");
				}
			} else {
				Central.playerComs.onCommand(sender, command, label, args, true);
			}
		}
		return false;
	}
}
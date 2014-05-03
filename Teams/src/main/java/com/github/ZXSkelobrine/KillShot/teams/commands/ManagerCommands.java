package com.github.ZXSkelobrine.KillShot.teams.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.Central;
import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class ManagerCommands extends TeamsPlugin {

	public ManagerCommands(Plugin plugin) {
		super(plugin);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("sethq")) {
				if (super.checkPermissions(player, "sethq")) {
					String team = SimpleMeta.getPlayerTeam(player);
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
			} else if (args[0].equalsIgnoreCase("setrally")) {
				if (super.checkPermissions(player, "setrally")) {
					String team = SimpleMeta.getPlayerTeam(player);
					Location newRally = player.getLocation();
					String x = String.valueOf(newRally.getX());
					String y = String.valueOf(newRally.getY());
					String z = String.valueOf(newRally.getZ());
					List<String> location = new ArrayList<String>();
					location.add(x);
					location.add(y);
					location.add(z);
					SimpleMeta.addListToConfig("teams." + team + ".rallyloc", location);
					String world = newRally.getWorld().getName();
					SimpleMeta.addConfig("teams." + team + ".rallyworld", world);
					super.message(player, "Successfully set the team rally point!");
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.setrally)");
				}
			} else if (args[0].equalsIgnoreCase("password")) {
				if (super.checkPermissions(player, "password")) {
					if (args.length == 2) {
						String team = SimpleMeta.getPlayerTeam(player);
						SimpleMeta.addConfig("teams." + team + ".pass", args[1]);
						super.message(player, "Successfully updated the password.");
					}
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.password)");
				}
			} else if (args[0].equalsIgnoreCase("kick")) {
				if (checkPermissions(player, "kick")) {
					if (args.length == 3) {
						if (canParse(args[2])) {
							Player kicked = Bukkit.getPlayer(args[1]);
							SimpleMeta.setKicked(player, kicked, Integer.parseInt(args[2]));
						} else {
							super.message(player, "Please specify a valid number");
						}
					} else {
						super.message(player, "You must specify how long to kick for (/t kick [Player] [time]");
					}
				}else{
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.kick)");
				}
			} else {
				Central.playerComs.onCommand(sender, command, label, args, true);
			}
		} else {
			super.message(sender, "You must be a player to do that.");
		}
		return false;
	}

	public boolean canParse(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

package com.github.ZXSkelobrine.KillShot.teams.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.Central;
import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class OverlordCommands extends TeamsPlugin {

	public OverlordCommands(Plugin plugin) {
		super(plugin);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equalsIgnoreCase("akick")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (checkPermissions(player, "ovr:kick")) {
					if (parseAble(args[2])) {
						Player kicked = Bukkit.getPlayer(args[1]);
						StringBuilder sb = new StringBuilder();
						for (int i = 3; i < args.length; i++) {
							sb.append(args[i] + " ");
						}
						SimpleMeta.setAccountKicked(kicked, player, sb.toString(), Long.parseLong(args[2]));
					} else {
						super.message(player, "Please enter a valid number for seconds.");
					}
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.admin.kick)");
				}
			} else {
				super.message(sender, "You must be a player to do that");
			}
		} else if (args[0].equalsIgnoreCase("details")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 2) {
					Player infoPlayer = Bukkit.getPlayer(args[1]);
					List<String> info = new ArrayList<String>();
					ChatColor dg = ChatColor.DARK_GREEN;
					ChatColor lg = ChatColor.GREEN;
					ChatColor g = ChatColor.DARK_GRAY;
					info.add(dg + "[KillShotTeams] " + lg + "Kick Details: " + infoPlayer.getName());
					boolean teamKicked = SimpleMeta.isTeamKicked(infoPlayer);
					boolean accountKicked = SimpleMeta.isAccountKicked(infoPlayer);
					boolean accountBanned = SimpleMeta.isAccountBanned(infoPlayer);
					info.add(ChatColor.GRAY + "Team Kicked: " + teamKicked);
					if (teamKicked) {
						info.add(g + "Time remaining: " + ((SimpleMeta.getTeamKickTime(infoPlayer) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER));
					}
					info.add(ChatColor.GRAY + "Account Kicked: " + accountKicked);
					if (accountKicked) {
						info.add(g + "Time remaining: " + ((SimpleMeta.getAccountKickTime(infoPlayer) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER));
						info.add(g + "Reason: " + SimpleMeta.getAccountKickReason(infoPlayer));
					}
					info.add(ChatColor.GRAY + "Account Banned: " + accountBanned);
					if (accountBanned) {
						info.add(g + "Reason: " + SimpleMeta.getAccountBanReason(infoPlayer));
						info.add(g + "By: " + SimpleMeta.getAccountBanner(infoPlayer));
					}
					String[] infoArray = new String[info.size()];
					info.toArray(infoArray);
					player.sendMessage(infoArray);
				}
			} else if (args[0].equalsIgnoreCase("ban")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (checkPermissions(player, "ovr:ban")) {
						if (args.length > 3) {
							Player banned = Bukkit.getPlayer(args[1]);
							StringBuilder sb = new StringBuilder();
							for (int i = 2; i < args.length; i++) {
								sb.append(args[i] + " ");
							}
							SimpleMeta.setAccountBanned(player, banned, sb.toString());
						}
					} else {
						super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.admin.ban)");
					}
				} else {
					super.message(sender, "You must be a player to do that");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		} else if (args[0].equalsIgnoreCase("admin")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (checkPermissions(player, "ovr:admin")) {
					if (player.getGameMode().equals(GameMode.CREATIVE)) {
						player.setGameMode(GameMode.SURVIVAL);
						super.message(player, "You have been switched to PLAY mode");
					} else if (player.getGameMode().equals(GameMode.SURVIVAL)) {
						player.setGameMode(GameMode.CREATIVE);
						super.message(player, "You have been switched to ADMIN mode");
					} else {
						player.setGameMode(GameMode.SURVIVAL);
						super.message(player, "You have been switched to PLAY mode");
					}
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.admin.admin)");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		} else {
			Central.playerComs.onCommand(sender, command, label, args, true);
		}
		return false;
	}

	public boolean parseAble(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
/**
 * <pre>
 * if (args[0].equalsIgnoreCase("kick")) {
 * 				if (checkPermissions(player, "kick")) {
 * 					if (args.length == 3) {
 * 						if (canParse(args[2])) {
 * 							Player kicked = Bukkit.getPlayer(args[1]);
 * 							SimpleMeta.setKicked(player, kicked, Integer.parseInt(args[2]));
 * 						} else {
 * 							super.message(player, "Please specify a valid number");
 * 						}
 * 					} else {
 * 						super.message(player, "You must specify how long to kick for (/t kick [Player] [time]");
 * 					}
 * 				}else{
 * 					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.kick)");
 * 				}
 * } else
 * 
 * <pre>
 */

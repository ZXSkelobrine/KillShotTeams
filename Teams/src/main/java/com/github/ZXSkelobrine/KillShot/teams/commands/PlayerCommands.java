package com.github.ZXSkelobrine.KillShot.teams.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class PlayerCommands extends TeamsPlugin {

	public PlayerCommands(Plugin plugin) {
		super(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args, boolean fallThrough) {
		if (args[0].equalsIgnoreCase("create") && !fallThrough) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!SimpleMeta.isAccountBanned(player)) {
					if (!SimpleMeta.isAccountKicked(player)) {
						if (super.checkPermissions(player, "create")) {
							if (args.length == 3) {
								List<String> teams = plugin.getConfig().getStringList("allteams");
								String name = args[1];
								boolean contin = true;
								for (String team : teams) {
									if (name.equalsIgnoreCase(team)) {
										contin = false;
									}
								}
								if (contin) {
									String password = args[2];
									SimpleMeta.setPlayerTeams(player, name);
									SimpleMeta.addStringToConfig("teams." + name + ".name", name);
									SimpleMeta.addStringToConfig("teams." + name + ".pass", password);
									SimpleMeta.addStringToConfig("teams." + name + ".owner", player.getUniqueId().toString());
									teams.add(name);
									SimpleMeta.addStringListToConfig("allteams", teams);
									List<String> names = new ArrayList<>();
									names.add(player.getUniqueId().toString());
									SimpleMeta.addStringListToConfig("teams." + name + ".members", names);
									super.message(player, "Team " + name + " has been create with the password: " + password);
								} else {
									super.message(player, "There is already another team with that name!");
								}
							} else {
								super.message(player, "You must specify and name and password as so: /t create [Team Name] [Password]");
							}
						} else {
							super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.create)");
						}
					} else {
						super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
					}
				} else {
					super.message(player, "Your account has been suspended permanently");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("join") && !fallThrough) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!SimpleMeta.isAccountBanned(player)) {
					if (!SimpleMeta.isAccountKicked(player)) {
						if (super.checkPermissions(player, "join")) {
							if (args.length == 3) {
								String name = args[1];
								String password = args[2];
								try {
									String offPassword = (String) plugin.getConfig().get("teams." + name + ".pass");
									if (password.equals(offPassword)) {
										super.joinTeam(player, name);
									}
								} catch (Exception e) {
									super.message(player, "There doesn't seem to be a team with that name!");
								}
							} else {
								super.message(player, "You must specify and name and password as so: /t join [Team Name] [Password]");
							}
						} else {
							super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.join)");
						}
					} else {
						super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
					}
				} else {
					super.message(player, "Your account has been suspended permanently");
				}
			} else {
				super.message(sender, "You must be a player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("leave") && !fallThrough) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!SimpleMeta.isAccountBanned(player)) {
					if (!SimpleMeta.isAccountKicked(player)) {
						if (super.checkPermissions(player, "leave")) {
							if (SimpleMeta.hasTeam(player)) {
								super.leaveTeam(player);
							} else {
								super.message(player, "You must be part of a team");
							}
						} else {
							super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.leave)");
						}
					} else {
						super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
					}
				} else {
					super.message(player, "Your account has been suspended permanently");
				}
			} else {
				super.message(sender, "You must be player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("info")) {
			if (args.length == 2) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (super.checkPermissions(player, "info")) {
						String team = args[1];
						String baseAddress = "teams." + team;

						String name = plugin.getConfig().getString(baseAddress + ".name");
						String owner = Bukkit.getPlayer(UUID.fromString(plugin.getConfig().getString(baseAddress + ".owner"))).getDisplayName();
						List<String> membersUUIDS = plugin.getConfig().getStringList(baseAddress + ".members");
						List<String> memberNames = new ArrayList<String>();
						for (String s : membersUUIDS) {
							memberNames.add(Bukkit.getPlayer(UUID.fromString(s)).getDisplayName());
						}
						List<String> message = new ArrayList<>();
						message.add(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[KillShot Teams]" + ChatColor.RESET + ChatColor.GREEN + "Team info");
						message.add(ChatColor.GRAY + "Team name: " + name);
						message.add(ChatColor.GRAY + "Team owner: " + owner);
						message.add(ChatColor.GRAY + "Team members: ");
						for (String s : memberNames) {
							message.add(ChatColor.GRAY + s);
						}
						String[] toSend = new String[message.size()];
						message.toArray(toSend);
						player.sendMessage(toSend);
					} else {
						super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.info)");
					}
				} else {
					super.message(sender, "You must be player to do that.");
				}
			} else {
				super.message(sender, "You must specify and name and password as so: /t info [Team Name]");
			}
		}
		if (args[0].equalsIgnoreCase("chat")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!SimpleMeta.isAccountBanned(player)) {
					if (!SimpleMeta.isAccountKicked(player)) {
						if (super.checkPermissions(player, "chat")) {
							SimpleMeta.changeChatStatus(player);
						} else {
							super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.chat)");
						}
					} else {
						super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
					}
				} else {
					super.message(player, "Your account has been suspended permanently");
				}
			} else {
				super.message(sender, "You must be player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("hq")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!SimpleMeta.isAccountBanned(player)) {
					if (!SimpleMeta.isAccountKicked(player)) {
						if (super.checkPermissions(player, "hq")) {
							if (SimpleMeta.hasTeam(player)) {
								try {
									String team = SimpleMeta.getPlayerTeam(player);
									List<String> location = plugin.getConfig().getStringList("teams." + team + ".hqloc");
									double x = Double.parseDouble(location.get(0));
									double y = Double.parseDouble(location.get(1));
									double z = Double.parseDouble(location.get(2));
									String worldName = plugin.getConfig().getString("teams." + team + ".hqworld");
									Location hq = new Location(plugin.getServer().getWorld(worldName), x, y, z);
									if (player.teleport(hq)) {
										super.message(player, "Successfully teleported to the HQ");
									} else {
										super.message(player, "Failed to teleport to the HQ");
									}
								} catch (IndexOutOfBoundsException e) {
									super.message(player, "Your team does not have a HQ yet. Please ask your team leader to set one with /t sethq");
								}
							} else {
								super.message(player, "You are not part of a team");
							}
						} else {
							super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.hq)");
						}
					} else {
						super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
					}
				} else {
					super.message(player, "Your account has been suspended permanently");
				}
			} else {
				super.message(sender, "You must be player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("rally")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!SimpleMeta.isAccountBanned(player)) {
					if (!SimpleMeta.isAccountKicked(player)) {
						if (super.checkPermissions(player, "rally")) {
							if (SimpleMeta.hasTeam(player)) {
								try {
									String team = SimpleMeta.getPlayerTeam(player);
									List<String> location = plugin.getConfig().getStringList("teams." + team + ".rallyloc");
									double x = Double.parseDouble(location.get(0));
									double y = Double.parseDouble(location.get(1));
									double z = Double.parseDouble(location.get(2));
									String worldName = plugin.getConfig().getString("teams." + team + ".rallyworld");
									Location rally = new Location(plugin.getServer().getWorld(worldName), x, y, z);
									if (player.teleport(rally)) {
										super.message(player, "Successfully teleported to the rally point");
									} else {
										super.message(player, "Failed to teleport to the rally point");
									}
								} catch (IndexOutOfBoundsException e) {
									super.message(player, "Your team does not have a rally point yet. Please ask your team leader to set one with /t setrally");
								}
							} else {
								super.message(player, "You are not part of a team");
							}
						} else {
							super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.rally)");
						}
					} else {
						super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
					}
				} else {
					super.message(player, "Your account has been suspended permanently");
				}
			} else {
				super.message(sender, "You must be player to do that.");
			}
		}
		if (args[0].equals("UUID")) {
			super.message(((Player) sender), ((Player) sender).getUniqueId().toString());
		}
		if (args[0].equalsIgnoreCase("spawnprotection")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (checkPermissions(player, "protspawn")) {
					if (!SimpleMeta.isAccountBanned(player)) {
						if (!SimpleMeta.isAccountKicked(player)) {
							SimpleMeta.setBooleanMetadata(player, "killshotteams.spawn.allowCommandProtection", true);
						} else {
							super.message(player, "You account has been suspended. " + ((SimpleMeta.getAccountKickTime(player) - System.nanoTime()) / SimpleMeta.NANO_MODIFIER) + " seconds remaining");
						}
					} else {
						super.message(player, "Your account has been suspended permanently");
					}
				} else {
					super.message(player, "Sorry, you dont have permissions to do that" + ChatColor.ITALIC + "(killshotteams.spawn.protection)");
				}
			} else {
				super.message(sender, "You must be player to do that.");
			}
		}
		if (args[0].equalsIgnoreCase("error")) {
			if (sender instanceof Player) {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				SimpleMeta.logError(sb.toString(), ((Player) sender));
			} else {
				super.message(sender, "You must be player to do that.");
			}
		}
		return false;
	}
}

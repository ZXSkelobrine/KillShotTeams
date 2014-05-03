package com.github.ZXSkelobrine.KillShot.teams;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ZXSkelobrine.KillShot.teams.commands.AdminCommands;
import com.github.ZXSkelobrine.KillShot.teams.commands.ManagerCommands;
import com.github.ZXSkelobrine.KillShot.teams.commands.OverlordCommands;
import com.github.ZXSkelobrine.KillShot.teams.commands.PlayerCommands;
import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;
import com.github.ZXSkelobrine.KillShot.teams.listeners.KillShotListeners;
import com.github.ZXSkelobrine.KillShot.teams.metadata.SimpleMeta;

public class Central extends JavaPlugin {
	public static PlayerCommands playerComs;
	public static AdminCommands adminComs;
	public static ManagerCommands managerComs;
	public static OverlordCommands overlordComs;
	public TeamsPlugin teams = new TeamsPlugin(this);

	@Override
	public void onEnable() {
		saveDefaultConfig();
		new SimpleMeta(this);
		getServer().getPluginManager().registerEvents(new KillShotListeners(this), this);
		playerComs = new PlayerCommands(this);
		adminComs = new AdminCommands(this);
		managerComs = new ManagerCommands(this);
		overlordComs = new OverlordCommands(this);
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
					if (args[0].equals("deb")) {
						if (args[1].equalsIgnoreCase("read")) {
							this.getLogger().info(player.getMetadata(args[2]).size() + "");
							this.getLogger().info(player.getMetadata(args[2]).get(0).asString());
							this.getLogger().info(player.getMetadata(args[2]).get(0).asBoolean() + "");
						}
						if (args[1].equalsIgnoreCase("write")) {
							if (args[2].equalsIgnoreCase("boolean")) {
								player.setMetadata(args[3], new FixedMetadataValue(this, Boolean.parseBoolean(args[4])));
							}
							if (args[2].equalsIgnoreCase("string")) {
								player.setMetadata(args[3], new FixedMetadataValue(this, args[4]));
							}
						}
					}
					if (args[0].equals("fileread")) {
						player.sendMessage(SimpleMeta.ownsTeam(player) + "");
						getLogger().info(SimpleMeta.ownsTeam(player) + "");
						player.sendMessage(SimpleMeta.getPlayerTeam(player) + "");
						getLogger().info(SimpleMeta.getPlayerTeam(player) + "");
					}
					if (args[0].equals("filewrite")) {
						SimpleMeta.setPlayerTeams(player, args[1]);
					}
					if (teams.checkPermissions(player, "t")) {
						if (SimpleMeta.hasTeam(player)) {
							if (SimpleMeta.ownsTeam(player)) {
								adminComs.onCommand(sender, command, label, args);
								return true;
							} else if (SimpleMeta.isManager(player)) {
								managerComs.onCommand(sender, command, label, args);
							} else {
								playerComs.onCommand(sender, command, label, args, false);
								return true;
							}
						} else {
							playerComs.onCommand(sender, command, label, args, false);
							return true;
						}
					}
				} else {
					new TeamsPlugin(this).printHelp(player);
					return true;
				}
			}
		}
		return false;
	}

}
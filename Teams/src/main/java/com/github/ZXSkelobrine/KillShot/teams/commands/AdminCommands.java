package com.github.ZXSkelobrine.KillShot.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;

public class AdminCommands extends TeamsPlugin implements CommandExecutor {

	public AdminCommands(Plugin plugin) {
		super(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

}

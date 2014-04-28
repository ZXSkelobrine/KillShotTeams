package com.github.ZXSkelobrine.KillShot.teams.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.ZXSkelobrine.KillShot.teams.general.TeamsPlugin;

public class AdminCommands extends TeamsPlugin implements CommandExecutor {

	public AdminCommands(Plugin plugin) {
		super(plugin);
	}

	@SuppressWarnings("static-access")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equalsIgnoreCase("create")) {
			if (sender instanceof Player) {
				super.message(sender, "Sorry, you are already the owner of a team. If you would like to create a new one please disband your team first");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("disband")) {
			if (args.length == 3) {
				String name = args[1];
				String password = args[2];
				try {
					String offPassword = (String) plugin.getConfig().get("teams." + name + ".pass");
					if (password.equals(offPassword) && ((Player) sender).getName().equals(plugin.getConfig().get("teams." + name + ".owner"))) {
						super.plugin.getConfig().set("teams." + name, null);
						super.plugin.getConfig().set("teams." + name + ".name", null);
						super.plugin.getConfig().set("teams." + name + ".pass", null);
						super.plugin.getConfig().set("teams." + name + ".owner", null);
					} else {
						super.message(sender, "You have either enter the name or password incorrectly or you are not the owner of this team.");
					}
				} catch (Exception e) {
					super.message(sender, "Something has gone wrong!");
				}
				return true;
			} else {
				super.message(sender, "Invalid arguments. Please format command as so /t disband [Team Name] [Password]");
			}
		}
		return false;
	}
}
/**
 * 
 * <pre>
 * String name = args[1];
 * String password = args[2];
 * try {
 * 	String offPassword = (String) plugin.getConfig().get("teams." + name + ".pass");
 * 	if (password.equals(offPassword)) {
 * 		List<String> current = plugin.getConfig().getStringList("teams." + name + ".members");
 * 		current.add(player.getName());
 * 		SimpleMeta.addListToConfig("teams." + name + ".members", current);
 * 		SimpleMeta.setStringnMetadata(player, "killshotteams.hasteam.team", name, plugin);
 * 		super.message(player, "You have successfully joined the " + name + " team!");
 * 		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "nick " + player.getName() + " [" + name + "]" + player.getCustomName());
 * 	}
 * } catch (Exception e) {
 * 	super.message(player, "There doesn't seem to be a team with that name!");
 * }
 * 
 * <pre>
 */

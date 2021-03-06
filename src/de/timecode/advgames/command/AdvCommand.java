package de.timecode.advgames.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timecode.advgames.main.AdvGames;

public class AdvCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.isOp()) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("setjoinspawn")) {
						AdvGames.pl.setSpawn(p.getLocation(), "Join");
						p.sendMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("SetSpawn").replace("&", "§").replace("%name%", "Join"));
					}else if(args[0].equalsIgnoreCase("setgamespawn")) {
						AdvGames.pl.setSpawn(p.getLocation(), "Game");
						p.sendMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("SetSpawn").replace("&", "§").replace("%name%", "Game"));
					}else if(args[0].equalsIgnoreCase("setspecspawn")) {
						AdvGames.pl.setSpawn(p.getLocation(), "Spec");
						p.sendMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("SetSpawn").replace("&", "§").replace("%name%", "Spectator"));
					}else if(args[0].equalsIgnoreCase("start")) {
						if(Bukkit.getOnlinePlayers().size() >= 2) {
							AdvGames.pl.time = 5;
						}
					}else if(args[0].equalsIgnoreCase("help")) {
						p.sendMessage("§7-----------------------------------");
						p.sendMessage("§a/advgames setjoinspawn");
						p.sendMessage("§a/advgames setgamespawn");
						p.sendMessage("§a/advgames setspecspawn");
						p.sendMessage("§a/advgames start");
						p.sendMessage("§7-----------------------------------");
					}else {
						p.sendMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("Syntax").replace("&", "§"));
					}
				}else {
					p.sendMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("Syntax").replace("&", "§"));
				}
			}else {
				p.sendMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("NoPerm").replace("&", "§"));
			}
		}
		return false;
	}

}

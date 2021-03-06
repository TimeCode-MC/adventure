package de.timecode.advgames.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.timecode.advgames.gui.TeleportGUI;
import de.timecode.advgames.main.AdvGames;

public class CompassListener implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(AdvGames.pl.messages.getProperty("SpecInvName").replace("&", "§"))) {
			ArrayList<String> names = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(AdvGames.pl.players.contains(all)) {
					names.add(all.getName());
				}
			}
			
			Player p = (Player) e.getWhoClicked();
			p.teleport(Bukkit.getPlayer(names.get(e.getSlot())));
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem().getType() == Material.COMPASS && !AdvGames.pl.players.contains(e.getPlayer())) {
				new TeleportGUI(e.getPlayer());
			}
		}
	}

}

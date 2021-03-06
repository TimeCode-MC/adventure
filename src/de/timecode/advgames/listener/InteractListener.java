package de.timecode.advgames.listener;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.timecode.advgames.main.AdvGames;

public class InteractListener implements Listener{
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(!AdvGames.pl.players.contains(e.getPlayer()) && e.getPlayer().getGameMode() == GameMode.ADVENTURE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.DIAMOND_BLOCK && AdvGames.pl.blocks.contains(e.getClickedBlock())) {
				Random r = new Random();
				System.out.println(AdvGames.pl.items.getList("Items").size());
				int rn = r.nextInt(AdvGames.pl.items.getList("Items").size());
				p.getInventory().addItem(AdvGames.pl.getItemStack(rn));
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 2);
			}
		}
	}

}

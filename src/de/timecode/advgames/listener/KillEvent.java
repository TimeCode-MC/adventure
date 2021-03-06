package de.timecode.advgames.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.timecode.advgames.main.AdvGames;

public class KillEvent implements Listener{
	
	HashMap<Player, Player> damagers = new HashMap<>();
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		if(e.getEntity().getKiller().getType() == EntityType.PLAYER) {
			e.setDeathMessage("");
			Player p = e.getEntity();
			p.spigot().respawn();
			Bukkit.getScheduler().runTaskLater(AdvGames.pl, new Runnable() {
				
				@Override
				public void run() {
					p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 2, 2);
					Bukkit.getServer().broadcastMessage(AdvGames.pl.prefix + AdvGames.pl.messages.getProperty("Death").replace("&", "§").replace("%player%", p.getName()).replace("%killer%", damagers.get(p).getName()));
					AdvGames.pl.setSpectator(p);
					
				}
			}, 2);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER && AdvGames.pl.players.contains(e.getDamager()) && AdvGames.pl.players.contains(e.getEntity())) {
			damagers.remove((Player)e.getEntity());
			damagers.put((Player)e.getEntity(), (Player)e.getDamager());
		}else {
			e.setCancelled(true);
		}
	}

}

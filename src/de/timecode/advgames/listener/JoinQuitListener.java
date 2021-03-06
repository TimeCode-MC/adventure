package de.timecode.advgames.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.timecode.advgames.main.AdvGames;
import de.timecode.advgames.state.GameState;

public class JoinQuitListener implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(AdvGames.pl.state == GameState.START) {
			if(Bukkit.getOnlinePlayers().size() >= 2) {
				AdvGames.pl.startCountdown();
			}else {
				AdvGames.pl.stopCountdown();
			}
		}else {
			AdvGames.pl.setSpectator(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(AdvGames.pl.state == GameState.START) {
			Bukkit.getScheduler().runTaskLater(AdvGames.pl, new Runnable() {
				
				@Override
				public void run() {
					if(Bukkit.getOnlinePlayers().size() >= 2) {
						AdvGames.pl.startCountdown();
					}else {
						AdvGames.pl.stopCountdown();
					}
					
				}
			}, 5);
		}else {
			AdvGames.pl.players.remove(e.getPlayer());
		}
	}

}

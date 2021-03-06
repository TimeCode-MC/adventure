package de.timecode.advgames.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.timecode.advgames.main.AdvGames;

public class TeleportGUI {
	
	public TeleportGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, AdvGames.pl.messages.getProperty("SpecInvName").toString().replace("&", "§"));
		ArrayList<String> names = new ArrayList<>();
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(AdvGames.pl.players.contains(all)) {
				names.add(all.getName());
			}
		}
		
		for(int i = 0; i <= 26; i++) {
			if(i <= names.size()) {
			ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta sm = (SkullMeta)is.getItemMeta();
			sm.setOwner(names.get(i).toString());
			sm.setDisplayName(AdvGames.pl.messages.getProperty("SkullFormat").replace("&", "§").replace("%name%", names.get(i).toString()));
			is.setItemMeta(sm);
			inv.setItem(i, is);
			i++;
			}
		}
		
		p.openInventory(inv);
	}

}

package de.timecode.advgames.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.timecode.advgames.command.AdvCommand;
import de.timecode.advgames.listener.CompassListener;
import de.timecode.advgames.listener.InteractListener;
import de.timecode.advgames.listener.JoinQuitListener;
import de.timecode.advgames.listener.KillEvent;
import de.timecode.advgames.state.GameState;

public class AdvGames extends JavaPlugin{
	
	public static AdvGames pl;
	
	public String prefix = "§3§lAdvGames §8〣 §7";
	public ArrayList<Integer> counts = new ArrayList<>();
	public ArrayList<Player> players = new ArrayList<>();
	public ArrayList<Block> blocks = new ArrayList<>();
	public Properties messages = new Properties();
	
	FileInputStream fi;
	FileOutputStream out;
	
	boolean running;
	int stopid;
	public int time = 60;
	
	public File f = new File("plugins//RoyalPixels", "datas.yml");
	public YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
	
	public File f2 = new File("plugins//RoyalPixels", "items.yml");
	public YamlConfiguration items = YamlConfiguration.loadConfiguration(f2);
	
	public GameState state = GameState.START;
	
	

	
	public void onEnable() {
		cfg.options().copyDefaults(true);
		items.options().copyDefaults(true);
		ArrayList<String> l = new ArrayList<>();
		items.addDefault("Items", l);
		items.addDefault("Amount", "[]");
		saveItems();
		save();
		state = GameState.START;
		pl = this;
		getServer().getPluginManager().registerEvents(new KillEvent(), this);
		getServer().getPluginManager().registerEvents(new InteractListener(), this);
		getServer().getPluginManager().registerEvents(new CompassListener(), this);
		getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
		getCommand("advgames").setExecutor(new AdvCommand());
		
		
		File folder = new File("plugins/RoyalPixels");
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		counts.add(60);
		counts.add(30);
		counts.add(20);
		counts.add(10);
		counts.add(5);
		counts.add(4);
		counts.add(3);
		counts.add(2);
		counts.add(1);
		
		File f = new File("plugins/RoyalPixels/messages.properties");
		if(!f.exists()) {
			try {
				f.createNewFile();
				try {
					fi = new FileInputStream(f);
					out = new FileOutputStream(f);
					messages.load(fi);
					
					messages.setProperty("Prefix", "&3&lAdvGames &8〣 &7");
						messages.setProperty("Start1", "&7Die Runde beginnt in &b%seconds% &7Sekunden");
						messages.setProperty("Start2", "&7Die Runde beginnt in &beiner &7Sekunde");
						messages.setProperty("Death", "&7Der Spieler &b%player% &7wurde von &b%killer% &7getötet!");
						messages.setProperty("NoPerm", "&cDazu hast du keine Berechtigung!");
						messages.setProperty("SetSpawn", "&7Der &b%name%-Spawn &7wurde gesetzt!");
						messages.setProperty("Syntax", "&cDieser Command existiert nicht! /advgames help");
						messages.setProperty("SpecInvName", "&cKompass");
						messages.setProperty("SkullFormat", "&6%name%");
						messages.setProperty("Compass", "&8&l〣 &c&lKompass");
					messages.store(out, "Message-Settings");


				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fi = new FileInputStream(f);
			messages.load(fi);
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefix = messages.getProperty("Prefix").replace("&", "§");
		
	}
	
	public void onDisable() {
		
	}
	
    public void setSpectator(Player p) {
    	ItemStack is = new ItemStack(Material.COMPASS);
    	ItemMeta im = is.getItemMeta();
    	im.setDisplayName(messages.getProperty("Compass").replace("&", "§"));
    	is.setItemMeta(im);
    	
    	
    	players.remove(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 33333333));
		p.setFlying(true);
		p.teleport(getSpawn("Spec"));
		p.getInventory().addItem(is);
	}
	
	public void startCountdown() {
		if(!running) {
			running = true;
			stopid = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
				
				
				@Override
				public void run() {				
						String start1 = messages.getProperty("Start1");
						String start2 = messages.getProperty("Start2");
				
					if(counts.contains(time)) {
					if(time != 1) {
						Bukkit.getServer().broadcastMessage(prefix + start1.replace("&", "§").replace("%seconds%", String.valueOf(time)));
					}else {
						Bukkit.getServer().broadcastMessage(prefix + start2.replace("&", "§").replace("%seconds%", String.valueOf(time)));
					}
					}else if(time == 0) {
						stopCountdown();
						startGame();
						time = 60;
					}
					time--;
				}
			},0, 20);
		}
	}
	
	public void stopCountdown() {
		if(running) {
			running = false;
			Bukkit.getScheduler().cancelTask(stopid);
		}
	}
	
	public void startGame() {
		state = GameState.INGAME;
		for(Player all : Bukkit.getOnlinePlayers()) {
			AdvGames.pl.players.add(all);
			all.setHealth(20);
			all.setFoodLevel(20);
			all.setGameMode(GameMode.SURVIVAL);
			all.setAllowFlight(false);
			all.teleport(getSpawn("Game"));
			all.getInventory().clear();
			all.playSound(all.getLocation(), Sound.LEVEL_UP, 2, 2);
			spawnDiamondChestBlock(15);
		}
	}
	
	public void setSpawn(Location loc, String name) {
		cfg.set(name, loc);
		save();
	}
	
	public Location getSpawn(String name) {
		if(cfg.get(name) != null) {
			return (Location) cfg.get(name);
		}
		return null;
	}
	
	public void save() {
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addItem(Material m, int amount, int subid) {
		ItemStack is = new ItemStack(m);
		List<ItemStack> it = (List<ItemStack>) items.getList("Items");
		List<Integer> ita = (List<Integer>) items.getIntegerList("Amount");
		it.add(is);
		ita.add(amount);
		items.set("Items", it);
		items.set("Amount", ita);
		saveItems();
	}
	
	public ItemStack getItemStack(int slot) {
		List<ItemStack> it = (List<ItemStack>) items.getList("Items");
		List<Integer> ita = (List<Integer>) items.getIntegerList("Amount");
		
		ItemStack is = it.get(slot);
		is.setAmount(ita.get(slot));
		return is;
	}
	
	public void saveItems() {
		try {
			items.save(f2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void spawnDiamondChestBlock(Integer chests) {
		boolean b = false;
		Location base = null;
		for(int i = 0; i <= chests; i++) {
			if(!b) {
			base = getSpawn("Game");
			}
			b = true;
			Random x = new Random();
			Random z = new Random();
			
			Integer rx = base.getBlockX()+x.nextInt(100);
			
			Integer rz = base.getBlockZ()+z.nextInt(100);
			
			boolean search = false;
			Integer y = 0;
			for(int s = 0; s <= 20 ;s++) {
				if(search == false) {
				Block hb = base.getWorld().getBlockAt(rx, base.getWorld().getHighestBlockYAt(rx, rz)-1, rz);
				y = base.getWorld().getHighestBlockYAt(rx, rz);
				if(hb.getType() != Material.WATER && hb.getType() != Material.STATIONARY_WATER && hb.getType() != Material.WOOD && hb.getType() != Material.LOG && hb.getType() != Material.LOG_2 && hb.getType() != Material.GRASS && hb.getType() != Material.LONG_GRASS && hb.getType() != Material.FLOWER_POT && hb.getType() != Material.LEAVES && hb.getType() != Material.LEAVES_2) {
					search = true;
				}
				if(search) {
					Location newloc = new Location(base.getWorld(), rx.doubleValue(), y.doubleValue(), rz.doubleValue());
					base.getWorld().getBlockAt(newloc).setType(Material.DIAMOND_BLOCK); 
					blocks.add(base.getWorld().getBlockAt(newloc));
				}
				}
			}
			
		}
	}
	
}

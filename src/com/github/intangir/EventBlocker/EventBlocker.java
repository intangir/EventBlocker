package com.github.intangir.RingTransporters;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class EventBlocker extends JavaPlugin implements Listener {

    public Logger log;
    public PluginDescriptionFile pdfFile;
    public PlateListener plateListener;
    
	public void onEnable() {
		log = this.getLogger();
		pdfFile = this.getDescription();
		plateListener = new PlateListener();

		Bukkit.getPluginManager().registerEvents(plateListener, this);


		
		log.info(pdfFile.getName() + " v" + pdfFile.getVersion() + " enabled!");
	}
	
	public void onDisable() {
		log.info(pdfFile.getName() + " v" + pdfFile.getVersion() + " disabled.");
	}

	@EventHandler(ignoreCancelled=true)
	public void onEntityInteract(EntityInteractEvent e) {
		if(e.getBlock().getType() == Material.STONE_PLATE) {
			e.getEntity().getWorld().playEffect(e.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			e.getEntity().getWorld().playEffect(e.getBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
		}
    }

	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock().getType() == Material.STONE_PLATE) {
			e.getPlayer().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			e.getPlayer().getWorld().playEffect(e.getClickedBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
		}

	}
	
}


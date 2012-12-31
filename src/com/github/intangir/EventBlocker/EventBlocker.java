package com.github.intangir.EventBlocker;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class EventBlocker extends JavaPlugin implements Listener
{
    public Logger log;
    public PluginDescriptionFile pdfFile;
    
    public void info(String message)
    {
    	log.info("[" + pdfFile.getName() + "] " + message);
    }
    
	public void onEnable()
	{
		log = this.getLogger();
		pdfFile = this.getDescription();

		Bukkit.getPluginManager().registerEvents(this, this);
		
		info("v" + pdfFile.getVersion() + " enabled!");
	}
	
	public void onDisable()
	{
		info("v" + pdfFile.getVersion() + " disabled.");
	}

	// block villager trading
	@EventHandler(ignoreCancelled=true)
	public void onEntityInteract(PlayerInteractEntityEvent e)
	{
	    Entity npc = e.getRightClicked();
	    if (npc == null)
	    {
	        return;
	    }
	    
	    if (npc.getType() == EntityType.VILLAGER)
	    {
	      e.setCancelled(true);
	    }
    }

	// block enderchests 
	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(e.getClickedBlock().getType() == Material.ENDER_CHEST)
		{
			e.setCancelled(true);
		}
	}
	@EventHandler(ignoreCancelled=true)
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if(e.getBlock().getType() == Material.ENDER_CHEST)
		{
			e.setCancelled(true);
		}
	}
	
	// block portals
	@EventHandler(ignoreCancelled=true)
	public void onPortalCreate(PortalCreateEvent e)
	{
		e.setCancelled(true);
	}

	// temporary spawner switcher
	@EventHandler(ignoreCancelled=true)
	public void onChunkPopulate(ChunkPopulateEvent e)
	{
		for (BlockState state: e.getChunk().getTileEntities())
		{
			if(state instanceof CreatureSpawner)
			{
				if(state.getBlock().getBiome() == Biome.HELL)
				{
					EntityType et = ((CreatureSpawner) state).getSpawnedType();
					if(et == EntityType.ZOMBIE || et == EntityType.SKELETON)
					{
						((CreatureSpawner) state).setSpawnedType(EntityType.BLAZE);
						info("Changed spawner to blaze");
					}
				}
			}
		}
	}
}


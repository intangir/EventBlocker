package com.github.intangir.EventBlocker;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class EventBlocker extends JavaPlugin implements Listener
{
    public Logger log;
    public PluginDescriptionFile pdfFile;
    
	public void onEnable()
	{
		log = this.getLogger();
		pdfFile = this.getDescription();

		Bukkit.getPluginManager().registerEvents(this, this);
		
		log.info("v" + pdfFile.getVersion() + " enabled!");
	}
	
	public void onDisable()
	{
		log.info("v" + pdfFile.getVersion() + " disabled.");
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
	
	// disable ender dragon
	@EventHandler(ignoreCancelled=true)
	public void onCreatureSpawn(CreatureSpawnEvent e)
	{
		if(e.getEntityType() == EntityType.ENDER_DRAGON)
		{
			e.setCancelled(true);
		}
	}

	// disable death message
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent e)
	{
		log.info(e.getDeathMessage());
		e.setDeathMessage(null);
	}

	// disable join message
	@EventHandler(priority=EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(null);
	}

	// disable quit message
	@EventHandler(priority=EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e)
	{
		e.setQuitMessage(null);
	}
}


package com.github.intangir.EventBlocker;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
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
	
	// block enderchests 
	@EventHandler(ignoreCancelled=true)
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(e.getClickedBlock().getType() == Material.ENDER_CHEST)
		{
			e.setCancelled(true);
		}
		
		// block water going down on cauldrons
		if(e.getClickedBlock().getType() == Material.CAULDRON && e.getMaterial() == Material.GLASS_BOTTLE && e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = e.getClickedBlock();
			if(block.getData() > 0)
			{
				block.setData((byte)(block.getData()+1));
			}
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
	@EventHandler(ignoreCancelled=true)
	public void onEntityPortalCreate(EntityCreatePortalEvent e)
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
		Location location = e.getEntity().getLocation();
		String msg = e.getDeathMessage() + " ([" + location.getWorld().getName() + "] " + (int)location.getX() + ", " + (int)location.getY() + ", " + (int)location.getZ() + ")";
		log.info(msg);
		e.getEntity().sendMessage(ChatColor.RED + msg);
		e.setDeathMessage(ChatColor.RED + e.getDeathMessage());
	}

	// disable join message
	/*@EventHandler(priority=EventPriority.MONITOR)
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

	// disable kick message
	@EventHandler(priority=EventPriority.MONITOR)
	public void onKick(PlayerKickEvent e)
	{
		e.setLeaveMessage(null);
	}*/
	
	// disable rail placement near piston (avoid dupe bug)
	@EventHandler
	public void onRailPlace(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		Material t = b.getType();
		
		if(t == Material.RAILS || t == Material.POWERED_RAIL || t == Material.DETECTOR_RAIL)
		{
			BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
			for (BlockFace face : faces)
			{
				t = b.getRelative(face).getType();
				if(t == Material.PISTON_STICKY_BASE || t == Material.PISTON_EXTENSION || t == Material.PISTON_MOVING_PIECE || t == Material.PISTON_BASE)
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	// disable pushing rails  (avoid dupe bug)
	@EventHandler
	public void onPistonPushRail(BlockPistonExtendEvent e)
	{
		for(Block b : e.getBlocks())
		{
			Material t = b.getType();
			
			if(t == Material.RAILS || t == Material.POWERED_RAIL || t == Material.DETECTOR_RAIL)
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	// disable endermen griefing
	@EventHandler(ignoreCancelled=true)
	public void onEntityChangeBlock(EntityChangeBlockEvent e)
	{
		if(e.getEntity() instanceof Enderman)
		{
			e.setCancelled(true);
		}
	}
	
}


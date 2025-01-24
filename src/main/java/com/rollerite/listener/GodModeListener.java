package com.rollerite.listener;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GodModeListener implements Listener
{
	private final RolleritePlugin rolleritePlugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelDamageTaken(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player player))
		{
			return;
		}
		
		RolleritePlayer rolleritePlayer = rolleritePlugin.getPlayerListener().get(player);
		
		if(rolleritePlayer.isGodMode())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelFoodDecrease(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player player))
		{
			return;
		}
		
		RolleritePlayer rolleritePlayer = rolleritePlugin.getPlayerListener().get(player);
		
		if(rolleritePlayer.isGodMode() && event.getFoodLevel() < player.getFoodLevel())
		{
			event.setCancelled(true);
		}
	}
}

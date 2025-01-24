package com.rollerite.listener;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerListener implements Listener
{
	private final RolleritePlugin rolleritePlugin;
	
	private final Map<UUID, RolleritePlayer> rolleritePlayers = new HashMap<>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		rolleritePlayers.put(player.getUniqueId(), new RolleritePlayer(rolleritePlugin, player));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		rolleritePlayers.remove(player.getUniqueId());
	}
	
	public RolleritePlayer get(Player player)
	{
		return get(player.getUniqueId());
	}
	
	public RolleritePlayer get(UUID uniqueId)
	{
		return rolleritePlayers.get(uniqueId);
	}
}

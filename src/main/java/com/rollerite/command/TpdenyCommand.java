package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import com.rollerite.util.TeleportRequest;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.rollerite.message.RolleriteMessage.*;

@RequiredArgsConstructor
public class TpdenyCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
	{
		if(!(commandSourceStack.getSender() instanceof Player denierPlayer))
		{
			return Collections.emptyList();
		}
		
		RolleritePlayer denierRolleritePlayer = rolleritePlugin.getPlayerListener().get(denierPlayer);
		
		List<String> suggestions = new ArrayList<>();
		
		if(args.length == 0 || args.length == 1)
		{
			String arg0 = args.length == 0 ? "" : args[0];
			
			for(TeleportRequest teleportRequest : denierRolleritePlayer.getTeleportRequests().values())
			{
				final String name = teleportRequest.getSender().getName();
				
				if(name.toLowerCase().startsWith(arg0.toLowerCase()))
				{
					suggestions.add(name);
				}
			}
			
			return suggestions;
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		CommandSender commandSender = commandSourceStack.getSender();
		RolleriteCommandSender rolleriteCommandSender = rolleritePlugin.getCommandSenderManager().getCommandSender(commandSender);
		
		if(!(rolleriteCommandSender instanceof RolleritePlayer denierRolleritePlayer))
		{
			return;
		}
		
		if(args.length == 1)
		{
			Player denierPlayer = denierRolleritePlayer.getPlayer();
			Player requesterPlayer = rolleritePlugin.getServer().getPlayerExact(args[0]);
			
			Context ctx = new Context()
					.player(requesterPlayer)
					.senderPlayer(requesterPlayer)
					.receiverPlayer(denierPlayer);
			
			if(requesterPlayer == null)
			{
				denierRolleritePlayer.sendMessage(PLAYER_NOT_FOUND, ctx);
				return;
			}
			
			RolleritePlayer requesterRolleritePlayer = rolleritePlugin.getPlayerListener().get(requesterPlayer);
			
			if(!denierRolleritePlayer.removeTeleportRequest(requesterRolleritePlayer))
			{
				denierRolleritePlayer.sendMessage(COMMAND_TPA_NO_REQUEST, ctx);
				return;
			}
			
			denierRolleritePlayer.sendMessage(COMMAND_TPDENY_YOU_DENIED, ctx);
			requesterRolleritePlayer.sendMessage(COMMAND_TPA_DENIED, ctx);
		}
		else
		{
			denierRolleritePlayer.sendMessage(COMMAND_TPDENY_USAGE);
		}
	}
	
	@Override
	public @NotNull String permission()
	{
		return "command.tpdeny";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && sender.hasPermission(permission());
	}
}

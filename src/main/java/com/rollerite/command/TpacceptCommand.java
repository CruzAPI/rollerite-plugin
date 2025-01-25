package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import com.rollerite.util.TeleportRequest;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.rollerite.message.RolleriteMessage.*;
import static org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT;

@RequiredArgsConstructor
public class TpacceptCommand implements BasicCommand
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
		
		if(!(rolleriteCommandSender instanceof RolleritePlayer rolleriteAccepterPlayer))
		{
			return;
		}
		
		if(args.length == 1)
		{
			Player accepterPlayer = rolleriteAccepterPlayer.getPlayer();
			Player requesterPlayer = rolleritePlugin.getServer().getPlayerExact(args[0]);
			
			Context ctx = new Context()
					.player(requesterPlayer)
					.senderPlayer(requesterPlayer)
					.receiverPlayer(accepterPlayer);
			
			if(requesterPlayer == null)
			{
				rolleriteAccepterPlayer.sendMessage(PLAYER_NOT_FOUND, ctx);
				return;
			}
			
			RolleritePlayer rolleriteRequesterPlayer = rolleriteAccepterPlayer.getRolleritePlugin().getPlayerListener().get(requesterPlayer);
			
			if(!rolleriteAccepterPlayer.removeTeleportRequest(rolleriteRequesterPlayer))
			{
				rolleriteAccepterPlayer.sendMessage(COMMAND_TPA_NO_REQUEST, ctx);
				return;
			}
			
			if(!requesterPlayer.teleport(accepterPlayer))
			{
				rolleriteAccepterPlayer.sendMessage(COMMAND_TPACCEPT_TELEPORT_FAILED, ctx);
				rolleriteRequesterPlayer.sendMessage(COMMAND_TPA_TELEPORT_FAILED, ctx);
				return;
			}
			
			Sound teleportSound = Sound.sound()
					.type(ENTITY_ENDERMAN_TELEPORT)
					.build();
			
			accepterPlayer.playSound(teleportSound, requesterPlayer);
			requesterPlayer.playSound(teleportSound, requesterPlayer);
			
			rolleriteAccepterPlayer.sendMessage(COMMAND_TPACCEPT_YOU_ACCEPTED, ctx);
			rolleriteRequesterPlayer.sendMessage(COMMAND_TPA_ACCEPTED, ctx);
		}
		else
		{
			rolleriteAccepterPlayer.sendMessage(COMMAND_TPACCEPT_USAGE);
		}
	}
	
	@Override
	public @NotNull String permission()
	{
		return "command.tpaccept";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && sender.hasPermission(permission());
	}
}

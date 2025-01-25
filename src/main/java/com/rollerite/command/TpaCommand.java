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
public class TpaCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
	{
		if(!(commandSourceStack.getSender() instanceof Player playerSender))
		{
			return Collections.emptyList();
		}
		
		List<String> suggestions = new ArrayList<>();
		
		if(args.length == 0 || args.length == 1)
		{
			String arg0 = args.length == 0 ? "" : args[0];
			
			for(Player player : rolleritePlugin.getServer().getOnlinePlayers())
			{
				if(player == playerSender)
				{
					continue;
				}
				
				if(player.getName().toLowerCase().startsWith(arg0.toLowerCase()))
				{
					suggestions.add(player.getName());
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
		
		if(!(rolleriteCommandSender instanceof RolleritePlayer rolleriteSenderPlayer))
		{
			return;
		}
		
		if(args.length == 1)
		{
			Player senderPlayer = rolleriteSenderPlayer.getPlayer();
			Player receiverPlayer = rolleritePlugin.getServer().getPlayerExact(args[0]);
			
			Context ctx = new Context()
					.player(receiverPlayer)
					.senderPlayer(senderPlayer)
					.receiverPlayer(receiverPlayer);
			
			if(receiverPlayer == null)
			{
				rolleriteSenderPlayer.sendMessage(PLAYER_NOT_FOUND, ctx);
				return;
			}
			
			if(senderPlayer == receiverPlayer)
			{
				rolleriteSenderPlayer.sendMessage(COMMAND_TPA_SELF_REQUEST);
				return;
			}
			
			RolleritePlayer rolleriteReceiverPlayer = rolleriteSenderPlayer.getRolleritePlugin().getPlayerListener().get(receiverPlayer);
			
			TeleportRequest teleportRequest = new TeleportRequest(rolleriteSenderPlayer, rolleriteReceiverPlayer);
			
			if(!rolleriteReceiverPlayer.addTeleportRequest(teleportRequest))
			{
				rolleriteSenderPlayer.sendMessage(COMMAND_TPA_REQUEST_ALREADY_PENDING, ctx);
				return;
			}
			
			rolleriteSenderPlayer.sendMessage(COMMAND_TPA_REQUEST_SENT, ctx);
			rolleriteReceiverPlayer.sendMessage(COMMAND_TPA_REQUEST_RECEIVED, ctx);
			
			rolleritePlugin.getServer().getScheduler().runTaskLater(rolleritePlugin, () ->
			{
				if(rolleriteReceiverPlayer.removeTeleportRequest(teleportRequest))
				{
					rolleriteSenderPlayer.sendMessage(COMMAND_TPA_REQUESTER_EXPIRED, ctx);
					rolleriteReceiverPlayer.sendMessage(COMMAND_TPA_RECEIVER_EXPIRED, ctx);
				}
			}, 10L * 20L);
		}
		else
		{
			rolleriteSenderPlayer.sendMessage(COMMAND_TPA_USAGE);
		}
	}
	
	@Override
	public @NotNull String permission()
	{
		return "command.tpa";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && sender.hasPermission(permission());
	}
}

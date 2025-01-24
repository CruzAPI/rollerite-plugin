package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
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

import static com.rollerite.message.RolleriteMessage.COMMAND_OPENINV_USAGE;
import static com.rollerite.message.RolleriteMessage.PLAYER_NOT_FOUND;

@RequiredArgsConstructor
public class OpenInvCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
	{
		List<String> suggestions = new ArrayList<>();
		
		if(args.length == 0 || args.length == 1)
		{
			String arg0 = args.length == 0 ? "" : args[0];
			
			for(Player player : rolleritePlugin.getServer().getOnlinePlayers())
			{
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
		
		if(!(rolleriteCommandSender instanceof RolleritePlayer rolleritePlayer))
		{
			return;
		}
		
		if(args.length == 1)
		{
			Player player = rolleritePlayer.getPlayer();
			Player target = rolleritePlugin.getServer().getPlayerExact(args[0]);
			
			if(target == null)
			{
				rolleriteCommandSender.sendMessage(PLAYER_NOT_FOUND);
				return;
			}
			
			player.openInventory(target.getInventory());
		}
		else
		{
			rolleritePlayer.sendMessage(COMMAND_OPENINV_USAGE);
		}
	}
	
	@Override
	public @NotNull String permission()
	{
		return "command.openinv";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && sender.hasPermission(permission());
	}
}

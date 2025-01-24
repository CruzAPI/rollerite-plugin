package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.message.RolleriteMessage;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import com.rollerite.type.Gamemode;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.rollerite.message.RolleriteMessage.*;

@RequiredArgsConstructor
public class GamemodeCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
	{
		List<String> suggestions = new ArrayList<>();
		
		if(args.length == 0 || args.length == 1)
		{
			String arg0 = args.length == 0 ? "" : args[0];
			
			for(Gamemode gamemode : Gamemode.values())
			{
				if(gamemode.name().toLowerCase().startsWith(arg0.toLowerCase()))
				{
					suggestions.add(gamemode.name().toLowerCase());
				}
			}
			
			return suggestions;
		}
		
		if(args.length == 2)
		{
			for(Player player : rolleritePlugin.getServer().getOnlinePlayers())
			{
				if(player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
				{
					suggestions.add(player.getName());
				}
			}
			
			return suggestions;
		}
		
		return suggestions;
	}
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		CommandSender commandSender = commandSourceStack.getSender();
		RolleriteCommandSender rolleriteCommandSender = rolleritePlugin.getCommandSenderManager().getCommandSender(commandSender);
		
		if(args.length == 1 && rolleriteCommandSender instanceof RolleritePlayer rolleritePlayer
				|| args.length == 2)
		{
			Context ctx = new Context();
			
			Gamemode gamemode = Gamemode.getByArg(args[0]);
			
			ctx.gamemode(gamemode);
			
			if(gamemode == null)
			{
				rolleriteCommandSender.sendMessage(GAME_MODE_NOT_FOUND, ctx);
				return;
			}
			
			Player target = args.length == 1
					? ((RolleritePlayer) rolleriteCommandSender).getPlayer()
					: rolleritePlugin.getServer().getPlayerExact(args[1]);
			
			ctx.player(target);
			
			if(target == null)
			{
				rolleriteCommandSender.sendMessage(PLAYER_NOT_FOUND, ctx);
				return;
			}
			
			boolean changed = target.getGameMode() != gamemode.getBukkitGameMode();
			target.setGameMode(gamemode.getBukkitGameMode());
			
			if(commandSender == target)
			{
				if(changed)
				{
					rolleriteCommandSender.sendMessage(COMMAND_GAMEMODE_CHANGED, ctx);
				}
				else
				{
					rolleriteCommandSender.sendMessage(COMMAND_GAMEMODE_ALREADY, ctx);
				}
			}
			else
			{
				if(changed)
				{
					rolleriteCommandSender.sendMessage(COMMAND_GAMEMODE_TARGET_CHANGED, ctx);
				}
				else
				{
					rolleriteCommandSender.sendMessage(COMMAND_GAMEMODE_TARGET_ALREADY, ctx);
				}
			}
		}
		else
		{
			RolleriteMessage usageMessage = rolleriteCommandSender instanceof RolleritePlayer
					? COMMAND_GAMEMODE_USAGE
					: COMMAND_GAMEMODE_CONSOLE_USAGE;
			rolleriteCommandSender.sendMessage(usageMessage);
		}
	}
	
	@Override
	public @Nullable String permission()
	{
		return "command.gamemode";
	}
}

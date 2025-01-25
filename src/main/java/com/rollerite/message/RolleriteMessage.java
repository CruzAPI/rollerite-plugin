package com.rollerite.message;

import com.google.common.base.Preconditions;
import com.rollerite.RolleritePlugin;
import com.rollerite.i18n.MessageBundle;
import com.rollerite.i18n.TranslatableMessage;
import com.rollerite.type.Gamemode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum RolleriteMessage implements TranslatableMessage<RolleriteMessage.Context>
{
	PLAYER_NOT_FOUND("player-not-found"),
	GAME_MODE_NOT_FOUND("game-mode-not-found"),
	
	COMMAND_GAMEMODE_USAGE("command.gamemode.usage"),
	COMMAND_GAMEMODE_CONSOLE_USAGE("command.gamemode.console-usage"),
	COMMAND_GAMEMODE_CHANGED("command.gamemode.changed", (locale, context) -> new TagResolver[]
	{
		Placeholder.component("gamemode", context.getGamemode().getStylishedTranslatableComponent()),
	}),
	COMMAND_GAMEMODE_TARGET_CHANGED("command.gamemode.target-changed", (locale, context) -> new TagResolver[]
	{
		Placeholder.component("player", context.getPlayer().displayName()),
		Placeholder.component("gamemode", context.getGamemode().getStylishedTranslatableComponent()),
	}),
	COMMAND_GAMEMODE_ALREADY("command.gamemode.already"),
	COMMAND_GAMEMODE_TARGET_ALREADY("command.gamemode.target-already", (locale, context) -> new TagResolver[]
	{
		Placeholder.component("player", context.getPlayer().displayName()),
	}),
	
	COMMAND_GOD_USAGE("command.god.usage"),
	COMMAND_GOD_CONSOLE_USAGE("command.god.console-usage"),
	COMMAND_GOD_ENABLE("command.god.enable"),
	COMMAND_GOD_DISABLE("command.god.disable"),
	COMMAND_GOD_TARGET_ENABLE("command.god.target-enable", (locale, context) -> new TagResolver[]
	{
		Placeholder.component("player", context.getPlayer().displayName()),
	}),
	COMMAND_GOD_TARGET_DISABLE("command.god.target-disable", (locale, context) -> new TagResolver[]
	{
		Placeholder.component("player", context.getPlayer().displayName()),
	}),
	
	COMMAND_OPENINV_USAGE("command.openinv.usage"),
	COMMAND_ENDERCHEST_USAGE("command.enderchest.usage"),
	;
	
	private final String key;
	private final BiFunction<Locale, Context, TagResolver[]> translateArgumentsBiFunction;
	
	RolleriteMessage(String key)
	{
		this(key, (locale, context) -> new TagResolver[0]);
	}
	
	@Override
	public MessageBundle getMessageBundle(Locale locale)
	{
		return RolleritePlugin.getInstance().getMessageBundleLoader().getBundle("message/message", locale);
	}
	
	@Override
	public TagResolver[] translateArguments(Locale locale, Context context)
	{
		return translateArgumentsBiFunction.apply(locale, context);
	}
	
	@Override
	public Plugin getPlugin()
	{
		return RolleritePlugin.getInstance();
	}
	
	@Override
	public Context newContext()
	{
		return new Context();
	}
	
	public static class Context
	{
		private Player player;
		private Gamemode gamemode;
		
		public Context player(Player player)
		{
			this.player = player;
			return this;
		}
		
		public Context gamemode(Gamemode gamemode)
		{
			this.gamemode = gamemode;
			return this;
		}
		
		public Player getPlayer()
		{
			return Preconditions.checkNotNull(player, "player is null");
		}
		
		public Gamemode getGamemode()
		{
			return Preconditions.checkNotNull(gamemode, "gamemode is null");
		}
	}
}

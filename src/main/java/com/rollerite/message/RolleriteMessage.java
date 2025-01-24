package com.rollerite.message;

import com.google.common.base.Preconditions;
import com.rollerite.RolleritePlugin;
import com.rollerite.i18n.MessageBundle;
import com.rollerite.i18n.TranslatableMessage;
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
	HELLO_WORLD("hello.world"),
	HELLO_PLAYER("hello.player", (locale, context) -> new TagResolver[]
	{
		Placeholder.component("player", context.getPlayer().displayName()),
	}),
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
		
		public Context player(Player player)
		{
			this.player = player;
			return this;
		}
		
		public Player getPlayer()
		{
			return Preconditions.checkNotNull(player, "player is null");
		}
	}
}

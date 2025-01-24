package com.rollerite.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import org.bukkit.GameMode;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.NamedTextColor.*;

@RequiredArgsConstructor
@Getter
public enum Gamemode
{
	SURVIVAL(GameMode.SURVIVAL, Style.style(GREEN)),
	CREATIVE(GameMode.CREATIVE, Style.style(RED)),
	ADVENTURE(GameMode.ADVENTURE, Style.style(YELLOW)),
	SPECTATOR(GameMode.SPECTATOR, Style.style(AQUA)),
	;
	
	private static final Map<Integer, Gamemode> BY_ID = Arrays.stream(values())
			.collect(Collectors.toUnmodifiableMap(Gamemode::getValue, Function.identity()));
	private static final Map<String, Gamemode> BY_NAME = Arrays.stream(values())
			.collect(Collectors.toUnmodifiableMap(Gamemode::name, Function.identity()));
	
	private final GameMode gameMode;
	private final Style style;
	
	public TranslatableComponent getStylishedTranslatableComponent()
	{
		return Component.translatable(translationKey()).style(style);
	}
	
	public GameMode getBukkitGameMode()
	{
		return gameMode;
	}
	
	public int getValue()
	{
		return gameMode.getValue();
	}
	
	public String translationKey()
	{
		return gameMode.translationKey();
	}
	
	public static Gamemode getById(int id)
	{
		return BY_ID.get(id);
	}
	
	public static Gamemode getByName(String name)
	{
		return BY_NAME.get(name);
	}
	
	public static Gamemode getByArg(String arg)
	{
		try
		{
			return getById(Integer.parseInt(arg));
		}
		catch(NumberFormatException e)
		{
			return getByName(arg.toUpperCase());
		}
	}
}

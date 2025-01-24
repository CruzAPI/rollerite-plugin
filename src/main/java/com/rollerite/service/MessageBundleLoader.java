package com.rollerite.service;

import com.google.common.base.Preconditions;
import com.rollerite.RolleritePlugin;
import com.rollerite.i18n.MessageBundle;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import static com.rollerite.i18n.TranslatableMessage.DEFAULT_LOCALE;
import static com.rollerite.i18n.TranslatableMessage.getSupportedLocaleOrDefault;

@RequiredArgsConstructor
public class MessageBundleLoader
{
	private final RolleritePlugin rolleritePlugin;
	
	private final Map<String, MessageBundle> messageBundleMap = new HashMap<>();
	
	public MessageBundle getBundle(String baseName, Locale locale)
	{
		return getSupportedBundle(baseName, getSupportedLocaleOrDefault(locale));
	}
	
	private MessageBundle getSupportedBundle(String baseName, Locale locale)
	{
		final String fullName = getFullName(baseName, locale);
		
		return messageBundleMap.computeIfAbsent(fullName, k ->
		{
			try
			{
				return loadBundle(baseName, locale);
			}
			catch(IOException e)
			{
				if(DEFAULT_LOCALE.equals(locale))
				{
					rolleritePlugin.getLogger().log(Level.SEVERE, "Failed to load " + fullName);
					throw new RuntimeException(e);
				}
				
				rolleritePlugin.getLogger().log(Level.WARNING, "Failed to load " + fullName + ". Plugin will try to load for default locale as fallback.", e);
				return getBundle(baseName, DEFAULT_LOCALE);
			}
		});
	}
	
	private MessageBundle loadBundle(final String baseName, final Locale locale) throws IOException
	{
		final String fullName = getFullName(baseName, locale);
		
		Properties properties = rolleritePlugin.getPropertiesLoader().loadProperties(fullName);
		return new MessageBundle(rolleritePlugin, baseName, locale, fullName, properties);
	}
	
	private String getFullName(String baseName, Locale locale)
	{
		Preconditions.checkNotNull(baseName, "baseName is null");
		
		return baseName + "_" + locale.toString() + ".properties";
	}
}

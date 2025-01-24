package com.rollerite.i18n;

import com.google.common.base.Preconditions;
import com.rollerite.RolleritePlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

@Getter
@RequiredArgsConstructor
public class MessageBundle
{
	private final RolleritePlugin rolleritePlugin;
	private final String baseName;
	private final Locale locale;
	private final String fullName;
	
	private final Properties properties;
	
	public String getString(String key)
	{
		Preconditions.checkNotNull(key, "key is null");
		return Objects.requireNonNull(properties.getProperty(key), "Missing key \"" + key + "\" in " + fullName);
	}
}

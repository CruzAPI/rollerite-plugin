package com.rollerite.util;

import com.rollerite.player.RolleritePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TeleportRequest
{
	private final RolleritePlayer sender;
	private final RolleritePlayer receiver;
	
	public boolean isValid()
	{
		return receiver.getTeleportRequests().get(sender.getUniqueId()) == this;
	}
}

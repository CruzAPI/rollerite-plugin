package com.rollerite.functional;

@FunctionalInterface
public interface ContextBuilder<C>
{
	C build(C context);
}

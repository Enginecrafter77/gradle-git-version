package dev.enginecrafter77.gitversion;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface GitVersionFormatter {
	@Nonnull
	public String format(@Nonnull GitVersion version);
}

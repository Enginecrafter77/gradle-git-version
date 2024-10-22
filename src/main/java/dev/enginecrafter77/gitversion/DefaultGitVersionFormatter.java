package dev.enginecrafter77.gitversion;

import javax.annotation.Nonnull;

public class DefaultGitVersionFormatter implements GitVersionFormatter {
	private static final DefaultGitVersionFormatter INSTANCE = new DefaultGitVersionFormatter();

	private DefaultGitVersionFormatter() {}

	@Nonnull
	@Override
	public String format(@Nonnull GitVersion version)
	{
		StringBuilder builder = new StringBuilder(String.format("%d.%d.%d", version.major, version.minor, version.patch));
		if(version.distance > 0 && version.commit != null)
			builder.append(String.format("-%d-g%s", version.distance, version.commit.name()));
		return builder.toString();
	}

	public static DefaultGitVersionFormatter get()
	{
		return INSTANCE;
	}
}

package dev.enginecrafter77.gitversion;

import org.gradle.api.internal.provider.PropertyFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;

import javax.annotation.Nonnull;

public abstract class GradleGitVersionExtension {
	public final Property<GitVersionFormatter> formatter;

	public GradleGitVersionExtension(PropertyFactory propertyFactory)
	{
		this.formatter = propertyFactory.property(GitVersionFormatter.class).convention(DefaultGitVersionFormatter.get());
	}

	public void format(Provider<GitVersionFormatter> provider)
	{
		this.formatter.set(provider);
	}

	public void format(@Nonnull CharSequence format)
	{
		this.formatter.set(SimpleGitVersionFormatter.from(format.toString()));
	}

	public void format(@Nonnull GitVersionFormatter formatter)
	{
		this.formatter.set(formatter);
	}
}

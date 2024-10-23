package dev.enginecrafter77.gitversion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gradle.api.provider.Provider;

import javax.annotation.Nonnull;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "from")
public class GitVersionProviderDelegate extends ProviderDelegate<GitVersion> {
	public final Provider<GitVersion> provider;
	public final Provider<GitVersionFormatter> formatter;

	@Nonnull
	@Override
	public String toString()
	{
		return this.asString().getOrElse("null");
	}

	@Nonnull
	public Provider<String> asString()
	{
		return this.provider.map((GitVersion version) -> this.formatter.getOrElse(DefaultGitVersionFormatter.get()).format(version));
	}

	@Override
	public Provider<GitVersion> getDelegatedProvider()
	{
		return this.provider;
	}
}

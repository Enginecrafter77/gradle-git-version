package dev.enginecrafter77.gitversion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Nullable;

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
		@Nullable GitVersion provided = this.provider.getOrNull();
		if(provided == null)
			return "null";
		return this.formatter.getOrElse(DefaultGitVersionFormatter.get()).format(provided);
	}

	@Override
	public Provider<GitVersion> getDelegatedProvider()
	{
		return this.provider;
	}
}

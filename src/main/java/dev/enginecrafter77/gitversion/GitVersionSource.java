package dev.enginecrafter77.gitversion;

import org.eclipse.jgit.api.Git;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ValueSource;
import org.gradle.api.provider.ValueSourceParameters;

import javax.annotation.Nullable;
import java.io.File;

public abstract class GitVersionSource implements ValueSource<GitVersion, GitVersionSource.Parameters> {
	@Nullable
	@Override
	public GitVersion obtain()
	{
		File projectDir = this.getParameters().getRepositoryDirectory().getAsFile().get();
		try(Git git = Git.open(projectDir))
		{
			String describeOutput = git.describe().setTags(true).call();
			if(describeOutput == null)
				throw new RuntimeException("Git describe returned null");
			return GitVersion.parse(describeOutput);
		}
		catch(Exception exc)
		{
			throw new RuntimeException(exc);
		}
	}

	public static interface Parameters extends ValueSourceParameters
	{
		public DirectoryProperty getRepositoryDirectory();
	}
}

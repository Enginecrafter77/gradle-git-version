package dev.enginecrafter77.gitversion;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ValueSource;
import org.gradle.api.provider.ValueSourceParameters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public abstract class GitVersionSource implements ValueSource<GitVersion, GitVersionSource.Parameters> {
	@Nullable
	@Override
	public GitVersion obtain()
	{
		VersionObtainMethod[] methods = {this::getVersionFromDescribe, this::getVersionWithNoTags, this::getVersionWithNoCommits};

		File projectDir = this.getParameters().getRepositoryDirectory().getAsFile().get();
		try(Git git = Git.open(projectDir))
		{
			for(VersionObtainMethod method : methods)
			{
				@Nullable GitVersion version = method.getVersion(git);
				if(version != null)
					return version;
			}
			return null;
		}
		catch(GitAPIException | IOException exc)
		{
			throw new RuntimeException(exc);
		}
	}

	@Nullable
	private GitVersion getVersionFromDescribe(Git git) throws GitAPIException
	{
		try
		{
			String describeOutput = git.describe().setTags(true).call();
			if(describeOutput == null)
				return null;
			return GitVersion.parse(describeOutput);
		}
		catch(RefNotFoundException exc)
		{
			return null;
		}
		catch(ParseException exc)
		{
			throw new RuntimeException(exc);
		}
	}

	@Nullable
	private GitVersion getVersionWithNoTags(Git git) throws GitAPIException
	{
		try
		{
			int count = 0;
			RevCommit last = null;
			for(RevCommit commit : git.log().call())
			{
				last = commit;
				++count;
			}
			if(last == null)
				return null;
			return GitVersion.builder().major(0).minor(0).patch(0).distance(count).commit(last.abbreviate(7)).build();
		}
		catch(NoHeadException exc)
		{
			return null;
		}
	}

	@Nonnull
	private GitVersion getVersionWithNoCommits(Git git)
	{
		return GitVersion.builder().major(0).minor(0).patch(0).distance(0).commit(AbbreviatedObjectId.fromString("0000000")).build();
	}

	public static interface Parameters extends ValueSourceParameters
	{
		public DirectoryProperty getRepositoryDirectory();
	}

	@FunctionalInterface
	private static interface VersionObtainMethod
	{
		public GitVersion getVersion(Git git) throws GitAPIException;
	}
}

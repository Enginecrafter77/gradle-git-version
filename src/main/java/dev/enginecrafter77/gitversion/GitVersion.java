package dev.enginecrafter77.gitversion;

import lombok.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.gradle.api.Project;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
public class GitVersion {
	private static final Pattern GIT_DESCRIBE_PATTERN = Pattern.compile("v?(\\d+)\\.(\\d+)\\.(\\d+)(?:-(\\d+)-g([0-9a-zA-Z]{7,}))?");

	public int major;
	public int minor;
	public int patch;

	public int distance;

	@Nullable
	public AbbreviatedObjectId commit;

	@Nullable
	public Ref findCommitRef(Project project)
	{
		if(this.commit == null)
			return null;

		try(Git git = Git.open(project.getProjectDir()))
		{
			Repository repository = git.getRepository();
			return repository.findRef(this.commit.name());
		}
		catch(IOException exc)
		{
			return null;
		}
	}

	public static GitVersion parse(@Nonnull String input) throws ParseException
	{
		Matcher matcher = GIT_DESCRIBE_PATTERN.matcher(input);
		if(!matcher.matches())
			throw new ParseException("String does not match semver-compilant git describe output pattern", matcher.end());
		GitVersion.GitVersionBuilder builder = GitVersion.builder();
		builder.major(Integer.parseInt(matcher.group(1)));
		builder.minor(Integer.parseInt(matcher.group(2)));
		builder.patch(Integer.parseInt(matcher.group(3)));
		@Nullable String heightGroup = matcher.group(4);
		@Nullable String commitGroup = matcher.group(5);
		if(heightGroup != null && commitGroup != null)
		{
			builder.distance(Integer.parseInt(heightGroup));
			builder.commit(AbbreviatedObjectId.fromString(commitGroup));
		}
		return builder.build();
	}
}

package dev.enginecrafter77.gitversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleGitVersionFormatter implements GitVersionFormatter {
	private final String formatString;

	public SimpleGitVersionFormatter(@Nonnull String formatString)
	{
		this.formatString = formatString;
	}

	@Nonnull
	@Override
	public String format(@Nonnull GitVersion version)
	{
		StringBuilder builder = new StringBuilder(this.formatString);
		Pattern varPattern = Pattern.compile("%(!)?(?:\\[([^]]+)])?([Mmpdc])");
		Matcher matcher = varPattern.matcher(builder);
		while(matcher.find())
		{
			boolean forceDisplay = matcher.group(1) != null;
			String format = matcher.group(2);
			if(format == null)
				format = "{}";
			char formatCode = matcher.group(3).charAt(0);
			@Nullable Object value = this.substituteFormat(version, formatCode);
			String repl = (!forceDisplay && canBeOmitted(version, formatCode)) ? "" : format.replace("{}", String.valueOf(value));
			builder.replace(matcher.start(), matcher.end(), repl);
			matcher.reset();
		}
		return builder.toString();
	}

	@Nullable
	private Object substituteFormat(@Nonnull GitVersion source, char formatChar)
	{
		switch(formatChar)
		{
		case 'M':
			return source.major;
		case 'm':
			return source.minor;
		case 'p':
			return source.patch;
		case 'd':
			return source.distance;
		case 'c':
			if(source.commit == null)
				return null;
			return source.commit.name();
		default:
			throw new UnsupportedOperationException("Unsupported format character " + formatChar);
		}
	}

	private boolean canBeOmitted(@Nonnull GitVersion source, char formatChar)
	{
		switch(formatChar)
		{
		case 'd':
			return source.distance == 0;
		case 'c':
			return source.commit == null;
		default:
			return false;
		}
	}

	public static SimpleGitVersionFormatter from(@Nonnull String formatString)
	{
		return new SimpleGitVersionFormatter(formatString);
	}
}

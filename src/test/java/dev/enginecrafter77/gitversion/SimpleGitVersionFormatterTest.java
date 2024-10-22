package dev.enginecrafter77.gitversion;

import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleGitVersionFormatterTest {
	@Test
	public void testSimpleFormat()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p").format(version);
		Assertions.assertEquals("1.0.0", formatted);
	}

	@Test
	public void testFormatWithExtraCharacters()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("m%M.m%m.m%p").format(version);
		Assertions.assertEquals("m1.m0.m0", formatted);
	}

	@Test
	public void testFormatWithProvidedDistance()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).distance(4).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p-%d").format(version);
		Assertions.assertEquals("1.0.0-4", formatted);
	}

	@Test
	public void testFormatWithMissingDistance()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p-%d").format(version);
		Assertions.assertEquals("1.0.0-", formatted);
	}

	@Test
	public void testFormatWithForcedDistance()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p-%!d").format(version);
		Assertions.assertEquals("1.0.0-0", formatted);
	}

	@Test
	public void testFormatWithComplexProvidedDistance()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).distance(4).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p%[-r{}]d").format(version);
		Assertions.assertEquals("1.0.0-r4", formatted);
	}

	@Test
	public void testFormatWithComplexMissingDistance()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p%[-r{}]d").format(version);
		Assertions.assertEquals("1.0.0", formatted);
	}

	@Test
	public void testFormatWithComplexForcedDistance()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p%![-r{}]d").format(version);
		Assertions.assertEquals("1.0.0-r0", formatted);
	}

	@Test
	public void testFormatWithProvidedCommit()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).commit(AbbreviatedObjectId.fromString("0123456")).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p-%c").format(version);
		Assertions.assertEquals("1.0.0-0123456", formatted);
	}

	@Test
	public void testFormatWithMissingCommit()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p-%c").format(version);
		Assertions.assertEquals("1.0.0-", formatted);
	}

	@Test
	public void testFormatWithForcedCommit()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p-%!c").format(version);
		Assertions.assertEquals("1.0.0-null", formatted);
	}

	@Test
	public void testFormatWithComplexProvidedCommit()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).commit(AbbreviatedObjectId.fromString("0123456")).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p%[-c{}]c").format(version);
		Assertions.assertEquals("1.0.0-c0123456", formatted);
	}

	@Test
	public void testFormatWithComplexMissingCommit()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p%[-c{}]c").format(version);
		Assertions.assertEquals("1.0.0", formatted);
	}

	@Test
	public void testFormatWithComplexForcedCommit()
	{
		GitVersion version = GitVersion.builder().major(1).minor(0).patch(0).build();
		String formatted = SimpleGitVersionFormatter.from("%M.%m.%p%![-c{}]c").format(version);
		Assertions.assertEquals("1.0.0-cnull", formatted);
	}
}

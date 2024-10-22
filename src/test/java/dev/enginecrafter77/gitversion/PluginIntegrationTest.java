package dev.enginecrafter77.gitversion;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PluginIntegrationTest extends GradleIntegrationTest {
	@Test
	public void testBasic() throws Exception
	{
		this.installBuildFile("simple.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("0.0.1", result.getOutput().trim());
	}

	@Test
	public void testWithHeightAndCommit() throws Exception
	{
		this.installBuildFile("simple.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");
		String commitName = this.makeCommit().abbreviate(7).name();

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("0.0.1-1-g" + commitName, result.getOutput().trim());
	}

	@Test
	public void testWithFormatterNoDistance() throws Exception
	{
		this.installBuildFile("formatted.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("v001", result.getOutput().trim());
	}

	@Test
	public void testWithFormatterWithDistance() throws Exception
	{
		this.installBuildFile("formatted.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");
		this.makeCommit();

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("v001", result.getOutput().trim());
	}

	@Test
	public void testWithFormatterConsideringDistanceWithoutDistance() throws Exception
	{
		this.installBuildFile("formatted2.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("v001", result.getOutput().trim());
	}

	@Test
	public void testWithFormatterConsideringDistanceWithDistance() throws Exception
	{
		this.installBuildFile("formatted2.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");
		String commitName = this.makeCommit().abbreviate(7).name();

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("v001-1-g" + commitName, result.getOutput().trim());
	}

	@Test
	public void testEarlyEval() throws Exception
	{
		this.installBuildFile("earlyeval.build.gradle");

		this.commitProject();
		this.makeTag("v0.0.1");

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("EARLY(0.0.1)\n0.0.1", result.getOutput().trim());
	}

	@Test
	public void testNoTags() throws Exception
	{
		this.installBuildFile("simple.build.gradle");

		String commitId = this.commitProject().abbreviate(7).name();

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("0.0.0-1-g" + commitId, result.getOutput().trim());
	}

	@Test
	public void testNoCommits() throws Exception
	{
		this.installBuildFile("simple.build.gradle");

		GradleRunner runner = GradleRunner.create();
		runner.withEnvironment(System.getenv());
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withProjectDir(projectDir);
		runner.withArguments("--quiet", "--stacktrace", "version");
		BuildResult result = runner.build();
		Assertions.assertEquals("0.0.0", result.getOutput().trim());
	}
}

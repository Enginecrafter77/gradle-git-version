package dev.enginecrafter77.gitversion;

import com.github.javafaker.Faker;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gradle.internal.impldep.org.testng.internal.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class GradleIntegrationTest {
	@TempDir
	File projectDir;

	@Nullable
	Git git = null;

	Faker faker;

	public GradleIntegrationTest()
	{
		this.faker = new Faker();
	}

	@BeforeEach
	public void setUpGit() throws GitAPIException
	{
		this.git = Git.init().setDirectory(this.projectDir).call();
	}

	@AfterEach
	public void cleanUpGit()
	{
		this.git.close();
	}

	private File getBuildFile() {
		return new File(projectDir, "build.gradle");
	}

	public void installBuildFile(String name) throws IOException
	{
		this.copyResourceToFile(name, this.getBuildFile());
	}

	public void copyResourceToFile(String resource, File dest) throws IOException
	{
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(resource);
		if(input == null)
			throw new FileNotFoundException("Resource not found");

		File dir = dest.getParentFile();
		if(dir != null && !dir.exists())
			dir.mkdirs();

		Files.copy(input, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		input.close();
	}

	public void addRandomFile(File dest) throws IOException
	{
		Random rng = new Random();
		try(OutputStream os = Files.newOutputStream(dest.toPath()))
		{
			byte[] data = new byte[1024];
			rng.nextBytes(data);
			os.write(data);
		}
	}

	public Ref makeTag(String tagName) throws GitAPIException
	{
		return this.git.tag().setAnnotated(true).setName(tagName).setMessage(tagName).call();
	}

	public RevCommit commitProject() throws GitAPIException
	{
		this.git.add().addFilepattern(".").call();
		return this.git.commit().setMessage("Initial commit").call();
	}

	public RevCommit makeCommit() throws IOException, GitAPIException
	{
		String filename = this.faker.internet().slug() + "." + this.faker.file().extension();
		this.addRandomFile(new File(this.projectDir, filename));
		this.git.add().addFilepattern(filename).call();
		return this.git.commit().setMessage(filename).call();
	}

	@FunctionalInterface
	public static interface TestAction<T>
	{
		public void execute(T t) throws Exception;
	}
}

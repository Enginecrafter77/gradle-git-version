package dev.enginecrafter77.gitversion;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class VersionPrintTask extends DefaultTask {
	@TaskAction
	public void run()
	{
		Project project = this.getProject();
		System.out.println(project.getVersion());
	}
}

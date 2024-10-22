/*
 * This file is part of the dropdroid project
 * Copyright (c) 2023 Enginecrafter77 <hutiramichal@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package dev.enginecrafter77.gitversion;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ValueSourceSpec;

import javax.annotation.Nonnull;

public class GradleGitVersionPlugin implements Plugin<Project> {
	@Override
	public void apply(@Nonnull Project project)
	{
		project.getExtensions().add("versioning", GradleGitVersionExtension.class);
		project.getTasks().register("version", VersionPrintTask.class);
		Provider<GitVersion> version = project.getProviders().of(GitVersionSource.class, (ValueSourceSpec<GitVersionSource.Parameters> spec) -> {
			spec.parameters((GitVersionSource.Parameters parameters) -> {
				parameters.getRepositoryDirectory().set(project.getLayout().getProjectDirectory());
			});
		});
		Provider<GitVersionFormatter> extensionFormatExtractor = project.provider(() -> {
			GradleGitVersionExtension extension = project.getExtensions().getByType(GradleGitVersionExtension.class);
			return extension.formatter.get();
		});
		project.setVersion(GitVersionProviderDelegate.from(version, extensionFormatExtractor));
	}
}

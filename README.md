# gitversion

## Overview

gitversion is a gradle plugin that utilizes the `org.eclipse.jgit` library
to load Git version info during build, which aims to simplify the project versioning process.

## Getting Started

First, add the plugin to gradle:

settings.gradle

```groovy
pluginManagement {
	repositories {
		maven {
			name "Enginecrafter77-Maven"
			url "https://maven.enginecrafter77.dev/general"
		}
	}
	plugins {
		id "dev.enginecrafter77.gitversion" version "<version>"
	}
}
```

build.gradle

```groovy
plugins {
	id "dev.enginecrafter77.gitversion"
}
```

And that's it! Your version is available from the `project.version` property.

## How it works

gitversion makes use of gradle's provider system, which enables lazy evaluation
of values. In our case, when we access `project.version`, nothing happens yet.
When we call `project.version.toString()` though, that's when the project's git version
is determined. Thanks to the jgit library, no external command is being run, and
thus you don't need git installed locally.

## Advanced usage

### Custom version format
The default format used is what `git describe --tags` gives, with few minor differences.
First of all, when there is no tag in the repository, the version is given as `0.0.0` with distnace
and commit being set appropriately.

One can, however, specify their own format as such:

build.gradle
```groovy
versioning {
    format "%M.%m.%p%[-{}]d%[-{}]c"
}
```

The formatting uses a simple substitutions representing individual components.

The available substitutions:
 * `%M` for major version
 * `%m` for minor version
 * `%p` for patch
 * `%d` for distance
 * `%c` for commit

Distance and commit may be, however, ommited from the pattern. Distance will be ommited
if it equals to 0 and commit if it equals to null (for tags on HEAD).

The variables can have additional patterns attached to them, so that if a variable is not ommited,
the whole pattern is appended, otherwise nothing at all is appended.

For example, the following pattern: `%[-{}]d` expands to `-1` when distance is 1,
but expands to empty string when distance is 0. The pattern is delimited by `[]` braces, and
inside the pattern, the `{}` is replaced by the value of the variable.

Additionally, one may force the pattern to display even if it can be ommited. This is done by adding `!` after the `%`
sign, as such: `%![-{}]d` will expand to `-1` but also to `-0`.

Moreover, if this is not enough, one may also provide a completely different formatting
by providing a `dev.enginecrafter77.gitversion.GitVersionFormatter` implementation.

For example, using lambda:
```groovy
versioning {
    format (version -> "${version.major}.${version.minor}.${version.patch}") 
}
```

### Accessing the version object
The git version is stored in an object of type `dev.enginecrafter77.gitversion.GitVersion`.

This object provides 5 fields:
 * `major`: The major version
 * `minor`: The minor version
 * `patch`: The patch version
 * `distance`: The distance to the last tag (0 when a tag is on HEAD)
 * `commit`: An abbreviated commit hash (null when a tag is on HEAD)

The `project.version` object acts as a delegate to the provider of `GitVersion` objects.
Thus, you can use `project.version.get()` to obtain a `GitVersion` instance.

Additionally, to convert the `project.version` into a string provider, you can use `project.version.asString()`
to create a lazy-evaluated string of the version.

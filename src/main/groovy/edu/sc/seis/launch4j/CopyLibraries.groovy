package edu.sc.seis.launch4j

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.plugins.JavaPlugin

class CopyLibraries {
    Project project
    FileOperations fileOperations

    CopyLibraries(Project project, FileOperations fileOperations) {
        this.project = project
        this.fileOperations = fileOperations
    }

    /**
     * Copies the project dependency jars to the configured library directory
     * @param libraryDir
     */
    void execute(File libraryDir, Object copyConfigurable) {
        def distSpec = {
            if (copyConfigurable) {
                with {
                    from { copyConfigurable }
                }
            } else if (project.plugins.hasPlugin('java')) {
                with {
                    from(project.tasks[JavaPlugin.JAR_TASK_NAME])
                    from(project.configurations.runtime)
                }
            }
            into { libraryDir }
        }

        fileOperations.sync(new Action<CopySpec>() {
            void execute(CopySpec t) {
                project.configure(t, distSpec)
            }
        })
    }
}

package org.sonatype.nexus.plugins.aether;

import java.util.List;

import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.nexus.artifact.Gav;
import org.sonatype.nexus.plugins.aether.workspace.NexusWorkspace;
import org.sonatype.nexus.proxy.maven.MavenRepository;

/**
 * High level functions provided by embedded Aether.
 * 
 * @author cstamas
 */
public interface NexusAether
{
    /**
     * Creates a nexus workspace containing the particiant MavenRepositories. These repositories will fed the Aether
     * engine with artifacts (probably causing some proxying to happen too, if needed). It may be a group or just a
     * bunch of repository (or any combination of them).
     * 
     * @param participants
     * @return
     */
    NexusWorkspace createWorkspace( List<MavenRepository> participants );

    /**
     * A shorthand method to create a Dependency from GAV and scope.
     * 
     * @param gav GAV to make Dependency, may not be {@code null}.
     * @param scope the needed scope, or {@code null}
     * @return
     */
    Dependency createDependencyFromGav( Gav gav, String scope );

    /**
     * Method collecting (and resolving if needed) the asked dependency.
     * 
     * @param nexusWorkspace
     * @param dependency
     * @param resolve
     * @return
     * @throws DependencyCollectionException
     * @throws ArtifactResolutionException
     */
    DependencyNode collectDependencies( NexusWorkspace nexusWorkspace, Dependency dependency, boolean resolve )
        throws DependencyCollectionException, ArtifactResolutionException;
}

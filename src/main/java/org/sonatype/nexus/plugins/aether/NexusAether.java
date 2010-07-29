package org.sonatype.nexus.plugins.aether;

import java.util.List;

import org.sonatype.aether.ArtifactResolutionException;
import org.sonatype.aether.DependencyCollectionException;
import org.sonatype.aether.DependencyNode;
import org.sonatype.nexus.artifact.Gav;
import org.sonatype.nexus.proxy.maven.MavenRepository;

public interface NexusAether
{
    NexusWorkspace createWorkspace( List<MavenRepository> participants );

    DependencyNode collectDependencies( NexusWorkspace nexusWorkspace, Gav gav, boolean resolve )
        throws DependencyCollectionException, ArtifactResolutionException;
}

package org.sonatype.nexus.plugins.aether;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;

public interface AetherProvider
{
    RepositorySystem getRepositorySystem();

    RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem,
                                                               NexusWorkspace nexusWorkspace );
}

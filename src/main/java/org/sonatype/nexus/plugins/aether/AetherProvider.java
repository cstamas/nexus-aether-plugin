package org.sonatype.nexus.plugins.aether;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.nexus.plugins.aether.workspace.NexusWorkspace;

/**
 * Aether provider provides prepped embedded Aether for either direct use, or for use by other "higher" level
 * components.
 * 
 * @author cstamas
 */
public interface AetherProvider
{
    RepositorySystem getRepositorySystem();

    RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem );

    RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem,
                                                               NexusWorkspace nexusWorkspace );
}

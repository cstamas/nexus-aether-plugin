package org.sonatype.nexus.plugins.aether;

import java.util.Collections;
import java.util.List;

import org.sonatype.aether.WorkspaceReader;
import org.sonatype.nexus.proxy.maven.MavenRepository;

public class NexusWorkspace
{
    private final String id;

    private final List<MavenRepository> repositories;

    public NexusWorkspace( String id, List<MavenRepository> repositories )
    {
        assert id != null && id.trim().length() > 0 : "Workspace ID cannotbe null or empty!";
        assert repositories != null : "Repository cannot be null!";

        this.id = id;
        this.repositories = repositories;
    }

    public String getId()
    {
        return id;
    }

    public List<MavenRepository> getRepositories()
    {
        return Collections.unmodifiableList( repositories );
    }

    protected WorkspaceReader getWorkspaceReader()
    {
        return new NexusWorkspaceReader( this );
    }
}

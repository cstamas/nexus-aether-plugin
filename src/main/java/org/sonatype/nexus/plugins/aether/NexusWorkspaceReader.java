package org.sonatype.nexus.plugins.aether;

import java.io.File;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.sonatype.aether.Artifact;
import org.sonatype.aether.WorkspaceReader;
import org.sonatype.aether.WorkspaceRepository;
import org.sonatype.nexus.artifact.Gav;
import org.sonatype.nexus.artifact.IllegalArtifactCoordinateException;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.maven.ArtifactStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.storage.local.fs.DefaultFSLocalRepositoryStorage;

public class NexusWorkspaceReader
    implements WorkspaceReader
{
    private final NexusWorkspace nexusWorkspace;

    private final WorkspaceRepository workspaceRepository;

    public NexusWorkspaceReader( NexusWorkspace nexusWorkspace )
    {
        this.nexusWorkspace = nexusWorkspace;

        this.workspaceRepository = new WorkspaceRepository( "nexus", nexusWorkspace.getId() );
    }

    public WorkspaceRepository getRepository()
    {
        return workspaceRepository;
    }

    public File findArtifact( Artifact artifact )
    {
        try
        {
            // fix for bug in M2GavCalculator
            final String classifier = StringUtils.isBlank( artifact.getClassifier() ) ? null : artifact.getClassifier();

            Gav gav =
                new Gav( artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), classifier,
                    artifact.getExtension(), null, null, null, false, false, null, false, null );

            ArtifactStoreRequest gavRequest;

            for ( MavenRepository mavenRepository : nexusWorkspace.getRepositories() )
            {
                gavRequest = new ArtifactStoreRequest( mavenRepository, gav, false, false );

                try
                {
                    StorageFileItem artifactFile =
                        mavenRepository.getArtifactStoreHelper().retrieveArtifact( gavRequest );

                    // this will work with local FS storage only, since Aether wants java.io.File
                    if ( artifactFile.getRepositoryItemUid().getRepository().getLocalStorage() instanceof DefaultFSLocalRepositoryStorage )
                    {
                        DefaultFSLocalRepositoryStorage ls =
                            (DefaultFSLocalRepositoryStorage) artifactFile.getRepositoryItemUid().getRepository().getLocalStorage();

                        return ls.getFileFromBase( artifactFile.getRepositoryItemUid().getRepository(), gavRequest );
                    }
                }
                catch ( Exception e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        catch ( IllegalArtifactCoordinateException e )
        {
            // will not happen or something is very wrong somewhere
        }

        // not found, return null
        return null;
    }

    public List<String> findVersions( Artifact artifact )
    {
        // TODO Auto-generated method stub
        return null;
    }

}

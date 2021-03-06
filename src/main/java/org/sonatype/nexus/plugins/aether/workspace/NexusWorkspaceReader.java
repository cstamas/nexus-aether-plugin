package org.sonatype.nexus.plugins.aether.workspace;

import java.io.File;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.WorkspaceReader;
import org.sonatype.aether.repository.WorkspaceRepository;
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

    /**
     * This method will in case of released artifact request just locate it, and return if found. In case of snapshot
     * repository, if it needs resolving, will resolve it 1st and than locate it. It will obey to the session (global
     * update policy, that correspondos to Maven CLI "-U" option.
     */
    public File findArtifact( Artifact artifact )
    {
        try
        {
            // fix for bug in M2GavCalculator
            final String classifier = StringUtils.isEmpty( artifact.getClassifier() ) ? null : artifact.getClassifier();

            Gav gav =
                new Gav( artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), classifier,
                    artifact.getExtension(), null, null, null, artifact.isSnapshot(), false, null, false, null );

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

    /**
     * Basically, this method will read the GA metadata, and return the "known versions".
     */
    public List<String> findVersions( Artifact artifact )
    {
        // TODO Auto-generated method stub
        return null;
    }

}

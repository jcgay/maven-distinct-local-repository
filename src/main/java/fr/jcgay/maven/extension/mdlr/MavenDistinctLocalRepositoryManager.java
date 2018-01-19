package fr.jcgay.maven.extension.mdlr;

import fr.jcgay.maven.extension.mdlr.aether.EnhancedLocalRepositoryManager;

import java.io.File;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.metadata.Metadata;
import org.eclipse.aether.metadata.Metadata.Nature;
import org.eclipse.aether.repository.RemoteRepository;

public class MavenDistinctLocalRepositoryManager extends EnhancedLocalRepositoryManager {

    private static final String SNAPSHOTS = "snapshots/";
    private static final String RELEASES = "releases/";

    public MavenDistinctLocalRepositoryManager(File basedir, RepositorySystemSession session) {
        super(basedir, session);
    }

    @Override
    public String getPathForLocalArtifact(Artifact artifact) {
        return getPrefixedPath(artifact, super.getPathForLocalArtifact(artifact));
    }

    @Override
    public String getPathForRemoteArtifact(Artifact artifact, RemoteRepository repository, String context) {
        return getPrefixedPath(artifact, super.getPathForRemoteArtifact(artifact, repository, context));
    }

    @Override
    public String getPathForLocalMetadata(Metadata metadata) {
        return getPrefixedPath(metadata, super.getPathForLocalMetadata(metadata));
    }

    @Override
    public String getPathForRemoteMetadata(Metadata metadata, RemoteRepository repository, String context) {
        return getPrefixedPath(metadata, super.getPathForRemoteMetadata(metadata, repository, context));
    }

    private static String getPrefixedPath(Artifact artifact, String result) {
        if (artifact.isSnapshot()) {
            return SNAPSHOTS + result;
        }
        return RELEASES + result;
    }

    private static String getPrefixedPath(Metadata metadata, String result) {
        if (metadata.getNature() == Nature.SNAPSHOT) {
            return SNAPSHOTS + result;
        }
        if (metadata.getNature() == Nature.RELEASE) {
            return RELEASES + result;
        }
        return result;
    }
}

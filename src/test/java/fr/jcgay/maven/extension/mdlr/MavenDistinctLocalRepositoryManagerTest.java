package fr.jcgay.maven.extension.mdlr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.metadata.Metadata;
import org.eclipse.aether.metadata.Metadata.Nature;
import org.eclipse.aether.repository.RemoteRepositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class MavenDistinctLocalRepositoryManagerTest {

   Path tempDirectory;
   MavenDistinctLocalRepositoryManager manager;

   @Mock
   RepositorySystemSession session;

   @BeforeEach
   void setUp() throws IOException {
      initMocks(this);
      tempDirectory = Files.createTempDirectory("distinct-local-repository");
      manager = new MavenDistinctLocalRepositoryManager(tempDirectory.toFile(), session);
   }

   @Test
   void snapshots_are_resolved_in_a_snapshots_directory() {
      assertThat(manager.getPathForLocalArtifact(snapshotArtifact("a", "b", "1.0-SNAPSHOT")))
            .startsWith("snapshots/a/b");
      assertThat(manager.getPathForRemoteArtifact(snapshotArtifact("a", "b", "1.0-SNAPSHOT"), RemoteRepositories.any(), ""))
            .startsWith("snapshots/a/b");
      assertThat(manager.getPathForLocalMetadata(snapshotMetadata("a", "b", "1.0-SNAPSHOT")))
            .startsWith("snapshots/a/b");
      assertThat(manager.getPathForRemoteMetadata(snapshotMetadata("a", "b", "1.0-SNAPSHOT"), RemoteRepositories.any(), ""))
            .startsWith("snapshots/a/b");
      assertThat(manager.getPathForArtifact(snapshotArtifact("a", "b", "1.0-SNAPSHOT"), true))
            .startsWith("snapshots/a/b");
      assertThat(manager.getPathForArtifact(snapshotArtifact("a", "b", "1.0-SNAPSHOT"), false))
            .startsWith("snapshots/a/b");
   }

   @Test
   void releases_are_resolved_in_a_releases_directory() {
      assertThat(manager.getPathForLocalArtifact(releaseArtifact("a", "b", "1.0-SNAPSHOT")))
            .startsWith("releases/a/b");
      assertThat(manager.getPathForRemoteArtifact(releaseArtifact("a", "b", "1.0-SNAPSHOT"), RemoteRepositories.any(), ""))
            .startsWith("releases/a/b");
      assertThat(manager.getPathForLocalMetadata(releaseMetadata("a", "b", "1.0-SNAPSHOT")))
            .startsWith("releases/a/b");
      assertThat(manager.getPathForRemoteMetadata(releaseMetadata("a", "b", "1.0-SNAPSHOT"), RemoteRepositories.any(), ""))
            .startsWith("releases/a/b");
      assertThat(manager.getPathForArtifact(releaseArtifact("a", "b", "1.0-SNAPSHOT"), true))
            .startsWith("releases/a/b");
      assertThat(manager.getPathForArtifact(releaseArtifact("a", "b", "1.0-SNAPSHOT"), false))
            .startsWith("releases/a/b");
   }

    @Test
    void resolve_metadata_in_the_default_directory_when_nature_is_ambiguous() {
        Metadata metadata = aMetadata("a", "b", "1.0-SNAPSHOT");
        when(metadata.getNature()).thenReturn(Nature.RELEASE_OR_SNAPSHOT);

        assertThat(manager.getPathForLocalMetadata(metadata))
            .doesNotStartWith("snapshots/")
            .doesNotStartWith("releases/");
        assertThat(manager.getPathForRemoteMetadata(metadata, RemoteRepositories.any(), ""))
            .doesNotStartWith("snapshots/")
            .doesNotStartWith("releases/");
    }

    private static Artifact anArtifact(String groupId, String artifactId, String version) {
      Artifact artifact = mock(Artifact.class);
      when(artifact.getGroupId()).thenReturn(groupId);
      when(artifact.getArtifactId()).thenReturn(artifactId);
      when(artifact.getBaseVersion()).thenReturn(version);
      when(artifact.getClassifier()).thenReturn("");
      when(artifact.getExtension()).thenReturn("jar");
      return artifact;
   }

   private static Artifact snapshotArtifact(String groupId, String artifactId, String version) {
      Artifact artifact = anArtifact(groupId, artifactId, version);
      when(artifact.isSnapshot()).thenReturn(true);
      return artifact;
   }

   private static Artifact releaseArtifact(String groupId, String artifactId, String version) {
      Artifact artifact = anArtifact(groupId, artifactId, version);
      when(artifact.isSnapshot()).thenReturn(false);
      return artifact;
   }

   private Metadata snapshotMetadata(String groupId, String artifactId, String version) {
      Metadata metadata = aMetadata(groupId, artifactId, version);
      when(metadata.getNature()).thenReturn(Nature.SNAPSHOT);
      return metadata;
   }

   private Metadata aMetadata(String groupId, String artifactId, String version) {
      Metadata metadata = mock(Metadata.class);
      when(metadata.getGroupId()).thenReturn(groupId);
      when(metadata.getArtifactId()).thenReturn(artifactId);
      when(metadata.getVersion()).thenReturn(version);
      when(metadata.getType()).thenReturn("jar");
      when(metadata.getFile()).thenReturn(tempDirectory.resolve(Paths.get(artifactId + ".jar")).toFile());
      return metadata;
   }

   private Metadata releaseMetadata(String groupId, String artifactId, String version) {
      Metadata metadata = aMetadata(groupId, artifactId, version);
      when(metadata.getNature()).thenReturn(Nature.RELEASE);
      return metadata;
   }
}

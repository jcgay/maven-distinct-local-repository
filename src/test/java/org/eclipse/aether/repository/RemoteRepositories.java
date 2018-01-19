package org.eclipse.aether.repository;

public class RemoteRepositories {

   public static RemoteRepository any() {
      return new RemoteRepository(new RemoteRepository.Builder("id", "default", "http://fake.repository"));
   }
}

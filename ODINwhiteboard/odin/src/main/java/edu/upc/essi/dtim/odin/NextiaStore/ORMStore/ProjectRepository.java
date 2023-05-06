package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import edu.upc.essi.dtim.odin.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
}

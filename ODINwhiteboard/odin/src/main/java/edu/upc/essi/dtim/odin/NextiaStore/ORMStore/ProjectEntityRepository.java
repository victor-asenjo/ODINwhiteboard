package edu.upc.essi.dtim.odin.NextiaStore.ORMStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface ProjectEntityRepository extends JpaRepository<ProjectEntity, String> {
}

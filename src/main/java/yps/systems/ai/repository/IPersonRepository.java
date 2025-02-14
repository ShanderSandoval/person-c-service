package yps.systems.ai.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Person;

@Repository
public interface IPersonRepository extends Neo4jRepository<Person, String> {
}

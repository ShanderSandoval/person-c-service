package yps.systems.ai.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class Person {

    @Id
    @GeneratedValue
    private String elementId;

    @Property("firstName")
    private String firstName;

    @Property("lastName")
    private String lastName;

    @Property("identification")
    private String identification;

    @Property("email")
    private String email;

    @Property("phone")
    private String phone;

}

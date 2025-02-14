package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Person;
import yps.systems.ai.repository.IPersonRepository;

import java.util.Optional;

@RestController
@RequestMapping("/command/personService")
public class PersonCommandController {

    private final IPersonRepository personRepository;
    private final KafkaTemplate<String, Person> kafkaTemplate;

    @Value("${env.kafka.topicEvent}")
    private String kafkaTopicEvent;

    @Autowired
    public PersonCommandController(IPersonRepository personRepository, KafkaTemplate<String, Person> kafkaTemplate) {
        this.personRepository = personRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> savePersonNode(@RequestBody Person person) {
        Person savedPerson = personRepository.save(person);
        Message<Person> message = MessageBuilder
                .withPayload(savedPerson)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "CREATE_PERSON")
                .setHeader("source", "personService")
                .build();
        kafkaTemplate.send(message);
        return new ResponseEntity<>("Person saved with ID: " + savedPerson.getElementId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{elementId}")
    public ResponseEntity<String> deletePersonNode(@PathVariable String elementId) {
        Optional<Person> personNodeOptional = personRepository.findById(elementId);
        if (personNodeOptional.isPresent()) {
            personRepository.deleteById(elementId);
            Message<String> message = MessageBuilder
                    .withPayload(elementId)
                    .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                    .setHeader("eventType", "DELETE_PERSON")
                    .setHeader("source", "personService")
                    .build();
            kafkaTemplate.send(message);
            return new ResponseEntity<>("Person deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Person not founded", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{elementId}")
    public ResponseEntity<String> updatePersonNode(@PathVariable String elementId, @RequestBody Person person) {
        Optional<Person> personNodeFounded = personRepository.findById(elementId);
        if (personNodeFounded.isPresent()) {
            person.setElementId(personNodeFounded.get().getElementId());
            personRepository.save(person);
            Message<Person> message = MessageBuilder
                    .withPayload(person)
                    .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                    .setHeader("eventType", "UPDATE_PERSON")
                    .setHeader("source", "personService")
                    .build();
            kafkaTemplate.send(message);
            return new ResponseEntity<>("Person updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Person not founded", HttpStatus.NOT_FOUND);
        }
    }

}

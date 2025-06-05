package at.fhv;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Term Service",
                version = "0.1.0",
                description = """
                        The TermService is responsible for managing individual terms within events of a holiday program.
                        
                        Main use cases include:
                        - Creating, reading, updating and deleting terms (CRUD)
                        - Assigning and unassigning caregivers to terms
                        - Retrieving terms by ID or by event

                        Naming conventions:
                        - All endpoints follow the pattern: /terms/{action}
                        - Resource IDs are passed via path parameters
                        - JSON fields use camelCase (e.g., termId, eventId, startTime)
                        - Date and time values use this format: "2025-07-10T14:30:00"

                        Example endpoints:
                        - POST /terms/create                      -> Create a new term
                        - GET /terms/{termId}                     -> Get a term by ID
                        - PUT /terms/update/{termId}              -> Update an existing term
                        - DELETE /terms/delete/{termId}           -> Delete a term
                        - POST /terms/assign-caregiver/{termId}/{caregiverId}     -> Assign a caregiver
                        - POST /terms/unassign-caregiver/{termId}/{caregiverId}   -> Unassign a caregiver
                    """,
                contact = @Contact(
                        name = "Max Mustermann",
                        email = "max@example.com"
                )
        )
)

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
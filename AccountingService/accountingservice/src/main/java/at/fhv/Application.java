package at.fhv;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Accounting Service",
                version = "0.1.0",
                description = """                
                        The AccountingService provides endpoints to manage payments and expenses related to a holiday program.
                        
                        Main use cases include:
                        - Creating and querying payments for users and sponsors
                        - Tracking payment status (pending, received, paid)
                        - Managing expenses for specific terms

                        Naming conventions:
                        - All endpoints follow the pattern: /accounting/{resource}/{action}
                        - JSON fields use camelCase (e.g., userId, paymentDeadline)
                        - IDs are STRINGS

                        Example endpoints:
                        - GET /accounting/payments/123          -> Get all payments for term 123
                        - POST /accounting/payment/create       -> Create a user payment
                        - POST /accounting/expense/create       -> Create a new expense
                        - GET /accounting/sponsor/payments      -> List sponsor payments
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
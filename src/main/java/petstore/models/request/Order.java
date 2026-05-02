package petstore.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO Petstore Order-ისთვის.
 * გამოიყენება როგორც POST request body, ასევე response deserialization-ისთვის.
 *
 * @JsonInclude(NON_NULL) — null fields არ გაიგზავნება request-ში.
 * ეს მნიშვნელოვანია ნეგატიური ტესტებისთვის, სადაც მხოლოდ კონკრეტული ველი გვინდა.
 */
@Data                              // Lombok: ავტომატური getters, setters, toString, equals
@Builder                           // Lombok: builder pattern (Order.builder().id(1L).build())
@NoArgsConstructor                 // Lombok: ცარიელი კონსტრუქტორი — Jackson-ისთვის
@AllArgsConstructor                // Lombok: კონსტრუქტორი ყველა ველით
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    private Long id;               // შეკვეთის ID
    private Long petId;            // ცხოველის ID, რომელსაც ვეკვეთავთ
    private Integer quantity;      // რაოდენობა
    private String shipDate;       // მიწოდების თარიღი (ISO 8601 format)
    private String status;         // "placed", "approved", "delivered"
    private Boolean complete;      // შეკვეთა დასრულებულია თუ არა
}
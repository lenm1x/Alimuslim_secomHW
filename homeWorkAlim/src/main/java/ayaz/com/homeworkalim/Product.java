package ayaz.com.homeworkalim;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Product(
        int id,
        String name,
        double price,
        int quantity,
        double totalPrice,
        LocalDate date
) {

}

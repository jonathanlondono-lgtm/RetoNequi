package co.com.nequi.model.product;
import co.com.nequi.model.product.command.CreateProductCommand;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private Long id;
    private String name;
    private Integer stock;
    private Long branchId;

    public static Product fromCommand(CreateProductCommand command) {
        return Product.builder()
                .name(command.name())
                .branchId(command.branchId())
                .stock(0)
                .build();
    }
}

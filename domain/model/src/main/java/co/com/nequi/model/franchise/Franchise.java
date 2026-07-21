package co.com.nequi.model.franchise;
import co.com.nequi.model.franchise.command.CreateFranchiseCommand;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Franchise {
    private Long id;
    private String name;

    public static Franchise fromCommand(CreateFranchiseCommand command) {
        return Franchise.builder()
                .name(command.name())
                .build();
    }
}

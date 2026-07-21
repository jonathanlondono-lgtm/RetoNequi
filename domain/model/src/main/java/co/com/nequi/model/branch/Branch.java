package co.com.nequi.model.branch;
import co.com.nequi.model.branch.command.CreateBranchCommand;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {
    private Long id;
    private String name;
    private Long franchiseId;

    public static Branch fromCommand(CreateBranchCommand command) {
        return Branch.builder()
                .name(command.name())
                .franchiseId(command.franchiseId())
                .build();
    }
}

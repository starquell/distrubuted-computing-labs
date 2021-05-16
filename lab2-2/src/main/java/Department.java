import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Department {
    private long id;
    private String name;
    List<Employee> employees = new ArrayList<Employee>();
}

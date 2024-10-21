package projetopi.projetopi.dto.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class BucketFirebase {
    private String name;
    private String key;

    public BucketFirebase(String name, String key) {
        this.name = name;
        this.key = key;
    }
}

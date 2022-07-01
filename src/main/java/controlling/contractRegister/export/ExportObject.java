package controlling.contractRegister.export;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public abstract class ExportObject<T> {

    private final List<T> objects;

    public abstract Class<T> getObjectClass();
}
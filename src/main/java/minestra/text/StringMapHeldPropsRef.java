package minestra.text;

import java.util.Map;
import java.util.Optional;

final class StringMapHeldPropsRef implements PropsRef {

    private final Map<String, String> props;

    StringMapHeldPropsRef(Map<String, String> props) {
        this.props = props;
    }

    @Override
    public Optional<String> stringOpt(String key) {
        return Optional.ofNullable(props.get(key));
    }

}

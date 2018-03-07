package minestra.text;

import java.util.Optional;
import java.util.Properties;

final class PropertiesHeldPropsRef implements PropsRef {

    private final Properties props;

    PropertiesHeldPropsRef(Properties props) {
        this.props = props;
    }

    @Override
    public Optional<String> stringOpt(String key) {
        return Optional.ofNullable(props.getProperty(key));
    }

}

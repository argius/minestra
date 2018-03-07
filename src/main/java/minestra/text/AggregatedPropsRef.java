package minestra.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Aggregated objects of PropsRefs.
 */
final class AggregatedPropsRef implements PropsRef {

    private final List<PropsRef> refList;

    AggregatedPropsRef(PropsRef... refs) {
        this(Arrays.asList(refs));
    }

    AggregatedPropsRef(Collection<PropsRef> refs) {
        this.refList = new ArrayList<>(refs);
    }

    @Override
    public Optional<String> stringOpt(String key) {
        return refList.stream().flatMap(x -> x.stringOpt(key).map(Stream::of).orElseGet(Stream::empty)).findFirst();
    }

    // TODO override integerOpt to ignore NumberFormatException

}

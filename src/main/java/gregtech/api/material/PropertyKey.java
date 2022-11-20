package gregtech.api.material;

public class PropertyKey<T extends IMaterialProperty> {

    private final String key;
    private final Class<T> type;

    public PropertyKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    public T constructDefault() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T cast(IMaterialProperty<?> property) {
        return this.type.cast(property);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PropertyKey) {
            return ((PropertyKey<?>) o).key.equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key;
    }
}

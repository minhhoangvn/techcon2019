package katalon.plugin.internal;

import com.katalon.platform.api.console.PluginConsoleOption;

public abstract class AbstractConsoleOption<T> implements PluginConsoleOption<T> {

    protected T value;

    @Override
    public boolean hasArgument() {
        return true;
    }

    @Override
    public String getDefaultArgumentValue() {
        return null;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    public T getValue() {
        return value;
    }

    @Override
    public void setValue(String rawValue) {
    }
}
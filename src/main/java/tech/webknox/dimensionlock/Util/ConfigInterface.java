package tech.webknox.dimensionlock.Util;

public interface ConfigInterface {
    void ReloadConfig();

    boolean isEndEnabled();

    void setEndEnabled(boolean endEnabled);

    boolean isNetherEnabled();

    void setNetherEnabled(boolean netherEnabled);

    boolean isEyeSpoofEnabled();

    void setEyeSpoofEnabled(boolean eyeSpoofEnabled);

    boolean isOpBypassEnabled();

    void setOpBypassEnabled(boolean opBypassEnabled);

    Coordinates getSpoofLocation();

    void setSpoofLocation(Coordinates spoofLocation);
}

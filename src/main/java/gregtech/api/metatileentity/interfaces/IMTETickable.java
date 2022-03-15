package gregtech.api.metatileentity.interfaces;

public interface IMTETickable {

    /**
     * The First processed Tick which was passed to this MetaTileEntity. This will get called when block was placed as well as on world load
     */
    void onFirstTick();

    void update();

    void onTickFailed(boolean isServerSide);
}

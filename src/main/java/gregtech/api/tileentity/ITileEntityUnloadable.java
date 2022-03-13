package gregtech.api.tileentity;

public interface ITileEntityUnloadable {
    /**
     * Checks if the TileEntity is Invalid or Unloaded. Minecraft cannot do that for an Unloaded Chunk.
     * Implementing this Function properly is very important, and should be required for every TileEntity.
     * That is why I made it a separate Interface and forced it as super Interface to all my other Interfaces.
     * <p>
     * To do it properly just add a true boolean Flag to your Member Variables ("mIsDead=false" for example) and set it to true when "onChunkUnload" is called. Then return the following:
     *
     * @return mIsDead || isInvalid()
     */
    boolean isDead();
}

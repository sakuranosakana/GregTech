package gregtech.apiOld.gui.resources.picturetexture;

import gregtech.apiOld.gui.resources.utils.ProcessedImageData;

public class OrdinaryTexture extends PictureTexture {

    private final int textureID;

    public OrdinaryTexture(ProcessedImageData image) {
        super(image.getWidth(), image.getHeight());
        textureID = image.uploadFrame(0);
    }

    @Override
    public void tick() {
    }

    @Override
    public int getTextureID() {
        return textureID;
    }
}


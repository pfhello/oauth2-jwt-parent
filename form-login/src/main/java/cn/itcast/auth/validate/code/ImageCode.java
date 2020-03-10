package cn.itcast.auth.validate.code;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class ImageCode extends ValidateCode{

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireTime) {
        super(code,expireTime);
        this.image = image;
    }

}

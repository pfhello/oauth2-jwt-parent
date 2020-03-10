package cn.itcast.auth.validate.code;

import cn.itcast.auth.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private SmsCodeSender defaultSmsCodeSender;

    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建验证码
        ImageCode imageCode= (ImageCode) imageCodeGenerator.generate(request);
        //保存到session,分布式保存在redis中
        request.getSession().setAttribute(CodeKey.IMAGE_CODE,imageCode);
        //将图片响应到前端
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request,String mobile){
        ValidateCode smsCode = smsCodeGenerator.generate(request);
        request.getSession().setAttribute(CodeKey.SMS_CODE,smsCode);
        defaultSmsCodeSender.send(mobile,smsCode.getCode());
    }


}

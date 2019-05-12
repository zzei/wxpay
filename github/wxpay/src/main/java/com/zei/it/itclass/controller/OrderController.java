package com.zei.it.itclass.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zei.it.itclass.domain.Order;
import com.zei.it.itclass.domain.ResponseBean;
import com.zei.it.itclass.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("add")
    public void saveOrder(@RequestParam(value = "articleId",required = true) int articleId,
                                  int userId, HttpServletRequest request, HttpServletResponse response) throws Exception{

        String ip = IpUtils.getIpAddr(request);

        Order order = new Order();
        order.setArticleId(articleId);
        order.setUserId(userId);
        order.setIp(ip);

        String codeUrl = orderService.save(order);
        if(StringUtils.isEmpty(codeUrl)){
            throw new NullPointerException();
        }

        try {
            //生成二维码
            //设置纠错等级
            Map<EncodeHintType, Object> hits = new HashMap<>();
            hits.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hits.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            //设置二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 300, 300, hits);
            //输出
            OutputStream outputStream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

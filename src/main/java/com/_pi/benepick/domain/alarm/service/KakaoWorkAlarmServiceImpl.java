package com._pi.benepick.domain.alarm.service;

import com._pi.benepick.domain.alarm.message.HeaderBlock;
import com._pi.benepick.domain.alarm.message.ImageBlock;
import com._pi.benepick.domain.alarm.message.Message;
import com._pi.benepick.domain.alarm.message.TextBlock;
import com._pi.benepick.domain.alarm.message.ButtonBlock;
import com._pi.benepick.domain.alarm.message.Action;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoWorkAlarmServiceImpl implements AlarmService {

    @Value("${kakao.work.token}")
    private String kakaoWorkToken;

    /**
     * sendMessage(getCongratulationsMessage(email, name)); 의 형태로 사용한다
     * @param jsonMessage :String getCongratulationsMessage(email, name) 메소드 리턴값
     * @return 카카오워크 메시지 응답 결과
     */
    // 수신인, 내용, 주소
    public Object sendAlarm(final String jsonMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(kakaoWorkToken);

        HttpEntity<String> requestAPI = new HttpEntity<>(jsonMessage, headers);
        String url = "https://api.kakaowork.com/v1/messages.send_by_email";

        return restTemplate.exchange(url, HttpMethod.POST, requestAPI, Object.class).getBody();
    }

    /**
     * 이메일, 사용자의 이름을 넣어 당첨 축하 메시지를 만들어 반환한다.
     * @param email: String
     * @param name: String
     * @return  json 형태의 메시지
     */
    private String getCongratulationsMessage(final String email, final String name) {
        ObjectMapper mapper = new ObjectMapper();
        Message message = new Message(email, "당첨 축하 메시지입니다.");

        HeaderBlock headerBlock = new HeaderBlock("header", "당첨 축하드립니다!", "white");
        TextBlock textBlock = new TextBlock("text", name + "님의 당첨을 축하드립니다!\n지금 당장 확인하러 가보세요~");
        ImageBlock imageBlock = new ImageBlock("image_link", "https://t1.kakaocdn.net/kakaowork/resources/block-kit/imagelink/image5@3x.jpg");

        ButtonBlock buttonBlock = new ButtonBlock("button", "확인하러 가기", "default");
        Action action = new Action("open_system_browser", "당첨_확인_button", "https://dktechin.com");
        buttonBlock.setAction(action);
        message.setBlocks(List.of(headerBlock, textBlock, imageBlock, buttonBlock));
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


package com._pi.benepick.domain.alarm.service;

import com._pi.benepick.domain.alarm.domain.Message;
import com._pi.benepick.domain.alarm.domain.MessageType;
import com._pi.benepick.domain.alarm.messageObject.*;
import com._pi.benepick.domain.alarm.repository.MessageRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(KakaoWorkAlarmServiceImpl.class);
    @Value("${kakao-work.token}")
    private String kakaoWorkToken;

    private final MessageRepository messageRepository;

    /**
     * sendMessage(getCongratulationsMessage(email, name)); 의 형태로 사용한다
     * @param jsonMessage :String getCongratulationsMessage(email, name) 메소드 리턴값
     * @return 카카오워크 메시지 응답 결과
     */
    public Object sendAlarm(final String jsonMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(kakaoWorkToken);

        HttpEntity<String> requestAPI = new HttpEntity<>(jsonMessage, headers);
        String url = "https://api.kakaowork.com/v1/messages.send_by_email";

        return restTemplate.exchange(url, HttpMethod.POST, requestAPI, Object.class).getBody();
    }

    public String getMessageFactory(final Members members, final String url, final MessageType type) {
        if (type.equals(MessageType.ADDITIONAL)) {
            return getAdditionalCongratulationsMessage(members.getId(), members.getName(), url);
        }
        return getCongratulationsMessage(members.getId(), members.getName(), url);
    }

    /**
     * 이메일, 사용자의 이름을 넣어 당첨 축하 메시지를 만들어 반환한다.
     * @param email: String
     * @param name: String
     * @return  json 형태의 메시지
     */
    public String getCongratulationsMessage(final String email, final String name, final String url) {
        ObjectMapper mapper = new ObjectMapper();
        MessageContent messageContent = new MessageContent(email, "당첨 축하 메시지입니다.");

        HeaderBlock headerBlock = new HeaderBlock("header", "당첨 축하드립니다!", "white");
        TextBlock textBlock = new TextBlock("text", name + "님의 당첨을 축하드립니다!\n지금 당장 확인하러 가보세요");
        ImageBlock imageBlock = new ImageBlock("image_link", "https://t1.kakaocdn.net/kakaowork/resources/block-kit/imagelink/image5@3x.jpg");

        ButtonBlock buttonBlock = setButtonBlock(url);
        messageContent.setBlocks(List.of(headerBlock, textBlock, imageBlock, buttonBlock));
        try {
            return mapper.writeValueAsString(messageContent);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 이메일, 사용자의 이름을 넣어 추가 당첨 축하 메시지를 만들어 반환한다.
     * @param email: String
     * @param name: String
     * @return  json 형태의 메시지
     */
    public String getAdditionalCongratulationsMessage(final String email, final String name, final String url) {
        ObjectMapper mapper = new ObjectMapper();
        MessageContent messageContent = new MessageContent(email, "추가 당첨 축하 메시지입니다.");

        HeaderBlock headerBlock = new HeaderBlock("header", "추가로 당첨되신 것을 축하드립니다!", "white");
        TextBlock textBlock = new TextBlock("text", name + "님의 추가 당첨을 축하드립니다!\n지금 당장 확인하러 가보세요");
        ImageBlock imageBlock = new ImageBlock("image_link", "https://t1.kakaocdn.net/kakaowork/resources/block-kit/imagelink/image1@3x.jpg");

        ButtonBlock buttonBlock = setButtonBlock(url);
        messageContent.setBlocks(List.of(headerBlock, textBlock, imageBlock, buttonBlock));
        try {
            return mapper.writeValueAsString(messageContent);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    private ButtonBlock setButtonBlock(String url) {
        ButtonBlock buttonBlock = new ButtonBlock("button", "확인하러 가기", "default");
        Action action = new Action("open_system_browser", "당첨_확인_button", url);
        buttonBlock.setAction(action);
        return buttonBlock;
    }

    @Override
    public void sendAlarmStart() {
        List<Message> messages = messageRepository.findAll();
        for (Message message : messages) {
            sendAlarm(message.getContents());
            log.info("send alarm to {}", message.getEmail());
            messageRepository.delete(message);
        }
    }

    @Override
    public void saveMessage(String email, String name, String contents) {
        log.info("save message to {}", email);
        messageRepository.save(Message.of(email, name, contents));
    }
}


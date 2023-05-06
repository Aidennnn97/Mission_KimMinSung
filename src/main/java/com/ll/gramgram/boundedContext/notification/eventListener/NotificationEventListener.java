package com.ll.gramgram.boundedContext.notification.eventListener;

import com.ll.gramgram.base.event.EventAfterLike;
import com.ll.gramgram.base.event.EventAfterModifyAttractiveType;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class NotificationEventListener {
    private final InstaMemberService instaMemberService;

    @EventListener
    public void listen(EventAfterModifyAttractiveType event) {
        // 누군가의 호감사유가 변경되었을 경우
    }

    @EventListener
    public void listen(EventAfterLike event) {
        // 누군가가 호감표시를 한 경우
    }

}

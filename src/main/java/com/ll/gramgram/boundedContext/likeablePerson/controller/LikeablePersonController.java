package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/likeablePerson")
@RequiredArgsConstructor
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String showAdd() {
        return "usr/likeablePerson/add";
    }

    @AllArgsConstructor
    @Getter
    public static class AddForm {
        private final String username;
        private final int attractiveTypeCode;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String add(@Valid AddForm addForm) {
        // 내가 좋아하는 사람 리스트
        List<LikeablePerson> fromLikeablePeople = rq.getMember().getInstaMember().getFromLikeablePeople();

        for(LikeablePerson person : fromLikeablePeople){    // 내가 좋아하는 사람 리스트 중에서
            if (person.getToInstaMemberUsername().equals(addForm.getUsername())){   // 현재 호감표시하고자 하는 상대방의 인스타그램 아이디와 같은게 있다면
                return rq.historyBack("이미 등록된 호감상대 입니다.");  // rq.historyBack();
            }
        }

        if (fromLikeablePeople.size() >= 10){
            return rq.historyBack("11명 이상 등록할 수 없습니다.");
        }

        RsData<LikeablePerson> createRsData = likeablePersonService.like(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        if (createRsData.isFail()) {
            return rq.historyBack(createRsData);
        }

        return rq.redirectWithMsg("/likeablePerson/list", createRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(Model model) {
        InstaMember instaMember = rq.getMember().getInstaMember();

        // 인스타인증을 했는지 체크
        if (instaMember != null) {
            // 해당 인스타회원이 좋아하는 사람 목록
//            List<LikeablePerson> likeablePeople = likeablePersonService.findByFromInstaMemberId(instaMember.getId());
            List<LikeablePerson> likeablePeople = instaMember.getFromLikeablePeople();
            model.addAttribute("likeablePeople", likeablePeople);
        }

        return "usr/likeablePerson/list";
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElse(null);

        RsData canMemberDeleteRsData = likeablePersonService.canMemberDelete(rq.getMember(), likeablePerson);

        if (canMemberDeleteRsData.isFail()){
            return rq.historyBack(canMemberDeleteRsData);
        }

        RsData deleteRsDate = likeablePersonService.delete(likeablePerson);

        if (deleteRsDate.isFail()) {
            return rq.historyBack(deleteRsDate);
        }

        return rq.redirectWithMsg("/likeablePerson/list", deleteRsDate);
    }

}

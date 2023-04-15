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
        RsData canLikeRsData = likeablePersonService.canLike(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        if (canLikeRsData.isFail()){
            return rq.historyBack(canLikeRsData);
        }

        RsData rsData = null;
        // 호감상대를 등록할 수 있으면
        if (canLikeRsData.getResultCode().equals("S-2")){
            rsData = likeablePersonService.modifyAttractive(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());
        } else {
            rsData = likeablePersonService.like(rq.getMember(), addForm.getUsername(), addForm.getAttractiveTypeCode());

        }

        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/likeablePerson/list", rsData);
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

        RsData canDeleteRsData = likeablePersonService.canDelete(rq.getMember(), likeablePerson);

        if (canDeleteRsData.isFail()){
            return rq.historyBack(canDeleteRsData);
        }

        RsData deleteRsDate = likeablePersonService.delete(likeablePerson);

        if (deleteRsDate.isFail()) {
            return rq.historyBack(deleteRsDate);
        }

        return rq.redirectWithMsg("/likeablePerson/list", deleteRsDate);
    }

}

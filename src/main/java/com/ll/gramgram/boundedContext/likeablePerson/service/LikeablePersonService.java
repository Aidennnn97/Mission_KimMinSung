package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    public RsData canLike(Member member, String username, int attractiveTypeCode){
        if (!member.hasConnectedInstaMember()) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }
        if (member.getInstaMember().getUsername().equals(username)){
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }
        // 내가 좋아하는 사람 리스트
        List<LikeablePerson> fromLikeablePeople = member.getInstaMember().getFromLikeablePeople();
        // 내가 좋아하는 사람 리스트 중에서 입력한 호감대상 아이디와 일치하는 아이디 찾기
        LikeablePerson fromLikeablePerson = fromLikeablePeople
                .stream()
                .filter(e -> e.getToInstaMember().getUsername().equals(username))
                .findFirst()
                .orElse(null);
        // 입력한 호감대상 아이디와 일치하는게 있고, 호감사유도 같으면
        if (fromLikeablePerson != null && fromLikeablePerson.getAttractiveTypeCode() == attractiveTypeCode){
            return RsData.of("F-3", "이미 %s님에 대해서 호감표시를 했습니다.".formatted(username));
        }
        // 설정파일에서 값 가져오기
        long likeablePersonFromMax = AppConfig.getLikeablePersonFromMax();

        if (fromLikeablePeople.size() >= likeablePersonFromMax){
            return RsData.of("F-4", "최대 %d명에 대해서만 호감표시가 가능합니다.".formatted(likeablePersonFromMax));
        }

        return RsData.of("S-2", "%s님에 대해서 호감표시가 가능합니다.".formatted(username));
    }

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감상대가 생겼어
        member.getInstaMember().addFromLikeablePerson(likeablePerson);
        // 너를 좋아하는 호감상대가 생겼어
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public Optional<LikeablePerson> findById(Long id){
        return likeablePersonRepository.findById(id);
    }

    @Transactional
    public RsData delete(LikeablePerson likeablePerson) {
        likeablePersonRepository.delete(likeablePerson);    // 삭제
        return RsData.of("S-1", "인스타유저(%s)를 호감상대목록에서 삭제하였습니다".formatted(likeablePerson.getToInstaMember().getUsername()));
    }

    public RsData canDelete(Member member, LikeablePerson likeablePerson) {
        if (likeablePerson == null){
            return RsData.of("F-1", "이미 삭제되었습니다.");
        }

        // 현제 사용자의 인스타계정 번호
        long memberInstaMemberId = member.getInstaMember().getId();
        // 삭제 대상을 등록한 유저(호감표시한 사람)의 인스타계정 번호
        long fromInstaMemberId = likeablePerson.getFromInstaMember().getId();

        if(memberInstaMemberId != fromInstaMemberId){
            return RsData.of("F-2", "권한이 없습니다.");
        }

        return RsData.of("S-1", "삭제가 가능합니다.");
    }

    @Transactional
    public RsData modifyAttractive(Member member, String username, int attractiveTypeCode) {
        // 내가 좋아하는 사람 리스트
        List<LikeablePerson> fromLikeablePeople = member.getInstaMember().getFromLikeablePeople();
        // 내가 좋아하는 사람 리스트 중에서 입력한 호감대상 아이디와 일치하는 아이디 찾기
        LikeablePerson fromLikeablePerson = fromLikeablePeople
                .stream()
                .filter(e -> e.getToInstaMember().getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (fromLikeablePerson == null){
            return RsData.of("F-7", "호감표시를 하지 않았습니다.");
        }

        String beforeAttractiveTypeDisplayName = fromLikeablePerson.getAttractiveTypeDisplayName();

        fromLikeablePerson.setAttractiveTypeCode(attractiveTypeCode);
        likeablePersonRepository.save(fromLikeablePerson);

        return RsData.of("S-3", "%s에 대한 호감사유를 %s에서 %s(으)로 변경합니다.".formatted(username, beforeAttractiveTypeDisplayName, fromLikeablePerson.getAttractiveTypeDisplayName()));
    }

    public RsData canModifyAttractive(Member member, LikeablePerson likeablePerson) {
        // 현제 사용자의 인스타계정 번호
        long memberInstaMemberId = member.getInstaMember().getId();
        // 등록(수정) 대상을 등록한 유저(호감표시한 사람)의 인스타계정 번호
        long fromInstaMemberId = likeablePerson.getFromInstaMember().getId();

        if(memberInstaMemberId != fromInstaMemberId){
            return RsData.of("F-2", "권한이 없습니다.");
        }

        return RsData.of("S-1", "수정이 가능합니다.");
    }
}

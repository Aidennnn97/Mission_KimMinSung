package com.ll.gramgram.boundedContext.instaMember.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInstaMember is a Querydsl query type for InstaMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInstaMember extends EntityPathBase<InstaMember> {

    private static final long serialVersionUID = -53334208L;

    public static final QInstaMember instaMember = new QInstaMember("instaMember");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final ListPath<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson> fromLikeablePeople = this.<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson>createList("fromLikeablePeople", com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson.class, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.class, PathInits.DIRECT2);

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final ListPath<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson> toLikeablePeople = this.<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson>createList("toLikeablePeople", com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson.class, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QInstaMember(String variable) {
        super(InstaMember.class, forVariable(variable));
    }

    public QInstaMember(Path<? extends InstaMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInstaMember(PathMetadata metadata) {
        super(InstaMember.class, metadata);
    }

}


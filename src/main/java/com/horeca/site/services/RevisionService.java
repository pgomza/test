package com.horeca.site.services;

import com.horeca.site.models.CustomRevisionEntity;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RevisionService {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Timestamp> getCreatedAt(Class<?> entityType, Object entityId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(entityType, false, false)
                .add(AuditEntity.id().eq(entityId))
                .add(AuditEntity.revisionType().in(new RevisionType[]{ RevisionType.ADD, RevisionType.MOD }))
                .getResultList();

        if (!revisions.isEmpty()) {
            CustomRevisionEntity revisionEntity = (CustomRevisionEntity) revisions.get(0)[1];
            return Optional.of(new Timestamp(revisionEntity.getTimestamp()));
        }
        else {
            return Optional.empty();
        }
    }
}

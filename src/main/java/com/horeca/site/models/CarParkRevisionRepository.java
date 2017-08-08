package com.horeca.site.models;

import com.horeca.site.models.hotel.services.carpark.CarPark;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class CarParkRevisionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void listCityRevisions(Long cityCode) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(CarPark.class, cityCode);

        for (Number number : revisions) {
            CarPark carPark = auditReader.find(CarPark.class, cityCode, number);
            Date revisionDate = auditReader.getRevisionDate(number);
            int a = 3;
        }
        System.out.println("revisions");
        revisions.forEach(System.out::println);
    }
}

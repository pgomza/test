package com.horeca.migrations;

import com.horeca.site.models.hotel.images.FileLink;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V3_0__Store_image_links_in_list implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        List<FileLink> fileLinks = jdbcTemplate.query("select * from FileLink", (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            String filename = resultSet.getString("filename");
            String url = resultSet.getString("url");

            FileLink fileLink = new FileLink();
            fileLink.setId(id);
            fileLink.setFilename(filename);
            fileLink.setUrl(url);
            return fileLink;
        });

        List<HotelFileLink> hotelFileLinks = jdbcTemplate.query("SELECT * FROM Hotel_FileLink order by Hotel_id",
                (resultSet, i) -> {
                    Long hotelId = resultSet.getLong("Hotel_id");
                    Long fileLinkId = resultSet.getLong("FileLink_id");
                    return new HotelFileLink(hotelId, fileLinkId);
                });

        jdbcTemplate.execute("alter table FileLink add filelink_order int default (null)");
        jdbcTemplate.execute("alter table FileLink add hotel_id bigint default (null)");
        jdbcTemplate.execute("alter table FileLink add constraint fk_hotel foreign key(hotel_id) " +
                "references Hotel(id)");

        jdbcTemplate.execute("drop table Hotel_FileLink");
        jdbcTemplate.execute("delete from FileLink");

        Map<Long, Integer> orderMap = new HashMap<>();
        for (HotelFileLink hotelFileLink : hotelFileLinks) {
            for (FileLink fileLink : fileLinks) {
                if (hotelFileLink.fileLinkId.equals(fileLink.getId())) {
                    Integer order = orderMap.get(hotelFileLink.hotelId);
                    if (order == null)
                        order = 0;

                    jdbcTemplate.update("insert into FileLink (filename, url, hotel_id, filelink_order) " +
                            "values (?, ?, ?, ?)", fileLink.getFilename(), fileLink.getUrl(),
                            hotelFileLink.hotelId, order);

                    orderMap.put(hotelFileLink.hotelId, ++order);
                }
            }
        }
    }

    private static class HotelFileLink {
        public Long hotelId;
        public Long fileLinkId;

        public HotelFileLink(Long hotelId, Long fileLinkId) {
            this.hotelId = hotelId;
            this.fileLinkId = fileLinkId;
        }
    }
}

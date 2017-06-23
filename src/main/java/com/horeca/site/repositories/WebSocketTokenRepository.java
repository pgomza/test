package com.horeca.site.repositories;

import com.horeca.site.websocket.WebSocketTokenMapping;
import org.springframework.data.repository.CrudRepository;

public interface WebSocketTokenRepository extends CrudRepository<WebSocketTokenMapping, String> {
}

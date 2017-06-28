create table WebSocketTokenMapping
(
  webSocketToken nvarchar(255) primary key,
  oauthToken nvarchar(255) not null
);

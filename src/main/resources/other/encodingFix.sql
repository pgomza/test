update Hotel set description = replace(description, '&amp;', '&');
update Hotel set name = replace(name, '&amp;', '&');

update Hotel set description = replace(description, "&#39;", "'");
update Hotel set name = replace(name, "&#39;", "'");

update Hotel set description = replace(description, '&#233;', 'e');
update Hotel set name = replace(name, '&#233;', 'e');
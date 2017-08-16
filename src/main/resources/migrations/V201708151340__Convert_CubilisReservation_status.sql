UPDATE CubilisReservation SET status = 'PENDING' WHERE status like 'Waitlisted';
UPDATE CubilisReservation SET status = 'NEW' WHERE status like 'Reserved';
UPDATE CubilisReservation SET status = 'MODIFIED' WHERE status like 'Modify';
UPDATE CubilisReservation SET status = 'CANCELLED' WHERE status like 'Cancelled' or status like 'Request denied';
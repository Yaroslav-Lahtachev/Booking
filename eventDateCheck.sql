CREATE EVENT checkDate
    ON SCHEDULE
      EVERY 1 minute
    DO
      UPDATE bookingdb.order_history  SET status = 'performed'
      where order_history.date_finish <= now() and order_history.status = 'reserve';
      
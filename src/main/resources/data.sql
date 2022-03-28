insert into operation_type(id, description) values
(1, 'Normal Purchase'),
(2, 'Purchase with installments'),
(3, 'Withdrawal'),
(4, 'Credit Voucher');

insert into account(id, document_number) values
                                                (1, '232331231'),
                                                (2, '2222222'),
                                                (3, '343432424');


insert into `transaction`(id, account_id, operation_type_id, amount, balance, event_date) values
                                                (1, 1, 1, -50.00, -50.00, '2020-01-01T10:32:07'),
                                                (2, 1, 1, -23.50, -23.50, '2020-01-01T10:48:12'),
                                                (3, 1, 1, -18.70, -18.70, '2020-01-02T19:01:23');


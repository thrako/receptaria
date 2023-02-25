USE receptaria;

INSERT
  INTO receptaria.users (id, email, first_name, last_name, password, active, display_name)
VALUES (1, 'alibaba@gmail.com', 'Aladin', 'Baba', '2d307daf87089b1dafa1f9fcc3ac63435e1054d64a0602317791d4067223037336fc9a446b33adffd08194f6820c6434',
        TRUE, 'Ali Baba'),
       (2, 'kralimarko@abv.bg', 'Krali', 'Marko', '1cc482554b5c28e94a11c2e04d8e693ea42565a3ffd8767cd7d3ba1d86a66667bd40df2a64825c5893508d3f09aaccaa',
        TRUE, 'Krali Marko'),
       (3, 'khanko@brat.bg', 'Khan', 'Kubrat', 'c514292d5e2b2b9b6d13131bf1b1923589f7beccbef84a726f128509e59440f6b98eb87fdc805ae1f756e41bb00497f6',
        TRUE, 'Khanko Brat'),
       (4, 'babamarta@martenitza.com', 'Baba', 'Marta', '2f647f8f3c7f4ea26bca975b2ea2d82b77e5d7779f7a542df09040ac2882fc857d706763fd2e9222b7b642cc2d2f89c1',
        TRUE, 'Marche'),
       (5, 'tor@balan.com', 'Tor', 'Balan', '20da0b198687569f34b32ba4d5d426576cd8e6ab48d09050f0967312c9c71849824247e99136b9b4cff69a32516f2998',
        TRUE, 'Torbalan'),
       (6, 'non@me.ussr', 'Noname', 'User', 'a942c86300a0ae728e5f06c5734d330fb570fe659403c7aee435c23907347187b0842fb5fc7800177a145bea9758fdf4',
        TRUE, 'Noname User');

INSERT
  INTO receptaria.users_roles (user_entity_id, roles_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1)
;

INSERT
  INTO receptaria.units (id, name)
VALUES (1, 'tbsp'),
       (2, 'tsp'),
       (3, 'mL'),
       (4, 'L'),
       (5, 'g'),
       (6, 'kg'),
       (7, 'drop'),
       (8, 'pinch'),
       (9, 'to taste');
INSERT INTO report_reasons (id, code, description)
VALUES
    (1, 'DIRECT_THREATS', 'Прямые угрозы или призывы к физическому насилию'),
        (2, 'GRAPHIC_VIOLENCE', 'Описание реальных или вымышленных сцен насилия с детализацией'),
        (3, 'VICTIM_HARASSMENT', 'Травля или оскорбление'),
        (4, 'MASS_VIOLENCE_INCITEMENT', 'Призывы к массовым беспорядкам или агрессии'),
        (5, 'PERSONAL_DATA_LEAK', 'Распространение персональных данных'),
        (6, 'VIOLENCE_PROPAGANDA', 'Пропаганда насилия как нормы'),
        (7, 'CRIME_JUSTIFICATION', 'Оправдание преступлений, связанных с насилием'),
        (8, 'SUICIDE_INCITEMENT', 'Подстрекательство к суициду')
 ON CONFLICT (code) DO NOTHING;
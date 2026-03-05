INSERT INTO `basic_v2_year_and_month` (`year`, `month`, `work_days`, `create_time`, `create_by`)
VALUES    
    -- 以2030 年为例，第一列是年，第二列是年月，第三是该月的工作日。更新前三列就可以
    ('2030', '2030-01-01', '18', CURDATE(), 'system'),
    ('2030', '2030-02-01', '20', CURDATE(), 'system'),
    ('2030', '2030-03-01', '20', CURDATE(), 'system'),
    ('2030', '2030-04-01', '20', CURDATE(), 'system'),
    ('2030', '2030-05-01', '20', CURDATE(), 'system'),
    ('2030', '2030-06-01', '20', CURDATE(), 'system'),
    ('2030', '2030-07-01', '20', CURDATE(), 'system'),
    ('2030', '2030-08-01', '20', CURDATE(), 'system'),
    ('2030', '2030-09-01', '20', CURDATE(), 'system'),
    ('2030', '2030-10-01', '20', CURDATE(), 'system'),
    ('2030', '2030-11-01', '20', CURDATE(), 'system'),
    ('2030', '2030-12-01', '20', CURDATE(), 'system');

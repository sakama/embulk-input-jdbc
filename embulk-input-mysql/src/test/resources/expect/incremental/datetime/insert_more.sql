
insert into datetime_load (time, note) values
('2016-11-02 04:00:00', 'more_skip'),
('2016-11-02 04:00:05.333000', 'more_skip'),
('2016-11-02 04:00:05.333003', 'more_skip'),
('2016-11-02 04:00:05.333004', 'more_load'),
('2016-11-02 04:00:06', 'more_load'),
('2016-11-02 04:00:06', 'more_load');
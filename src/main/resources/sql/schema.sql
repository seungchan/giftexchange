CREATE TABLE IF NOT EXISTS familymember (
    id                     VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    fullname                   VARCHAR      NOT NULL
);

CREATE TABLE IF NOT EXISTS giftexchange (
    id                     VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    giverid                     VARCHAR(60),
    receiverid                  VARCHAR(60),
    time                        TIMESTAMP WITH TIME ZONE
);
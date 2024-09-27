CREATE TABLE broadcaster
(
    broadcaster_id BIGINT NOT NULL,
    contents_id    BIGINT NULL,
    networks_id    BIGINT NULL,
    CONSTRAINT pk_broadcaster PRIMARY KEY (broadcaster_id)
);

CREATE TABLE comment
(
    comment_id        BIGINT NOT NULL,
    created_date      datetime NULL,
    modified_date     datetime NULL,
    posting_id        BIGINT NULL,
    user_id           BIGINT NULL,
    parent_comment_id BIGINT NULL,
    content           VARCHAR(1000) NULL,
    is_removed        BIT(1) NOT NULL,
    like_cnt          INT    NOT NULL,
    dislike_cnt       INT    NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id)
);

CREATE TABLE comment_like
(
    comment_like_id BIGINT NOT NULL,
    created_date    datetime NULL,
    modified_date   datetime NULL,
    user_id         BIGINT NULL,
    comment_id      BIGINT NULL,
    like_status     INT    NOT NULL,
    CONSTRAINT pk_commentlike PRIMARY KEY (comment_like_id)
);

CREATE TABLE contents
(
    contents_id       BIGINT AUTO_INCREMENT NOT NULL,
    dtype             VARCHAR(31) NULL,
    tmdb_id           BIGINT NULL,
    tmdb_type         SMALLINT NULL,
    contents_type     VARCHAR(255) NOT NULL,
    translated_title  VARCHAR(255) NULL,
    original_title    VARCHAR(255) NULL,
    original_language VARCHAR(255) NULL,
    overview          VARCHAR(5000) NULL,
    tagline           VARCHAR(255) NULL,
    poster_path       VARCHAR(255) NULL,
    backdrop_path     VARCHAR(255) NULL,
    status            VARCHAR(255) NULL,
    certification     VARCHAR(255) NULL,
    review_cnt        INT          NOT NULL,
    posting_cnt       INT          NOT NULL,
    imad_score        FLOAT NULL,
    CONSTRAINT pk_contents PRIMARY KEY (contents_id)
);

CREATE TABLE contents_bookmark
(
    contents_bookmark_id BIGINT AUTO_INCREMENT NOT NULL,
    created_date         datetime NULL,
    modified_date        datetime NULL,
    user_id              BIGINT NULL,
    contents_id          BIGINT NULL,
    CONSTRAINT pk_contentsbookmark PRIMARY KEY (contents_bookmark_id)
);

CREATE TABLE contents_daily_ranking_score
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    contents_id   BIGINT NULL,
    ranking_score INT NOT NULL,
    CONSTRAINT pk_contentsdailyrankingscore PRIMARY KEY (id)
);

CREATE TABLE contents_genre
(
    contents_id     BIGINT NOT NULL,
    contents_genres INT NULL
);

CREATE TABLE countries
(
    contents_id          BIGINT NOT NULL,
    production_countries VARCHAR(255) NULL
);

CREATE TABLE credit
(
    credit_id            VARCHAR(255) NOT NULL,
    person_id            BIGINT NULL,
    contents_id          BIGINT NULL,
    department           VARCHAR(255) NULL,
    known_for_department VARCHAR(255) NULL,
    job                  VARCHAR(255) NULL,
    character_name       VARCHAR(255) NULL,
    credit_type          SMALLINT NULL,
    importance_order     INT          NOT NULL,
    CONSTRAINT pk_credit PRIMARY KEY (credit_id)
);

CREATE TABLE genre
(
    genre_id   BIGINT NOT NULL,
    name       VARCHAR(255) NULL,
    genre_type INT    NOT NULL,
    CONSTRAINT pk_genre PRIMARY KEY (genre_id)
);

CREATE TABLE movie_data
(
    contents_id  BIGINT NOT NULL,
    release_date date NULL,
    runtime      INT NULL,
    CONSTRAINT pk_moviedata PRIMARY KEY (contents_id)
);

CREATE TABLE networks
(
    networks_id    BIGINT NOT NULL,
    networks_name  VARCHAR(255) NULL,
    logo_path      VARCHAR(255) NULL,
    origin_country VARCHAR(255) NULL,
    CONSTRAINT pk_networks PRIMARY KEY (networks_id)
);

CREATE TABLE person
(
    person_id       BIGINT NOT NULL,
    original_name   VARCHAR(255) NULL,
    translated_name VARCHAR(255) NULL,
    gender          SMALLINT NULL,
    profile_path    VARCHAR(255) NULL,
    CONSTRAINT pk_person PRIMARY KEY (person_id)
);

CREATE TABLE posting
(
    posting_id    BIGINT NOT NULL,
    created_date  datetime NULL,
    modified_date datetime NULL,
    user_id       BIGINT NULL,
    contents_id   BIGINT NULL,
    category      INT    NOT NULL,
    title         VARCHAR(255) NULL,
    content       VARCHAR(5000) NULL,
    is_spoiler    BIT(1) NOT NULL,
    view_cnt      INT    NOT NULL,
    comment_cnt   INT    NOT NULL,
    like_cnt      INT    NOT NULL,
    dislike_cnt   INT    NOT NULL,
    CONSTRAINT pk_posting PRIMARY KEY (posting_id)
);

CREATE TABLE posting_like
(
    posting_like_id BIGINT NOT NULL,
    created_date    datetime NULL,
    modified_date   datetime NULL,
    user_id         BIGINT NULL,
    posting_id      BIGINT NULL,
    like_status     INT    NOT NULL,
    CONSTRAINT pk_postinglike PRIMARY KEY (posting_like_id)
);

CREATE TABLE posting_scrap
(
    posting_scrap_id BIGINT AUTO_INCREMENT NOT NULL,
    created_date     datetime NULL,
    modified_date    datetime NULL,
    user_id          BIGINT NULL,
    posting_id       BIGINT NULL,
    CONSTRAINT pk_postingscrap PRIMARY KEY (posting_scrap_id)
);

CREATE TABLE preferred_movie_genres
(
    user_id                BIGINT NOT NULL,
    preferred_movie_genres BIGINT NULL
);

CREATE TABLE preferred_tv_genres
(
    user_id             BIGINT NOT NULL,
    preferred_tv_genres BIGINT NULL
);

CREATE TABLE ranking_all_time
(
    id                        BIGINT AUTO_INCREMENT NOT NULL,
    contents_id               BIGINT NULL,
    contents_type             SMALLINT NULL,
    ranking                   BIGINT NULL,
    ranking_tv                BIGINT NULL,
    ranking_movie             BIGINT NULL,
    ranking_animation         BIGINT NULL,
    ranking_changed           BIGINT NULL,
    ranking_changed_tv        BIGINT NULL,
    ranking_changed_movie     BIGINT NULL,
    ranking_changed_animation BIGINT NULL,
    ranking_score             BIGINT NULL,
    CONSTRAINT pk_rankingalltime PRIMARY KEY (id)
);

CREATE TABLE ranking_monthly
(
    id                        BIGINT AUTO_INCREMENT NOT NULL,
    contents_id               BIGINT NULL,
    contents_type             SMALLINT NULL,
    ranking                   BIGINT NULL,
    ranking_tv                BIGINT NULL,
    ranking_movie             BIGINT NULL,
    ranking_animation         BIGINT NULL,
    ranking_changed           BIGINT NULL,
    ranking_changed_tv        BIGINT NULL,
    ranking_changed_movie     BIGINT NULL,
    ranking_changed_animation BIGINT NULL,
    ranking_score             BIGINT NULL,
    CONSTRAINT pk_rankingmonthly PRIMARY KEY (id)
);

CREATE TABLE ranking_weekly
(
    id                        BIGINT AUTO_INCREMENT NOT NULL,
    contents_id               BIGINT NULL,
    contents_type             SMALLINT NULL,
    ranking                   BIGINT NULL,
    ranking_tv                BIGINT NULL,
    ranking_movie             BIGINT NULL,
    ranking_animation         BIGINT NULL,
    ranking_changed           BIGINT NULL,
    ranking_changed_tv        BIGINT NULL,
    ranking_changed_movie     BIGINT NULL,
    ranking_changed_animation BIGINT NULL,
    ranking_score             BIGINT NULL,
    CONSTRAINT pk_rankingweekly PRIMARY KEY (id)
);

CREATE TABLE report_comment
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_date        datetime NULL,
    modified_date       datetime NULL,
    reporter_id         BIGINT NULL,
    reported_comment_id BIGINT NULL,
    report_type         SMALLINT NULL,
    report_desc         VARCHAR(255) NULL,
    CONSTRAINT pk_report_comment PRIMARY KEY (id)
);

CREATE TABLE report_posting
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_date        datetime NULL,
    modified_date       datetime NULL,
    reporter_id         BIGINT NULL,
    reported_posting_id BIGINT NULL,
    report_type         SMALLINT NULL,
    report_desc         VARCHAR(255) NULL,
    CONSTRAINT pk_report_posting PRIMARY KEY (id)
);

CREATE TABLE report_review
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_date       datetime NULL,
    modified_date      datetime NULL,
    reporter_id        BIGINT NULL,
    reported_review_id BIGINT NULL,
    report_type        SMALLINT NULL,
    report_desc        VARCHAR(255) NULL,
    CONSTRAINT pk_report_review PRIMARY KEY (id)
);

CREATE TABLE report_user
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_date     datetime NULL,
    modified_date    datetime NULL,
    reporter_id      BIGINT NULL,
    reported_user_id BIGINT NULL,
    report_type      SMALLINT NULL,
    report_desc      VARCHAR(255) NULL,
    CONSTRAINT pk_report_user PRIMARY KEY (id)
);

CREATE TABLE review
(
    review_id     BIGINT NOT NULL,
    created_date  datetime NULL,
    modified_date datetime NULL,
    user_id       BIGINT NULL,
    contents_id   BIGINT NULL,
    title         VARCHAR(255) NULL,
    content       VARCHAR(5000) NULL,
    score         FLOAT  NOT NULL,
    is_spoiler    BIT(1) NOT NULL,
    like_cnt      INT    NOT NULL,
    dislike_cnt   INT    NOT NULL,
    CONSTRAINT pk_review PRIMARY KEY (review_id)
);

CREATE TABLE review_like
(
    review_like_id BIGINT NOT NULL,
    created_date   datetime NULL,
    modified_date  datetime NULL,
    user_id        BIGINT NULL,
    review_id      BIGINT NULL,
    like_status    INT    NOT NULL,
    CONSTRAINT pk_reviewlike PRIMARY KEY (review_like_id)
);

CREATE TABLE season
(
    season_id     BIGINT NOT NULL,
    season_name   VARCHAR(255) NULL,
    air_date      date NULL,
    episode_count INT    NOT NULL,
    overview      VARCHAR(5000) NULL,
    poster_path   VARCHAR(255) NULL,
    season_number INT    NOT NULL,
    CONSTRAINT pk_season PRIMARY KEY (season_id)
);

CREATE TABLE season_collection
(
    season_collection_id BIGINT NOT NULL,
    tv_program_data_id   BIGINT NULL,
    season_id            BIGINT NULL,
    CONSTRAINT pk_seasoncollection PRIMARY KEY (season_collection_id)
);

CREATE TABLE today_popular_posting
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    posting_id    BIGINT NULL,
    popular_score BIGINT NULL,
    CONSTRAINT pk_todaypopularposting PRIMARY KEY (id)
);

CREATE TABLE today_popular_review
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    review_id     BIGINT NULL,
    popular_score BIGINT NULL,
    CONSTRAINT pk_todaypopularreview PRIMARY KEY (id)
);

CREATE TABLE tv_program_data
(
    contents_id        BIGINT NOT NULL,
    first_air_date     date NULL,
    last_air_date      date NULL,
    number_of_episodes INT NULL,
    number_of_seasons  INT NULL,
    CONSTRAINT pk_tvprogramdata PRIMARY KEY (contents_id)
);

CREATE TABLE user_account
(
    user_id            BIGINT AUTO_INCREMENT                              NOT NULL,
    created_date       datetime NULL,
    modified_date      datetime NULL,
    email              VARCHAR(255) NULL,
    password           VARCHAR(255) NULL,
    nickname           VARCHAR(50) NULL,
    gender             SMALLINT NULL,
    birth_year         INT          NOT NULL,
    age_range          INT          NOT NULL,
    profile_image      VARCHAR(255) DEFAULT 'default_profile_image_1.png' NULL,
    `role`             VARCHAR(255) NOT NULL,
    auth_provider      VARCHAR(255) NOT NULL,
    social_id          VARCHAR(255) NULL,
    oauth2access_token VARCHAR(255) NULL,
    refresh_token      VARCHAR(255) NULL,
    CONSTRAINT pk_useraccount PRIMARY KEY (user_id)
);

CREATE TABLE user_activity
(
    user_activity_id BIGINT AUTO_INCREMENT NOT NULL,
    created_date     datetime NULL,
    modified_date    datetime NULL,
    user_id          BIGINT       NOT NULL,
    contents_id      BIGINT       NOT NULL,
    bookmark_id      BIGINT NULL,
    review_id        BIGINT NULL,
    review_like_id   BIGINT NULL,
    posting_id       BIGINT NULL,
    posting_like_id  BIGINT NULL,
    activity_type    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_useractivity PRIMARY KEY (user_activity_id)
);

CREATE INDEX idx_164d4ec6546f1903b59c47bfd ON ranking_weekly (ranking);

CREATE INDEX idx_2ddff227e1e01bb90cbe7a3a5 ON ranking_monthly (ranking_changed);

CREATE INDEX idx_7f2e6f497350b98f8734fdb7c ON ranking_weekly (ranking_changed);

CREATE INDEX idx_8a91a452cbd13c6d70112b1ec ON ranking_monthly (ranking);

CREATE INDEX idx_a0d0f195de41514f099760318 ON contents (contents_type);

CREATE INDEX idx_c26e2e1fdd02f6e13ff1b5dcc ON ranking_all_time (ranking_changed);

CREATE UNIQUE INDEX idx_c3d6495bbfad6f0e851abb81a ON user_account (nickname);

CREATE UNIQUE INDEX idx_cf8351eb53a6f0eef82cd59ae ON user_account (email);

CREATE INDEX idx_f8dd137532b71471a9fc9ffe1 ON ranking_all_time (ranking);

ALTER TABLE broadcaster
    ADD CONSTRAINT FK_BROADCASTER_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES tv_program_data (contents_id) ON DELETE CASCADE;

ALTER TABLE broadcaster
    ADD CONSTRAINT FK_BROADCASTER_ON_NETWORKS FOREIGN KEY (networks_id) REFERENCES networks (networks_id) ON DELETE CASCADE;

ALTER TABLE comment_like
    ADD CONSTRAINT FK_COMMENTLIKE_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE;

ALTER TABLE comment_like
    ADD CONSTRAINT FK_COMMENTLIKE_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_PARENT_COMMENT FOREIGN KEY (parent_comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_POSTING FOREIGN KEY (posting_id) REFERENCES posting (posting_id) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE contents_bookmark
    ADD CONSTRAINT FK_CONTENTSBOOKMARK_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE contents_bookmark
    ADD CONSTRAINT FK_CONTENTSBOOKMARK_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE contents_daily_ranking_score
    ADD CONSTRAINT FK_CONTENTSDAILYRANKINGSCORE_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE credit
    ADD CONSTRAINT FK_CREDIT_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE credit
    ADD CONSTRAINT FK_CREDIT_ON_PERSON FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE CASCADE;

ALTER TABLE movie_data
    ADD CONSTRAINT FK_MOVIEDATA_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id);

ALTER TABLE posting_like
    ADD CONSTRAINT FK_POSTINGLIKE_ON_POSTING FOREIGN KEY (posting_id) REFERENCES posting (posting_id) ON DELETE CASCADE;

ALTER TABLE posting_like
    ADD CONSTRAINT FK_POSTINGLIKE_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE posting_scrap
    ADD CONSTRAINT FK_POSTINGSCRAP_ON_POSTING FOREIGN KEY (posting_id) REFERENCES posting (posting_id) ON DELETE CASCADE;

ALTER TABLE posting_scrap
    ADD CONSTRAINT FK_POSTINGSCRAP_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE posting
    ADD CONSTRAINT FK_POSTING_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE posting
    ADD CONSTRAINT FK_POSTING_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE ranking_all_time
    ADD CONSTRAINT FK_RANKINGALLTIME_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE ranking_monthly
    ADD CONSTRAINT FK_RANKINGMONTHLY_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE ranking_weekly
    ADD CONSTRAINT FK_RANKINGWEEKLY_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE report_comment
    ADD CONSTRAINT FK_REPORT_COMMENT_ON_REPORTED_COMMENT FOREIGN KEY (reported_comment_id) REFERENCES comment (comment_id) ON DELETE CASCADE;

ALTER TABLE report_comment
    ADD CONSTRAINT FK_REPORT_COMMENT_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE report_posting
    ADD CONSTRAINT FK_REPORT_POSTING_ON_REPORTED_POSTING FOREIGN KEY (reported_posting_id) REFERENCES posting (posting_id) ON DELETE CASCADE;

ALTER TABLE report_posting
    ADD CONSTRAINT FK_REPORT_POSTING_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE report_review
    ADD CONSTRAINT FK_REPORT_REVIEW_ON_REPORTED_REVIEW FOREIGN KEY (reported_review_id) REFERENCES review (review_id) ON DELETE CASCADE;

ALTER TABLE report_review
    ADD CONSTRAINT FK_REPORT_REVIEW_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE report_user
    ADD CONSTRAINT FK_REPORT_USER_ON_REPORTED_USER FOREIGN KEY (reported_user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE report_user
    ADD CONSTRAINT FK_REPORT_USER_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE review_like
    ADD CONSTRAINT FK_REVIEWLIKE_ON_REVIEW FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE;

ALTER TABLE review_like
    ADD CONSTRAINT FK_REVIEWLIKE_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE season_collection
    ADD CONSTRAINT FK_SEASONCOLLECTION_ON_SEASON FOREIGN KEY (season_id) REFERENCES season (season_id) ON DELETE CASCADE;

ALTER TABLE season_collection
    ADD CONSTRAINT FK_SEASONCOLLECTION_ON_TV_PROGRAM_DATA FOREIGN KEY (tv_program_data_id) REFERENCES tv_program_data (contents_id) ON DELETE CASCADE;

ALTER TABLE today_popular_posting
    ADD CONSTRAINT FK_TODAYPOPULARPOSTING_ON_POSTING FOREIGN KEY (posting_id) REFERENCES posting (posting_id) ON DELETE CASCADE;

ALTER TABLE today_popular_review
    ADD CONSTRAINT FK_TODAYPOPULARREVIEW_ON_REVIEW FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE;

ALTER TABLE tv_program_data
    ADD CONSTRAINT FK_TVPROGRAMDATA_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id);

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_BOOKMARK FOREIGN KEY (bookmark_id) REFERENCES contents_bookmark (contents_bookmark_id) ON DELETE CASCADE;

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_CONTENTS FOREIGN KEY (contents_id) REFERENCES contents (contents_id) ON DELETE CASCADE;

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_POSTING FOREIGN KEY (posting_id) REFERENCES posting (posting_id) ON DELETE CASCADE;

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_POSTING_LIKE FOREIGN KEY (posting_like_id) REFERENCES posting_like (posting_like_id) ON DELETE CASCADE;

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_REVIEW FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE;

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_REVIEW_LIKE FOREIGN KEY (review_like_id) REFERENCES review_like (review_like_id) ON DELETE CASCADE;

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (user_id) ON DELETE CASCADE;

ALTER TABLE contents_genre
    ADD CONSTRAINT fk_contents_genre_on_contents FOREIGN KEY (contents_id) REFERENCES contents (contents_id);

ALTER TABLE countries
    ADD CONSTRAINT fk_countries_on_contents FOREIGN KEY (contents_id) REFERENCES contents (contents_id);

ALTER TABLE preferred_movie_genres
    ADD CONSTRAINT fk_preferred_movie_genres_on_user_account FOREIGN KEY (user_id) REFERENCES user_account (user_id);

ALTER TABLE preferred_tv_genres
    ADD CONSTRAINT fk_preferred_tv_genres_on_user_account FOREIGN KEY (user_id) REFERENCES user_account (user_id);
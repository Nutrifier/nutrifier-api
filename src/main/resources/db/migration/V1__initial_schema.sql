-- ---------------- --
-- Reference tables --
-- ---------------- --

create table roles
(
    id char(36) not null primary key,
    name varchar(50) not null,
    constraint uk_roles_name unique (name)
);

create table meal_types
(
    id char(36) not null primary key,
    name varchar(50) not null,
    constraint uk_meal_types_name unique (name)
);

create table serving_types
(
    id char(36) not null primary key,
    name varchar(50) not null,
    constraint uk_serving_types_name unique (name)
);

create table activity_levels
(
    id char(36) not null primary key,
    name varchar(50) not null,
    multiplier double not null,
    description varchar(255) not null,
    constraint uk_activity_levels_name unique (name)
);

create table diets
(
    id char(36) not null primary key,
    name varchar(50) not null,
    constraint uk_diets_name unique (name)
);

create table food_categories
(
    id char(36) not null primary key,
    name varchar(50) not null,
    constraint uk_food_categories_name unique (name)
);

create table food_brands
(
    id char(36) not null primary key,
    name varchar(50) not null,
    verified tinyint(1) default 0 not null,
    constraint uk_food_brands_name unique (name)
);



-- -------------------- --
-- User specific tables --
-- -------------------- --

create table users
(
    id char(36) not null primary key,
    email varchar(255) not null,
    password varchar(255) not null,
    role_id char(36) not null,
    constraint uk_users_email unique (email),
    constraint users_roles_id_fk
        foreign key (role_id) references roles (id)
            on update cascade on delete restrict
);

create table profile
(
    user_id char(36) not null
        primary key,
    height int null,
    age int null,
    sex enum('MALE', 'FEMALE', 'OTHER') null,
    activity_level_id char(36) null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint user_profile_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade,
    constraint user_profile_activity_levels_id_fk
        foreign key (activity_level_id) references activity_levels (id)
            on update cascade
);

CREATE TABLE settings
(
    user_id CHAR(36) NOT NULL PRIMARY KEY,
    weight_unit VARCHAR(10) NOT NULL,
    energy_unit VARCHAR(5) NOT NULL,
    language VARCHAR(5) NOT NULL,
    nutrient_display_mode VARCHAR(20) NOT NULL,
    time_between_meals INT NULL,
    diet_id char(36) null,
    week_starts_on INT NOT NULL,
    protein_efficiency_enabled TINYINT(1) NOT NULL,
    meal_reminder_enabled tinyint(1) NOT NULL,
    weigh_in_reminder_enabled tinyint(1) NOT NULL,
    motivation_messages_enabled tinyint(1) NOT NULL,
    updated_at timestamp default CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT settings_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
            on update cascade on delete cascade,
    CONSTRAINT settings_diets_id_fk
        FOREIGN KEY (diet_id) REFERENCES diets (id)
            on update cascade on delete set null
);

create table weights
(
    id char(36) not null
        primary key,
    user_id char(36) not null,
    weight double not null,
    date datetime not null,
    constraint weights_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);

create table goals
(
    id char(36) not null
        primary key,
    user_id char(36) not null,
    goal_type enum('CUT', 'BULK', 'MAINTAIN') not null,
    start_date date not null,
    target_date date null,
    start_weight double not null,
    target_weight double null,
    is_reached tinyint(1) default 0 null,
    daily_tdee double not null,
    daily_calorie_balance double not null,
    daily_calorie_target double not null,
    daily_fat_target double not null,
    daily_carb_target double not null,
    daily_protein_target double not null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint goals_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);



-- ----------- --
-- Food tables --
-- ----------- --

create table foods
(
    id char(36) not null
        primary key,
    name varchar(255) not null,
    brand_id char(36) null,
    category_id char(36) null,
    barcode varchar(255) null,
    calories double not null,
    fat double not null,
    carbs double not null,
    protein double not null,
    verified tinyint(1) null,
    status enum('ACTIVE', 'INACTIVE') default 'ACTIVE' not null,
    created_at datetime(6) not null,
    created_by char(36) null,
    updated_at datetime(6) not null,
    updated_by char(36) null,
    constraint foods_created_by_user_id_fk
        foreign key (created_by) references users (id)
            on update cascade on delete set null,
    constraint foods_updated_by_user_id_fk
        foreign key (updated_by) references users (id)
            on update cascade on delete set null,
    constraint foods_food_brands_id_fk
        foreign key (brand_id) references food_brands (id)
            on update cascade on delete set null,
    constraint foods_food_categories_id_fk
        foreign key (category_id) references food_categories (id)
            on update cascade on delete set null
);

create table food_servings
(
    food_id char(36) not null,
    serving_type_id char(36) not null,
    amount double not null,
    primary key (food_id, serving_type_id),
    constraint food_servings_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade on delete cascade,
    constraint food_servings_serving_types_id_fk
        foreign key (serving_type_id) references serving_types (id)
            on update cascade
);

create table food_entries
(
    id char(36) not null
        primary key,
    serving_amount double not null,
    serving_type_id char(36) not null,
    date date not null,
    time time(6) not null,
    meal_type_id char(36) not null,
    calories_snapshot double not null,
    fat_snapshot double not null,
    carbs_snapshot double not null,
    protein_snapshot double not null,
    fineli_id int null,
    user_id char(36) not null,
    food_id char(36) null,
    constraint logs_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade,
    constraint logs_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade,
    constraint food_entries_meal_types_id_fk
        foreign key (meal_type_id) references meal_types (id)
            on update cascade,
    constraint food_entries_serving_types_id_fk
        foreign key (serving_type_id) references serving_types (id)
            on update cascade
);

create table food_reports
(
    id char(36) not null
        primary key,
    food_id char(36) null,
    user_id char(36) null,
    type enum('REPORT', 'UPDATE_REQUEST', 'DELETION_REQUEST') not null,
    reason varchar(255) not null,
    status enum('PENDING', 'APPROVED', 'REJECTED') default 'PENDING' not null,
    description varchar(255) null,
    proposed_name varchar(255) null,
    proposed_calories double null,
    proposed_fat double null,
    proposed_carbs double null,
    proposed_protein double null,
    decision_reasoning varchar(255) null,
    reviewed_by char(36) null,
    reviewed_at datetime null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint food_reports_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade on delete set null,
    constraint food_reports_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete set null,
    constraint food_reports_users_id_fk2
        foreign key (reviewed_by) references users (id)
            on update cascade on delete set null
);

create table food_usage
(
    user_id char(36) not null,
    food_id char(36) not null,
    usage_count int not null,
    last_used_at datetime not null,
    primary key (user_id, food_id),
    constraint food_usage_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade on delete cascade,
    constraint food_usage_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);

create table favourite_foods
(
    user_id char(36) not null,
    food_id char(36) not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    primary key (user_id, food_id),
    constraint favourite_foods_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade on delete cascade,
    constraint favourite_foods_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);



-- ------------- --
-- Recipe tables --
-- ------------- --

create table recipes
(
    id char(36) not null
        primary key,
    owner_id char(36) null,
    name varchar(255) not null,
    description varchar(255) null,
    servings int not null,
    preparation_time int null,
    is_public tinyint(1) default 0 not null,
    is_forked tinyint(1) default 0 not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint recipes_users_id_fk
        foreign key (owner_id) references users (id)
            on update cascade on delete set null
);

create table recipe_steps
(
    id char(36) not null
        primary key,
    recipe_id char(36) null,
    position int not null,
    instruction varchar(255) not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint recipe_steps_pk
        unique (recipe_id, position),
    constraint FKof4i3g3aiwgro5ykaf1j28iw1
        foreign key (recipe_id) references recipes (id)
);

create table recipe_sections
(
    id char(36) not null
        primary key,
    recipe_id char(36) not null,
    name varchar(255) null,
    position int not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint recipe_sections_recipes_id_fk
        foreign key (recipe_id) references recipes (id)
            on update cascade on delete cascade
);

create table recipe_ingredients
(
    id char(36) not null
        primary key,
    section_id char(36) null,
    food_id char(36) null,
    amount double not null,
    unit varchar(5) null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint recipe_ingredients_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade,
    constraint recipe_ingredients_recipe_sections_id_fk
        foreign key (section_id) references recipe_sections (id)
            on update cascade on delete cascade
);

create table recipe_reports
(
    id char(36) not null
        primary key,
    recipe_id char(36) null,
    user_id char(36) null,
    type enum('REPORT', 'UPDATE_REQUEST', 'DELETION_REQUEST') not null,
    reason varchar(255) not null,
    description varchar(255) null,
    status enum('PENDING', 'APPROVED', 'REJECTED') default 'PENDING' not null,
    decision_reasoning varchar(255) null,
    reviewed_by char(36) null,
    reviewed_at datetime null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint recipe_reports_recipe_id_fk
        foreign key (recipe_id) references recipes (id)
            on update cascade on delete set null,
    constraint recipe_reports_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete set null,
    constraint recipe_reports_users_id_fk2
        foreign key (reviewed_by) references users (id)
            on update cascade on delete set null
);

create table favourite_recipes
(
    user_id char(36) not null,
    recipe_id char(36) not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    primary key (user_id, recipe_id),
    constraint favourite_recipes_recipes_id_fk
        foreign key (recipe_id) references recipes (id)
            on update cascade on delete cascade,
    constraint favourite_recipes_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);



-- ----------- --
-- Meal tables --
-- ----------- --

create table meals
(
    id char(36) not null
        primary key,
    owner_id char(36) null,
    name varchar(255) not null,
    is_public tinyint(1) default 0 not null,
    is_forked tinyint(1) default 0 not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint meals_users_id_fk
        foreign key (owner_id) references users (id)
            on update cascade on delete set null
);

create table meal_entries
(
    meal_id char(36) not null,
    food_id char(36) not null,
    amount double not null,
    unit varchar(5) null,
    id char(36) not null,
    primary key (meal_id, food_id),
    constraint meal_entries_foods_id_fk
        foreign key (food_id) references foods (id)
            on update cascade,
    constraint meal_entries_meals_id_fk
        foreign key (meal_id) references meals (id)
            on update cascade on delete cascade
);

create table favourite_meals
(
    user_id char(36) not null,
    meal_id char(36) not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    primary key (user_id, meal_id),
    constraint favourite_meals_meals_id_fk
        foreign key (meal_id) references meals (id)
            on update cascade on delete cascade,
    constraint favourite_meals_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);



-- -------------- --
-- Summary tables --
-- -------------- --

create table daily_summaries
(
    id char(36) not null
        primary key,
    user_id char(36) not null,
    date date not null,
    calorie_target double not null,
    fat_target double not null,
    carb_target double not null,
    protein_target double not null,
    confirmed tinyint(1) default 0 not null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint daily_summaries_pk
        unique (date, user_id),
    constraint daily_summaries_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);

create table daily_summary_meals
(
    id char(36) not null
        primary key,
    daily_summary_id char(36) not null,
    meal_type_id char(36) not null,
    calories double not null,
    fat double not null,
    carbs double not null,
    protein double not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint daily_summary_meals_daily_summaries_id_fk
        foreign key (daily_summary_id) references daily_summaries (id)
            on update cascade on delete cascade,
    constraint daily_summary_meals_meal_types_id_fk
        foreign key (meal_type_id) references meal_types (id)
            on update cascade
);



-- ------------------ --
-- Maintenance tables --
-- ------------------ --

create table user_feedbacks
(
    id char(36) not null
        primary key,
    user_id char(36) null,
    type enum('BUG', 'FEATURE', 'FEEDBACK', 'SUPPORT') not null,
    title varchar(255) not null,
    message varchar(255) not null,
    status enum('PENDING', 'APPROVED', 'RESOLVED', 'CLOSED') default 'PENDING' not null,
    decision_reasoning varchar(255) null,
    reviewed_by char(36) null,
    reviewed_at datetime null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint feedback_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete set null,
    constraint feedback_users_id_fk2
        foreign key (reviewed_by) references users (id)
            on update cascade on delete set null
);

create table audit_logs
(
    id char(36) not null
        primary key,
    user_id char(36) null,
    action varchar(255) not null,
    category varchar(20) not null,
    source varchar(20) not null,
    datetime datetime not null,
    old_value varchar(255) null,
    new_value varchar(255) null,
    session varchar(255) null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint audit_logs_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete set null
);

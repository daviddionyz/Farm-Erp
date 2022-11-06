create table farm_erp.workers
(
    id                   serial not null primary key,
    name                 varchar,
    join_date            timestamp,
    vehicle_id           int references farm_erp.vehicles(id),
    position             varchar,
    is_deleted           bool
)

create table farm_erp.vehicles
(
    id                   serial not null primary key,
    name                 varchar,
    type                 varchar,
    status               int,
    is_deleted           bool
)

create table farm_erp.storages
(
    id                   serial not null primary key,
    name                 varchar,
    capacity             int,
    fullness             int,
    is_deleted           bool
)

create table farm_erp.crops
(
    id                   serial not null primary key,
    storage_id           int 	 farm_erp.storage(id),
    crops_name           varchar,
    crops_type           varchar,
    amount               int
)

create table farm_erp.fields
(
    id                   serial not null primary key,
    name                 varchar,
    size                 int,
    crop_name           varchar,
    corp_type           varchar,
    is_deleted           bool
)

create table farm_erp.harvest_diaries
(
    id                   serial not null primary key,
    name                 varchar,
    year                 int
)

create table farm_erp.deliveries
(
    id                   serial not null primary key,
    diary_id 			 int references farm_erp.harvest_diaries(id),
    gross				 int,
    empty				 int,
    net					 int,
    worker_id			 int references farm_erp.workers(id),
    vehicle_id			 int references farm_erp.vehicles(id),
    intake_date 		 timestamp,
    from_id				 int references farm_erp.fields(id),
    where_id			 int references farm_erp.storages(id),
    is_crop_moving       bool,
    from_storage_id      int references farm_erp.storages(id)
)


create table farm_erp.users
(
    id                   serial not null primary key,
    username 			 varchar,
    password             varchar,
    created_at  		 timestamp,
    role				 int references farm_erp.user_roles(id)
)

create table farm_erp.user_roles
(
    id                   serial not null primary key,
    role 				 varchar
)
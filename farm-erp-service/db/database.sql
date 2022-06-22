create table farm_erp.workerEntities
(
    id                   serial not null primary key,
    name                 varchar,
    join_date            timestamp,
    vehicle_id           int references farm_erp.vehiclesEntity(id),
    position             varchar
)

create table farm_erp.vehiclesEntity
(
    id                   serial not null primary key,
    name                 varchar,
    type                 varchar,
    status               int
)

create table farm_erp.storageEntity
(
    id                   serial not null primary key,
    name                 varchar,
    capacity             int,
    fullness             int
)

create table farm_erp.crops
(
    id                   serial not null primary key,
    storage_id           int 	 farm_erp.storageEntity(id),
    crops_name           varchar,
    crops_type           varchar,
    amount               int
)

create table farm_erp.fieldEntity
(
    id                   serial not null primary key,
    name                 varchar,
    size                 int,
    crops_name           varchar,
    corps_type           varchar
)

create table farm_erp.harvest_diary
(
    id                   serial not null primary key,
    name                 varchar,
    year                 int
)

create table farm_erp.deliveriesEntity
(
    id                   serial not null primary key,
    diary_id 			 int references farm_erp.harvest_diary(id),
    gross				 int,
    empty				 int,
    net					 int,
    worker_id			 int references farm_erp.workerEntities(id),
    vehicle_id			 int references farm_erp.vehiclesEntity(id),
    intake_date 		 timestamp,
    "from"				 int references farm_erp.fieldEntity(id),
    "where"				 int references farm_erp.storageEntity(id),
    is_crop_moving       bool,
    from_storage_id      int references farm_erp.storageEntity(id)
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
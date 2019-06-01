create table shoppingstreet.address
(
	id bigint not null,
	bind_object bigint not null comment '地址绑定的对象',
	kp_name varchar(20) not null comment '联系人',
	kp_phone varchar(11) not null comment '联系电话',
	province int default '0' not null,
	city int default '0' not null,
	area int default '0' not null,
	detail varchar(128) not null,
	post_code varchar(8) not null,
	state int default '1' not null comment '-1删除，1正常使用地址，2默认使用地址',
	create_time timestamp default CURRENT_TIMESTAMP not null,
	modify_time timestamp default CURRENT_TIMESTAMP not null,
	primary key (id)
)
engine=InnoDB
;

create index shoppingstreet.adress_bind_object_index
	on shoppingstreet.address (bind_object)
;

create table shoppingstreet.commodity
(
	id bigint not null comment 'ID',
	name varchar(64) null comment '商品名称',
	stock int default '0' not null comment '库存',
	category_id bigint null comment '类目ID',
	thumbs_url varchar(128) null comment '缩略图链接',
	origin_price decimal(21,4) not null comment '原价格',
	current_price decimal(21,4) not null comment '当前价格',
	detail text null comment '详情',
	created datetime default CURRENT_TIMESTAMP not null comment '创建时间',
	updated datetime default CURRENT_TIMESTAMP not null comment '更新时间',
	primary key (id)
)
comment '商品' engine=InnoDB
;

create table shoppingstreet.delivery_way
(
	id bigint not null,
	way_name varchar(32) not null,
	`desc` varchar(256) not null,
	price decimal(21,4) not null,
	created datetime default CURRENT_TIMESTAMP not null,
	updated datetime not null,
	primary key (id)
)
comment '配送方式' engine=InnoDB
;

create table shoppingstreet.order_detail
(
	id bigint auto_increment,
	order_id bigint not null,
	commodity_id bigint not null,
	amount decimal(21,4) not null,
	create_time timestamp default CURRENT_TIMESTAMP not null,
	modify_time timestamp default CURRENT_TIMESTAMP not null,
	buy_num int default '1' not null comment '购买数量',
	primary key (id)
)
comment '订单详情' engine=InnoDB
;

create index shoppingstreet.order_detail_order_id_index
	on shoppingstreet.order_detail (order_id)
;

create table shoppingstreet.payment_flow
(
	id bigint not null,
	order_id bigint not null,
	amount decimal(21,4) not null,
	pay_way int not null,
	payer bigint not null,
	payer_name varchar(32) default '' not null,
	payee bigint not null,
	payee_name varchar(32) default '' not null,
	type int not null,
	created datetime default CURRENT_TIMESTAMP not null,
	updated datetime default CURRENT_TIMESTAMP not null,
	primary key (id)
)
comment '支付流水' engine=InnoDB
;

create index shoppingstreet.payment_flow_order_id_index
	on shoppingstreet.payment_flow (order_id)
;

create index shoppingstreet.payment_flow_payer_index
	on shoppingstreet.payment_flow (payer)
;

create index shoppingstreet.payment_flow_payee_index
	on shoppingstreet.payment_flow (payee)
;

create table shoppingstreet.shopping_cart
(
	id bigint not null,
	uid bigint not null,
	commodity_id bigint not null,
	buy_num int default '1' not null,
	state int default '1' not null comment '-1移除购物车，1正常展示，2已购买',
	create_time timestamp default CURRENT_TIMESTAMP not null,
	modify_time timestamp default CURRENT_TIMESTAMP not null,
	primary key (id)
)
comment '购物车' engine=InnoDB
;

create index shoppingstreet.shopping_cart_uid_state_index
	on shoppingstreet.shopping_cart (uid, state)
;

create index shoppingstreet.shopping_cart_commodity_id_state_index
	on shoppingstreet.shopping_cart (commodity_id, state)
;

create table shoppingstreet.trade_order
(
	id bigint not null,
	buyer bigint not null comment '买家',
	address_id bigint not null comment '收货地址id',
	delivery_way bigint default '0' not null comment '送货方式',
	amount decimal(21,4) not null comment '交易金额',
	state int default '1' not null comment '订单状态：-1失效，1未支付，2已支付',
	create_time timestamp default CURRENT_TIMESTAMP not null,
	modify_time timestamp default CURRENT_TIMESTAMP not null,
	memo varchar(128) null,
	primary key (id)
)
comment '订单' engine=InnoDB
;

create index shoppingstreet.trade_order_buyer_state_index
	on shoppingstreet.trade_order (buyer, state)
;

create table shoppingstreet.user_account
(
	uid bigint not null,
	phone varchar(11) not null,
	pwd varchar(64) not null,
	salt varchar(64) not null,
	username varchar(50) default '' not null,
	email varchar(50) null,
	create_time timestamp default CURRENT_TIMESTAMP not null,
	modify_time timestamp default CURRENT_TIMESTAMP not null,
	primary key (uid),
	constraint user_account_phone_uindex
		unique (phone)
)
engine=InnoDB
;



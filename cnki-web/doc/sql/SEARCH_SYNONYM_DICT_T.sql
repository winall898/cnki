-- Create table
create table KMDATA.SEARCH_SYNONYM_DICT_T
(
  id           NUMBER not null,
  keyword      VARCHAR2(200),
  synonym_word VARCHAR2(1000),
  unidir_word  VARCHAR2(1000),
  creater      VARCHAR2(50),
  create_time  DATE,
  updater      VARCHAR2(50),
  update_time  DATE,
  description  VARCHAR2(2000),
  constraint PK_SEARCH_SYNONYM_DICT primary key (id)
);

-- Add comments to the table 
comment on table KMDATA.SEARCH_SYNONYM_DICT_T
  is '同义词词库表';
-- Add comments to the columns 
comment on column KMDATA.SEARCH_SYNONYM_DICT_T.id
  is '主键id';
comment on column KMDATA.SEARCH_SYNONYM_DICT_T.keyword
  is '关键词';
comment on column KMDATA.SEARCH_SYNONYM_DICT_T.synonym_word
  is '双向同义词（多值）';
comment on column KMDATA.SEARCH_SYNONYM_DICT_T.unidir_word
  is '单项同义词（多值）';
comment on column KMDATA.SEARCH_SYNONYM_DICT_T.description
  is '描述'; 

 -- Create sequence 
create sequence KMDATA.SEQ_SEARCH_SYNONYM_DICT_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 100
increment by 1
cache 40;
 
grant select on KMDATA.SEQ_SEARCH_SYNONYM_DICT_ID to r_kmdata_qry;
create public synonym SEQ_SEARCH_SYNONYM_DICT_ID for kmdata.SEQ_SEARCH_SYNONYM_DICT_ID;

grant select on KMDATA.SEARCH_SYNONYM_DICT_T to r_kmdata_qry;
grant insert,update,delete on KMDATA.SEARCH_SYNONYM_DICT_T to r_kmdata_dml;
create public synonym SEARCH_SYNONYM_DICT_T for kmdata.SEARCH_SYNONYM_DICT_T;
  

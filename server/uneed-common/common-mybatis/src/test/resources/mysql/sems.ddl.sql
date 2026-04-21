drop table if exists ud_classes;

drop table if exists ud_exam_score;

drop table if exists ud_student;

drop table if exists ud_teacher;

drop table if exists ud_teacher_classes;

drop table if exists ud_teacher_summary;

drop table if exists ud_exam_subject;

/*==============================================================*/
/* Table: ud_classes                                           */
/*==============================================================*/
create table ud_classes
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            bigint comment '最后更新人',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   class_code           varchar(32) comment '班级编号',
   class_name           varchar(64) comment '班级名称',
   primary key (id)
);

alter table ud_classes comment '班级信息';

/*==============================================================*/
/* Table: ud_exam_score                                        */
/*==============================================================*/
create table ud_exam_score
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            bigint comment '最后更新人',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   student_id           bigint comment '学生id',
   subject              varchar(32) comment '考试科目，取值数据字典：exam_subject，chinese/math/english，语文/数学/英语',
   score                decimal(5,2) comment '考试分数',
   primary key (id)
);

alter table ud_exam_score comment '考试成绩信息';

/*==============================================================*/
/* Table: ud_student                                           */
/*==============================================================*/
create table ud_student
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   update_by            bigint comment '最后更新人',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   stu_code             varchar(32) comment '学生编号',
   stu_name             varchar(64) comment '学生姓名',
   stu_name_py          varchar(128) comment '学生姓名拼音',
   stu_age              int comment '学生年龄',
   stu_sex              varchar(32) comment '学生性别，取值数据字典：gender，0/1/2（未知/男/女）',
   enrolment_time       datetime comment '入学时间',
   classes_id           bigint comment '所属班级',
   primary key (id)
);

alter table ud_student comment '学生信息';

/*==============================================================*/
/* Table: ud_teacher                                           */
/*==============================================================*/
create table ud_teacher
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            bigint comment '最后更新人',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   tea_code             varchar(32) comment '教师编号',
   tea_name             varchar(64) comment '教师姓名',
   tea_age              double comment '教师教龄',
   tea_sex              varchar(32) comment '教师性别，取值数据字典：gender，0/1/2（未知/男/女）',
   primary key (id)
);

alter table ud_teacher comment '教师信息';

/*==============================================================*/
/* Table: ud_teacher_classes                                   */
/*==============================================================*/
create table ud_teacher_classes
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            bigint comment '最后更新人',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   teacher_id           bigint comment '教师id',
   classes_id           bigint comment '班级id',
   primary key (id)
);

alter table ud_teacher_classes comment '教师班级信息';

/*==============================================================*/
/* Table: ud_teacher_summary                                   */
/*==============================================================*/
create table ud_teacher_summary
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            bigint comment '最后更新人',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   teacher_id           bigint comment '教师id',
   summary              text comment '教师简介',
   primary key (id)
);

alter table ud_teacher_summary comment '教师简介信息';

/*==============================================================*/
/* Table: ud_exam_subject                                   */
/*==============================================================*/
create table ud_exam_subject
(
   id                   bigint not null comment '主键',
   create_by            bigint comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            bigint comment '最后更新人',
   update_time          datetime comment '最后更新时间',
   remove_flag          smallint comment '逻辑删除标记，取值0/1,1表示被删除的数据',
   tenant_id            bigint comment '租户id',
   subject_code         varchar(32) comment '科目编号',
   subject_name         varchar(32) comment '科目名称',
   primary key (id)
);

alter table ud_exam_subject comment '考试科目信息';

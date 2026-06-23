
-- ----------------------------
-- Type structure for tablefunc_crosstab_2
-- ----------------------------
DROP TYPE IF EXISTS "MP_IPMS_PA"."tablefunc_crosstab_2";
CREATE TYPE "MP_IPMS_PA"."tablefunc_crosstab_2" AS (
  "row_name" text COLLATE "pg_catalog"."default",
  "category_1" text COLLATE "pg_catalog"."default",
  "category_2" text COLLATE "pg_catalog"."default"
);
ALTER TYPE "MP_IPMS_PA"."tablefunc_crosstab_2" OWNER TO "mindpro";

-- ----------------------------
-- Type structure for tablefunc_crosstab_3
-- ----------------------------
DROP TYPE IF EXISTS "MP_IPMS_PA"."tablefunc_crosstab_3";
CREATE TYPE "MP_IPMS_PA"."tablefunc_crosstab_3" AS (
  "row_name" text COLLATE "pg_catalog"."default",
  "category_1" text COLLATE "pg_catalog"."default",
  "category_2" text COLLATE "pg_catalog"."default",
  "category_3" text COLLATE "pg_catalog"."default"
);
ALTER TYPE "MP_IPMS_PA"."tablefunc_crosstab_3" OWNER TO "mindpro";

-- ----------------------------
-- Type structure for tablefunc_crosstab_4
-- ----------------------------
DROP TYPE IF EXISTS "MP_IPMS_PA"."tablefunc_crosstab_4";
CREATE TYPE "MP_IPMS_PA"."tablefunc_crosstab_4" AS (
  "row_name" text COLLATE "pg_catalog"."default",
  "category_1" text COLLATE "pg_catalog"."default",
  "category_2" text COLLATE "pg_catalog"."default",
  "category_3" text COLLATE "pg_catalog"."default",
  "category_4" text COLLATE "pg_catalog"."default"
);
ALTER TYPE "MP_IPMS_PA"."tablefunc_crosstab_4" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for ipms_user_user_sq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."ipms_user_user_sq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."ipms_user_user_sq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."ipms_user_user_sq_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for seq_product_no
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."seq_product_no";
CREATE SEQUENCE "MP_IPMS_PA"."seq_product_no" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."seq_product_no" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_common_code_code_seq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_common_code_code_seq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_common_code_code_seq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_common_code_code_seq_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_connection_connection_seq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_connection_connection_seq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_connection_connection_seq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_connection_connection_seq_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_document_mst_doc_seq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_document_mst_doc_seq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_document_mst_doc_seq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_document_mst_doc_seq_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_duedate_mst_order_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_duedate_mst_order_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_duedate_mst_order_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_duedate_mst_order_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_group_code_group_seq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_group_code_group_seq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_group_code_group_seq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_group_code_group_seq_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_item_manage_mst_item_seq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_item_manage_mst_item_seq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_item_manage_mst_item_seq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_item_manage_mst_item_seq_seq" OWNER TO "mindpro";

-- ----------------------------
-- Sequence structure for utb_locarno_goods_mst_goods_seq_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "MP_IPMS_PA"."utb_locarno_goods_mst_goods_seq_seq";
CREATE SEQUENCE "MP_IPMS_PA"."utb_locarno_goods_mst_goods_seq_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;
ALTER SEQUENCE "MP_IPMS_PA"."utb_locarno_goods_mst_goods_seq_seq" OWNER TO "mindpro";


-- ----------------------------
-- Table structure for biz_info_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."biz_info_mapp";
CREATE TABLE "MP_IPMS_PA"."biz_info_mapp" (
  "biz_info_mapp_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_role_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."biz_info_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."biz_info_mapp_seq" IS '사업자_사용자_매핑_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."biz_info_seq" IS '사업_정보_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."user_info_seq" IS '사용자_정보_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."biz_role_code" IS '사업자_내_역할_코드 (OWNER, CO_OWNER, EMPLOYEE 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."create_at" IS '생성일시';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."biz_info_mapp"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."biz_info_mapp" IS '사업자-사용자 매핑 테이블';

-- ----------------------------
-- Table structure for ipms_user
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."ipms_user";
CREATE TABLE "MP_IPMS_PA"."ipms_user" (
  "user_sq" int8 NOT NULL DEFAULT nextval('"MP_IPMS_PA".ipms_user_user_sq_seq'::regclass),
  "user_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "user_pw" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "user_role" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'ROLE_USER'::character varying,
  "use_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::bpchar,
  "reg_date" timestamp(6) DEFAULT now(),
  "chg_date" timestamp(6) DEFAULT now()
)
;
ALTER TABLE "MP_IPMS_PA"."ipms_user" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for stb_tlb_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."stb_tlb_code";
CREATE TABLE "MP_IPMS_PA"."stb_tlb_code" (
  "tbl_code_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_name" varchar(100) COLLATE "pg_catalog"."default",
  "tbl_short_name" varchar(30) COLLATE "pg_catalog"."default",
  "tbl_mapping_nm" varchar(25500) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."stb_tlb_code" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."stb_tlb_code"."tbl_code_seq" IS '테이블_코드_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."stb_tlb_code"."tbl_name" IS '테이블_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."stb_tlb_code"."tbl_short_name" IS '테이블_약어_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."stb_tlb_code"."tbl_mapping_nm" IS '테이블_약어_이름';
COMMENT ON TABLE "MP_IPMS_PA"."stb_tlb_code" IS 'STB_테이블_코드';

-- ----------------------------
-- Table structure for tmp_locarno_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."tmp_locarno_code";
CREATE TABLE "MP_IPMS_PA"."tmp_locarno_code" (
  "class_group_cd" varchar(10) COLLATE "pg_catalog"."default",
  "item_nm_kr" text COLLATE "pg_catalog"."default",
  "item_nm_en" text COLLATE "pg_catalog"."default",
  "version_yr" varchar(4) COLLATE "pg_catalog"."default" DEFAULT '2025'::character varying,
  "create_at" timestamptz(6) DEFAULT now()
)
;
ALTER TABLE "MP_IPMS_PA"."tmp_locarno_code" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for tmp_locarno_en
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."tmp_locarno_en";
CREATE TABLE "MP_IPMS_PA"."tmp_locarno_en" (
  "class_group_cd" varchar(10) COLLATE "pg_catalog"."default",
  "item_id" varchar(20) COLLATE "pg_catalog"."default",
  "item_nm_en" text COLLATE "pg_catalog"."default",
  "version_yr" varchar(4) COLLATE "pg_catalog"."default" DEFAULT '2025'::character varying,
  "create_at" timestamptz(6) DEFAULT now()
)
;
ALTER TABLE "MP_IPMS_PA"."tmp_locarno_en" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_app_design
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_design";
CREATE TABLE "MP_IPMS_PA"."utb_app_design" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "multi_view_drawing_file" varchar(30) COLLATE "pg_catalog"."default",
  "design_description" text COLLATE "pg_catalog"."default",
  "design_summary" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" varchar(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "is_partial_design" varchar(1) COLLATE "pg_catalog"."default",
  "multi_design" varchar(5) COLLATE "pg_catalog"."default",
  "design_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_design" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."multi_view_drawing_file" IS '입체_도면_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."design_description" IS '디자인_설명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."design_summary" IS '디자인_요약';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."is_partial_design" IS '부분_디자인_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."multi_design" IS '다의장(복수 디자인 수)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_design"."design_seq" IS '디자인_식별자';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_design" IS '"MP_IPMS_PA".UTB_디자인';

-- ----------------------------
-- Table structure for utb_app_ext_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_ext_mst";
CREATE TABLE "MP_IPMS_PA"."utb_app_ext_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_ext_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "asset_no" varchar(30) COLLATE "pg_catalog"."default",
  "case_code" varchar(30) COLLATE "pg_catalog"."default",
  "dept_code" varchar(30) COLLATE "pg_catalog"."default",
  "individual_country_cnt" int4,
  "individual_country_content" text COLLATE "pg_catalog"."default",
  "pct_cnt" int4,
  "pct_content" text COLLATE "pg_catalog"."default",
  "ep_cnt" int4,
  "ep_content" text COLLATE "pg_catalog"."default",
  "madrid_cnt" int4,
  "madrid_content" text COLLATE "pg_catalog"."default",
  "international_design_cnt" int4,
  "international_design_content" text COLLATE "pg_catalog"."default",
  "grade" varchar(15) COLLATE "pg_catalog"."default",
  "independent_claim_cnt" varchar(5) COLLATE "pg_catalog"."default",
  "dependent_claim_cnt" varchar(5) COLLATE "pg_catalog"."default",
  "spec_cnt" varchar(5) COLLATE "pg_catalog"."default",
  "drawing_cnt" varchar(5) COLLATE "pg_catalog"."default",
  "global_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "representative_file_seq" varchar(30) COLLATE "pg_catalog"."default",
  "give_up_content" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "app_name_ko" varchar(255) COLLATE "pg_catalog"."default",
  "app_name_en" varchar(255) COLLATE "pg_catalog"."default",
  "right_category" varchar(30) COLLATE "pg_catalog"."default",
  "app_kind" varchar(15) COLLATE "pg_catalog"."default",
  "product_class" varchar(30) COLLATE "pg_catalog"."default",
  "case_classification" varchar(5) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_ext_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."case_code" IS '사건 분야 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."dept_code" IS '부서 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."individual_country_cnt" IS '개별국가 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."pct_cnt" IS 'PCT 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."ep_cnt" IS 'EP 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."madrid_cnt" IS '마드리드 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."international_design_cnt" IS '국제디자인 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."grade" IS '등급';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."independent_claim_cnt" IS '독립항 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."dependent_claim_cnt" IS '종속항 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."spec_cnt" IS '명세서 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."drawing_cnt" IS '도면 수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."global_app_no" IS '국제출원번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."representative_file_seq" IS '대표도 파일 SEQ';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."give_up_content" IS '포기내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."app_name_ko" IS '출원 국문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."app_name_en" IS '출원 영문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."right_category" IS '권리구분';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."app_kind" IS '출원종류';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."product_class" IS '물품류';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ext_mst"."case_classification" IS '해외기본사건 관련 코드';

-- ----------------------------
-- Table structure for utb_app_grace_period
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_grace_period";
CREATE TABLE "MP_IPMS_PA"."utb_app_grace_period" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "grace_period_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "grace_period_content" varchar(15) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_grace_period" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."grace_period_seq" IS '공지예외_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."grace_period_content" IS '공지예외_내용 (GRACE_PRD_CONT)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_grace_period"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_grace_period" IS '출원_공지예외';

-- ----------------------------
-- Table structure for utb_app_ids
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_ids";
CREATE TABLE "MP_IPMS_PA"."utb_app_ids" (
  "ids_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "occur_country_code" varchar(5) COLLATE "pg_catalog"."default" NOT NULL,
  "occur_no" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "family_no_en" varchar(30) COLLATE "pg_catalog"."default",
  "is_ids_submitted" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_ids" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."ids_seq" IS 'IDS 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."occur_country_code" IS '발생국가 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."occur_no" IS '발생번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."family_no_en" IS '영문패밀리번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."is_ids_submitted" IS 'IDS 기제출 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."create_user" IS '등록자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."create_at" IS '등록일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."update_user" IS '수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."del_yn" IS '삭제 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_ids"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_ids" IS '출원 IDS 정보';

-- ----------------------------
-- Table structure for utb_app_inventor
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_inventor";
CREATE TABLE "MP_IPMS_PA"."utb_app_inventor" (
  "app_inventor_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "sort" int4 DEFAULT 1,
  "note" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_inventor" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_app_locarno
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_locarno";
CREATE TABLE "MP_IPMS_PA"."utb_app_locarno" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "locarno_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "class_no" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "sub_class_no" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "goods_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "locarno_group_id" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "locarno_name_ko" varchar(500) COLLATE "pg_catalog"."default",
  "locarno_name_en" varchar(500) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "goods_count" varchar(10) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_locarno" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."app_seq" IS '출원식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."office_seq" IS '사무소 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."locarno_seq" IS '로카르노 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."goods_seq" IS '굿즈 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."locarno_group_id" IS '로카르노 그룹 아이디';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."locarno_name_ko" IS '로카르노 한글명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."locarno_name_en" IS '로카르노 영문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_locarno"."goods_count" IS '물품 개수';

-- ----------------------------
-- Table structure for utb_app_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_mst";
CREATE TABLE "MP_IPMS_PA"."utb_app_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "retain_seq" varchar(30) COLLATE "pg_catalog"."default",
  "app_state" varchar(30) COLLATE "pg_catalog"."default",
  "state" varchar(30) COLLATE "pg_catalog"."default" DEFAULT 10,
  "country_code" varchar(30) COLLATE "pg_catalog"."default",
  "right_category" varchar(30) COLLATE "pg_catalog"."default",
  "product_class" varchar(30) COLLATE "pg_catalog"."default",
  "app_name_ko" varchar(255) COLLATE "pg_catalog"."default",
  "app_name_en" varchar(255) COLLATE "pg_catalog"."default",
  "outsourcing_corp_name" varchar(255) COLLATE "pg_catalog"."default",
  "outsourcing_yn" varchar(30) COLLATE "pg_catalog"."default",
  "give_up_content" text COLLATE "pg_catalog"."default",
  "app_category" varchar(15) COLLATE "pg_catalog"."default",
  "app_name" varchar(255) COLLATE "pg_catalog"."default",
  "grade" varchar(15) COLLATE "pg_catalog"."default",
  "independent_claim" text COLLATE "pg_catalog"."default",
  "dependent_claim" text COLLATE "pg_catalog"."default",
  "drawing_paper_count" varchar(30) COLLATE "pg_catalog"."default",
  "ulti_dependent_claim_count" int4,
  "app_no" varchar(30) COLLATE "pg_catalog"."default",
  "original_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "original_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "double_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "global_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "global_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "open_no" varchar(30) COLLATE "pg_catalog"."default",
  "product_class_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "re_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "public_no" varchar(30) COLLATE "pg_catalog"."default",
  "reg_public_no" varchar(30) COLLATE "pg_catalog"."default",
  "madrid_no" varchar(30) COLLATE "pg_catalog"."default",
  "ipc_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "external_app_approach" varchar(30) COLLATE "pg_catalog"."default",
  "year_cnt_management_yn" char(1) COLLATE "pg_catalog"."default",
  "external_app_yn" char(1) COLLATE "pg_catalog"."default",
  "trademark_research_yn" char(1) COLLATE "pg_catalog"."default",
  "renewal_management_yn" char(1) COLLATE "pg_catalog"."default",
  "interior_preference_assert_yn" char(1) COLLATE "pg_catalog"."default",
  "mandate_paper_submit_yn" char(1) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "app_classification" varchar(15) COLLATE "pg_catalog"."default",
  "app_kind" varchar(15) COLLATE "pg_catalog"."default",
  "app_language" varchar(15) COLLATE "pg_catalog"."default",
  "proposal_name" varchar(255) COLLATE "pg_catalog"."default",
  "first_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "foreign_app_timing" varchar(15) COLLATE "pg_catalog"."default",
  "access_code" varchar(30) COLLATE "pg_catalog"."default",
  "annuity_year" varchar(30) COLLATE "pg_catalog"."default",
  "annuity_reduc_rate" varchar(30) COLLATE "pg_catalog"."default",
  "spec_page" varchar(255) COLLATE "pg_catalog"."default",
  "figure_count" varchar(30) COLLATE "pg_catalog"."default",
  "drawing_count" varchar(30) COLLATE "pg_catalog"."default",
  "reg_reduction_rate" varchar(30) COLLATE "pg_catalog"."default",
  "asset_no" varchar(30) COLLATE "pg_catalog"."default",
  "agent_ref" varchar(30) COLLATE "pg_catalog"."default",
  "dept_code" varchar(30) COLLATE "pg_catalog"."default",
  "oversea_spec_page" varchar(30) COLLATE "pg_catalog"."default",
  "is_oversea" varchar(1) COLLATE "pg_catalog"."default",
  "trademark_renewal_fee" varchar(100) COLLATE "pg_catalog"."default",
  "next_payment_installment" varchar(30) COLLATE "pg_catalog"."default",
  "renewal_late_fee" varchar(100) COLLATE "pg_catalog"."default",
  "etc_title" varchar(100) COLLATE "pg_catalog"."default",
  "app_route" varchar(15) COLLATE "pg_catalog"."default",
  "provisional_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "notice_exception_apply" varchar(15) COLLATE "pg_catalog"."default",
  "parent_reg_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "main_drawing_file" varchar(30) COLLATE "pg_catalog"."default",
  "domestic_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "goods_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "complete_20_yn" char(1) COLLATE "pg_catalog"."default",
  "app_20_country" text COLLATE "pg_catalog"."default",
  "complete_30_yn" char(1) COLLATE "pg_catalog"."default",
  "app_30_country" text COLLATE "pg_catalog"."default",
  "kr_designation_yn" char(1) COLLATE "pg_catalog"."default",
  "search_result" varchar(500) COLLATE "pg_catalog"."default",
  "intl_pub_no" varchar(30) COLLATE "pg_catalog"."default",
  "deemed_withdrawal_content" text COLLATE "pg_catalog"."default",
  "designated" text COLLATE "pg_catalog"."default",
  "registered_states" text COLLATE "pg_catalog"."default",
  "subsequent" text COLLATE "pg_catalog"."default",
  "div_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "ep_search_result" varchar(500) COLLATE "pg_catalog"."default",
  "wipo_ref_no" varchar(30) COLLATE "pg_catalog"."default",
  "public_yn" char(1) COLLATE "pg_catalog"."default",
  "deferment_month_count" varchar(30) COLLATE "pg_catalog"."default",
  "pub_no" varchar(30) COLLATE "pg_catalog"."default",
  "authority_ref_no" varchar(30) COLLATE "pg_catalog"."default",
  "payment_installment" varchar(30) COLLATE "pg_catalog"."default",
  "client_name" varchar(100) COLLATE "pg_catalog"."default",
  "applicant_name" varchar(100) COLLATE "pg_catalog"."default",
  "reg_mgr_name" varchar(100) COLLATE "pg_catalog"."default",
  "foreign_agent_name" varchar(100) COLLATE "pg_catalog"."default",
  "case_classification" varchar(5) COLLATE "pg_catalog"."default" NOT NULL,
  "kipo_delay_days" int4
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."retain_seq" IS '의뢰_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_state" IS '출원_상태(STATUS)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."state" IS '상태(???)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."country_code" IS '국가_코드(CITY_SEQ)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."right_category" IS '권리_구분(PAT_TYPE)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."product_class" IS '상품_류';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_name_ko" IS '출원_이름_한글';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_name_en" IS '출원_이름_영어';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."outsourcing_corp_name" IS '외주업체_기업_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."outsourcing_yn" IS '외주업체_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."give_up_content" IS '포기_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_category" IS '출원_구분(APP_DIV_TYPE)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_name" IS '출원_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."grade" IS '등급(IP_GRADE_CD)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."independent_claim" IS '독립항';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."dependent_claim" IS '종속항';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."drawing_paper_count" IS '도면_서류_개수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."ulti_dependent_claim_count" IS '최종항_개수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_no" IS '출원_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."original_app_no" IS '원_출원_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."original_reg_no" IS '원_등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."double_app_no" IS '이중_출원_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."global_app_no" IS '국제_출원_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."global_reg_no" IS '국제_등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."reg_no" IS '등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."open_no" IS '공개_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."product_class_app_no" IS '상품_류_출원_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."re_app_no" IS '재_출원_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."public_no" IS '공고_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."reg_public_no" IS '등록_공고_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."madrid_no" IS '마드리드_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."ipc_category_code" IS 'IPC_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."external_app_approach" IS '해외_출원_방법';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."year_cnt_management_yn" IS '년차_관리_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."external_app_yn" IS '해외_출원_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."trademark_research_yn" IS '상표_조사_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."renewal_management_yn" IS '갱신_관리_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."interior_preference_assert_yn" IS '국내_우선권_주장_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."mandate_paper_submit_yn" IS '위임_서류_제출_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."del_yn" IS '삭제여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_classification" IS '구분(내국/외국/해외 - NAT_IO_DIV)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_kind" IS '출원종류 (APP_TYPE_CD)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_language" IS '출원 언어 분류(APP_LANG_DIV)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."proposal_name" IS '제안명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."first_app_no" IS '최초 출원 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."foreign_app_timing" IS '해외 출원 시점(APP_SUBMIT_DIV)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."access_code" IS '접근코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."annuity_year" IS '연차 차수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."annuity_reduc_rate" IS '연차관리 감면율(REDUC_RATE_CD)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."spec_page" IS '(임시) 명세서 파일첨부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."figure_count" IS '(임시) 도수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."drawing_count" IS '(임시) 도면수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."reg_reduction_rate" IS '(임시) 등록 감면율';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."asset_no" IS 'our_ref';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."agent_ref" IS 'your_ref';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."dept_code" IS '부서_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."oversea_spec_page" IS '해외_명세서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."is_oversea" IS '해외출원 yn(x)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."trademark_renewal_fee" IS '상표_갱신등록료';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."next_payment_installment" IS '차기납부차수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."renewal_late_fee" IS '갱신과태료';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."etc_title" IS '기타표기명칭(디자인)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_route" IS '출원루트(IP_PROC_TYPE)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."provisional_app_no" IS '가출원번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."notice_exception_apply" IS '공지예외적용(해외 개국 디자인 - NOTICE_EXCEPTION)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."parent_reg_app_no" IS '모등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."main_drawing_file" IS '메인도면파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."domestic_reg_no" IS '국내등록번호(기타사건관리 사용)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."goods_app_no" IS '분류출원번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."complete_20_yn" IS '20개월 마감 완료 유무';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_20_country" IS '20개월 마감 지정국가';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."complete_30_yn" IS '30개월 마감 완료 유무';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."app_30_country" IS '30개월 마감 지정국가';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."kr_designation_yn" IS '지정국가(KR 지정유무)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."search_result" IS '국제 조사 결과';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."intl_pub_no" IS '국제공개번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."deemed_withdrawal_content" IS '포기취하내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."designated" IS '지정국가(개국, pct, ep, madrid, intlDesign 공통컬럼)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."registered_states" IS '등록국가(공통 컬럼)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."subsequent" IS '사후 지정국가(추후 지정국가)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."div_app_no" IS '분할출원번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."ep_search_result" IS 'EP 서치결과';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."wipo_ref_no" IS 'WIPO 참조번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."public_yn" IS '출원공개유무';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."deferment_month_count" IS '연기월수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."pub_no" IS '출원공개번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."authority_ref_no" IS '특허청참조번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."payment_installment" IS '갱신차수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."client_name" IS '의뢰인 이름 (여러이름 허용)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."applicant_name" IS '출원인 이름 (여러이름 허용)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."reg_mgr_name" IS '등록권리인 이름 (여러이름 허용)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."foreign_agent_name" IS '해외대리인 이름(여러이름 허용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."case_classification" IS '사건 분류 (국내, 해외, 이심, 고객, 기타 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_mst"."kipo_delay_days" IS '특허청 지연일(일수)';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_mst" IS '"MP_IPMS_PA".UTB_출원_마스터';

-- ----------------------------
-- Table structure for utb_app_mst_history
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_mst_history";
CREATE TABLE "MP_IPMS_PA"."utb_app_mst_history" (
  "app_history_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "retain_seq" varchar(30) COLLATE "pg_catalog"."default",
  "app_state" varchar(30) COLLATE "pg_catalog"."default",
  "state" varchar(30) COLLATE "pg_catalog"."default",
  "country_code" varchar(30) COLLATE "pg_catalog"."default",
  "right_category" varchar(30) COLLATE "pg_catalog"."default",
  "product_class" varchar(30) COLLATE "pg_catalog"."default",
  "app_name_ko" varchar(255) COLLATE "pg_catalog"."default",
  "app_name_en" varchar(255) COLLATE "pg_catalog"."default",
  "outsourcing_corp_name" varchar(30) COLLATE "pg_catalog"."default",
  "outsourcing_yn" varchar(30) COLLATE "pg_catalog"."default",
  "give_up_content" text COLLATE "pg_catalog"."default",
  "app_category" varchar(15) COLLATE "pg_catalog"."default",
  "app_name" varchar(30) COLLATE "pg_catalog"."default",
  "grade" varchar(15) COLLATE "pg_catalog"."default",
  "independent_claim" text COLLATE "pg_catalog"."default",
  "dependent_claim" text COLLATE "pg_catalog"."default",
  "drawing_paper_count" varchar(5) COLLATE "pg_catalog"."default",
  "ulti_dependent_claim_count" int4,
  "app_no" varchar(30) COLLATE "pg_catalog"."default",
  "original_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "original_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "double_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "global_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "global_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "open_no" varchar(30) COLLATE "pg_catalog"."default",
  "product_class_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "re_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "public_no" varchar(30) COLLATE "pg_catalog"."default",
  "reg_public_no" varchar(30) COLLATE "pg_catalog"."default",
  "madrid_no" varchar(30) COLLATE "pg_catalog"."default",
  "ipc_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "external_app_approach" varchar(30) COLLATE "pg_catalog"."default",
  "year_cnt_management_yn" char(1) COLLATE "pg_catalog"."default",
  "external_app_yn" char(1) COLLATE "pg_catalog"."default",
  "trademark_research_yn" char(1) COLLATE "pg_catalog"."default",
  "renewal_management_yn" char(1) COLLATE "pg_catalog"."default",
  "interior_preference_assert_yn" char(1) COLLATE "pg_catalog"."default",
  "mandate_paper_submit_yn" char(1) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "app_classification" varchar(15) COLLATE "pg_catalog"."default",
  "app_kind" varchar(15) COLLATE "pg_catalog"."default",
  "app_language" varchar(15) COLLATE "pg_catalog"."default",
  "proposal_name" varchar(30) COLLATE "pg_catalog"."default",
  "first_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "foreign_app_timing" varchar(15) COLLATE "pg_catalog"."default",
  "access_code" varchar(30) COLLATE "pg_catalog"."default",
  "annuity_year" varchar(5) COLLATE "pg_catalog"."default",
  "annuity_reduc_rate" varchar(5) COLLATE "pg_catalog"."default",
  "spec_page" varchar(255) COLLATE "pg_catalog"."default",
  "figure_count" varchar(5) COLLATE "pg_catalog"."default",
  "drawing_count" varchar(5) COLLATE "pg_catalog"."default",
  "reg_reduction_rate" varchar(5) COLLATE "pg_catalog"."default",
  "asset_no" varchar(30) COLLATE "pg_catalog"."default",
  "agent_ref" varchar(30) COLLATE "pg_catalog"."default",
  "dept_code" varchar(30) COLLATE "pg_catalog"."default",
  "oversea_spec_page" varchar(5) COLLATE "pg_catalog"."default",
  "is_oversea" varchar(1) COLLATE "pg_catalog"."default",
  "trademark_renewal_fee" varchar(100) COLLATE "pg_catalog"."default",
  "next_payment_installment" varchar(5) COLLATE "pg_catalog"."default",
  "renewal_late_fee" varchar(100) COLLATE "pg_catalog"."default",
  "etc_title" varchar(100) COLLATE "pg_catalog"."default",
  "app_route" varchar(15) COLLATE "pg_catalog"."default",
  "provisional_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "notice_exception_apply" varchar(15) COLLATE "pg_catalog"."default",
  "parent_reg_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "main_drawing_file" varchar(30) COLLATE "pg_catalog"."default",
  "domestic_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "goods_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "complete_20_yn" char(1) COLLATE "pg_catalog"."default",
  "app_20_country" text COLLATE "pg_catalog"."default",
  "complete_30_yn" char(1) COLLATE "pg_catalog"."default",
  "app_30_country" text COLLATE "pg_catalog"."default",
  "kr_designation_yn" char(1) COLLATE "pg_catalog"."default",
  "search_result" varchar(500) COLLATE "pg_catalog"."default",
  "intl_pub_no" varchar(30) COLLATE "pg_catalog"."default",
  "deemed_withdrawal_content" text COLLATE "pg_catalog"."default",
  "designated" text COLLATE "pg_catalog"."default",
  "registered_states" text COLLATE "pg_catalog"."default",
  "subsequent" text COLLATE "pg_catalog"."default",
  "div_app_no" varchar(30) COLLATE "pg_catalog"."default",
  "ep_search_result" varchar(500) COLLATE "pg_catalog"."default",
  "wipo_ref_no" varchar(30) COLLATE "pg_catalog"."default",
  "public_yn" char(1) COLLATE "pg_catalog"."default",
  "deferment_month_count" varchar(5) COLLATE "pg_catalog"."default",
  "pub_no" varchar(30) COLLATE "pg_catalog"."default",
  "authority_ref_no" varchar(30) COLLATE "pg_catalog"."default",
  "payment_installment" varchar(5) COLLATE "pg_catalog"."default",
  "right_snapshot" jsonb,
  "kipo_delay_days" int4
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_mst_history" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_app_oa
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_oa";
CREATE TABLE "MP_IPMS_PA"."utb_app_oa" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "oa_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "oa_index" int4,
  "paper_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "oa_paper_file" varchar(30) COLLATE "pg_catalog"."default",
  "dead_line_date" timestamptz(6),
  "retouch_writeyn" varchar(1) COLLATE "pg_catalog"."default",
  "staff" varchar(30) COLLATE "pg_catalog"."default",
  "main_pat_attorney" varchar(30) COLLATE "pg_catalog"."default",
  "original_file" varchar(30) COLLATE "pg_catalog"."default",
  "update_file" varchar(30) COLLATE "pg_catalog"."default",
  "state" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_oa" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."oa_seq" IS 'OA_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."oa_index" IS 'OA_순서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."paper_category_code" IS '서류_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."oa_paper_file" IS 'OA_서류_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."dead_line_date" IS '마감_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."retouch_writeyn" IS '보정서_작성여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."staff" IS '담당자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."main_pat_attorney" IS '메인_변리사';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."original_file" IS '원_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."update_file" IS '수정_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."state" IS '상태';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_oa"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_oa" IS '"MP_IPMS_PA".UTB_APP_OA';

-- ----------------------------
-- Table structure for utb_app_patent
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_patent";
CREATE TABLE "MP_IPMS_PA"."utb_app_patent" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_category" varchar(30) COLLATE "pg_catalog"."default",
  "main_drawing_file" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "summary" text COLLATE "pg_catalog"."default",
  "claim_scope" text COLLATE "pg_catalog"."default",
  "patent_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_patent" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."app_category" IS '출원_구분';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."main_drawing_file" IS '대표_도면_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."summary" IS '요약서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."claim_scope" IS '청구범위';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_patent"."patent_seq" IS '특허/실용신안_식별자';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_patent" IS '"MP_IPMS_PA".UTB_특허';

-- ----------------------------
-- Table structure for utb_app_preference
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_preference";
CREATE TABLE "MP_IPMS_PA"."utb_app_preference" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "preference_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "prior_country_code" varchar(30) COLLATE "pg_catalog"."default",
  "preference_assert_date" timestamptz(6),
  "preference_no" varchar(30) COLLATE "pg_catalog"."default",
  "wipo_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "preference_search" varchar(30) COLLATE "pg_catalog"."default",
  "full_content_url" text COLLATE "pg_catalog"."default",
  "reg_date" varchar(30) COLLATE "pg_catalog"."default",
  "submit_dead_line_date" timestamptz(6),
  "submit_closing_date" timestamptz(6),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_preference" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."preference_seq" IS '우선권_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."prior_country_code" IS '우선_국가_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."preference_assert_date" IS '우선권_주장_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."preference_no" IS '우선권_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."wipo_category_code" IS 'WIPO_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."preference_search" IS '우선권_조회';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."full_content_url" IS '전문내용_url';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."reg_date" IS '등록_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."submit_dead_line_date" IS '제출_마감_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."submit_closing_date" IS '제출_종료_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_preference"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_preference" IS '출원_우선권';

-- ----------------------------
-- Table structure for utb_app_product
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_product";
CREATE TABLE "MP_IPMS_PA"."utb_app_product" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "product_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "product_group_id" varchar(200) COLLATE "pg_catalog"."default",
  "product_class" varchar(30) COLLATE "pg_catalog"."default",
  "product_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "product_count" int4,
  "product_name_ko" varchar(100) COLLATE "pg_catalog"."default",
  "product_name_en" varchar(100) COLLATE "pg_catalog"."default",
  "nice_version" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_product" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_seq" IS '지정상품_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_group_id" IS '상품_그룹_ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_class" IS '상품_류';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_id" IS '상품_ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_count" IS '상품_개수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_name_ko" IS '상품_이름_한글';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."product_name_en" IS '상품_이름_영어';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."nice_version" IS '나이스_분류_버전';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_product"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_product" IS '출원 상품 정보 테이블';

-- ----------------------------
-- Table structure for utb_app_rnd
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_rnd";
CREATE TABLE "MP_IPMS_PA"."utb_app_rnd" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "rnd_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "project_no" varchar(30) COLLATE "pg_catalog"."default",
  "research_no" varchar(30) COLLATE "pg_catalog"."default",
  "rnd_name" varchar(500) COLLATE "pg_catalog"."default",
  "rnd_start_date" timestamptz(6),
  "rnd_closing_date" timestamptz(6),
  "main_lab" varchar(255) COLLATE "pg_catalog"."default",
  "performing_lab" varchar(255) COLLATE "pg_catalog"."default",
  "share_ratio" numeric(5,2),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "ministry_name" varchar(50) COLLATE "pg_catalog"."default",
  "agency_name" varchar(100) COLLATE "pg_catalog"."default",
  "biz_name" varchar(100) COLLATE "pg_catalog"."default",
  "total_rnd_cost" varchar(50) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_rnd" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."rnd_seq" IS '연구과제_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."project_no" IS '프로젝트_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."research_no" IS '연구_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."rnd_name" IS '연구과제_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."rnd_start_date" IS '연구과제_시작_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."rnd_closing_date" IS '연구과제_종료_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."main_lab" IS '대표_연구소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."performing_lab" IS '수행_연구소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."share_ratio" IS '지분_비율';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."ministry_name" IS '국가부처명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."agency_name" IS '과제관리(전문)기관';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."biz_name" IS '연구사업명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_rnd"."total_rnd_cost" IS '연구비 총액';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_rnd" IS '출원_연구과제';

-- ----------------------------
-- Table structure for utb_app_specification
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_specification";
CREATE TABLE "MP_IPMS_PA"."utb_app_specification" (
  "specification_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "invention_name" varchar(255) COLLATE "pg_catalog"."default",
  "tech_field" varchar(30) COLLATE "pg_catalog"."default",
  "background_tech" text COLLATE "pg_catalog"."default",
  "problem" text COLLATE "pg_catalog"."default",
  "patent_claim_range" text COLLATE "pg_catalog"."default",
  "invention_effect" text COLLATE "pg_catalog"."default",
  "drawing_description" text COLLATE "pg_catalog"."default",
  "invention_content" text COLLATE "pg_catalog"."default",
  "drawing_file" varchar(30) COLLATE "pg_catalog"."default",
  "summary_file" varchar(30) COLLATE "pg_catalog"."default",
  "state" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_specification" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."specification_seq" IS '명세서_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."invention_name" IS '발명_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."tech_field" IS '기술_분야';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."background_tech" IS '배경_기술';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."problem" IS '해결과제';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."patent_claim_range" IS '특허_청구_범위';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."invention_effect" IS '발명_효과';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."drawing_description" IS '도면_설명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."invention_content" IS '발명_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."drawing_file" IS '도면_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."summary_file" IS '요약_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."state" IS '상태';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_specification"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_specification" IS '출원_명세서';

-- ----------------------------
-- Table structure for utb_app_trademark
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_app_trademark";
CREATE TABLE "MP_IPMS_PA"."utb_app_trademark" (
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "trademark_image_file" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "trademark_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_app_trademark" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."trademark_image_file" IS '상표_이미지_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_app_trademark"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_app_trademark" IS '상표_출원';

-- ----------------------------
-- Table structure for utb_appr_doc
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_appr_doc";
CREATE TABLE "MP_IPMS_PA"."utb_appr_doc" (
  "doc_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "form_template_seq" varchar(30) COLLATE "pg_catalog"."default",
  "doc_no" varchar(100) COLLATE "pg_catalog"."default",
  "doc_title" varchar(500) COLLATE "pg_catalog"."default",
  "doc_content" text COLLATE "pg_catalog"."default",
  "doc_status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'DRAFT'::character varying,
  "drafter_seq" varchar(30) COLLATE "pg_catalog"."default",
  "draft_dept_seq" varchar(30) COLLATE "pg_catalog"."default",
  "submit_at" timestamptz(6),
  "complete_at" timestamptz(6),
  "create_user" varchar(200) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(200) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT now(),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" varchar(500) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_appr_doc" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_appr_doc_line
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_appr_doc_line";
CREATE TABLE "MP_IPMS_PA"."utb_appr_doc_line" (
  "line_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "doc_seq" varchar(30) COLLATE "pg_catalog"."default",
  "step_order" varchar(10) COLLATE "pg_catalog"."default",
  "step_name" varchar(100) COLLATE "pg_catalog"."default",
  "step_type" varchar(30) COLLATE "pg_catalog"."default",
  "approver_seq" varchar(30) COLLATE "pg_catalog"."default",
  "approver_name" varchar(100) COLLATE "pg_catalog"."default",
  "approver_type" varchar(20) COLLATE "pg_catalog"."default",
  "line_status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'PENDING'::character varying,
  "action_at" timestamptz(6),
  "action_comment" varchar(500) COLLATE "pg_catalog"."default",
  "create_user" varchar(200) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(200) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT now(),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" varchar(500) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_appr_doc_line" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_appr_doc_target
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_appr_doc_target";
CREATE TABLE "MP_IPMS_PA"."utb_appr_doc_target" (
  "target_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "doc_seq" varchar(30) COLLATE "pg_catalog"."default",
  "target_role" varchar(20) COLLATE "pg_catalog"."default",
  "target_type" varchar(20) COLLATE "pg_catalog"."default",
  "ref_seq" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(200) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(200) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT now(),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" varchar(500) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_appr_doc_target" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_appr_template
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_appr_template";
CREATE TABLE "MP_IPMS_PA"."utb_appr_template" (
  "appr_template_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "template_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_ord" varchar(10) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_appr_template" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_appr_template_line
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_appr_template_line";
CREATE TABLE "MP_IPMS_PA"."utb_appr_template_line" (
  "template_line_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "appr_template_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "step_order" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "step_type" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "step_name" varchar(100) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "approver_type" varchar(30) COLLATE "pg_catalog"."default",
  "approver_ref_seq" varchar(20) COLLATE "pg_catalog"."default",
  "approver_name" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_appr_template_line" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_biz_info
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_biz_info";
CREATE TABLE "MP_IPMS_PA"."utb_biz_info" (
  "biz_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "corp_code" varchar(30) COLLATE "pg_catalog"."default",
  "ceo_name" varchar(30) COLLATE "pg_catalog"."default",
  "biz_reg_file" varchar(200) COLLATE "pg_catalog"."default",
  "biz_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "biz_corp_name" varchar(30) COLLATE "pg_catalog"."default",
  "biz_addr" varchar(255) COLLATE "pg_catalog"."default",
  "biz_addr_detail" varchar(255) COLLATE "pg_catalog"."default",
  "biz_fax_no" varchar(100) COLLATE "pg_catalog"."default",
  "biz_tel_no" varchar(100) COLLATE "pg_catalog"."default",
  "biz_post_no" varchar(100) COLLATE "pg_catalog"."default",
  "biz_type" varchar(30) COLLATE "pg_catalog"."default",
  "biz_kind" varchar(30) COLLATE "pg_catalog"."default",
  "reg_discount_code" varchar(30) COLLATE "pg_catalog"."default",
  "discount_closing_date" timestamptz(6),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "year_discount_code" varchar(30) COLLATE "pg_catalog"."default",
  "discount_ratio_code" varchar(30) COLLATE "pg_catalog"."default",
  "discount_start_date" varchar(30) COLLATE "pg_catalog"."default",
  "biz_workplace_no" varchar(100) COLLATE "pg_catalog"."default",
  "biz_email" varchar(100) COLLATE "pg_catalog"."default",
  "biz_dept_name" varchar(100) COLLATE "pg_catalog"."default",
  "biz_contact_name" varchar(100) COLLATE "pg_catalog"."default",
  "reduction_reason" varchar(500) COLLATE "pg_catalog"."default",
  "reduction_issue_date" timestamptz(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_biz_info" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_info_seq" IS '사업_정보_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."corp_code" IS '기업_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."ceo_name" IS '대표자_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_reg_file" IS '사업자등록증파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_reg_no" IS '사업_등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_corp_name" IS '사업_기업_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_addr" IS '사업_주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_addr_detail" IS '사업_주소_상세';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_fax_no" IS '사업_팩스_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_tel_no" IS '사업_유선전화_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_post_no" IS '사업_우편_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_type" IS '업태';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."biz_kind" IS '업종';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."reg_discount_code" IS '등록_감면_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."discount_closing_date" IS '감면_종료_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_biz_info"."year_discount_code" IS '연차_감면_코드';
COMMENT ON TABLE "MP_IPMS_PA"."utb_biz_info" IS '"MP_IPMS_PA".UTB_사업_정보';

-- ----------------------------
-- Table structure for utb_board_backup_hist
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_backup_hist";
CREATE TABLE "MP_IPMS_PA"."utb_board_backup_hist" (
  "backup_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "config_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "board_name" varchar(200) COLLATE "pg_catalog"."default",
  "status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'REQUEST'::character varying,
  "file_path" varchar(500) COLLATE "pg_catalog"."default",
  "file_size" int8 DEFAULT 0,
  "file_name" varchar(300) COLLATE "pg_catalog"."default",
  "del_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar,
  "create_user" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_at" timestamp(6) NOT NULL DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6),
  "file_seq" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_backup_hist" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."backup_seq" IS '백업 시퀀스 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."office_seq" IS '사무소 시퀀스 (FK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."config_seq" IS '대상 게시판 설정 시퀀스';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."board_name" IS '게시판 명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."status" IS '백업 상태 (REQUEST, PROCESSING, COMPLETED, FAILED)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."file_path" IS '백업 파일 경로';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."file_size" IS '파일 크기 (Byte)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."file_name" IS '파일 명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_backup_hist"."file_seq" IS '파일 마스터 일련번호 (utb_file_mst FK)';
COMMENT ON TABLE "MP_IPMS_PA"."utb_board_backup_hist" IS '게시판 데이터 백업 이력';

-- ----------------------------
-- Table structure for utb_board_config_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_config_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_board_config_mapp" (
  "config_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "master_user_id" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now()
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_config_mapp" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_board_config_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_config_mst";
CREATE TABLE "MP_IPMS_PA"."utb_board_config_mst" (
  "config_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_seq" varchar(30) COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "board_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "board_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(1000) COLLATE "pg_catalog"."default",
  "share_scope" varchar(10) COLLATE "pg_catalog"."default",
  "admin_write_only_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "notice_auth_type" varchar(10) COLLATE "pg_catalog"."default" DEFAULT 'ALL'::character varying,
  "new_post_alert_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::bpchar,
  "use_reaction_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::bpchar,
  "prefix_tags" text COLLATE "pg_catalog"."default",
  "view_type" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'LIST'::character varying,
  "status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'ACTIVE'::character varying,
  "disp_ord" int4 DEFAULT 0,
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT now()
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_config_mst" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_board_config_mst_backup
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_config_mst_backup";
CREATE TABLE "MP_IPMS_PA"."utb_board_config_mst_backup" (
  "config_seq" varchar(30) COLLATE "pg_catalog"."default",
  "parent_seq" varchar(30) COLLATE "pg_catalog"."default",
  "board_type" varchar(20) COLLATE "pg_catalog"."default",
  "board_name" varchar(100) COLLATE "pg_catalog"."default",
  "description" varchar(1000) COLLATE "pg_catalog"."default",
  "share_scope" varchar(10) COLLATE "pg_catalog"."default",
  "admin_write_only_yn" char(1) COLLATE "pg_catalog"."default",
  "notice_auth_type" varchar(10) COLLATE "pg_catalog"."default",
  "new_post_alert_yn" char(1) COLLATE "pg_catalog"."default",
  "use_reaction_yn" char(1) COLLATE "pg_catalog"."default",
  "prefix_tags" text COLLATE "pg_catalog"."default",
  "view_type" varchar(20) COLLATE "pg_catalog"."default",
  "status" varchar(20) COLLATE "pg_catalog"."default",
  "disp_ord" int4,
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_config_mst_backup" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_board_config_target
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_config_target";
CREATE TABLE "MP_IPMS_PA"."utb_board_config_target" (
  "target_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "config_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "target_role" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "target_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "ref_seq" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_config_target" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_board_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_mst";
CREATE TABLE "MP_IPMS_PA"."utb_board_mst" (
  "board_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "category_code" varchar(30) COLLATE "pg_catalog"."default",
  "title" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "view_count" int4 DEFAULT 0,
  "is_pinned" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "pinned_start_at" varchar(8) COLLATE "pg_catalog"."default",
  "pinned_end_at" varchar(8) COLLATE "pg_catalog"."default",
  "tags" varchar(200) COLLATE "pg_catalog"."default",
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "post_status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'PUBLISHED'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_mst"."is_pinned" IS '상단 고정 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_mst"."pinned_start_at" IS '고정 노출 시작일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_board_mst"."pinned_end_at" IS '고정 노출 종료일';
COMMENT ON TABLE "MP_IPMS_PA"."utb_board_mst" IS '게시판 마스터';

-- ----------------------------
-- Table structure for utb_board_system_config
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_board_system_config";
CREATE TABLE "MP_IPMS_PA"."utb_board_system_config" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "max_file_size" int4 DEFAULT 500,
  "max_body_size" int4 DEFAULT 50,
  "trash_retention_days" varchar(10) COLLATE "pg_catalog"."default" DEFAULT '30'::character varying,
  "allow_master_change_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::bpchar,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6) DEFAULT now(),
  "main_layout_type" varchar(50) COLLATE "pg_catalog"."default",
  "recent_post_count" int4,
  "new_badge_duration" int4,
  "show_pin_on_top_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::bpchar
)
;
ALTER TABLE "MP_IPMS_PA"."utb_board_system_config" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_code_dtl
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_code_dtl";
CREATE TABLE "MP_IPMS_PA"."utb_code_dtl" (
  "code_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT "MP_IPMS_PA".fn_get_dynamic_seq('utb_code_dtl'::character varying),
  "grp_cd" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "dtl_cd" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "cd_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "kipo_cd" varchar(20) COLLATE "pg_catalog"."default",
  "ref_val_1" varchar(100) COLLATE "pg_catalog"."default",
  "ref_val_2" varchar(100) COLLATE "pg_catalog"."default",
  "disp_ord" int4 DEFAULT 0,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "note" text COLLATE "pg_catalog"."default",
  "use_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'Y'::bpchar
)
;
ALTER TABLE "MP_IPMS_PA"."utb_code_dtl" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."code_seq" IS '코드 상세 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."grp_cd" IS '소속 그룹 코드 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."dtl_cd" IS '상세 코드 값 (예: 100, 200)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."cd_nm" IS '상세 코드명 (한글 설명)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."kipo_cd" IS '특허청(KIPO) 연동용 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."ref_val_1" IS '참조값 1 (추가 속성값)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."ref_val_2" IS '참조값 2 (추가 속성값)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."disp_ord" IS '그룹 내 출력 순서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."create_user" IS '최초 생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."update_at" IS '최종 수정 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."del_yn" IS '삭제 여부 (N: 사용, Y: 삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_dtl"."note" IS '개별 코드에 대한 비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_code_dtl" IS '공통 코드 상세: 마스터 그룹에 속한 개별 코드 값을 관리합니다.';

-- ----------------------------
-- Table structure for utb_code_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_code_mst";
CREATE TABLE "MP_IPMS_PA"."utb_code_mst" (
  "code_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT "MP_IPMS_PA".fn_get_dynamic_seq('utb_code_mst'::character varying),
  "grp_cd" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "cd_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "disp_ord" int4 DEFAULT 0,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "note" text COLLATE "pg_catalog"."default",
  "use_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'Y'::bpchar
)
;
ALTER TABLE "MP_IPMS_PA"."utb_code_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."code_seq" IS '코드 마스터 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."grp_cd" IS '그룹 코드 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."cd_nm" IS '그룹 코드명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."disp_ord" IS '화면 출력 순서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."create_user" IS '최초 생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."update_at" IS '최종 수정 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."del_yn" IS '삭제 여부 (N: 사용, Y: 삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_code_mst"."note" IS '그룹 코드에 대한 상세 설명 및 비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_code_mst" IS '공통 코드 마스터: 시스템에서 사용하는 코드 그룹을 정의합니다.';

-- ----------------------------
-- Table structure for utb_comment
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_comment";
CREATE TABLE "MP_IPMS_PA"."utb_comment" (
  "comment_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_comment_seq" varchar(30) COLLATE "pg_catalog"."default",
  "comment_type" varchar(30) COLLATE "pg_catalog"."default",
  "comment_content" text COLLATE "pg_catalog"."default",
  "comment_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_comment" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."comment_seq" IS '댓글_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."parent_comment_seq" IS '상위_댓글_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."comment_type" IS '댓글_타입';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."comment_content" IS '댓글_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."comment_code" IS '댓글_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_comment" IS '"MP_IPMS_PA".UTB_댓글';

-- ----------------------------
-- Table structure for utb_comment_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_comment_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_comment_mapp" (
  "mapping_comment_seq" timestamptz(6) NOT NULL,
  "board_category_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "board_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "comment_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_comment_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."mapping_comment_seq" IS '댓글_매핑_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."board_category_code" IS '게시판_카테고리_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."board_seq" IS '게시판_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."comment_seq" IS '댓글_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_comment_mapp"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_comment_mapp" IS '매핑_댓글';

-- ----------------------------
-- Table structure for utb_common_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_common_code";
CREATE TABLE "MP_IPMS_PA"."utb_common_code" (
  "code_seq" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "code_name" varchar(30) COLLATE "pg_catalog"."default",
  "del_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "group_seq" int8,
  "order" int4
)
;
ALTER TABLE "MP_IPMS_PA"."utb_common_code" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."code_seq" IS '공통코드SEQ';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."code" IS '코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."code_name" IS '코드명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."del_yn" IS '삭제여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."create_at" IS '생성일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."update_at" IS '수정일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."group_seq" IS '그룹코드SEQ';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_common_code"."order" IS '순서';

-- ----------------------------
-- Table structure for utb_conflict_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_conflict_mst";
CREATE TABLE "MP_IPMS_PA"."utb_conflict_mst" (
  "conflict_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default",
  "litigation_case_no" varchar(30) COLLATE "pg_catalog"."default",
  "court_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "status" varchar(30) COLLATE "pg_catalog"."default" DEFAULT 10,
  "petitioner_type" varchar(10) COLLATE "pg_catalog"."default",
  "petitioner_memo" text COLLATE "pg_catalog"."default",
  "respondent_type" varchar(10) COLLATE "pg_catalog"."default",
  "respondent_memo" text COLLATE "pg_catalog"."default",
  "pre_exam_result" varchar(100) COLLATE "pg_catalog"."default",
  "final_result" varchar(100) COLLATE "pg_catalog"."default",
  "decision_content" text COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6),
  "appeal_content" text COLLATE "pg_catalog"."default",
  "abandon_yn" varchar(1) COLLATE "pg_catalog"."default",
  "agent_category_code" varchar(100) COLLATE "pg_catalog"."default",
  "abandon_content" text COLLATE "pg_catalog"."default",
  "case_type_code" varchar(30) COLLATE "pg_catalog"."default",
  "dept_name" varchar(30) COLLATE "pg_catalog"."default",
  "etc_yn" varchar(1) COLLATE "pg_catalog"."default",
  "case_title_ko" varchar(255) COLLATE "pg_catalog"."default",
  "asset_no" varchar(255) COLLATE "pg_catalog"."default",
  "agent_ref" varchar(255) COLLATE "pg_catalog"."default",
  "respondent_name" varchar(255) COLLATE "pg_catalog"."default",
  "case_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "introducer" varchar(255) COLLATE "pg_catalog"."default",
  "case_classification" varchar(5) COLLATE "pg_catalog"."default" NOT NULL,
  "foreign_agent" varchar(255) COLLATE "pg_catalog"."default",
  "client" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_conflict_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."conflict_seq" IS '이의심판 관리 번호(PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."office_seq" IS '소속 사무소 식별 코드(PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."app_seq" IS '연관된 출원 마스터 테이블(utb_app_mst) 시퀀스';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."litigation_case_no" IS '심판용 사건번호 (예: 2026-당-1234)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."court_category_code" IS '심판 종류를 정의하는 공통 코드(CODE:COURT_TYPE)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."status" IS '현재 심판 진행 상태 (진행/포기/완료 등)CODE:STATUS';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."petitioner_type" IS '청구인 측의 소송 지위 (원고/피고 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."petitioner_memo" IS '청구인(원고) 측에 대한 담당자 전용 내부 메모';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."respondent_type" IS '피청구인 측의 소송 지위 (원고/피고 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."respondent_memo" IS '피청구인(피고) 측에 대한 담당자 전용 내부 메모';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."pre_exam_result" IS '심사전치 단계의 처리 결과 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."final_result" IS '사건의 최종 종결 결과 상태';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."decision_content" IS '심판관의 최종 결정 또는 판결문 핵심 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."note" IS '심판 사건 전체에 대한 종합 비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."appeal_content" IS '불복제기 코멘트 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."agent_category_code" IS 'CODE:AGENT_TYPE';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."case_type_code" IS 'CODE:CASE_TYPE_TRIAL';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."respondent_name" IS '상대방이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."case_category_code" IS 'NAT_IO_DIV';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."case_classification" IS '사건구분 (CASE_CLASSIFICATION)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."foreign_agent" IS '해외대리인 명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_mst"."client" IS '의뢰인 명칭';
COMMENT ON TABLE "MP_IPMS_PA"."utb_conflict_mst" IS '이의심판 마스터 테이블 (고유 정보 관리용)';

-- ----------------------------
-- Table structure for utb_conflict_result
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_conflict_result";
CREATE TABLE "MP_IPMS_PA"."utb_conflict_result" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "conflict_result_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "conflict_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "judgment_case_no" varchar(255) COLLATE "pg_catalog"."default",
  "judgment_content" text COLLATE "pg_catalog"."default",
  "judgment_search_url" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "judgment_category_code" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_conflict_result" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_conflict_result"."judgment_category_code" IS '판결코드';

-- ----------------------------
-- Table structure for utb_connection
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_connection";
CREATE TABLE "MP_IPMS_PA"."utb_connection" (
  "connection_seq" int8 NOT NULL DEFAULT nextval('"MP_IPMS_PA".utb_connection_connection_seq_seq'::regclass),
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "ai_type" varchar(50) COLLATE "pg_catalog"."default",
  "ai_base_url" varchar(255) COLLATE "pg_catalog"."default",
  "ai_model_nm" varchar(100) COLLATE "pg_catalog"."default",
  "ai_api_key" text COLLATE "pg_catalog"."default",
  "ai_temperature" numeric(3,1),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar
)
;
ALTER TABLE "MP_IPMS_PA"."utb_connection" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_contry_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_contry_code";
CREATE TABLE "MP_IPMS_PA"."utb_contry_code" (
  "ctry_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "ctry_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "ctry_ko_nm" varchar(100) COLLATE "pg_catalog"."default",
  "ctry_en_nm" varchar(100) COLLATE "pg_catalog"."default",
  "org_ind" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "ids_required" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar,
  "pct_entry_months" int4,
  "annuity_base_date" varchar(10) COLLATE "pg_catalog"."default",
  "annuity_cycle_years" int4 DEFAULT 1,
  "official_url" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_contry_code" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."ctry_seq" IS '국가 순번 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."ctry_code" IS '국가 코드 (ISO 2자리, 예: KR, US)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."ctry_ko_nm" IS '국가 국문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."ctry_en_nm" IS '국가 영문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."org_ind" IS '국가/기구 구분 (O: 기구, C: 국가)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."ids_required" IS 'IDS(정보개시진술서) 제출 필수 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."pct_entry_months" IS 'PCT 국내단계 진입 기한 (일)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."annuity_base_date" IS '연차료 기산일 기준(FD (Filing Date): 출원일, GD (Grant Date): 등록/설정일)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."annuity_cycle_years" IS '연차료 납부 주기 (단위: 년)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_contry_code"."official_url" IS '해당 국 특허청 공식 웹사이트 주소';
COMMENT ON TABLE "MP_IPMS_PA"."utb_contry_code" IS '국가 및 국제기구 코드 관리 테이블';

-- ----------------------------
-- Table structure for utb_cost_external
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_cost_external";
CREATE TABLE "MP_IPMS_PA"."utb_cost_external" (
  "external_cost_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "cost_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "outgoing_country_code" varchar(30) COLLATE "pg_catalog"."default",
  "incoming_country_code" varchar(30) COLLATE "pg_catalog"."default",
  "external_office_code" varchar(30) COLLATE "pg_catalog"."default",
  "external_invoiceno" varchar(30) COLLATE "pg_catalog"."default",
  "external_invoice_file" varchar(30) COLLATE "pg_catalog"."default",
  "currency" varchar(5) COLLATE "pg_catalog"."default",
  "exchange_date" timestamptz(6),
  "exchange_ratio" int4,
  "krw_amount" int4,
  "exchange_cost" int4,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_cost_external" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."external_cost_seq" IS '해외_비용_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."cost_seq" IS '비용_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."outgoing_country_code" IS '아웃고잉_국가_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."incoming_country_code" IS '인커밍_국가_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."external_office_code" IS '해외_사무소_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."external_invoiceno" IS '해외_송장번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."external_invoice_file" IS '해외_송장_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."currency" IS '통화';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."exchange_date" IS '환전_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."exchange_ratio" IS '환전_비율';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."krw_amount" IS '원화_금액';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."exchange_cost" IS '환전_비용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_external"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_cost_external" IS '국외_비용';

-- ----------------------------
-- Table structure for utb_cost_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_cost_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_cost_mapp" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "mapping_cost_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "cost_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'UNKNOWN'::character varying,
  "cost_category_code" varchar(50) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_cost_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."mapping_cost_seq" IS '비용_매핑_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."tbl_seq" IS '단위업무_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."cost_seq" IS '비용_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mapp"."tbl_code" IS '업무구분코드';
COMMENT ON TABLE "MP_IPMS_PA"."utb_cost_mapp" IS '비용_업무_매핑';

-- ----------------------------
-- Table structure for utb_cost_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_cost_mst";
CREATE TABLE "MP_IPMS_PA"."utb_cost_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "cost_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "cost_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "krw_amount" numeric(15,0),
  "discount_ratio" int4,
  "remittance_count" int4,
  "cost_date" timestamptz(6),
  "cost_remittance_count" varchar(30) COLLATE "pg_catalog"."default",
  "cost_remittance_date" timestamptz(6),
  "cost_remittance_yn" char(1) COLLATE "pg_catalog"."default",
  "cost_fee" numeric(15,0),
  "cost_vat" numeric(15,0),
  "payment_div" varchar(20) COLLATE "pg_catalog"."default",
  "app_no" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "mapping_cost_seq" varchar(20) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_cost_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_seq" IS '비용_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_category_code" IS '비용_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."krw_amount" IS '원화_금액';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."discount_ratio" IS '감면_비율';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."remittance_count" IS '납부_횟수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_date" IS '비용_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_remittance_count" IS '비용_납부_횟수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_remittance_date" IS '비용_납부_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_remittance_yn" IS '비용_납부_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_fee" IS '비용_수수료';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."cost_vat" IS '비용_부가세';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_cost_mst"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_cost_mst" IS '"MP_IPMS_PA".UTB_비용_마스터';

-- ----------------------------
-- Table structure for utb_customer
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_customer";
CREATE TABLE "MP_IPMS_PA"."utb_customer" (
  "customer_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_info_seq" varchar(30) COLLATE "pg_catalog"."default",
  "client_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "applicant_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "corp_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "attorney_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "client_name_ko" varchar(100) COLLATE "pg_catalog"."default",
  "client_name_en" varchar(255) COLLATE "pg_catalog"."default",
  "client_name_ch" varchar(255) COLLATE "pg_catalog"."default",
  "client_name_jp" varchar(255) COLLATE "pg_catalog"."default",
  "company_name" varchar(255) COLLATE "pg_catalog"."default",
  "dept_name" varchar(100) COLLATE "pg_catalog"."default",
  "customer_position" varchar(100) COLLATE "pg_catalog"."default",
  "app_zip_code" varchar(20) COLLATE "pg_catalog"."default",
  "app_address" text COLLATE "pg_catalog"."default",
  "app_tel" varchar(30) COLLATE "pg_catalog"."default",
  "app_fax" varchar(30) COLLATE "pg_catalog"."default",
  "contact_zip_code" varchar(20) COLLATE "pg_catalog"."default",
  "contact_address" text COLLATE "pg_catalog"."default",
  "contact_person" varchar(100) COLLATE "pg_catalog"."default",
  "contact_tel" varchar(30) COLLATE "pg_catalog"."default",
  "contact_fax" varchar(30) COLLATE "pg_catalog"."default",
  "etc_zip_code" varchar(20) COLLATE "pg_catalog"."default",
  "etc_address" text COLLATE "pg_catalog"."default",
  "etc_tel" varchar(30) COLLATE "pg_catalog"."default",
  "etc_fax" varchar(30) COLLATE "pg_catalog"."default",
  "oversea_zip_code" varchar(20) COLLATE "pg_catalog"."default",
  "oversea_address" text COLLATE "pg_catalog"."default",
  "oversea_tel" varchar(30) COLLATE "pg_catalog"."default",
  "oversea_fax" varchar(30) COLLATE "pg_catalog"."default",
  "country_code" varchar(30) COLLATE "pg_catalog"."default",
  "resident_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "corp_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "kipo_client_no" varchar(30) COLLATE "pg_catalog"."default",
  "manager_name" varchar(100) COLLATE "pg_catalog"."default",
  "general_mandate_no" varchar(30) COLLATE "pg_catalog"."default",
  "mobile" varchar(30) COLLATE "pg_catalog"."default",
  "homepage" varchar(255) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "registration_date" timestamptz(6),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "case_classification" varchar(5) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_customer" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."customer_seq" IS '고객정보 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."office_seq" IS '사무소 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."biz_info_seq" IS '사업자정보 시퀀스';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."client_category_code" IS '고객구분 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."applicant_category_code" IS '출원인구분 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."corp_category_code" IS '기업구분 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."attorney_category_code" IS '변리사구분 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."client_name_ko" IS '고객 한글명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."client_name_en" IS '고객 영문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."client_name_ch" IS '고객 한자명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."client_name_jp" IS '고객 일문명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."company_name" IS '회사명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."dept_name" IS '부서명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."customer_position" IS '직책';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."app_zip_code" IS '출원 우편번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."app_address" IS '출원 주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."app_tel" IS '출원 전화번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."app_fax" IS '출원 팩스번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."contact_zip_code" IS '연락처 우편번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."contact_address" IS '연락처 주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."contact_person" IS '연락받는 사람';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."contact_tel" IS '연락처 전화번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."contact_fax" IS '연락처 팩스번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."etc_zip_code" IS '기타 우편번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."etc_address" IS '기타 주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."etc_tel" IS '기타 전화번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."etc_fax" IS '기타 팩스번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."oversea_zip_code" IS '해외 우편번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."oversea_address" IS '해외 주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."oversea_tel" IS '해외 전화번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."oversea_fax" IS '해외 팩스번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."country_code" IS '국가 코드 (공통코드 참조)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."resident_reg_no" IS '주민등록번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."corp_reg_no" IS '법인등록번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."kipo_client_no" IS '특허청 고객번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."manager_name" IS '담당 관리자명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."general_mandate_no" IS '포괄위임번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."mobile" IS '휴대폰 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."homepage" IS '홈페이지 주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."email" IS '이메일 주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."registration_date" IS '고객 등록일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."create_at" IS '생성일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."del_yn" IS '삭제 여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer"."case_classification" IS '사건구분 (국내출원, 해외출원, 이의심판, 고객 등)';
COMMENT ON TABLE "MP_IPMS_PA"."utb_customer" IS '고객 기본 정보 테이블 (인적사항, 출원/연락처, 구분코드 관리)';

-- ----------------------------
-- Table structure for utb_customer_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_customer_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_customer_mapp" (
  "customer_mapp_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "customer_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "relation_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "share_ratio" varchar(5) COLLATE "pg_catalog"."default",
  "customer_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "order_no" varchar(3) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_customer_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."customer_mapp_seq" IS '고객 맵퍼 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."office_seq" IS '사무소 식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."tbl_seq" IS '각 업무 식별키';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."customer_seq" IS '각 업무 식별키';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."create_at" IS '생성일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."update_at" IS '수정일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."del_yn" IS '삭제여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."share_ratio" IS '지분율(권리에 대한 지분)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."customer_category_code" IS '고객 카테고리 코드 (client, applicant 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_customer_mapp"."order_no" IS '당사자 순서';

-- ----------------------------
-- Table structure for utb_dept_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_dept_mst";
CREATE TABLE "MP_IPMS_PA"."utb_dept_mst" (
  "dept_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_dept_seq" varchar(30) COLLATE "pg_catalog"."default",
  "dept_code" varchar(50) COLLATE "pg_catalog"."default",
  "dept_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "dept_path" varchar(500) COLLATE "pg_catalog"."default",
  "depth" varchar(10) COLLATE "pg_catalog"."default",
  "sort_ord" varchar(10) COLLATE "pg_catalog"."default",
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_dept_mst" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_doc_dispatch
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_doc_dispatch";
CREATE TABLE "MP_IPMS_PA"."utb_doc_dispatch" (
  "dispatch_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "category" varchar(50) COLLATE "pg_catalog"."default",
  "doc_type" varchar(50) COLLATE "pg_catalog"."default",
  "dispatch_date" varchar(10) COLLATE "pg_catalog"."default",
  "client" varchar(200) COLLATE "pg_catalog"."default",
  "manager" varchar(100) COLLATE "pg_catalog"."default",
  "doc_content" text COLLATE "pg_catalog"."default",
  "method" varchar(50) COLLATE "pg_catalog"."default",
  "send_date" varchar(10) COLLATE "pg_catalog"."default",
  "reg_no" varchar(100) COLLATE "pg_catalog"."default",
  "ack_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "post_addr" varchar(500) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(20) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(20) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_doc_dispatch" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_document_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_document_mst";
CREATE TABLE "MP_IPMS_PA"."utb_document_mst" (
  "doc_seq" int4 NOT NULL DEFAULT nextval('"MP_IPMS_PA".utb_document_mst_doc_seq_seq'::regclass),
  "doc_div" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "entry_type" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "pat_type" varchar(10) COLLATE "pg_catalog"."default",
  "doc_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "auto_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar,
  "base_date_type" varchar(10) COLLATE "pg_catalog"."default",
  "deadline_unit" varchar(10) COLLATE "pg_catalog"."default",
  "deadline_val" int4,
  "ref_val" varchar(100) COLLATE "pg_catalog"."default",
  "sort_ord" int4,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_document_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."doc_seq" IS '서류 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."doc_div" IS '서류구분 (DOC_DIV: 10:국내, 20:개국, 30:PCT, 40:EP, 50:마드리드, 60:국제디자인, 70:이심, 90:기타)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."entry_type" IS '접수/제출 구분 (ENTRY_TYPE: 10:접수, 20:제출, 90:기타)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."pat_type" IS '권리구분 (10:특허, 20:실용, 30:디자인, 40:상표)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."doc_nm" IS '서류 명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."auto_yn" IS '시스템 자동 처리 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."base_date_type" IS '기한계산 기준일유형 (BASE_DATE_TYPE: 10:통지일, 20:발송일 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."deadline_unit" IS '기한 단위 (DEADLINE_UNIT: 10:일, 20:월, 30:년)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."deadline_val" IS '기한 값 (숫자)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."ref_val" IS '참조값';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."sort_ord" IS '정렬 순서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."create_user" IS '생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."create_at" IS '생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."update_user" IS '수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."update_at" IS '수정 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."del_yn" IS '삭제 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_document_mst"."note" IS '비고 및 서류 상세 설명';
COMMENT ON TABLE "MP_IPMS_PA"."utb_document_mst" IS '서류 마스터 정보 (특허청 접수/제출 서류 기준 정보)';

-- ----------------------------
-- Table structure for utb_duedate_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_duedate_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_duedate_mapp" (
  "mapping_duedate_seq" varchar(30) COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_seq" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'UNKNOWN'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_duedate_mapp" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_duedate_mapp_back
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_duedate_mapp_back";
CREATE TABLE "MP_IPMS_PA"."utb_duedate_mapp_back" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "mapping_duedate_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "duedate_seq" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'UNKNOWN'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_duedate_mapp_back" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."mapping_duedate_seq" IS '기일_매핑_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."tbl_seq" IS '단위업무_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."duedate_seq" IS '기일관리_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_duedate_mapp_back"."tbl_code" IS '업무구분코드';
COMMENT ON TABLE "MP_IPMS_PA"."utb_duedate_mapp_back" IS '기일_업무_매핑';

-- ----------------------------
-- Table structure for utb_duedate_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_duedate_mst";
CREATE TABLE "MP_IPMS_PA"."utb_duedate_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "duedate_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "duedate_order" int8 NOT NULL,
  "duedate_date" timestamptz(6),
  "duedate_kind_code" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_complete_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "alarm_establishment_code" varchar(30) COLLATE "pg_catalog"."default",
  "alarm_yn" char(1) COLLATE "pg_catalog"."default",
  "alarm_complete_yn" char(1) COLLATE "pg_catalog"."default",
  "alarm_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "unit_category_code" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_duedate_mst" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_duedate_mst_backup_20240304
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_duedate_mst_backup_20240304";
CREATE TABLE "MP_IPMS_PA"."utb_duedate_mst_backup_20240304" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_seq" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_order" int8,
  "duedate_date" timestamptz(6),
  "duedate_kind_code" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "duedate_complete_yn" char(1) COLLATE "pg_catalog"."default",
  "alarm_establishment_code" varchar(30) COLLATE "pg_catalog"."default",
  "alarm_yn" char(1) COLLATE "pg_catalog"."default",
  "alarm_complete_yn" char(1) COLLATE "pg_catalog"."default",
  "alarm_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_duedate_mst_backup_20240304" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_ext_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_ext_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_ext_mapp" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_ext_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "ext_mapp_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_ext_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."office_seq" IS '사무소 식별키';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."app_ext_seq" IS '해외기본 식별키';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."app_seq" IS '출원 식별키';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."ext_mapp_seq" IS '해외기본 맵핑 식별키';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."create_user" IS '등록자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."create_at" IS '등록일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."del_yn" IS '삭제여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_ext_mapp"."note" IS '비고';

-- ----------------------------
-- Table structure for utb_file_history
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_file_history";
CREATE TABLE "MP_IPMS_PA"."utb_file_history" (
  "file_history_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_repository_seq" varchar(30) COLLATE "pg_catalog"."default",
  "prevent_server_info" varchar(30) COLLATE "pg_catalog"."default",
  "server_info" varchar(30) COLLATE "pg_catalog"."default",
  "move_reason" varchar(255) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_file_history" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."file_history_seq" IS '파일_히스토리_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."file_repository_seq" IS '파일_저장소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."prevent_server_info" IS '이전_서버_정보';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."server_info" IS '서버_정보';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."move_reason" IS '이동_사유';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_history"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_file_history" IS '파일_히스토리';

-- ----------------------------
-- Table structure for utb_file_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_file_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_file_mapp" (
  "file_mapp_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_seq" varchar(30) COLLATE "pg_catalog"."default",
  "doc_seq" varchar(30) COLLATE "pg_catalog"."default",
  "file_kind_code" varchar(30) COLLATE "pg_catalog"."default",
  "file_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'UNKNOWN'::character varying,
  "input_create_at" timestamptz(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_file_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."file_mapp_seq" IS '파일 매핑 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."office_seq" IS '사무소 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."tbl_seq" IS '업무 테이블 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."doc_seq" IS '파일 유형 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."file_kind_code" IS '파일 종류 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."file_category_code" IS '파일 카테고리 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."create_user" IS '등록자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."create_at" IS '등록일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."del_yn" IS '삭제여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."tbl_code" IS '업무구분코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mapp"."input_create_at" IS '입력받은 등록일';
COMMENT ON TABLE "MP_IPMS_PA"."utb_file_mapp" IS '파일 매핑 정보 테이블';

-- ----------------------------
-- Table structure for utb_file_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_file_mst";
CREATE TABLE "MP_IPMS_PA"."utb_file_mst" (
  "file_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_repository_seq" varchar(30) COLLATE "pg_catalog"."default",
  "file_nm" text COLLATE "pg_catalog"."default",
  "file_original_nm" text COLLATE "pg_catalog"."default",
  "file_size" int4,
  "file_display_size" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_file_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."file_seq" IS '파일 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."file_repository_seq" IS '파일 저장소 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."file_nm" IS '저장 파일명 (물리명)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."file_original_nm" IS '원본 파일명 (논리명)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."file_size" IS '파일 크기 (Byte)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."file_display_size" IS '파일 크기 표기용 (예: 10MB)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."create_user" IS '등록자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."create_at" IS '등록 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."update_user" IS '수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."update_at" IS '수정 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."del_yn" IS '삭제 여부 (Y: 삭제, N: 미삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_mst"."note" IS '비고 및 추가 설명';
COMMENT ON TABLE "MP_IPMS_PA"."utb_file_mst" IS '파일 관리 마스터 테이블';

-- ----------------------------
-- Table structure for utb_file_mst_back
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_file_mst_back";
CREATE TABLE "MP_IPMS_PA"."utb_file_mst_back" (
  "file_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_type_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_kind_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_category_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "file_repository_seq" varchar(30) COLLATE "pg_catalog"."default",
  "file_size" int4,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "file_name" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_file_mst_back" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_file_repository
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_file_repository";
CREATE TABLE "MP_IPMS_PA"."utb_file_repository" (
  "file_repository_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "repository_kind" varchar(30) COLLATE "pg_catalog"."default",
  "repository_path" varchar(255) COLLATE "pg_catalog"."default",
  "repository_url" varchar(255) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_file_repository" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."file_repository_seq" IS '파일_저장소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."repository_kind" IS '저장소_종류';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."repository_path" IS '저장소_경로';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."repository_url" IS '저장소_URL';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_file_repository"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_file_repository" IS '파일_저장소';

-- ----------------------------
-- Table structure for utb_form_template
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_form_template";
CREATE TABLE "MP_IPMS_PA"."utb_form_template" (
  "form_template_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "category_code" varchar(50) COLLATE "pg_catalog"."default",
  "template_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "doc_modify_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "template_data" text COLLATE "pg_catalog"."default",
  "sort_ord" varchar(10) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "doc_num_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "doc_num_format" varchar(200) COLLATE "pg_catalog"."default",
  "footer_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "footer_content" text COLLATE "pg_catalog"."default",
  "external_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "appr_template_seq" varchar(30) COLLATE "pg_catalog"."default",
  "receive_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "share_scope" varchar(10) COLLATE "pg_catalog"."default",
  "share_timing" varchar(10) COLLATE "pg_catalog"."default",
  "share_change_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "redirect_url" varchar(500) COLLATE "pg_catalog"."default",
  "appr_required_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "appr_admin_set_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "appr_default_line_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "appr_cond_line_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "appr_change_allow_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "appr_skip_upper_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "fully_approve_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "receive_timing" varchar(10) COLLATE "pg_catalog"."default" DEFAULT 'APPROVED'::character varying,
  "receive_change_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_form_template" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_form_template_target
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_form_template_target";
CREATE TABLE "MP_IPMS_PA"."utb_form_template_target" (
  "target_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "form_template_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "target_role" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "target_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "ref_seq" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_form_template_target" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_group_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_group_code";
CREATE TABLE "MP_IPMS_PA"."utb_group_code" (
  "group_seq" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "group_code" varchar(30) COLLATE "pg_catalog"."default",
  "group_name" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_group_code" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."group_seq" IS '그룹코드시퀀스';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."group_code" IS '그룹코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."group_name" IS '그룹코드명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."create_at" IS '생성일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."update_at" IS '수정일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_group_code"."del_yn" IS '삭제여부';
COMMENT ON TABLE "MP_IPMS_PA"."utb_group_code" IS '공통그룹코드';

-- ----------------------------
-- Table structure for utb_invoice_banking
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_invoice_banking";
CREATE TABLE "MP_IPMS_PA"."utb_invoice_banking" (
  "banking_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_info_seq" varchar(30) COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "banking_category" varchar(30) COLLATE "pg_catalog"."default",
  "deposit_check_date" timestamptz(6),
  "deposit_amount" int4,
  "deposit_name" varchar(30) COLLATE "pg_catalog"."default",
  "deposit_bank" varchar(30) COLLATE "pg_catalog"."default",
  "deposit_accountno" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "invoice_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "deposit_fee" varchar(100) COLLATE "pg_catalog"."default",
  "deposit_way" varchar(100) COLLATE "pg_catalog"."default",
  "deposit_send_date" timestamptz(6),
  "currency_unit" varchar(20) COLLATE "pg_catalog"."default",
  "exchange_ratio" numeric(15,2),
  "exchange_amount" numeric(15,2),
  "banking_kind" varchar(30) COLLATE "pg_catalog"."default",
  "prepayment_deposit_no" varchar(30) COLLATE "pg_catalog"."default",
  "general_prepayment_balance" numeric(15,0) DEFAULT 0,
  "general_prepayment_used_amount" numeric(15,0) DEFAULT 0,
  "designated_prepayment_balance" numeric(15,0) DEFAULT 0,
  "designated_prepayment_used_amount" numeric(15,0) DEFAULT 0
)
;
ALTER TABLE "MP_IPMS_PA"."utb_invoice_banking" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."banking_seq" IS '입출금_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."user_info_seq" IS '사용자_정보';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."banking_category" IS '입출금_구분 (입금, 출금, 선수금)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_check_date" IS '입금_확인_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_amount" IS '입금_금액';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_name" IS '입금_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_bank" IS '입금_은행';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_accountno" IS '입금_계좌번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_way" IS '입금_방법';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_banking"."deposit_send_date" IS '입금_일자';
COMMENT ON TABLE "MP_IPMS_PA"."utb_invoice_banking" IS '송장_입출금내역';

-- ----------------------------
-- Table structure for utb_invoice_claim
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_invoice_claim";
CREATE TABLE "MP_IPMS_PA"."utb_invoice_claim" (
  "invoice_claim_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "invoice_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "cost_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "item_content" varchar(200) COLLATE "pg_catalog"."default",
  "unit_price" numeric(15,0) DEFAULT 0,
  "quantity" numeric(15,0) DEFAULT 0,
  "amount" numeric(15,0) DEFAULT 0,
  "vat_amount" numeric(15,0) DEFAULT 0,
  "total_amount" numeric(15,0) DEFAULT 0,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "unit_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "claim_kind" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_invoice_claim" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."invoice_claim_seq" IS '청구내역 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."invoice_seq" IS '청구서 마스터 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."office_seq" IS '사무소 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."cost_category_code" IS '비용구분코드(관납료, 수수료, 번역료 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."item_content" IS '청구 상세 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."unit_price" IS '단가';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."quantity" IS '수량';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."amount" IS '공급가액(단가 * 수량)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."vat_amount" IS '부가세액';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."total_amount" IS '합계금액(공급가액 + 부가세액)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."del_yn" IS '삭제 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_claim"."note" IS '비고 및 특이사항';
COMMENT ON TABLE "MP_IPMS_PA"."utb_invoice_claim" IS '청구서 상세 내역 테이블';

-- ----------------------------
-- Table structure for utb_invoice_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_invoice_mst";
CREATE TABLE "MP_IPMS_PA"."utb_invoice_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "invoice_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default",
  "customer_seq" varchar(30) COLLATE "pg_catalog"."default",
  "biz_info_seq" varchar(30) COLLATE "pg_catalog"."default",
  "invoice_no" varchar(100) COLLATE "pg_catalog"."default",
  "invoice_category_code" varchar(50) COLLATE "pg_catalog"."default",
  "invoice_type_code" varchar(50) COLLATE "pg_catalog"."default",
  "invoice_class_code" varchar(50) COLLATE "pg_catalog"."default",
  "our_ref" varchar(100) COLLATE "pg_catalog"."default",
  "your_ref" varchar(100) COLLATE "pg_catalog"."default",
  "client_ref" varchar(100) COLLATE "pg_catalog"."default",
  "dept_name" varchar(255) COLLATE "pg_catalog"."default",
  "debit_no" varchar(100) COLLATE "pg_catalog"."default",
  "oa_document_code" varchar(50) COLLATE "pg_catalog"."default",
  "invoice_content" text COLLATE "pg_catalog"."default",
  "agent_invoice_category_code" varchar(50) COLLATE "pg_catalog"."default",
  "currency_unit" varchar(20) COLLATE "pg_catalog"."default",
  "exchange_rate" numeric(15,2),
  "give_up_content" text COLLATE "pg_catalog"."default",
  "outsource_content" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_at" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "tax_bill_no" varchar(100) COLLATE "pg_catalog"."default",
  "tax_bill_type_code" varchar(100) COLLATE "pg_catalog"."default",
  "tax_bill_category_code" varchar(100) COLLATE "pg_catalog"."default",
  "in_out_type" varchar(100) COLLATE "pg_catalog"."default",
  "case_classification" varchar(5) COLLATE "pg_catalog"."default" NOT NULL,
  "client" varchar(20) COLLATE "pg_catalog"."default",
  "foreign_agent" varchar(20) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_invoice_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."office_seq" IS '사무소 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."invoice_seq" IS '청구서 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."app_seq" IS '출원 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."customer_seq" IS '고객 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."biz_info_seq" IS '사업자 정보 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."invoice_no" IS '청구서 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."invoice_category_code" IS '청구서 구분 코드(BILL_DIV_CD)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."invoice_type_code" IS '청구서 유형 코드(BILL_TYPE_CD)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."invoice_class_code" IS '청구서 분류 코드(BILL_EVENT_DIV)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."our_ref" IS '당사 Ref 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."your_ref" IS '상대방 Ref 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."client_ref" IS '고객 Ref 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."dept_name" IS '부서명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."debit_no" IS '차변 번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."oa_document_code" IS 'OA 문서 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."invoice_content" IS '청구 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."agent_invoice_category_code" IS '대리인 청구 구분 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."currency_unit" IS '통화 단위';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."exchange_rate" IS '환율';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."give_up_content" IS '포기 사유';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."outsource_content" IS '외주 사유';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."create_at" IS '생성일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."del_yn" IS '삭제 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."tax_bill_type_code" IS '발행구분(TAXBILL_TYPE)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."tax_bill_category_code" IS '계산서구분코드(TAX_STMT_DIV)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."in_out_type" IS 'INV ( 국내 ) , INV_INC( 인커밍)  , INV_OUT(아웃고잉)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."case_classification" IS '사건구분 (CASE_CLASSIFICATION)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."client" IS '의뢰인 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_mst"."foreign_agent" IS '해외대리인 일련번호';
COMMENT ON TABLE "MP_IPMS_PA"."utb_invoice_mst" IS '청구서 마스터 테이블';

-- ----------------------------
-- Table structure for utb_invoice_unpaid
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_invoice_unpaid";
CREATE TABLE "MP_IPMS_PA"."utb_invoice_unpaid" (
  "unpaid_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "participant_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "unpaid_amount" int4,
  "unpaid_date" timestamptz(6),
  "unpaid_notify_date" timestamptz(6),
  "notify_count" int4,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_invoice_unpaid" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."unpaid_seq" IS '미수_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."participant_seq" IS '관계자_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."user_info_seq" IS '사용자_정보';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."unpaid_amount" IS '미수_금액';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."unpaid_date" IS '미수_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."unpaid_notify_date" IS '미수_통보_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."notify_count" IS '통보_횟수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_invoice_unpaid"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_invoice_unpaid" IS '송장_미수금';

-- ----------------------------
-- Table structure for utb_item_manage_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_item_manage_mst";
CREATE TABLE "MP_IPMS_PA"."utb_item_manage_mst" (
  "item_seq" int4 NOT NULL DEFAULT nextval('"MP_IPMS_PA".utb_item_manage_mst_item_seq_seq'::regclass),
  "page_id" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "depth1_id" varchar(50) COLLATE "pg_catalog"."default",
  "depth2_id" varchar(50) COLLATE "pg_catalog"."default",
  "depth3_id" varchar(50) COLLATE "pg_catalog"."default",
  "depth4_id" varchar(50) COLLATE "pg_catalog"."default",
  "depth5_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "depth1_nm" varchar(100) COLLATE "pg_catalog"."default",
  "depth2_nm" varchar(100) COLLATE "pg_catalog"."default",
  "depth3_nm" varchar(100) COLLATE "pg_catalog"."default",
  "depth4_nm" varchar(100) COLLATE "pg_catalog"."default",
  "depth5_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "data_field" varchar(50) COLLATE "pg_catalog"."default",
  "item_div" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'INPUT'::character varying,
  "item_type" varchar(20) COLLATE "pg_catalog"."default",
  "sort_ord" int4 DEFAULT 0,
  "required_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "use_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::bpchar,
  "note1" text COLLATE "pg_catalog"."default",
  "note2" text COLLATE "pg_catalog"."default",
  "note3" text COLLATE "pg_catalog"."default",
  "note4" text COLLATE "pg_catalog"."default",
  "note5" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_item_manage_mst" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_law_attorney_info
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_law_attorney_info";
CREATE TABLE "MP_IPMS_PA"."utb_law_attorney_info" (
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "lawyer_affiliation" varchar(255) COLLATE "pg_catalog"."default",
  "lawyer_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "digital_litigation_auth_no" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_law_attorney_info" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."lawyer_affiliation" IS '변호사_소속';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."lawyer_reg_no" IS '변호사_등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."digital_litigation_auth_no" IS '전자_소송_인증_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_law_attorney_info"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_law_attorney_info" IS '"MP_IPMS_PA".UTB_변호사_정보';

-- ----------------------------
-- Table structure for utb_locarno_goods_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_locarno_goods_mst";
CREATE TABLE "MP_IPMS_PA"."utb_locarno_goods_mst" (
  "goods_seq" int4 NOT NULL DEFAULT nextval('"MP_IPMS_PA".utb_locarno_goods_mst_goods_seq_seq'::regclass),
  "class_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "subclass_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "locarno_version" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "goods_no" varchar(10) COLLATE "pg_catalog"."default",
  "goods_nm_ko" text COLLATE "pg_catalog"."default" NOT NULL,
  "goods_nm_en" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default" DEFAULT 'SYSTEM'::character varying,
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default" DEFAULT 'SYSTEM'::character varying,
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_locarno_goods_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."goods_seq" IS '물품 일련번호 (자동증가)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."class_no" IS '로카르노 분류 - 류 (Class)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."subclass_no" IS '로카르노 분류 - 군 (Subclass)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."locarno_version" IS '로카르노 분류 판수 (예: 15-2025)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."goods_no" IS '물품 고유번호 (ID)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."goods_nm_ko" IS '물품 국문 명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."goods_nm_en" IS '물품 영문 명칭';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."del_yn" IS '삭제 여부 (Y: 삭제, N: 사용)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."create_user" IS '최초 생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."update_user" IS '최종 수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_goods_mst"."update_at" IS '최종 수정 일시';
COMMENT ON TABLE "MP_IPMS_PA"."utb_locarno_goods_mst" IS '로카르노 국제 디자인 물품 마스터';

-- ----------------------------
-- Table structure for utb_locarno_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_locarno_mst";
CREATE TABLE "MP_IPMS_PA"."utb_locarno_mst" (
  "class_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "locarno_version" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "category_gb" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "class_nm_ko" text COLLATE "pg_catalog"."default" NOT NULL,
  "class_nm_en" text COLLATE "pg_catalog"."default",
  "class_desc_ko" text COLLATE "pg_catalog"."default",
  "class_desc_en" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_locarno_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."class_no" IS '류 번호 (01~32)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."locarno_version" IS '로카르노 분류 판수 (Edition, 예: 15)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."category_gb" IS '분류 구분 (예: G:GOODS, S:SERVICE 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."class_nm_ko" IS '류 명칭 (국문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."class_nm_en" IS '류 명칭 (영문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."class_desc_ko" IS '류 상세 설명 (국문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."class_desc_en" IS '류 상세 설명 (영문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."del_yn" IS '삭제 여부 (Y: 삭제, N: 미삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."create_user" IS '최초 생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_mst"."update_at" IS '최종 수정 일시';
COMMENT ON TABLE "MP_IPMS_PA"."utb_locarno_mst" IS '로카르노 국제디자인분류 마스터 정보';

-- ----------------------------
-- Table structure for utb_locarno_subclass_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_locarno_subclass_mst";
CREATE TABLE "MP_IPMS_PA"."utb_locarno_subclass_mst" (
  "class_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "subclass_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "locarno_version" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "subclass_nm_ko" text COLLATE "pg_catalog"."default" NOT NULL,
  "subclass_nm_en" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default" DEFAULT 'SYSTEM'::character varying,
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default" DEFAULT 'SYSTEM'::character varying,
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_locarno_subclass_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."class_no" IS '로카르노 류 번호 (01~32)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."subclass_no" IS '로카르노 군 번호 (예: 01, 02, 99)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."locarno_version" IS '로카르노 분류 판수 (Edition, 예: 15)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."subclass_nm_ko" IS '군 명칭 (국문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."subclass_nm_en" IS '군 명칭 (영문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."del_yn" IS '삭제 여부 (Y: 삭제, N: 미삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."create_user" IS '최초 생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_locarno_subclass_mst"."update_at" IS '최종 수정 일시';
COMMENT ON TABLE "MP_IPMS_PA"."utb_locarno_subclass_mst" IS '로카르노 국제디자인분류 세부 군(Subclass) 마스터 정보';

-- ----------------------------
-- Table structure for utb_login_history
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_login_history";
CREATE TABLE "MP_IPMS_PA"."utb_login_history" (
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "login_history_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "login_ip" inet,
  "login_success_yn" char(1) COLLATE "pg_catalog"."default",
  "login_device_type" varchar(20) COLLATE "pg_catalog"."default",
  "login_country" varchar(10) COLLATE "pg_catalog"."default",
  "login_type" varchar(10) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "category" varchar(50) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_login_history" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."login_history_seq" IS '로그인_히스토리_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."login_ip" IS '로그인_IP';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."login_success_yn" IS '로그인_성공_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."login_device_type" IS '로그인_기기_타입';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."login_country" IS '로그인_국가';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."login_type" IS '로그인_타입';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_history"."category" IS '이력 구분(로그인, 로그아웃, 기타)';
COMMENT ON TABLE "MP_IPMS_PA"."utb_login_history" IS '"MP_IPMS_PA".UTB_로그인_히스토리';

-- ----------------------------
-- Table structure for utb_login_info
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_login_info";
CREATE TABLE "MP_IPMS_PA"."utb_login_info" (
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "office_employee_seq" varchar(30) COLLATE "pg_catalog"."default",
  "user_id" varchar(50) COLLATE "pg_catalog"."default",
  "user_password" varchar(255) COLLATE "pg_catalog"."default",
  "password_update_date" timestamptz(6),
  "login_fail_count" char(1) COLLATE "pg_catalog"."default",
  "email_auth_yn" char(1) COLLATE "pg_catalog"."default",
  "login_lock_yn" char(1) COLLATE "pg_catalog"."default",
  "active_at" timestamptz(6),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_login_info" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."office_employee_seq" IS '직원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."user_id" IS '사용자_아이디';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."user_password" IS '사용자_비밀번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."password_update_date" IS '비밀번호_변경_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."login_fail_count" IS '로그인_실패_횟수';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."email_auth_yn" IS '이메일_인증_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."login_lock_yn" IS '로그인_잠금_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."active_at" IS '활성_일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_login_info"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_login_info" IS '"MP_IPMS_PA".UTB_LOGIN_INFO';

-- ----------------------------
-- Table structure for utb_maintenance_fee
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_maintenance_fee";
CREATE TABLE "MP_IPMS_PA"."utb_maintenance_fee" (
  "maintenance_fee_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "next_payment_installment" int4,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "MP_IPMS_PA"."utb_maintenance_fee" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_memo
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_memo";
CREATE TABLE "MP_IPMS_PA"."utb_memo" (
  "memo_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "must_read_yn" char(1) COLLATE "pg_catalog"."default",
  "memo_title" text COLLATE "pg_catalog"."default",
  "memo_reg_date" varchar(20) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "customer_name" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_memo" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."memo_seq" IS '메모_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."must_read_yn" IS '필독_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."memo_title" IS '메모_제목';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."memo_reg_date" IS '메모_사용자_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo"."customer_name" IS '(임시) 나중에 고객 seq로 조인할거';
COMMENT ON TABLE "MP_IPMS_PA"."utb_memo" IS 'UTB_메모';

-- ----------------------------
-- Table structure for utb_memo_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_memo_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_memo_mapp" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "memo_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default",
  "memo_mapp_seq" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_memo_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."tbl_seq" IS '테이블_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."memo_seq" IS '메모_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_memo_mapp"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_memo_mapp" IS 'UTB_맵핑_메모';

-- ----------------------------
-- Table structure for utb_menu_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_menu_mst";
CREATE TABLE "MP_IPMS_PA"."utb_menu_mst" (
  "menu_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_cd" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_menu_seq" varchar(30) COLLATE "pg_catalog"."default",
  "menu_url" varchar(200) COLLATE "pg_catalog"."default",
  "menu_icon" varchar(50) COLLATE "pg_catalog"."default",
  "disp_ord" int4 DEFAULT 0,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'Y'::character varying,
  "disp_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'GNB'::character varying,
  "menu_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'PAGE'::character varying,
  "sidebar_yn" varchar(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'Y'::character varying,
  "super_admin_only" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_menu_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_menu_mst"."disp_type" IS 'GNB: 상단좌측 | ICON_SIDEBAR: 상단우측아이콘+사이드바 | HIDDEN: 미노출';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_menu_mst"."menu_type" IS 'FOLDER: 그룹(URL없음) | PAGE: 페이지 | LINK: 외부링크 | DIVIDER: 구분선';

-- ----------------------------
-- Table structure for utb_modified_hist
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_modified_hist";
CREATE TABLE "MP_IPMS_PA"."utb_modified_hist" (
  "modified_hist_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "before_value" text COLLATE "pg_catalog"."default",
  "after_value" text COLLATE "pg_catalog"."default",
  "modified_content" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default",
  "modified_date" timestamptz(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_modified_hist" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_nice_class_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_nice_class_mst";
CREATE TABLE "MP_IPMS_PA"."utb_nice_class_mst" (
  "class_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "nice_version" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "category_gb" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "class_nm_ko" text COLLATE "pg_catalog"."default" NOT NULL,
  "class_nm_en" text COLLATE "pg_catalog"."default",
  "class_desc_ko" text COLLATE "pg_catalog"."default",
  "class_desc_en" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_nice_class_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."class_no" IS '나이스 류 번호 (01~45)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."nice_version" IS '나이스 분류 판수 (Edition, 예: 11, 12)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."category_gb" IS '분류 구분 (G:GOODS, S:SERVICE 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."class_nm_ko" IS '류 명칭 (국문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."class_nm_en" IS '류 명칭 (영문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."class_desc_ko" IS '류 상세 설명 (국문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."class_desc_en" IS '류 상세 설명 (영문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."del_yn" IS '삭제 여부 (Y: 삭제, N: 미삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."create_user" IS '최초 생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_nice_class_mst"."update_at" IS '최종 수정 일시';
COMMENT ON TABLE "MP_IPMS_PA"."utb_nice_class_mst" IS '나이스(NICE) 국제상품분류 마스터 정보';

-- ----------------------------
-- Table structure for utb_office_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_office_code";
CREATE TABLE "MP_IPMS_PA"."utb_office_code" (
  "office_code_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default",
  "code_class" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "office_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "code_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "code_name_en" varchar(100) COLLATE "pg_catalog"."default",
  "ref_val_1" varchar(100) COLLATE "pg_catalog"."default",
  "ref_val_2" varchar(100) COLLATE "pg_catalog"."default",
  "sort_ord" varchar(10) COLLATE "pg_catalog"."default",
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_office_code" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_office_default_code
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_office_default_code";
CREATE TABLE "MP_IPMS_PA"."utb_office_default_code" (
  "default_code_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "code_class" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "code_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "code_name_en" varchar(100) COLLATE "pg_catalog"."default",
  "sort_ord" varchar(10) COLLATE "pg_catalog"."default" DEFAULT '0'::character varying,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "create_user" varchar(50) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(50) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_office_default_code" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_office_employee
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_office_employee";
CREATE TABLE "MP_IPMS_PA"."utb_office_employee" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_employee_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default",
  "admin_auth" varchar(30) COLLATE "pg_catalog"."default",
  "office_employee_position" varchar(30) COLLATE "pg_catalog"."default",
  "office_employee_dept" varchar(30) COLLATE "pg_catalog"."default",
  "work_code" varchar(30) COLLATE "pg_catalog"."default",
  "position_code" varchar(30) COLLATE "pg_catalog"."default",
  "job_grade_code" varchar(30) COLLATE "pg_catalog"."default",
  "org_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "dept_seq" varchar(30) COLLATE "pg_catalog"."default",
  "role_seq" varchar(30) COLLATE "pg_catalog"."default",
  "acct_status_code" varchar(20) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_office_employee" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."office_employee_seq" IS '직원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."admin_auth" IS '관리자_인증';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."office_employee_position" IS '직원_직책';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."office_employee_dept" IS '직원_부서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."work_code" IS '재직_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."position_code" IS '직책_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."job_grade_code" IS '직급_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."org_code" IS '조직_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."role_seq" IS '사무소 내 역할 (utb_role_mst.role_seq). admin_auth=Y면 NULL 허용.';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_employee"."acct_status_code" IS '사무소별 membership 상태 (PENDING/ACTIVE/LOCKED). 관리자 승인 대기 등
  per-membership 관리용.';
COMMENT ON TABLE "MP_IPMS_PA"."utb_office_employee" IS '"MP_IPMS_PA".UTB_사무소_직원_마스터';

-- ----------------------------
-- Table structure for utb_office_menu_map
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_office_menu_map";
CREATE TABLE "MP_IPMS_PA"."utb_office_menu_map" (
  "office_menu_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "create_user" varchar(20) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(20) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6),
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_office_menu_map" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_office_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_office_mst";
CREATE TABLE "MP_IPMS_PA"."utb_office_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_short_name" varchar(30) COLLATE "pg_catalog"."default",
  "office_addr" varchar(255) COLLATE "pg_catalog"."default",
  "office_tel" varchar(30) COLLATE "pg_catalog"."default",
  "office_auth_yn" varchar(1) COLLATE "pg_catalog"."default",
  "office_state_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "office_invite_code" varchar(20) COLLATE "pg_catalog"."default",
  "plan_seq" varchar(20) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_office_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."office_short_name" IS '사무소_약어_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."office_addr" IS '사무소_주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."office_tel" IS '사무소_유선전화';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."office_auth_yn" IS '사무소_인증_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."office_state_code" IS '사무소_상태_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_office_mst"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_office_mst" IS '"MP_IPMS_PA".UTB_사무소';

-- ----------------------------
-- Table structure for utb_outsourcing_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_outsourcing_mst";
CREATE TABLE "MP_IPMS_PA"."utb_outsourcing_mst" (
  "outsourcing_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_info_seq" varchar(30) COLLATE "pg_catalog"."default",
  "outsourcing_corp" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_outsourcing_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."outsourcing_seq" IS '외주업체_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."biz_info_seq" IS '사업자_정보_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."outsourcing_corp" IS '외주_기업명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_mst"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_outsourcing_mst" IS '외주업체_마스터';

-- ----------------------------
-- Table structure for utb_outsourcing_work
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_outsourcing_work";
CREATE TABLE "MP_IPMS_PA"."utb_outsourcing_work" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "outsourcing_work_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "outsourcing_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "outsourcing_content" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_outsourcing_work" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."outsourcing_work_seq" IS '외주_작업_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."outsourcing_seq" IS '외주업체_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."outsourcing_content" IS '외주_작업_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_outsourcing_work"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_outsourcing_work" IS '외주_작업_단위업무';

-- ----------------------------
-- Table structure for utb_paper_history
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_paper_history";
CREATE TABLE "MP_IPMS_PA"."utb_paper_history" (
  "paper_history_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "paper_category_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "delyn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_paper_history" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."paper_history_seq" IS '서류_히스토리_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."paper_category_code" IS '서류_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."delyn" IS '삭제여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_history"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_paper_history" IS '진행서류_히스토리';

-- ----------------------------
-- Table structure for utb_paper_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_paper_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_paper_mapp" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "mapping_paper_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "paper_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'UNKNOWN'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_paper_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."mapping_paper_seq" IS '서류_매핑_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."tbl_seq" IS '단위업무_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."paper_seq" IS '서류_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mapp"."tbl_code" IS '업무구분코드';
COMMENT ON TABLE "MP_IPMS_PA"."utb_paper_mapp" IS '서류_업무_매핑';

-- ----------------------------
-- Table structure for utb_paper_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_paper_mst";
CREATE TABLE "MP_IPMS_PA"."utb_paper_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "paper_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "paper_kind_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "paper_category_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "paper_status_code" varchar(30) COLLATE "pg_catalog"."default",
  "file_seq" varchar(30) COLLATE "pg_catalog"."default",
  "upload_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_paper_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."paper_seq" IS '서류_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."paper_kind_code" IS '서류_종류_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."paper_category_code" IS '서류_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."paper_status_code" IS '서류_상태_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."file_seq" IS '첨부_파일_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."upload_user" IS '업로드_사용자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_paper_mst"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_paper_mst" IS '서류_마스터';

-- ----------------------------
-- Table structure for utb_participant
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_participant";
CREATE TABLE "MP_IPMS_PA"."utb_participant" (
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "participant_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "main_yn" char(1) COLLATE "pg_catalog"."default",
  "participant_code" varchar(30) COLLATE "pg_catalog"."default",
  "share_ratio" int4,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'UNKNOWN'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_participant" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."tbl_seq" IS '테이블_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."participant_seq" IS '관계자_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."user_info_seq" IS '사용자_정보';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."participant_code" IS '관계자_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."share_ratio" IS '지분_비율';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_participant"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_participant" IS '"MP_IPMS_PA".UTB_관계자';

-- ----------------------------
-- Table structure for utb_pat_attorney_info
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_pat_attorney_info";
CREATE TABLE "MP_IPMS_PA"."utb_pat_attorney_info" (
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "pat_attorney_reg_no" varchar(30) COLLATE "pg_catalog"."default",
  "pat_attorney_affiliation" varchar(255) COLLATE "pg_catalog"."default",
  "pat_attorney_digital_sign" text COLLATE "pg_catalog"."default",
  "pat_attorney_tech_specialty_category" varchar(30) COLLATE "pg_catalog"."default",
  "pat_attorney_specialty_category" varchar(30) COLLATE "pg_catalog"."default",
  "pat_attorney_year_cnt" int4,
  "pat_attorney_description" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_pat_attorney_info" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_reg_no" IS '변리사_등록_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_affiliation" IS '변리사_소속';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_digital_sign" IS '변리사_전자_서명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_tech_specialty_category" IS '변리사_기술_전공_구분';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_specialty_category" IS '변리사_전공_구분';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_year_cnt" IS '변리사_년차';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."pat_attorney_description" IS '변리사_설명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_pat_attorney_info"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_pat_attorney_info" IS '"MP_IPMS_PA".UTB_변리사_정보';

-- ----------------------------
-- Table structure for utb_patent_claim
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_patent_claim";
CREATE TABLE "MP_IPMS_PA"."utb_patent_claim" (
  "patent_claim_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "specification_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "patent_claim_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "patent_claim_no" varchar(30) COLLATE "pg_catalog"."default",
  "patent_claim_dependent_claim_no" varchar(30) COLLATE "pg_catalog"."default",
  "patent_claim_content" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_patent_claim" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."patent_claim_seq" IS '청구항_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."specification_seq" IS '명세서_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."app_seq" IS '출원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."patent_claim_category_code" IS '청구항_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."patent_claim_no" IS '청구항_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."patent_claim_dependent_claim_no" IS '청구항_종속항_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."patent_claim_content" IS '청구항_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_patent_claim"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_patent_claim" IS '"MP_IPMS_PA".UTB_청구항';

-- ----------------------------
-- Table structure for utb_performance
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_performance";
CREATE TABLE "MP_IPMS_PA"."utb_performance" (
  "performance_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "staff" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "performance_content" varchar(100) COLLATE "pg_catalog"."default",
  "performance_amount" numeric(15,0),
  "performance_reg_date" timestamptz(6),
  "performance_confirm_date" timestamptz(6),
  "share_ratio" numeric(5,2),
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "invoice_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "dept_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "performance_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "performance_perf_date" timestamptz(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_performance" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."performance_seq" IS '실적 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."staff" IS '담당자 사용자 정보 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."office_seq" IS '사무소 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."performance_content" IS '실적 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."performance_amount" IS '실적 금액';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."performance_reg_date" IS '실적 등록일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."performance_confirm_date" IS '실적 확정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."share_ratio" IS '실적 배분 비율(%)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."create_at" IS '생성일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."del_yn" IS '삭제 여부 (Y/N)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_performance"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_performance" IS '실적 관리 테이블';

-- ----------------------------
-- Table structure for utb_plan_menu_map
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_plan_menu_map";
CREATE TABLE "MP_IPMS_PA"."utb_plan_menu_map" (
  "plan_menu_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "create_user" varchar(20) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_plan_menu_map" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_plan_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_plan_mst";
CREATE TABLE "MP_IPMS_PA"."utb_plan_mst" (
  "plan_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_cd" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "note" text COLLATE "pg_catalog"."default",
  "sort_ord" int4 DEFAULT 0,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(20) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(20) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_plan_mst" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_priorresearch_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_priorresearch_mst";
CREATE TABLE "MP_IPMS_PA"."utb_priorresearch_mst" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "priorresearch_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "priorresearch_state" varchar(30) COLLATE "pg_catalog"."default",
  "priorresearch_no" varchar(30) COLLATE "pg_catalog"."default",
  "priorresearch_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "priorresearch_goal" text COLLATE "pg_catalog"."default",
  "priorresearch_retaincontent" text COLLATE "pg_catalog"."default",
  "priorresearch_retain_file" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_priorresearch_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_seq" IS '선행기술조사_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_state" IS '선행기술조사_상태';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_no" IS '선행기술조사_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_category_code" IS '선행기술조사_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_goal" IS '선행기술조사_목표';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_retaincontent" IS '선행기술조사_의뢰내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."priorresearch_retain_file" IS '선행기술조사_의뢰_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_mst"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_priorresearch_mst" IS '"MP_IPMS_PA".UTB_선행기술조사';

-- ----------------------------
-- Table structure for utb_priorresearch_result
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_priorresearch_result";
CREATE TABLE "MP_IPMS_PA"."utb_priorresearch_result" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "priorresearch_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "priorresearch_result" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "result_category_code" varchar(30) COLLATE "pg_catalog"."default",
  "priorresearch_result_file" varchar(30) COLLATE "pg_catalog"."default",
  "priorresearch_send_date" timestamptz(6),
  "priorresearch_result_content" varchar(30) COLLATE "pg_catalog"."default",
  "priorresearch_result_title" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_priorresearch_result" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."priorresearch_seq" IS '선행기술조사_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."priorresearch_result" IS '선행기술조사_결과';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."result_category_code" IS '결과_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."priorresearch_result_file" IS '선행기술조사_결과_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."priorresearch_send_date" IS '선행기술조사_송부_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."priorresearch_result_content" IS '선행기술조사_결과_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."priorresearch_result_title" IS '선행기술조사_결과_제목';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_priorresearch_result"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_priorresearch_result" IS '"MP_IPMS_PA".UTB_선행기술조사_결과';

-- ----------------------------
-- Table structure for utb_product_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_product_mst";
CREATE TABLE "MP_IPMS_PA"."utb_product_mst" (
  "product_id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "nice_version" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "class_no" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
  "similarity_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "product_nm_ko" text COLLATE "pg_catalog"."default" NOT NULL,
  "product_nm_en" text COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_product_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."product_id" IS '물품 고유 식별자 (자동 생성)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."nice_version" IS '나이스 분류 판수 (예: 11, 12)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."class_no" IS '나이스 류 번호 (01~45)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."similarity_code" IS '특허청 유사군 코드 (예: G0101, S120302)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."product_nm_ko" IS '물품 명칭 (국문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."product_nm_en" IS '물품 명칭 (영문)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."note" IS '비고 및 참고사항';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."del_yn" IS '삭제 여부 (Y: 삭제, N: 미삭제)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."create_user" IS '최초 생성자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."create_at" IS '최초 생성 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_product_mst"."update_at" IS '최종 수정 일시';
COMMENT ON TABLE "MP_IPMS_PA"."utb_product_mst" IS '상표(NICE) 세부 물품 마스터 테이블';

-- ----------------------------
-- Table structure for utb_progress
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_progress";
CREATE TABLE "MP_IPMS_PA"."utb_progress" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "progress_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_code" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "tbl_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "progress_state" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "dept_name" varchar(100) COLLATE "pg_catalog"."default",
  "target_code" varchar(30) COLLATE "pg_catalog"."default",
  "instruction_content" text COLLATE "pg_catalog"."default",
  "receipt_doc_name" varchar(255) COLLATE "pg_catalog"."default",
  "receipt_doc_content" text COLLATE "pg_catalog"."default",
  "submit_doc_name" varchar(255) COLLATE "pg_catalog"."default",
  "extension_count" varchar(100) COLLATE "pg_catalog"."default",
  "submit_doc_seq" varchar(30) COLLATE "pg_catalog"."default",
  "receipt_doc_seq" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_progress" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."office_seq" IS '사무소 SEQ';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."progress_seq" IS '진행사항 SEQ';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."tbl_code" IS '상위 업무 구분 코드 (APP, PROGRESS, COST 등)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."tbl_seq" IS '상위 업무 SEQ';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."progress_state" IS '진행 상태 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."create_at" IS '생성일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."update_at" IS '수정일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."del_yn" IS '삭제 여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."dept_name" IS '부서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_progress"."target_code" IS '대상';

-- ----------------------------
-- Table structure for utb_required_document
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_required_document";
CREATE TABLE "MP_IPMS_PA"."utb_required_document" (
  "required_doc_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "app_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "required_doc_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_required_document" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_retain
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_retain";
CREATE TABLE "MP_IPMS_PA"."utb_retain" (
  "retain_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "retain_title" varchar(255) COLLATE "pg_catalog"."default",
  "retain_content" text COLLATE "pg_catalog"."default",
  "state" varchar(30) COLLATE "pg_catalog"."default",
  "affiliation" varchar(255) COLLATE "pg_catalog"."default",
  "corp_staff_name" varchar(30) COLLATE "pg_catalog"."default",
  "corp_staff_position" varchar(30) COLLATE "pg_catalog"."default",
  "corp_staff_email" varchar(30) COLLATE "pg_catalog"."default",
  "corp_staff_mobile" varchar(30) COLLATE "pg_catalog"."default",
  "corp_staff_tel" varchar(30) COLLATE "pg_catalog"."default",
  "corp_staff_dept" varchar(30) COLLATE "pg_catalog"."default",
  "office_employee" varchar(30) COLLATE "pg_catalog"."default",
  "office_employee_mobile" varchar(30) COLLATE "pg_catalog"."default",
  "office_employee_email" varchar(30) COLLATE "pg_catalog"."default",
  "retain_file" varchar(30) COLLATE "pg_catalog"."default",
  "retain_date" timestamptz(6),
  "rightcategory_code" varchar(30) COLLATE "pg_catalog"."default",
  "app_name_ko" varchar(30) COLLATE "pg_catalog"."default",
  "app_name_en" varchar(100) COLLATE "pg_catalog"."default",
  "interior_yn" varchar(1) COLLATE "pg_catalog"."default",
  "external_yn" varchar(1) COLLATE "pg_catalog"."default",
  "retain_send_date" timestamptz(6),
  "retain_reg_date" timestamptz(6),
  "retain_result" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_retain" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_seq" IS '의뢰_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."office_seq" IS '사무_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_title" IS '의뢰_제목';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_content" IS '의뢰_내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."state" IS '상태';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."affiliation" IS '소속';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."corp_staff_name" IS '기업_담당자_이름';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."corp_staff_position" IS '기업_담당자_직책';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."corp_staff_email" IS '기업_담당자_이메일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."corp_staff_mobile" IS '기업_담당자_휴대전화';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."corp_staff_tel" IS '기업_담당자_유선전화';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."corp_staff_dept" IS '기업_담당자_부서';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."office_employee" IS '사무소_담당자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."office_employee_mobile" IS '사무소_담당자_휴대전화';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."office_employee_email" IS '사무소_담당자_이메일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_file" IS '의뢰_파일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_date" IS '의뢰_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."rightcategory_code" IS '권리구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."app_name_ko" IS '출원_이름_한글';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."app_name_en" IS '출원_이름_영어';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."interior_yn" IS '국내_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."external_yn" IS '해외_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_send_date" IS '의뢰_송부_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_reg_date" IS '의뢰_등록_일자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."retain_result" IS '의뢰_결과';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_retain"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_retain" IS '"MP_IPMS_PA".UTB_의뢰';

-- ----------------------------
-- Table structure for utb_role_menu_map
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_role_menu_map";
CREATE TABLE "MP_IPMS_PA"."utb_role_menu_map" (
  "map_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "role_seq" varchar(30) COLLATE "pg_catalog"."default",
  "menu_seq" varchar(30) COLLATE "pg_catalog"."default",
  "can_read" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying,
  "can_write" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "can_delete" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "can_excel" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_role_menu_map" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_role_menu_map_bak
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_role_menu_map_bak";
CREATE TABLE "MP_IPMS_PA"."utb_role_menu_map_bak" (
  "map_seq" varchar(30) COLLATE "pg_catalog"."default",
  "role_seq" varchar(30) COLLATE "pg_catalog"."default",
  "menu_seq" varchar(30) COLLATE "pg_catalog"."default",
  "can_read" varchar(1) COLLATE "pg_catalog"."default",
  "can_write" varchar(1) COLLATE "pg_catalog"."default",
  "can_delete" varchar(1) COLLATE "pg_catalog"."default",
  "can_excel" varchar(1) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_role_menu_map_bak" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_role_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_role_mst";
CREATE TABLE "MP_IPMS_PA"."utb_role_mst" (
  "role_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "role_cd" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "role_nm" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "note" text COLLATE "pg_catalog"."default",
  "del_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::character varying,
  "office_seq" varchar(20) COLLATE "pg_catalog"."default",
  "role_type" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'CUSTOM'::character varying,
  "use_yn" varchar(1) COLLATE "pg_catalog"."default" DEFAULT 'Y'::character varying
)
;
ALTER TABLE "MP_IPMS_PA"."utb_role_mst" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_search_condition
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_search_condition";
CREATE TABLE "MP_IPMS_PA"."utb_search_condition" (
  "condition_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "user_info_seq" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "condition_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "search_options" text COLLATE "pg_catalog"."default",
  "date_filters" text COLLATE "pg_catalog"."default",
  "text_filters" text COLLATE "pg_catalog"."default",
  "use_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'Y'::bpchar,
  "del_yn" char(1) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'N'::bpchar,
  "create_user" varchar(20) COLLATE "pg_catalog"."default",
  "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_user" varchar(20) COLLATE "pg_catalog"."default",
  "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
ALTER TABLE "MP_IPMS_PA"."utb_search_condition" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."condition_seq" IS '검색조건 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."user_info_seq" IS '사용자 일련번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."menu_code" IS '메뉴 코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."condition_name" IS '검색 조건명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."search_options" IS '핵심 검색 옵션(JSON)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."date_filters" IS '일자 필터 리스트(JSON)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_search_condition"."text_filters" IS '문자 필터 리스트(JSON)';
COMMENT ON TABLE "MP_IPMS_PA"."utb_search_condition" IS '검색 조건 저장 관리 테이블';

-- ----------------------------
-- Table structure for utb_search_field_map
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_search_field_map";
CREATE TABLE "MP_IPMS_PA"."utb_search_field_map" (
  "search_field_seq" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "field_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "target_kind" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "db_column" varchar(100) COLLATE "pg_catalog"."default",
  "search_code_column" varchar(50) COLLATE "pg_catalog"."default",
  "search_code_value" varchar(50) COLLATE "pg_catalog"."default",
  "data_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(1000) COLLATE "pg_catalog"."default",
  "source_table" varchar(50) COLLATE "pg_catalog"."default",
  "code_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar
)
;
ALTER TABLE "MP_IPMS_PA"."utb_search_field_map" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_staff
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_staff";
CREATE TABLE "MP_IPMS_PA"."utb_staff" (
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_employee_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "staff_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "retain_seq" varchar(30) COLLATE "pg_catalog"."default",
  "role_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_staff" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."office_employee_seq" IS '직원_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."staff_seq" IS '담당자_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."retain_seq" IS '의뢰_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."role_code" IS '역할_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_staff"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_staff" IS '"MP_IPMS_PA".UTB_담당자';

-- ----------------------------
-- Table structure for utb_usage_history
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_usage_history";
CREATE TABLE "MP_IPMS_PA"."utb_usage_history" (
  "usage_history_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_name" varchar(100) COLLATE "pg_catalog"."default",
  "action_name" varchar(100) COLLATE "pg_catalog"."default",
  "req_url" varchar(255) COLLATE "pg_catalog"."default",
  "req_method" varchar(10) COLLATE "pg_catalog"."default",
  "client_ip" inet,
  "user_agent" text COLLATE "pg_catalog"."default",
  "target_class" varchar(255) COLLATE "pg_catalog"."default",
  "target_method" varchar(100) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_usage_history" OWNER TO "mindpro";

-- ----------------------------
-- Table structure for utb_user_info
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_user_info";
CREATE TABLE "MP_IPMS_PA"."utb_user_info" (
  "user_info_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default",
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name_ko" varchar(30) COLLATE "pg_catalog"."default",
  "user_name_en" varchar(100) COLLATE "pg_catalog"."default",
  "user_name_zh" varchar(100) COLLATE "pg_catalog"."default",
  "user_addr" varchar(255) COLLATE "pg_catalog"."default",
  "user_addr_detail" varchar(255) COLLATE "pg_catalog"."default",
  "user_email" varchar(30) COLLATE "pg_catalog"."default",
  "corp_code" varchar(10) COLLATE "pg_catalog"."default",
  "user_mobile_no" varchar(30) COLLATE "pg_catalog"."default",
  "user_fax_no" varchar(100) COLLATE "pg_catalog"."default",
  "user_tel_no" varchar(100) COLLATE "pg_catalog"."default",
  "user_post_no" varchar(100) COLLATE "pg_catalog"."default",
  "passport_no" varchar(30) COLLATE "pg_catalog"."default",
  "country_code" varchar(30) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "dept_name" varchar(255) COLLATE "pg_catalog"."default",
  "user_position" varchar(255) COLLATE "pg_catalog"."default",
  "etax_yn" varchar(1) COLLATE "pg_catalog"."default",
  "profile_image_url" varchar(500) COLLATE "pg_catalog"."default",
  "user_name_ja" varchar(100) COLLATE "pg_catalog"."default",
  "user_addr_en" varchar(255) COLLATE "pg_catalog"."default",
  "user_addr_zh" varchar(255) COLLATE "pg_catalog"."default",
  "user_addr_ja" varchar(255) COLLATE "pg_catalog"."default",
  "resident_no" varchar(100) COLLATE "pg_catalog"."default",
  "sort_seq" int4 DEFAULT 1,
  "user_type_code" varchar(20) COLLATE "pg_catalog"."default",
  "work_status_code" varchar(20) COLLATE "pg_catalog"."default",
  "employ_status_code" varchar(20) COLLATE "pg_catalog"."default",
  "acct_status_code" varchar(20) COLLATE "pg_catalog"."default",
  "role_seq" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_user_info" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_info_seq" IS '사용자_정보';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."office_seq" IS '사무소_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_name_ko" IS '사용자_이름_한글';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_name_en" IS '사용자_이름_영어';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_name_zh" IS '사용자_이름_한자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_addr" IS '사용자_주소';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_addr_detail" IS '사용자_주소_상세';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_email" IS '사용자_이메일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."corp_code" IS '기업_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_mobile_no" IS '사용자_휴대전화_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_fax_no" IS '사용자_팩스_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_tel_no" IS '사용자_유선전화_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_post_no" IS '사용자_우편_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."passport_no" IS '여권_번호';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."country_code" IS '국가_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."note" IS '비고';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."user_type_code" IS '사용자 유형 코드 (USER_TYPE)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."work_status_code" IS '근무 상태 코드 (WORK_STATUS)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."employ_status_code" IS '재직 상태 코드 (EMPLOY_STATUS)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."acct_status_code" IS '계정 상태 코드 (ACCT_STATUS)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_info"."role_seq" IS '권한 그룹 일련번호';
COMMENT ON TABLE "MP_IPMS_PA"."utb_user_info" IS '"MP_IPMS_PA".UTB_사용자_정보';

-- ----------------------------
-- Table structure for utb_user_mst
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_user_mst";
CREATE TABLE "MP_IPMS_PA"."utb_user_mst" (
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_category_code" varchar(10) COLLATE "pg_catalog"."default",
  "mobile_auth_yn" char(1) COLLATE "pg_catalog"."default",
  "privacypolicy_agree" char(1) COLLATE "pg_catalog"."default",
  "terms_agree" char(1) COLLATE "pg_catalog"."default",
  "marketing_agree" char(1) COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_user_mst" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."user_category_code" IS '사용자_구분_코드';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."mobile_auth_yn" IS '휴대전화_인증_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."privacypolicy_agree" IS '정보제공_동의';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."terms_agree" IS '이용약관_동의';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."marketing_agree" IS '마케팅_동의';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_mst"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_user_mst" IS '"MP_IPMS_PA".UTB_사용자_마스터';

-- ----------------------------
-- Table structure for utb_user_role
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_user_role";
CREATE TABLE "MP_IPMS_PA"."utb_user_role" (
  "user_role" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_user_role" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."user_role" IS '사용자_역할';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_role"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_user_role" IS '"MP_IPMS_PA".UTB_사용자_권한';

-- ----------------------------
-- Table structure for utb_user_social
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_user_social";
CREATE TABLE "MP_IPMS_PA"."utb_user_social" (
  "auth_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "auth_approach" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "auth_token" text COLLATE "pg_catalog"."default",
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default",
  "provider_email" varchar(200) COLLATE "pg_catalog"."default",
  "provider_name" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_user_social" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."auth_seq" IS '인증_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."auth_approach" IS '인증_방법';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."auth_token" IS '인증_토큰';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_user_social" IS '"MP_IPMS_PA".UTB_소셜_로그인';

-- ----------------------------
-- Table structure for utb_user_social_mapp
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_user_social_mapp";
CREATE TABLE "MP_IPMS_PA"."utb_user_social_mapp" (
  "auth_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "auth_approach" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "user_mst_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6),
  "del_yn" char(1) COLLATE "pg_catalog"."default",
  "note" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "MP_IPMS_PA"."utb_user_social_mapp" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."auth_seq" IS '인증_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."auth_approach" IS '인증_방법';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."user_mst_seq" IS '사용자_마스터_식별자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."create_user" IS '생성자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."create_at" IS '생성일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."update_user" IS '수정자';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."update_at" IS '수정일';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."del_yn" IS '삭제_여부';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_user_social_mapp"."note" IS '비고';
COMMENT ON TABLE "MP_IPMS_PA"."utb_user_social_mapp" IS '매핑_소셜_로그인';

-- ----------------------------
-- Table structure for utb_wrappermandate
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."utb_wrappermandate";
CREATE TABLE "MP_IPMS_PA"."utb_wrappermandate" (
  "wrappermandate_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "customer_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "office_seq" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "attorney_name" varchar(100) COLLATE "pg_catalog"."default",
  "designated_attorney" varchar(100) COLLATE "pg_catalog"."default",
  "agent_no" varchar(100) COLLATE "pg_catalog"."default",
  "mandate_date" varchar(8) COLLATE "pg_catalog"."default",
  "mandate_wrapper_no" varchar(100) COLLATE "pg_catalog"."default",
  "patent_customer_no" varchar(100) COLLATE "pg_catalog"."default",
  "mandate_range" text COLLATE "pg_catalog"."default",
  "sort_order" int4 DEFAULT 0,
  "note" text COLLATE "pg_catalog"."default",
  "del_yn" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "create_user" varchar(30) COLLATE "pg_catalog"."default",
  "create_at" timestamptz(6) DEFAULT now(),
  "update_user" varchar(30) COLLATE "pg_catalog"."default",
  "update_at" timestamptz(6)
)
;
ALTER TABLE "MP_IPMS_PA"."utb_wrappermandate" OWNER TO "mindpro";
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."wrappermandate_seq" IS '위임 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."customer_seq" IS '고객 일련번호 (FK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."office_seq" IS '사무소 일련번호 (PK)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."attorney_name" IS '변리사 성명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."designated_attorney" IS '지정변리사 성명';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."agent_no" IS '대리인번호 (특허청 등록 번호)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."mandate_date" IS '위임일 (YYYYMMDD 형식)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."mandate_wrapper_no" IS '포괄위임 등록번호 (특허청 부여 번호)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."patent_customer_no" IS '특허고객번호 (고객의 출원인번호)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."mandate_range" IS '위임범위 상세 내용';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."sort_order" IS '화면 출력 순서 (낮을수록 우선순위)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."note" IS '비고 및 특이사항';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."del_yn" IS '삭제 여부 (Y:삭제, N:사용)';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."create_user" IS '최초 등록자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."create_at" IS '최초 등록 일시';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."update_user" IS '최종 수정자 ID';
COMMENT ON COLUMN "MP_IPMS_PA"."utb_wrappermandate"."update_at" IS '최종 수정 일시';
COMMENT ON TABLE "MP_IPMS_PA"."utb_wrappermandate" IS '고객별 포괄위임 관리 테이블';

-- ----------------------------
-- Table structure for vector_store
-- ----------------------------
DROP TABLE IF EXISTS "MP_IPMS_PA"."vector_store";
CREATE TABLE "MP_IPMS_PA"."vector_store" (
  "id" text COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "metadata" jsonb,
  "embedding" "public"."vector"
)
;
ALTER TABLE "MP_IPMS_PA"."vector_store" OWNER TO "mindpro";

-- ----------------------------
-- Function structure for connectby
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."connectby"(text, text, text, text, int4, text);
CREATE FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, int4, text)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'connectby_text'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, int4, text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for connectby
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."connectby"(text, text, text, text, text, int4);
CREATE FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, text, int4)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'connectby_text_serial'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, text, int4) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for connectby
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."connectby"(text, text, text, text, text, int4, text);
CREATE FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, text, int4, text)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'connectby_text_serial'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, text, int4, text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for connectby
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."connectby"(text, text, text, text, int4);
CREATE FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, int4)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'connectby_text'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."connectby"(text, text, text, text, int4) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for crosstab
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."crosstab"(text);
CREATE FUNCTION "MP_IPMS_PA"."crosstab"(text)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'crosstab'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."crosstab"(text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for crosstab
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."crosstab"(text, text);
CREATE FUNCTION "MP_IPMS_PA"."crosstab"(text, text)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'crosstab_hash'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."crosstab"(text, text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for crosstab
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."crosstab"(text, int4);
CREATE FUNCTION "MP_IPMS_PA"."crosstab"(text, int4)
  RETURNS SETOF "pg_catalog"."record" AS '$libdir/tablefunc', 'crosstab'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."crosstab"(text, int4) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for crosstab2
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."crosstab2"(text);
CREATE FUNCTION "MP_IPMS_PA"."crosstab2"(text)
  RETURNS SETOF "MP_IPMS_PA"."tablefunc_crosstab_2" AS '$libdir/tablefunc', 'crosstab'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."crosstab2"(text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for crosstab3
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."crosstab3"(text);
CREATE FUNCTION "MP_IPMS_PA"."crosstab3"(text)
  RETURNS SETOF "MP_IPMS_PA"."tablefunc_crosstab_3" AS '$libdir/tablefunc', 'crosstab'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."crosstab3"(text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for crosstab4
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."crosstab4"(text);
CREATE FUNCTION "MP_IPMS_PA"."crosstab4"(text)
  RETURNS SETOF "MP_IPMS_PA"."tablefunc_crosstab_4" AS '$libdir/tablefunc', 'crosstab'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."crosstab4"(text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_generate_duedate_pivot
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_generate_duedate_pivot"("p_office_seq" varchar, "p_app_seq" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_generate_duedate_pivot"("p_office_seq" varchar, "p_app_seq" varchar)
  RETURNS "pg_catalog"."text" AS $BODY$
DECLARE
    col_list TEXT;
    sql_query TEXT;
    result_msg TEXT;
BEGIN
    -- 1. 해당 출원(app_seq)에 존재하는 기한 카테고리 추출 및 컬럼화 구문 생성
    SELECT STRING_AGG(col_part, ', ')
    INTO col_list
    FROM (
        SELECT DISTINCT 
            d.duedate_category_code,
            FORMAT('json_data->>%L AS %I', 
                   d.duedate_category_code, d.duedate_category_code) AS col_part
        FROM "MP_IPMS_PA".utb_duedate_mapp m
        JOIN "MP_IPMS_PA".utb_duedate_mst d ON m.duedate_seq = d.duedate_seq
        WHERE m.tbl_seq = p_app_seq 
          AND m.office_seq = p_office_seq
          AND d.del_yn = 'N'
        ORDER BY 1
    ) sub;

    -- 2. 기존 세션의 임시 테이블 삭제
    DROP TABLE IF EXISTS temp_duedate_report;

    -- 3. 데이터 존재 여부에 따른 동적 테이블 생성
    IF col_list IS NOT NULL THEN
        sql_query := FORMAT('
            CREATE TEMP TABLE temp_duedate_report AS
            WITH json_base AS (
                SELECT 
                    a.app_seq,
                    a.app_no,
                    a.app_name_ko,
                    JSONB_OBJECT_AGG(d.duedate_category_code, TO_CHAR(d.duedate_date, ''YYYY-MM-DD'')) AS json_data
                FROM "MP_IPMS_PA".utb_app_mst a
                JOIN "MP_IPMS_PA".utb_duedate_mapp m ON a.app_seq = m.tbl_seq AND a.office_seq = m.office_seq
                JOIN "MP_IPMS_PA".utb_duedate_mst d ON m.duedate_seq = d.duedate_seq AND m.office_seq = d.office_seq
                WHERE a.app_seq = %L AND a.office_seq = %L 
                  AND a.del_yn = ''N'' AND m.del_yn = ''N'' AND d.del_yn = ''N''
                GROUP BY a.app_seq, a.app_no, a.app_name_ko
            )
            SELECT app_seq, app_no, app_name_ko, %s FROM json_base', 
            p_app_seq, p_office_seq, col_list);
            
        EXECUTE sql_query;
        result_msg := 'SUCCESS: Temporary table created for ' || p_app_seq;
    ELSE
        -- 데이터가 없을 경우 구조만 가진 빈 테이블 생성
        CREATE TEMP TABLE temp_duedate_report (app_seq varchar, app_no varchar, message text);
        INSERT INTO temp_duedate_report (app_seq, message) VALUES (p_app_seq, 'No data found');
        result_msg := 'NODATA: No due date categories found for ' || p_app_seq;
    END IF;

    RETURN result_msg;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_generate_duedate_pivot"("p_office_seq" varchar, "p_app_seq" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_generate_product_id
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_generate_product_id"();
CREATE FUNCTION "MP_IPMS_PA"."fn_generate_product_id"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
DECLARE
    first_sim_code TEXT;
BEGIN
    IF NEW.product_id IS NULL OR NEW.product_id = '' THEN
        -- 1. 콤마(,)를 기준으로 분리하여 첫 번째 코드만 가져옴
        -- 2. 혹시 모를 공백 제거
        first_sim_code := TRIM(SPLIT_PART(NEW.similarity_code, ',', 1));
        
        -- 3. ID 조합 (P + 류 2자리 + 첫번째 유사군코드 + 시퀀스 6자리)
        -- 결과 예시: P01G0101000001
        NEW.product_id := 'P' 
            || LPAD(NEW.class_no, 2, '0') 
            || first_sim_code
            || LPAD(nextval('"MP_IPMS_PA".seq_product_no')::text, 6, '0');
    END IF;
    RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_generate_product_id"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_cd_nm
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_cd_nm"("p_grp_cd" varchar, "p_dtl_cd" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_cd_nm"("p_grp_cd" varchar, "p_dtl_cd" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_cd_nm VARCHAR(100);
BEGIN
    -- dtl 테이블에서 그룹코드와 상세코드가 일치하고 삭제되지 않은 명칭 조회
    SELECT cd_nm
      INTO v_cd_nm
      FROM "MP_IPMS_PA".utb_code_dtl
     WHERE grp_cd = p_grp_cd
       AND dtl_cd = p_dtl_cd
       AND del_yn = 'N'
     LIMIT 1;

    -- 값이 없을 경우 입력을 그대로 반환하거나 NULL 처리 (선택사항)
    RETURN COALESCE(v_cd_nm, p_dtl_cd); 
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_cd_nm"("p_grp_cd" varchar, "p_dtl_cd" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_cost_amount
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_cost_amount"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_category_code" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_cost_amount"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_category_code" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_amount_str VARCHAR;
BEGIN
    SELECT CAST(dmst.krw_amount AS VARCHAR)
    INTO v_amount_str
    FROM "MP_IPMS_PA".utb_cost_mapp dmap
    JOIN "MP_IPMS_PA".utb_cost_mst dmst 
      ON dmst.mapping_cost_seq = dmap.mapping_cost_seq -- 💡 조인 조건을 mapping_cost_seq로 변경
     AND dmst.office_seq = dmap.office_seq
    WHERE dmap.tbl_seq = p_tbl_seq
      AND dmap.office_seq = p_office_seq
      AND dmst.cost_category_code = p_category_code -- 💡 카테고리 코드 매칭
      AND dmap.del_yn = 'N'
      AND dmst.del_yn = 'N'
    ORDER BY dmst.create_at DESC -- 💡 혹시 모를 중복 대비 최신순 정렬
    LIMIT 1;
    RETURN COALESCE(v_amount_str, '0');
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_cost_amount"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_category_code" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_ctry_nm
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_ctry_nm"("p_ctry_code" varchar, "p_org_ind" varchar, "p_lang_type" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_ctry_nm"("p_ctry_code" varchar, "p_org_ind" varchar, "p_lang_type" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_result_nm VARCHAR;
BEGIN
    SELECT 
        CASE 
            WHEN UPPER(p_lang_type) = 'KO' THEN ctry_ko_nm
            WHEN UPPER(p_lang_type) = 'EN' THEN ctry_en_nm
            ELSE NULL 
        END INTO v_result_nm
    FROM "MP_IPMS_PA".utb_contry_code
    WHERE ctry_code = UPPER(p_ctry_code)
      AND org_ind = UPPER(p_org_ind);

    RETURN v_result_nm;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_ctry_nm"("p_ctry_code" varchar, "p_org_ind" varchar, "p_lang_type" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_doc_nm
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_doc_nm"("p_doc_seq" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_doc_nm"("p_doc_seq" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_doc_nm VARCHAR(100);
BEGIN
    -- doc_seq는 숫아이므로 casting 처리하여 조회
    -- 만약 p_doc_seq가 null이거나 숫자가 아니면 예외처리가 필요할 수 있음
    IF p_doc_seq IS NULL OR p_doc_seq = '' THEN
        RETURN '';
    END IF;

    SELECT doc_nm
      INTO v_doc_nm
      FROM "MP_IPMS_PA".utb_document_mst
     WHERE doc_seq = p_doc_seq::integer
       AND del_yn = 'N'
     LIMIT 1;

    -- 값이 없을 경우 빈 문자열 또는 NULL 반환
    RETURN COALESCE(v_doc_nm, ''); 
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_doc_nm"("p_doc_seq" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_dossier_category_code
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_dossier_category_code"("p_case_classification" varchar, "p_tbl_seq" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_dossier_category_code"("p_case_classification" varchar, "p_tbl_seq" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_prefix VARCHAR(6);
BEGIN
    IF p_tbl_seq IS NULL THEN RETURN NULL; END IF;
    v_prefix := LEFT(p_tbl_seq, 6);

    IF p_case_classification = '10' THEN
		IF v_prefix = 'PROGRS' THEN
	        RETURN '20'; -- 국내진행
		ELSIF v_prefix = 'MEMMST' THEN
	        RETURN '30'; -- 국내메모
		END IF;

        RETURN '10'; -- 국내

	ELSIF p_case_classification = '20' THEN
		IF v_prefix = 'MEMMST' THEN
	        RETURN '50'; -- 해외기본메모
		END IF;

        RETURN '40'; -- 해외기본

	ELSIF p_case_classification = '30' THEN
		IF v_prefix = 'PROGRS' THEN
	        RETURN '70'; -- 해외출원진행
		ELSIF v_prefix = 'MEMMST' THEN
	        RETURN '80'; -- 해외출원메모
		END IF;

        RETURN '60'; -- 해외출원

	ELSIF p_case_classification = '40' THEN
		IF v_prefix = 'PROGRS' THEN
	        RETURN '100'; -- 이의심판진행

		ELSIF v_prefix = 'MEMMST' THEN
	        RETURN '110'; -- 이의심판메모
		END IF;

        RETURN '90'; -- 이의심판

	ELSIF p_case_classification = '50' THEN
		IF v_prefix = 'PROGRS' THEN
	        RETURN '130'; -- 기타사건진행

		ELSIF v_prefix = 'MEMMST' THEN
	        RETURN '140'; -- 기타사건메모
		END IF;

        RETURN '120'; -- 기타사건

	ELSIF p_case_classification = '60' THEN
        RETURN '150'; -- 내국청구서

	ELSIF p_case_classification = '70' THEN
        RETURN '160'; -- 외국청구서

	ELSIF p_case_classification = '80' THEN
        RETURN '170'; -- 해외청구서

	ELSIF p_case_classification = '90' THEN
		IF v_prefix = 'MEMMST' THEN
	        RETURN '190'; -- 고객메모
		END IF;

        RETURN '180'; -- 고객
	END IF;
		
    RETURN '00';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_dossier_category_code"("p_case_classification" varchar, "p_tbl_seq" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_duedate_str
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_duedate_str"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_category_code" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_duedate_str"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_category_code" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_date_str VARCHAR;
BEGIN
    SELECT TO_CHAR(dmst.duedate_date, 'YYYYMMDD')
    INTO v_date_str
    FROM "MP_IPMS_PA".utb_duedate_mapp dmap
    JOIN "MP_IPMS_PA".utb_duedate_mst dmst 
      ON dmst.duedate_seq = dmap.duedate_seq 
     AND dmst.office_seq = dmap.office_seq
    WHERE dmap.tbl_seq = p_tbl_seq
      AND dmap.office_seq = p_office_seq
      AND dmst.duedate_category_code = p_category_code
      AND dmap.del_yn = 'N'
      AND dmst.del_yn = 'N'
    LIMIT 1;

    RETURN v_date_str;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_duedate_str"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_category_code" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_dynamic_seq
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_dynamic_seq"("p_tbl_name" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_dynamic_seq"("p_tbl_name" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$                                                                   
  DECLARE                                                                       
      v_target_col   VARCHAR(100);
      v_short_name   VARCHAR(30);
      v_prefix       VARCHAR(30);
      v_curr_year    CHAR(4);
      v_max_id       VARCHAR(30);
      v_new_seq      INTEGER;
      v_result       VARCHAR(30);
      v_like_pattern VARCHAR(30);
      v_query_tbl    VARCHAR(100);   -- [신규] 실제 SELECT 대상 테이블 (논리명과 다를 수 있음)
  BEGIN
      v_curr_year := TO_CHAR(CURRENT_DATE, 'YYYY');
      v_query_tbl := p_tbl_name;     -- 기본: 논리명 = 실제 테이블명

      IF p_tbl_name = 'utb_code_mst' THEN
          v_prefix := 'CDMST';
          v_target_col := 'code_seq';
          v_like_pattern := v_prefix || '%';

      ELSIF p_tbl_name = 'utb_code_dtl' THEN
          v_prefix := 'CDDTL';
          v_target_col := 'code_seq';
          v_like_pattern := v_prefix || '%';

      ELSIF p_tbl_name = 'utb_office_mst' THEN
          v_prefix := 'PGOKR';
          v_target_col := 'office_seq';
          v_like_pattern := v_prefix || v_curr_year || '%';

      -- [신규] 개인 사무소: prefix=USRKR, 실제 테이블은 utb_office_mst
      ELSIF p_tbl_name = 'utb_office_mst_user' THEN
          v_prefix := 'USRKR';
          v_target_col := 'office_seq';
          v_like_pattern := v_prefix || v_curr_year || '%';
          v_query_tbl := 'utb_office_mst';

      ELSE
          SELECT tbl_short_name, tbl_mapping_nm
            INTO v_short_name, v_target_col
            FROM "MP_IPMS_PA".stb_tlb_code
           WHERE tbl_name = p_tbl_name;

          IF NOT FOUND THEN
              RAISE EXCEPTION 'stb_tlb_code에 테이블 정보가 없습니다: %', p_tbl_name;
          END IF;

          v_prefix := v_short_name;
          v_like_pattern := v_prefix || v_curr_year || '%';
      END IF;

      EXECUTE format('SELECT MAX(%I) FROM "MP_IPMS_PA".%I WHERE %I LIKE %L',
                     v_target_col, v_query_tbl, v_target_col, v_like_pattern)
      INTO v_max_id;

      IF v_max_id IS NULL THEN
          v_new_seq := 1;
      ELSE
          v_new_seq := RIGHT(v_max_id, 7)::INTEGER + 1;
      END IF;

      IF p_tbl_name IN ('utb_code_mst', 'utb_code_dtl') THEN
          v_result := v_prefix || LPAD(v_new_seq::TEXT, 7, '0');
      ELSE
          v_result := v_prefix || v_curr_year || LPAD(v_new_seq::TEXT, 7, '0');
      END IF;

      RETURN v_result;
  END;
  $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_dynamic_seq"("p_tbl_name" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for fn_get_participant_name
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."fn_get_participant_name"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_participant_code" varchar);
CREATE FUNCTION "MP_IPMS_PA"."fn_get_participant_name"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_participant_code" varchar)
  RETURNS "pg_catalog"."varchar" AS $BODY$
DECLARE
    v_user_name VARCHAR;
BEGIN
    SELECT 
        COALESCE(u.user_name_ko, p.user_info_seq) -- 유저 테이블 이름 우선, 없으면 참여자 테이블 이름
    INTO v_user_name
    FROM "MP_IPMS_PA".utb_participant p
    LEFT JOIN "MP_IPMS_PA".utb_user_info u 
        ON p.user_info_seq = u.user_info_seq 
       AND p.office_seq = u.office_seq
    WHERE p.office_seq = p_office_seq
      AND p.tbl_seq = p_tbl_seq
      AND p.participant_code = p_participant_code
      AND p.del_yn = 'N'
    ORDER BY p.create_at DESC -- 여러 명일 경우 가장 최근 등록자
    LIMIT 1;

    RETURN v_user_name;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "MP_IPMS_PA"."fn_get_participant_name"("p_office_seq" varchar, "p_tbl_seq" varchar, "p_participant_code" varchar) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for normal_rand
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."normal_rand"(int4, float8, float8);
CREATE FUNCTION "MP_IPMS_PA"."normal_rand"(int4, float8, float8)
  RETURNS SETOF "pg_catalog"."float8" AS '$libdir/tablefunc', 'normal_rand'
  LANGUAGE c VOLATILE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "MP_IPMS_PA"."normal_rand"(int4, float8, float8) OWNER TO "mindpro";

-- ----------------------------
-- Procedure structure for prc_refresh_participant_view
-- ----------------------------
DROP PROCEDURE IF EXISTS "MP_IPMS_PA"."prc_refresh_participant_view"();
CREATE PROCEDURE "MP_IPMS_PA"."prc_refresh_participant_view"()
 AS $BODY$
DECLARE
    v_col_list TEXT;      -- ID와 성명 컬럼 리스트 (코드 + 코드_NM)
    v_final_sql TEXT;
BEGIN
    -- 1. 유동적 participant_code별로 [ID 컬럼]과 [성명_NM 컬럼]을 쌍으로 생성
    SELECT STRING_AGG(col_part, ', ' ORDER BY participant_code)
    INTO v_col_list
    FROM (
        SELECT DISTINCT 
            participant_code,
            -- %1$L: 코드값(값 비교용), %1$I: 코드값(ID 컬럼명), %2$I: 성명 컬럼명(_NM)
            FORMAT('MAX(CASE WHEN p.participant_code = %1$L THEN p.user_info_seq END) AS %1$I, ' ||
                   'MAX(CASE WHEN p.participant_code = %1$L THEN u.user_name_ko END) AS %2$I', 
                   participant_code, participant_code || '_NM') AS col_part
        FROM "MP_IPMS_PA".utb_participant
        WHERE participant_code IS NOT NULL AND del_yn = 'N'
    ) sub;

    -- 2. SQL 조립 (utb_user_info JOIN 포함)
    v_final_sql := FORMAT('
        DROP VIEW IF EXISTS "MP_IPMS_PA".v_participant_pivot CASCADE;
        CREATE VIEW "MP_IPMS_PA".v_participant_pivot AS
        SELECT 
            p.tbl_seq,
            p.office_seq,
            %s
        FROM "MP_IPMS_PA".utb_participant p
        LEFT JOIN "MP_IPMS_PA".utb_user_info u 
               ON p.user_info_seq = u.user_info_seq 
              AND p.office_seq = u.office_seq
        WHERE p.del_yn = ''N''
        GROUP BY p.tbl_seq, p.office_seq', 
        v_col_list);

    -- 3. 실행
    EXECUTE v_final_sql;
END;
$BODY$
  LANGUAGE plpgsql;
ALTER PROCEDURE "MP_IPMS_PA"."prc_refresh_participant_view"() OWNER TO "mindpro";

-- ----------------------------
-- Procedure structure for prc_refresh_total_duedate_view
-- ----------------------------
DROP PROCEDURE IF EXISTS "MP_IPMS_PA"."prc_refresh_total_duedate_view"();
CREATE PROCEDURE "MP_IPMS_PA"."prc_refresh_total_duedate_view"()
 AS $BODY$
DECLARE
    v_col_list TEXT;      -- 동적 기한 컬럼 리스트
    v_join_list TEXT;     -- 동적 마스터 테이블 조인 리스트
    v_coalesce_seq TEXT;  -- PK 통합 리스트
    v_case_title TEXT;    -- 한글 명칭 매핑 리스트
    v_final_sql TEXT;
BEGIN
    -- 1. 유동적 기한 컬럼 리스트 생성 (NULL 필터링 추가)
    SELECT STRING_AGG(col_part, ', ')
    INTO v_col_list
    FROM (
        SELECT DISTINCT 
            duedate_category_code,
            FORMAT('(v.data->>%L)::timestamptz AS %I', 
                   duedate_category_code, duedate_category_code) AS col_part
        FROM "MP_IPMS_PA".utb_duedate_mst 
        WHERE del_yn = 'N' 
          AND duedate_category_code IS NOT NULL  -- NULL 값 제외 (에러 방지)
        ORDER BY duedate_category_code
    ) sub;

    -- 2. stb_tlb_code를 기반으로 동적 JOIN 및 COALESCE 구문 생성 (필수값 체크 추가)
    SELECT 
        STRING_AGG(FORMAT('LEFT JOIN "MP_IPMS_PA".%I t%s ON v.tbl_seq = t%s.%I AND c.tbl_name = %L', 
                   tbl_name, tbl_code_seq, tbl_code_seq, tbl_mapping_nm, tbl_name), ' ' ORDER BY tbl_code_seq),
        STRING_AGG(FORMAT('t%s.%I', tbl_code_seq, tbl_mapping_nm), ', ' ORDER BY tbl_code_seq),
        STRING_AGG(FORMAT('WHEN %L THEN %L', tbl_short_name, COALESCE(tbl_name, tbl_short_name)), ' ' ORDER BY tbl_short_name)
    INTO v_join_list, v_coalesce_seq, v_case_title
    FROM "MP_IPMS_PA".stb_tlb_code
    WHERE tbl_name IS NOT NULL 
      AND tbl_mapping_nm IS NOT NULL; -- 식별자로 쓰이는 컬럼의 NULL 방지

    -- 3. 최종 VIEW 생성 SQL 조립
    -- v_col_list나 v_join_list가 비어있을 경우를 대비한 방어 로직
    IF v_col_list IS NULL OR v_join_list IS NULL THEN
        RAISE EXCEPTION '필수 데이터(기일 카테고리 또는 테이블 코드)가 부족하여 뷰를 생성할 수 없습니다.';
    END IF;

    v_final_sql := FORMAT('
        DROP VIEW IF EXISTS "MP_IPMS_PA".v_total_duedate_pivot CASCADE;
        CREATE VIEW "MP_IPMS_PA".v_total_duedate_pivot AS
        WITH d_vals AS (
            SELECT 
                m.tbl_seq, m.office_seq,
                JSONB_OBJECT_AGG(d.duedate_category_code, d.duedate_date) AS data
            FROM "MP_IPMS_PA".utb_duedate_mapp m
            JOIN "MP_IPMS_PA".utb_duedate_mst d ON m.duedate_seq = d.duedate_seq
            WHERE m.del_yn = ''N'' AND d.del_yn = ''N''
			  AND d.duedate_category_code IS NOT NULL  -- <--- JSON 키 NULL 방지 추가
            GROUP BY m.tbl_seq, m.office_seq
        )
        SELECT 
            c.tbl_name,
            c.tbl_short_name,
            v.office_seq,
            v.tbl_seq,
            COALESCE(%s) AS master_ref_seq,
            CASE c.tbl_short_name %s ELSE ''기타'' END AS master_title,
            %s
        FROM d_vals v
        JOIN "MP_IPMS_PA".stb_tlb_code c ON LEFT(v.tbl_seq, 6) = c.tbl_short_name
        %s', 
        v_coalesce_seq, v_case_title, v_col_list, v_join_list);

    -- 4. 뷰 재생성 실행
    EXECUTE v_final_sql;
    
    RAISE NOTICE 'View v_total_duedate_pivot has been successfully refreshed.';
END;
$BODY$
  LANGUAGE plpgsql;
ALTER PROCEDURE "MP_IPMS_PA"."prc_refresh_total_duedate_view"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_generate_v1
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_generate_v1"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_generate_v1"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v1'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_generate_v1"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_generate_v1mc
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_generate_v1mc"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_generate_v1mc"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v1mc'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_generate_v1mc"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_generate_v3
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_generate_v3"("namespace" uuid, "name" text);
CREATE FUNCTION "MP_IPMS_PA"."uuid_generate_v3"("namespace" uuid, "name" text)
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v3'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_generate_v3"("namespace" uuid, "name" text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_generate_v4
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_generate_v4"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_generate_v4"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v4'
  LANGUAGE c VOLATILE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_generate_v4"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_generate_v5
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_generate_v5"("namespace" uuid, "name" text);
CREATE FUNCTION "MP_IPMS_PA"."uuid_generate_v5"("namespace" uuid, "name" text)
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v5'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_generate_v5"("namespace" uuid, "name" text) OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_nil
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_nil"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_nil"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_nil'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_nil"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_ns_dns
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_ns_dns"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_ns_dns"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_dns'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_ns_dns"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_ns_oid
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_ns_oid"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_ns_oid"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_oid'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_ns_oid"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_ns_url
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_ns_url"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_ns_url"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_url'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_ns_url"() OWNER TO "mindpro";

-- ----------------------------
-- Function structure for uuid_ns_x500
-- ----------------------------
DROP FUNCTION IF EXISTS "MP_IPMS_PA"."uuid_ns_x500"();
CREATE FUNCTION "MP_IPMS_PA"."uuid_ns_x500"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_x500'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION "MP_IPMS_PA"."uuid_ns_x500"() OWNER TO "mindpro";

-- ----------------------------
-- View structure for v_participant_pivot
-- ----------------------------
DROP VIEW IF EXISTS "MP_IPMS_PA"."v_participant_pivot";
CREATE VIEW "MP_IPMS_PA"."v_participant_pivot" AS  SELECT p.tbl_seq,
    p.office_seq,
    max(
        CASE
            WHEN p.participant_code::text = 'adminMgr'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "adminMgr",
    max(
        CASE
            WHEN p.participant_code::text = 'adminMgr'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "adminMgr_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'applicant'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS applicant,
    max(
        CASE
            WHEN p.participant_code::text = 'applicant'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "applicant_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'applicantContact'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "applicantContact",
    max(
        CASE
            WHEN p.participant_code::text = 'applicantContact'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "applicantContact_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'applicantName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "applicantName",
    max(
        CASE
            WHEN p.participant_code::text = 'applicantName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "applicantName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'appManager'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "appManager",
    max(
        CASE
            WHEN p.participant_code::text = 'appManager'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "appManager_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'attorney'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS attorney,
    max(
        CASE
            WHEN p.participant_code::text = 'attorney'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "attorney_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'caseMgr'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "caseMgr",
    max(
        CASE
            WHEN p.participant_code::text = 'caseMgr'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "caseMgr_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'client'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS client,
    max(
        CASE
            WHEN p.participant_code::text = 'client'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "client_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'clientContact'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "clientContact",
    max(
        CASE
            WHEN p.participant_code::text = 'clientContact'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "clientContact_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'clientName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "clientName",
    max(
        CASE
            WHEN p.participant_code::text = 'clientName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "clientName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'customerContact'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "customerContact",
    max(
        CASE
            WHEN p.participant_code::text = 'customerContact'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "customerContact_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'deptName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "deptName",
    max(
        CASE
            WHEN p.participant_code::text = 'deptName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "deptName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'examiner'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS examiner,
    max(
        CASE
            WHEN p.participant_code::text = 'examiner'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "examiner_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'foreignAgent'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "foreignAgent",
    max(
        CASE
            WHEN p.participant_code::text = 'foreignAgent'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "foreignAgent_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'foreignClient'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "foreignClient",
    max(
        CASE
            WHEN p.participant_code::text = 'foreignClient'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "foreignClient_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'idsSubmitMng'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "idsSubmitMng",
    max(
        CASE
            WHEN p.participant_code::text = 'idsSubmitMng'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "idsSubmitMng_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'introducer'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS introducer,
    max(
        CASE
            WHEN p.participant_code::text = 'introducer'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "introducer_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'inventor'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS inventor,
    max(
        CASE
            WHEN p.participant_code::text = 'inventor'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "inventor_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'inventorName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "inventorName",
    max(
        CASE
            WHEN p.participant_code::text = 'inventorName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "inventorName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'invMgr'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "invMgr",
    max(
        CASE
            WHEN p.participant_code::text = 'invMgr'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "invMgr_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'MGR01'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "MGR01",
    max(
        CASE
            WHEN p.participant_code::text = 'MGR01'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "MGR01_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'PET'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "PET",
    max(
        CASE
            WHEN p.participant_code::text = 'PET'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "PET_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'petitioner'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS petitioner,
    max(
        CASE
            WHEN p.participant_code::text = 'petitioner'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "petitioner_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'petitionerName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "petitionerName",
    max(
        CASE
            WHEN p.participant_code::text = 'petitionerName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "petitionerName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'receiptReportManager'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "receiptReportManager",
    max(
        CASE
            WHEN p.participant_code::text = 'receiptReportManager'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "receiptReportManager_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'regMgr'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "regMgr",
    max(
        CASE
            WHEN p.participant_code::text = 'regMgr'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "regMgr_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'RES'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "RES",
    max(
        CASE
            WHEN p.participant_code::text = 'RES'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "RES_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'respondent'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS respondent,
    max(
        CASE
            WHEN p.participant_code::text = 'respondent'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "respondent_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'respondentName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "respondentName",
    max(
        CASE
            WHEN p.participant_code::text = 'respondentName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "respondentName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'resultPetitioner'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "resultPetitioner",
    max(
        CASE
            WHEN p.participant_code::text = 'resultPetitioner'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "resultPetitioner_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'ResultPetitionerName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "ResultPetitionerName",
    max(
        CASE
            WHEN p.participant_code::text = 'ResultPetitionerName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "ResultPetitionerName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'resultRespondent'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "resultRespondent",
    max(
        CASE
            WHEN p.participant_code::text = 'resultRespondent'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "resultRespondent_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'ResultRespondentName'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "ResultRespondentName",
    max(
        CASE
            WHEN p.participant_code::text = 'ResultRespondentName'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "ResultRespondentName_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'reviewReportManager'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "reviewReportManager",
    max(
        CASE
            WHEN p.participant_code::text = 'reviewReportManager'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "reviewReportManager_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'string'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS string,
    max(
        CASE
            WHEN p.participant_code::text = 'string'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "string_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'submitManager'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "submitManager",
    max(
        CASE
            WHEN p.participant_code::text = 'submitManager'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "submitManager_NM",
    max(
        CASE
            WHEN p.participant_code::text = 'submitReportManager'::text THEN p.user_info_seq
            ELSE NULL::character varying
        END::text) AS "submitReportManager",
    max(
        CASE
            WHEN p.participant_code::text = 'submitReportManager'::text THEN u.user_name_ko
            ELSE NULL::character varying
        END::text) AS "submitReportManager_NM"
   FROM "MP_IPMS_PA".utb_participant p
     LEFT JOIN "MP_IPMS_PA".utb_user_info u ON p.user_info_seq::text = u.user_info_seq::text AND p.office_seq::text = u.office_seq::text
  WHERE p.del_yn = 'N'::bpchar
  GROUP BY p.tbl_seq, p.office_seq;
ALTER TABLE "MP_IPMS_PA"."v_participant_pivot" OWNER TO "mindpro";

-- ----------------------------
-- View structure for v_total_duedate_pivot
-- ----------------------------
DROP VIEW IF EXISTS "MP_IPMS_PA"."v_total_duedate_pivot";
CREATE VIEW "MP_IPMS_PA"."v_total_duedate_pivot" AS  WITH d_vals AS (
         SELECT m.tbl_seq,
            m.office_seq,
            jsonb_object_agg(d.duedate_category_code, d.duedate_date) AS data
           FROM "MP_IPMS_PA".utb_duedate_mapp m
             JOIN "MP_IPMS_PA".utb_duedate_mst d ON m.duedate_seq::text = d.duedate_seq::text
          WHERE m.del_yn = 'N'::bpchar AND d.del_yn = 'N'::bpchar AND d.duedate_category_code IS NOT NULL
          GROUP BY m.tbl_seq, m.office_seq
        )
 SELECT c.tbl_name,
    c.tbl_short_name,
    v.office_seq,
    v.tbl_seq,
    COALESCE(ttblcod0000001.user_info_seq, ttblcod0000002.user_mst_seq, ttblcod0000003.biz_info_seq, ttblcod0000004.app_seq, ttblcod0000005.retain_seq, ttblcod0000006.duedate_seq, ttblcod0000007.mapping_duedate_seq, ttblcod0000008.participant_seq, ttblcod0000009.conflict_seq, ttblcod0000010.condition_seq, ttblcod0000011.conflict_result_seq, ttblcod0000012.file_mapp_seq, ttblcod0000013.app_ext_seq, ttblcod0000014.cost_seq, ttblcod0000015.mapping_cost_seq, ttblcod0000016.progress_seq, ttblcod0000017.memo_seq, ttblcod0000018.memo_mapp_seq, ttblcod0000019.grace_period_seq, ttblcod0000020.patent_seq, ttblcod0000021.preference_seq, ttblcod0000022.customer_seq, ttblcod0000023.rnd_seq, ttblcod0000024.design_seq, ttblcod0000025.trademark_seq, ttblcod0000026.wrappermandate_seq, ttblcod0000027.product_seq, ttblcod0000028.locarno_seq, ttblcod0000029.customer_mapp_seq, ttblcod0000030.modified_hist_seq, ttblcod0000031.invoice_seq, ttblcod0000032.performance_seq, ttblcod0000034.banking_seq, ttblcod0000035.invoice_claim_seq, ttblcod0000036.ext_mapp_seq, ttblcod0000037.ids_seq, ttblcod0000038.app_history_seq, ttblcod0000039.dept_seq, ttblcod0000040.office_code_seq, ttblcod0000041.appr_template_seq, ttblcod0000042.template_line_seq, ttblcod0000043.form_template_seq, ttblcod0000044.target_seq, ttblcod0000045.office_employee_seq, ttblcod0000046.app_inventor_seq, ttblcod0000047.required_doc_seq, ttblcod0000048.maintenance_fee_seq, ttblcod0000049.login_history_seq, ttblcod0000050.usage_history_seq, ttblcod0000051.role_seq, ttblcod0000052.map_seq, ttblcod0000053.doc_seq, ttblcod0000054.line_seq, ttblcod0000055.target_seq, ttblcod0000056.menu_seq) AS master_ref_seq,
        CASE c.tbl_short_name
            WHEN 'APLIN'::text THEN 'utb_appr_template_line'::text
            WHEN 'APPHST'::text THEN 'utb_app_mst_history'::text
            WHEN 'APPIDS'::text THEN 'utb_app_ids'::text
            WHEN 'APPLCN'::text THEN 'utb_app_locarno'::text
            WHEN 'APPMST'::text THEN 'utb_app_mst'::text
            WHEN 'APPPRD'::text THEN 'utb_app_product'::text
            WHEN 'APPRND'::text THEN 'utb_app_rnd'::text
            WHEN 'APRDOC'::text THEN 'utb_appr_doc'::text
            WHEN 'APRLN'::text THEN 'utb_appr_doc_line'::text
            WHEN 'APRTG'::text THEN 'utb_appr_doc_target'::text
            WHEN 'APTPL'::text THEN 'utb_appr_template'::text
            WHEN 'BIZINF'::text THEN 'utb_biz_info'::text
            WHEN 'CFTMST'::text THEN 'utb_conflict_mst'::text
            WHEN 'CFTRES'::text THEN 'utb_conflict_result'::text
            WHEN 'CSRMAP'::text THEN 'utb_modified_hist'::text
            WHEN 'CSRMAP'::text THEN 'utb_customer_mapp'::text
            WHEN 'CSTMAP'::text THEN 'utb_cost_mapp'::text
            WHEN 'CSTMST'::text THEN 'utb_cost_mst'::text
            WHEN 'CUSTMR'::text THEN 'utb_customer'::text
            WHEN 'DESIGN'::text THEN 'utb_app_design'::text
            WHEN 'DPTMS'::text THEN 'utb_dept_mst'::text
            WHEN 'DUEMAP'::text THEN 'utb_duedate_mapp'::text
            WHEN 'DUEMST'::text THEN 'utb_duedate_mst'::text
            WHEN 'EXTMAP'::text THEN 'utb_ext_mapp'::text
            WHEN 'EXTMST'::text THEN 'utb_app_ext_mst'::text
            WHEN 'FILMAP'::text THEN 'utb_file_mapp'::text
            WHEN 'FMTPL'::text THEN 'utb_form_template'::text
            WHEN 'FMTTG'::text THEN 'utb_form_template_target'::text
            WHEN 'GRCPRD'::text THEN 'utb_app_grace_period'::text
            WHEN 'INVBAK'::text THEN 'utb_invoice_banking'::text
            WHEN 'INVCLM'::text THEN 'utb_invoice_claim'::text
            WHEN 'INVETR'::text THEN 'utb_app_inventor'::text
            WHEN 'INVMST'::text THEN 'utb_invoice_mst'::text
            WHEN 'LOGHST'::text THEN 'utb_login_history'::text
            WHEN 'MAINTF'::text THEN 'utb_maintenance_fee'::text
            WHEN 'MEMMAP'::text THEN 'utb_memo_mapp'::text
            WHEN 'MEMMST'::text THEN 'utb_memo'::text
            WHEN 'MENUMST'::text THEN 'utb_menu_mst'::text
            WHEN 'OFCCD'::text THEN 'utb_office_code'::text
            WHEN 'OFEMP'::text THEN 'utb_office_employee'::text
            WHEN 'PAPMST'::text THEN 'utb_participant'::text
            WHEN 'PATENT'::text THEN 'utb_app_patent'::text
            WHEN 'PERFOM'::text THEN 'utb_performance'::text
            WHEN 'PRFENC'::text THEN 'utb_app_preference'::text
            WHEN 'PROGRS'::text THEN 'utb_progress'::text
            WHEN 'REQDOC'::text THEN 'utb_required_document'::text
            WHEN 'RETAIN'::text THEN 'utb_retain'::text
            WHEN 'RLMAP'::text THEN 'utb_role_menu_map'::text
            WHEN 'ROLE'::text THEN 'utb_role_mst'::text
            WHEN 'SCHCON'::text THEN 'utb_search_condition'::text
            WHEN 'TDMARK'::text THEN 'utb_app_trademark'::text
            WHEN 'USEHST'::text THEN 'utb_usage_history'::text
            WHEN 'USERIF'::text THEN 'utb_user_info'::text
            WHEN 'USERMS'::text THEN 'utb_user_mst'::text
            WHEN 'WRPMAD'::text THEN 'utb_wrappermandate'::text
            ELSE '기타'::text
        END AS master_title,
    (v.data ->> 'abandonDate'::text)::timestamp with time zone AS "abandonDate",
    (v.data ->> 'abandonInstructDate'::text)::timestamp with time zone AS "abandonInstructDate",
    (v.data ->> 'abandonOrderDate'::text)::timestamp with time zone AS "abandonOrderDate",
    (v.data ->> 'abandonReceiptDate'::text)::timestamp with time zone AS "abandonReceiptDate",
    (v.data ->> 'abndDate'::text)::timestamp with time zone AS "abndDate",
    (v.data ->> 'agentInvDate'::text)::timestamp with time zone AS "agentInvDate",
    (v.data ->> 'agentReceiptDate'::text)::timestamp with time zone AS "agentReceiptDate",
    (v.data ->> 'amendDeadline'::text)::timestamp with time zone AS "amendDeadline",
    (v.data ->> 'amendLimitDate'::text)::timestamp with time zone AS "amendLimitDate",
    (v.data ->> 'amendNoticeDate'::text)::timestamp with time zone AS "amendNoticeDate",
    (v.data ->> 'amendSubmitDate'::text)::timestamp with time zone AS "amendSubmitDate",
    (v.data ->> 'announcementDate'::text)::timestamp with time zone AS "announcementDate",
    (v.data ->> 'announcementDecisionDate'::text)::timestamp with time zone AS "announcementDecisionDate",
    (v.data ->> 'annuityGraceDeadline'::text)::timestamp with time zone AS "annuityGraceDeadline",
    (v.data ->> 'annuityNormalDeadline'::text)::timestamp with time zone AS "annuityNormalDeadline",
    (v.data ->> 'annuityOrderDate'::text)::timestamp with time zone AS "annuityOrderDate",
    (v.data ->> 'annuityRecoveryDeadline'::text)::timestamp with time zone AS "annuityRecoveryDeadline",
    (v.data ->> 'appCompleteDate'::text)::timestamp with time zone AS "appCompleteDate",
    (v.data ->> 'appDate'::text)::timestamp with time zone AS "appDate",
    (v.data ->> 'appDeadline'::text)::timestamp with time zone AS "appDeadline",
    (v.data ->> 'appealDate'::text)::timestamp with time zone AS "appealDate",
    (v.data ->> 'appealLimitDate'::text)::timestamp with time zone AS "appealLimitDate",
    (v.data ->> 'appOrderDate'::text)::timestamp with time zone AS "appOrderDate",
    (v.data ->> 'authoritySubmissionDate'::text)::timestamp with time zone AS "authoritySubmissionDate",
    (v.data ->> 'autoProtectionDate'::text)::timestamp with time zone AS "autoProtectionDate",
    (v.data ->> 'claimAmendDate'::text)::timestamp with time zone AS "claimAmendDate",
    (v.data ->> 'claimDate'::text)::timestamp with time zone AS "claimDate",
    (v.data ->> 'claimsDeadline'::text)::timestamp with time zone AS "claimsDeadline",
    (v.data ->> 'claimsNoticeDate'::text)::timestamp with time zone AS "claimsNoticeDate",
    (v.data ->> 'claimsSubmitDate'::text)::timestamp with time zone AS "claimsSubmitDate",
    (v.data ->> 'costRemittanceDate'::text)::timestamp with time zone AS "costRemittanceDate",
    (v.data ->> 'debitReceiptDate'::text)::timestamp with time zone AS "debitReceiptDate",
    (v.data ->> 'DEC'::text)::timestamp with time zone AS "DEC",
    (v.data ->> 'deemedWithdrawalDate'::text)::timestamp with time zone AS "deemedWithdrawalDate",
    (v.data ->> 'deemedWithdrawalReceiptDate'::text)::timestamp with time zone AS "deemedWithdrawalReceiptDate",
    (v.data ->> 'divAppDate'::text)::timestamp with time zone AS "divAppDate",
    (v.data ->> 'divDeadline'::text)::timestamp with time zone AS "divDeadline",
    (v.data ->> 'documentLimitDate'::text)::timestamp with time zone AS "documentLimitDate",
    (v.data ->> 'documentSubmitDate'::text)::timestamp with time zone AS "documentSubmitDate",
    (v.data ->> 'domesticPriorDate'::text)::timestamp with time zone AS "domesticPriorDate",
    (v.data ->> 'domesticPriorDeadline'::text)::timestamp with time zone AS "domesticPriorDeadline",
    (v.data ->> 'domesticRegDate'::text)::timestamp with time zone AS "domesticRegDate",
    (v.data ->> 'domesticRegDecisionDate'::text)::timestamp with time zone AS "domesticRegDecisionDate",
    (v.data ->> 'draftDeadline'::text)::timestamp with time zone AS "draftDeadline",
    (v.data ->> 'draftSendDate'::text)::timestamp with time zone AS "draftSendDate",
    (v.data ->> 'dualAppDate'::text)::timestamp with time zone AS "dualAppDate",
    (v.data ->> 'dueLimitDate'::text)::timestamp with time zone AS "dueLimitDate",
    (v.data ->> 'earlyPubRequestDate'::text)::timestamp with time zone AS "earlyPubRequestDate",
    (v.data ->> 'entry20CompleteDate'::text)::timestamp with time zone AS "entry20CompleteDate",
    (v.data ->> 'entry30CompleteDate'::text)::timestamp with time zone AS "entry30CompleteDate",
    (v.data ->> 'epAnnouncementDate'::text)::timestamp with time zone AS "epAnnouncementDate",
    (v.data ->> 'examRequestDate'::text)::timestamp with time zone AS "examRequestDate",
    (v.data ->> 'examRequestDeadline'::text)::timestamp with time zone AS "examRequestDeadline",
    (v.data ->> 'examRequestOrderDate'::text)::timestamp with time zone AS "examRequestOrderDate",
    (v.data ->> 'exchangeRateDate'::text)::timestamp with time zone AS "exchangeRateDate",
    (v.data ->> 'filingFeeDeadline'::text)::timestamp with time zone AS "filingFeeDeadline",
    (v.data ->> 'filingFeePayDate'::text)::timestamp with time zone AS "filingFeePayDate",
    (v.data ->> 'firstAppDate'::text)::timestamp with time zone AS "firstAppDate",
    (v.data ->> 'foreign1yDeadline'::text)::timestamp with time zone AS "foreign1yDeadline",
    (v.data ->> 'foreign6mDeadline'::text)::timestamp with time zone AS "foreign6mDeadline",
    (v.data ->> 'foreignAppDate'::text)::timestamp with time zone AS "foreignAppDate",
    (v.data ->> 'globalAppDate'::text)::timestamp with time zone AS "globalAppDate",
    (v.data ->> 'govFeePayDate'::text)::timestamp with time zone AS "govFeePayDate",
    (v.data ->> 'gracePeriodDate'::text)::timestamp with time zone AS "gracePeriodDate",
    (v.data ->> 'hagueDeliveryDate'::text)::timestamp with time zone AS "hagueDeliveryDate",
    (v.data ->> 'idsDeadline'::text)::timestamp with time zone AS "idsDeadline",
    (v.data ->> 'idsPubDate'::text)::timestamp with time zone AS "idsPubDate",
    (v.data ->> 'idsReceiptDate'::text)::timestamp with time zone AS "idsReceiptDate",
    (v.data ->> 'idsSendDate'::text)::timestamp with time zone AS "idsSendDate",
    (v.data ->> 'idsSubmitDate'::text)::timestamp with time zone AS "idsSubmitDate",
    (v.data ->> 'instructionDate'::text)::timestamp with time zone AS "instructionDate",
    (v.data ->> 'intlPubDate'::text)::timestamp with time zone AS "intlPubDate",
    (v.data ->> 'intlReceiptDate'::text)::timestamp with time zone AS "intlReceiptDate",
    (v.data ->> 'invDate'::text)::timestamp with time zone AS "invDate",
    (v.data ->> 'inventionReportDate'::text)::timestamp with time zone AS "inventionReportDate",
    (v.data ->> 'invSendDate'::text)::timestamp with time zone AS "invSendDate",
    (v.data ->> 'ipeDeadline'::text)::timestamp with time zone AS "ipeDeadline",
    (v.data ->> 'ipeReportDate'::text)::timestamp with time zone AS "ipeReportDate",
    (v.data ->> 'ipeRequestDate'::text)::timestamp with time zone AS "ipeRequestDate",
    (v.data ->> 'isaReceiptDate'::text)::timestamp with time zone AS "isaReceiptDate",
    (v.data ->> 'isrReportDate'::text)::timestamp with time zone AS "isrReportDate",
    (v.data ->> 'judgmentDate'::text)::timestamp with time zone AS "judgmentDate",
    (v.data ->> 'judgmentServedDate'::text)::timestamp with time zone AS "judgmentServedDate",
    (v.data ->> 'kipoDelayDays'::text)::timestamp with time zone AS "kipoDelayDays",
    (v.data ->> 'madridAppDate'::text)::timestamp with time zone AS "madridAppDate",
    (v.data ->> 'maintFeeDeadline'::text)::timestamp with time zone AS "maintFeeDeadline",
    (v.data ->> 'maintFeeOrderDate'::text)::timestamp with time zone AS "maintFeeOrderDate",
    (v.data ->> 'maintFeePaymentDate'::text)::timestamp with time zone AS "maintFeePaymentDate",
    (v.data ->> 'maintFeePenaltyDeadline'::text)::timestamp with time zone AS "maintFeePenaltyDeadline",
    (v.data ->> 'noticeDate'::text)::timestamp with time zone AS "noticeDate",
    (v.data ->> 'npe20Deadline'::text)::timestamp with time zone AS "npe20Deadline",
    (v.data ->> 'npe30Deadline'::text)::timestamp with time zone AS "npe30Deadline",
    (v.data ->> 'oaDeliveryDate'::text)::timestamp with time zone AS "oaDeliveryDate",
    (v.data ->> 'occurDate'::text)::timestamp with time zone AS "occurDate",
    (v.data ->> 'originalAppDate'::text)::timestamp with time zone AS "originalAppDate",
    (v.data ->> 'originalRegDate'::text)::timestamp with time zone AS "originalRegDate",
    (v.data ->> 'outsourceDate'::text)::timestamp with time zone AS "outsourceDate",
    (v.data ->> 'parentAppDate'::text)::timestamp with time zone AS "parentAppDate",
    (v.data ->> 'parentRegAppDate'::text)::timestamp with time zone AS "parentRegAppDate",
    (v.data ->> 'penaltyDeadline'::text)::timestamp with time zone AS "penaltyDeadline",
    (v.data ->> 'perfDate'::text)::timestamp with time zone AS "perfDate",
    (v.data ->> 'preExamDate'::text)::timestamp with time zone AS "preExamDate",
    (v.data ->> 'preferenceAssertDate'::text)::timestamp with time zone AS "preferenceAssertDate",
    (v.data ->> 'preferenceRegDate'::text)::timestamp with time zone AS "preferenceRegDate",
    (v.data ->> 'priorExamDecDate'::text)::timestamp with time zone AS "priorExamDecDate",
    (v.data ->> 'priorExamReqDate'::text)::timestamp with time zone AS "priorExamReqDate",
    (v.data ->> 'priorityDate'::text)::timestamp with time zone AS "priorityDate",
    (v.data ->> 'processDate'::text)::timestamp with time zone AS "processDate",
    (v.data ->> 'protectionStartDate'::text)::timestamp with time zone AS "protectionStartDate",
    (v.data ->> 'provisionalAppDate'::text)::timestamp with time zone AS "provisionalAppDate",
    (v.data ->> 'pubDate'::text)::timestamp with time zone AS "pubDate",
    (v.data ->> 'publicDecisionDate'::text)::timestamp with time zone AS "publicDecisionDate",
    (v.data ->> 'reAppDate'::text)::timestamp with time zone AS "reAppDate",
    (v.data ->> 'receiptDate'::text)::timestamp with time zone AS "receiptDate",
    (v.data ->> 'receiptReportDate'::text)::timestamp with time zone AS "receiptReportDate",
    (v.data ->> 'receiptReportLimitDate'::text)::timestamp with time zone AS "receiptReportLimitDate",
    (v.data ->> 'recoveryDeadline'::text)::timestamp with time zone AS "recoveryDeadline",
    (v.data ->> 'regAnnounceDate'::text)::timestamp with time zone AS "regAnnounceDate",
    (v.data ->> 'regDate'::text)::timestamp with time zone AS "regDate",
    (v.data ->> 'regDecisionDate'::text)::timestamp with time zone AS "regDecisionDate",
    (v.data ->> 'regGraceDeadline'::text)::timestamp with time zone AS "regGraceDeadline",
    (v.data ->> 'regNormalDeadline'::text)::timestamp with time zone AS "regNormalDeadline",
    (v.data ->> 'regOrderDate'::text)::timestamp with time zone AS "regOrderDate",
    (v.data ->> 'regPaymentDate'::text)::timestamp with time zone AS "regPaymentDate",
    (v.data ->> 'regReceiptDate'::text)::timestamp with time zone AS "regReceiptDate",
    (v.data ->> 'renewalDeadline'::text)::timestamp with time zone AS "renewalDeadline",
    (v.data ->> 'REQ'::text)::timestamp with time zone AS "REQ",
    (v.data ->> 'requestDate'::text)::timestamp with time zone AS "requestDate",
    (v.data ->> 'resultDecisionDate'::text)::timestamp with time zone AS "resultDecisionDate",
    (v.data ->> 'ResultDecisionDate'::text)::timestamp with time zone AS "ResultDecisionDate",
    (v.data ->> 'resultRequestDate'::text)::timestamp with time zone AS "resultRequestDate",
    (v.data ->> 'ResultRequestDate'::text)::timestamp with time zone AS "ResultRequestDate",
    (v.data ->> 'reviewOpinionLimitDate'::text)::timestamp with time zone AS "reviewOpinionLimitDate",
    (v.data ->> 'reviewReportDate'::text)::timestamp with time zone AS "reviewReportDate",
    (v.data ->> 'rightPeriod'::text)::timestamp with time zone AS "rightPeriod",
    (v.data ->> 'searchReceiptDate'::text)::timestamp with time zone AS "searchReceiptDate",
    (v.data ->> 'searchReportDate'::text)::timestamp with time zone AS "searchReportDate",
    (v.data ->> 'sendDate'::text)::timestamp with time zone AS "sendDate",
    (v.data ->> 'signReqDate'::text)::timestamp with time zone AS "signReqDate",
    (v.data ->> 'standardDeadline'::text)::timestamp with time zone AS "standardDeadline",
    (v.data ->> 'submitClosingDate'::text)::timestamp with time zone AS "submitClosingDate",
    (v.data ->> 'submitDate'::text)::timestamp with time zone AS "submitDate",
    (v.data ->> 'submitDeadline'::text)::timestamp with time zone AS "submitDeadline",
    (v.data ->> 'submitDeadLineDate'::text)::timestamp with time zone AS "submitDeadLineDate",
    (v.data ->> 'submitReportDate'::text)::timestamp with time zone AS "submitReportDate",
    (v.data ->> 'submitReportLimitDate'::text)::timestamp with time zone AS "submitReportLimitDate",
    (v.data ->> 'taxBillDate'::text)::timestamp with time zone AS "taxBillDate",
    (v.data ->> 'trademarkGraceDeadline'::text)::timestamp with time zone AS "trademarkGraceDeadline",
    (v.data ->> 'trademarkNormalDeadline'::text)::timestamp with time zone AS "trademarkNormalDeadline",
    (v.data ->> 'transDeadline'::text)::timestamp with time zone AS "transDeadline",
    (v.data ->> 'transSubmitDate'::text)::timestamp with time zone AS "transSubmitDate",
    (v.data ->> 'vatPayDate'::text)::timestamp with time zone AS "vatPayDate"
   FROM d_vals v
     JOIN "MP_IPMS_PA".stb_tlb_code c ON "left"(v.tbl_seq::text, 6) = c.tbl_short_name::text
     LEFT JOIN "MP_IPMS_PA".utb_user_info ttblcod0000001 ON v.tbl_seq::text = ttblcod0000001.user_info_seq::text AND c.tbl_name::text = 'utb_user_info'::text
     LEFT JOIN "MP_IPMS_PA".utb_user_mst ttblcod0000002 ON v.tbl_seq::text = ttblcod0000002.user_mst_seq::text AND c.tbl_name::text = 'utb_user_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_biz_info ttblcod0000003 ON v.tbl_seq::text = ttblcod0000003.biz_info_seq::text AND c.tbl_name::text = 'utb_biz_info'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_mst ttblcod0000004 ON v.tbl_seq::text = ttblcod0000004.app_seq::text AND c.tbl_name::text = 'utb_app_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_retain ttblcod0000005 ON v.tbl_seq::text = ttblcod0000005.retain_seq::text AND c.tbl_name::text = 'utb_retain'::text
     LEFT JOIN "MP_IPMS_PA".utb_duedate_mst ttblcod0000006 ON v.tbl_seq::text = ttblcod0000006.duedate_seq::text AND c.tbl_name::text = 'utb_duedate_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_duedate_mapp ttblcod0000007 ON v.tbl_seq::text = ttblcod0000007.mapping_duedate_seq::text AND c.tbl_name::text = 'utb_duedate_mapp'::text
     LEFT JOIN "MP_IPMS_PA".utb_participant ttblcod0000008 ON v.tbl_seq::text = ttblcod0000008.participant_seq::text AND c.tbl_name::text = 'utb_participant'::text
     LEFT JOIN "MP_IPMS_PA".utb_conflict_mst ttblcod0000009 ON v.tbl_seq::text = ttblcod0000009.conflict_seq::text AND c.tbl_name::text = 'utb_conflict_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_search_condition ttblcod0000010 ON v.tbl_seq::text = ttblcod0000010.condition_seq::text AND c.tbl_name::text = 'utb_search_condition'::text
     LEFT JOIN "MP_IPMS_PA".utb_conflict_result ttblcod0000011 ON v.tbl_seq::text = ttblcod0000011.conflict_result_seq::text AND c.tbl_name::text = 'utb_conflict_result'::text
     LEFT JOIN "MP_IPMS_PA".utb_file_mapp ttblcod0000012 ON v.tbl_seq::text = ttblcod0000012.file_mapp_seq::text AND c.tbl_name::text = 'utb_file_mapp'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_ext_mst ttblcod0000013 ON v.tbl_seq::text = ttblcod0000013.app_ext_seq::text AND c.tbl_name::text = 'utb_app_ext_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_cost_mst ttblcod0000014 ON v.tbl_seq::text = ttblcod0000014.cost_seq::text AND c.tbl_name::text = 'utb_cost_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_cost_mapp ttblcod0000015 ON v.tbl_seq::text = ttblcod0000015.mapping_cost_seq::text AND c.tbl_name::text = 'utb_cost_mapp'::text
     LEFT JOIN "MP_IPMS_PA".utb_progress ttblcod0000016 ON v.tbl_seq::text = ttblcod0000016.progress_seq::text AND c.tbl_name::text = 'utb_progress'::text
     LEFT JOIN "MP_IPMS_PA".utb_memo ttblcod0000017 ON v.tbl_seq::text = ttblcod0000017.memo_seq::text AND c.tbl_name::text = 'utb_memo'::text
     LEFT JOIN "MP_IPMS_PA".utb_memo_mapp ttblcod0000018 ON v.tbl_seq::text = ttblcod0000018.memo_mapp_seq::text AND c.tbl_name::text = 'utb_memo_mapp'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_grace_period ttblcod0000019 ON v.tbl_seq::text = ttblcod0000019.grace_period_seq::text AND c.tbl_name::text = 'utb_app_grace_period'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_patent ttblcod0000020 ON v.tbl_seq::text = ttblcod0000020.patent_seq::text AND c.tbl_name::text = 'utb_app_patent'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_preference ttblcod0000021 ON v.tbl_seq::text = ttblcod0000021.preference_seq::text AND c.tbl_name::text = 'utb_app_preference'::text
     LEFT JOIN "MP_IPMS_PA".utb_customer ttblcod0000022 ON v.tbl_seq::text = ttblcod0000022.customer_seq::text AND c.tbl_name::text = 'utb_customer'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_rnd ttblcod0000023 ON v.tbl_seq::text = ttblcod0000023.rnd_seq::text AND c.tbl_name::text = 'utb_app_rnd'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_design ttblcod0000024 ON v.tbl_seq::text = ttblcod0000024.design_seq::text AND c.tbl_name::text = 'utb_app_design'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_trademark ttblcod0000025 ON v.tbl_seq::text = ttblcod0000025.trademark_seq::text AND c.tbl_name::text = 'utb_app_trademark'::text
     LEFT JOIN "MP_IPMS_PA".utb_wrappermandate ttblcod0000026 ON v.tbl_seq::text = ttblcod0000026.wrappermandate_seq::text AND c.tbl_name::text = 'utb_wrappermandate'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_product ttblcod0000027 ON v.tbl_seq::text = ttblcod0000027.product_seq::text AND c.tbl_name::text = 'utb_app_product'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_locarno ttblcod0000028 ON v.tbl_seq::text = ttblcod0000028.locarno_seq::text AND c.tbl_name::text = 'utb_app_locarno'::text
     LEFT JOIN "MP_IPMS_PA".utb_customer_mapp ttblcod0000029 ON v.tbl_seq::text = ttblcod0000029.customer_mapp_seq::text AND c.tbl_name::text = 'utb_customer_mapp'::text
     LEFT JOIN "MP_IPMS_PA".utb_modified_hist ttblcod0000030 ON v.tbl_seq::text = ttblcod0000030.modified_hist_seq::text AND c.tbl_name::text = 'utb_modified_hist'::text
     LEFT JOIN "MP_IPMS_PA".utb_invoice_mst ttblcod0000031 ON v.tbl_seq::text = ttblcod0000031.invoice_seq::text AND c.tbl_name::text = 'utb_invoice_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_performance ttblcod0000032 ON v.tbl_seq::text = ttblcod0000032.performance_seq::text AND c.tbl_name::text = 'utb_performance'::text
     LEFT JOIN "MP_IPMS_PA".utb_invoice_banking ttblcod0000034 ON v.tbl_seq::text = ttblcod0000034.banking_seq::text AND c.tbl_name::text = 'utb_invoice_banking'::text
     LEFT JOIN "MP_IPMS_PA".utb_invoice_claim ttblcod0000035 ON v.tbl_seq::text = ttblcod0000035.invoice_claim_seq::text AND c.tbl_name::text = 'utb_invoice_claim'::text
     LEFT JOIN "MP_IPMS_PA".utb_ext_mapp ttblcod0000036 ON v.tbl_seq::text = ttblcod0000036.ext_mapp_seq::text AND c.tbl_name::text = 'utb_ext_mapp'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_ids ttblcod0000037 ON v.tbl_seq::text = ttblcod0000037.ids_seq::text AND c.tbl_name::text = 'utb_app_ids'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_mst_history ttblcod0000038 ON v.tbl_seq::text = ttblcod0000038.app_history_seq::text AND c.tbl_name::text = 'utb_app_mst_history'::text
     LEFT JOIN "MP_IPMS_PA".utb_dept_mst ttblcod0000039 ON v.tbl_seq::text = ttblcod0000039.dept_seq::text AND c.tbl_name::text = 'utb_dept_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_office_code ttblcod0000040 ON v.tbl_seq::text = ttblcod0000040.office_code_seq::text AND c.tbl_name::text = 'utb_office_code'::text
     LEFT JOIN "MP_IPMS_PA".utb_appr_template ttblcod0000041 ON v.tbl_seq::text = ttblcod0000041.appr_template_seq::text AND c.tbl_name::text = 'utb_appr_template'::text
     LEFT JOIN "MP_IPMS_PA".utb_appr_template_line ttblcod0000042 ON v.tbl_seq::text = ttblcod0000042.template_line_seq::text AND c.tbl_name::text = 'utb_appr_template_line'::text
     LEFT JOIN "MP_IPMS_PA".utb_form_template ttblcod0000043 ON v.tbl_seq::text = ttblcod0000043.form_template_seq::text AND c.tbl_name::text = 'utb_form_template'::text
     LEFT JOIN "MP_IPMS_PA".utb_form_template_target ttblcod0000044 ON v.tbl_seq::text = ttblcod0000044.target_seq::text AND c.tbl_name::text = 'utb_form_template_target'::text
     LEFT JOIN "MP_IPMS_PA".utb_office_employee ttblcod0000045 ON v.tbl_seq::text = ttblcod0000045.office_employee_seq::text AND c.tbl_name::text = 'utb_office_employee'::text
     LEFT JOIN "MP_IPMS_PA".utb_app_inventor ttblcod0000046 ON v.tbl_seq::text = ttblcod0000046.app_inventor_seq::text AND c.tbl_name::text = 'utb_app_inventor'::text
     LEFT JOIN "MP_IPMS_PA".utb_required_document ttblcod0000047 ON v.tbl_seq::text = ttblcod0000047.required_doc_seq::text AND c.tbl_name::text = 'utb_required_document'::text
     LEFT JOIN "MP_IPMS_PA".utb_maintenance_fee ttblcod0000048 ON v.tbl_seq::text = ttblcod0000048.maintenance_fee_seq::text AND c.tbl_name::text = 'utb_maintenance_fee'::text
     LEFT JOIN "MP_IPMS_PA".utb_login_history ttblcod0000049 ON v.tbl_seq::text = ttblcod0000049.login_history_seq::text AND c.tbl_name::text = 'utb_login_history'::text
     LEFT JOIN "MP_IPMS_PA".utb_usage_history ttblcod0000050 ON v.tbl_seq::text = ttblcod0000050.usage_history_seq::text AND c.tbl_name::text = 'utb_usage_history'::text
     LEFT JOIN "MP_IPMS_PA".utb_role_mst ttblcod0000051 ON v.tbl_seq::text = ttblcod0000051.role_seq::text AND c.tbl_name::text = 'utb_role_mst'::text
     LEFT JOIN "MP_IPMS_PA".utb_role_menu_map ttblcod0000052 ON v.tbl_seq::text = ttblcod0000052.map_seq::text AND c.tbl_name::text = 'utb_role_menu_map'::text
     LEFT JOIN "MP_IPMS_PA".utb_appr_doc ttblcod0000053 ON v.tbl_seq::text = ttblcod0000053.doc_seq::text AND c.tbl_name::text = 'utb_appr_doc'::text
     LEFT JOIN "MP_IPMS_PA".utb_appr_doc_line ttblcod0000054 ON v.tbl_seq::text = ttblcod0000054.line_seq::text AND c.tbl_name::text = 'utb_appr_doc_line'::text
     LEFT JOIN "MP_IPMS_PA".utb_appr_doc_target ttblcod0000055 ON v.tbl_seq::text = ttblcod0000055.target_seq::text AND c.tbl_name::text = 'utb_appr_doc_target'::text
     LEFT JOIN "MP_IPMS_PA".utb_menu_mst ttblcod0000056 ON v.tbl_seq::text = ttblcod0000056.menu_seq::text AND c.tbl_name::text = 'utb_menu_mst'::text;
ALTER TABLE "MP_IPMS_PA"."v_total_duedate_pivot" OWNER TO "mindpro";

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."ipms_user_user_sq_seq"
OWNED BY "MP_IPMS_PA"."ipms_user"."user_sq";
SELECT setval('"MP_IPMS_PA"."ipms_user_user_sq_seq"', 4, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"MP_IPMS_PA"."seq_product_no"', 106504, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."utb_common_code_code_seq_seq"
OWNED BY "MP_IPMS_PA"."utb_common_code"."code_seq";
SELECT setval('"MP_IPMS_PA"."utb_common_code_code_seq_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."utb_connection_connection_seq_seq"
OWNED BY "MP_IPMS_PA"."utb_connection"."connection_seq";
SELECT setval('"MP_IPMS_PA"."utb_connection_connection_seq_seq"', 27, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."utb_document_mst_doc_seq_seq"
OWNED BY "MP_IPMS_PA"."utb_document_mst"."doc_seq";
SELECT setval('"MP_IPMS_PA"."utb_document_mst_doc_seq_seq"', 398, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"MP_IPMS_PA"."utb_duedate_mst_order_seq"', 44043, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."utb_group_code_group_seq_seq"
OWNED BY "MP_IPMS_PA"."utb_group_code"."group_seq";
SELECT setval('"MP_IPMS_PA"."utb_group_code_group_seq_seq"', 30, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."utb_item_manage_mst_item_seq_seq"
OWNED BY "MP_IPMS_PA"."utb_item_manage_mst"."item_seq";
SELECT setval('"MP_IPMS_PA"."utb_item_manage_mst_item_seq_seq"', 125, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "MP_IPMS_PA"."utb_locarno_goods_mst_goods_seq_seq"
OWNED BY "MP_IPMS_PA"."utb_locarno_goods_mst"."goods_seq";
SELECT setval('"MP_IPMS_PA"."utb_locarno_goods_mst_goods_seq_seq"', 10534, true);

-- ----------------------------
-- Primary Key structure for table biz_info_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."biz_info_mapp" ADD CONSTRAINT "pk_biz_info_mapp" PRIMARY KEY ("biz_info_mapp_seq");

-- ----------------------------
-- Uniques structure for table ipms_user
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."ipms_user" ADD CONSTRAINT "ipms_user_user_id_key" UNIQUE ("user_id");

-- ----------------------------
-- Primary Key structure for table ipms_user
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."ipms_user" ADD CONSTRAINT "ipms_user_pkey" PRIMARY KEY ("user_sq");

-- ----------------------------
-- Primary Key structure for table stb_tlb_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."stb_tlb_code" ADD CONSTRAINT "pk_stb_tlb_code" PRIMARY KEY ("tbl_code_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_design
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_design" ADD CONSTRAINT "utb_app_design_pkey" PRIMARY KEY ("office_seq", "app_seq", "design_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_ext_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_ext_mst" ADD CONSTRAINT "pk_utb_app_ext_mst" PRIMARY KEY ("office_seq", "app_ext_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_grace_period
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_grace_period" ADD CONSTRAINT "pk_utb_app_grace_period" PRIMARY KEY ("app_seq", "office_seq", "grace_period_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_ids
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_ids" ADD CONSTRAINT "utb_app_ids_pkey" PRIMARY KEY ("office_seq", "app_seq", "ids_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_inventor
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_inventor" ADD CONSTRAINT "pk_utb_app_inventor" PRIMARY KEY ("app_inventor_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_locarno
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_locarno" ADD CONSTRAINT "pk_utb_app_locarno" PRIMARY KEY ("app_seq", "office_seq", "locarno_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_mst" ADD CONSTRAINT "pk_utb_app_mst" PRIMARY KEY ("office_seq", "app_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_mst_history
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_mst_history" ADD CONSTRAINT "pk_utb_app_mst_history" PRIMARY KEY ("app_history_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_oa
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_oa" ADD CONSTRAINT "pk_utb_app_oa" PRIMARY KEY ("app_seq", "office_seq", "oa_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_patent
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_patent" ADD CONSTRAINT "utb_app_patent_pkey" PRIMARY KEY ("office_seq", "app_seq", "patent_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_preference
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_preference" ADD CONSTRAINT "pk_utb_app_preference" PRIMARY KEY ("app_seq", "office_seq", "preference_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_product
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_product" ADD CONSTRAINT "pk_utb_app_product" PRIMARY KEY ("office_seq", "app_seq", "product_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_rnd
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_rnd" ADD CONSTRAINT "pk_utb_app_rnd" PRIMARY KEY ("app_seq", "office_seq", "rnd_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_specification
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_specification" ADD CONSTRAINT "pk_utb_app_specification" PRIMARY KEY ("specification_seq", "app_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_app_trademark
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_app_trademark" ADD CONSTRAINT "utb_app_trademark_pkey" PRIMARY KEY ("office_seq", "app_seq", "trademark_seq");

-- ----------------------------
-- Primary Key structure for table utb_appr_doc
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_appr_doc" ADD CONSTRAINT "utb_appr_doc_pkey" PRIMARY KEY ("doc_seq");

-- ----------------------------
-- Primary Key structure for table utb_appr_doc_line
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_appr_doc_line" ADD CONSTRAINT "utb_appr_doc_line_pkey" PRIMARY KEY ("line_seq");

-- ----------------------------
-- Primary Key structure for table utb_appr_doc_target
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_appr_doc_target" ADD CONSTRAINT "utb_appr_doc_target_pkey" PRIMARY KEY ("target_seq");

-- ----------------------------
-- Primary Key structure for table utb_appr_template
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_appr_template" ADD CONSTRAINT "pk_utb_appr_template" PRIMARY KEY ("appr_template_seq");

-- ----------------------------
-- Primary Key structure for table utb_appr_template_line
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_appr_template_line" ADD CONSTRAINT "pk_utb_appr_template_line" PRIMARY KEY ("template_line_seq");

-- ----------------------------
-- Primary Key structure for table utb_biz_info
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_biz_info" ADD CONSTRAINT "pk_utb_biz_info" PRIMARY KEY ("biz_info_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_board_backup_hist
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_board_backup_hist" ADD CONSTRAINT "pk_utb_board_backup_hist" PRIMARY KEY ("backup_seq");

-- ----------------------------
-- Primary Key structure for table utb_board_config_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_board_config_mapp" ADD CONSTRAINT "utb_board_master_pkey" PRIMARY KEY ("config_seq", "master_user_id");

-- ----------------------------
-- Primary Key structure for table utb_board_config_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_board_config_mst" ADD CONSTRAINT "utb_board_config_pkey" PRIMARY KEY ("config_seq");

-- ----------------------------
-- Primary Key structure for table utb_board_config_target
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_board_config_target" ADD CONSTRAINT "pk_utb_board_config_target" PRIMARY KEY ("target_seq");

-- ----------------------------
-- Primary Key structure for table utb_board_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_board_mst" ADD CONSTRAINT "utb_board_mst_pkey" PRIMARY KEY ("board_seq");

-- ----------------------------
-- Primary Key structure for table utb_board_system_config
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_board_system_config" ADD CONSTRAINT "pk_utb_board_system_config" PRIMARY KEY ("office_seq");

-- ----------------------------
-- Primary Key structure for table utb_code_dtl
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_code_dtl" ADD CONSTRAINT "pk_utb_code_dtl" PRIMARY KEY ("code_seq");

-- ----------------------------
-- Indexes structure for table utb_code_mst
-- ----------------------------
CREATE UNIQUE INDEX "uk_utb_code_mst_grp" ON "MP_IPMS_PA"."utb_code_mst" USING btree (
  "grp_cd" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE del_yn::text = 'N'::text;

-- ----------------------------
-- Primary Key structure for table utb_code_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_code_mst" ADD CONSTRAINT "pk_utb_code_mst" PRIMARY KEY ("code_seq");

-- ----------------------------
-- Primary Key structure for table utb_comment
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_comment" ADD CONSTRAINT "pk_utb_comment" PRIMARY KEY ("comment_seq");

-- ----------------------------
-- Primary Key structure for table utb_comment_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_comment_mapp" ADD CONSTRAINT "pk_utb_comment_mapp" PRIMARY KEY ("mapping_comment_seq", "board_category_code", "board_seq", "comment_seq");

-- ----------------------------
-- Primary Key structure for table utb_common_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_common_code" ADD CONSTRAINT "utb_common_code_pkey" PRIMARY KEY ("code_seq");

-- ----------------------------
-- Primary Key structure for table utb_conflict_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_conflict_mst" ADD CONSTRAINT "pk_utb_conflict_mst" PRIMARY KEY ("conflict_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_conflict_result
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_conflict_result" ADD CONSTRAINT "pk_utb_conflict_result" PRIMARY KEY ("conflict_result_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_connection
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_connection" ADD CONSTRAINT "pk_utb_connection" PRIMARY KEY ("connection_seq");

-- ----------------------------
-- Uniques structure for table utb_contry_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_contry_code" ADD CONSTRAINT "uk_utb_contry_code" UNIQUE ("ctry_code");

-- ----------------------------
-- Primary Key structure for table utb_contry_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_contry_code" ADD CONSTRAINT "pk_utb_contry_code" PRIMARY KEY ("ctry_seq");

-- ----------------------------
-- Primary Key structure for table utb_cost_external
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_cost_external" ADD CONSTRAINT "pk_utb_cost_external" PRIMARY KEY ("external_cost_seq", "cost_seq");

-- ----------------------------
-- Primary Key structure for table utb_cost_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_cost_mapp" ADD CONSTRAINT "pk_utb_cost_mapp" PRIMARY KEY ("office_seq", "mapping_cost_seq");

-- ----------------------------
-- Primary Key structure for table utb_cost_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_cost_mst" ADD CONSTRAINT "pk_utb_cost_mst" PRIMARY KEY ("office_seq", "cost_seq");

-- ----------------------------
-- Primary Key structure for table utb_customer
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_customer" ADD CONSTRAINT "pk_utb_customer" PRIMARY KEY ("customer_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_customer_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_customer_mapp" ADD CONSTRAINT "pk_utb_customer_mapp" PRIMARY KEY ("customer_mapp_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_dept_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_dept_mst" ADD CONSTRAINT "pk_utb_dept_mst" PRIMARY KEY ("dept_seq");

-- ----------------------------
-- Primary Key structure for table utb_doc_dispatch
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_doc_dispatch" ADD CONSTRAINT "utb_doc_dispatch_pk" PRIMARY KEY ("dispatch_seq");

-- ----------------------------
-- Primary Key structure for table utb_document_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_document_mst" ADD CONSTRAINT "pk_stb_document_mst" PRIMARY KEY ("doc_seq");

-- ----------------------------
-- Indexes structure for table utb_duedate_mapp
-- ----------------------------
CREATE INDEX "idx_duedate_mapp_del" ON "MP_IPMS_PA"."utb_duedate_mapp" USING btree (
  "del_yn" COLLATE "pg_catalog"."default" "pg_catalog"."bpchar_ops" ASC NULLS LAST
);
CREATE INDEX "idx_duedate_mapp_seq" ON "MP_IPMS_PA"."utb_duedate_mapp" USING btree (
  "duedate_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "office_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_duedate_mapp_tbl" ON "MP_IPMS_PA"."utb_duedate_mapp" USING btree (
  "tbl_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tbl_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "udx_duedate_mapp_unique" ON "MP_IPMS_PA"."utb_duedate_mapp" USING btree (
  "office_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tbl_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table utb_duedate_mapp_back
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_duedate_mapp_back" ADD CONSTRAINT "pk_utb_duedate_mapp" PRIMARY KEY ("office_seq", "mapping_duedate_seq");

-- ----------------------------
-- Primary Key structure for table utb_duedate_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_duedate_mst" ADD CONSTRAINT "pk_utb_duedate_mst" PRIMARY KEY ("office_seq", "duedate_seq", "duedate_order");

-- ----------------------------
-- Primary Key structure for table utb_ext_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_ext_mapp" ADD CONSTRAINT "pk_utb_app_ext_mapp" PRIMARY KEY ("ext_mapp_seq");

-- ----------------------------
-- Primary Key structure for table utb_file_history
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_file_history" ADD CONSTRAINT "pk_utb_file_history" PRIMARY KEY ("file_history_seq");

-- ----------------------------
-- Indexes structure for table utb_file_mapp
-- ----------------------------
CREATE INDEX "idx_file_mapp_office_del" ON "MP_IPMS_PA"."utb_file_mapp" USING brin (
  "office_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_minmax_ops",
  "del_yn" COLLATE "pg_catalog"."default" "pg_catalog"."bpchar_minmax_ops"
);
CREATE UNIQUE INDEX "uq_utb_file_mapp_file_kind" ON "MP_IPMS_PA"."utb_file_mapp" USING btree (
  "tbl_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "doc_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "file_kind_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "file_category_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE del_yn = 'N'::bpchar;

-- ----------------------------
-- Primary Key structure for table utb_file_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_file_mapp" ADD CONSTRAINT "pk_utb_file_mapp" PRIMARY KEY ("file_mapp_seq");

-- ----------------------------
-- Primary Key structure for table utb_file_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_file_mst" ADD CONSTRAINT "pk_utb_file_mst" PRIMARY KEY ("file_seq");

-- ----------------------------
-- Primary Key structure for table utb_file_mst_back
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_file_mst_back" ADD CONSTRAINT "pk_utb_file_mst_back" PRIMARY KEY ("file_seq");

-- ----------------------------
-- Primary Key structure for table utb_file_repository
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_file_repository" ADD CONSTRAINT "pk_utb_file_repository" PRIMARY KEY ("file_repository_seq");

-- ----------------------------
-- Primary Key structure for table utb_form_template
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_form_template" ADD CONSTRAINT "pk_utb_form_template" PRIMARY KEY ("form_template_seq");

-- ----------------------------
-- Primary Key structure for table utb_form_template_target
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_form_template_target" ADD CONSTRAINT "pk_utb_form_template_target" PRIMARY KEY ("target_seq");

-- ----------------------------
-- Primary Key structure for table utb_group_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_group_code" ADD CONSTRAINT "utb_group_code_pkey" PRIMARY KEY ("group_seq");

-- ----------------------------
-- Primary Key structure for table utb_invoice_claim
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_invoice_claim" ADD CONSTRAINT "pk_utb_invoice_claim" PRIMARY KEY ("invoice_claim_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_invoice_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_invoice_mst" ADD CONSTRAINT "utb_invoice_mst_pk" PRIMARY KEY ("office_seq", "invoice_seq");

-- ----------------------------
-- Primary Key structure for table utb_invoice_unpaid
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_invoice_unpaid" ADD CONSTRAINT "pk_utb_invoice_unpaid" PRIMARY KEY ("unpaid_seq", "participant_seq", "user_mst_seq", "user_info_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_item_manage_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_item_manage_mst" ADD CONSTRAINT "pk_utb_item_manage_mst" PRIMARY KEY ("item_seq");

-- ----------------------------
-- Primary Key structure for table utb_law_attorney_info
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_law_attorney_info" ADD CONSTRAINT "pk_utb_law_attorney_info" PRIMARY KEY ("user_mst_seq");

-- ----------------------------
-- Primary Key structure for table utb_locarno_goods_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_locarno_goods_mst" ADD CONSTRAINT "utb_locarno_goods_mst_pkey" PRIMARY KEY ("goods_seq");

-- ----------------------------
-- Primary Key structure for table utb_locarno_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_locarno_mst" ADD CONSTRAINT "utb_locarno_mst_pkey" PRIMARY KEY ("class_no", "locarno_version");

-- ----------------------------
-- Primary Key structure for table utb_locarno_subclass_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_locarno_subclass_mst" ADD CONSTRAINT "utb_locarno_subclass_mst_pkey" PRIMARY KEY ("class_no", "subclass_no", "locarno_version");

-- ----------------------------
-- Primary Key structure for table utb_login_history
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_login_history" ADD CONSTRAINT "pk_utb_login_history" PRIMARY KEY ("user_mst_seq", "login_history_seq");

-- ----------------------------
-- Primary Key structure for table utb_login_info
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_login_info" ADD CONSTRAINT "pk_utb_login_info" PRIMARY KEY ("user_mst_seq");

-- ----------------------------
-- Primary Key structure for table utb_maintenance_fee
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_maintenance_fee" ADD CONSTRAINT "pk_utb_maintenance_fee" PRIMARY KEY ("maintenance_fee_seq");

-- ----------------------------
-- Primary Key structure for table utb_memo
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_memo" ADD CONSTRAINT "pk_utb_memo" PRIMARY KEY ("memo_seq");

-- ----------------------------
-- Primary Key structure for table utb_memo_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_memo_mapp" ADD CONSTRAINT "pk_utb_memo_mapp" PRIMARY KEY ("office_seq", "tbl_seq", "memo_seq");

-- ----------------------------
-- Uniques structure for table utb_menu_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_menu_mst" ADD CONSTRAINT "utb_menu_mst_menu_cd_key" UNIQUE ("menu_cd");

-- ----------------------------
-- Primary Key structure for table utb_menu_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_menu_mst" ADD CONSTRAINT "utb_menu_mst_pkey" PRIMARY KEY ("menu_seq");

-- ----------------------------
-- Primary Key structure for table utb_modified_hist
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_modified_hist" ADD CONSTRAINT "pk_utb_modified_hist" PRIMARY KEY ("modified_hist_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_nice_class_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_nice_class_mst" ADD CONSTRAINT "utb_nice_class_mst_pkey" PRIMARY KEY ("class_no", "nice_version");

-- ----------------------------
-- Uniques structure for table utb_office_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_code" ADD CONSTRAINT "uq_office_code" UNIQUE ("office_seq", "code_class", "office_code");

-- ----------------------------
-- Primary Key structure for table utb_office_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_code" ADD CONSTRAINT "pk_utb_office_code" PRIMARY KEY ("office_code_seq");

-- ----------------------------
-- Primary Key structure for table utb_office_default_code
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_default_code" ADD CONSTRAINT "utb_office_default_code_pkey" PRIMARY KEY ("default_code_seq");

-- ----------------------------
-- Indexes structure for table utb_office_employee
-- ----------------------------
CREATE INDEX "idx_utb_office_employee_acct_status" ON "MP_IPMS_PA"."utb_office_employee" USING btree (
  "acct_status_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE del_yn = 'N'::bpchar;
CREATE INDEX "idx_utb_office_employee_role_seq" ON "MP_IPMS_PA"."utb_office_employee" USING btree (
  "role_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE del_yn = 'N'::bpchar;

-- ----------------------------
-- Primary Key structure for table utb_office_employee
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_employee" ADD CONSTRAINT "pk_utb_office_employee" PRIMARY KEY ("office_seq", "office_employee_seq");

-- ----------------------------
-- Indexes structure for table utb_office_menu_map
-- ----------------------------
CREATE INDEX "idx_office_menu_office" ON "MP_IPMS_PA"."utb_office_menu_map" USING btree (
  "office_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "use_yn" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "del_yn" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table utb_office_menu_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_menu_map" ADD CONSTRAINT "uk_office_menu" UNIQUE ("office_seq", "menu_seq");

-- ----------------------------
-- Primary Key structure for table utb_office_menu_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_menu_map" ADD CONSTRAINT "utb_office_menu_map_pkey" PRIMARY KEY ("office_menu_seq");

-- ----------------------------
-- Primary Key structure for table utb_office_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_office_mst" ADD CONSTRAINT "pk_utb_office_mst" PRIMARY KEY ("office_seq");

-- ----------------------------
-- Primary Key structure for table utb_outsourcing_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_outsourcing_mst" ADD CONSTRAINT "pk_utb_outsourcing_mst" PRIMARY KEY ("office_seq", "outsourcing_seq");

-- ----------------------------
-- Primary Key structure for table utb_outsourcing_work
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_outsourcing_work" ADD CONSTRAINT "pk_utb_outsourcing_work" PRIMARY KEY ("office_seq", "outsourcing_work_seq");

-- ----------------------------
-- Primary Key structure for table utb_paper_history
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_paper_history" ADD CONSTRAINT "pk_utb_paper_history" PRIMARY KEY ("paper_history_seq", "paper_category_code");

-- ----------------------------
-- Primary Key structure for table utb_paper_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_paper_mapp" ADD CONSTRAINT "pk_utb_paper_mapp" PRIMARY KEY ("office_seq", "mapping_paper_seq");

-- ----------------------------
-- Primary Key structure for table utb_paper_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_paper_mst" ADD CONSTRAINT "pk_utb_paper_mst" PRIMARY KEY ("office_seq", "paper_seq");

-- ----------------------------
-- Primary Key structure for table utb_participant
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_participant" ADD CONSTRAINT "pk_utb_participant" PRIMARY KEY ("participant_seq", "user_info_seq", "office_seq", "tbl_seq");

-- ----------------------------
-- Primary Key structure for table utb_pat_attorney_info
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_pat_attorney_info" ADD CONSTRAINT "pk_utb_pat_attorney_info" PRIMARY KEY ("user_mst_seq");

-- ----------------------------
-- Primary Key structure for table utb_patent_claim
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_patent_claim" ADD CONSTRAINT "pk_utb_patent_claim" PRIMARY KEY ("patent_claim_seq", "specification_seq", "app_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_performance
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_performance" ADD CONSTRAINT "pk_utb_performance" PRIMARY KEY ("performance_seq", "office_seq");

-- ----------------------------
-- Uniques structure for table utb_plan_menu_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_plan_menu_map" ADD CONSTRAINT "uk_plan_menu" UNIQUE ("plan_seq", "menu_seq");

-- ----------------------------
-- Primary Key structure for table utb_plan_menu_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_plan_menu_map" ADD CONSTRAINT "utb_plan_menu_map_pkey" PRIMARY KEY ("plan_menu_seq");

-- ----------------------------
-- Uniques structure for table utb_plan_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_plan_mst" ADD CONSTRAINT "utb_plan_mst_plan_cd_key" UNIQUE ("plan_cd");

-- ----------------------------
-- Primary Key structure for table utb_plan_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_plan_mst" ADD CONSTRAINT "utb_plan_mst_pkey" PRIMARY KEY ("plan_seq");

-- ----------------------------
-- Primary Key structure for table utb_priorresearch_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_priorresearch_mst" ADD CONSTRAINT "pk_utb_proprresearch_mst" PRIMARY KEY ("office_seq", "priorresearch_seq");

-- ----------------------------
-- Primary Key structure for table utb_priorresearch_result
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_priorresearch_result" ADD CONSTRAINT "pk_utb_proprresearch_result" PRIMARY KEY ("office_seq", "priorresearch_seq", "priorresearch_result");

-- ----------------------------
-- Indexes structure for table utb_product_mst
-- ----------------------------
CREATE INDEX "idx_product_mst_similarity" ON "MP_IPMS_PA"."utb_product_mst" USING btree (
  "similarity_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uidx_product_version" ON "MP_IPMS_PA"."utb_product_mst" USING btree (
  "nice_version" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "class_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "product_nm_ko" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "similarity_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Triggers structure for table utb_product_mst
-- ----------------------------
CREATE TRIGGER "trg_auto_product_id" BEFORE INSERT ON "MP_IPMS_PA"."utb_product_mst"
FOR EACH ROW
EXECUTE PROCEDURE "MP_IPMS_PA"."fn_generate_product_id"();

-- ----------------------------
-- Primary Key structure for table utb_product_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_product_mst" ADD CONSTRAINT "utb_product_mst_pkey" PRIMARY KEY ("product_id");

-- ----------------------------
-- Indexes structure for table utb_progress
-- ----------------------------
CREATE INDEX "idx_progress_app_seq" ON "MP_IPMS_PA"."utb_progress" USING btree (
  "tbl_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "office_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table utb_progress
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_progress" ADD CONSTRAINT "pk_utb_progress" PRIMARY KEY ("office_seq", "progress_seq");

-- ----------------------------
-- Primary Key structure for table utb_required_document
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_required_document" ADD CONSTRAINT "pk_utb_required_document" PRIMARY KEY ("required_doc_seq");

-- ----------------------------
-- Primary Key structure for table utb_retain
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_retain" ADD CONSTRAINT "pk_utb_retain" PRIMARY KEY ("retain_seq");

-- ----------------------------
-- Primary Key structure for table utb_role_menu_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_role_menu_map" ADD CONSTRAINT "utb_role_menu_map_pkey" PRIMARY KEY ("map_seq");

-- ----------------------------
-- Uniques structure for table utb_role_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_role_mst" ADD CONSTRAINT "utb_role_mst_role_cd_key" UNIQUE ("role_cd");

-- ----------------------------
-- Primary Key structure for table utb_role_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_role_mst" ADD CONSTRAINT "utb_role_mst_pkey" PRIMARY KEY ("role_seq");

-- ----------------------------
-- Indexes structure for table utb_search_condition
-- ----------------------------
CREATE INDEX "idx_search_condition_user_menu" ON "MP_IPMS_PA"."utb_search_condition" USING btree (
  "user_info_seq" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "menu_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table utb_search_condition
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_search_condition" ADD CONSTRAINT "utb_search_condition_pk" PRIMARY KEY ("condition_seq");

-- ----------------------------
-- Primary Key structure for table utb_search_field_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_search_field_map" ADD CONSTRAINT "pk_utb_search_field_map" PRIMARY KEY ("search_field_seq");

-- ----------------------------
-- Primary Key structure for table utb_staff
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_staff" ADD CONSTRAINT "pk_utb_staff" PRIMARY KEY ("office_seq", "office_employee_seq", "staff_seq");

-- ----------------------------
-- Primary Key structure for table utb_usage_history
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_usage_history" ADD CONSTRAINT "pk_utb_usage_history" PRIMARY KEY ("usage_history_seq");

-- ----------------------------
-- Primary Key structure for table utb_user_info
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_user_info" ADD CONSTRAINT "pk_utb_user_info" PRIMARY KEY ("user_info_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table utb_user_mst
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_user_mst" ADD CONSTRAINT "pk_utb_user_mst" PRIMARY KEY ("user_mst_seq");

-- ----------------------------
-- Primary Key structure for table utb_user_role
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_user_role" ADD CONSTRAINT "pk_utb_user_role" PRIMARY KEY ("user_role", "user_mst_seq");

-- ----------------------------
-- Primary Key structure for table utb_user_social
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_user_social" ADD CONSTRAINT "pk_utb_user_social" PRIMARY KEY ("auth_seq", "auth_approach");

-- ----------------------------
-- Primary Key structure for table utb_user_social_mapp
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_user_social_mapp" ADD CONSTRAINT "pk_utb_user_social_mapp" PRIMARY KEY ("auth_seq", "auth_approach", "user_mst_seq");

-- ----------------------------
-- Primary Key structure for table utb_wrappermandate
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_wrappermandate" ADD CONSTRAINT "pk_utb_wrappermandate" PRIMARY KEY ("wrappermandate_seq", "office_seq");

-- ----------------------------
-- Primary Key structure for table vector_store
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."vector_store" ADD CONSTRAINT "vector_store_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table utb_role_menu_map
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_role_menu_map" ADD CONSTRAINT "utb_role_menu_map_menu_seq_fkey" FOREIGN KEY ("menu_seq") REFERENCES "MP_IPMS_PA"."utb_menu_mst" ("menu_seq") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "MP_IPMS_PA"."utb_role_menu_map" ADD CONSTRAINT "utb_role_menu_map_role_seq_fkey" FOREIGN KEY ("role_seq") REFERENCES "MP_IPMS_PA"."utb_role_mst" ("role_seq") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table utb_user_info
-- ----------------------------
ALTER TABLE "MP_IPMS_PA"."utb_user_info" ADD CONSTRAINT "fk_utb_user_info" FOREIGN KEY ("user_mst_seq") REFERENCES "MP_IPMS_PA"."utb_user_mst" ("user_mst_seq") ON DELETE SET NULL ON UPDATE NO ACTION;

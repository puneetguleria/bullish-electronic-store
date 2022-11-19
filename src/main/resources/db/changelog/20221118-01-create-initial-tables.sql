-- liquibase formatted sql
-- changeset puneet:20221118-01-create-initial-tables

CREATE TABLE "ELECTRONIC_STORE"."PRODUCT"
(
  "ID"             BIGINT NOT NULL,
  "CODE"     UUID NOT NULL,
  "NAME"        VARCHAR(255) NOT NULL,
  "PRICE"           BIGINT NOT NULL,
  "LAST_MODIFIED_AT"     TIMESTAMP NOT NULL,
  PRIMARY KEY ("ID")
);

CREATE TABLE "ELECTRONIC_STORE"."DISCOUNT_DEAL"
(
  "ID"             BIGINT NOT NULL,
  "CODE"     UUID NOT NULL,
  "PRODUCT_CODE"     UUID,
  "DEAL_TYPE"        VARCHAR(50) NOT NULL,
  "DISCOUNT_TYPE"           VARCHAR(50) NOT NULL,
  "RATE"             BIGINT NOT NULL,
  "LAST_MODIFIED_AT"     TIMESTAMP NOT NULL,
  PRIMARY KEY ("ID")
);

CREATE TABLE "ELECTRONIC_STORE"."SHOPPING_BASKET"
(
  "ID"             BIGINT NOT NULL,
  "CODE"     UUID NOT NULL,
  "LAST_MODIFIED_AT"     TIMESTAMP NOT NULL,
  PRIMARY KEY ("ID")
);

CREATE TABLE "ELECTRONIC_STORE"."SHOPPING_BASKET_ITEM"
(
  "ID"             BIGINT NOT NULL,
  "PRODUCT_CODE"     UUID NOT NULL,
  "SHOPPING_BASKET_ID" BIGINT NOT NULL,
  "LAST_MODIFIED_AT"     TIMESTAMP NOT NULL,
  PRIMARY KEY ("ID")
);

ALTER TABLE "ELECTRONIC_STORE"."SHOPPING_BASKET_ITEM"
  ADD CONSTRAINT "FK_SHOPPING_BASKET_ID" FOREIGN KEY ("SHOPPING_BASKET_ID") REFERENCES "ELECTRONIC_STORE"."SHOPPING_BASKET" ("ID");

CREATE SEQUENCE ELECTRONIC_STORE.HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;
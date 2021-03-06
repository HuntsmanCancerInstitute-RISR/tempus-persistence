/****** Script for SelectTopNRows command from SSMS  ******/

Use MolecularProfiling
BEGIN TRANSACTION

Drop Table TpsIhcFinding;


--- no sense in having a table if you are only capturing 'status' which is all ready a part of the result table
--- yet we will make it just incase they add more fields to it
CREATE TABLE TpsMicrosatelliteInstability (
	idMSI BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	status VARCHAR(20),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_msi FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE TpsHrdFinding(
	idHrdFinding BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	hrdResult VARCHAR(10),
	genomeWideLoh DECIMAL(4,2),
	cohortSpecificGenomeWideLohThreshold INT,
	idResult BIGINT NOT NULL,
	CONSTRAINT result_hrdfindings FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);
CREATE TABLE TpsPertinentNegativeGene(
	idPertinentNegativeGene BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene VARCHAR(20),
	symbol VARCHAR(20),
	hgncId VARCHAR(20),
	entrezId VARCHAR(20),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_pertneggenes FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE TpsIhcFinding (
	idIhcFinding BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	antigenName VARCHAR(20),
    antigenPdl1clone VARCHAR(10),
    mmrInterpretation VARCHAR(15),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_ihcfinding FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)

);


CREATE TABLE TpsIhcAntigen (
	idIhcAntigen BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    interpretation VARCHAR(15),
    percentTumorCellStaining INT,
    percentImmuneCellStaining INT,
    tumorProportionScore VARCHAR(10),
    combinedPositiveScore VARCHAR(10),
	idIhcFinding BIGINT NOT NULL,
	CONSTRAINT ihcfinding_ihcantigen FOREIGN KEY (idIhcFinding) REFERENCES TpsIhcFinding (idIhcFinding)

);
CREATE TABLE TpsIhcMmr (
	idIhcMmr BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    protein VARCHAR(20),
    result VARCHAR(20),
	idIhcFinding BIGINT NOT NULL,
	CONSTRAINT ihcfinding_ihcmmr FOREIGN KEY (idIhcFinding) REFERENCES TpsIhcFinding (idIhcFinding)

);
CREATE TABLE TpsRnaFinding (
        idRnaFinding BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
        gene VARCHAR(20),
        hgncId VARCHAR(20),
        entrezId VARCHAR(20),
        mechanism VARCHAR(20),
        idResult BIGINT NOT NULL,
        CONSTRAINT result_rnafinding FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);


ALTER TABLE TpsInheritedVariantValue
ADD geneDescription varchar(500);

ALTER TABLE TpsSPActionableCPVariant
ADD geneDescription varchar(500);

ALTER TABLE TpsSPActionableMutation
ADD geneDescription varchar(500);

ALTER TABLE TpsSPBioRelevantVariant
ADD geneDescription varchar(500);

ALTER TABLE TpsInheritedVariantValue
ADD geneDescription varchar(500);

ALTER TABLE TpsSVUknownSignificance
ADD geneDescription varchar(500);

ALTER TABLE TpsFusionVariant
    ADD geneDescription varchar(500);
ALTER TABLE TpsTest
    ALTER COLUMN description VARCHAR (250);

ALTER TABLE TpsInheritedVariantValue
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsSPActionableCPVariant
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsSPActionableMutation
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsSPBioRelevantVariant
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsInheritedVariantValue
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsSVUknownSignificance
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsFusionVariant
    ALTER COLUMN geneDescription VARCHAR (2000);

ALTER TABLE TpsMicrosatelliteInstability
    ALTER COLUMN status VARCHAR (50);

ALTER TABLE TpsPatient
    ALTER COLUMN sex VARCHAR (20);


COMMIT TRANSACTION











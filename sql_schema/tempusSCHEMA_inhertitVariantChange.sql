/****** Script for SelectTopNRows command from SSMS  ******/

Use MolecularProfiling
BEGIN TRANSACTION


Drop Table TpsTest;
Drop Table TpsOrder;
Drop Table TpsPatient;
Drop Table TpsSpecimen;
Drop Table TpsReport;
Drop Table TpsVariant;
Drop Table TpsSPActionableMutation;
Drop Table TpsSPActionableCPVariant;
Drop Table TpsSPBioRelevantVariant;
Drop Table TpsSVUknownSignificance;
Drop Table TpsInheritedVariantValue;
Drop Table TpsInheritedVariant;
Drop Table TpsLowCoverageAmplicon;
Drop Table TpsFusionVariant;
Drop Table TpsIhcFinding;
Drop Table TpsResult;
Drop Table TpsTempusFile;



CREATE TABLE TpsTempusFile(
	idTempusFile BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	tempusJsonId VARCHAR(100)
);

CREATE TABLE TpsOrder (
	idOrder BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	institution VARCHAR(100),
	physician VARCHAR(100),
	accessionId VARCHAR(100),
	idTempusFile BIGINT NOT NULL,
	CONSTRAINT fk_tempus_file_order FOREIGN KEY (idTempusFile) REFERENCES TpsTempusFile (idTempusFile)
);

CREATE TABLE TpsTest(
	idTest BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	code VARCHAR(100),
	name VARCHAR(100),
	description VARCHAR(100),
	idOrder BIGINT NOT NULL,
	CONSTRAINT fk_order_test FOREIGN KEY (idOrder) REFERENCES TpsOrder (idOrder)
);

CREATE TABLE TpsPatient(
	idPatient BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	firstName VARCHAR(100),
	lastName VARCHAR(100),
	tempusId VARCHAR(100),
    emrId VARCHAR(100),
    sex VARCHAR(10),
    dateOfBirth VARCHAR(100),
    diagnosis VARCHAR(100),
    diagnosisDate VARCHAR(100),
	HCIPersonID BIGINT,
	BSTShadowID VARCHAR(50),
	comment VARCHAR(250),
	idTempusFile BIGINT NOT NULL,
	CONSTRAINT fk_tempus_file_patient FOREIGN KEY (idTempusFile) REFERENCES tpstempusfile (idTempusFile)
);

CREATE TABLE  TpsSpecimen(
	idSpecimen BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	tempusSampleId VARCHAR(100),
	collectionDate VARCHAR(100),
	sampleCategory VARCHAR(100),
    sampleSite VARCHAR(100),
    sampleType VARCHAR(100),
    notes VARCHAR(1000),
    tumorPercentage INT,
	idTempusFile BIGINT NOT NULL,
	CONSTRAINT fk_tempus_file_specimen FOREIGN KEY (idTempusFile) REFERENCES tpstempusfile (idTempusFile)
);

CREATE TABLE TpsReport(
	idReport BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	reportId VARCHAR(100),
	reportStatus VARCHAR(250),
	signout_date VARCHAR(250),
    bioInfPipeline VARCHAR(250),
    notes VARCHAR(2500),
	idTempusFile BIGINT NOT NULL,
	CONSTRAINT fk_tempus_file_report FOREIGN KEY (idTempusFile) REFERENCES tpstempusfile (idTempusFile)
);

CREATE TABLE  TpsResult(
	idResult BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	tumorMutationalBurden DECIMAL(12,8),
	tumorMutationBurdenPercentile INT,
	msiStatus VARCHAR(250),
	idTempusFile BIGINT NOT NULL,
	CONSTRAINT fk_tempus_file_result FOREIGN KEY (idTempusFile) REFERENCES tpstempusfile (idTempusFile)
);

CREATE TABLE TpsSPActionableMutation (
	idSPActionableMutation BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene VARCHAR(250),
	display VARCHAR(250),
	hgncId VARCHAR(250),
    entrezId VARCHAR(250),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_action_mutation FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE  TpsVariant (
	idVariant BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	mutationEffect VARCHAR(250),
	HGVSp VARCHAR(250),
    HGVSpFull VARCHAR(250),
	HGVSc VARCHAR(250),
	transcript VARCHAR(250),
	nucleotideAlteration VARCHAR(3000),
	referenceGenome VARCHAR(250),
    allelicFraction VARCHAR(250),
    variantDescription VARCHAR(250),
	coverage INT,
	idSPActionableMutation BIGINT NOT NULL,
	CONSTRAINT action_mutation_variant FOREIGN KEY (idSPActionableMutation) REFERENCES TpsSPActionableMutation (idSPActionableMutation)
);
CREATE TABLE TpsSPActionableCPVariant (
	idSPActionableCPVariant BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene VARCHAR(250),
	display VARCHAR(250),
	hgncId VARCHAR(250),
    entrezId VARCHAR(250),
    variantDescription VARCHAR(250),
    variantType VARCHAR(250),
    copyNumber INT,
	idResult BIGINT NOT NULL,
	CONSTRAINT result_cpvariant FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE TpsSPBioRelevantVariant (
	idSPBioRelevantVariant BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene VARCHAR(250),
	display VARCHAR(250),
	hgncId VARCHAR(250),
    entrezId VARCHAR(250),
	gene5 VARCHAR(250),
    gene5display VARCHAR(250),
    gene5hgncId VARCHAR(250),
    gene5entrezId VARCHAR(250),
    gene3 VARCHAR(250),
    gene3display VARCHAR(250),
    gene3hgncId VARCHAR(250),
    gene3entrezId VARCHAR(250),
    variantType VARCHAR(250),
    variantDescription VARCHAR(250),
    fusionType VARCHAR(250),
    structuralVariant VARCHAR(250),
    mutationEffect VARCHAR(250),
    HGVSp VARCHAR(250),
    HGVSpFull VARCHAR(250),
	HGVSc VARCHAR(250),
	transcript VARCHAR(250),
    nucleotideAlteration VARCHAR(3000),
    allelicFraction VARCHAR(250),
    coverage INT,
    copyNumber INT,
	idResult BIGINT NOT NULL,
	CONSTRAINT result_biovariant FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE TpsSVUknownSignificance (
	idSVUknownSignificance BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene VARCHAR(250),
	display VARCHAR(250),
	hgncId VARCHAR(250),
    entrezId VARCHAR(250),
    variantType VARCHAR(250),
    variantDescription VARCHAR(250),
    mutationEffect VARCHAR(250),
    HGVSp VARCHAR(250),
    HGVSpFull VARCHAR(250),
	HGVSc VARCHAR(250),
	transcript VARCHAR(250),
    nucleotideAlteration VARCHAR(7500),
    allelicFraction VARCHAR(250),
    coverage INT,
	idResult BIGINT NOT NULL,
	CONSTRAINT result_unknownSignificance FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE TpsInheritedVariant (
	idInheritedVariant BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	note VARCHAR(1000),
    variantCategory VARCHAR(250),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_inheritedvariant FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

CREATE TABLE TpsInheritedVariantValue(
	idInheritedVariantValue BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene VARCHAR(250),
	display VARCHAR(250),
	hgncId VARCHAR(250),
    entrezId VARCHAR(250),
    mutationEffect VARCHAR(250),
    HGVSp VARCHAR(250),
    HGVSpFull VARCHAR(250),
    HGVSc VARCHAR(250),
	transcript VARCHAR(250),
    variantDescription VARCHAR(250),
    clinicalSignificance VARCHAR(250),
    disease VARCHAR(250),
    allelicFraction VARCHAR(250),
    coverage INT,
    chromosome VARCHAR(250),
	ref VARCHAR(250),
    alt VARCHAR(250),
    pos  VARCHAR(250),
	idInheritedVariant BIGINT NOT NULL,
	CONSTRAINT	inheritedvariant_inhertedvariantvalue FOREIGN KEY(idInheritedVariant) REFERENCES TpsInheritedVariant(idInheritedVariant)
);


CREATE TABLE TpsLowCoverageAmplicon(
	idLowCoverageAmplicon BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	amplicon VARCHAR(250),
	display VARCHAR(250),
	hgncId VARCHAR(250),
    entrezId VARCHAR(250),
	coverage INT,
	idResult BIGINT NOT NULL,
	CONSTRAINT result_amplicon FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)

);

CREATE TABLE TpsFusionVariant (
	idFusionVariant BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	gene5 VARCHAR(250),
    gene5display VARCHAR(250),
    gene5hgncId VARCHAR(250),
    gene5entrezId VARCHAR(100),
    gene3 VARCHAR(100),
    gene3display VARCHAR(100),
    gene3hgncId VARCHAR(100),
    gene3entrezId VARCHAR(100),
    variantDescription VARCHAR(250),
    fusionType VARCHAR(100),
    structuralVariant VARCHAR(100),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_fusionvariant FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);


CREATE TABLE TpsIhcFinding(
	idIhcFinding BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	strHello VARCHAR(100),
	idResult BIGINT NOT NULL,
	CONSTRAINT result_ihcfinding FOREIGN KEY (idResult) REFERENCES TpsResult (idResult)
);

COMMIT TRANSACTION



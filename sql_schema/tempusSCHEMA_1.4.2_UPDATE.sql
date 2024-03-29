/****** Script for SelectTopNRows command from SSMS  ******/

Use MolecularProfiling
BEGIN TRANSACTION

 ALTER TABLE TpsHrdFinding
	ADD percentGenomeWideLoh VARCHAR(10),
	    percentCohortSpecificGenomeWideLohThreshold VARCHAR(10),
		brcaDoubleHit VARCHAR(100),
		hrdAnalysisType VARCHAR(5),
		rnaExpressionHrdScore VARCHAR(10),
		rnaExpressionThreshold VARCHAR(10);
-- although sampleType has been dropped in newer json schema we are using it in db but grabbing out contentsReceivedLabel value and saving in sampleType.
-- For schema version > 1.4 thus no need to add a new column in TpsSpecimen for contentsReceivedLabel
  ALTER TABLE TpsSpecimen DROP COLUMN tempusSampleId;


COMMIT TRANSACTION











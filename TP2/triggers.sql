CREATE OR REPLACE TRIGGER TRG_UPD_MALADIE_Q2_1
AFTER INSERT OR DELETE ON LesMaladies
FOR EACH ROW
Begin  
	IF INSERTING Then
    	UPDATE LesAnimaux SET nb_maladies = nb_maladies+1 WHERE nomA = :new.nomA;

    ELSIF DELETING Then
    	UPDATE LesAnimaux SET nb_maladies = nb_maladies-1 WHERE nomA = :old.nomA;
    END IF;
End;
/

CREATE OR REPLACE TRIGGER TRG_CHECK_CAGE_Q2_2
AFTER INSERT OR UPDATE OF noCage ON LesAnimaux
FOR EACH ROW
Declare
	str VARCHAR2(100);
Begin
	SELECT Fonction INTO str
	FROM LesCages
	WHERE noCage = :new.noCage;

	IF str <> :new.fonction_cage THEN
		RAISE_APPLICATION_ERROR(-20000,'cohabitation impossible entre animaux de types different');
	END IF;
End;
/

CREATE OR REPLACE TRIGGER TRG_CAGE_GARDEE_Q2_3
AFTER INSERT OR UPDATE OF noCage ON LesAnimaux
FOR EACH ROW
Declare
	nb NUMBER;
Begin
	SELECT COUNT(*) INTO nb
	FROM LesGardiens
	WHERE noCage = :new.noCage;

	IF nb = 0 THEN
		RAISE_APPLICATION_ERROR(-20001,'deplacement impossible dans une cage non gardee');
	END IF;
End;
/

CREATE OR REPLACE TRIGGER TRG_CHECK_TYPE_Q2_4
AFTER INSERT OR UPDATE OF noCage ON LesAnimaux
Declare
	verify NUMBER;
Begin
	SELECT COUNT(*) into verify
	FROM (	SELECT noCage, COUNT(DISTINCT type_an)
			FROM LesAnimaux
			GROUP BY noCage
			HAVING COUNT(DISTINCT type_an) > 1 );

	IF (verify > 0) THEN
		RAISE_APPLICATION_ERROR(-20002,	'cohabitation impossible entre animaux de types differents');
	END IF;
End;
/
CREATE OR REPLACE TRIGGER TRG_UPD_MALADIE
AFTER INSERT OR DELETE ON LesAnimaux
FOR EACH ROW
Begin  
	IF INSERTING Then
    	UPDATE LesAnimaux SET nb_maladies = nb_maladies+1 WHERE nomA = :new.nomA;

    ELSIF DELETING Then
    	UPDATE LesAnimaux SET nb_maladies = nb_maladies-1 WHERE nomA = :old.nomA;
    END IF;
End;
/
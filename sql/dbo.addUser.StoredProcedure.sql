USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[addUser]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[addUser](
	@user varchar(30),
	@email varchar(50),
	@pass varchar(MAX))
AS

--Checks if username is already in database, returns error if so
IF EXISTS (SELECT * FROM Merchant WHERE Username = @user OR Email = @email)
	BEGIN
		PRINT 'User already exists. Please enter new parameters.'
		RETURN 1
	END
ELSE
	BEGIN
	--Insert values into Merchant table
	--No need for GUID, generated by SQL Server
	DECLARE @id varchar(MAX)
	SET @id = NEWID()

	INSERT INTO Merchant(MerchantUID, Username, PasswordHash, Email)
	VALUES (@id, @user, @pass, @email)

	--Check for errors, taaken from CSSE333 Lab 7 - Stored Procedures
	DECLARE @Status SMALLINT
	SET @Status = @@ERROR
	IF @Status != 0
		BEGIN
			PRINT 'INsertion into Merchant failed. Error Code is: ' + CONVERT(varchar(30), @Status)
			RETURN @Status
		END
	--Insert Successful, return
	RETURN 0
	END
GO

USE [GMM]
GO
/****** Object:  User [holman]    Script Date: 5/19/2017 12:54:42 AM ******/
CREATE USER [holman] FOR LOGIN [holmanah] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [holman]
GO

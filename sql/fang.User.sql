USE [GMM]
GO
/****** Object:  User [fang]    Script Date: 5/19/2017 12:54:42 AM ******/
CREATE USER [fang] FOR LOGIN [fangj] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [fang]
GO

USE [GMM]
GO
/****** Object:  StoredProcedure [dbo].[isItemInShop]    Script Date: 5/19/2017 12:54:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE procedure [dbo].[isItemInShop]
	(@itemID int,
	@shopID int,
	@result bit output)
as

if (select count(*) from ItemsByStorefront where ItemID=@itemID and ShopID=@shopID) = 0
begin
	set @result = 0
end
else
begin
	set @result = 1
end
GO

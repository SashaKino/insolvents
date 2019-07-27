
CREATE TABLE [dbo].[bankrots](
	[FirstName] [nvarchar](40) NULL,
	[MiddleName] [nvarchar](40) NULL,
	[LastName] [nvarchar](80) NULL,
	[Address] [nvarchar](150) NULL,
	[INN] [bigint] NULL,
	[birthdate] [nvarchar](40) NULL,
	[Birthplace] [nvarchar](150) NULL,
	[CaseNumber] [nvarchar](40) NULL,
	[CDId] [nvarchar](8) NULL,
--	[MessageType] [nvarchar](40) NULL
) ON [PRIMARY]


CREATE TABLE [dbo].[creditors](
	[INN] [bigint] NULL,
	[CreditorName] [nvarchar](100) NULL,
	[DemandSum] [nvarchar](30) NULL,
	[DemandDate] [nvarchar](30) NULL,
	[ReasonOccurence] [nvarchar](70) NULL
)


CREATE TABLE [dbo].[mfo_demands](

	[INN] [bigint] NULL,
	[FirstName] [nvarchar](40) NULL,
	[MiddleName] [nvarchar](40) NULL,
	[LastName] [nvarchar](80) NULL,
	[Address] [nvarchar](150) NULL,
	[birthdate] [nvarchar](40) NULL,
	[Birthplace] [nvarchar](150) NULL,
	[CreditorName] [nvarchar](100) NULL,
	[DemandSum] [nvarchar](30) NULL,
	[DemandDate] [nvarchar](30) NULL,
	[ReasonOccurence] [nvarchar](70) NULL

)











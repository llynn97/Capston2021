module.exports = {
  server_port: 3000,
  db_url: "mongodb+srv://ServerMJ:4bdf1234@cluster0.4xdnv.mongodb.net/test",
  db_schemas: [
    {
      file: "./member_schema",
      collection: "Member",
      schemaName: "MemberSchema",
      modelName: "MemberModel",
    },
    {
      file: "./itemlist_schema",
      collection: "ItemList",
      schemaName: "ItemListSchema",
      modelName: "ItemListModel",
    },
    {
      file: "./category_schema",
      collection: "Category",
      schemaName: "CategorySchema",
      modelName: "CategoryModel",
    },
    {
      file: "./betcashbook_schema",
      collection: "BetCashBook",
      schemaName: "BetCashBookSchema",
      modelName: "BetCashBookModel",
    },
  ],
  route_info: [
    {
      file: "./login",
      path: "/process/login",
      method: "authuser",
      type: "post",
    },
    {
      file: "./login",
      path: "/process/register",
      method: "register",
      type: "post",
    },
    {
      file: "./login",
      path: "/process/validate",
      method: "validate",
      type: "post",
    },
    {
      file: "./itemlist",
      path: "/process/additemlist",
      method: "additemlist",
      type: "post",
    },
    {
      file: "./itemlist",
      path: "/process/deleteitemlist",
      method: "deleteitemlist",
      type: "post",
    },
    {
      file: "./itemlist",
      path: "/process/deleteitem",
      method: "deleteitem",
      type: "post",
    },

    {
      file: "./itemlist",
      path: "/process/showitemlist/:id/:year/:month/:date",
      method: "showitemlist",
      type: "get",
    },
    {
      file: "./itemlist",
      path: "/process/modifyitemlist",
      method: "modifyitemlist",
      type: "post",
    },
    {
      file: "./betcashbook",
      path: "/process/addbetcashbook",
      method: "addbetcashbook",
      type: "post",
    },
      {
      file: "./betcashbook",
      path: "/process/randomcodecheck",
      method: "randomcodecheck",
      type: "post",
    },
      {
      file: "./betcashbook",
      path: "/process/showbetcashbook/:id",
      method: "showbetcashbook",
      type: "get",
    },
      {
      file: "./betcashbook",
      path: "/process/showdetailbetcashbook/:inviteCode",
      method: "showdetailbetcashbook",
      type: "get",
    },
        {
      file: "./betcashbook",
      path: "/process/deletebetcashbook/:id",
      method: "deletebetcashbook",
      type: "post",
    },
      {
      file: "./betcashbook",
      path: "/process/invitebetcashbook",
      method: "invitebetcashbook",
      type: "post",
    },
      {
      file: "./category",
      path: "/process/addcategory",
      method: "addcategory",
      type: "post",
    },
  ],
  jsonrpc_api_path: "/api",
};

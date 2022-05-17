var Schema = {};

Schema.createSchema = function (mongoose) {
  var BetCashBookSchema = mongoose.Schema({
    groupname: { type: String, default: "" },
    goal: { type: String, default: "" },
    goalprice: { type: Number, default: "" },
    reward: { type: String, default: "" },
    penalty: { type: String, default: "" },
    startDay: { type: String, default: "" },
    endDay: { type: String, default: "" },
    id: { type: Array, default: "" },
    category: { type: String, default: "" },
    inviteCode: { type: String, default: "" },
  });
  return BetCashBookSchema;
};

module.exports = Schema;

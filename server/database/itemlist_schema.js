var Schema = {};

Schema.createSchema = function (mongoose) {

  var ItemListSchema = mongoose.Schema({
    title: { type: String, default: "" },
    itemname: { type: String, required: true, default: "" },
    price: { type: Number, default: "" },
    category: { type: String, default: "" },
    paymethod: { type: String, default: "" },
    profit: { type: String, default: "" },
    amount: { type: Number, default: "" },
    wholeDay: { type: Date, default: "" },
    year: { type: Number, default: "" },
    month: { type: Number, default: "" },
    date: { type: Number, default: "" },
    id: { type: String, default: "" },
  });

  ItemListSchema.static("findAll", function (id, callback) {
    return this.find({}, callback);
  });


  return ItemListSchema;
};

module.exports = Schema;

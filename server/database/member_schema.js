var Schema = {};

Schema.createSchema = function(mongoose) {

	var MemberSchema = mongoose.Schema({
	    id: {type: String, 'default':''},
        password:{type:String,required:true,'default':''},
	    name: {type: String, index: 'hashed', 'default':''},
        photoname:{type: String, 'default':''},
        budget:{type:Number , 'default':0}    
	});
	
	var validatePresenceOf = function(value) {
		return value && value.length;
	};
		
	MemberSchema.pre('save', function(next) {
		if (!this.isNew) return next();
	
		if (!validatePresenceOf(this.password)) {
			next(new Error('유효하지 않은 password 필드입니다.'));
		} else {
			next();
		}
	})
	
   MemberSchema.path('id').validate(function(id){
       return id.length;
   },'id칼럼의 값이 없습니다');
	

	// 스키마에 static 메소드 추가
	MemberSchema.static('findById', function(id, callback) {
		return this.find({id:id}, callback);
	});
	
	MemberSchema.static('findAll', function(callback) {
		return this.find({}, callback);
	});
	
	console.log('RegisterSchema 정의함.');

	return MemberSchema;
};

module.exports = Schema;
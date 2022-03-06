var local_login = require('./passport/local_login');
var local_signup = require('./passport/local_signup');
var local_validate = require('./passport/local_validate');

module.exports = function (app, passport) {
	console.log('config/passport 호출됨.');

    // 사용자 인증 성공 시 호출
    // 사용자 정보를 이용해 세션을 만듦
    
    passport.serializeUser(function(user, done) {
        console.log('serializeUser() 호출됨.');
        console.dir(user);

        done(null, user);  // 이 인증 콜백에서 넘겨주는 user 객체의 정보를 이용해 세션 생성
    });

    // 사용자 인증 이후 사용자 요청 시마다 호출
    // user -> 사용자 인증 성공 시 serializeUser 메소드를 이용해 만들었던 세션 정보가 파라미터로 넘어온 것임
    passport.deserializeUser(function(user, done) {
        console.log('deserializeUser() 호출됨.');
        console.dir(user);

        
        done(null, user);  
    });

	// 인증방식 설정
	passport.use('local_login', local_login);
	passport.use('local_signup', local_signup);
    passport.use('local_validate',local_validate);
};
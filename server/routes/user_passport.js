

module.exports = function(router,passport) {
  console.log('user_passport 호출됨.');
    router.route('/process/login1').post(function(req,res,next){
        passport.authenticate('local_login',function(err,user,info){
            if(err){
                return next(err);
                
            }
            if(!user){ 
                res.writeHead('200',{'Content-Type':'application/json;charset=utf8'});
                res.write({"success":false});
                res.end();
            }
            if(user){
                res.writeHead('200',{'Content-Type':'application/json;charset=utf8'});
                res.write({"success":true});
                res.end();
            }
        });
        
    });
    
    router.route('/process/validate1').post(function(req,res,next){
        passport.authenticate('local_signup',function(err,user,info){
            if(err){
                return next(err);
                
            }
            if(!user){ 
                res.writeHead('200',{'Content-Type':'application/json;charset=utf8'});
                res.write({"success":false});
                res.end();
            }else{
                res.writeHead('200',{'Content-Type':'application/json;charset=utf8'});
                res.write({"success":true});
                res.end();
            }
        });
        
    });
    
  
    
    router.route('/process/register1').post(function(req,res,next){
        passport.authenticate('local_signup',function(err,user,info){
            if(err){
                return next(err);
                
            }
            if(!user){ 
                res.writeHead('200',{'Content-Type':'application/json;charset=utf8'});
                res.write({"success":false});
                res.end();
            }else{
                res.writeHead('200',{'Content-Type':'application/json;charset=utf8'});
                res.write({"success":true});
                res.end();
            }
        });
        
    });
    
};



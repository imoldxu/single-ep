define(['app'], function(app){
    return app.directive('setFocus',[ function(){
        return {
         scope:false,
         link:function(scope, element){      
            element[0].focus(); //获取焦点
         }
        };
    }]);
});
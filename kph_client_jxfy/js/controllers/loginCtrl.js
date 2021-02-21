
define([], function(){

    return ['$scope', '$http', '$cookieStore','$location','$rootScope','dataVer' ,'$state', function($scope, $http, $cookieStore,$location,$rootScope,dataVer,$state){

        //默认值


        $scope.login = function(cardNo){

            if (cardNo == '' || cardNo == undefined){
                alert('请输入就诊卡号')
				return false;
            }
			$rootScope.myloader = true;
			
            $http({
                method: 'get',
                url: URL+'prescription/init',
                requestType: 'json',
                params: {
                    cardNo: cardNo
                }
            })
            .success(function(resp){
				
				$rootScope.myloader = false;

                if (resp.code == 1){

                    dataVer.put('drugInfo','');

                    dataVer.put('userInfo',resp.data);


                    if (resp.data.doctorname || ''){


                        var docArray = {

                            doctorname : resp.data.doctorname,

                            department : resp.data.department

                        }

                        dataVer.put('docArray',docArray);

                    }

                    $state.go('home');
					// if(type==1){
                    //     $state.go('home');
					// }else{
					//     $state.go('zyhome');
					// }
                    return false

                }else{

					alert(data.msg);

					console.log(data)
				}

            })
			.error(function(resp){
				
				$rootScope.myloader = false;
                
				alert(resp.message);
				
			})
        }
    }];

});

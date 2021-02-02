
define(['angular','layer'], function(angular,layer){

    return ['$scope', '$http', '$window','$location','$rootScope','dataVer' ,'$state', function($scope, $http, $window,$location,$rootScope,dataVer,$state){

        //默认值

        $scope.userArry = dataVer.get('userInfo');

        $scope.drugArry = dataVer.get('drugInfo');

        var date = new Date();

        $scope.dateTime = date.getFullYear() +"/"+(date.getMonth()+1) + '/' + date.getDate();

		$scope.nowTime = date.getFullYear() +"-"+(date.getMonth()+1) + '-' + date.getDate() + ' ' +date.getHours()+':'+date.getMinutes();
		
        $scope.price = 0;

        angular.forEach($scope.drugArry, function(data,index,array){


            $scope.price  = $scope.price  + (parseFloat(data.price)*data.number)*$scope.userArry.zynum;


        });

        $scope.goBack = function(){

            $window.history.back()

        };



        $scope.commit = function(){
		
			$rootScope.myloader = true;
			
			$http({
				method: 'post',

				url: URL+'prescition/open',

				requestType: 'json',

				params: {

					prescriptionInfo: JSON.stringify($scope.userArry),

					drugList: JSON.stringify($scope.drugArry)
				}
			})
			.success(function(data){
				
				$rootScope.myloader = false;

				if (data.code == 1){
			
					
					var docArray = {

						doctorname : $scope.userArry.doctorname,

						department : $scope.userArry.department

					}

					dataVer.put('docArray',docArray);

					if(document.execCommand("print") == true){

						layer.confirm('是否完成打印？', {
							btn: ['确定','取消'] //按钮
						}, function(){
							setTimeout(function(){

								layer.closeAll();

								$state.go('welcome')

							},500)

						});

					}else{
						alert("打印服务异常，请联系管理员");
					}
					
				}else{
				
					alert(data.msg);

					console.log(data)
				}
			
			})
			.error(function(data){
			
				$rootScope.myloader = false;
			
				alert('系统服务异常，请联系管理员');
			
			});
		
           
        }
    }];

});

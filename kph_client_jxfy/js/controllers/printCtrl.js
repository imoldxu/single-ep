
define(['angular','layer'], function(angular,layer){

    return ['$scope', '$http', '$window','$location','$rootScope','dataVer' ,'$state', function($scope, $http, $window,$location,$rootScope,dataVer,$state){

        //默认值

        $scope.userArry = dataVer.get('userInfo');

        $scope.drugArry = dataVer.get('drugInfo');

        var date = new Date();

        $scope.createTime = date.getFullYear() +"-"+(date.getMonth()+1) + '-' + date.getDate();

		$scope.nowTime = date.getFullYear() +"-"+(date.getMonth()+1) + '-' + date.getDate() + ' ' +date.getHours()+':'+date.getMinutes();
		
		$scope.price = 0;
		
		let paycodeStr = JSON.stringify({"PatientName": $scope.userArry.patientname,"PatientNo": $scope.userArry.regNo})

		$scope.paycode = urlSateBase64Encode($window.btoa($window.encodeURIComponent(paycodeStr)))

		$scope.isCommit = false;

		$scope.isDrug = true

        angular.forEach($scope.drugArry, function(data,index,array){

			if(0 == data.drugno.indexOf("F") || 0 == data.drugno.indexOf("S"){
				$scope.isDrug = false
			}

            $scope.price  = $scope.price  + data.price*data.number;

        });

        $scope.goBack = function(){

            $window.history.back()

        };

		$scope.print = function(){
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
				alert("打印配置异常");
			}
		}

        $scope.commit = function(){
		
			$rootScope.myloader = true;
			
			$http({
				method: 'post',

				url: URL+'prescription/open',

				requestType: 'json',

				data: {
					prescriptionno: $scope.userArry.prescriptionno,
					doctorname: $scope.userArry.doctorname,
					department: $scope.userArry.department,
					diagnosis: $scope.userArry.diagnosis,
					patientname: $scope.userArry.patientname,
					patientage: $scope.userArry.patientage,
					patientsex: $scope.userArry.patientsex,
					patientphone: $scope.userArry.patientphone,
					patientBirthday: $scope.userArry.patientBirthday,
					regNo: $scope.userArry.regNo,
					cardNo: $scope.userArry.cardNo,
					type: 1,//西药
					zyusage: "",
					zynum: 1,
					zysingledoes: "",
					zyfrequence: "",
					zymode: "",
					drugs: $scope.drugArry
				},
				headers: {
					'Content-Type': 'application/json'
				}
			})
			.success(function(data){
				
				$rootScope.myloader = false;

				if (data.code == 1){
			
					$scope.isCommit = true;

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
						alert("打印服务异常");
					}
					
				}else{
				
					alert(data.message);

					console.log(data)
				}
			
			})
			.error(function(data){
			
				$rootScope.myloader = false;
			
				alert('系统服务异常，请联系管理员');
			
			});
		
           
		}
		
		  /**
		 * 编码 base64 -> URL Safe base64
		 * description: base64
		 * '+' -> '-'
		 * '/' -> '_'
		 * '=' -> ''
		 * param {type} string
		 * return: URL Safe base64 string;
		 */
		function urlSateBase64Encode(base64Str) {
			if (!base64Str) 
				return;
			let safeStr = base64Str.replace(/\+/g, '-').replace(/\//g, '_'); //.replace(/\=/g, '’');不替换=
			return safeStr;
		}
    }];

});

/**
 * 建立angular.module
 */


define(['angular','ng-route','ng-cookies','io-barcode','angular-qrcode'], function (angular) {

    var app = angular.module('fnApp', ['ui.router','ngCookies','Encrypt','monospaced.qrcode','io-barcode'])

    .run(function($rootScope,$location,$window,$state,$http) {

        $rootScope.cssVer = '1.0.01';

    });

    return app;

});
